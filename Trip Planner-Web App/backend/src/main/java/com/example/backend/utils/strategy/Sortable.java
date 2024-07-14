package com.example.backend.utils.strategy;

// Common interface for all sortable entities
public interface Sortable {
    double getRating();  // Common method used by all sortable entities

    String getPrice();   // Common method for price (not all entities might implement it)

    String getName();    // Common method for name (not all entities might implement it)
}
