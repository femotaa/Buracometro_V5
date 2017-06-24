package com.example.felipe.buracometro_v5.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.felipe.buracometro_v5.R;
import com.example.felipe.buracometro_v5.util.FuncoesDoSistema;


public class TelaConfiguracoes extends Fragment {

    private static final String TEXTO_TOOLBAR = "Configurações";

    View view;
    Button btnSincronizar;
    Button btnDeletarDadosLocais;

    ProgressBar mProgress;
    ProgressBar mProgressDeleta;
    ToggleButton btnPrefManual;

    String identificador;
    boolean mudaImagem = true;
    int resultado = -1;
    int progresso = 0;
    private Handler handler = new Handler();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fr_tela_configuracoes, container, false);

        ImageView imgToolbar = (ImageView) getActivity().findViewById(R.id.img_icone);
        imgToolbar.setBackgroundDrawable(getResources().getDrawable(R.drawable.icone_configuracoes));

        TextView textoToolbar = (TextView) getActivity().findViewById(R.id.texto_toolbar);
        textoToolbar.setText(TEXTO_TOOLBAR);

        btnSincronizar  = (Button) view.findViewById(R.id.btnSincronizar);
        btnDeletarDadosLocais = (Button) view.findViewById(R.id.btnDeletaLocal);
        mProgress       = (ProgressBar) view.findViewById(R.id.circularProgressbar1);
        mProgressDeleta = (ProgressBar) view.findViewById(R.id.progressBarDeleta);

        btnPrefManual   = (ToggleButton) view.findViewById(R.id.btnPrefManual);

        identificador   = Settings.Secure.getString(getActivity().getContentResolver(),Settings.Secure.ANDROID_ID);

        mProgressDeleta.setVisibility(View.INVISIBLE);
        btnPrefManual   = (ToggleButton) view.findViewById(R.id.btnPrefManual);

        //Pega as configuracoes escolhidas pelo usuário
        SharedPreferences settings = getActivity().getSharedPreferences("preferencias", 0);
        boolean mostraBtnManual = settings.getBoolean("botaoManual", false);

        if(mostraBtnManual){
            btnPrefManual.setChecked(true);
        }

        return view;
    }

    @Override
    public void onResume()
    {
        super.onResume();

        Resources res = getResources();
        Drawable drawable = res.getDrawable(R.drawable.barra_progresso_circular);
        mProgress.setProgress(75);   // Main Progress
        mProgress.setMax(100);
        mProgress.setProgressDrawable(drawable);

        btnPrefManual.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {

                    SharedPreferences settings = getActivity().getSharedPreferences("preferencias", 0);
                    SharedPreferences.Editor editor = settings.edit();
                    editor.putBoolean("botaoManual", true);
                    editor.commit();

                } else {

                    SharedPreferences settings = getActivity().getSharedPreferences("preferencias", 0);
                    SharedPreferences.Editor editor = settings.edit();
                    editor.putBoolean("botaoManual", false);
                    editor.commit();

                }
            }
        });

        btnSincronizar.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                acaoSincroniza();
            }
        });

        btnDeletarDadosLocais.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                deletarTodosLocais();
            }
        });

    }


    public void acaoSincroniza(){

        btnSincronizar.setEnabled(false);

        if (mudaImagem == false){

            btnSincronizar.setBackgroundDrawable(getResources().getDrawable(R.drawable.btn_sincronizar_neutro));
            btnSincronizar.setEnabled(true);
            mProgress.setProgress(75);
            mudaImagem = true;

        }else{

            new Thread(new Runnable() {
                public void run() {

                    Looper.prepare();

                    handler.post(new Runnable() {
                        public void run() {
                            btnSincronizar.setBackgroundDrawable(getResources().getDrawable(R.drawable.btn_sincronizar_progress));
                        }});

                    resultado = -1;
                    sincronizaComBDExterno();

                }
            }).start();

            new Thread(new Runnable() {
                public void run() {

                    progresso = 0;
                    mProgress.setProgress(0);

                    while(progresso <= mProgress.getMax())
                    {
                        while(resultado != 0 && resultado != 1){

                            try {
                                // ---simulate doing some work---
                                Thread.sleep(100);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }

                            if (mProgress.getProgress() > 30){
                                try {
                                    // ---simulate doing some work---
                                    Thread.sleep(250);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }

                            if (mProgress.getProgress() > 70){
                                try {
                                    // ---simulate doing some work---
                                    Thread.sleep(450);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }

                            mProgress.setProgress(progresso);
                            progresso++;
                        }

                        try {
                            // ---simulate doing some work---
                            Thread.sleep(20);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        mProgress.setProgress(progresso);
                        progresso++;

                    }

                    mHandler.sendEmptyMessage(0);

                }
            }).start();
        }
    }


    public void sincronizaComBDExterno(){
        FuncoesDoSistema fds = new FuncoesDoSistema(getContext());
        resultado = fds.sincronizaComBDExterno(identificador);
    }


    Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {

            btnSincronizar.setEnabled(true);
            mudaImagem = false;

            switch (resultado)
            {
                case 0:
                    btnSincronizar.setBackgroundDrawable(getResources().getDrawable(R.drawable.btn_sincronizar_ok));
                    Toast.makeText(getContext(), "Sincronizado com sucesso!", Toast.LENGTH_SHORT).show();
                    break;
                case 1:
                    btnSincronizar.setBackgroundDrawable(getResources().getDrawable(R.drawable.btn_sincronizar_erro));
                    Toast.makeText(getContext(), "Erro ao sincronizar!", Toast.LENGTH_SHORT).show();
                    break;
                case 2:
                    btnSincronizar.setBackgroundDrawable(getResources().getDrawable(R.drawable.btn_sincronizar_erro));
                    Toast.makeText(getContext(), "Erro ao sincronizar!", Toast.LENGTH_SHORT).show();
                    break;
            }

        }
    };


    //----------------------------------------------------------------------------------------
    //                                      DEMAIS METODOS
    //----------------------------------------------------------------------------------------

    public void deletarTodosLocais (){

        AlertDialog alertar;
        AlertDialog.Builder mensagemInfo = new AlertDialog.Builder(getContext());
        mensagemInfo.setTitle("Deseja realmente deletar registros do seu dispositivo? ");

        mensagemInfo.setPositiveButton(" Sim ", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                FuncoesDoSistema fs = new FuncoesDoSistema(getContext());
                fs.excluirTodosBuracosLocais();
            }
        });

        mensagemInfo.setNegativeButton(" Cancelar ", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        alertar = mensagemInfo.create();
        alertar.show();
    }

    public void deletarTodosServidor (View v){

//        FuncoesDoSistema fs = new FuncoesDoSistema(this);
//        fs.excluirTodosBuracosWEB();

    }

    public void mostraProgresso(boolean mostra){
        if(mostra)
        {
            mProgressDeleta.setVisibility(View.VISIBLE);
            mProgressDeleta.setIndeterminate(true);
        }else
        {
            mProgressDeleta.setVisibility(View.INVISIBLE);
        }
    }


}
