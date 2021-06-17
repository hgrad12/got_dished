package com.example.gotdished.util;

import com.example.gotdished.model.Ingredient;
import com.example.gotdished.model.Step;

import java.util.List;
import java.util.stream.Collectors;

public class RecipeUtil {

    public static String ingredientsToString(List<Ingredient> ingredients) {
        if (ingredients.isEmpty()) return "";

        StringBuilder sb = new StringBuilder();
        sb.append(ingredients.get(0).getName());
        for (int x = 1; x < ingredients.size(); x++) {
            sb.append(", ").append(ingredients.get(x).getName());
        }
        return sb.toString();
    }

    public static String listToString(List<String> list){
        if (list.isEmpty()) return "";

        return list.stream().collect(Collectors.joining(", "));
    }

    public static String stepsToString(List<Step> steps) {
        if (steps.isEmpty()) return "";

        StringBuilder sb = new StringBuilder();
        sb.append(1 + ".\t").append(steps.get(0).getDetails());
        for (int x = 1; x < steps.size(); x++) {
            sb.append("\n\n").append(x + 1).append(".\t").append(steps.get(x).getDetails());
        }
        return sb.toString();
    }
}
