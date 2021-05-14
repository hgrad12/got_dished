package com.example.gotdished;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;

import com.example.gotdished.util.FirebaseUtil;
import com.example.gotdished.util.UserApi;
import com.example.gotdished.util.UserValues;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity.class";
    private final Handler mWaitHandler = new Handler();

    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseUser currentUser;

    private UserApi api = new UserApi().getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        FirebaseAuth.getInstance().signOut();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() != null) {
                    String userUuid = currentUser.getUid();
                    FirebaseUtil.retrieveUsersCollection().whereEqualTo(UserValues.USER_ID, userUuid)
                            .addSnapshotListener(new EventListener<QuerySnapshot>() {
                                @Override
                                public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                                    if (error != null || value.getDocuments().isEmpty()) {
//                                        startActivity(new Intent(MainActivity.this, LoginActivity.class));
                                        return;
                                    }

                                    currentUser = firebaseAuth.getCurrentUser();

                                    for (DocumentSnapshot snapshot: value.getDocuments()) {
                                        Log.d(TAG, "userUuid:  " + userUuid);
                                        api.setUsername(snapshot.getString(UserValues.USERNAME));
                                        api.setUserId(userUuid);
                                        api.setEmail(snapshot.getString(UserValues.EMAIL));
                                        api.setCreateDate(snapshot.getDate(UserValues.CREATED_DATE));
                                        api.setUpdatedDate(snapshot.getDate(UserValues.UPDATED_DATE));
                                        api.setImageUrl(snapshot.getString(UserValues.IMAGE_URI));

                                        List<String> recipes = (List<String>) snapshot.get(UserValues.RECIPES);
                                        api.setRecipes(recipes);
                                        List<String> favorites = (List<String>) snapshot.get(UserValues.FAVORITES);
                                        api.setFavorites(favorites);

//                                        startActivity(new Intent(MainActivity.this, RecipesActivity.class));
//                                        finish();
                                    }
                                }
                            });
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + currentUser.getUid());

                } else {
                    Log.d(TAG, "onAuthStateChanged:signed_out");
//                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
//                    finish();
                }
            }
        };

        mWaitHandler.postDelayed(new Runnable() {

            @Override
            public void run() {
                try {
                    Intent intent;
                    if (currentUser != null) {
                        intent = new Intent(MainActivity.this, RecipesActivity.class);
                    } else {
                        intent = new Intent(MainActivity.this, LoginActivity.class);
                    }
                    startActivity(intent);
                    finish();
                } catch (Exception ignored) {
                    Log.e(TAG, ignored.toString());
                }
            }
        }, 5000);  // Give a 5 seconds delay.
    }

    @Override
    protected void onStart() {
        super.onStart();
        currentUser = firebaseAuth.getCurrentUser();
        firebaseAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (firebaseAuth != null)
            firebaseAuth.removeAuthStateListener(mAuthListener);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        //Remove all the callbacks otherwise navigation will execute even after activity is killed or closed.
        mWaitHandler.removeCallbacksAndMessages(null);
    }
}