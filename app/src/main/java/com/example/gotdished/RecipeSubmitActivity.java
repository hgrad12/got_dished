package com.example.gotdished;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gotdished.model.Recipe;
import com.example.gotdished.model.RecipeItem;
import com.example.gotdished.util.FirebaseUtil;
import com.example.gotdished.util.UserApi;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.storage.StorageReference;

import java.util.Map;

import static com.example.gotdished.util.RecipeUtil.ingredientsToString;
import static com.example.gotdished.util.RecipeUtil.listToString;
import static com.example.gotdished.util.RecipeUtil.stepsToString;

public class RecipeSubmitActivity extends AppCompatActivity {
    private static final String TAG = "RecipeSubmitActivity.class";
    private ProgressBar progressBar;
    private Recipe recipe;

    private UserApi api = new UserApi().getInstance();

    private Uri uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_submit);

        if (getIntent().getExtras() != null) {
            recipe = (Recipe) getIntent().getParcelableExtra("recipe");
            uri = Uri.parse(getIntent().getStringExtra("imageUri"));
        } else {
            recipe = new Recipe();
        }

        ((TextView)findViewById(R.id.recipe_submit_name)).setText(recipe.getName());

        ((TextView)findViewById(R.id.recipe_submit_category)).setText(recipe.getCategory());

        ((TextView)findViewById(R.id.recipe_submit_ttc)).setText(recipe.getTimeToCompletion());

        ((TextView)findViewById(R.id.recipe_submit_equipment)).setText(listToString(recipe.getEquipment()));

        ((TextView)findViewById(R.id.recipe_submit_ingredients)).setText(ingredientsToString(recipe.getIngredients()));

        ((TextView)findViewById(R.id.recipe_submit_steps)).setText(stepsToString(recipe.getSteps()));

        progressBar = findViewById(R.id.recipe_submit_progressBar);

        findViewById(R.id.recipe_submit_prev_button).setOnClickListener(this::prevButtonClicked);
        findViewById(R.id.recipe_submit_confirm_button).setOnClickListener(this::onConfirmationButtonClicked);
    }

    private void prevButtonClicked(View view) {
        Intent intent = new Intent(RecipeSubmitActivity.this, StepsActivity.class);
        intent.putExtra("recipe", recipe);
        intent.putExtra("imageUri", uri.toString());
        startActivity(intent);
        finish();
    }

    private void onConfirmationButtonClicked(View view) {

        DocumentReference recipeRef = FirebaseUtil.retrieveRecipesCollection().document();

        final StorageReference storage = FirebaseUtil.retrieveRecipeImageFolderStorageReference()
                .child(recipeRef.toString() + "_" + Timestamp.now().getSeconds());

        progressBar.setVisibility(View.VISIBLE);

        storage.putFile(uri).addOnSuccessListener(s ->
                storage.getDownloadUrl().addOnSuccessListener(success -> {

                    String imageUrl = success.toString();
                    String recipeUUid = recipeRef.getId();

                    Timestamp timestamp = Timestamp.now();

                    recipe.setDateCreated(timestamp);
                    recipe.setDateModified(timestamp);

                    recipe.setCreatedBy(api.getUsername());
                    recipe.setImageUri(imageUrl);
                    recipe.setRecipeUuid(recipeUUid);
                    recipe.setUserUuid(api.getUserId());

                    Map<String, Object> rec = recipe.toMap();

                    Map<String, Object> recipeItem = new RecipeItem(recipeUUid, recipe.getName(), recipe.getTimeToCompletion(),
                            imageUrl, recipe.getCategory(), 0l).toMap();

                    recipeRef.set(rec).addOnSuccessListener(aVoid ->
                            FirebaseUtil.retrieveRecipeItemsCollection().document()
                                    .set(recipeItem).addOnSuccessListener(aVoid1 -> {
                                progressBar.setVisibility(View.INVISIBLE);
                                finish();
                            }).addOnFailureListener(e -> {
                                Log.d(TAG, "RecipeItem (Object) was not added to Recipe Item collection.");
                                progressBar.setVisibility(View.INVISIBLE);
                            })).addOnFailureListener(e -> {
                        Log.d(TAG, "Failed to add Recipe (Object) to Recipe collection");
                        progressBar.setVisibility(View.INVISIBLE);
                    });
                })).addOnFailureListener(f -> {
            progressBar.setVisibility(View.INVISIBLE);
        });
    }
}