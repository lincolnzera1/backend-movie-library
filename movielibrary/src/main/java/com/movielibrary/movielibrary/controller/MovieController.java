package com.movielibrary.movielibrary.controller;


import com.movielibrary.movielibrary.movie.Movie;
import com.movielibrary.movielibrary.movie.MovieRepository;
import com.movielibrary.movielibrary.movie.MovieRequestDTO;
import com.movielibrary.movielibrary.movie.MovieResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Collections;
import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("movie")
public class MovieController {

    @Autowired
    private MovieRepository repository;

    // Endpoint para salvar um novo filme
    @Operation(summary = "Adicionar novo filme", description = "Este endpoint adiciona um novo filme à lista.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Filme criado com sucesso."),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor ao criar o filme.") // Adicione mais respostas conforme necessário
    })
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @PostMapping
    public ResponseEntity<Object> saveMovie(@RequestBody MovieRequestDTO data){
        try {
            // Cria um novo objeto Movie com base nos dados fornecidos
            Movie movieData = new Movie(data);

            // Salva o filme no banco de dados usando o repository
            repository.save(movieData);

            // Retorna uma resposta com o código 201 Created e a URI do novo recurso criado
            URI location = new URI("/movie/" + movieData.getId());
            return ResponseEntity.created(location).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Endpoint para obter todos os filmes, com a opção de pesquisa por título
    @Operation(summary = "Buscar filmes",
            description = "Este endpoint retorna uma lista de filmes. " +
                    "É possível pesquisar filmes por título, fornecendo o parâmetro 'title'. " +
                    "Se nenhum parâmetro for fornecido, retorna todos os filmes.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de filmes retornada com sucesso.")
    })
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @GetMapping
    public List<MovieResponseDTO> getAll(
            @Parameter(in = ParameterIn.QUERY, name = "title",
                    description = "Título do filme a ser pesquisado. A pesquisa é insensível a maiúsculas e minúsculas.",
                    example = "Ação") @RequestParam(required = false) String title) {
        try {
            // Se houver um parâmetro de pesquisa, filtra os filmes por título
            if (title != null && !title.isEmpty()) {
                return repository.findByTitleContainingIgnoreCase(title)
                        .stream().map(MovieResponseDTO::new).toList();
            }

            // Caso contrário, retorna todos os filmes
            return repository.findAll().stream().map(MovieResponseDTO::new).toList();
        } catch (Exception e) {
            // Em caso de erro, você pode personalizar a resposta de erro conforme necessário
            return Collections.emptyList(); // Retorna uma lista vazia em caso de erro
        }
    }

    // Endpoint para deletar um filme por ID
    @Operation(summary = "Deletar filme por ID",
            description = "Este endpoint exclui um filme com o ID especificado.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Filme excluído com sucesso."),
            @ApiResponse(responseCode = "404", description = "Filme não encontrado com o ID especificado."),
            @ApiResponse(responseCode = "500", description = "Erro interno no servidor.")
    })
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteMovie(@PathVariable("id") Long id){
        try {
            // Verifica se o filme existe no banco de dados
            if (repository.existsById(id)) {
                repository.deleteById(id);
                return ResponseEntity.ok().build(); // Resposta 200 No Content
            } else {
                return ResponseEntity.notFound().build(); // Resposta 404 Not Found
            }
        } catch (Exception e) {
            // Em caso de erro interno, você pode personalizar a resposta de erro conforme necessário

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Endpoint para atualizar os dados de um filme por ID
    @Operation(summary = "Atualizar filme por ID",
            description = "Este endpoint atualiza os dados de um filme com o ID especificado.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Filme atualizado com sucesso."),
            @ApiResponse(responseCode = "404", description = "Filme não encontrado com o ID especificado."),
            @ApiResponse(responseCode = "500", description = "Erro interno no servidor.")
    })
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
            // Em caso de erro interno.
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}