package com.example.gotdished.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gotdished.R;
import com.example.gotdished.model.RecipeItem;
import com.squareup.picasso.Picasso;

import java.util.List;

public class RecipeItemRecyclerAdapter extends RecyclerView.Adapter<RecipeItemRecyclerAdapter.ViewHolder> {
    private final List<RecipeItem> listOfRecipes;
    private final OnRecipeItemClickListener onClickListener;

    public RecipeItemRecyclerAdapter(List<RecipeItem> listOfRecipes, OnRecipeItemClickListener onRecipeItemClickListener) {
        this.listOfRecipes = listOfRecipes;
        onClickListener = onRecipeItemClickListener;
    }

    @NonNull
    @Override
    public RecipeItemRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recipes_row, parent, false);
        return new ViewHolder(view);
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
    }

    @Override
    public int getItemCount() {
        return listOfRecipes.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView name, ttc, category;
        public ImageView image;
        OnRecipeItemClickListener onRecipeItemClickListener;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.recipe_name);
            ttc = itemView.findViewById(R.id.recipe_ttc);
            category = itemView.findViewById(R.id.recipe_category);
            image = itemView.findViewById(R.id.recipe_image);

            this.onRecipeItemClickListener = onClickListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            RecipeItem item = listOfRecipes.get(getAdapterPosition());
            onRecipeItemClickListener.onRecipeItemClicked(item);
        }
    }
}
