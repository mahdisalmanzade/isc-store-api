package com.isc.store.services;

import com.isc.store.dtos.ChangePasswordRequest;
import com.isc.store.dtos.RegisterUserRequest;
import com.isc.store.dtos.UpdateUserRequest;
import com.isc.store.dtos.UserDto;
import com.isc.store.entities.User;
import com.isc.store.enums.Role;
import com.isc.store.exceptions.DuplicateUserException;
import com.isc.store.exceptions.UserNotFoundException;
import com.isc.store.mappers.UserMapper;
import com.isc.store.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    private User user;
    private UserDto userDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        user = User.builder()
                .id(1L)
                .name("John")
                .email("test@example.com")
                .password("password")
                .role(Role.USER)
                .build();

        userDto = new UserDto(1L, "John", "test@example.com");
    }

    @Test
    void testGetAllUsers() {
        when(userRepository.findAll(any(Sort.class))).thenReturn(List.of(user));
        when(userMapper.mapToUserDto(user)).thenReturn(userDto);

        var result = userService.getAllUsers("name");

        assertEquals(1, ((List<?>) result).size());
        verify(userRepository, times(1)).findAll(any(Sort.class));
        verify(userMapper, times(1)).mapToUserDto(user);
    }

    @Test
    void testGetUserFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userMapper.mapToUserDto(user)).thenReturn(userDto);

        var result = userService.getUser(1L);

        assertEquals("John", result.getName());
        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    void testGetUserNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.getUser(1L));
    }

    // ================== registerUser ==================
    @Test
    void testRegisterUserSuccess() {
        RegisterUserRequest request = new RegisterUserRequest();
        request.setEmail("new@example.com");
        request.setPassword("password");
        request.setName("Alice");

        when(userRepository.existsByEmail("new@example.com")).thenReturn(false);
        when(userMapper.mapUserDtoToEntity(request)).thenReturn(user);
        when(passwordEncoder.encode("password")).thenReturn("encodedPassword");
        when(userMapper.mapToUserDto(user)).thenReturn(userDto);

        var result = userService.registerUser(request);

        assertEquals("John", result.getName());
        verify(userRepository).save(user);
    }

    @Test
    void testRegisterUserDuplicate() {
        RegisterUserRequest request = new RegisterUserRequest();
        request.setEmail("test@example.com");

        when(userRepository.existsByEmail("test@example.com")).thenReturn(true);

        assertThrows(DuplicateUserException.class, () -> userService.registerUser(request));
    }

    // ================== changePassword ==================
    @Test
    void testChangePasswordSuccess() {
        ChangePasswordRequest request = new ChangePasswordRequest();
        request.setOldPassword("password");
        request.setNewPassword("newPassword");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("password", "password")).thenReturn(true);

        userService.changePassword(1L, request);

        assertEquals("newPassword", user.getPassword());
        verify(userRepository).save(user);
    }

    @Test
    void testChangePasswordWrongOldPassword() {
        ChangePasswordRequest request = new ChangePasswordRequest();
        request.setOldPassword("wrong");
        request.setNewPassword("newPassword");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("wrong", "password")).thenReturn(false);

        assertThrows(AccessDeniedException.class, () -> userService.changePassword(1L, request));
    }

    // ================== deleteUser ==================
    @Test
    void testDeleteUserSuccess() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        userService.deleteUser(1L);

        verify(userRepository).delete(user);
    }

    @Test
    void testDeleteUserNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.deleteUser(1L));
    }
}
