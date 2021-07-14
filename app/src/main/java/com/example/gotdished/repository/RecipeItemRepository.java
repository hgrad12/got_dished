package com.example.gotdished.repository;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.gotdished.model.RecipeItem;
import com.example.gotdished.util.FirebaseUtil;
import com.example.gotdished.util.RecipeItemValues;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class RecipeItemRepository {
    private static final String TAG = RecipeItemRepository.class.getName();

    public RecipeItemRepository() {}

    public LiveData<List<RecipeItem>> getRecipeItems() {
        MutableLiveData<List<RecipeItem>> mutableRecipeItems = new MutableLiveData<>();
        List<RecipeItem> items = new ArrayList<>();

        FirebaseUtil.retrieveRecipeItemsCollection().limit(25).get().addOnCompleteListener(task -> {
           if (!task.isSuccessful())
               return;

           if (Objects.requireNonNull(task.getResult()).getDocuments().isEmpty())
               return;

           for (DocumentSnapshot snapshot: task.getResult().getDocuments()) {
               Long likes = snapshot.getLong(RecipeItemValues.NUMBER_OF_LIKES);
               RecipeItem item = new RecipeItem(snapshot.getString(RecipeItemValues.UUID),
                       snapshot.getString(RecipeItemValues.NAME),
                       snapshot.getString(RecipeItemValues.TIME_TO_COMPLETION),
                       snapshot.getString(RecipeItemValues.IMAGE_URI),
                       snapshot.getString(RecipeItemValues.CATEGORY), (likes == null)?0l:likes);

               items.add(item);
           }
           mutableRecipeItems.setValue(items);
        });

        return mutableRecipeItems;
    }
}
