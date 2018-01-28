package com.example.migue.adivinalabandera;


import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;

import static java.lang.Thread.sleep;


public class MainActivity extends AppCompatActivity {

    //Declaramos la lista donde almacenaremos los objetos Pais
    ArrayList<Pais> listapaises;
    Pais paiscorrecto;
    ImageView imaok;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Declaramos el hilo
        imaok=findViewById(R.id.imagenok);
        final GestionPaises ges = new GestionPaises(this);
        //Ejecutamos el hilo para cargar los paises
        ges.execute();
        //Bucle que controla que haya terminado de leer todos los paises
        while (ges.getListapaises() == null) {

        }
        if (!ges.isOk()) {
            
            //Muestra un dialogo que para avisar de que vamos a cerrar el juego
            AlertDialog alerti;
            AlertDialog.Builder dialog=new AlertDialog.Builder(this);
            dialog.setTitle("ERROR");
            dialog.setMessage("No ha sido posible cargar las banderas se procedera a cerrar la aplicación");
            dialog.setPositiveButton(
                    "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                            ges.cierra();
                            dialog.cancel();

                        }
                    });
            alerti = dialog.create();
            alerti.show();
        }
        else{

            //Asigamos la lista que contiene los paises para utilizarla
            listapaises = ges.getListapaises();
            //System.out.println(listapaises.get(0).getNombre()+"fdisifud");

            //Asignamos los botones de la vista
            Button boton1 = findViewById(R.id.boton1);
            Button boton2 = findViewById(R.id.boton2);
            Button boton3 = findViewById(R.id.boton3);
            Button boton4 = findViewById(R.id.boton4);

            //Creamos una lista
            final ArrayList<Button> libotones = new ArrayList();
            libotones.add(boton1);
            libotones.add(boton2);
            libotones.add(boton3);
            libotones.add(boton4);

            paiscorrecto = asignabotones(libotones, listapaises);

            for (int i = 0; i < libotones.size(); i++) {

                final Button b = libotones.get(i);

                b.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    evaluarespuesta(b,libotones);

                    }
                });

            }


    }





    }

    public void evaluarespuesta(Button b, ArrayList libotones){
        if (b.getText().equals(paiscorrecto.getNombre())) {

            Toast.makeText(MainActivity.this, "Correcto", Toast.LENGTH_SHORT).show();
            imaok.setImageResource(R.drawable.ok);

            paiscorrecto = asignabotones(libotones, listapaises);

        } else {
            Toast.makeText(MainActivity.this, "Mojon", Toast.LENGTH_SHORT).show();
            imaok.setImageResource(R.drawable.mal);
        }
    }


    public void asignaimagen(Pais pais){

        BajaImagen baima=new BajaImagen();
        baima.setDireccion(pais.getUrl());
        baima.execute();
        while(baima.getBitmap() == null){

        }
        ImageView t=findViewById(R.id.caja);

        t.setImageBitmap(baima.getBitmap());

    }

    public Pais asignabotones(ArrayList<Button> botones,ArrayList paises){
        //Seleccionamos un pais al azaar
        Pais pais= (Pais) paises.get((int) (Math.random()*paises.size()));

        //Asignamos el pais a un boton y asignamos la imagen
        asignaimagen(pais);
        //desordenamos la lista
        Collections.shuffle(botones);
        botones.get(0).setTag(pais);
        botones.get(0).setText(pais.getNombre());
            for (int i = 1; i < botones.size(); i++) {

                Pais paisno = (Pais) paises.get((int) (Math.random()*paises.size()));
                botones.get(i).setTag(paisno);
                botones.get(i).setText(paisno.getNombre());
            }


        return pais;
    }



}
