package com.example.backend.utils.strategy.strategies;

import com.example.backend.utils.strategy.SortStrategy;
import com.example.backend.utils.strategy.Sortable;

import java.util.Comparator;
import java.util.List;

public class ByRating<T extends Sortable> implements SortStrategy<T> {
    public void sort(List<T> items) {
        items.sort(Comparator.comparingDouble(Sortable::getRating).reversed());
        System.out.println("Sorted by rating");
    }
}