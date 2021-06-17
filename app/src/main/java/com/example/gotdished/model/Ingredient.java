package com.example.gotdished.model;

import java.io.Serializable;

public class Ingredient implements Serializable {
    private String name;
    private double quantity;
    private String measurement;

    public Ingredient(){}

    public Ingredient(String name, double quantity, String measurement) {
        this.name = name;
        this.quantity = quantity;
        this.measurement = measurement;
    }

    public String getName() {
        return name;
    }

    public double getQuantity() {
        return quantity;
    }

    public String getMeasurement() {
        return measurement;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Ingredient))
            return false;

        Ingredient o = (Ingredient)obj;

        return name.equals(o.getName());
    }
}
