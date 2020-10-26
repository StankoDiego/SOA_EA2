package com.example.proyecto;

import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity{

    private EditText editTextUsuario;
    private EditText editTextContraseña;
    
    private TextView textViewUsuario;
    private TextView textViewContraseña;
    
    private JSONObject paqueteDatos;
    private HttpURLConnection peticion;
    private URL link;
    private DataOutputStream salida;
    private DataInputStream entrada;

    private static final String URI_LOGIN = "http://so-unlam.net.ar/api/api/login";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextUsuario = (EditText) findViewById(R.id.editTextEmail);
        editTextContraseña = (EditText) findViewById(R.id.editTextContraseña);

        textViewUsuario = (TextView) findViewById(R.id.textViewEmail);
        textViewContraseña = (TextView) findViewById(R.id.textViewContraseña);

        paqueteDatos = new JSONObject();
    }

    public void eventoIngresar(View view) {
        boolean camposVacios = false;

        String usuario = editTextUsuario.getText().toString();
        String pass = editTextContraseña.getText().toString();

        if (usuario.isEmpty()){
            textViewUsuario.setText("Completar por favor");
            camposVacios = true;
        }else{
            textViewUsuario.setText("");
        }

        if (pass.isEmpty()){
            textViewContraseña.setText("Completar por favor");
            camposVacios = true;
        }else{
            textViewContraseña.setText("");
        }

        if (camposVacios) return;

        paqueteDatos = new JSONObject();

        try {
            paqueteDatos.put("env", "PROD");
            paqueteDatos.put("email", usuario);
            paqueteDatos.put("password", pass);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        HiloConexion hiloLogin = new HiloConexion(URI_LOGIN, paqueteDatos, "LOGIN");
        hiloLogin.start();

    }

    public void eventoCrearUsuario(View view) {
        Intent intentRegistrarActivity = new Intent(this, RegistrarActivity.class);
        startActivity(intentRegistrarActivity);
    }
}


