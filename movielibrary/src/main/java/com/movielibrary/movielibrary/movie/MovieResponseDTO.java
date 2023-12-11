package com.movielibrary.movielibrary.movie;

import java.util.Date;

public record MovieResponseDTO(Long id, String title, Date releaseYear, String genre, Integer rating, String synopsis, String image) {
    public MovieResponseDTO(Movie movie){
        this(movie.getId(), movie.getTitle(), movie.getReleaseYear(), movie.getGenre(), movie.getRating(), movie.getSynopsis(), movie.getImage());
    }
}
