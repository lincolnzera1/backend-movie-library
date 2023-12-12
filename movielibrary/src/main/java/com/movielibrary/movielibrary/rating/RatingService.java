package com.movielibrary.movielibrary.rating;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RatingService {

    @Autowired
    private RatingRepository ratingRepository;

    public Optional<Rating> getRatingByUserIdAndMovieId(Long userId, Long movieId) {
        return ratingRepository.findByUserIdAndMovieId(userId, movieId);
    }
}
