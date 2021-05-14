package com.example.gotdished.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.gotdished.util.RecipeValues;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.ServerTimestamp;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Recipe implements Parcelable {
    private String name;
    private String imageUri;
    private String recipeUuid;
    private String userUuid;
    private String createdBy;
    private List<Step> steps;
    private List<String> equipment;
    private List<String> ingredients; //Todo: convert to a list of Ingredient (Object)
    private String category;
    private String timeToCompletion;

    @ServerTimestamp private Timestamp dateCreated;

    @ServerTimestamp private Timestamp dateModified;

    public Recipe(String name, String imageUri, String recipeUuid, String userUuid, String createdBy,
                  List<Step> steps, List<String> equipment, List<String> ingredients, String category,
                  String timeToCompletion, Timestamp dateCreated, Timestamp dateModified) {
        this.name = name;
        this.imageUri = imageUri;
        this.recipeUuid = recipeUuid;
        this.userUuid = userUuid;
        this.createdBy = createdBy;
        this.steps = steps;
        this.equipment = equipment;
        this.ingredients = ingredients;
        this.category = category;
        this.timeToCompletion = timeToCompletion;
        this.dateCreated = dateCreated;
        this.dateModified = dateModified;
    }

    protected Recipe(Parcel in) {
        name = in.readString();
        imageUri = in.readString();
        userUuid = in.readString();
        createdBy = in.readString();
        recipeUuid = in.readString();
        steps = new ArrayList<>();
        in.readList(steps, Step.class.getClassLoader());
        equipment = in.createStringArrayList();
        ingredients = in.createStringArrayList();
        category = in.readString();
        timeToCompletion = in.readString();
        dateCreated = new Timestamp(new Date(in.readLong()));
        dateModified = new Timestamp(new Date(in.readLong()));
    }

    public static final Creator<Recipe> CREATOR = new Creator<Recipe>() {
        @Override
        public Recipe createFromParcel(Parcel in) {
            return new Recipe(in);
        }

        @Override
        public Recipe[] newArray(int size) {
            return new Recipe[size];
        }
    };

    public Timestamp getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Timestamp dateCreated) {
        this.dateCreated = dateCreated;
    }

    public Timestamp getDateModified() {
        return dateModified;
    }

    public void setDateModified(Timestamp dateModified) {
        this.dateModified = dateModified;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getTimeToCompletion() {
        return timeToCompletion;
    }

    public void setTimeToCompletion(String timeToCompletion) {
        this.timeToCompletion = timeToCompletion;
    }

    public String getRecipeUuid() {
        return recipeUuid;
    }

    public void setRecipeUuid(String recipeUuid) {
        this.recipeUuid = recipeUuid;
    }

    public Recipe() {}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }

    public String getUserUuid() {
        return userUuid;
    }

    public void setUserUuid(String userUuid) {
        this.userUuid = userUuid;
    }

    public List<Step> getSteps() {
        return steps;
    }

    public void setSteps(List<Step> steps) {
        this.steps = steps;
    }

    public List<String> getEquipment() {
        return equipment;
    }

    public void setEquipment(List<String> equipment) {
        this.equipment = equipment;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public List<String> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<String> ingredients) {
        this.ingredients = ingredients;
    }

    public Map<String, Object> toMap(){
        Map<String, Object> recipe = new HashMap<>();
        recipe.put(RecipeValues.NAME, name);
        recipe.put(RecipeValues.IMAGE_URI, imageUri);
        recipe.put(RecipeValues.RECIPE_UUID, recipeUuid);
        recipe.put(RecipeValues.USER_UUID, userUuid);
        recipe.put(RecipeValues.CREATED_BY, createdBy);
        recipe.put(RecipeValues.STEPS, steps);
        recipe.put(RecipeValues.EQUIPMENT, equipment);
        recipe.put(RecipeValues.INGREDIENTS, ingredients);
        recipe.put(RecipeValues.CATEGORY, category);
        recipe.put(RecipeValues.TIME_TO_COMPLETION, timeToCompletion);
        recipe.put(RecipeValues.DATE_CREATED, dateCreated);
        recipe.put(RecipeValues.DATE_MODIFIED, dateModified);
        return recipe;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(imageUri);
        dest.writeString(userUuid);
        dest.writeString(createdBy);
        dest.writeString(recipeUuid);
        dest.writeList(steps);
        dest.writeStringList(equipment);
        dest.writeStringList(ingredients);
        dest.writeString(category);
        dest.writeString(timeToCompletion);
        dest.writeLong(dateCreated.getSeconds());
        dest.writeLong(dateModified.getSeconds());
    }
}
