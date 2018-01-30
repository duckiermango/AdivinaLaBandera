package com.example.migue.adivinalabandera;


import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;

import static java.lang.Thread.sleep;


public class MainActivity extends AppCompatActivity {

    //Declaramos la lista donde almacenaremos los objetos Pais
    ArrayList<Pais> listapaises;
    Pais paiscorrecto;
    ImageView imaok;
    MediaPlayer mp;
    MediaPlayer mpfin;
    ArrayList<Button> libotones;
    TextView tiempo;
    TextView puntos;
    TextView puntosmal;
    static boolean flag;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Asignamos los elementos a sus variables
        imaok=findViewById(R.id.imagenok);
        tiempo=findViewById(R.id.cuenta);
        puntos=findViewById(R.id.puntos);
        puntosmal=findViewById(R.id.puntosmal);
        flag=false;
        //Intanciamos los sonidos de la partida
        mp=MediaPlayer.create(this,R.raw.cli);
        mpfin=MediaPlayer.create(this,R.raw.sonifin);
        //Asignamos los botones de la vista
        Button boton1 = findViewById(R.id.boton1);
        Button boton2 = findViewById(R.id.boton2);
        Button boton3 = findViewById(R.id.boton3);
        Button boton4 = findViewById(R.id.boton4);

        //Creamos una lista
        libotones = new ArrayList();
        libotones.add(boton1);
        libotones.add(boton2);
        libotones.add(boton3);
        libotones.add(boton4);

        //Apaga los botones
        controlbotones(libotones,false);
        this.setTitle("");

        //Asignamos los onclick
        for (int i = 0; i < libotones.size(); i++) {

            final Button b = libotones.get(i);

            b.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    mp.start();
                    evaluarespuesta(b,libotones);

                }
            });

        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){

        getMenuInflater().inflate(R.menu.menu, menu);

        return true;

    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem opcion_menu) {

        int id=opcion_menu.getItemId();
        boolean flag=false;

        if(id==R.id.inicia) {
            //Asignamos el cerrojo a true
            //Muestra un dialogo que para avisar del inicio el juego
            AlertDialog alerti;
            AlertDialog.Builder dialog=new AlertDialog.Builder(this);
            dialog.setTitle("INICIO¡");
            dialog.setMessage("La partida va a comenzar...PREPARADO¡");
            dialog.setPositiveButton(
                    "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            //Habilitamos los botones
                            controlbotones(libotones,true);
                            cargajuego();
                            //Iniciamos el temporizador
                            temporizador();
                            opcion_menu.setEnabled(false);
                            //Cerramos el dialogo
                            dialog.cancel();

                        }
                    });
            alerti = dialog.create();
            alerti.show();


            return true;
        }

        if(id==R.id.acerca){
            //Muestra un dialogo que para avisar de que vamos a cerrar el juego
            AlertDialog alerti;
            AlertDialog.Builder dialog=new AlertDialog.Builder(this);
            dialog.setTitle("Acerca de");
            dialog.setMessage("Realizada por miguel angel griñan arocas");
            dialog.setPositiveButton(
                    "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                            dialog.cancel();

                        }
                    });
            alerti = dialog.create();
            alerti.show();

            return true;
        }
        if(id==R.id.reinicia){
            //IF para controlar que no es el inicio del juego
            if(this.flag){
                //Muestra un dialogo que para avisar de que vamos a cerrar el juego
                AlertDialog alerti;
                AlertDialog.Builder dialog=new AlertDialog.Builder(this);
                dialog.setTitle("Reiniciar");
                dialog.setMessage("Esta seguro de que desea reiniciar la partida?");
                dialog.setPositiveButton(
                        "SI",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //Seteamos las puntuaciones a 0
                                puntos.setText("0");
                                puntosmal.setText("0");
                                //Habilitamos los botones
                                controlbotones(libotones,true);
                                cargajuego();

                                //Iniciamos el temporizador
                                temporizador();

                                dialog.cancel();

                            }
                        });
                dialog.setNegativeButton(
                        "NO",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                dialog.cancel();

                            }
                        });
                alerti = dialog.create();
                alerti.show();

            }
            else{

            }
            return true;
        }

        //Opcion de enu para oompartir la nota con aplicaciones de texto plano
        if(id==R.id.comparte){
            //IF para controlar que no es el inicio del juego que estamos a 0
            if(this.flag){
                // Creamos el intent le asignamos el tipo y el contenido del intent
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("text/plain");
                i.putExtra(Intent.EXTRA_SUBJECT,"Puntuacion");
                //Preparamos el String del intent
                String pun = "!Mira mi puntuacion en \"AdivinaLaBandera\"¡ " +
                        "=> BIEN: "+puntos.getText().toString()+" / MAL: "+puntosmal.getText().toString();
                i.putExtra(Intent.EXTRA_TEXT, pun);
                //Se lanza el intent
                startActivity(i);
            }
            else{

            }


            return true;
        }

        return true;
    }

    public void cargajuego(){
        //Declaramos el Hilo
        final GestionPaises ges = new GestionPaises(this);
        MainActivity.flag=false;
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
            //Elegimos un pais al azaar y lo asignamos
            paiscorrecto = asignabotones(libotones, listapaises);


        }
    }

    public void temporizador(){
        //Inicializamos el contador
        new CountDownTimer(31000, 1000) {

            public void onTick(long millisUntilFinished) {
                //Actualizamos el temporizador segundo a segundo
                String tiem="" + millisUntilFinished / 1000;
                tiempo.setText(tiem);
            }

            public void onFinish() {
                //Acaba el tiempo desactivamos los botones
                controlbotones(libotones,false);
                tiempo.setText("FIN");
                MainActivity.flag=true;
                mpfin.start();
            }
        }.start();
    }

    public void controlbotones(ArrayList<Button> liboton,boolean flag){

        //Recorremos los botones para activarlos/desactivarlos
        for (int i = 0; i < liboton.size(); i++) {
            liboton.get(i).setEnabled(flag);
        }

    }

    public void evaluarespuesta(Button b, ArrayList libotones){

        if (b.getText().equals(paiscorrecto.getNombre())) {
            //Asignamos la image de error contamos la respuesta equivocada
            imaok.setImageResource(R.drawable.ok);
            //Asignamos un punto cada vez que acertamos una bandera
            String punt=String.valueOf(Integer.parseInt(puntos.getText().toString())+1);
            puntos.setText(punt);
            //Cambiamos de bandera
            paiscorrecto = asignabotones(libotones, listapaises);

        } else {
            //Asignamos la image de error contamos la respuesta equivocada
            imaok.setImageResource(R.drawable.mal);
            //Asignamos un punto cada vez que acertamos una bandera
            String punt=String.valueOf(Integer.parseInt(puntosmal.getText().toString())+1);
            puntosmal.setText(punt);
            //Cambiamos de bandera
            paiscorrecto = asignabotones(libotones, listapaises);
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
        baima.cancel(true);

    }

    public Pais asignabotones(ArrayList<Button> botones,ArrayList paises){
        //Seleccionamos un pais al azaar
        Pais pais= (Pais) paises.get((int) (Math.random()*paises.size()));

        //Asignamos el pais a un boton y asignamos la imagen
        asignaimagen(pais);
        //Desordenamos la lista
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
