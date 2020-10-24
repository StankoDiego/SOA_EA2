package com.example.proyecto;

import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import org.json.JSONObject;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity implements Runnable{

    private EditText editTextUsuario;
    private EditText editTextContraseña;
    
    private TextView textViewUsuario;
    private TextView textViewPassword;
    
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

        editTextUsuario = (EditText) findViewById(R.id.editTextEmail);
        editTextContraseña = (EditText) findViewById(R.id.editTextContraseña);

        textViewUsuario = (TextView) findViewById(R.id.textViewEmail);
        textViewPassword = (TextView) findViewById(R.id.textViewContraseña);

        paqueteDatos = new JSONObject();
        hiloConexion = new Thread(this);
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
            textViewPassword.setText("Completar por favor");
            camposVacios = true;
        }else{
            textViewPassword.setText("");
        }

        if (camposVacios) return;

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


