package com.example.gotdished.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.gotdished.model.RecipeItem;
import com.example.gotdished.repository.RecipeItemRepository;

import java.util.List;

public class HomeViewModel extends ViewModel {
    RecipeItemRepository repository;

    public HomeViewModel() {
        repository = new RecipeItemRepository();
    }

    public LiveData<List<RecipeItem>> getRecipeItems() { return repository.getRecipeItems(); }
}