package com.movielibrary.movielibrary.user;

import com.movielibrary.movielibrary.movie.MovieRequestDTO;
import jakarta.persistence.*;
import lombok.*;

@Table(name = "users") // Nome da tabela no database
@Entity(name = "users") // Nome da entidade
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class User {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String email;
    private String password;
    private String role;


    public User(UserRequestDTO data){
        this.username = data.username();
        this.email = data.email();
        this.password = data.password();
    }

    public void updateFromDTO(UserRequestDTO userRequestDTO) {
        this.username = userRequestDTO.username();
        this.email = userRequestDTO.email();
        this.password = userRequestDTO.password();
    }
}
