package com.example.gotdished.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.gotdished.util.RecipeItemValues;

import java.util.HashMap;
import java.util.Map;

public class RecipeItem implements Parcelable {
    private String recipeUuid;
    private String name;
    private String timeToCompletion;
    private String imageUri;
    private String category;

    public RecipeItem(){}

    public RecipeItem(String recipeUuid, String name, String timeToCompletion, String imageUri, String category) {
        this.recipeUuid = recipeUuid;
        this.name = name;
        this.timeToCompletion = timeToCompletion;
        this.imageUri = imageUri;
        this.category = category;
    }

    protected RecipeItem(Parcel in) {
        recipeUuid = in.readString();
        name = in.readString();
        timeToCompletion = in.readString();
        imageUri = in.readString();
        category = in.readString();
    }

    public static final Creator<RecipeItem> CREATOR = new Creator<RecipeItem>() {
        @Override
        public RecipeItem createFromParcel(Parcel in) {
            return new RecipeItem(in);
        }

        @Override
        public RecipeItem[] newArray(int size) {
            return new RecipeItem[size];
        }
    };

    public String getRecipeUuid() {
        return recipeUuid;
    }

    public void setRecipeUuid(String recipeUuid) {
        this.recipeUuid = recipeUuid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTimeToCompletion() {
        return timeToCompletion;
    }

    public void setTimeToCompletion(String timeToCompletion) {
        this.timeToCompletion = timeToCompletion;
    }

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
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

    public Map<String, Object> toMap(){
        Map<String, Object> recipeItem = new HashMap<>();
        recipeItem.put(RecipeItemValues.UUID, recipeUuid);
        recipeItem.put(RecipeItemValues.NAME, name);
        recipeItem.put(RecipeItemValues.TIME_TO_COMPLETION, timeToCompletion);
        recipeItem.put(RecipeItemValues.IMAGE_URI, imageUri);
        recipeItem.put(RecipeItemValues.CATEGORY, category);
        return recipeItem;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(recipeUuid);
        dest.writeString(name);
        dest.writeString(timeToCompletion);
        dest.writeString(imageUri);
        dest.writeString(category);
    }
}
