package com.movielibrary.movielibrary.controller;

import com.movielibrary.movielibrary.user.User;
import com.movielibrary.movielibrary.user.UserRepository;
import com.movielibrary.movielibrary.user.UserRequestDTO;
import com.movielibrary.movielibrary.user.UserResponseDTO;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class UserControllerTest {

    @InjectMocks
    private UserController userController;

    @Mock
    private UserRepository userRepository;

    @Test
    public void testGetAllUsers() {
        // Criar alguns usuários fictícios para simular o retorno do repository
        List<User> mockUsers = Arrays.asList(
                new User(1L, "user1", "email1@example.com", "password1"),
                new User(2L, "user2", "email2@example.com", "password2")
        );

        // Configurar o comportamento simulado do repository
        when(userRepository.findAll()).thenReturn(mockUsers);

        // Chamar o método do controller
        List<UserResponseDTO> result = userController.getAll();

        // Verificar se o método do repository foi chamado
        verify(userRepository, times(1)).findAll();

        // Verificar se o resultado do método do controller corresponde ao esperado
        assertEquals(2, result.size());
        assertEquals("user1", result.get(0).username());
        assertEquals("email1@example.com", result.get(0).email());
        assertEquals("user2", result.get(1).username());
        assertEquals("email2@example.com", result.get(1).email());
    }

    @Mock
    private PasswordEncoder passwordEncoder;
    @Test
    public void testCreateUser() {
        // Dados de entrada para o novo usuário
        UserRequestDTO requestData = new UserRequestDTO("testuser", "testuser@example.com", "password123");

        // Senha criptografada simulada
        String encryptedPassword = "encryptedPassword123";

        // Configurar o comportamento simulado do passwordEncoder
        when(passwordEncoder.encode("password123")).thenReturn(encryptedPassword);

        // Chamar o método do controller
        ResponseEntity<Object> response = userController.createUser(requestData);

        // Verificar se o método do passwordEncoder foi chamado
        verify(passwordEncoder, times(1)).encode("password123");

        // Verificar se o método do repository foi chamado
        verify(userRepository, times(1)).save(any(User.class));

        // Verificar se a resposta é 201 CREATED
        assertEquals(201, response.getStatusCodeValue());
    }

    ///// LOGIN USER

    @Test
    public void testLoginUserSuccess() {
        // Dados de entrada para o login
        UserRequestDTO loginData = new UserRequestDTO("testuser", "testuser@gmail.com","123456789");

        // Usuário simulado no banco de dados
        User user = new User();
        user.setUsername("testuser");
        user.setPassword(passwordEncoder.encode("123456789"));

        // Configurar o comportamento simulado do repository
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));

        // Configurar o comportamento simulado do passwordEncoder
        when(passwordEncoder.matches("123456789", user.getPassword())).thenReturn(true);

        // Chamar o método do controller
        ResponseEntity<Object> response = userController.loginUser(loginData);

        // Verificar se a resposta é 200 OK
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    public void testLoginUserInvalidCredentials() {
        // Dados de entrada para o login
        UserRequestDTO loginData = new UserRequestDTO("testuser", "testuser@gmail.com","wrongpassword");


        // Usuário simulado no banco de dados
        User user = new User();
        user.setUsername("testuser");
        user.setPassword(passwordEncoder.encode("password123"));

        // Configurar o comportamento simulado do repository
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));

        // Configurar o comportamento simulado do passwordEncoder
        when(passwordEncoder.matches("wrongpassword", user.getPassword())).thenReturn(false);

        // Chamar o método do controller
        ResponseEntity<Object> response = userController.loginUser(loginData);

        // Verificar se a resposta é 401 Unauthorized
        assertEquals(401, response.getStatusCodeValue());
    }

    @Test
    public void testLoginUserUserNotFound() {
        // Dados de entrada para o login
        UserRequestDTO loginData = new UserRequestDTO("nonexistentuser", "testuser@gmail.com","password123");

        // Configurar o comportamento simulado do repository
        when(userRepository.findByUsername("nonexistentuser")).thenReturn(Optional.empty());

        // Chamar o método do controller
        ResponseEntity<Object> response = userController.loginUser(loginData);

        // Verificar se a resposta é 401 Unauthorized
        assertEquals(401, response.getStatusCodeValue());
    }



    ///// DELETE USER

    @Test
    public void testDeleteUserSuccess() {
        // ID simulado para exclusão
        Long userId = 1L;

        // Configurar o comportamento simulado do repository
        when(userRepository.existsById(userId)).thenReturn(true);

        // Chamar o método do controller
        ResponseEntity<Object> response = userController.deleteUser(userId);

        // Verificar se a resposta é 204 No Content
        assertEquals(204, response.getStatusCodeValue());
    }

    @Test
    public void testDeleteUserNotFound() {
        // ID simulado para exclusão
        Long userId = 1L;

        // Configurar o comportamento simulado do repository
        when(userRepository.existsById(userId)).thenReturn(false);

        // Chamar o método do controller
        ResponseEntity<Object> response = userController.deleteUser(userId);

        // Verificar se a resposta é 404 Not Found
        assertEquals(404, response.getStatusCodeValue());
    }
}
