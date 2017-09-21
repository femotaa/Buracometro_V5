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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.felipe.buracometro_v5.R;
import com.example.felipe.buracometro_v5.dao.BuracoLocalDao;
import com.example.felipe.buracometro_v5.dao.DaoFirebase;
import com.example.felipe.buracometro_v5.listeners.OnGetFirebaseBuracosListener;
import com.example.felipe.buracometro_v5.modelo.Buraco;
import com.example.felipe.buracometro_v5.modelo.Usuario;
import com.google.firebase.database.DatabaseError;

import java.util.ArrayList;


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

    Usuario usuarioAtual = new Usuario();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.tela_configuracoes, container, false);

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
        String emailAtual = settings.getString("login","");
        String idUsuarioAtual = settings.getString("IdLogin","");
        String nomeAtual = settings.getString("nome","");

        usuarioAtual.setEmail(emailAtual);
        usuarioAtual.setId(idUsuarioAtual);
        usuarioAtual.setNome(nomeAtual);


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
                    editor.apply();

                } else {

                    SharedPreferences settings = getActivity().getSharedPreferences("preferencias", 0);
                    SharedPreferences.Editor editor = settings.edit();
                    editor.putBoolean("botaoManual", false);
                    editor.apply();

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

        if (!mudaImagem){

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
                        while(resultado != 0 && resultado != 1 && resultado != 2){

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

        DaoFirebase daoFirebase = new DaoFirebase();

        daoFirebase.listarBuracosPorUsuario(usuarioAtual, new OnGetFirebaseBuracosListener() {
            @Override

            public void onStart() {

            }

            @Override
            public void onRetornoLista(ArrayList<Buraco> listaRetornada){

                Log.e("Tamanho", "" + listaRetornada.size());

                if(listaRetornada.isEmpty()){

                    resultado = 2;

                }else{

                    BuracoLocalDao daoLocal = new BuracoLocalDao(getContext(),usuarioAtual.getEmail());
                    try{
                        daoLocal.deletarTodosDoUsuario();

                    }catch (Exception e){
                        e.printStackTrace();
                    }

                    for(int i = 0; i<listaRetornada.size(); i++) {

                        try{
                            daoLocal.inserirBuraco(listaRetornada.get(i));
                        }catch (Exception e){
                            e.printStackTrace();
                            resultado = 1;
                        }

                        if (resultado == 1){
                            break;
                        }

                    }

                    if (resultado != 1){
                        resultado = 0;
                    }
                }

            }

            @Override
            public void onRetornoDuasLista(ArrayList<Buraco> buracosAbertos, ArrayList<Buraco> buracosTampados){

            }

            @Override
            public void onFailed(DatabaseError databaseError) {
            }

            @Override
            public void onRetornoExiste(Boolean existe){
            }

            @Override
            public void onRetornoBuraco(Buraco buraco) {
            }

        });

    }


    Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {

            btnSincronizar.setEnabled(true);
            mudaImagem = false;

            try{
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
                        Toast.makeText(getContext(), "Não possui registro!", Toast.LENGTH_SHORT).show();
                        break;
                }
            }catch (Exception e){
                e.printStackTrace();
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
                BuracoLocalDao dao = new BuracoLocalDao(getContext(), usuarioAtual.getEmail());
                try{
                    dao.deletarTodosDoUsuario();
                }catch(Exception e){
                    e.printStackTrace();
                }
                Toast.makeText(getContext(), "Seus registros foram deletados do dispositivo.", Toast.LENGTH_SHORT).show();

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

}
