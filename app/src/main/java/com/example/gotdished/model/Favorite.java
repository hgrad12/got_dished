package com.example.gotdished.model;

import com.example.gotdished.util.FavoriteValues;
import com.example.gotdished.util.RecipeItemValues;

import java.util.HashMap;
import java.util.Map;

public class Favorite extends RecipeItem {
    private final String user_uuid;
//    private final String favorite_uuid;

    public Favorite(String user_uuid, String recipe_uuid, String name, String ttc,
                    String imageUri, String category, long numOfLikes) {
        super(recipe_uuid, name, ttc, imageUri, category, numOfLikes);
        this.user_uuid = user_uuid;
    }

    public Favorite(String user_uuid, RecipeItem item) {
        super(item);
        this.user_uuid = user_uuid;
    }

    @Override
    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put(FavoriteValues.USER_UUID, user_uuid);
        map.put(RecipeItemValues.NAME, getName());
        map.put(RecipeItemValues.UUID, getRecipeUuid());
        map.put(RecipeItemValues.TIME_TO_COMPLETION, getTimeToCompletion());
        map.put(RecipeItemValues.IMAGE_URI, getImageUri());
        map.put(RecipeItemValues.CATEGORY, getCategory());
        map.put(RecipeItemValues.NUMBER_OF_LIKES, getNumOfLikes());
        return map;
    }
}
