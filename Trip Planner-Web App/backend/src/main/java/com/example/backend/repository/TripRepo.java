package com.example.backend.repository;

import com.example.backend.model.Trip;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface TripRepo extends JpaRepository<Trip, Long> {
    List<Trip> findAllByUserId(Long userId);

    @Modifying
    @Transactional
    @Query("DELETE FROM Trip t WHERE t.trip_id = :trip_id")
    void deleteByTripId(Long trip_id);
}
