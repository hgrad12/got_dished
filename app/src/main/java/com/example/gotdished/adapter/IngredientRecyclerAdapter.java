package com.example.gotdished.adapter;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gotdished.R;

import java.util.List;

public class IngredientRecyclerAdapter extends RecyclerView.Adapter<IngredientRecyclerAdapter.ViewHolder>  {
    private static final String TAG = "IngredientRecyclerAdapter.class";
    private List<String> listOfIngredients;

    public IngredientRecyclerAdapter(List<String> listOfIngredients) {
        this.listOfIngredients = listOfIngredients;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.ingredient_row, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String ingredient = listOfIngredients.get(position);
        holder.name.setText(ingredient);

        holder.name.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {}

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                listOfIngredients.set(position, s.toString());
            }
        });
    }

    @Override
    public int getItemCount() {
        return listOfIngredients.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public EditText name;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.ingredient_name);
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
