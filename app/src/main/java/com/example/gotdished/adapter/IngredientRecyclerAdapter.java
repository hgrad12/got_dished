package com.example.gotdished.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gotdished.R;
import com.example.gotdished.model.Ingredient;

import java.text.MessageFormat;
import java.util.List;

public class IngredientRecyclerAdapter extends RecyclerView.Adapter<IngredientRecyclerAdapter.ViewHolder>  {
    private static final String TAG = "IngredientRecyclerAdapter.class";
    private final List<Ingredient> listOfIngredients;
    private final Context context;
    private AlertDialog dialog;

    public IngredientRecyclerAdapter(List<Ingredient> listOfIngredients, Context context) {
        this.listOfIngredients = listOfIngredients;
        this.context = context;
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
            itemView.findViewById(R.id.ingredient_edit).setOnClickListener(this);
        }

        @SuppressLint("NonConstantResourceId")
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.ingredient_edit:
                    editAt(getAdapterPosition());
                    break;
                case R.id.ingredient_remove:
                    removeAt(getAdapterPosition());
            }
        }
    }

    @SuppressLint("SetTextI18n")
    public void editAt(int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View v = LayoutInflater.from(context).inflate(R.layout.ingredients_popup, null);
        Ingredient ingredient = listOfIngredients.get(position);
        ((EditText)v.findViewById(R.id.ingredients_popup_name)).setText(ingredient.getName());
        ((EditText)v.findViewById(R.id.ingredients_popup_quantity)).setText(String.valueOf(ingredient.getQuantity()));
        ((EditText)v.findViewById(R.id.ingredients_popup_measurement)).setText(ingredient.getMeasurement());
        Button update = v.findViewById(R.id.ingredients_popup_save_button);
        update.setText("Update");
        update.setOnClickListener(e -> onEditClicked(v, position));
        builder.setView(v);
        dialog = builder.create();
        dialog.show();
    }

    private void onEditClicked(View view, int position) {
        TextView nm = view.findViewById(R.id.ingredients_popup_name);
        TextView qty = view.findViewById(R.id.ingredients_popup_quantity);
        TextView measure = view.findViewById(R.id.ingredients_popup_measurement);

        if (hasValidationErrors(nm, qty, measure)) {
            return;
        }

        double quantityValue = Double.parseDouble(qty.getText().toString().trim());

        Ingredient ingredient = listOfIngredients.get(position);

        ingredient.setName(nm.getText().toString().trim());
        ingredient.setMeasurement(measure.getText().toString().trim());
        ingredient.setQuantity(quantityValue);

        new Handler().postDelayed(() -> {
            notifyItemChanged(position);
            dialog.dismiss();
        }, 120);
    }

    private boolean hasValidationErrors(TextView name, TextView quantity, TextView measurement){
        if (name.getText().toString().isEmpty()) {
            name.setError("Name required");
            name.requestFocus();
            return true;
        }

        if (quantity.getText().toString().trim().isEmpty()) {
            quantity.setError("Password required");
            quantity.requestFocus();
            return true;
        }

        if (measurement.getText().toString().trim().isEmpty()) {
            measurement.setError("Password required");
            measurement.requestFocus();
            return true;
        }
        return false;
    }

    public void removeAt(int position) {
        listOfIngredients.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, listOfIngredients.size());
        notifyDataSetChanged();
    }
}
