package com.example.gotdished;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.gotdished.adapter.StepRecyclerAdapter;
import com.example.gotdished.model.Ingredient;
import com.example.gotdished.model.Recipe;
import com.example.gotdished.model.Step;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class StepsActivity extends AppCompatActivity {
    private AlertDialog dialog;

    private EditText description;

    private List<Step> listOfSteps;

    private StepRecyclerAdapter adapter;

    private Recipe recipe;
    private String uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_steps);

        if (getIntent().getExtras() != null) {
            recipe = (Recipe) getIntent().getParcelableExtra("recipe");
            uri = getIntent().getStringExtra("imageUri");
        } else {
            recipe = new Recipe();
        }

        listOfSteps = new ArrayList<>();
        RecyclerView recyclerView = findViewById(R.id.stepsRecyclerView);
        adapter = new StepRecyclerAdapter(listOfSteps);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setAdapter(adapter);

        findViewById(R.id.stepsFloatingActionButton).setOnClickListener(this::addAStep);

        findViewById(R.id.steps_prev_button).setOnClickListener(this::prevButtonClicked);

        findViewById(R.id.steps_next_button).setOnClickListener(this::nextButtonClicked);
    }

    private void nextButtonClicked(View view) {
        recipe.setSteps(listOfSteps);

        Intent intent = new Intent(StepsActivity.this, RecipeSubmitActivity.class);
        intent.putExtra("recipe", recipe);
        intent.putExtra("imageUri", uri);
        startActivity(intent);
        finish();
    }

    private void prevButtonClicked(View view) {
        recipe.setSteps(listOfSteps);

        Intent intent = new Intent(StepsActivity.this, IngredientsActivity.class);
        intent.putExtra("recipe", recipe);
        intent.putExtra("imageUri", uri);
        startActivity(intent);
        finish();
    }

    private void addAStep(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View v = getLayoutInflater().inflate(R.layout.steps_popup, null);

        description = v.findViewById(R.id.steps_popup_description);

        v.findViewById(R.id.steps_popup_save_button).setOnClickListener(this::onSaveClicked);

        builder.setView(v);
        dialog = builder.create();
        dialog.show();
    }

    private void onSaveClicked(View view) {
        String d = description.getText().toString().trim();

        if (d.isEmpty()) {
            description.setError("Name required");
            description.requestFocus();
            return;
        }

        listOfSteps.add(new Step(d));
        Snackbar.make(view, "Step was added!", Snackbar.LENGTH_SHORT).show();

        new Handler().postDelayed(() -> {
            dialog.dismiss();
            adapter.notifyDataSetChanged();
        }, 120);
    }
}