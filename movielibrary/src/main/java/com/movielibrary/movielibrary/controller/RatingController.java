package com.movielibrary.movielibrary.controller;

import com.movielibrary.movielibrary.movie.Movie;
import com.movielibrary.movielibrary.movie.MovieRepository;
import com.movielibrary.movielibrary.rating.Rating;
import com.movielibrary.movielibrary.rating.RatingRepository;
import com.movielibrary.movielibrary.rating.RatingRequestDTO;
import com.movielibrary.movielibrary.rating.RatingService;
import com.movielibrary.movielibrary.user.User;
import com.movielibrary.movielibrary.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/ratings")
public class RatingController {

    @Autowired
    private RatingRepository ratingRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private RatingService ratingService;

    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @PostMapping
    public ResponseEntity<String> rateMovie(@RequestBody RatingRequestDTO data) {
        try {
            Optional<User> userFind = userRepository.findById(data.user().getId().longValue());
            Optional<Movie> movieFind = movieRepository.findById(data.movie().getId().longValue());

            User user = new User(userFind.get().getId(),userFind.get().getUsername(), userFind.get().getEmail(), userFind.get().getPassword());
            Movie movie = new Movie(movieFind.get().getId(), movieFind.get().getTitle(), movieFind.get().getReleaseYear(), movieFind.get().getGenre(), movieFind.get().getRating(), movieFind.get().getSynopsis(), movieFind.get().getImage());
            Rating ratingData = new Rating(data);
            ratingData.setUser(user);
            ratingData.setMovie(movie);
            userRepository.save(user);
            movieRepository.save(movie);
            ratingRepository.save(ratingData);
            return ResponseEntity.ok("Avaliação registrada com sucesso.");
        } catch ( Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuário, filme ou avaliação não encontrados.");
        }
    }

    @GetMapping
    public ResponseEntity<List<Rating>> getAllRatings() {
        List<Rating> ratings = ratingRepository.findAll();
        return ResponseEntity.ok(ratings);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteRating(@PathVariable Long id) {
        try {
            ratingRepository.deleteById(id);
            return ResponseEntity.ok("Avaliação excluída com sucesso.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao excluir a avaliação.");
        }
    }

    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @PatchMapping("/{id}")
    public ResponseEntity<String> updateRatingValue(@PathVariable Long id, @RequestBody Map<String, Integer> requestBody) {
        try {
            Integer newRatingValue = requestBody.get("newRatingValue");;
            if (newRatingValue != null) {
                Optional<Rating> existingRating = ratingRepository.findById(id);
                if (existingRating.isPresent()) {
                    Rating ratingToUpdate = existingRating.get();
                    ratingToUpdate.setRatingValue(newRatingValue);
                    ratingRepository.save(ratingToUpdate);
                    return ResponseEntity.ok("Valor do rating atualizado com sucesso.");
                } else {
                    return ResponseEntity.notFound().build();
                }
            } else {
                return ResponseEntity.badRequest().body("Parâmetro 'newRatingValue' ausente no corpo da solicitação.");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao atualizar o valor do rating.");
        }
    }
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @GetMapping("/user/{userId}/movie/{movieId}")
    public ResponseEntity<Rating> getRatingByUserIdAndMovieId(@PathVariable Long userId, @PathVariable Long movieId) {
        try {
            Optional<Rating> rating = ratingService.getRatingByUserIdAndMovieId(userId, movieId);
            return rating.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}