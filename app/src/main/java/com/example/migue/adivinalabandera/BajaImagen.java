package com.example.migue.adivinalabandera;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by MIGUE on 26/01/2018.
 */

public class BajaImagen extends AsyncTask {
    private Bitmap bitmap;
    private String direccion;

    public BajaImagen() {
        bitmap=null;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    @Override
    protected Object doInBackground(Object[] objects) {
        URL url;
        try {
            url = new URL("https://cursoandroid.000webhostapp.com/"+this.direccion);
            bitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream());
        } catch (MalformedURLException e) {

            e.printStackTrace();
        } catch (IOException e) {

            e.printStackTrace();
        }
        return null;
    }


}
