package com.example.proyecto;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
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

    private TextView datosSensorAcelerometro;
    private TextView datosSensorLuz;
    private TextView datosGiroscopo;

    private SensorManager mSensorManager;

    @SuppressLint("LongLogTag")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantalla_principal);

        Intent iRecibido = getIntent();
        Bundle datos = iRecibido.getExtras();
        this.mensajeRecibido = datos.getString("MENSAJE");
        Log.i(PROYECTO + "->" + TAG, this.mensajeRecibido);
        nivelBateria();

        datosSensorAcelerometro = (TextView) findViewById(R.id.textViewDatosAcelerometro);
        datosSensorLuz = (TextView) findViewById(R.id.textViewLuz);
        datosGiroscopo = (TextView) findViewById(R.id.textViewGiroscopo);

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
        String txt = "";

        synchronized (this) {
            switch (event.sensor.getType()) {
                case Sensor.TYPE_ACCELEROMETER:
                    txt = "Acelerometro:\n";
                    txt += "x: " + event.values[0] + "m/seg2\n";
                    txt += "y: " + event.values[1] + "m/seg2\n";
                    txt += "z: " + event.values[2] + "m/seg2\n";
                    datosSensorAcelerometro.setText(txt);
                    break;

                case Sensor.TYPE_LIGHT:
                    txt = "Luz ambiente: ";
                    txt += event.values[0] + "lx";
                    datosSensorLuz.setText(txt);
                    break;

                case Sensor.TYPE_ORIENTATION:
                    txt = "Orientacion:\n";
                    txt += "Acimut: " + getAcimut(event.values[0]) + "\n";
                    txt += "y: " + event.values[1] + "\n";
                    txt += "z: " + event.values[2] + "\n";
                    datosGiroscopo.setText(txt);
                    break;
            }

        }
    }



    private String getAcimut(float value) {

        if (value < 22.5)
            return "N";
        if (value >=  22.5 && value < 67.5)
            return  "NE";
        if (value >=  67.5 && value < 122.5)
            return "E";
        if (value >= 122.5 && value < 157.5)
            return "SE";
        if (value >= 157.5 && value < 205.5)
            return "S";
        if (value >= 205.5 && value < 247.5)
            return "SO";
        if (value >= 247.5 && value < 292.5)
            return "O";
        if (value >= 292.5 && value < 337.5)
            return "NO";
        if (value >= 337.5 && value < 360)
            return "N";

        return "Errror";
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
        mSensorManager.unregisterListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT));
        mSensorManager.unregisterListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION));
    }

    private void initSensores() {
        mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
        mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION), SensorManager.SENSOR_DELAY_NORMAL);
        mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT), SensorManager.SENSOR_DELAY_NORMAL);
    }
}