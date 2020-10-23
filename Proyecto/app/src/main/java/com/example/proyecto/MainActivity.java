package com.example.proyecto;

import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity implements Runnable{

    public EditText email;
    public EditText passw;

    private JSONObject paqueteDatos;
    private HttpURLConnection peticion;
    private URL link;
    private DataOutputStream salida;
    private DataInputStream entrada;
    private Thread hiloConexion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        email = (EditText) findViewById(R.id.editTextEmail);
        passw = (EditText) findViewById(R.id.editTextPassword);

        paqueteDatos = new JSONObject();
        hiloConexion = new Thread(this);
    }

    public void eventoIngresar(View view) {
        try {
            paqueteDatos.put("email",email.getText().toString());
            paqueteDatos.put("password", passw.getText().toString());
            hiloConexion.start();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        /*
        link = new URL("http://so-unlam.net.ar/api/api/login");
        peticion = (HttpURLConnection) link.openConnection();
        peticion.setRequestMethod("POST");
        peticion.connect();
        */
    }

    public void eventoCrearUsuario(View view) {
        Intent intentRegistrarActivity = new Intent(this, RegistrarActivity.class);
        startActivity(intentRegistrarActivity);
    }
}


