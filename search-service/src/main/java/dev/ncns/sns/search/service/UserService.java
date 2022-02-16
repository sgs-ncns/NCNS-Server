package dev.ncns.sns.search.service;

import dev.ncns.sns.common.domain.ResponseType;
import dev.ncns.sns.common.exception.NotFoundException;
import dev.ncns.sns.search.domain.User;
import dev.ncns.sns.search.dto.request.CreateUserRequestDto;
import dev.ncns.sns.search.dto.request.UpdateUserRequestDto;
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
                .orElseThrow(() -> new NotFoundException(ResponseType.SEARCH_NOT_EXIST_USER));
        return UserResponseDto.of(user);
    }

    public void createUser(CreateUserRequestDto createUserRequest) {
        User user = userRepository.findByUserId(createUserRequest.getUserId());
        if (user == null) {
            userRepository.save(createUserRequest.toEntity());
        } else {
            userRepository.update(user, createUserRequest.getAccountName(), createUserRequest.getNickname());
        }
    }

    public void updateUser(UpdateUserRequestDto updateUserRequest) {
        User user = userRepository.getByUserId(updateUserRequest.getUserId())
                .orElseThrow(() -> new NotFoundException(ResponseType.SEARCH_NOT_EXIST_USER));
        userRepository.update(user, updateUserRequest.getAccountName(), updateUserRequest.getNickname());
    }

    public void deleteUser(Long userId) {
        User user = userRepository.getByUserId(userId)
                .orElseThrow(() -> new NotFoundException(ResponseType.SEARCH_NOT_EXIST_USER));
        userRepository.delete(user);
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
