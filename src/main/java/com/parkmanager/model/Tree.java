package com.parkmanager.model;

import java.time.LocalDate;

public class Tree {
    private int id;
    private String species;
    private double circumference;
    private LocalDate plantingDate;
    private boolean estimatedPlantingDate;
    private int condition; // 1-10 scale

    public Tree() {
    }

    public Tree(String species, double circumference, LocalDate plantingDate, boolean estimatedPlantingDate, int condition) {
        this.species = species;
        this.circumference = circumference;
        this.plantingDate = plantingDate;
        this.estimatedPlantingDate = estimatedPlantingDate;
        setCondition(condition);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSpecies() {
        return species;
    }

    public void setSpecies(String species) {
        this.species = species;
    }

    public double getCircumference() {
        return circumference;
    }

    public void setCircumference(double circumference) {
        this.circumference = circumference;
    }

    public LocalDate getPlantingDate() {
        return plantingDate;
    }

    public void setPlantingDate(LocalDate plantingDate) {
        this.plantingDate = plantingDate;
    }

    public boolean isEstimatedPlantingDate() {
        return estimatedPlantingDate;
    }

    public void setEstimatedPlantingDate(boolean estimatedPlantingDate) {
        this.estimatedPlantingDate = estimatedPlantingDate;
    }

    public int getCondition() {
        return condition;
    }

    public void setCondition(int condition) {
        if (condition < 1) {
            this.condition = 1;
        } else if (condition > 10) {
            this.condition = 10;
        } else {
            this.condition = condition;
        }
    }
} 