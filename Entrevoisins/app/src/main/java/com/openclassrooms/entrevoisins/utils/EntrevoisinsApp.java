package com.openclassrooms.entrevoisins.utils;

import android.app.Application;
import android.content.Context;
import android.util.Log;

public class EntrevoisinsApp extends Application {
    /** Context de l'application */
    private static Context context;

    /**
     * Lancement de l'application
     */
    public void onCreate() {
        super.onCreate();
        EntrevoisinsApp.context = getApplicationContext();

        Log.i("DEBUG", "EntrevoisinsApp onCreate");
    }

    /**
     * Retourne le context de l'application
     * @return Context de l'application
     */
    public static Context getContext()
    {
        return EntrevoisinsApp.context;
    }
}


