package dev.ncns.sns.user.service;

import dev.ncns.sns.common.domain.ResponseType;
import dev.ncns.sns.common.exception.BadRequestException;
import dev.ncns.sns.common.exception.NotFoundException;
import dev.ncns.sns.user.common.SecurityUtil;
import dev.ncns.sns.user.domain.AuthType;
import dev.ncns.sns.user.domain.CountType;
import dev.ncns.sns.user.domain.UserCount;
import dev.ncns.sns.user.domain.Users;
import dev.ncns.sns.user.dto.request.LoginRequestDto;
import dev.ncns.sns.user.dto.request.ProfileUpdateRequestDto;
import dev.ncns.sns.user.dto.request.UpdateUserPostCountDto;
import dev.ncns.sns.user.dto.response.UserResponseDto;
import dev.ncns.sns.user.dto.response.UserSummaryResponseDto;
import dev.ncns.sns.user.repository.UserCountRepository;
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
    private final UserCountRepository userCountRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public UserResponseDto getUserInfo(Long userId) {
        Users user = getUserById(userId);
        UserCount userCount = userCountRepository.findByUserId(userId);
        return UserResponseDto.of(user, userCount);
    }

    /**
     * 회원가입 요청 시 이메일/계정의 중복 여부를 체크합니다.
     * 유효한 정보라면 패스워드를 암호화 후 DB에 저장합니다.
     */
    @Transactional
    public void signUp(Users user) {
        if (isExistEmail(user.getEmail())) {
            throw new BadRequestException(ResponseType.USER_DUPLICATED_EMAIL);
        }
        if (isExistAccountName(user.getAccountName())) {
            throw new BadRequestException(ResponseType.USER_DUPLICATED_ACCOUNT_NAME);
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user = userRepository.save(user);
        UserCount userCount = UserCount.builder().userId(user.getId()).build();
        userCountRepository.save(userCount);
    }

    /**
     * 회원 탈퇴와 회원 정보 업데이트는 요청을 보낸 인증 정보를 기반으로 이루어집니다.
     */
    @Transactional
    public void signOut() {
        Users user = getUserById(SecurityUtil.getCurrentMemberId());
        userCountRepository.deleteByUserId(user.getId());
        userRepository.delete(user);
    }

    @Transactional
    public void updateProfile(ProfileUpdateRequestDto dto) {
        Users user = userRepository.getById(SecurityUtil.getCurrentMemberId());
        user.updateProfile(dto.getAccountName(), dto.getNickname(), dto.getIntroduce());
    }

    /**
     * 팔로워/팔로잉 조회는 계정명과 사용자 이름만 포함된 간략한 정보를 제공합니다.
     */
    public List<UserSummaryResponseDto> getFollowingList(List<Long> followingIdList) {
        return followingIdList.stream()
                .map(id -> new UserSummaryResponseDto(getUserById(id)))
                .collect(Collectors.toList());
    }

    public List<UserSummaryResponseDto> getFollowerList(List<Long> followerIdList) {
        return followerIdList.stream()
                .map(id -> new UserSummaryResponseDto(getUserById(id)))
                .collect(Collectors.toList());
    }

    /**
     * Auth 서버에서 로그인 요청 정보를 받아 검증합니다.
     * 소셜로 회원가입한 사용자는 소셜 로그인/이메일 로그인/계정 로그인을 사용 할 수 있습니다.
     * 자체 회원가입을 이용한 사용자는 이메일 로그인/계정 로그인만 사용할 수 있습니다.
     *
     * 소셜 로그인(Google, Apple) 을 요청한 경우 가입 여부와 소셜로 가입한 사용자인지를 검증합니다.
     * 이메일/계정 로그인 요청은 가입 여부와 비밀번호 일치 여부를 검증합니다.
     */
    public Long handleLoginRequest(LoginRequestDto dto) {
        AuthType authType = dto.getAuthType();
        switch (authType) {
            case GOOGLE:
            case APPLE:
                return socialLogin(dto.getEmail(), authType);
            case LOCAL:
                return localLogin(dto.getEmail(), dto.getPassword());
            default:
                return accountLogin(dto.getAccountName(), dto.getPassword());
        }
    }

    /**
     * Post 서버에서 요청한 post count 정보를 업데이트합니다.
     * post count는 마이너스가 될 수 없습니다.
     */
    @Transactional
    public void updatePostCount(UpdateUserPostCountDto dto) {
        UserCount userCount = userCountRepository.findByUserId(dto.getUserId());
        if (userCount.getPostCount() <= 0 && dto.getIsUp() == false) {
            throw new BadRequestException(ResponseType.REQUEST_NOT_VALID);
        }
        userCount.update(CountType.POST, dto.getIsUp());
    }

    private Long socialLogin(String email, AuthType authType) {
        Users user = getUserByEmail(email);
        checkAuthTypeMatch(user.getAuthType(), authType);
        return user.getId();
    }

    private Long localLogin(String email, String password) {
        Users user = getUserByEmail(email);
        checkAuthTypeMatch(user.getAuthType(), AuthType.LOCAL);
        checkPasswordMatch(password, user.getPassword());
        return user.getId();
    }

    private Long accountLogin(String accountName, String password) {
        Users user = getUserByAccountName(accountName);
        checkPasswordMatch(password, user.getPassword());
        return user.getId();
    }

    private Users getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(ResponseType.USER_NOT_EXIST_ID));
    }

    private Users getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException(ResponseType.USER_NOT_EXIST_EMAIL));
    }

    private Users getUserByAccountName(String accountName) {
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
        if (target == authType) {
            throw new BadRequestException(ResponseType.USER_NOT_MATCH_AUTH_TYPE);
        }
    }

}
