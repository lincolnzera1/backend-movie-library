package com.movielibrary.movielibrary.user;

import com.movielibrary.movielibrary.movie.Movie;

public record UserResponseDTO(Long id, String username, String email, String password) {
    public UserResponseDTO(User user){
        this(user.getId(), user.getUsername(), user.getEmail(), user.getPassword());
    }
}
