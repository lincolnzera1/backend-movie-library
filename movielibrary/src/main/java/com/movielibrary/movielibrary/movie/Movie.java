package com.movielibrary.movielibrary.movie;

import com.movielibrary.movielibrary.rating.Rating;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import java.util.Date;
import java.util.List;

import lombok.NoArgsConstructor;

@Table(name = "movies") // Nome da tabela no database
@Entity(name = "movies") // Nome da entidade
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Movie {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private Date releaseYear;
    private String genre;
    private Integer rating;
    private String synopsis;
    private String image;



    public Movie(MovieRequestDTO data){
        this.title = data.title();
        this.releaseYear = data.releaseYear();
        this.genre = data.genre();
        this.rating = data.rating();
        this.synopsis = data.synopsis();
        this.image = data.image();
    }

    public void updateFromDTO(MovieRequestDTO movieRequestDTO) {
        this.title = movieRequestDTO.title();
        this.releaseYear = movieRequestDTO.releaseYear();
        this.genre = movieRequestDTO.genre();
        this.rating = movieRequestDTO.rating();
        this.synopsis = movieRequestDTO.synopsis();
        this.image = movieRequestDTO.image();
    }
}