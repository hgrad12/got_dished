package com.example.gotdished;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.gotdished.util.FirebaseUtil;
import com.example.gotdished.util.UserApi;
import com.example.gotdished.util.UserValues;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;
import java.util.Objects;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private Button createAccountButton, loginButton;
    private AutoCompleteTextView emailTextView;
    private EditText passwordTextView;
    private ProgressBar progressBar;

    private final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Objects.requireNonNull(getSupportActionBar()).setElevation(0);

        createAccountButton = findViewById(R.id.login_create_acct_button);
        createAccountButton.setOnClickListener(this);

        loginButton = findViewById(R.id.login_button);
        loginButton.setOnClickListener(this);

        emailTextView = findViewById(R.id.login_email);
        passwordTextView = findViewById(R.id.login_password);
        progressBar = findViewById(R.id.login_progress);
    }

    private void createUserAccount() {
        startActivity(new Intent(LoginActivity.this, CreateAccountActivity.class));
    }

    private boolean hasValidationErrors(String email, String password){
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

    private void loginWithEmailPassword() {
        String email = emailTextView.getText().toString();
        String password = passwordTextView.getText().toString();

        if (hasValidationErrors(email, password)) return;

        progressBar.setVisibility(View.VISIBLE);

        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                FirebaseUser user = firebaseAuth.getCurrentUser();

                if (user == null) return;

                String uuid = user.getUid();

                FirebaseUtil.retrieveUsersCollection().whereEqualTo(UserValues.USER_ID, uuid)
                        .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        progressBar.setVisibility(View.INVISIBLE);

                        if (error != null) return;

                        if (value == null) return;

                        UserApi api = new UserApi().getInstance();
                        for (DocumentSnapshot fe: value) {
                            api.setUserId(fe.getString("userId"));
                            api.setUsername(fe.getString("username"));
                            api.setEmail(fe.getString(UserValues.EMAIL));
                            api.setCreateDate(fe.getDate(UserValues.CREATED_DATE));
                            api.setUpdatedDate(fe.getDate(UserValues.UPDATED_DATE));
                            api.setImageUrl(fe.getString(UserValues.IMAGE_URI));

                            List<String> recipes = (List<String>) fe.get(UserValues.RECIPES);
                            api.setRecipes(recipes);
                            List<String> favorites = (List<String>) fe.get(UserValues.FAVORITES);
                            api.setFavorites(favorites);

                            startActivity(new Intent(LoginActivity.this, RecipesActivity.class));
                            finish();
                        }
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressBar.setVisibility(View.INVISIBLE);
                Log.d("LoginActivity.class", "Unable to collect User");
            }
        });

    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login_button:
                loginWithEmailPassword();
                break;
            case R.id.login_create_acct_button:
                createUserAccount();
                break;
            default:
        }
    }
}