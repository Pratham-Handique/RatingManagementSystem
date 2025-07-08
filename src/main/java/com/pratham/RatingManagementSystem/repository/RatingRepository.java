package com.pratham.RatingManagementSystem.repository;

import com.pratham.RatingManagementSystem.model.Rating;
import com.pratham.RatingManagementSystem.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RatingRepository extends JpaRepository<Rating, Long> {
    List<Rating> findByUser(User user);

    @Query("""
        SELECT r FROM Rating r
        WHERE (:ambiance IS NULL OR r.ambiance = :ambiance)
          AND (:food IS NULL OR r.food = :food)
          AND (:service IS NULL OR r.service = :service)
          AND (:cleanliness IS NULL OR r.cleanliness = :cleanliness)
          AND (:drinks IS NULL OR r.drinks = :drinks)
    """)
    List<Rating> filterRatings(
            @Param("ambiance") Integer ambiance,
            @Param("food") Integer food,
            @Param("service") Integer service,
            @Param("cleanliness") Integer cleanliness,
            @Param("drinks") Integer drinks
    );
}
