package com.example.proyecto;

import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class HiloRegistrar extends Thread {


    private static final String TAG = "ThreadActivity";
    private int codigoRespuesta;
    private String mensajeRecibido;
    private JSONObject paquete;
    private String uri;

    HiloRegistrar(String uri, JSONObject paqueteDatos) {
        this.paquete = paqueteDatos;
        this.uri = uri;
    }


    private synchronized void ejecutarPost() {
        HttpURLConnection urlConnection = null;

        String resul = "";

        try{
            URL mUrl = new URL(uri);
            urlConnection = (HttpURLConnection) mUrl.openConnection();
            urlConnection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            urlConnection.setDoOutput(true);
            urlConnection.setDoInput(true);
            urlConnection.setConnectTimeout(5000);
            urlConnection.setRequestMethod("POST");

            DataOutputStream wr = new DataOutputStream(urlConnection.getOutputStream());
            wr.write(paquete.toString().getBytes("UTF-8"));
            wr.flush();
            urlConnection.connect();

            int respondeCode = urlConnection.getResponseCode();

            if (respondeCode == HttpURLConnection.HTTP_OK || respondeCode == HttpURLConnection.HTTP_CREATED){
                InputStreamReader inputStream =  new InputStreamReader(urlConnection.getInputStream());
                resul = inputToString(inputStream);
            } else if (respondeCode == HttpURLConnection.HTTP_BAD_REQUEST){
                InputStreamReader inputStreamReader = new InputStreamReader(urlConnection.getErrorStream());
                resul = inputToString(inputStreamReader);
            } else{
                resul = "NO_OK";
            }

            wr.close();
            urlConnection.disconnect();
        }catch (Exception e){
            Log.i(TAG, e.toString());
            return;
        }
        Log.i(TAG, resul.toString());
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
        Log.i(TAG, "startThread");
        ejecutarPost();
    }
}
