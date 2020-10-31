package com.example.proyecto;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;

import java.util.Map;

public class InformarDatos extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_informar_datos);

        SharedPreferences preferences = getSharedPreferences("EventosRegistrados", MODE_PRIVATE);
        Map<String, ?> datos = preferences.getAll();
    }
}