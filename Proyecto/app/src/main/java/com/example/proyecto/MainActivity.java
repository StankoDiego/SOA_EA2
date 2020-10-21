package com.example.proyecto;

import androidx.appcompat.app.AppCompatActivity;


import android.os.Bundle;
import android.view.View;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void eventoIngresar(View view) {
        Toast.makeText(getApplicationContext(), "boton ingresar", Toast.LENGTH_LONG).show();
    }

    public void eventoRegistrar(View view) {
        Toast.makeText(getApplicationContext(), "boton registrar", Toast.LENGTH_LONG).show();
    }
}
