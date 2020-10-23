package com.example.proyecto;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class RegistrarActivity extends AppCompatActivity {

    private EditText editTextNombre, editTextApellido, editTextDni, editTextEmail,
            editTextPassword, editTextComs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar);

        editTextApellido = (EditText) findViewById(R.id.editTextApellido);
        editTextNombre = (EditText) findViewById(R.id.editTextNombre);
        editTextDni = (EditText) findViewById(R.id.editTextTextDni);
        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        editTextComs = (EditText) findViewById(R.id.editTextComision);
    }

    public void eventoRegistrar(View view) {
        if (editTextApellido.getText().toString().isEmpty()){
            return;
        }
        if (editTextNombre.getText().toString().isEmpty()){
            return;
        }
        if (editTextDni.getText().toString().isEmpty()){
            return;
        }
        if (editTextEmail.getText().toString().isEmpty()){
            return;
        }
        if (editTextPassword.getText().toString().isEmpty()){
            return;
        }
        if (editTextComs.getText().toString().isEmpty()){
            return;
        }

    }
}