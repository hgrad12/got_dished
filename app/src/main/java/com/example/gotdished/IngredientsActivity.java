package com.example.gotdished;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.gotdished.adapter.IngredientRecyclerAdapter;
import com.example.gotdished.model.Ingredient;
import com.example.gotdished.model.Recipe;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class IngredientsActivity extends AppCompatActivity {
    private AlertDialog dialog;

    private EditText name, quantity, measurement;

    private List<Ingredient> listOfIngredients;

    private IngredientRecyclerAdapter adapter;
    private Recipe recipe;
    private String uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingredients);

        //todo: retrieve the Recipe object from the bundle
        if (getIntent().getExtras() != null) {
            recipe = (Recipe) getIntent().getParcelableExtra("recipe");
            uri = getIntent().getStringExtra("imageUri");
        } else {
            recipe = new Recipe();
        }

        listOfIngredients = new ArrayList<>();

        findViewById(R.id.ingredientsFloatingActionButton).setOnClickListener(this::addAnIngredient);

        RecyclerView recyclerView = findViewById(R.id.ingredientsRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setHasFixedSize(true);
        adapter = new IngredientRecyclerAdapter(listOfIngredients);
        recyclerView.setAdapter(adapter);

        findViewById(R.id.ingredient_prev_button).setOnClickListener(this::prevButtonClicked);

        findViewById(R.id.ingredient_next_button).setOnClickListener(this::nextButtonClicked);
    }

    private void nextButtonClicked(View view) {
        recipe.setIngredients(listOfIngredients);

        Intent intent = new Intent(IngredientsActivity.this, StepsActivity.class);
        intent.putExtra("recipe", recipe);
        intent.putExtra("imageUri", uri);
        startActivity(intent);
        finish();
    }

    private void prevButtonClicked(View view) {
        recipe.setIngredients(listOfIngredients);

        Intent intent = new Intent(IngredientsActivity.this, CreateRecipeActivity.class);
        intent.putExtra("recipe", recipe);
        intent.putExtra("imageUri", uri);
        startActivity(intent);
        finish();
    }

    private void addAnIngredient(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View v = getLayoutInflater().inflate(R.layout.ingredients_popup, null);

        name = v.findViewById(R.id.ingredients_popup_name);
        quantity = v.findViewById(R.id.ingredients_popup_quantity);
        measurement = v.findViewById(R.id.ingredients_popup_measurement);

        v.findViewById(R.id.ingredients_popup_save_button).setOnClickListener(this::onSaveClicked);

        builder.setView(v);
        dialog = builder.create();
        dialog.show();
    }

    private void onSaveClicked(View view) {
        String nm = name.getText().toString().trim();
        String qty = quantity.getText().toString().trim();
        String measure = measurement.getText().toString().trim();

        if (hasValidationErrors(nm, qty, measure)) {
            return;
        }

        double quantityValue = Double.parseDouble(qty);

        Ingredient ingredient = new Ingredient(nm, quantityValue, measure);
        listOfIngredients.add(ingredient);

        Snackbar.make(view, "Item was added!", Snackbar.LENGTH_SHORT).show();

        new Handler().postDelayed(() -> {
            adapter.notifyDataSetChanged();
            dialog.dismiss();
        }, 120);
    }

    private boolean hasValidationErrors(String nm, String quan, String measure){
        if (nm.isEmpty()) {
            name.setError("Name required");
            name.requestFocus();
            return true;
        }

        if (quan.isEmpty()) {
            quantity.setError("Password required");
            quantity.requestFocus();
            return true;
        }

        if (measure.isEmpty()) {
            measurement.setError("Password required");
            measurement.requestFocus();
            return true;
        }
        return false;
    }

}