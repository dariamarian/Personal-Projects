package com.example.backend.utils.strategy;

import java.util.List;

public class EntitySorter<T extends Sortable> {
    private SortStrategy<T> strategy;

    public EntitySorter(SortStrategy<T> strategy) {
        this.strategy = strategy;
    }

    public void setStrategy(SortStrategy<T> strategy) {
        this.strategy = strategy;
    }

    public void sortEntities(List<T> entities) {
        strategy.sort(entities);
    }
}
