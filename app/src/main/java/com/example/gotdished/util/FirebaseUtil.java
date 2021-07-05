package com.example.gotdished.util;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class FirebaseUtil {
    //Firestore Collections
    private static final String USERS_COLLECTION = "Users";
    private static final String RECIPES_COLLECTION = "Recipes";
    private static final String RECIPE_ITEM_COLLECTION = "Recipe Items";
    private static final String FAVORITES_COLLECTION = "Favorites";
    private static final String COMMENTS_COLLECTION = "Comments";
    private static final String REPLIES_COLLECTION = "Replies";

    //Firebase Storage References
    private static final String RECIPE_IMAGE_FOLDER = "recipe_images";
    private static final String USER_IMAGE_FOLDER = "user_images";


    public static CollectionReference retrieveUsersCollection() {
        return FirebaseFirestore.getInstance().collection(USERS_COLLECTION);
    }

    public static CollectionReference retrieveRecipesCollection() {
        return FirebaseFirestore.getInstance().collection(RECIPES_COLLECTION);
    }

    public static CollectionReference retrieveRecipeItemsCollection() {
        return FirebaseFirestore.getInstance().collection(RECIPE_ITEM_COLLECTION);
    }

    public static CollectionReference retrieveFavoritesCollection() {
        return FirebaseFirestore.getInstance().collection(FAVORITES_COLLECTION);
    }

    public static CollectionReference retrieveCommentsCollection() {
        return FirebaseFirestore.getInstance().collection(COMMENTS_COLLECTION);
    }

    public static CollectionReference retrieveRepliesCollection() {
        return FirebaseFirestore.getInstance().collection(REPLIES_COLLECTION);
    }

    public static StorageReference retrieveRecipeImageFolderStorageReference() {
        return FirebaseStorage.getInstance().getReference().child(RECIPE_IMAGE_FOLDER);
    }

    public static StorageReference retrieveUserImageFolderStorageReference() {
        return FirebaseStorage.getInstance().getReference().child(USER_IMAGE_FOLDER);
    }
    public static FirebaseAuth getFirebaseAuth() {
        return FirebaseAuth.getInstance();
    }

    public static FirebaseUser getCurrentUser() {
        return getFirebaseAuth().getCurrentUser();
    }
}
