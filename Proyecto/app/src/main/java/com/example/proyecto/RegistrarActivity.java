package com.example.proyecto;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class RegistrarActivity extends AppCompatActivity {

    private List<TextView> textsViews;
    private List<EditText> editsText;
    private static final int LONG_MIN_PASS = 8;

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

    }

    public void eventoRegistrar(View view) {
        boolean errorDatos = false;

        for(int i = 0; i < editsText.size(); i++){
            EditText actual = editsText.get(i);
            if (actual.getText().toString().isEmpty()){
                textsViews.get(i).setText("Completar por favor");
                errorDatos = true;
                } else {
                textsViews.get(i).setText("");
            }
        }

        if (((EditText) findViewById(R.id.editTextPassword)).getText().toString().length() <= LONG_MIN_PASS) {
            ((TextView) findViewById(R.id.textViewPass)).setText("Minimo 8 caracteres");
            errorDatos = true;
        }
        if (errorDatos) return;

        /*
        * Tengo que armar el paquete de datos para el registro
        * */
    }
}