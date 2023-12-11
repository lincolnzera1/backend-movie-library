package com.movielibrary.movielibrary.controller;

import com.movielibrary.movielibrary.movie.Movie;
import com.movielibrary.movielibrary.movie.MovieRepository;
import com.movielibrary.movielibrary.movie.MovieRequestDTO;
import com.movielibrary.movielibrary.movie.MovieResponseDTO;
import com.movielibrary.movielibrary.user.User;
import com.movielibrary.movielibrary.user.UserRepository;
import com.movielibrary.movielibrary.user.UserRequestDTO;
import com.movielibrary.movielibrary.user.UserResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;



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
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @GetMapping
    public List<UserResponseDTO> getAll(){
        // Obtém todos os usuários da tabela "users" usando o repository
        List<UserResponseDTO> userList = repository.findAll().stream().map(UserResponseDTO::new).toList();
        return userList;
    }

    // Endpoint para salvar um novo usuário
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @PostMapping
    public ResponseEntity<Object> createUser(@RequestBody UserRequestDTO data){
        try {
            String encryptedPassword = passwordEncoder.encode(data.password());

            // Cria um novo usuário com a senha criptografada
            User userData = new User(data);
            userData.setPassword(encryptedPassword);


            // Salva o usuário no banco de dados usando o repository
            repository.save(userData);

            URI location = new URI("/user/" + userData.getId());
            return ResponseEntity.created(location).build();

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Endpoint para realizar o login do usuário
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
                    return ResponseEntity.ok().build(); // Você pode retornar mais informações se necessário
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
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Endpoint para atualizar os dados de um usuário por ID
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @PutMapping("/{id}")
    public ResponseEntity<Object> updateUser(@PathVariable Long id, @RequestBody UserRequestDTO newData){
        try {
            // Busca o usuário pelo ID
            Optional<User> userToUpdate = repository.findById(id);


            if (userToUpdate.isPresent()) {
                User existingUser = userToUpdate.get();
                // Atualiza os dados do usuário com os novos dados
                existingUser.updateFromDTO(newData);

                // Verifica se a senha foi fornecida na solicitação
                if (newData.password() != null && !newData.password().isEmpty()) {
                    // Criptografa a nova senha e atualiza no usuário existente
                    String encryptedPassword = passwordEncoder.encode(newData.password());
                    existingUser.setPassword(encryptedPassword);
                }

                repository.save(existingUser);
                return ResponseEntity.ok().build(); // Resposta 200 OK
            } else {
                return ResponseEntity.notFound().build(); // Resposta 404 Not Found
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Endpoint para deletar um usuário por ID
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteMovie(@PathVariable("id") Long id){
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
