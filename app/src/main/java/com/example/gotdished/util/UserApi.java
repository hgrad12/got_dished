package com.example.gotdished.util;

import android.app.Application;

import java.util.Date;
import java.util.List;

public class UserApi extends Application {
    private String username;
    private String userId;
    private String email;
    private String imageUrl;
    private List<String> favorites;
    private List<String> recipes;
    private Date createDate;
    private Date updatedDate;

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(Date updatedDate) {
        this.updatedDate = updatedDate;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public List<String> getFavorites() {
        return favorites;
    }

    public void setFavorites(List<String> favorites) {
        this.favorites = favorites;
    }

    public List<String> getRecipes() {
        return recipes;
    }

    public void setRecipes(List<String> recipes) {
        this.recipes = recipes;
    }

    private static UserApi instance;

    public UserApi getInstance() {
        if (instance == null)
            instance = new UserApi();

        return instance;
    }

    public UserApi(){}

    public String getUsername() { return username; }

    public void setUsername(String username) { this.username = username; }

    public String getUserId() { return userId; }

    public void setUserId(String userId) { this.userId = userId; }
}
