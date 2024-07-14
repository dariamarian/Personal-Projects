package com.example.backend.service;

import com.example.backend.model.Attraction;
import com.example.backend.repository.AttractionRepo;
import com.example.backend.repository.EntityRepo;
import com.example.backend.repository.LocationRepo;
import com.example.backend.utils.adapter.AttractionAdapter;
import com.example.backend.utils.adapter.EntityAdapter;
import org.springframework.stereotype.Service;

@Service
public class AttractionService extends EntityService<Attraction> {
    public AttractionService(AttractionRepo entityRepo, LocationRepo locationRepo, AttractionAdapter entityAdapter) {
        super(entityRepo, locationRepo, entityAdapter);
    }
}
