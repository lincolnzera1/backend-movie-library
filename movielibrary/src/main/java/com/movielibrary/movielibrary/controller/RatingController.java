package com.movielibrary.movielibrary.controller;

import com.movielibrary.movielibrary.movie.Movie;
import com.movielibrary.movielibrary.movie.MovieRepository;
import com.movielibrary.movielibrary.rating.Rating;
import com.movielibrary.movielibrary.rating.RatingRepository;
import com.movielibrary.movielibrary.rating.RatingRequestDTO;
import com.movielibrary.movielibrary.rating.RatingService;
import com.movielibrary.movielibrary.user.User;
import com.movielibrary.movielibrary.user.UserRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

    @Operation(summary = "Registrar Avaliação", description = "Registra uma nova avaliação de filme.")
    @ApiResponses({
            @ApiResponse(responseCode  = "200", description = "Avaliação registrada com sucesso."),
            @ApiResponse(responseCode = "404", description = "Usuário, filme ou avaliação não encontrados."),
            @ApiResponse(responseCode = "500", description = "Erro interno no servidor.")
    })
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

    @Operation(summary = "Obter Todas as Avaliações", description = "Recupera todas as avaliações cadastradas.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Avaliações recuperadas com sucesso."),
            @ApiResponse(responseCode = "500", description = "Erro interno no servidor.")
    })
    @GetMapping
    public ResponseEntity<List<Rating>> getAllRatings() {
        List<Rating> ratings = ratingRepository.findAll();
        return ResponseEntity.ok(ratings);
    }

    @Operation(summary = "Excluir Avaliação por ID", description = "Exclui uma avaliação com o ID especificado.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Avaliação excluída com sucesso."),
            @ApiResponse(responseCode = "404", description = "Avaliação não encontrada com o ID especificado."),
            @ApiResponse(responseCode = "500", description = "Erro interno no servidor.")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteRating(@PathVariable Long id) {
        try {
            ratingRepository.deleteById(id);
            return ResponseEntity.ok("Avaliação excluída com sucesso.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao excluir a avaliação.");
        }
    }

    @Operation(summary = "Atualizar Valor do Rating por ID",
            description = "Atualiza o valor do rating de uma avaliação com o ID especificado.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Valor do rating atualizado com sucesso."),
            @ApiResponse(responseCode = "404", description = "Avaliação não encontrada com o ID especificado."),
            @ApiResponse(responseCode = "400", description = "Parâmetro 'newRatingValue' ausente no corpo da solicitação."),
            @ApiResponse(responseCode = "500", description = "Erro interno no servidor.")
    })
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

    @Operation(summary = "Obter Avaliação por ID de Usuário e ID de Filme",
            description = "Recupera a avaliação correspondente a um usuário e filme pelos IDs especificados.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Avaliação recuperada com sucesso."),
            @ApiResponse(responseCode = "404", description = "Avaliação não encontrada para os IDs de usuário e filme especificados."),
            @ApiResponse(responseCode = "500", description = "Erro interno no servidor.")
    })
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