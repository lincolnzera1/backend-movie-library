package com.movielibrary.movielibrary.controller;

import com.movielibrary.movielibrary.movie.Movie;
import com.movielibrary.movielibrary.movie.MovieRepository;
import com.movielibrary.movielibrary.movie.MovieRequestDTO;
import com.movielibrary.movielibrary.movie.MovieResponseDTO;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class MovieControllerTest {

    @InjectMocks
    private MovieController movieController;

    @Mock
    private MovieRepository movieRepository;

    // GET MOVIES

    //Verificar se o método getAll do controlador retorna uma lista não vazia de filmes quando nenhum filtro é aplicado.
    @Test
    public void testGetAllMoviesNoFilter() {
        // Configurar o comportamento simulado do repository
        when(movieRepository.findAll()).thenReturn(Collections.singletonList(new Movie()));

        // Chamar o método do controller
        List<MovieResponseDTO> movies = movieController.getAll(null);

        // Verificar se a lista de filmes não está vazia
        assertFalse(movies.isEmpty());
    }

    // Verificar se o método getAll do controlador retorna uma lista não vazia de filmes quando um filtro por título é aplicado.
    @Test
    public void testGetAllMoviesWithFilter() {
        // Configurar o comportamento simulado do repository
        when(movieRepository.findByTitleContainingIgnoreCase("pirata"))
                .thenReturn(Collections.singletonList(new Movie()));

        // Chamar o método do controller
        List<MovieResponseDTO> movies = movieController.getAll("pirata");

        // Verificar se a lista de filmes não está vazia
        assertFalse(movies.isEmpty());
    }


    // Verificar se o método getAll do controlador retorna uma lista vazia quando ocorre
    // um erro ao obter a lista de filmes do repositório.
    @Test
    public void testGetAllMoviesError() {
        // Configurar o comportamento simulado do repository para lançar uma exceção
        when(movieRepository.findAll()).thenThrow(new RuntimeException("Erro ao obter a lista de filmes"));

        // Chamar o método do controller
        List<MovieResponseDTO> movies = movieController.getAll(null);

        // Verificar se a lista de filmes está vazia devido ao erro
        assertTrue(movies.isEmpty());
    }

    // POST MOVIE

    // Verificar se o método saveMovie do controlador retorna uma resposta 201 Created ao salvar um filme com sucesso.
    @Test
    public void testSaveMovieSuccess() {
        // Dados de entrada para salvar um filme
        MovieRequestDTO movieData = new MovieRequestDTO("Inception",new Date(), "Sci-Fi", 10, "Muito bom", "");

        // Configurar o comportamento simulado do repository
        when(movieRepository.save(any())).thenReturn(new Movie(movieData));

        // Chamar o método do controller
        ResponseEntity<Object> response = movieController.saveMovie(movieData);

        // Verificar se a resposta é 201 Created
        assertEquals(201, response.getStatusCodeValue());
    }

    // Verificar se o método saveMovie do controlador retorna uma resposta 500 Internal Server Error
    // ao ocorrer um erro ao salvar um filme.
    @Test
    public void testSaveMovieError() {
        // Dados de entrada para salvar um filme
        MovieRequestDTO movieData = new MovieRequestDTO("Inception",new Date(), "Sci-Fi", 10, "Muito bom", "");

        // Configurar o comportamento simulado do repository para lançar uma exceção
        when(movieRepository.save(any())).thenThrow(new RuntimeException("Erro ao salvar o filme"));

        // Chamar o método do controller
        ResponseEntity<Object> response = movieController.saveMovie(movieData);

        // Verificar se a resposta é 500 Internal Server Error
        assertEquals(500, response.getStatusCodeValue());
    }

    ////////// DELETE MOVIE

    // Verificar se o método deleteMovie do controlador retorna uma resposta 200 OK ao excluir um filme com sucesso.
    @Test
    public void testDeleteMovieSuccess() {
        // ID do filme a ser excluído
        Long movieId = 1L;

        // Configurar o comportamento simulado do repository
        when(movieRepository.existsById(movieId)).thenReturn(true);

        // Chamar o método do controller
        ResponseEntity<Object> response = movieController.deleteMovie(movieId);

        // Verificar se a resposta é 200 OK
        assertEquals(200, response.getStatusCodeValue());
    }


    // Verificar se o método deleteMovie do controlador retorna uma resposta 404 Not Found
    // quando tenta excluir um filme que não existe.
    @Test
    public void testDeleteMovieNotFound() {
        // ID do filme a ser excluído
        Long movieId = 1L;

        // Configurar o comportamento simulado do repository
        when(movieRepository.existsById(movieId)).thenReturn(false);

        // Chamar o método do controller
        ResponseEntity<Object> response = movieController.deleteMovie(movieId);

        // Verificar se a resposta é 404 Not Found
        assertEquals(404, response.getStatusCodeValue());
    }


    // Verificar se o método deleteMovie do controlador retorna uma resposta 500 Internal Server Error
    // ao ocorrer um erro ao excluir um filme.
    @Test
    public void testDeleteMovieError() {
        // ID do filme a ser excluído
        Long movieId = 1L;

        // Configurar o comportamento simulado do repository para lançar uma exceção
        when(movieRepository.existsById(movieId)).thenThrow(new RuntimeException("Erro ao excluir o filme"));

        // Chamar o método do controller
        ResponseEntity<Object> response = movieController.deleteMovie(movieId);

        // Verificar se a resposta é 500 Internal Server Error
        assertEquals(500, response.getStatusCodeValue());
    }


    /////// UPDATE MOVIE

    // Verificar se o método updateMovie do controlador retorna uma resposta 200 OK ao atualizar um filme com sucesso.
    @Test
    public void testUpdateMovieSuccess() {
        // ID do filme a ser atualizado
        Long movieId = 1L;

        // Dados de entrada para atualizar o filme
        MovieRequestDTO newData = new MovieRequestDTO("Updated Title",new Date(), "Drama", 10, "Muito bom", "");


        // Configurar o comportamento simulado do repository
        when(movieRepository.findById(movieId)).thenReturn(Optional.of(new Movie()));

        // Chamar o método do controller
        ResponseEntity<Object> response = movieController.updateMovie(movieId, newData);

        // Verificar se a resposta é 200 OK
        assertEquals(200, response.getStatusCodeValue());
    }

    // Verificar se o método updateMovie do controlador retorna uma resposta 404 Not Found
    // quando tenta atualizar um filme que não existe.
    @Test
    public void testUpdateMovieNotFound() {
        // ID do filme a ser atualizado
        Long movieId = 1L;

        // Dados de entrada para atualizar o filme
        MovieRequestDTO newData = new MovieRequestDTO("Updated Title",new Date(), "Drama", 10, "Muito bom", "");

        // Configurar o comportamento simulado do repository
        when(movieRepository.findById(movieId)).thenReturn(Optional.empty());

        // Chamar o método do controller
        ResponseEntity<Object> response = movieController.updateMovie(movieId, newData);

        // Verificar se a resposta é 404 Not Found
        assertEquals(404, response.getStatusCodeValue());
    }

    // Verificar se o método updateMovie do controlador retorna uma resposta 500 Internal Server Error
    // ao ocorrer um erro ao atualizar um filme.
    @Test
    public void testUpdateMovieError() {
        // ID do filme a ser atualizado
        Long movieId = 1L;

        // Dados de entrada para atualizar o filme
        MovieRequestDTO newData = new MovieRequestDTO("Updated Title",new Date(), "Drama", 10, "Muito bom", "");

        // Configurar o comportamento simulado do repository para lançar uma exceção
        when(movieRepository.findById(movieId)).thenThrow(new RuntimeException("Erro ao atualizar o filme"));

        // Chamar o método do controller
        ResponseEntity<Object> response = movieController.updateMovie(movieId, newData);

        // Verificar se a resposta é 500 Internal Server Error
        assertEquals(500, response.getStatusCodeValue());
    }
}

