package com.example.backend.utils.adapter;

import com.example.backend.utils.strategy.Sortable;
import org.springframework.stereotype.Component;

@Component
public abstract class EntityAdapter<T extends Sortable> {
    public abstract void process(Long locationId, String responseBody);
}
