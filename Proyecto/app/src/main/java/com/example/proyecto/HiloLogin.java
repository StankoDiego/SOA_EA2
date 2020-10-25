package com.example.proyecto;

import org.json.JSONObject;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;

public class HiloLogin extends Thread {

    private String uri;
    private JSONObject paquete;

    private HttpURLConnection conexion;
    private DataOutputStream out;
    private DataInputStream in;
    private int codigoRespuesta;

    HiloLogin(String uri, JSONObject paquete){
        this.uri = uri;
        this.paquete = paquete;
    }

    @Override
    public void run() {
        /*URL url = new URL(uri);
        conexion = (HttpURLConnection) url.openConnection();

        try {
            conexion.setRequestMethod("POST");
        } catch (ProtocolException e) {
            e.printStackTrace();
        }
        conexion.setRequestProperty("Content-Type", "application/json");

        conexion.setDoOutput(true);
        conexion.setDoInput(true);
        */
     }
}