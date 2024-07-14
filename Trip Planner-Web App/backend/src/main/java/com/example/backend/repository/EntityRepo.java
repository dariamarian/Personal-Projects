package com.example.backend.repository;

import com.example.backend.model.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.List;

@NoRepositoryBean
public interface EntityRepo<T> extends JpaRepository<T, Long> {
    List<T> findAllByLocation(Location location);
}

