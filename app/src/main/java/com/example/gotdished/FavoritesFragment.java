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
import com.example.gotdished.util.FirebaseUtil;
import com.example.gotdished.util.RecipeItemValues;
import com.example.gotdished.util.UserApi;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;

public class FavoritesFragment extends Fragment implements RecipeItemRecyclerAdapter.OnRecipeItemClickListener {
    private static final String TAG = "FavoritesFragment";
//    private TextView noEntry;
    private RecyclerView recyclerView;
    private RecipeItemRecyclerAdapter adapter;
    private ArrayList<RecipeItem> listOfRecipeItems;
    private static final String FAVORITE_ITEMS = "favorite_items";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        listOfRecipeItems = new ArrayList<>();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (savedInstanceState != null) {
            listOfRecipeItems.addAll(savedInstanceState.getParcelableArrayList(FAVORITE_ITEMS));
        } else {
            FirebaseUtil.retrieveRecipeItemsCollection()
                    .whereEqualTo("user_uuid", new UserApi().getInstance().getUserId())
                    .limit(25)
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task == null) return;

                        if (!task.isSuccessful()) return;

                        if (task.getResult().getDocuments().isEmpty()) {
//                            noEntry.setVisibility(View.VISIBLE);
                            return;
                        }

                        for (DocumentSnapshot snapshot: task.getResult().getDocuments()) {
                            listOfRecipeItems.add(new RecipeItem(snapshot.getString(RecipeItemValues.UUID),
                                    snapshot.getString(RecipeItemValues.NAME),
                                    snapshot.getString(RecipeItemValues.TIME_TO_COMPLETION),
                                    snapshot.getString(RecipeItemValues.IMAGE_URI),
                                    snapshot.getString(RecipeItemValues.CATEGORY)));
                        }
                        adapter = new RecipeItemRecyclerAdapter(listOfRecipeItems, this);
                        recyclerView.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                    }).addOnFailureListener(error -> {
                Log.d(TAG, "Issue retrieving favorites list");
            });
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
