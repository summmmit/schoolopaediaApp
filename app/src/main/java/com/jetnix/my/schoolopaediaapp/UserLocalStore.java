package com.jetnix.my.schoolopaediaapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

/**
 * Created by 1084760 on 2015/07/30.
 */
public class UserLocalStore {

    public static final String SP_NAME = "userDetails";

    SharedPreferences userLocalDatabase;

    public UserLocalStore(Context context){
        userLocalDatabase = context.getSharedPreferences(SP_NAME, 0);
    }

    public void storeUserData(Integer user_id, String email){
        Log.d("email", email);
        Log.d("user_id", user_id+"");
        SharedPreferences.Editor userLocalDatabaseEditor = userLocalDatabase.edit();
        userLocalDatabaseEditor.putInt("user_id", user_id);
        userLocalDatabaseEditor.putString("email", email);
        userLocalDatabaseEditor.commit();
    }

    public void setUserLoggedIn(Boolean loggedIn){
        SharedPreferences.Editor userLocalDatabaseEditor = userLocalDatabase.edit();
        userLocalDatabaseEditor.putBoolean("loggedIn", loggedIn);
        userLocalDatabaseEditor.commit();
    }

    public void clearUserData() {
        SharedPreferences.Editor userLocalDatabaseEditor = userLocalDatabase.edit();
        userLocalDatabaseEditor.clear();
        userLocalDatabaseEditor.commit();
    }

    public int getLoggedInUserId() {
        if (userLocalDatabase.getBoolean("loggedIn", false) == false) {
            return -1;
        }

        int user_id = userLocalDatabase.getInt("user_id", -1);
        return user_id;
    }

    public String getLoggedInEmailAddress(){
        if (userLocalDatabase.getBoolean("loggedIn", false) == false) {
            return null;
        }

        String email = userLocalDatabase.getString("email", "");
        return email;
    }
}
