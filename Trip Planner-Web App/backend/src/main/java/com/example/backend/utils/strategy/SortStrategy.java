package com.example.backend.utils.strategy;

import java.util.List;

public interface SortStrategy<T extends Sortable> {
    void sort(List<T> items);
}
