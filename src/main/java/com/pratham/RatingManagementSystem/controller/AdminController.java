package com.pratham.RatingManagementSystem.controller;

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
    public List<Rating> getAllRatings() {
        return ratingRepository.findAll();
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

    @GetMapping("/filter")
    public List<Rating> filterRatings(
            @RequestParam(required = false) Integer ambiance,
            @RequestParam(required = false) Integer food,
            @RequestParam(required = false) Integer service,
            @RequestParam(required = false) Integer cleanliness,
            @RequestParam(required = false) Integer drinks
    ) {
        return ratingRepository.findAll().stream()
                .filter(r -> ambiance == null || r.getAmbiance() == ambiance)
                .filter(r -> food == null || r.getFood() == food)
                .filter(r -> service == null || r.getService() == service)
                .filter(r -> cleanliness == null || r.getCleanliness() == cleanliness)
                .filter(r -> drinks == null || r.getDrinks() == drinks)
                .collect(Collectors.toList());
    }
}
