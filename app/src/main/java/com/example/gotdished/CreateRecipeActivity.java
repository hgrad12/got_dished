package com.example.gotdished;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.gotdished.model.Recipe;
import com.google.firebase.Timestamp;

import mabbas007.tagsedittext.TagsEditText;

public class CreateRecipeActivity extends AppCompatActivity {
    private static final String TAG = "CreateRecipeActivity.class";

    private static final int GALLERY_CODE = 1;
    private ImageView recipeImage;
    private EditText name, timeToCompletion;
    private AutoCompleteTextView category;
    private TagsEditText equipment;

    private Recipe recipe;

    private Uri uri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_recipe);

        if (getIntent().getExtras() != null) {
            recipe = (Recipe) getIntent().getParcelableExtra("recipe");
        } else {
            recipe = new Recipe();
        }
        recipeImage = findViewById(R.id.create_recipe_image);
        findViewById(R.id.create_recipe_post_button).setOnClickListener(this::onPostImageClicked);
        name = findViewById(R.id.create_recipe_name);
        timeToCompletion = findViewById(R.id.create_recipe_ttc);
        category = findViewById(R.id.create_recipe_category);
        String[] categories = getResources().getStringArray(R.array.type_of_recipes);
        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, categories);
        category.setAdapter(categoryAdapter);
        equipment = findViewById(R.id.create_recipe_equipment);
        findViewById(R.id.create_recipe_next_button).setOnClickListener(this::onNextButtonClicked);
    }

    private void onNextButtonClicked(View view) {
        String nm = name.getText().toString().trim();
        String catg = category.getText().toString().trim();
        String ttc = timeToCompletion.getText().toString().trim();

        if (hasValidationErrors(nm, catg, ttc)) {
            return;
        }

        recipe.setName(nm);
        recipe.setCategory(catg);
        recipe.setTimeToCompletion(ttc);
        recipe.setEquipment(equipment.getTags());
        Timestamp timestamp = Timestamp.now();
        recipe.setDateModified(timestamp);
        recipe.setDateCreated(timestamp);

        Intent intent = new Intent(CreateRecipeActivity.this, IngredientsActivity.class);
        intent.putExtra("recipe", recipe);
        if (uri != null)
            intent.putExtra("imageUri", uri.toString());
        startActivity(intent);
        finish();
    }

    private boolean hasValidationErrors(String nm, String catg, String ttc){
        if (nm.isEmpty()) {
            name.setError("Name required");
            name.requestFocus();
            return true;
        }

        if (catg.isEmpty()) {
            category.setError("Category required");
            category.requestFocus();
            return true;
        }

        if (ttc.isEmpty()) {
            timeToCompletion.setError("Time required");
            timeToCompletion.requestFocus();
            return true;
        }
        return false;
    }

    private void onPostImageClicked(View view) {
        //get image from gallery
//        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
//        galleryIntent.setType("image/*");
//        startActivityForResult(galleryIntent, GALLERY_CODE);
        Intent intent;

        intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("*/*");
        startActivityForResult(intent, GALLERY_CODE);
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
}