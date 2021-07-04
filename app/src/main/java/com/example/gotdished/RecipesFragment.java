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

import com.example.gotdished.adapter.RecipeItemRecyclerAdapter;
import com.example.gotdished.model.RecipeItem;
import com.example.gotdished.util.FirebaseUtil;
import com.example.gotdished.util.RecipeItemValues;
import com.example.gotdished.util.UserApi;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.Objects;

public class RecipesFragment extends Fragment implements RecipeItemRecyclerAdapter.OnRecipeItemClickListener {
    private static final String TAG = "RecipesFragment";
//    private TextView noEntry;
    private RecyclerView recyclerView;
    private RecipeItemRecyclerAdapter adapter;
    private ArrayList<RecipeItem> listOfRecipeItems;
    private static final String RECIPE_ITEMS = "recipe_items";
    private final UserApi api = new UserApi().getInstance();

    public RecipesFragment(){}

    public static RecipesFragment newInstance() {
        return new RecipesFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        listOfRecipeItems = new ArrayList<>();
    }

    @Override
    public void onResume() {
        super.onResume();

        FirebaseUtil.retrieveRecipeItemsCollection().limit(25)
                .get().addOnCompleteListener(task -> {
            if (task == null) return;

            if (!task.isSuccessful()) return;

            if (Objects.requireNonNull(task.getResult()).getDocuments().isEmpty()) {
//                            noEntry.setVisibility(View.VISIBLE);
                return;
            }

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
            adapter = new RecipeItemRecyclerAdapter(getContext(), listOfRecipeItems, api.getUserId(), this);
            recyclerView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }).addOnFailureListener(e -> Log.d(TAG, "issue retrieving recipe items."));

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (savedInstanceState != null) {
            listOfRecipeItems.addAll(savedInstanceState.getParcelableArrayList(RECIPE_ITEMS));
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(RECIPE_ITEMS, listOfRecipeItems);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.dishes_recipes, container, false);
        recyclerView = view.findViewById(R.id.dishesRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
//        noEntry = view.findViewById(R.id.emptyDishesRecyclerView);
        return view;
    }

    @Override
    public void onRecipeItemClick(int position) {
        Intent intent = new Intent(getActivity(), RecipeDetailsActivity.class);
        intent.putExtra("recipeUuid", listOfRecipeItems.get(position).getRecipeUuid());
        startActivity(intent);
    }
}
