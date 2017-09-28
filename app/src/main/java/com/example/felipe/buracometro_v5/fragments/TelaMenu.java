package com.example.felipe.buracometro_v5.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.felipe.buracometro_v5.R;
import com.example.felipe.buracometro_v5.modelo.Buraco;
import com.example.felipe.buracometro_v5.modelo.Usuario;
import com.example.felipe.buracometro_v5.util.BuracometroUtil;
import com.example.felipe.buracometro_v5.util.PreencheDadosDoBuraco;


public class TelaMenu extends Fragment implements SensorEventListener {

    private static final String TEXTO_TOOLBAR = "";

    View view;
    Button btnAddBuraco;
    Button btnLigarBuracometro;
    ImageButton btnRegistros, btnAdicionar, btnMapa, btnEstatisticas;
    ImageView   buracoImagem;

    ProgressBar progressLigarBuracometro;
    boolean apertado = false;
    boolean isBuracometroLigado = false;
    private Handler handler = new Handler();

    Usuario usuarioAtual = new Usuario();

    private SensorManager senSensorManager;
    private Sensor        senAccelerometer;

    Handler handler1 = new Handler();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.tela_menu, container, false);

        ImageView imgToolbar = (ImageView) getActivity().findViewById(R.id.img_icone);
        imgToolbar.setBackgroundDrawable(getResources().getDrawable(R.drawable.icone_lista));

        TextView textoToolbar = (TextView) getActivity().findViewById(R.id.texto_toolbar);
        textoToolbar.setText(TEXTO_TOOLBAR);

        btnLigarBuracometro = (Button) view.findViewById(R.id.btnligarBuracometro);
        progressLigarBuracometro = (ProgressBar) view.findViewById(R.id.progressLigarBuracometro);

        btnRegistros = (ImageButton) view.findViewById(R.id.btnRegistrados);
        btnAdicionar = (ImageButton) view.findViewById(R.id.btnAdicionar);
        btnMapa = (ImageButton) view.findViewById(R.id.btnMapa);
        btnEstatisticas = (ImageButton) view.findViewById(R.id.btnEstatisticas);
        buracoImagem = (ImageView) view.findViewById(R.id.buracometroLigado);

        btnAddBuraco = (Button) view.findViewById(R.id.btnManual);

        SharedPreferences settings = getActivity().getSharedPreferences("preferencias", 0);
        boolean mostraBtnManual = settings.getBoolean("botaoManual", false);
        String emailAtual = settings.getString("login","");
        String idUsuarioAtual = settings.getString("IdLogin","");
        String nomeAtual = settings.getString("nome","");


        usuarioAtual.setEmail(emailAtual);
        usuarioAtual.setId(idUsuarioAtual);
        usuarioAtual.setNome(nomeAtual);

        if(mostraBtnManual){

            btnAddBuraco.setVisibility(View.VISIBLE);
        }

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        btnAddBuraco.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                encontrarDadosDoBuraco();
            }
        });



        btnLigarBuracometro.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch (event.getAction()){

                    case MotionEvent.ACTION_DOWN:

                        if(!isBuracometroLigado){
                            apertado = true;
                            progresso = 0;
                            progressLigarBuracometro.setProgress(0);
                            ativarProgressBar();
                            break;
                        }else{
                            desligarBuracometro();
                            break;
                        }

                    case MotionEvent.ACTION_UP:
                        apertado = false;
                        progresso = 0;
                        progressLigarBuracometro.setProgress(0);
                        break;
                }

                return false;
            }
        });

    }


    int progresso;
    public void ativarProgressBar (){

        progressLigarBuracometro.setMax(100);
        progressLigarBuracometro.setProgress(50);
        progressLigarBuracometro.setProgress(0);
        progressLigarBuracometro.setVisibility(View.VISIBLE);

        new Thread(new Runnable() {
            public void run() {

                while (apertado){

                    try {
                        // ---simulate doing some work---
                        Thread.sleep(15);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    progressLigarBuracometro.setProgress(progresso);
                    progresso++;

                     if(progressLigarBuracometro.getProgress() >= 100){

                         handler.post(new Runnable() {
                             public void run() {
                                 ligarBuracometro();
                             }});
                         break;
                     }
                }

                progresso = 0;
                handler.post(new Runnable() {
                    public void run() {
                        progressLigarBuracometro.setVisibility(View.INVISIBLE);
                        progressLigarBuracometro.setProgress(0);
                    }});


                apertado = false;
            }
        }).start();


    }

    public void ligarBuracometro (){

        if (!isBuracometroLigado){
            Log.e("Buracometro", "Ligado");
            btnRegistros.setVisibility(View.INVISIBLE);
            btnAdicionar.setVisibility(View.INVISIBLE);
            btnMapa.setVisibility(View.INVISIBLE);
            btnEstatisticas.setVisibility(View.INVISIBLE);

            buracoImagem.setVisibility(View.VISIBLE);
            buracoImagem.setBackgroundResource(R.drawable.img_buracometro_ligado);
            AnimationDrawable anim = (AnimationDrawable) buracoImagem.getBackground();
            anim.start();

            ativarSensores();
            isBuracometroLigado = true;
        }

    }

    public void desligarBuracometro (){

        if (isBuracometroLigado){
            Log.e("Buracometro", "Desligado");
            btnRegistros.setVisibility(View.VISIBLE);
            btnAdicionar.setVisibility(View.VISIBLE);
            btnMapa.setVisibility(View.VISIBLE);
            btnEstatisticas.setVisibility(View.VISIBLE);

            buracoImagem.setVisibility(View.INVISIBLE);
            buracoImagem.setBackgroundResource(R.drawable.img_buracometro_ligado);

            desativarSensores();
            isBuracometroLigado = false;

        }

    }


    //----------------------------------------------------------------------------------------
    //                 METODOS PARA UTILIZAÇÃO DOS SENSORES DE MOVIMENTO
    //----------------------------------------------------------------------------------------

    //Ver quais metodos do ciclo de vida do Android devera deixar o sensores de movimento ativos.
    @Override
    public void onPause() {
        super.onPause();
        //Quando celular inativo, desativar sensores do android
        if (isBuracometroLigado){
            desativarSensores();
        }
    }

    public void ativarSensores(){
        //senSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        senSensorManager = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
        senAccelerometer = senSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        senSensorManager.registerListener(this, senAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    public void desativarSensores (){
        senSensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {

        if (isBuracometroLigado)
        {
            Sensor mySensor = sensorEvent.sensor;
            if (mySensor.getType() == Sensor.TYPE_ACCELEROMETER)
            {
                float x = sensorEvent.values[0];
                float y = sensorEvent.values[1];
                float z = sensorEvent.values[2];

                //Regras para que o Buraco seja identificado
                if (x > 4 || y > 4)
                {
                    Log.e("Buraco", "IDENTIFICADO");
                    encontrarDadosDoBuraco();
//                  Toast.makeText(this, "Buraco identificado!", Toast.LENGTH_SHORT).show();
                }
            }

        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }



    //----------------------------------------------------------------------------------------
    //                 METODOS PARA GRAVAÇAO DO BURACO QUANDO IDENTIFICADO
    //----------------------------------------------------------------------------------------

    public void encontrarDadosDoBuraco(){

        new Thread(new Runnable() {
            public void run() {

                Looper.prepare();

                try{
                    PreencheDadosDoBuraco preencheDadosDoBuraco = new PreencheDadosDoBuraco(getContext());
                    final Buraco buracoEncontrado =  preencheDadosDoBuraco.preencherDados();
                    Log.e("buracoEcontrado", buracoEncontrado.toString());

                    handler1.post(new Runnable() {
                        public void run() {
                            if(!(buracoEncontrado.getRua() == null)){
                                Log.e("indo salvar", "............");
                                salvarBuraco(buracoEncontrado);
                            }else{
                                Log.e("Erro buraco", "buraco esta nulo: " + buracoEncontrado.toString());
                                Toast.makeText(getContext(), "Ocorreu erro ao encontrar dados de localização", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                }catch(Exception e){
                    Log.e("Erro Erro", "Adicionar Buraco");
                    e.printStackTrace();

                    handler1.post(new Runnable() {
                        public void run() {
                            Toast.makeText(getContext(), "Ocorreu erro ao encontrar dados de localização", Toast.LENGTH_SHORT).show();
                        }
                    });
                }

            }
        }).start();


    }

    public void salvarBuraco (Buraco buracoEncontrado){

        BuracometroUtil buracometroUtil = new BuracometroUtil(getContext(), usuarioAtual);
        try{
            int jaPosssuiBuraco = buracometroUtil.inserirBuraco(buracoEncontrado);

            if(jaPosssuiBuraco == 1){
                Toast.makeText(getContext(), "Você já cadastrou esse buraco", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(getContext(), "Buraco cadastrado!", Toast.LENGTH_SHORT).show();
            }

        }catch (Exception e){
            e.printStackTrace();
            Toast.makeText(getContext(), "Erro ao inserir buraco", Toast.LENGTH_SHORT).show();
        }
    }



}
