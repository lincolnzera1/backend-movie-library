package com.movielibrary.movielibrary.movie;

import java.util.Date;

public record MovieRequestDTO(String title, Date releaseYear, String genre, Integer rating, String synopsis, String image) {
}