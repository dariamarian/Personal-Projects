package com.example.backend.repository;

import com.example.backend.model.FavoriteTrip;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FavoriteTripRepo extends JpaRepository<FavoriteTrip, Long> {
    List<FavoriteTrip> findAllByIdUser(Long idUser);

    Optional<FavoriteTrip> findByIdUserAndIdTrip(Long idUser, Long idTrip);
}
