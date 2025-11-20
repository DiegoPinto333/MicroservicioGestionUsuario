package com.example.gestiondeusuario.service;

import com.example.gestiondeusuario.dto.UserRequestDTO;
import com.example.gestiondeusuario.dto.UserResponseDTO;

import java.util.List;
import java.util.Optional;

public interface UserService {
    List<UserResponseDTO> findAllUsers();
    Optional<UserResponseDTO> findUserById(Long id);
    UserResponseDTO saveUser(UserRequestDTO userRequestDTO);
    UserResponseDTO updateUser(Long id, UserRequestDTO userRequestDTO);
    void deleteUser(Long id);
}