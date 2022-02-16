package dev.ncns.sns.user.service;

import dev.ncns.sns.common.domain.ResponseType;
import dev.ncns.sns.common.exception.BadRequestException;
import dev.ncns.sns.common.exception.NotFoundException;
import dev.ncns.sns.user.domain.AuthType;
import dev.ncns.sns.user.domain.CountType;
import dev.ncns.sns.user.domain.User;
import dev.ncns.sns.user.domain.UserCount;
import dev.ncns.sns.user.dto.request.*;
import dev.ncns.sns.user.dto.response.CheckResponseDto;
import dev.ncns.sns.user.dto.response.LoginResponseDto;
import dev.ncns.sns.user.dto.response.UserResponseDto;
import dev.ncns.sns.user.dto.response.UserSummaryResponseDto;
import dev.ncns.sns.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;

    private final UserCountService userCountService;
    private final PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public UserResponseDto getUserInfo(Long userId) {
        User user = getUserById(userId);
        UserCount userCount = userCountService.getUserCount(userId);
        return UserResponseDto.of(user, userCount);
    }

    @Transactional
    public Long signUp(SignUpRequestDto signUpRequest) {
        User user = signUpRequest.toEntity();
        if (isExistEmail(user.getEmail())) {
            throw new BadRequestException(ResponseType.USER_DUPLICATED_EMAIL);
        }
        if (isExistAccountName(user.getAccountName())) {
            throw new BadRequestException(ResponseType.USER_DUPLICATED_ACCOUNT_NAME);
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user = userRepository.save(user);
        userCountService.createUserCount(user.getId());
        return user.getId();
    }

    /**
     * 회원 탈퇴와 회원 정보 업데이트는 요청을 보낸 인증 정보를 기반으로 이루어집니다.
     */
    @Transactional
    public void signOut(Long userId) {
        userCountService.deleteUserCount(userId);
        userRepository.delete(getUserById(userId));
    }

    @Transactional
    public void updateProfile(Long userId, UpdateProfileRequestDto profileRequest) {
        User user = getUserById(userId);
        if (!user.getAccountName().equals(profileRequest.getAccountName()) && isExistAccountName(profileRequest.getAccountName())) {
            throw new BadRequestException(ResponseType.USER_DUPLICATED_ACCOUNT_NAME);
        }
        user.updateProfile(profileRequest.getAccountName(), profileRequest.getNickname(), profileRequest.getIntroduce());
    }

    public CheckResponseDto isDuplicateEmail(CheckEmailRequestDto checkEmailRequest) {
        boolean result = isExistEmail(checkEmailRequest.getEmail());
        return CheckResponseDto.of(result);
    }

    public CheckResponseDto isDuplicateAccountName(CheckAccountRequestDto checkAccountRequest) {
        boolean result = isExistAccountName(checkAccountRequest.getAccountName());
        return CheckResponseDto.of(result);
    }

    /**
     * Auth 서버에서 로그인 요청 정보를 받아 검증합니다.
     * 소셜로 회원가입한 사용자는 소셜 로그인/이메일 로그인/계정 로그인을 사용 할 수 있습니다.
     * 자체 회원가입을 이용한 사용자는 이메일 로그인/계정 로그인만 사용할 수 있습니다.
     * <p>
     * 소셜 로그인(Google, Apple) 을 요청한 경우 가입 여부와 소셜로 가입한 사용자인지를 검증합니다.
     * 이메일/계정 로그인 요청은 가입 여부와 비밀번호 일치 여부를 검증합니다.
     */
    public LoginResponseDto handleLoginRequest(LoginRequestDto loginRequest) {
        AuthType authType = loginRequest.getAuthType();
        switch (authType) {
            case GOOGLE:
            case APPLE:
                return socialLogin(loginRequest.getEmail(), authType);
            case LOCAL:
                return localLogin(loginRequest.getEmail(), loginRequest.getPassword());
            default:
                return accountLogin(loginRequest.getAccountName(), loginRequest.getPassword());
        }
    }

    /**
     * Post 서버에서 요청한 post count 정보를 업데이트합니다.
     * post count는 마이너스가 될 수 없습니다.
     */
    @Transactional
    public void updatePostCount(UpdateUserPostCountDto dto) {
        userCountService.updateUserCount(dto.getUserId(), CountType.POST, dto.getIsUp());
    }

    public void checkExistUser(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException(ResponseType.USER_NOT_EXIST_ID);
        }
    }

    public List<UserSummaryResponseDto> getUserSummaryList(List<Long> userIdList) {
        return userIdList.stream()
                .map(id -> new UserSummaryResponseDto(getUserById(id)))
                .collect(Collectors.toList());
    }

    @Transactional
    public void updateUserAccessAt(Long userId) {
        getUserById(userId).updateAccessAt();
    }

    private LoginResponseDto socialLogin(String email, AuthType authType) {
        User user = getUserByEmail(email);
        checkAuthTypeMatch(user.getAuthType(), authType);
        return LoginResponseDto.of(user.getId(), user.getAccountName());
    }

    private LoginResponseDto localLogin(String email, String password) {
        User user = getUserByEmail(email);
        checkAuthTypeMatch(user.getAuthType(), AuthType.LOCAL);
        checkPasswordMatch(password, user.getPassword());
        return LoginResponseDto.of(user.getId(), user.getAccountName());
    }

    private LoginResponseDto accountLogin(String accountName, String password) {
        User user = getUserByAccountName(accountName);
        checkPasswordMatch(password, user.getPassword());
        return LoginResponseDto.of(user.getId(), user.getAccountName());
    }

    private User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(ResponseType.USER_NOT_EXIST_ID));
    }

    private User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException(ResponseType.USER_NOT_EXIST_EMAIL));
    }

    private User getUserByAccountName(String accountName) {
        return userRepository.findByAccountName(accountName)
                .orElseThrow(() -> new NotFoundException(ResponseType.USER_NOT_EXIST_ACCOUNT_NAME));
    }

    private boolean isExistEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    private boolean isExistAccountName(String accountName) {
        return userRepository.existsByAccountName(accountName);
    }

    private void checkPasswordMatch(String target, String password) {
        if (!passwordEncoder.matches(target, password)) {
            throw new BadRequestException(ResponseType.USER_NOT_MATCH_PASSWORD);
        }
    }

    private void checkAuthTypeMatch(AuthType target, AuthType authType) {
        if (!authType.equals(target)) {
            throw new BadRequestException(ResponseType.USER_NOT_MATCH_AUTH_TYPE);
        }
    }

}
