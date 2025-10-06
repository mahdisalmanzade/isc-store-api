package com.isc.store.services;

import com.isc.store.dtos.ChangePasswordRequest;
import com.isc.store.dtos.RegisterUserRequest;
import com.isc.store.dtos.UpdateUserRequest;
import com.isc.store.dtos.UserDto;
import com.isc.store.entities.User;
import com.isc.store.enums.Role;
import com.isc.store.exceptions.UserNotFoundException;
import com.isc.store.mappers.UserMapper;
import com.isc.store.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
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

    // First, we need to create and initialize a User and UserDto
    // This method gets executed before each test case in this class to initialize objects
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        // using builder() method to create an object in an easier way
        user = User.builder()
                .id(1L)
                .name("Mahdi")
                .email("mahdisalmanizadehgan@example.com")
                .password("mahdi1234")
                .role(Role.USER)
                .build();
        System.out.println("Name: " + user.getName() + " " + "Email: " + user.getEmail());
        userDto = new UserDto(1L, "Mahdi", "mahdisalmanizadehgan@example.com", "USER");
    }

    @Test
    void testGetAllUsers() {
        // fetch all users from the mocked userRepository
        when(userRepository.findAll(any(Sort.class))).thenReturn(List.of(user));
        when(userMapper.mapToUserDto(user)).thenReturn(userDto); // map user to userDto

        var result = userService.getAllUsers("name"); // sort

        // casting result to list and check if the size is equal to 1
        assertEquals(1, ((List<?>) result).size());
        verify(userRepository, times(1)).findAll(any(Sort.class));
        verify(userMapper, times(1)).mapToUserDto(user);
    }

    @Test
    void testGetUserFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userMapper.mapToUserDto(user)).thenReturn(userDto);

        var result = userService.getUser(1L);

        assertEquals("Mahdi", result.getName());
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

        when(userRepository.existsByEmail("mahdivoker22@example.com")).thenReturn(false);
        when(userMapper.mapUserDtoToEntity(request)).thenReturn(user);
        when(passwordEncoder.encode("password")).thenReturn("encodedPassword");
        when(userMapper.mapToUserDto(user)).thenReturn(userDto);

        var result = userService.registerUser(request);

        assertEquals("Mahdi", result.getName());
        verify(userRepository).save(user);
    }
}
