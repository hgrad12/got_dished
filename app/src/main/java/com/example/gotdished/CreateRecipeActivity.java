package com.example.gotdished;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.gotdished.model.Recipe;
import com.example.gotdished.model.RecipeItem;
import com.example.gotdished.model.Step;
import com.example.gotdished.util.FirebaseUtil;
import com.example.gotdished.util.UserApi;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import mabbas007.tagsedittext.TagsEditText;

public class CreateRecipeActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "CreateRecipeActivity.class";

    private static final int GALLERY_CODE = 1;
    private ImageView recipeImage;
    private EditText name, timeTiCompletion;
    private AutoCompleteTextView category;
    private TagsEditText equipment;
    private ProgressBar progressBar;
    private List<String> listOfSteps;
    private List<String> listOfIngredients;

    private UserApi api = new UserApi().getInstance();

    private Uri uri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_recipe);

        recipeImage = findViewById(R.id.create_recipe_image);
        findViewById(R.id.create_recipe_post_button).setOnClickListener(this);
        name = findViewById(R.id.create_recipe_name);
        timeTiCompletion = findViewById(R.id.create_recipe_ttc);
        category = findViewById(R.id.create_recipe_category);
        String[] categories = getResources().getStringArray(R.array.type_of_recipes);
        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, categories);
        category.setAdapter(categoryAdapter);
        equipment = findViewById(R.id.create_recipe_equipment);
        findViewById(R.id.create_recipe_add_steps).setOnClickListener(this);
        findViewById(R.id.create_recipe_add_button).setOnClickListener(this);
        findViewById(R.id.create_recipe_ingredients).setOnClickListener(this);
        progressBar = findViewById(R.id.create_recipe_progressBar);
        listOfSteps = new ArrayList<>();
        listOfIngredients = new ArrayList<>();
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.create_recipe_add_button:
                onAddRecipeClicked();
                break;
            case R.id.create_recipe_add_steps:
                onAddStepsClicked();
                break;
            case R.id.create_recipe_post_button:
                onPostImageClicked();
                break;
            case R.id.create_recipe_ingredients:
                onIngredientsButtonClicked();
            default:
        }
    }

    private void onIngredientsButtonClicked() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.ingredientsContainer, IngredientsFragment.newInstance(listOfIngredients)).commit();
    }

    private void onPostImageClicked() {
        //get image from gallery
        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, GALLERY_CODE);
    }

    private void onAddRecipeClicked() {
        String nm = name.getText().toString().trim();
        String catg = category.getText().toString();
        String time = timeTiCompletion.getText().toString();

        if (TextUtils.isEmpty(nm) || TextUtils.isEmpty(catg) || TextUtils.isEmpty(time)
                || listOfSteps.isEmpty() || uri == null || listOfIngredients.isEmpty()) {
            Toast.makeText(this, "A field is empty", Toast.LENGTH_SHORT).show();
            return;
        }
        DocumentReference recipeRef = FirebaseUtil.retrieveRecipesCollection().document();

        final StorageReference storage = FirebaseUtil.retrieveRecipeImageFolderStorageReference()
                .child(recipeRef.toString() + "_" + Timestamp.now().getSeconds());

        progressBar.setVisibility(View.VISIBLE);

        storage.putFile(uri).addOnSuccessListener(s ->
                storage.getDownloadUrl().addOnSuccessListener(success -> {

            String imageUrl = success.toString();
            String recipeUUid = recipeRef.getId();

            Timestamp timestamp = Timestamp.now();

            Map<String, Object> recipe = new Recipe(nm, imageUrl, recipeUUid, api.getUserId(), api.getUsername(), convertStringToSteps(),
                    equipment.getTags(), listOfIngredients, category.toString().trim(), timeTiCompletion.toString(), timestamp, timestamp).toMap();

                    Map<String, Object> recipeItem = new RecipeItem(recipeUUid, nm, timeTiCompletion.getText().toString().trim(),
                    imageUrl, category.getText().toString().trim()).toMap();

            recipeRef.set(recipe).addOnSuccessListener(aVoid ->
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

    private List<Step> convertStringToSteps() {
        List<Step> steps = new ArrayList<>();
        for (int x = 0; x < listOfSteps.size(); x++) {
            steps.add(new Step(x + 1, listOfSteps.get(x)));
        }
        return steps;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_CODE && resultCode == RESULT_OK) {
            if (data != null) {
                uri = data.getData();
                recipeImage.setImageURI(uri);
            }
        }
    }

    private void onAddStepsClicked() {
//        getSupportFragmentManager().beginTransaction()
//                .add(R.id.stepsContainer, StepsFragment.newInstance(listOfSteps)).commit();

        if (getSupportFragmentManager().findFragmentById(R.id.stepsContainer)==null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.stepsContainer, new StepsFragment(listOfSteps))
                    .commit();
        }
    }
}