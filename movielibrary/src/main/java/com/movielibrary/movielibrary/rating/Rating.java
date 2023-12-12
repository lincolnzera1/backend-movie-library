package com.movielibrary.movielibrary.rating;

import com.movielibrary.movielibrary.movie.Movie;
import com.movielibrary.movielibrary.movie.MovieRequestDTO;
import com.movielibrary.movielibrary.user.User;
import jakarta.persistence.*;

import lombok.*;

@Entity(name = "ratings")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Rating {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "movie_id")
    private Movie movie;

    private Integer ratingValue;

    public Rating(RatingRequestDTO data){
        this.user = data.user();
        this.movie = data.movie();
        this.ratingValue = data.ratingValue();
    }

    public void updateFromDTO(RatingRequestDTO ratingRequestDTO) {
        this.user = ratingRequestDTO.user();
        this.movie = ratingRequestDTO.movie();
        this.ratingValue = ratingRequestDTO.ratingValue();
    }
}
