package com.example.migue.adivinalabandera;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by MIGUE on 26/01/2018.
 */

public class GestionPaises extends AsyncTask {

    private ArrayList listapaises;
    private Activity actividad;
    private boolean ok;

    public GestionPaises(Activity acti) {
        this.listapaises=null;
        this.actividad=acti;
    }

    public ArrayList getListapaises() {
        return listapaises;
    }

    public void setListapaises(ArrayList listapaises) {
        this.listapaises = listapaises;
    }

    public Activity getActividad() {
        return actividad;
    }

    public void setActividad(Activity actividad) {
        this.actividad = actividad;
    }

    public boolean isOk() {
        return ok;
    }

    public void setOk(boolean ok) {
        this.ok = ok;
    }

    @Override
    protected Object doInBackground(Object[] objects) {

        //Creamos la URL y nos conectamos para obtener los datos de la web
        URL url;
        HttpURLConnection urlConnection = null;
        try {
            url = new URL("https://cursoandroid.000webhostapp.com/banderas.html");

            urlConnection = (HttpURLConnection) url
                    .openConnection();

            int estado=urlConnection.getResponseCode();

            System.out.println("polleteeee "+estado);

            if(estado==200){
                //Obtenemos los datos y los metemo en el InputStream para procesarlos
                ok=true;
                InputStream in = urlConnection.getInputStream();

                InputStreamReader isw = new InputStreamReader(in);

                //Creamos un buffer para leer linea a linea los datos
                BufferedReader inc = new BufferedReader(isw);

                //Declaramos la linea los patrones para buscar y las lista para almacenar los valores
                String linea;
                Pattern p = Pattern.compile("<td><img src=\".*");
                Pattern pa = Pattern.compile("<td class=\"nombrePais\">.*");
                ArrayList<String> listaurl=new ArrayList();
                ArrayList<String> listanombres=new ArrayList();
                ArrayList<Pais> paistemp=new ArrayList();

                while ((linea = inc.readLine()) != null) {
                    Matcher m = p.matcher(linea);
                    Matcher ma = pa.matcher(linea);
                    String uri="",nom="";
                    //Vamos recorriendo el fichero y asignando a las listas los valores que coincidan
                    if(m.find()){
                        //System.out.println(linea.split("\"")[1]);
                        uri=linea.split("\"")[1];
                        listaurl.add(uri);
                    }
                    if(ma.find()){
                        //System.out.println(linea.substring(35).split("<")[0]);
                        nom=linea.substring(35).split("<")[0];
                        listanombres.add(nom);
                    }

                }
                //System.out.println(listanombres.size()+"   fds  "+listaurl.size());
                //Creamos la lista de la clase y la rellenamos de Objetos Pais

                for (int i = 0; i <listanombres.size() ; i++) {
                    paistemp.add(new Pais(listanombres.get(i),listaurl.get(i)));
                }
                //Acaba el hilo y cerramos el Stream
                inc.close();
                this.listapaises=paistemp;
                System.out.println("he acabado");

            }
            else{
                this.listapaises=new ArrayList();
                ok=false;
            }


        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }

        return null;
    }

    public void cierra(){
        actividad.finish();
    }
}
