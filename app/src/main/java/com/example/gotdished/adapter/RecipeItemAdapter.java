package com.example.gotdished.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gotdished.R;
import com.example.gotdished.RecipeDetailsActivity;
import com.example.gotdished.databinding.RecipesRow2Binding;
import com.example.gotdished.handler.CustomClickListener;
import com.example.gotdished.model.Favorite;
import com.example.gotdished.model.RecipeItem;
import com.example.gotdished.util.FirebaseUtil;
import com.example.gotdished.util.UserApi;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class RecipeItemAdapter extends RecyclerView.Adapter<RecipeItemAdapter.ViewHolder> implements CustomClickListener {
    private static final String TAG = RecipeItemAdapter.class.getName();
    private final Context context;
    private List<RecipeItem> listOfRecipeItems;
    private boolean favorites_flag;

    public RecipeItemAdapter(Context context, boolean favorites_flag) {
        this.context = context;
        this.favorites_flag = favorites_flag;
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        RecipesRow2Binding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.recipes_row2, parent,false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
        RecipeItem item = listOfRecipeItems.get(position);
        holder.binding.setItem(item);
        setLikes(holder.binding.getRoot(), position);
    }

    @Override
    public int getItemCount() {
        return (listOfRecipeItems == null)? 0 :listOfRecipeItems.size();
    }

    @Override
    public void onRecipeItemListener(RecipeItem item) {
        Intent intent = new Intent(context.getApplicationContext(), RecipeDetailsActivity.class);
        intent.putExtra("recipeUuid", item.getRecipeUuid());
        context.startActivity(intent);
    }

    public void setListOfRecipeItems(List<RecipeItem> items) {
        listOfRecipeItems = items;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final RecipesRow2Binding binding;
        public ViewHolder(@NonNull RecipesRow2Binding binding) {
            super(binding.getRoot());
            this.binding = binding;
            binding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onRecipeItemListener(binding.getItem());
                }
            });
        }
    }

    private void setLikes(View view, final int position) {
        String userId = new UserApi().getInstance().getUserId();

        final CollectionReference favorites = FirebaseUtil.retrieveFavoritesCollection();

        Query query = favorites.whereEqualTo("user_uuid",  userId)
                .whereEqualTo("recipe_uuid", listOfRecipeItems.get(position).getRecipeUuid());

        final ImageButton likeButton = view.findViewById(R.id.recipe_like_image);
        final TextView numOfLikes = view.findViewById(R.id.recipe_num_likes);

        final EventListener<QuerySnapshot> checkIfLiked = new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot documentSnapshot, FirebaseFirestoreException e) {

                assert documentSnapshot != null;
                if (!documentSnapshot.isEmpty()){
                    likeButton.setImageResource(R.drawable.ic_baseline_favorite_48_red);
                    numOfLikes.setTextColor(Color.RED);
                }else {
                    if (favorites_flag && !listOfRecipeItems.isEmpty()) {
                        notifyItemRemoved(position);
                        notifyItemRangeChanged(position, listOfRecipeItems.size());
                        listOfRecipeItems.remove(position);
                    }
                    else {

                        likeButton.setImageResource(R.drawable.ic_baseline_favorite_border_48);
                        numOfLikes.setTextColor(Color.GRAY);
                    }
                }

            }
        };

        final EventListener<QuerySnapshot> likeEvent = new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                assert documentSnapshots != null;
                String likes = String.valueOf(documentSnapshots.getDocuments().size());
                if (likes.equals("0")){
                    numOfLikes.setText("");
                }else {
                    numOfLikes.setText(likes);
                }
            }
        };

        favorites.addSnapshotListener(likeEvent);

        query.addSnapshotListener(checkIfLiked);

        likeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                query.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (queryDocumentSnapshots.isEmpty()) {
                            favorites.document().set(new Favorite(userId, Timestamp.now(), listOfRecipeItems.get(position)).toMap());
                            return;
                        }

                        for (QueryDocumentSnapshot snapshot: queryDocumentSnapshots) {
                            snapshot.getReference().delete();
                        }
                    }
                }).addOnFailureListener(error ->
                        Toast.makeText(context, "Error occured", Toast.LENGTH_SHORT).show());
            }
        });

    }
}
