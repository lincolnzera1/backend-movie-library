package com.movielibrary.movielibrary.rating;

import com.movielibrary.movielibrary.movie.Movie;
import com.movielibrary.movielibrary.user.User;

import java.util.Date;

public record RatingRequestDTO(User user, Movie movie, Integer ratingValue) {
}