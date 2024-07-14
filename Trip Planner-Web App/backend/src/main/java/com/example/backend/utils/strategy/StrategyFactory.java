package com.example.backend.utils.strategy;


import com.example.backend.utils.strategy.strategies.ByName;
import com.example.backend.utils.strategy.strategies.ByPrice;
import com.example.backend.utils.strategy.strategies.ByRating;

public class StrategyFactory {
    public static <T extends Sortable> SortStrategy<T> getStrategy(String strategyType) {
        return switch (strategyType) {
            case "rating" -> new ByRating<>();
            case "price" -> new ByPrice<>();
            case "name" -> new ByName<>();
            default -> throw new IllegalArgumentException("Unknown strategy type");
        };
    }
}