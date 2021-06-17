package com.example.gotdished.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gotdished.R;
import com.example.gotdished.model.Ingredient;

import java.text.MessageFormat;
import java.util.List;

public class IngredientRecyclerAdapter extends RecyclerView.Adapter<IngredientRecyclerAdapter.ViewHolder>  {
    private static final String TAG = "IngredientRecyclerAdapter.class";
    private final List<Ingredient> listOfIngredients;

    public IngredientRecyclerAdapter(List<Ingredient> listOfIngredients) {
        this.listOfIngredients = listOfIngredients;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.ingredient_row, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Ingredient ingredient = listOfIngredients.get(position);
        holder.name.setText(ingredient.getName());
        holder.quantity.setText(MessageFormat.format("{0} {1}", ingredient.getQuantity(), ingredient.getMeasurement()));
    }

    @Override
    public int getItemCount() {
        return listOfIngredients.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView name, quantity;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.ingredient_name);
            quantity = itemView.findViewById(R.id.ingredient_quantity);
            itemView.findViewById(R.id.ingredient_remove).setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            removeAt(getAdapterPosition());
        }
    }

    public void removeAt(int position) {
        Log.d(TAG, "Removing Ingredient " + (position + 1));
        listOfIngredients.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, listOfIngredients.size());
        notifyDataSetChanged();
    }
}
