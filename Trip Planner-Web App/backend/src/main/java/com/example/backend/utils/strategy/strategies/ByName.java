package com.example.backend.utils.strategy.strategies;

import com.example.backend.utils.strategy.SortStrategy;
import com.example.backend.utils.strategy.Sortable;

import java.util.Comparator;
import java.util.List;

public class ByName<T extends Sortable> implements SortStrategy<T> {
    public void sort(List<T> items) {
        items.sort(Comparator.comparing(Sortable::getName));
        System.out.println("Sorted alphabetically by name");
    }
}

