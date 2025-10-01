package com.isc.store.services;

import com.isc.store.dtos.ChangePasswordRequest;
import com.isc.store.dtos.RegisterUserRequest;
import com.isc.store.dtos.UpdateUserRequest;
import com.isc.store.dtos.UserDto;
import com.isc.store.enums.Role;
import com.isc.store.exceptions.DuplicateUserException;
import com.isc.store.exceptions.UserNotFoundException;
import com.isc.store.mappers.UserMapper;
import com.isc.store.repositories.UserRepository;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, UserMapper userMapper, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
    }

    public Iterable<UserDto> getAllUsers
            (String sortBy) {
        if(!Set.of("name", "email").contains(sortBy)) sortBy = "name";
        return userRepository.findAll(Sort.by(sortBy)).stream()
                .map(user -> userMapper.mapToUserDto(user))
                .collect(Collectors.toList());
    }

    public UserDto getUser(@PathVariable Long id) {
        var user = userRepository.findById(id).orElseThrow(UserNotFoundException::new);
        return userMapper.mapToUserDto(user);
    }

    public UserDto registerUser(RegisterUserRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateUserException();
        }

        var user = userMapper.mapUserDtoToEntity(request);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        System.out.println("Encoded Password is: " + passwordEncoder.encode(user.getPassword()));
        user.setRole(Role.USER);
        userRepository.save(user);

        return userMapper.mapToUserDto(user);
    }

    public UserDto updateUser(Long userId, UpdateUserRequest request) {
        var user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        userMapper.update(request, user);
        userRepository.save(user);

        return userMapper.mapToUserDto(user);
    }

    public void deleteUser(Long userId) {
        var user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        System.out.println(user);
        userRepository.delete(user);
    }

    public void changePassword(Long userId, ChangePasswordRequest request) {
        var user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);

        if (!passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
            throw new AccessDeniedException("Password does not match");
        }

        user.setPassword(request.getNewPassword());
        userRepository.save(user);
    }
}
