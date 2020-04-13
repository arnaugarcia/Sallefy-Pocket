package com.sallefy.services.authentication;

import android.content.Context;
import android.content.SharedPreferences;

import com.sallefy.model.JWTToken;

import static java.util.Objects.isNull;

public class AuthenticationUtils {

    private static String LOGIN_COLLECTION = "userPreferences";
    private static String KEY_USER = "username";
    private static String KEY_TOKEN = "token";

    public static boolean saveLogin(Context context, String login) {
        SharedPreferences prefs = context.getSharedPreferences(LOGIN_COLLECTION, Context.MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = prefs.edit();
        prefsEditor.putString(KEY_USER, login);
        prefsEditor.apply();
        return true;
    }

    public static boolean saveToken(Context context, JWTToken token) {
        SharedPreferences prefs = context.getSharedPreferences(LOGIN_COLLECTION, Context.MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = prefs.edit();
        prefsEditor.putString(KEY_TOKEN, token.getToken());
        prefsEditor.apply();
        TokenStoreManager.getInstance().setToken(token.getToken());
        return true;
    }

    public static boolean isUserLogged(Context context) {
        String username = getLogin(context);
        String token = getToken(context);

        return !isNull(username) && !isNull(token);
    }

    public static String getToken(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(LOGIN_COLLECTION, Context.MODE_PRIVATE);
        return prefs.getString(KEY_TOKEN, null);
    }

    public static String getLogin(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(LOGIN_COLLECTION, Context.MODE_PRIVATE);
        return prefs.getString(KEY_USER, null);
    }

    public static void resetValues(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(LOGIN_COLLECTION, Context.MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = prefs.edit();
        prefsEditor.putString(KEY_TOKEN, null);
        prefsEditor.putString(KEY_USER, null);
        prefsEditor.apply();
    }
}
