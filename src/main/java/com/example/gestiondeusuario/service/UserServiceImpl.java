package com.example.gestiondeusuario.service;

import com.example.gestiondeusuario.dto.UserRequestDTO;
import com.example.gestiondeusuario.dto.UserResponseDTO;
import com.example.gestiondeusuario.model.User;
import com.example.gestiondeusuario.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public List<UserResponseDTO> findAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<UserResponseDTO> findUserById(Long id) {
        return userRepository.findById(id).map(this::convertToResponseDTO);
    }

    @Override
    public UserResponseDTO saveUser(UserRequestDTO userRequestDTO) {
        User user = new User();
        user.setName(userRequestDTO.getName());
        user.setEmail(userRequestDTO.getEmail());
        user.setPassword(userRequestDTO.getPassword()); // Idealmente, aquí se encriptaría la contraseña
        User savedUser = userRepository.save(user);
        return convertToResponseDTO(savedUser);
    }

    @Override
    public UserResponseDTO updateUser(Long id, UserRequestDTO userRequestDTO) {
        return userRepository.findById(id).map(user -> {
            user.setName(userRequestDTO.getName());
            user.setEmail(userRequestDTO.getEmail());
            // Solo actualizar la contraseña si se proporciona una nueva
            if (userRequestDTO.getPassword() != null && !userRequestDTO.getPassword().isEmpty()) {
                user.setPassword(userRequestDTO.getPassword()); // Encriptar aquí también
            }
            User updatedUser = userRepository.save(user);
            return convertToResponseDTO(updatedUser);
        }).orElse(null); // O lanzar una excepción
    }

    @Override
    public void deleteUser(Long id) {
        // Se podría verificar si existe antes de borrar para lanzar una excepción si no se encuentra
        userRepository.deleteById(id);
    }

    private UserResponseDTO convertToResponseDTO(User user) {
        UserResponseDTO dto = new UserResponseDTO();
        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());
        return dto;
    }
}