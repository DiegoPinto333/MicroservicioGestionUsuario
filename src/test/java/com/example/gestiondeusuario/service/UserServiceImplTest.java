package com.example.gestiondeusuario.service;

import com.example.gestiondeusuario.dto.UserRequestDTO;
import com.example.gestiondeusuario.dto.UserResponseDTO;
import com.example.gestiondeusuario.model.User;
import com.example.gestiondeusuario.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    private User user;
    private UserRequestDTO userRequestDTO;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setName("Test User");
        user.setEmail("test@example.com");
        user.setPassword("password");

        userRequestDTO = new UserRequestDTO();
        userRequestDTO.setName("Test User");
        userRequestDTO.setEmail("test@example.com");
        userRequestDTO.setPassword("password");
    }

    @Test
    void findAllUsers() {
        when(userRepository.findAll()).thenReturn(Collections.singletonList(user));

        List<UserResponseDTO> users = userService.findAllUsers();

        assertNotNull(users);
        assertEquals(1, users.size());
        assertEquals(user.getName(), users.get(0).getName());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void findUserById_whenUserExists() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        Optional<UserResponseDTO> foundUser = userService.findUserById(1L);

        assertTrue(foundUser.isPresent());
        assertEquals(user.getId(), foundUser.get().getId());
        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    void findUserById_whenUserDoesNotExist() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<UserResponseDTO> foundUser = userService.findUserById(1L);

        assertFalse(foundUser.isPresent());
        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    void saveUser() {
        when(userRepository.save(any(User.class))).thenReturn(user);

        UserResponseDTO savedUser = userService.saveUser(userRequestDTO);

        assertNotNull(savedUser);
        assertEquals(user.getName(), savedUser.getName());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void updateUser_whenUserExists() {
        UserRequestDTO updatedRequest = new UserRequestDTO();
        updatedRequest.setName("Updated Name");
        updatedRequest.setEmail("updated@example.com");
        updatedRequest.setPassword("newpassword");

        User updatedUser = new User();
        updatedUser.setId(1L);
        updatedUser.setName(updatedRequest.getName());
        updatedUser.setEmail(updatedRequest.getEmail());

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(updatedUser);

        UserResponseDTO result = userService.updateUser(1L, updatedRequest);

        assertNotNull(result);
        assertEquals("Updated Name", result.getName());
        verify(userRepository, times(1)).findById(1L);
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void updateUser_whenUserDoesNotExist() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        UserResponseDTO result = userService.updateUser(1L, userRequestDTO);

        assertNull(result);
        verify(userRepository, times(1)).findById(1L);
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void deleteUser() {
        Long userId = 1L;
        // doNothing() es útil para métodos void
        doNothing().when(userRepository).deleteById(userId);

        userService.deleteUser(userId);

        verify(userRepository, times(1)).deleteById(userId);
    }
}