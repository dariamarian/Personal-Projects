package com.example.backend.repository;

import com.example.backend.model.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * This interface represents the repository for the Location entity.
 */
@Repository
public interface LocationRepo extends JpaRepository<Location, Long> {
    Location findByName(String name);

    @Query("SELECT l FROM Location l WHERE l.location_id = :locationId")
    Optional<Location> findByLocationId(@Param("locationId") Long locationId);

}
