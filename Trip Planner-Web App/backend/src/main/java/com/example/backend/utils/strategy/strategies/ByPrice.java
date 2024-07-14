package com.example.backend.utils.strategy.strategies;

import com.example.backend.utils.strategy.SortStrategy;
import com.example.backend.utils.strategy.Sortable;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ByPrice<T extends Sortable> implements SortStrategy<T> {
    public void sort(List<T> items) {
        items.sort(Comparator.comparing(Sortable::getPrice));
        System.out.println("Sorted by price");
    }
}
