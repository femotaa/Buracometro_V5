package com.example.felipe.buracometro_v5.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.ImageButton;
import android.widget.ProgressBar;

import com.example.felipe.buracometro_v5.R;

public class TelaPrimeiroAcesso extends Activity
{
    private ImageButton btnSincronizar;
    private ProgressBar mProgress;
    private Handler handler = new Handler();
    int progresso = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tela_primeiro_acesso);

        btnSincronizar  = (ImageButton) findViewById(R.id.btnSincronizar);
        mProgress       = (ProgressBar) findViewById(R.id.circularProgressbar1);

        AlertDialog infoSobre;
        AlertDialog.Builder mensagemSobre = new AlertDialog.Builder(this);

        mensagemSobre.setTitle("Bem vindo!");
        mensagemSobre.setMessage("Não identificamos registros neste celular.\nDeseja tentar recuperar seus dados anteriores, caso tenha?");

        mensagemSobre.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                sincronizarDadoComServidor();
                dialog.dismiss();

            }
        });

        mensagemSobre.setNegativeButton("Não", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                mudaDeTela();
                dialog.dismiss();
            }
        });

        mensagemSobre.setCancelable(false); //Para o BackButton nao funcionar no dialog
        infoSobre = mensagemSobre.create();
        infoSobre.show();

    }

    @Override
    protected void onResume() {
        super.onResume();

    }


    public void sincronizarDadoComServidor()
    {
        new Thread(new Runnable() {
            public void run() {

                Looper.prepare();

                handler.post(new Runnable() {
                    public void run() {

                        try {
                            // ---simulate doing some work---
                            Thread.sleep(50);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        btnSincronizar.setBackgroundDrawable(getResources().getDrawable(R.drawable.btn_sincronizar_progress));

                    }});

            }
        }).start();

        new Thread(new Runnable() {
            public void run() {

                progresso = 0;
                mProgress.setProgress(0);

                while(progresso <= mProgress.getMax())
                {

                    try {
                        // ---simulate doing some work---
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    mProgress.setProgress(progresso);
                    progresso++;

                }

                mudaDeTela();
//                mHandler.sendEmptyMessage(0);
            }
        }).start();

    }


    public void mudaDeTela(){
        Intent mudarDeTela = new Intent(getBaseContext(), MainActivity.class);
        startActivity(mudarDeTela);
        finish();

    }




}
