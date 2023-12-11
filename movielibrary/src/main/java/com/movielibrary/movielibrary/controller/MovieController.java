package com.movielibrary.movielibrary.controller;


import com.movielibrary.movielibrary.movie.Movie;
import com.movielibrary.movielibrary.movie.MovieRepository;
import com.movielibrary.movielibrary.movie.MovieRequestDTO;
import com.movielibrary.movielibrary.movie.MovieResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("movie")
public class MovieController {

    @Autowired
    private MovieRepository repository;

    // Endpoint para salvar um novo filme
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @PostMapping
    public ResponseEntity<Object> saveMovie(@RequestBody MovieRequestDTO data){
        try {
            Movie movieData = new Movie(data);

            // Salva o filme no banco de dados usando o repository
            repository.save(movieData);
            URI location = new URI("/movie/" + movieData.getId());
            return ResponseEntity.created(location).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Endpoint para obter todos os filmes
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @GetMapping
    public List<MovieResponseDTO> getAll(){
        // Obtém todos os filmes da tabela "movie" usando o repository
        List<MovieResponseDTO> movieList = repository.findAll().stream().map(MovieResponseDTO::new).toList();
        return movieList;
    }

    // Endpoint para deletar um filme por ID
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteMovie(@PathVariable("id") Long id){
        try {
            // Verifica se o filme existe no banco de dados
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

    // Endpoint para atualizar os dados de um filme por ID
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @PutMapping("/{id}")
    public ResponseEntity<Object> updateMovie(@PathVariable Long id, @RequestBody MovieRequestDTO newData){
        try {
            // Busca o filme pelo ID
            Optional<Movie> optionalMovie = repository.findById(id);

            if (optionalMovie.isPresent()) {
                Movie existingMovie = optionalMovie.get();
                // Atualiza os dados do filme com os novos dados
                existingMovie.updateFromDTO(newData);
                repository.save(existingMovie);
                return ResponseEntity.ok().build(); // Resposta 200 OK
            } else {
                return ResponseEntity.notFound().build(); // Resposta 404 Not Found
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}