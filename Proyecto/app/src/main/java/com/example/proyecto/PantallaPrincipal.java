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
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.BatteryManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class PantallaPrincipal extends AppCompatActivity implements SensorEventListener {

    private static final String PROYECTO = "PROYECTO";
    private static final String TAG = "PANTALLA PRINCIPAL";
    private static final String URI_REFRESH = "http://so-unlam.net.ar/api/api/refresh";
    private static final String URI_EVENT = "http://so-unlam.net.ar/api/api/event";
    private TextView datosSensorAcelerometro;
    private TextView datosSensorLuz;
    private TextView datosGiroscopo;

    private Handler handlerRefresh;
    private Handler handlerEvento;

    private JSONObject paqueteDatos;
    private SensorManager mSensorManager;

    private String token;
    private String tokenRefresh;

    private Timer timer;
    private TimerTask refreshToken;

    private String datosLuz;
    private String datosAcel;
    private String datosOri;

    @SuppressLint("LongLogTag")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantalla_principal);

        Intent iRecibido = getIntent();
        Bundle datos = iRecibido.getBundleExtra("MENSAJE");

        JSONObject datosRecibidos = null;
        try {
            datosRecibidos = new JSONObject(datos.getString("MENSAJE"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        nivelBateria();
        datosSensorAcelerometro = (TextView) findViewById(R.id.textViewAcelerometro);
        datosSensorLuz = (TextView) findViewById(R.id.textViewLuz);
        datosGiroscopo = (TextView) findViewById(R.id.textViewOrientacion);
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        initSensores();
        this.tokenRefresh = null;
        try {
            this.tokenRefresh = datosRecibidos.getString("token_refresh");
            this.token = datosRecibidos.getString("token");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        final String refresh = tokenRefresh;

        timer = new Timer();
        refreshToken = new TimerTask() {
            @Override
            public void run() {
                Date date = new Date();
                Log.i(PROYECTO + "->" + TAG, "Se va a actualizar el token: " + DateFormat.getDateTimeInstance().format(date) + ":" + refresh);
                if (verificarConexion()) {
                    HiloConexion threadConexion = new HiloConexion(URI_REFRESH, null, handlerRefresh, "PUT", "Content-Type", "application/json", "Authorization", refresh);
                    threadConexion.start();
                }
            }
        };
        timer.schedule(refreshToken, 60000, 180000);

        this.handlerRefresh = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                synchronized (this) {
                    Bundle datos = msg.getData();
                    try {
                        JSONObject respuesta = new JSONObject(datos.getString("MENSAJE"));
                        String estadoPeticion = respuesta.getString("success");

                        if (estadoPeticion.equals("false")) {
                            Log.i(PROYECTO + "->" + TAG, "Fallo de refresh");
                            return;
                        } else {
                            Log.i(PROYECTO + "->" + TAG, "Refresh exitoso");
                            token = respuesta.getString("token");
                            tokenRefresh = respuesta.getString("token_refresh");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        };

        this.handlerEvento = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                synchronized (this) {
                    Bundle datos = msg.getData();
                    try {
                        JSONObject respuesta = new JSONObject(datos.getString("MENSAJE"));
                        String estadoPeticion = respuesta.getString("success");

                        if (estadoPeticion.equals("false")) {
                            Log.i(PROYECTO + "->" + TAG, "Fallo de registro de evento");
                            return;
                        } else {
                            Log.i(PROYECTO + "->" + TAG, "Registro de evento ok");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
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
                    this.datosAcel = txt;
                    break;
                case Sensor.TYPE_LIGHT:
                    txt = "Luz ambiente: ";
                    txt += event.values[0] + "lx";
                    datosSensorLuz.setText(txt);
                    this.datosLuz = txt;
                    break;
                case Sensor.TYPE_ORIENTATION:
                    txt = "Orientacion:\n";
                    txt += "Acimut: " + getAcimut(event.values[0]) + "\n";
                    txt += "y: " + event.values[1] + "\n";
                    txt += "z: " + event.values[2] + "\n";
                    datosGiroscopo.setText(txt);
                    this.datosOri = txt;
                    break;
            }
        }
    }

    private String getAcimut(float value) {

        if (value < 22.5)
            return "N";
        if (value >= 22.5 && value < 67.5)
            return "NE";
        if (value >= 67.5 && value < 122.5)
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

    @Override
    protected void onStop() {
        super.onStop();
        detenerSensores();
    }

    @SuppressLint("LongLogTag")
    @Override
    protected void onDestroy() {
        super.onDestroy();
        refreshToken.cancel();
        timer.cancel();
        Log.i(PROYECTO + "->" + TAG, "Actualizacion de token finalizada");
    }

    private synchronized void detenerSensores() {
        mSensorManager.unregisterListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER));
        mSensorManager.unregisterListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT));
        mSensorManager.unregisterListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION));
    }

    private void initSensores() {
        mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
        mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION), SensorManager.SENSOR_DELAY_NORMAL);
        mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT), SensorManager.SENSOR_DELAY_NORMAL);
    }

    public void eventoRegistrarOrientacion(View view) {
        Date date = new Date();
        paqueteDatos = new JSONObject();
        try {
            paqueteDatos.put("env", "TEST");
            paqueteDatos.put("type_events", "Lectura de sensor");
            paqueteDatos.put("description", DateFormat.getDateTimeInstance().format(date) + "Datos: " + this.datosOri);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (verificarConexion()) {
            HiloConexion hiloOrientacion = new HiloConexion(URI_EVENT, paqueteDatos, handlerEvento, "POST", "Content-Type", "application/json", "Authorization", "Bearer" + " " + token);
            hiloOrientacion.start();
        } else {
            Toast.makeText(this, "Conexion: No disponible", Toast.LENGTH_SHORT).show();
            return;
        }
    }

    public void eventoRegistrarLuz(View view) {
        Date date = new Date();
        paqueteDatos = new JSONObject();
        try {
            paqueteDatos.put("env", "TEST");
            paqueteDatos.put("type_events", "Lectura de sensor");
            paqueteDatos.put("description", DateFormat.getDateTimeInstance().format(date) + "Datos: " + datosLuz);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (verificarConexion()) {
            HiloConexion hiloLuz = new HiloConexion(URI_EVENT, paqueteDatos, handlerEvento, "POST", "Content-Type", "application/json", "Authorization", "Bearer" + " " + token);
            hiloLuz.start();
        } else {
            Toast.makeText(this, "Conexion: No disponible", Toast.LENGTH_SHORT).show();
            return;
        }
    }

    public void eventoRegistrarAcelerometro(View view) {
        Date date = new Date();
        paqueteDatos = new JSONObject();
        try {
            paqueteDatos.put("env", "TEST");
            paqueteDatos.put("type_events", "Lectura de sensor");
            paqueteDatos.put("description", DateFormat.getDateTimeInstance().format(date) + "Datos: " + this.datosOri);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (verificarConexion()) {
            HiloConexion hiloAcel = new HiloConexion(URI_EVENT, paqueteDatos, handlerEvento, "POST", "Content-Type", "application/json", "Authorization", "Bearer" + " " + token);
            hiloAcel.start();
        } else {
            Toast.makeText(this, "Conexion: No disponible", Toast.LENGTH_SHORT).show();
            return;
        }
    }

    public boolean verificarConexion() {
        ConnectivityManager cm = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null && activeNetwork.isConnectedOrConnecting()) {
            return true;
        } else {
            return false;
        }
    }
}