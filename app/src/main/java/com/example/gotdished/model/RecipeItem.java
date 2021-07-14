package com.example.gotdished.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.databinding.BindingAdapter;

import com.example.gotdished.R;
import com.example.gotdished.util.RecipeItemValues;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class RecipeItem implements Parcelable {
    private String recipeUuid;
    private String name;
    private String timeToCompletion;
    private String imageUri;
    private String category;
    private long numOfLikes;

    public RecipeItem(){}

    public RecipeItem(String recipeUuid, String name, String timeToCompletion, String imageUri,
                      String category, long numOfLikes) {
        this.recipeUuid = recipeUuid;
        this.name = name;
        this.timeToCompletion = timeToCompletion;
        this.imageUri = imageUri;
        this.category = category;
        this.numOfLikes = numOfLikes;
    }

    public RecipeItem(RecipeItem other){
        recipeUuid = other.getRecipeUuid();
        name = other.getName();
        timeToCompletion = other.getTimeToCompletion();
        imageUri = other.getImageUri();
        category = other.getCategory();
        numOfLikes = other.getNumOfLikes();
    }

    protected RecipeItem(Parcel in) {
        recipeUuid = in.readString();
        name = in.readString();
        timeToCompletion = in.readString();
        imageUri = in.readString();
        category = in.readString();
        numOfLikes = in.readLong();
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

    public long getNumOfLikes() {
        return numOfLikes;
    }

    public void setNumOfLikes(long numOfLikes) {
        this.numOfLikes = numOfLikes;
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
        recipeItem.put(RecipeItemValues.NUMBER_OF_LIKES, numOfLikes);
        return recipeItem;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(recipeUuid);
        dest.writeString(name);
        dest.writeString(timeToCompletion);
        dest.writeString(imageUri);
        dest.writeString(category);
        dest.writeLong(numOfLikes);
    }

    @Override
    public boolean equals(@Nullable @org.jetbrains.annotations.Nullable Object obj) {
        if (!(obj instanceof RecipeItem)) return false;

        RecipeItem o = (RecipeItem)obj;

        return Objects.equals(recipeUuid, o.getRecipeUuid());
    }

    @BindingAdapter("android:loadImage")
    public static void loadImage(ImageView imageView, String imageUrl) {
        Picasso.get().load(imageUrl).placeholder(R.drawable.dish_placeholder).fit().into(imageView);
    }
}
