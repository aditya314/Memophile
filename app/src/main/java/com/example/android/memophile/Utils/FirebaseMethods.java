package com.example.android.memophile.Utils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.example.android.memophile.Models.User;
import com.example.android.memophile.Models.UserAccountSettings;
import com.example.android.memophile.Models.UserSettings;
import com.example.android.memophile.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by aditya314 on 3/10/2018.
 */

public class FirebaseMethods {
    //firebase
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference myRef;
    private String userID;

    private Context mContext;

    public FirebaseMethods(Context context) {
        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference();
        mContext = context;

        if(mAuth.getCurrentUser() != null){
            userID = mAuth.getCurrentUser().getUid();
        }
    }

    /**
     * Update 'user_account_settings' node for the current user
     * @param displayName
     * @param website
     * @param description
     * @param phoneNumber
     */
    public void updateUserAccountSettings(String displayName, String website, String description, long phoneNumber){

        if(displayName != null){
            myRef.child(mContext.getString(R.string.dbname_user_account_settings))
                    .child(userID)
                    .child(mContext.getString(R.string.field_display_name))
                    .setValue(displayName);
        }


        if(website != null) {
            myRef.child(mContext.getString(R.string.dbname_user_account_settings))
                    .child(userID)
                    .child(mContext.getString(R.string.field_website))
                    .setValue(website);
        }

        if(description != null) {
            myRef.child(mContext.getString(R.string.dbname_user_account_settings))
                    .child(userID)
                    .child(mContext.getString(R.string.field_description))
                    .setValue(description);
        }

        if(phoneNumber != 0) {
            myRef.child(mContext.getString(R.string.dbname_user_account_settings))
                    .child(userID)
                    .child(mContext.getString(R.string.field_phone_number))
                    .setValue(phoneNumber);

            myRef.child(mContext.getString(R.string.dbname_users))
                    .child(userID)
                    .child(mContext.getString(R.string.field_phone_number))
                    .setValue(phoneNumber);
        }
    }

    public void updateUsername(String username){

        myRef.child(mContext.getString(R.string.dbname_users))
                .child(userID)
                .child(mContext.getString(R.string.field_username))
                .setValue(username);

        myRef.child(mContext.getString(R.string.dbname_user_account_settings))
                .child(userID)
                .child(mContext.getString(R.string.field_username))
                .setValue(username);
    }

//    public boolean checkIfUsernameExists(String username, DataSnapshot datasnapshot){
//        Log.d(TAG, "checkIfUsernameExists: checking if " + username + " already exists.");
//
//        User user = new User();
//
//        for (DataSnapshot ds: datasnapshot.child(userID).getChildren()){
//            Log.d(TAG, "checkIfUsernameExists: datasnapshot: " + ds);
//
//            user.setUsername(ds.getValue(User.class).getUsername());
//            Log.d(TAG, "checkIfUsernameExists: username: " + user.getUsername());
//
//            if(StringManipulation.expandUsername(user.getUsername()).equals(username)){
//                Log.d(TAG, "checkIfUsernameExists: FOUND A MATCH: " + user.getUsername());
//                return true;
//            }
//        }
//        return false;
//    }

    /**
     * Register a new email and password to Firebase Authentication
     * @param email
     * @param password
     * @param username
     */
    public void registerNewEmail(final String email, String password, final String username){
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Toast.makeText(mContext, "Authentication Failed!!", Toast.LENGTH_SHORT).show();

                        }
                        else if(task.isSuccessful()){
                            //send verificaton email
                            sendVerificationEmail();

                            userID = mAuth.getCurrentUser().getUid();
                        }

                    }
                });
    }

    public void sendVerificationEmail(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if(user != null){
            user.sendEmailVerification()
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){

                            }else{
                                Toast.makeText(mContext, "couldn't send verification email.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }

    /**
     * Add information to the users nodes
     * Add information to the user_account_settings node
     * @param email
     * @param username
     * @param description
     * @param website
     * @param profile_photo
     */
    public void addNewUser(String email, String username, String description, String website, String profile_photo){

        User user = new User( userID,  1,  email,  username );

        myRef.child(mContext.getString(R.string.dbname_users))
                .child(userID)
                .setValue(user);


        UserAccountSettings settings = new UserAccountSettings(
                description,
                StringManipulation.expandUsername(username),
                0,
                0,
                0,
                profile_photo,
                username,
                website
        );

        myRef.child(mContext.getString(R.string.dbname_user_account_settings))
                .child(userID)
                .setValue(settings);

    }


    /**
     * Retrieves the account settings for teh user currently logged in
     * Database: user_acount_Settings node
     * @param dataSnapshot
     * @return
     */
    public UserSettings getUserSettings(DataSnapshot dataSnapshot){

        UserAccountSettings settings  = new UserAccountSettings();
        User user = new User();

        for(DataSnapshot ds: dataSnapshot.getChildren()){

            // user_account_settings node
            if(ds.getKey().equals(mContext.getString(R.string.dbname_user_account_settings))) {

                try {

                    settings.setDisplay_name(
                            ds.child(userID)
                                    .getValue(UserAccountSettings.class)
                                    .getDisplay_name()
                    );
                    settings.setUsername(
                            ds.child(userID)
                                    .getValue(UserAccountSettings.class)
                                    .getUsername()
                    );
                    settings.setWebsite(
                            ds.child(userID)
                                    .getValue(UserAccountSettings.class)
                                    .getWebsite()
                    );
                    settings.setDescription(
                            ds.child(userID)
                                    .getValue(UserAccountSettings.class)
                                    .getDescription()
                    );
                    settings.setProfile_photo(
                            ds.child(userID)
                                    .getValue(UserAccountSettings.class)
                                    .getProfile_photo()
                    );
                    settings.setPosts(
                            ds.child(userID)
                                    .getValue(UserAccountSettings.class)
                                    .getPosts()
                    );
                    settings.setFollowing(
                            ds.child(userID)
                                    .getValue(UserAccountSettings.class)
                                    .getFollowing()
                    );
                    settings.setFollowers(
                            ds.child(userID)
                                    .getValue(UserAccountSettings.class)
                                    .getFollowers()
                    );


                } catch (NullPointerException e) {

                }
            }


            // users node
            if(ds.getKey().equals(mContext.getString(R.string.dbname_users))) {

                user.setUsername(
                        ds.child(userID)
                                .getValue(User.class)
                                .getUsername()
                );
                user.setEmail(
                        ds.child(userID)
                                .getValue(User.class)
                                .getEmail()
                );
                user.setPhone_number(
                        ds.child(userID)
                                .getValue(User.class)
                                .getPhone_number()
                );
                user.setUser_id(
                        ds.child(userID)
                                .getValue(User.class)
                                .getUser_id()
                );

            }
        }
        return new UserSettings(user, settings);

    }
}