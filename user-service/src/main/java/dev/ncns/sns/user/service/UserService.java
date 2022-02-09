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

    @Transactional
    public void signUp(Users user) {
        if (isExistEmail(user.getEmail())) {
            throw new BadRequestException(ResponseType.USER_DUPLICATED_EMAIL);
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user = userRepository.save(user);
        UserCount userCount = UserCount.builder().userId(user.getId()).build();
        userCountRepository.save(userCount);
    }

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
