package com.example.gotdished;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gotdished.adapter.StepRecyclerAdapter;
import com.example.gotdished.model.Recipe;
import com.example.gotdished.model.Step;
import com.google.android.material.snackbar.Snackbar;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
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

        listOfSteps = new ArrayList<>();

        if (getIntent().getExtras() != null) {
            recipe = (Recipe) getIntent().getParcelableExtra("recipe");
            uri = getIntent().getStringExtra("imageUri");
            listOfSteps.addAll(recipe.getSteps());
        } else {
            recipe = new Recipe();
        }

        RecyclerView recyclerView = findViewById(R.id.stepsRecyclerView);
        adapter = new StepRecyclerAdapter(listOfSteps, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setAdapter(adapter);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);

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

        new Handler().postDelayed(() -> {
            dialog.dismiss();
            adapter.notifyDataSetChanged();
        }, 120);
    }

    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.START | ItemTouchHelper.END, 0) {
        @Override
        public boolean onMove(@NonNull @NotNull RecyclerView recyclerView,
                              @NonNull @NotNull RecyclerView.ViewHolder viewHolder,
                              @NonNull @NotNull RecyclerView.ViewHolder target) {
            int fromPosition = viewHolder.getAdapterPosition();
            int toPosition = target.getAdapterPosition();

            ((TextView)viewHolder.itemView.findViewById(R.id.step_row_number)).setText(String.valueOf(toPosition + 1));
            ((TextView)target.itemView.findViewById(R.id.step_row_number)).setText(String.valueOf(fromPosition + 1));

            Collections.swap(listOfSteps, fromPosition, toPosition);
            recyclerView.getAdapter().notifyItemMoved(fromPosition, toPosition);
            return false;
        }

        @Override
        public void onSwiped(@NonNull @NotNull RecyclerView.ViewHolder viewHolder, int direction) {

        }
    };
}