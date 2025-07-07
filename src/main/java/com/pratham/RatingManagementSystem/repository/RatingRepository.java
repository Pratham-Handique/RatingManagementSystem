package com.pratham.RatingManagementSystem.repository;

import com.pratham.RatingManagementSystem.model.Rating;
import com.pratham.RatingManagementSystem.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface RatingRepository extends JpaRepository<Rating, Long> {
    List<Rating> findByUser(User user);
}