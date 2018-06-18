package com.icsdeliverysystem.storage;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.util.Map;

public class SharedPreferenceUtil {

    private static SharedPreferences sharedPreferences = null;

    private static SharedPreferences.Editor editor = null;

    @SuppressLint("CommitPrefEdits")
    public static void init(Context mcontext) {

        if (sharedPreferences == null) {
            sharedPreferences = PreferenceManager
                    .getDefaultSharedPreferences(mcontext);
            editor = sharedPreferences.edit();
        }
    }

    public static void clear() {
        editor.clear();
        save();
    }

    public static void putValue(String key, String value) {
        editor.putString(key, value);
        save();
    }

    public static void putValue(String key, int value) {
        editor.putInt(key, value);
        save();
    }

    public static void putValue(String key, long value) {
        editor.putLong(key, value);
        save();
    }

    public static void putValue(String key, boolean value) {
        editor.putBoolean(key, value);
        save();
    }

    public static void save() {
        editor.commit();
        editor.apply();
    }

    public static String getString(String key, String defValue) {
        return sharedPreferences.getString(key, defValue);
    }

    public static void clearKey(String key){
        editor.remove(key);
        editor.apply();
    }

    public static int getInt(String key, int defValue) {
        return sharedPreferences.getInt(key, defValue);
    }

    public static long getLong(String key, long defValue) {
        return sharedPreferences.getLong(key, defValue);
    }

    public static boolean getBoolean(String key, boolean defValue) {
        return sharedPreferences.getBoolean(key, defValue);
    }

    public static boolean contains(String key) {
        return sharedPreferences.contains(key);
    }

    public static Map<String, ?> getAll() {
        return sharedPreferences.getAll();
    }

}
