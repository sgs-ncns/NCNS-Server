package dev.ncns.sns.search.service;

import dev.ncns.sns.common.domain.ResponseType;
import dev.ncns.sns.common.exception.NotFoundException;
import dev.ncns.sns.search.domain.User;
import dev.ncns.sns.search.dto.request.CreateUserRequestDto;
import dev.ncns.sns.search.dto.response.UserResponseDto;
import dev.ncns.sns.search.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;

    public UserResponseDto getUser(Long userId) {
        User user = userRepository.getByUserId(userId)
                .orElseThrow(() -> new NotFoundException(ResponseType.USER_NOT_EXIST_ID));
        return UserResponseDto.of(user);
    }

    public void createUser(CreateUserRequestDto createUserRequest) {
        User user = userRepository.findByUserId(createUserRequest.getUserId());
        if (user == null) {
            userRepository.save(createUserRequest.toEntity());
        } else {
            // TODO: update
        }
    }

    public List<UserResponseDto> findUsersByAccountName(String accountName) {
        List<User> users = userRepository.findByAccountNameContains(accountName);
        return users.stream()
                .map(UserResponseDto::of)
                .collect(Collectors.toList());
    }

    public List<UserResponseDto> findUsersByNickname(String nickname) {
        List<User> users = userRepository.findByNicknameContains(nickname);
        return users.stream()
                .map(UserResponseDto::of)
                .collect(Collectors.toList());
    }

}
