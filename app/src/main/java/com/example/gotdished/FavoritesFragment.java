package com.example.gotdished;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gotdished.model.RecipeItem;
import com.example.gotdished.adapter.RecipeItemRecyclerAdapter;
import com.example.gotdished.util.FavoriteValues;
import com.example.gotdished.util.FirebaseUtil;
import com.example.gotdished.util.RecipeItemValues;
import com.example.gotdished.util.UserApi;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.Objects;

public class FavoritesFragment extends Fragment implements RecipeItemRecyclerAdapter.OnRecipeItemClickListener {
    private static final String TAG = FavoritesFragment.class.getSimpleName();
//    private TextView noEntry;
    private RecyclerView recyclerView;
    private RecipeItemRecyclerAdapter adapter;
    private ArrayList<RecipeItem> listOfRecipeItems;
    private static final String FAVORITE_ITEMS = "favorite_items";
    private String userId = new UserApi().getInstance().getUserId();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        listOfRecipeItems = new ArrayList<>();
    }

    @Override
    public void onResume() {
        super.onResume();

        FirebaseUtil.retrieveFavoritesCollection()
                .whereEqualTo(FavoriteValues.USER_UUID, userId)
                .limit(25)
                .orderBy(FavoriteValues.DATE_CREATED, Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) return;
                    Log.d(TAG, "Favorites was successful");

                    if (Objects.requireNonNull(task.getResult()).getDocuments().isEmpty()) {
//                            noEntry.setVisibility(View.VISIBLE);
                        return;
                    }

                    Log.d(TAG, "Conditions were met");

                    for (DocumentSnapshot snapshot: task.getResult().getDocuments()) {
                        Long likes = snapshot.getLong(RecipeItemValues.NUMBER_OF_LIKES);
                        RecipeItem item = new RecipeItem(snapshot.getString(RecipeItemValues.UUID),
                                snapshot.getString(RecipeItemValues.NAME),
                                snapshot.getString(RecipeItemValues.TIME_TO_COMPLETION),
                                snapshot.getString(RecipeItemValues.IMAGE_URI),
                                snapshot.getString(RecipeItemValues.CATEGORY), (likes == null)?0l:likes);
                        if (listOfRecipeItems.contains(item))
                            continue;
                        listOfRecipeItems.add(item);
                    }
                    adapter = new RecipeItemRecyclerAdapter(getContext(), listOfRecipeItems, userId, this);
                    recyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                }).addOnFailureListener(error -> {
            Log.d(TAG, "Issue retrieving favorites list");
        });
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (savedInstanceState != null) {
            listOfRecipeItems.addAll(savedInstanceState.getParcelableArrayList(FAVORITE_ITEMS));
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(FAVORITE_ITEMS, listOfRecipeItems);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.favorites_recipes, container, false);
        recyclerView = view.findViewById(R.id.favoritesRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
//        noEntry = view.findViewById(R.id.emptyFavoritesRecyclerView);
        return view;
    }

    @Override
    public void onRecipeItemClick(int position) {
        Intent intent = new Intent(getActivity(), RecipeDetailsActivity.class);
        intent.putExtra("recipeUuid", listOfRecipeItems.get(position));
        startActivity(intent);
    }
}
