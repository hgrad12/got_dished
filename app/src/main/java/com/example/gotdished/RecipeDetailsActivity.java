package com.example.gotdished;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.gotdished.model.Recipe;
import com.example.gotdished.model.Step;
import com.example.gotdished.util.FirebaseUtil;
import com.example.gotdished.util.RecipeValues;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.stream.Collectors;

public class RecipeDetailsActivity extends AppCompatActivity {
    private TextView category, createdBy, date, timeToCompletion, equipment, ingredients, steps;
    private ImageView image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_details);

        category = findViewById(R.id.recipe_details_category);
        createdBy = findViewById(R.id.recipe_details_created_by);
        date = findViewById(R.id.recipe_details_date);
        timeToCompletion = findViewById(R.id.recipe_details_ttc);
        equipment = findViewById(R.id.recipe_details_equipment);
        ingredients = findViewById(R.id.recipe_details_ingredients);
        steps = findViewById(R.id.recipe_details_steps);
        image = findViewById(R.id.recipe_details_image);

        String recipeUuid;
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                recipeUuid = null;
            } else {
                recipeUuid = extras.getString("recipeUuid");
            }
        } else {
            recipeUuid = (String) savedInstanceState.getSerializable("recipeUuid");
        }

        FirebaseUtil.retrieveRecipesCollection()
                .whereEqualTo(RecipeValues.RECIPE_UUID, recipeUuid).get()
                .addOnCompleteListener(task -> {
                    if (task == null) return;

                    if (!task.isSuccessful()) return;

                    if (task.getResult().getDocuments().isEmpty()) return;

                    for (DocumentSnapshot snapshot: task.getResult().getDocuments()) {
                        Recipe recipe = snapshot.toObject(Recipe.class);

                        category.setText(recipe.getCategory());
                        createdBy.setText(recipe.getCreatedBy());
                        date.setText(recipe.getDateCreated().toString());
                        timeToCompletion.setText(recipe.getTimeToCompletion());
                        equipment.setText(listToString(recipe.getEquipment()));
                        ingredients.setText(listToString(recipe.getIngredients()));
                        steps.setText(stepsToString(recipe.getSteps()));
                        Picasso.get().load(recipe.getImageUri())
                                .placeholder(R.drawable.dish_placeholder)
                                .error(R.drawable.dish_placeholder).into(image);
                    }
                });
    }

    private String listToString(List<String> list){
        if (list.isEmpty()) return "";

        return list.stream().collect(Collectors.joining(", "));
    }

    private String stepsToString(List<Step> steps) {
        if (steps.isEmpty()) return "";

        StringBuilder sb = new StringBuilder();
        sb.append(1 + ".\t").append(steps.get(0).getDetails());
        for (int x = 1; x < steps.size(); x++) {
            sb.append("\n\n").append(x + 1).append(".\t").append(steps.get(0).getDetails());
        }
        return sb.toString();
    }
}