package com.pratham.RatingManagementSystem.controller;

import com.pratham.RatingManagementSystem.dto.RatingResponse;
import com.pratham.RatingManagementSystem.dto.SimpleUserResponse;
import com.pratham.RatingManagementSystem.model.Rating;
import com.pratham.RatingManagementSystem.repository.RatingRepository;
import org.springframework.web.bind.annotation.*;

import java.util.DoubleSummaryStatistics;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/admin")
public class AdminController {

    private final RatingRepository ratingRepository;

    public AdminController(RatingRepository ratingRepository) {
        this.ratingRepository = ratingRepository;
    }

    @GetMapping("/all-ratings")
    public List<RatingResponse> getAllRatings() {
        return ratingRepository.findAll().stream()
                .map(this::toDto)
                .toList();
    }

    @GetMapping("/filter")
    public List<RatingResponse> filterRatings(
            @RequestParam(required = false) Integer ambiance,
            @RequestParam(required = false) Integer food,
            @RequestParam(required = false) Integer service,
            @RequestParam(required = false) Integer cleanliness,
            @RequestParam(required = false) Integer drinks
    ) {
        return ratingRepository.filterRatings(ambiance, food, service, cleanliness, drinks)
                .stream()
                .map(this::toDto)
                .toList();
    }

    @GetMapping("/report")
    public Map<String, Object> generateReport() {
        List<Rating> ratings = ratingRepository.findAll();
        DoubleSummaryStatistics ambianceStats = ratings.stream().collect(Collectors.summarizingDouble(Rating::getAmbiance));
        DoubleSummaryStatistics foodStats = ratings.stream().collect(Collectors.summarizingDouble(Rating::getFood));
        DoubleSummaryStatistics serviceStats = ratings.stream().collect(Collectors.summarizingDouble(Rating::getService));
        DoubleSummaryStatistics cleanStats = ratings.stream().collect(Collectors.summarizingDouble(Rating::getCleanliness));
        DoubleSummaryStatistics drinkStats = ratings.stream().collect(Collectors.summarizingDouble(Rating::getDrinks));

        double overallAverage = (ambianceStats.getAverage() + foodStats.getAverage() + serviceStats.getAverage() + cleanStats.getAverage() + drinkStats.getAverage()) / 5;

        return Map.of(
                "ambianceAvg", ambianceStats.getAverage(),
                "foodAvg", foodStats.getAverage(),
                "serviceAvg", serviceStats.getAverage(),
                "cleanlinessAvg", cleanStats.getAverage(),
                "drinksAvg", drinkStats.getAverage(),
                "overallAvg", overallAverage
        );
    }

    private RatingResponse toDto(Rating rating) {
        RatingResponse dto = new RatingResponse();
        dto.id = rating.getId();

        SimpleUserResponse userDto = new SimpleUserResponse();
        userDto.id = rating.getUser().getId();
        userDto.name = rating.getUser().getName();
        userDto.email = rating.getUser().getEmail();
        userDto.role = rating.getUser().getRole().name();

        dto.user = userDto;
        dto.ambiance = rating.getAmbiance();
        dto.food = rating.getFood();
        dto.service = rating.getService();
        dto.cleanliness = rating.getCleanliness();
        dto.drinks = rating.getDrinks();

        return dto;
    }
}
