package com.example.proyecto;

import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.BatteryManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

public class PantallaPrincipal extends AppCompatActivity implements SensorEventListener {

    private String mensajeRecibido;
    private static final String PROYECTO = "PROYECTO";
    private static final String TAG = "PANTALLA PRINCIPAL";

    private int TIMER_REFRESH = 300;//5 min //1800 30min;

    private TextView datosSensorAcelerometro;
    private TextView textViewSensorAcelerometro;

    private HiloConexion hiloPantallaPrincipal;

    private SensorManager mSensorManager;
    private Sensor sensorAcelerometro;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantalla_principal);

        Intent iRecibido = getIntent();
        Bundle datos = iRecibido.getExtras();
        this.mensajeRecibido = datos.getString("MENSAJE");
        Log.i(TAG, this.mensajeRecibido);
        nivelBateria();

        datosSensorAcelerometro = (TextView) findViewById(R.id.textViewDatosAcelerometro);
        textViewSensorAcelerometro = (TextView) findViewById(R.id.textViewAcelerometro);

        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        initSensores();

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

    @Override
    public void onSensorChanged(SensorEvent event) {

        synchronized (this){

        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        initSensores();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        initSensores();
    }

    @Override
    protected void onPause() {
        super.onPause();
        detenerSensores();
    }

    private void detenerSensores() {
        mSensorManager.unregisterListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER));
    }

    private void initSensores() {
        mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
    }
}