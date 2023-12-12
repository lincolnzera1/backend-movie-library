package com.movielibrary.movielibrary.controller;

import com.movielibrary.movielibrary.movie.Movie;
import com.movielibrary.movielibrary.movie.MovieRepository;
import com.movielibrary.movielibrary.movie.MovieRequestDTO;
import com.movielibrary.movielibrary.movie.MovieResponseDTO;
import com.movielibrary.movielibrary.user.User;
import com.movielibrary.movielibrary.user.UserRepository;
import com.movielibrary.movielibrary.user.UserRequestDTO;
import com.movielibrary.movielibrary.user.UserResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.server.ResponseStatusException;


import java.net.Authenticator;
import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("user")
public class UserController {

    @Autowired
    private UserRepository repository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // Endpoint para obter todos os usuários
    @Operation(summary = "Obter todos os usuários", description = "Este endpoint retorna uma lista de todos os usuários cadastrados.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de usuários obtida com sucesso.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserResponseDTO.class))),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor.") // Adicione mais respostas conforme necessário
    })
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @GetMapping
    public List<UserResponseDTO> getAll(){
        try {
            // Obtém todos os usuários da tabela "users" usando o repository
            List<UserResponseDTO> userList = repository.findAll().stream().map(UserResponseDTO::new).toList();
            return userList;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Erro ao obter a lista de usuários.", e);
        }
    }

    // Endpoint para salvar um novo usuário
    @Operation(summary = "Criar um novo usuário", description = "Este endpoint cria um novo usuário no sistema.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Usuário criado com sucesso.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserResponseDTO.class))),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor ao criar o usuário.") // Adicione mais respostas conforme necessário
    })
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @PostMapping
    public ResponseEntity<Object> createUser(@RequestBody UserRequestDTO data){
        try {
            // Criptografa a senha antes de salvar no banco
            String encryptedPassword = passwordEncoder.encode(data.password());

            // Cria um novo usuário com a senha criptografada
            User userData = new User(data);
            userData.setPassword(encryptedPassword);


            // Salva o usuário no banco de dados usando o repository
            repository.save(userData);

            // Retorna a resposta com o código 201 e o local do novo recurso criado
            URI location = new URI("/user/" + userData.getId());
            return ResponseEntity.created(location).build();

        } catch (Exception e) {
            // Em caso de erro, retorna uma resposta com código 500
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Endpoint para realizar o login do usuário
    @Operation(summary = "Autenticar usuário", description = "Este endpoint autentica o usuário no sistema.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Autenticação bem-sucedida."),
            @ApiResponse(responseCode = "401", description = "Credenciais inválidas."),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor ao autenticar o usuário.") // Adicione mais respostas conforme necessário
    })
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @PostMapping("/login")
    public ResponseEntity<Object> loginUser(@RequestBody UserRequestDTO loginData){
        try {
            // Encontrar o usuário pelo nome de usuário
            Optional<User> user = repository.findByUsername(loginData.username());

            if (user.isPresent()) {
                // Verificar se a senha fornecida coincide com a senha armazenada (usando o PasswordEncoder)
                if (passwordEncoder.matches(loginData.password(), user.get().getPassword())) {
                    // Autenticação bem-sucedida
                    return ResponseEntity.ok().build();
                } else {
                    // Senha incorreta
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build(); // Resposta 401 Unauthorized
                }
            } else {
                // Usuário não encontrado
                System.out.println("Usuario não encontrado!!");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build(); // Resposta 401 Unauthorized
            }
        } catch (Exception e) {
            // Em caso de erro, retorna uma resposta com código 500
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Endpoint para deletar um usuário por ID
    @Operation(summary = "Deletar usuário", description = "Este endpoint exclui um usuário com base no ID fornecido.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Usuário deletado com sucesso."),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado para o ID fornecido."),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor ao excluir o usuário.") // Adicione mais respostas conforme necessário
    })
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteUser(@PathVariable("id") Long id){
        try {
            // Verifica se o usuário existe no banco de dados
            if (repository.existsById(id)) {
                repository.deleteById(id);
                return ResponseEntity.noContent().build(); // Resposta 204 No Content
            } else {
                return ResponseEntity.notFound().build(); // Resposta 404 Not Found
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
