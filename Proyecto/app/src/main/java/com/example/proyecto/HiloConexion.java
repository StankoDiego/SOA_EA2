package com.example.proyecto;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import android.os.Handler;

public class HiloConexion extends Thread {
    private static final String PROYECTO = "PROYECTO";
    private static final String TAG = "HILO_CONEXION";

    private JSONObject paquete;
    private String uri;
    private Handler handler;
    private String accionRecibida;
    private String key;
    private String value;

    public HiloConexion(String uri, JSONObject paqueteDatos, Handler handler, String accion, String key, String value) {
        this.paquete = paqueteDatos;
        this.uri = uri;
        this.handler = handler;
        this.accionRecibida = accion;
        this.key = key;
        this.value = value;
    }

    @SuppressLint("LongLogTag")
    private synchronized void ejecutarPeticion() {
        HttpURLConnection urlConnection = null;
        String resul = "";
        try {
            URL mUrl = new URL(this.uri);
            urlConnection = (HttpURLConnection) mUrl.openConnection();
            urlConnection.setRequestProperty(this.key, this.value + "; charset=UTF-8");
            urlConnection.setDoOutput(true);
            urlConnection.setDoInput(true);
            urlConnection.setConnectTimeout(5000);
            urlConnection.setRequestMethod(this.accionRecibida);

            DataOutputStream wr = new DataOutputStream(urlConnection.getOutputStream());
            wr.write(paquete.toString().getBytes("UTF-8"));
            wr.flush();
            Log.i(PROYECTO + "->" + TAG + "->" + "PETICION ENVIADA", paquete.toString());
            urlConnection.connect();

            int respondeCode = urlConnection.getResponseCode();

            if (respondeCode == HttpURLConnection.HTTP_OK || respondeCode == HttpURLConnection.HTTP_CREATED) {
                InputStreamReader inputStream = new InputStreamReader(urlConnection.getInputStream());
                resul = inputToString(inputStream);
            } else if (respondeCode == HttpURLConnection.HTTP_BAD_REQUEST) {
                InputStreamReader inputStreamReader = new InputStreamReader(urlConnection.getErrorStream());
                resul = inputToString(inputStreamReader);
            } else {
                resul = "NO_OK";
            }

            wr.close();
            urlConnection.disconnect();
        } catch (Exception e) {
            Log.i(PROYECTO + "->" + TAG, e.toString());
            return;
        }
        Log.i(PROYECTO + "->" + TAG + "->" + "PAQUETE RECIBIDO", resul);
        Message msg = new Message();
        Bundle datos = new Bundle();
        datos.putString("MENSAJE", resul);
        msg.setData(datos);

        this.handler.sendMessage(msg);
    }


    private String inputToString(InputStreamReader inputStream) throws IOException {
        BufferedReader br = new BufferedReader(inputStream);
        StringBuilder response = new StringBuilder();
        String responseLine = null;
        while ((responseLine = br.readLine()) != null) {
            response.append(responseLine + "\n");
        }
        br.close();
        return response.toString();
    }

    @Override
    public void run() {
        Log.i(PROYECTO + "->" + TAG, "startThread");
        ejecutarPeticion();
        Log.i(PROYECTO + "->" + TAG, "finishThread");
    }
}
