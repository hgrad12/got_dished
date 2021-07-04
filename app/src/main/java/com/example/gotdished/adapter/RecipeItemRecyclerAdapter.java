package com.example.gotdished.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gotdished.R;
import com.example.gotdished.model.Favorite;
import com.example.gotdished.model.RecipeItem;
import com.example.gotdished.util.FavoriteValues;
import com.example.gotdished.util.FirebaseUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RecipeItemRecyclerAdapter extends RecyclerView.Adapter<RecipeItemRecyclerAdapter.ViewHolder> {
    private static final String TAG = RecipeItemRecyclerAdapter.class.getName();
    private final List<RecipeItem> listOfRecipes;
    private OnRecipeItemClickListener onClickListener;
    private final Context context;
    private final String userId;

    public RecipeItemRecyclerAdapter(Context context, List<RecipeItem> listOfRecipes, String userId,
                                     OnRecipeItemClickListener onRecipeItemClickListener) {
        this.context = context;
        this.listOfRecipes = listOfRecipes;
        this.userId = userId;
        onClickListener = onRecipeItemClickListener;
    }

    @NonNull
    @Override
    public RecipeItemRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recipes_row, parent, false);
        return new ViewHolder(view, onClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        RecipeItem recipeItem = listOfRecipes.get(position);
        String imageUri;
        holder.name.setText(recipeItem.getName());
        holder.ttc.setText(recipeItem.getTimeToCompletion());
        holder.category.setText(recipeItem.getCategory());

        imageUri = recipeItem.getImageUri();

        Picasso.get().load(imageUri).placeholder(R.drawable.dish_placeholder).fit().into(holder.image);

        //Todo: add more details to the recipe card view
        setLikes(holder.view, position);
    }

    @Override
    public int getItemCount() {
        return listOfRecipes.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        View view;
        public TextView name, ttc, category;
        public ImageView image;
        OnRecipeItemClickListener onRecipeItemClickListener;
        public ViewHolder(@NonNull View itemView, OnRecipeItemClickListener onRecipeItemClickListener) {
            super(itemView);
            view = itemView;
            name = itemView.findViewById(R.id.recipe_name);
            ttc = itemView.findViewById(R.id.recipe_ttc);
            category = itemView.findViewById(R.id.recipe_category);
            image = itemView.findViewById(R.id.recipe_image);

            this.onRecipeItemClickListener = onRecipeItemClickListener;
            itemView.setOnClickListener(this);
        }

        @SuppressLint("NonConstantResourceId")
        @Override
        public void onClick(View v) {
            onRecipeItemClickListener.onRecipeItemClick(getAdapterPosition());
        }
    }

    private void setLikes(View view, final int position) {

        final CollectionReference favorites = FirebaseUtil.retrieveFavoritesCollection();

        Query query = favorites.whereEqualTo("user_uuid",  userId)
                .whereEqualTo("recipe_uuid", listOfRecipes.get(position).getRecipeUuid());

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
                    likeButton.setImageResource(R.drawable.ic_baseline_favorite_border_48);
                    numOfLikes.setTextColor(Color.GRAY);
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
                            favorites.document().set(new Favorite(userId, listOfRecipes.get(position)).toMap());
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

    public interface OnRecipeItemClickListener{
        void onRecipeItemClick(int position);
    }
}
