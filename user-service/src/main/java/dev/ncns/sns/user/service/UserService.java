package dev.ncns.sns.user.service;

import dev.ncns.sns.user.common.SecurityUtil;
import dev.ncns.sns.user.domain.Users;
import dev.ncns.sns.user.dto.ProfileUpdateRequestDto;
import dev.ncns.sns.user.dto.UserResponseDto;
import dev.ncns.sns.user.dto.UserSummaryResponseDto;
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
    private final PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public List<UserResponseDto> getAllUserInfo() {
        return userRepository.findAll().stream()
                .map(UserResponseDto::new)
                .collect(Collectors.toList());
    }

    // TODO: orElse(null) -> custom exception
    @Transactional(readOnly = true)
    public Users getUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    @Transactional(readOnly = true)
    public Users getUserByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }

    @Transactional
    public void signUp(Users user) throws Exception {
        final boolean isValidEmail = !userRepository.existsByEmail(user.getEmail());
        if (!isValidEmail) {
            throw new Exception("email already exists");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    @Transactional
    public void signOut() throws Exception {
        Users user = userRepository.findById(SecurityUtil.getCurrentMemberId()).orElseThrow(() -> new Exception("no such user"));
        userRepository.delete(user);
    }

    @Transactional(readOnly = true)
    public UserResponseDto getUserInfo(Long id) throws Exception {
        Users user = userRepository.findById(id).orElseThrow(() -> new Exception("account not exists"));
        return new UserResponseDto(user);
    }

    @Transactional
    public void updateProfile(ProfileUpdateRequestDto dto) {
        Users user = userRepository.getById(SecurityUtil.getCurrentMemberId());
        user.updateProfile(dto.getAccountName(), dto.getNickname(), dto.getIntroduce());
    }

    public List<UserSummaryResponseDto> getFollowingList(List<Long> followingIdList) {
        return followingIdList.stream()
                .map(id -> new UserSummaryResponseDto(userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("no such user"))))
                .collect(Collectors.toList());
    }

    public List<UserSummaryResponseDto> getFollowerList(List<Long> followerIdList) {
        return followerIdList.stream()
                .map(id -> new UserSummaryResponseDto(userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("no such user"))))
                .collect(Collectors.toList());
    }
}
