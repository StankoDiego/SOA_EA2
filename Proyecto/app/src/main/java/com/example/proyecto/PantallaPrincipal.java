package com.example.proyecto;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

public class PantallaPrincipal extends AppCompatActivity {

    private String mensajeRecibido;
    private TextView textView;
    private static final String PROYECTO = "PROYECTO";
    private static final String TAG = "PANTALLA PRINCIPAL";

    @SuppressLint("LongLogTag")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantalla_principal);

        Intent iRecibido = getIntent();
        Bundle datos = iRecibido.getExtras();
        this.mensajeRecibido = datos.getString("MENSAJE");
        textView = (TextView) findViewById(R.id.textView);
        Log.i(PROYECTO + "->" + TAG, this.mensajeRecibido);
        nivelBateria();
    }

    private void nivelBateria() {
        BroadcastReceiver batteryLevelReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                context.unregisterReceiver(this);
                int rawlevel = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
                int scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
                int level = -1;
                if (rawlevel >= 0 && scale > 0) {
                    level = (rawlevel * 100) / scale;
                }
                Toast.makeText(getApplicationContext(), "Nivel de bateria actual: " + level + "%", Toast.LENGTH_LONG).show();
            }
        };
        IntentFilter batteryLevelFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        registerReceiver(batteryLevelReceiver, batteryLevelFilter);
    }
}