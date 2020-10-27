package com.example.proyecto;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity{

    private EditText editTextUsuario;
    private EditText editTextContraseña;
    
    private TextView textViewUsuario;
    private TextView textViewContraseña;
    
    private JSONObject paqueteDatos;
    private Handler handlerMain;
    private static final String URI_LOGIN = "http://so-unlam.net.ar/api/api/login";
    private static final String TAG = "MAIN";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextUsuario = (EditText) findViewById(R.id.editTextEmail);
        editTextContraseña = (EditText) findViewById(R.id.editTextContraseña);

        textViewUsuario = (TextView) findViewById(R.id.textViewEmail);
        textViewContraseña = (TextView) findViewById(R.id.textViewContraseña);

        paqueteDatos = new JSONObject();

        this.handlerMain = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                Bundle datos = msg.getData();

                try {
                    JSONObject respuesta = new JSONObject(datos.getString("MENSAJE"));
                    String estadoPeticion = respuesta.getString("success");

                    if (estadoPeticion.equals("false")) {
                        Toast.makeText(getApplicationContext(), "Fallo de credenciales", Toast.LENGTH_SHORT).show();
                        return;
                    } else {
                        Intent i = new Intent(getBaseContext(), PantallaPrincipal.class);
                        i.putExtra("MENSAJE", datos.getString("MENSAJE"));
                        startActivity(i);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

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

        ConnectivityManager cm = (ConnectivityManager)getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        if (activeNetwork != null && activeNetwork.isConnectedOrConnecting()){
            Toast.makeText(this, "Conexion: Disponible", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this, "Conexion: No disponible", Toast.LENGTH_SHORT).show();
            return;
        }

        HiloConexion hiloLogin = new HiloConexion(URI_LOGIN, paqueteDatos, handlerMain);
        hiloLogin.start();
    }

    public void eventoCrearUsuario(View view) {
        Intent intentRegistrarActivity = new Intent(this, RegistrarActivity.class);
        startActivity(intentRegistrarActivity);
    }
}


