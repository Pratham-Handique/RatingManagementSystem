package com.pratham.RatingManagementSystem.controller;

import com.pratham.RatingManagementSystem.dto.RatingRequest;
import com.pratham.RatingManagementSystem.model.Rating;
import com.pratham.RatingManagementSystem.model.User;
import com.pratham.RatingManagementSystem.repository.RatingRepository;
import com.pratham.RatingManagementSystem.security.JwtUtil;
import com.pratham.RatingManagementSystem.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    private final RatingRepository ratingRepository;
    private final UserService userService;

    public UserController(RatingRepository ratingRepository, UserService userService) {
        this.ratingRepository = ratingRepository;
        this.userService = userService;
    }

    @PostMapping("/rate")
    public ResponseEntity<String> submitRating(@RequestBody RatingRequest request, @AuthenticationPrincipal UserDetails userDetails) {
        User user = userService.getUser(userDetails.getUsername());

        Rating rating = new Rating();
        rating.setUser(user);
        rating.setAmbiance(request.ambiance);
        rating.setFood(request.food);
        rating.setService(request.service);
        rating.setCleanliness(request.cleanliness);
        rating.setDrinks(request.drinks);

        ratingRepository.save(rating);

        return ResponseEntity.ok("Rating submitted successfully");
    }
}
