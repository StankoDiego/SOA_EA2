package com.example.proyecto;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.os.Build;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class InformarDatos extends AppCompatActivity {

    private static final String PROYECTO = "PROYECTO";
    private static final String TAG = "INFORMAR DATOS";

    private ArrayList<String[]> eventos;
    private Spinner spinnerDni;

    private LinearLayout table;

    @SuppressLint("LongLogTag")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_informar_datos);

        Log.i(PROYECTO + "->" + TAG, "Mostrando Datos");

        SharedPreferences preferences = getSharedPreferences("EventosRegistrados", MODE_PRIVATE);
        Map<String, ?> datos = preferences.getAll();

        this.spinnerDni = (Spinner) findViewById(R.id.spinnerDni);
        this.table = (LinearLayout) findViewById(R.id.table);

        obtenerEventos(datos);
        String[] opciones = obtenerOpciones();

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, opciones);
        spinnerDni.setAdapter(adapter);

        //PROBANDO

    }

    private String[] obtenerOpciones() {
        LinkedHashSet<String> aux = new LinkedHashSet<>();
        aux.add("Seleccionar DNI...");
        for (int i = 0; i < this.eventos.size(); i++) {
            aux.add(this.eventos.get(i)[1]);
        }
        String[] a = new String[aux.size()];
        a = aux.toArray(a);
        return a;
    }

    private void obtenerEventos(Map<String, ?> datos) {
        String[] keys = datos.keySet().toArray(new String[datos.size()]);

        this.eventos = new ArrayList<>();
        for (String index : keys) {
            Set<String> aux = (Set<String>) datos.get(index);

            String[] array = new String[aux.size()];
            int i = 0;
            for (String a : aux) {
                array[i++] = a;
            }
            this.eventos.add(array);
        }
    }

    public void listarDatos(View view) {
        String itemSeleccionado = this.spinnerDni.getSelectedItem().toString();
        List<String[]> elemetrosFiltrados = extraerElementosFiltrados(itemSeleccionado);
        String[] encabezado = {"DATOS", "DNI"};
        table.removeAllViews();
        if (elemetrosFiltrados.size() == 0) return;

        for (int cantFilas = 0; cantFilas < elemetrosFiltrados.size(); cantFilas++) {
            LinearLayout fila = new LinearLayout(getBaseContext());
            fila.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, 300));

            for (int cantTextViews = 0; cantTextViews < 2; cantTextViews++) {
                TextView text = new TextView(getBaseContext());
                text.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, 100));
                text.setTextColor(Color.WHITE);
                int indice = encabezado.length - cantTextViews - 1;
                String info = elemetrosFiltrados.get(cantFilas)[indice];
                String aux = encabezado[indice];
                text.setText(aux + ":" + info);
                fila.setOrientation(LinearLayout.VERTICAL);
                fila.addView(text);

            }
            table.addView(fila);
        }
    }

    private List<String[]> extraerElementosFiltrados(String itemSeleccionado) {
        List<String[]> listAux = new ArrayList<>();
        if (itemSeleccionado.equals("Seleccionar DNI...")) return listAux;

        for (int i = 0; i < this.eventos.size(); i++) {
            String[] aux = this.eventos.get(i);
            if (aux[1].equals(itemSeleccionado)) {
                listAux.add(aux);
            }
        }
        return listAux;
    }
}