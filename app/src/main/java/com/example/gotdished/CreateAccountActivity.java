package com.example.gotdished;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.gotdished.util.FirebaseUtil;
import com.example.gotdished.util.UserApi;
import com.example.gotdished.util.UserValues;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.storage.StorageReference;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class CreateAccountActivity extends AppCompatActivity implements View.OnClickListener {
    private static final int GALLERY_CODE = 1;
    private EditText usernameTextView, passwordTextView;
    private AutoCompleteTextView emailTextView;
    private ProgressBar progressBar;
    private ImageView profileImage;

    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener listener;
    private FirebaseUser currentUser;

    private Uri uri;

    private UserApi api = new UserApi().getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        Objects.requireNonNull(getSupportActionBar()).setElevation(0);

        firebaseAuth = FirebaseAuth.getInstance();

        usernameTextView = findViewById(R.id.create_acct_user_name);
        passwordTextView = findViewById(R.id.create_acct_password);
        emailTextView = findViewById(R.id.create_acct_email);
        progressBar = findViewById(R.id.create_acct_progress_bar);
        findViewById(R.id.create_acct_button).setOnClickListener(this);

        findViewById(R.id.create_acct_post_image).setOnClickListener(this);
        profileImage = findViewById(R.id.create_acct_image);

    }

    private boolean hasValidationErrors(String username, String email, String password) {
        if (username.isEmpty()) {
            usernameTextView.setError("Username required");
            usernameTextView.requestFocus();
            return true;
        }

        if (email.isEmpty()) {
            emailTextView.setError("Email required");
            emailTextView.requestFocus();
            return true;
        }

        if (password.isEmpty()) {
            passwordTextView.setError("Password required");
            passwordTextView.requestFocus();
            return true;
        }

        return false;
    }

    private void createUserAccount() {
        String username = usernameTextView.getText().toString();
        String email = emailTextView.getText().toString();
        String password = passwordTextView.getText().toString();

        if (hasValidationErrors(username, email, password)) return;

        progressBar.setVisibility(View.VISIBLE);

        firebaseAuth.createUserWithEmailAndPassword(email, password)
        .addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                currentUser = firebaseAuth.getCurrentUser();

                assert currentUser != null;
                String currentUserId = currentUser.getUid();

                StorageReference storageReference = FirebaseUtil.retrieveUserImageFolderStorageReference()
                        .child(currentUserId + "_" + Timestamp.now().getSeconds());

                Map<String, Object> userObj = new HashMap<>();
                userObj.put(UserValues.USER_ID, currentUserId);
                userObj.put(UserValues.USERNAME, username);
                userObj.put(UserValues.EMAIL, email);
                Date date = new Date();
                userObj.put(UserValues.CREATED_DATE, new Timestamp(date));
                userObj.put(UserValues.UPDATED_DATE, new Timestamp(date));
                userObj.put(UserValues.FAVORITES, Collections.emptyList());
                userObj.put(UserValues.RECIPES, Collections.emptyList());
                userObj.put(UserValues.IMAGE_URI, (uri == null)? "":storageReference.toString());

                storageReference.putFile(uri).addOnSuccessListener(taskSnapshot -> {

                }).addOnFailureListener(error -> {
                            Log.e("LoginActivity.class", "Picture not stored in storage");
                        });

                FirebaseUtil.retrieveUsersCollection()
                        .add(userObj).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        progressBar.setVisibility(View.INVISIBLE);
                        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                api.setUsername(documentSnapshot.getString(UserValues.USERNAME));
                                api.setUserId(currentUserId);
                                api.setEmail(documentSnapshot.getString(UserValues.EMAIL));
                                api.setCreateDate(documentSnapshot.getDate(UserValues.CREATED_DATE));
                                api.setUpdatedDate(documentSnapshot.getDate(UserValues.UPDATED_DATE));
                                api.setImageUrl(documentSnapshot.getString(UserValues.IMAGE_URI));

                                List<String> recipes = (List<String>) documentSnapshot.get(UserValues.RECIPES);
                                api.setRecipes(recipes);
                                List<String> favorites = (List<String>) documentSnapshot.get(UserValues.FAVORITES);
                                api.setFavorites(favorites);

                                Toast.makeText(CreateAccountActivity.this, "Successfully signed in", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(CreateAccountActivity.this, RecipesActivity.class));
                                finish();
                            }
                        }).addOnFailureListener(error ->
                                Log.e("CreateAccountActivity.class", "Failed to retrieve user data."));

                    }
                }).addOnFailureListener(error -> {
                    Log.e("CreateAccountActivity.class", "Failed to retrieve user data.");
                });

            } else {
                progressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(CreateAccountActivity.this, "Issue signing in", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(error -> {
            progressBar.setVisibility(View.INVISIBLE);
            Log.d("CreateAccountActivity.class", "Error occurred creating the account");
        });
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.create_acct_post_image:
                Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, GALLERY_CODE);
                break;
            case R.id.create_acct_button:
                createUserAccount();
                break;
            default:
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_CODE && resultCode == RESULT_OK) {
            if (data != null) {
                uri = data.getData();
                profileImage.setImageURI(uri);
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        currentUser = firebaseAuth.getCurrentUser();
    }
}