package com.example.proyecto;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

public class RegistrarActivity extends AppCompatActivity {

    private List<TextView> textsViews;
    private List<EditText> editsText;
    private JSONObject paqueteDatos;
    private Handler handlerRegistar;

    private static  final String TAG = "REGISTRAR";
    private static final int LONG_MIN_PASS = 8;
    private static final String URI_REGISTRAR = "http://so-unlam.net.ar/api/api/register";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar);

        textsViews = new ArrayList<TextView>();
        editsText = new ArrayList<EditText>();

        editsText.add((EditText) findViewById(R.id.editTextNombre));
        editsText.add((EditText) findViewById(R.id.editTextApellido));
        editsText.add((EditText) findViewById(R.id.editTextTextDni));
        editsText.add((EditText) findViewById(R.id.editTextEmail));
        editsText.add((EditText) findViewById(R.id.editTextPassword));
        editsText.add((EditText) findViewById(R.id.editTextComision));

        textsViews.add((TextView) findViewById(R.id.textViewNombre));
        textsViews.add((TextView) findViewById(R.id.textViewApellido));
        textsViews.add((TextView) findViewById(R.id.textViewDni));
        textsViews.add((TextView) findViewById(R.id.textViewEmail));
        textsViews.add((TextView) findViewById(R.id.textViewPass));
        textsViews.add((TextView) findViewById(R.id.textViewCom));

        this.handlerRegistar = new Handler(){
            @Override
            public void handleMessage(@NonNull Message msg) {
                Bundle datos = msg.getData();
                Log.i(TAG, datos.getString("MENSAJE"));

                Intent i = new Intent(getBaseContext(), PantallaPrincipal.class);
                String mensaje = datos.getString("MENSAJE");
                i.putExtra("MENSAJE", mensaje);
                startActivity(i);
            }
        };
    }

    public void eventoRegistrar(View view) {

        if (validarCampos()) return;

        paqueteDatos = new JSONObject();

        try {
            paqueteDatos.put("env", "TEST");
            paqueteDatos.put("name", editsText.get(0).getText().toString());
            paqueteDatos.put("lastname", editsText.get(1).getText().toString());
            paqueteDatos.put("dni", editsText.get(2).getText().toString());
            paqueteDatos.put("email", editsText.get(3).getText().toString());
            paqueteDatos.put("password", editsText.get(4).getText().toString());
            paqueteDatos.put("commission", editsText.get(5).getText().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        HiloConexion hiloRegistrar = new HiloConexion(URI_REGISTRAR, paqueteDatos, handlerRegistar);
        hiloRegistrar.start();
    }

    private boolean validarCampos() {
        boolean errorDatos = false;

        for (int i = 0; i < editsText.size(); i++) {
            EditText actual = editsText.get(i);
            if (actual.getText().toString().isEmpty()) {
                textsViews.get(i).setText("Completar por favor");
                errorDatos = true;
            } else {
                textsViews.get(i).setText("");
            }
        }

        if (((EditText) findViewById(R.id.editTextPassword)).getText().toString().length() < LONG_MIN_PASS) {
            ((TextView) findViewById(R.id.textViewPass)).setText("Minimo 8 caracteres");
            errorDatos = true;
        }

        if (!((EditText) findViewById(R.id.editTextTextDni)).getText().toString().matches("[0-9]+")) {
            ((TextView) findViewById(R.id.textViewDni)).setText("Solo se admite numeros");
            errorDatos = true;
        }

        if (!((EditText) findViewById(R.id.editTextComision)).getText().toString().matches("[0-9]+")) {
            ((TextView) findViewById(R.id.textViewCom)).setText("Solo se admite numeros");
            errorDatos = true;
        }

        return errorDatos;
    }

}

