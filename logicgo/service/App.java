package com.example.logicgo.service;

import android.app.Application;
import android.content.SharedPreferences;

import androidx.room.Room;

import com.example.logicgo.R;
import com.example.logicgo.database.AppDatabase;
import com.google.gson.Gson;

public class App extends Application {


    public static SharedPreferences pref;
    public static SharedPreferences.Editor editor;

    public static Gson gson = new Gson();

    public static AppDatabase db;

    @Override
    public void onCreate() {
        super.onCreate();

        pref = getSharedPreferences(getString(R.string.app_name), MODE_PRIVATE);
        editor = pref.edit();

        db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, getString(R.string.app_name))
//                .allowMainThreadQueries()
                .build();
    }


    public static void putString(String key, String value) {
        editor.putString(key, value).apply();
    }

    public static String getString(String key) {
        return pref.getString(key, "");
    }

    public static void putBoolean(String key, boolean value) {
        editor.putBoolean(key, value).apply();
    }

    public static boolean getBoolean(String key) {
        return pref.getBoolean(key, false);
    }

    public static void putInt(String key, int value) {
        editor.putInt(key, value).apply();
    }

    public static int getInt(String key) {
        return pref.getInt(key, -1);
    }

    public static void putFloat(String key, float value) {
        editor.putFloat(key, value).apply();
    }

    public static float getFloat(String key) {
        return pref.getFloat(key, -1f);
    }

    public static void putLong(String key, long value) {
        editor.putLong(key, value).apply();
    }

    public static float getLong(String key) {
        return pref.getLong(key, -1L);
    }
    //--------------------common methods-------------------------//

    public static void removeAll() {
        editor.clear().commit();
    }
}
