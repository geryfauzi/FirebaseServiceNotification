package com.ergnologi.firebaseservicenotification;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;

public class Session {
    public static final String un = "USERNAME";
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    Context context;
    int privateMode = 0;
    String namwe = "FIREBASE";

    public Session(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences(namwe, privateMode);
        editor = sharedPreferences.edit();
    }

    public void saveUsername(String username) {
        editor.putString(un, username);
        editor.apply();
    }

    public HashMap<String, String> getUsername() {
        HashMap<String, String> user = new HashMap<>();
        user.put(un, sharedPreferences.getString(un, null));
        return user;
    }

}
