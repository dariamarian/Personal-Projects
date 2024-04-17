package com.example.gamificationapp.domain;

public class Badge extends Entity<Integer>{

    private String name;
    private String description;

    public Badge(String name, String description)
    {
        this.name=name;
        this.description=description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
