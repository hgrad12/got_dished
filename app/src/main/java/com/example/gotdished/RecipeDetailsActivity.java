package com.example.gotdished;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.gotdished.model.Recipe;
import com.example.gotdished.util.DateFormatter;
import com.example.gotdished.util.FirebaseUtil;
import com.example.gotdished.util.RecipeValues;
import com.google.firebase.firestore.DocumentSnapshot;
import com.squareup.picasso.Picasso;

import java.text.MessageFormat;

import static com.example.gotdished.util.RecipeUtil.ingredientsToString;
import static com.example.gotdished.util.RecipeUtil.listToString;
import static com.example.gotdished.util.RecipeUtil.stepsToString;

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
                        createdBy.setText(MessageFormat.format("Created By: {0}", recipe.getCreatedBy()));
                        date.setText(DateFormatter.getTimeAgo(recipe.getDateCreated()));
                        timeToCompletion.setText(recipe.getTimeToCompletion());
                        equipment.setText(listToString(recipe.getEquipment()));
                        ingredients.setText(ingredientsToString(recipe.getIngredients()));
                        steps.setText(stepsToString(recipe.getSteps()));
                        Picasso.get().load(recipe.getImageUri())
                                .placeholder(R.drawable.dish_placeholder)
                                .error(R.drawable.dish_placeholder).into(image);
                    }
                });
    }

}