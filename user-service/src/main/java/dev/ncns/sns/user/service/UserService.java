package dev.ncns.sns.user.service;

import dev.ncns.sns.user.common.SecurityUtil;
import dev.ncns.sns.user.domain.Users;
import dev.ncns.sns.user.dto.ProfileUpdateRequestDto;
import dev.ncns.sns.user.dto.UserResponseDto;
import dev.ncns.sns.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public List<UserResponseDto> getAllUserInfo(){
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
        if(!isValidEmail) {
            throw new Exception("email already exists");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    @Transactional
    public boolean signOut() throws Exception{
        Users user = userRepository.findById(SecurityUtil.getCurrentMemberId()).orElseThrow(()->new Exception("no such user"));
        userRepository.delete(user);
        return true;

    }

    @Transactional(readOnly = true)
    public UserResponseDto getUserInfo(String account) throws Exception {
        Users user = userRepository.findByAccount(account).orElseThrow(()->new Exception("account not exists"));
        return new UserResponseDto(user);
    }

    @Transactional
    public boolean updateProfile(ProfileUpdateRequestDto dto) {
        Users user = userRepository.getById(SecurityUtil.getCurrentMemberId());
        user.updateProfile(dto.getAccount(),dto.getNickname(),dto.getIntroduce());
        return true;
    }
}
