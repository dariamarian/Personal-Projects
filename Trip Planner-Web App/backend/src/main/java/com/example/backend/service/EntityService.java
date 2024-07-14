package com.example.backend.service;

import com.example.backend.exception.NotFoundException;
import com.example.backend.model.Location;
import com.example.backend.repository.EntityRepo;
import com.example.backend.repository.LocationRepo;
import com.example.backend.utils.adapter.EntityAdapter;
import com.example.backend.utils.strategy.EntitySorter;
import com.example.backend.utils.strategy.SortStrategy;
import com.example.backend.utils.strategy.Sortable;
import com.example.backend.utils.strategy.StrategyFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public abstract class EntityService<T extends Sortable> {
    private final EntityRepo<T> entityRepo;
    private final LocationRepo locationRepo;
    private final EntityAdapter<T> entityAdapter;

    public EntityService(EntityRepo<T> entityRepo, LocationRepo locationRepo, EntityAdapter<T> entityAdapter) {
        this.entityRepo = entityRepo;
        this.locationRepo = locationRepo;
        this.entityAdapter = entityAdapter;
    }

    public void parseEntities(Long locationId, String responseBody) {
        entityAdapter.process(locationId, responseBody);
    }

    public List<T> getEntitiesByLocationId(Long locationId) throws NotFoundException {
        Location location = locationRepo.findById(locationId)
                .orElseThrow(() -> new NotFoundException("Location not found with id: " + locationId));
        return entityRepo.findAllByLocation(location);
    }

    public List<T> sortEntities(Long locationId, String strategy) throws NotFoundException {
        SortStrategy<T> sortStrategy = StrategyFactory.getStrategy(strategy);
        EntitySorter<T> entitySorter = new EntitySorter<>(sortStrategy);
        List<T> entities = getEntitiesByLocationId(locationId);
        entitySorter.sortEntities(entities);
        return entities;
    }
}

