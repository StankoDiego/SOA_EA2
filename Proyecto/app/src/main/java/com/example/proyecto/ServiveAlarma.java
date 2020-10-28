package com.example.proyecto;


import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

public class ServiveAlarma extends IntentService {

    private static final String TAG = "ALARMA";
    private static final String PROYECTO = "PROYECTO";

    public ServiveAlarma() {
        super("Refresh Token");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(PROYECTO + "->" + TAG, "Comenzo el conteo");
    }

    @Override
    protected void onHandleIntent(Intent intent) {


    }
}
