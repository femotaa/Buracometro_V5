package com.example.felipe.buracometro_v5.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.felipe.buracometro_v5.R;
import com.example.felipe.buracometro_v5.dao.DaoFirebase;
import com.example.felipe.buracometro_v5.listeners.OnGetFirebaseBuracosListener;
import com.example.felipe.buracometro_v5.listeners.OnGetFirebaseDados;
import com.example.felipe.buracometro_v5.modelo.Buraco;
import com.example.felipe.buracometro_v5.modelo.Cidade;
import com.example.felipe.buracometro_v5.modelo.Rua;
import com.google.firebase.database.DatabaseError;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class TelaEstatisticas extends Fragment {

    ProgressBar progressBarThread1;
    ProgressBar progressBarThread2;
    ProgressBar progressBarThread3;
    ProgressBar progressBarThread4;
    ProgressBar progressBarThread5;
    ProgressBar progressBarThread6;

    ImageView   imgErro;
    ImageView   imgErro2;
    ImageView   imgErro3;
    ImageView   imgErro4;
    ImageView   imgErro5;
    ImageView   imgErro6;

    ImageButton   btnInfo1;
    ImageButton   btnInfo2;
    ImageButton   btnInfo3;
    ImageButton   btnInfo4;
    ImageButton   btnInfo5;
    ImageButton   btnInfo6;

    //Campos Estatistica 1
    ProgressBar progressEstatistica1;
    TextView    textoPorcentagem1;
    TextView    textoInfo1;
    TextView    textoCidade1;

    //Campos Estatistica 2
    TextView    textoInfo2;
    TextView    textoQtdBuraco2;
    TextView    textoInforRegistro2;

    //Campos Estatistica 3
    ProgressBar progressEstatistica3;
    TextView    textoPorcentagem3;
    TextView    textoInfo3;
    TextView    textoCidade4;

    //Campos Estatistica 4
    TextView    textoInfo4;
    TextView    textoInfoContinua4;
    TextView    textoEndereco4;
    TextView    TextoQtdBuraco4;
    TextView    textoInforRegistro4;

    //Campos Estatistica 5
    TextView    textoInfo5;
    TextView    textoBuracoTampados5;
    TextView    textoInforRegistro5;

    //Campos Estatistica 6
    TextView    textoInfo6;
    TextView    textoInfoContinua6;
    TextView    textoEndereco6;
    TextView    textoInforDesde6;
    TextView    textoData6;
    TextView    textoQtdDias6;
    TextView    textoInfoDias6;
    TextView    textoInforRegistro6;

    private Handler handler1 = new Handler();
    private Handler handler2 = new Handler();
    private Handler handler3 = new Handler();
    private Handler handler4 = new Handler();
    private Handler handler5 = new Handler();
    private Handler handler6 = new Handler();

    View view;
    private static final String TEXTO_TOOLBAR = "Estatísticas";


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.tela_estatisticas,container,false);

        ImageView imgToolbar = (ImageView) getActivity().findViewById(R.id.img_icone);
        imgToolbar.setBackgroundDrawable(getResources().getDrawable(R.drawable.icone_estatisticas));

        TextView textoToolbar = (TextView) getActivity().findViewById(R.id.texto_toolbar);
        textoToolbar.setText(TEXTO_TOOLBAR);



        progressBarThread1 = (ProgressBar) view.findViewById(R.id.progress_back);
        progressBarThread2 = (ProgressBar) view.findViewById(R.id.progress_back2);
        progressBarThread3 = (ProgressBar) view.findViewById(R.id.progress_back3);
        progressBarThread4 = (ProgressBar) view.findViewById(R.id.progress_back4);
        progressBarThread5 = (ProgressBar) view.findViewById(R.id.progress_back5);
        progressBarThread6 = (ProgressBar) view.findViewById(R.id.progress_back6);

        imgErro            = (ImageView)   view.findViewById(R.id.img_erro1);
        imgErro2           = (ImageView)   view.findViewById(R.id.img_erro2);
        imgErro3           = (ImageView)   view.findViewById(R.id.img_erro3);
        imgErro4           = (ImageView)   view.findViewById(R.id.img_erro4);
        imgErro5           = (ImageView)   view.findViewById(R.id.img_erro5);
        imgErro6           = (ImageView)   view.findViewById(R.id.img_erro6);

        btnInfo1 = (ImageButton) view.findViewById(R.id.btn_info1);
        btnInfo2 = (ImageButton) view.findViewById(R.id.btn_info2);
        btnInfo3 = (ImageButton) view.findViewById(R.id.btn_info3);
        btnInfo4 = (ImageButton) view.findViewById(R.id.btn_info4);
        btnInfo5 = (ImageButton) view.findViewById(R.id.btn_info5);
        btnInfo6 = (ImageButton) view.findViewById(R.id.btn_info6);

        //Campos Estatistica 1
        progressEstatistica1    = (ProgressBar) view.findViewById(R.id.progressCidadeMaisRegistrada);
        progressEstatistica1.setProgress(0);
        textoPorcentagem1       = (TextView) view.findViewById(R.id.textoCidadeMaisRegistrada);
        textoInfo1              = (TextView) view.findViewById(R.id.textInfo_s1);
        textoCidade1            = (TextView) view.findViewById(R.id.textInfo);

        //Campos Estatistica 2
        textoInfo2          = (TextView) view.findViewById(R.id.textoTotalBuracos);
        textoQtdBuraco2     = (TextView) view.findViewById(R.id.textoTotalBuracosNum);
        textoInforRegistro2 = (TextView) view.findViewById(R.id.textoInfo2);

        //Campos Estatistica 3
        progressEstatistica3 = (ProgressBar) view.findViewById(R.id.progressCidadeMenosRegistrada);
        textoPorcentagem3    = (TextView) view.findViewById(R.id.textoCidadeMenosRegistrada);
        textoInfo3           = (TextView) view.findViewById(R.id.textInfo_s2);
        textoCidade4         = (TextView) view.findViewById(R.id.textInfo2);

        //Campos Estatistica 4
        textoInfo4          = (TextView) view.findViewById(R.id.textoEnderecoMaisRegistro);
        textoInfoContinua4  = (TextView) view.findViewById(R.id.textoEnderecoMaisRegistroC);
        textoEndereco4      = (TextView) view.findViewById(R.id.textoTotalEnderecoNum);
        TextoQtdBuraco4     = (TextView) view.findViewById(R.id.textoInfoNum4);
        textoInforRegistro4 = (TextView) view.findViewById(R.id.textoInfo4);


        //Campos Estatistica 5
        textoInfo5              = (TextView) view.findViewById(R.id.textoBuracosTampados);
        textoBuracoTampados5    = (TextView) view.findViewById(R.id.textoBuracosTampadosNum);
        textoInforRegistro5     = (TextView) view.findViewById(R.id.textoInfo5);

        //Campos Estatistica 6
        textoInforRegistro6 = (TextView) view.findViewById(R.id.textoInfo6);
        textoInfo6          = (TextView) view.findViewById(R.id.textoBuracoComMais6);
        textoInfoContinua6  = (TextView) view.findViewById(R.id.textoTempoAberto6);
        textoEndereco6      = (TextView) view.findViewById(R.id.textoRuaMaiorTempo6);
        textoInforDesde6    = (TextView) view.findViewById(R.id.textoDesde6);
        textoData6          = (TextView) view.findViewById(R.id.textoData6);
        textoQtdDias6       = (TextView) view.findViewById(R.id.textoDias6);
        textoInfoDias6      = (TextView) view.findViewById(R.id.textoInfoDias6);

        return view;
    }

    @Override
    public void onResume()
    {
        super.onResume();

        atualizaEstatistica1();
        atualizaEstatistica2();
        atualizaEstatistica3();
        atualizaEstatistica4();
        atualizaEstatistica5();
        atualizaEstatistica6();

        btnInfo1.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                mostrarInformacaoEstatitica1();
            }
        });

        btnInfo3.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                mostrarInformacaoEstatitica3();
            }
        });

        btnInfo4.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                mostrarInformacaoEstatitica4();
            }
        });

        btnInfo6.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                mostrarInformacaoEstatitica6();
            }
        });

    }



    //----------------------------------------------------------------------------------------
    //                     METODOS PARA ATUALIZAR CAMPOS DE ESTATISTICAS
    //----------------------------------------------------------------------------------------

    ArrayList<Cidade> listaDasCidades = new ArrayList<Cidade>();
    int progresso;
    public void atualizaEstatistica1(){

        DaoFirebase dao = new DaoFirebase();

        dao.buscarRegistrosDaCidade(true, new OnGetFirebaseDados() {

            @Override
            public void onStart() {
                progressBarThread1.setVisibility(View.VISIBLE);
                progressEstatistica1.setVisibility(View.INVISIBLE);
                textoPorcentagem1.setVisibility(View.INVISIBLE);
                textoInfo1.setText("Carregando...");
                textoCidade1.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onRetornoDados(int total) {}

            @Override
            public void registrosDaCidade(int total, ArrayList<Cidade> listaCidades) {

                final String cidade = listaCidades.get(0).cidade;

                double y,z;
                y = (double)listaCidades.get(0).qtd;
                z = (double)total;

                double x = (y / z) * 100;
                final double porcentagem = Math.round(x);

                Log.e("Porcentagem", "" + porcentagem);

                progressBarThread1.setVisibility(View.INVISIBLE);
                progressEstatistica1.setVisibility(View.VISIBLE);

                textoInfo1.setText("Cidade mais registrada:");

                textoCidade1.setVisibility(View.VISIBLE);
                textoCidade1.setText(cidade);

                btnInfo1.setVisibility(View.VISIBLE);

                new Thread(new Runnable() {
                    public void run() {

                        progresso = 0;
                        while(progresso < porcentagem){

                            try {
                                // ---simulate doing some work---
                                Thread.sleep(30);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }


                            handler1.post(new Runnable() {
                                public void run() {

                                    textoPorcentagem1.setVisibility(View.VISIBLE);
                                    textoPorcentagem1.setText(progresso + "%");

                                }
                            });

                            progresso++;
                            progressEstatistica1.setProgress(progresso);
                        }

                    }
                }).start();

                listaDasCidades = listaCidades;

            }

            @Override
            public void ruaComMaisRegistro(int total, ArrayList<Rua> listaRua){}

            @Override
            public void onFailed(DatabaseError databaseError) {}

        });

    }


    int progresso2;
    public void atualizaEstatistica2(){

        DaoFirebase dao = new DaoFirebase();

        dao.pegarTotaldeBuracos(true, new OnGetFirebaseDados() {

            @Override
            public void onStart() {
                progressBarThread2.setVisibility(View.VISIBLE);
                textoInfo2.setVisibility(View.INVISIBLE);
                textoQtdBuraco2.setVisibility(View.INVISIBLE);
                textoInforRegistro2.setText("Carregando...");
            }


            @Override
            public void onRetornoDados(int t) {

                final int total = t;

                progressBarThread2.setVisibility(View.INVISIBLE);

                textoInfo2.setText("Total de Buracos");
                textoInfo2.setVisibility(View.VISIBLE);
                textoInforRegistro2.setTextSize(27);
                textoInforRegistro2.setText("Registros");

                new Thread(new Runnable() {
                    public void run() {

                        progresso2 = 0;
                        while(progresso2 < total){

                            try {
                                // ---simulate doing some work---
                                Thread.sleep(30);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }

                            handler2.post(new Runnable() {
                                public void run() {

                                    textoQtdBuraco2.setVisibility(View.VISIBLE);
                                    textoQtdBuraco2.setText(progresso2 + "");

                                }
                            });

                            progresso2++;
                        }
                    }
                }).start();

            }

            @Override
            public void registrosDaCidade(int total, ArrayList<Cidade> listaRuas) {}

            @Override
            public void ruaComMaisRegistro(int total, ArrayList<Rua> listaRua){}

            @Override
            public void onFailed(DatabaseError databaseError) {}

        });
    }


    ArrayList<Cidade> listaCidadeMenos = new ArrayList<Cidade>();
    int progresso3;
    public void atualizaEstatistica3(){

        DaoFirebase dao = new DaoFirebase();

        dao.buscarRegistrosDaCidade(false, new OnGetFirebaseDados() {

            @Override
            public void onStart() {

                progressBarThread3.setVisibility(View.VISIBLE);
                progressEstatistica3.setVisibility(View.INVISIBLE);
                textoPorcentagem3.setVisibility(View.INVISIBLE);
                textoInfo3.setText("Carregando...");
                textoCidade4.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onRetornoDados(int total) {}

            @Override
            public void registrosDaCidade(int total, ArrayList<Cidade> listaCidades) {

                final String cidade = listaCidades.get(0).cidade;

                double y,z;
                y = (double)listaCidades.get(0).qtd;
                z = (double)total;

                double x = (y / z) * 100;
                final double porcentagem = Math.round(x);

                Log.e("Porcentagem", "" + porcentagem);

                progressBarThread3.setVisibility(View.INVISIBLE);
                progressEstatistica3.setVisibility(View.VISIBLE);
                textoInfo3.setText("Cidade menos registrada:");
                textoCidade4.setText(cidade);
                textoCidade4.setVisibility(View.VISIBLE);
                btnInfo3.setVisibility(View.VISIBLE);

                new Thread(new Runnable() {
                    public void run() {

                        progresso3 = 0;
                        while(progresso3 < porcentagem){

                            try {
                                // ---simulate doing some work---
                                Thread.sleep(30);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }

                            handler3.post(new Runnable() {
                                public void run() {

                                    textoPorcentagem3.setVisibility(View.VISIBLE);
                                    textoPorcentagem3.setText(progresso3 + "%");

                                }
                            });

                            progresso3++;
                            progressEstatistica3.setProgress(progresso3);
                        }

                    }
                }).start();

                listaCidadeMenos = listaCidades;

            }

            @Override
            public void ruaComMaisRegistro(int total, ArrayList<Rua> listaRua){}

            @Override
            public void onFailed(DatabaseError databaseError) {
            }

        });
    }


    ArrayList<Rua> listaDeRuas = new ArrayList<Rua>();
    int progresso4;
    public void atualizaEstatistica4(){

        DaoFirebase dao = new DaoFirebase();

        dao.ruaComMaisBuracos(new OnGetFirebaseDados() {

            @Override
            public void onStart() {

                textoInfo4.setVisibility(View.INVISIBLE);
                textoInfoContinua4.setVisibility(View.INVISIBLE);
                textoEndereco4.setVisibility(View.INVISIBLE);
                TextoQtdBuraco4.setVisibility(View.INVISIBLE);
                textoInforRegistro4.setTextSize(20);
                textoInforRegistro4.setText("Carregando...");
                textoInforRegistro4.setVisibility(View.VISIBLE);
                progressBarThread4.setVisibility(View.VISIBLE);
            }

            @Override
            public void onRetornoDados(int t) {}

            @Override
            public void registrosDaCidade(int total, ArrayList<Cidade> listaRuas) {}

            @Override
            public void ruaComMaisRegistro(int total, ArrayList<Rua> listaRua){

                final String dadosEndereco = listaRua.get(0).rua;
                final int qtdBuracos = listaRua.get(0).qtd;

                progressBarThread4.setVisibility(View.INVISIBLE);
                textoInfo4.setVisibility(View.VISIBLE);
                textoInfoContinua4.setVisibility(View.VISIBLE);
                textoEndereco4.setText(dadosEndereco);
                textoEndereco4.setVisibility(View.VISIBLE);
                textoInforRegistro4.setTextSize(27);
                textoInforRegistro4.setText("registros");
                textoInforRegistro4.setVisibility(View.VISIBLE);
                btnInfo4.setVisibility(View.VISIBLE);

                new Thread(new Runnable() {
                    public void run() {

                        progresso4 = 0;
                        while(progresso4 < qtdBuracos){

                            try {
                                // ---simulate doing some work---
                                Thread.sleep(30);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            handler4.post(new Runnable() {
                                public void run() {

                                    TextoQtdBuraco4.setVisibility(View.VISIBLE);
                                    TextoQtdBuraco4.setText(progresso4 + "");
                                }
                            });

                            progresso4++;
                        }

                    }
                }).start();
                listaDeRuas = listaRua;
            }

            @Override
            public void onFailed(DatabaseError databaseError) {}

        });

    }


    int progresso5;
    public void atualizaEstatistica5(){

        DaoFirebase dao = new DaoFirebase();

        dao.pegarTotaldeBuracos(false, new OnGetFirebaseDados() {

            @Override
            public void onStart() {

                progressBarThread5.setVisibility(View.VISIBLE);
                textoInfo5.setVisibility(View.INVISIBLE);
                textoBuracoTampados5.setVisibility(View.INVISIBLE);
                textoInforRegistro5.setText("Carregando...");
            }

            @Override
            public void onRetornoDados(int t) {

                final int total = t;

                progressBarThread5.setVisibility(View.INVISIBLE);
                textoInfo5.setText("Buracos Tampados");
                textoInfo5.setVisibility(View.VISIBLE);
                textoInforRegistro5.setTextSize(27);
                textoInforRegistro5.setText("Registros");

                new Thread(new Runnable() {
                    public void run() {

                        progresso5 = 0;
                        while(progresso5 < total){

                            try {
                                // ---simulate doing some work---
                                Thread.sleep(30);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }

                            handler5.post(new Runnable() {
                                public void run() {

                                    textoBuracoTampados5.setVisibility(View.VISIBLE);
                                    textoBuracoTampados5.setText(progresso5 + "");

                                }
                            });

                            progresso5++;
                        }

                    }

                }).start();

            }

            @Override
            public void registrosDaCidade(int total, ArrayList<Cidade> listaRuas) {}

            @Override
            public void ruaComMaisRegistro(int total, ArrayList<Rua> listaRua){}

            @Override
            public void onFailed(DatabaseError databaseError) {}

        });

    }


    ArrayList<Buraco> listaBuracosAntigos = new ArrayList<Buraco>();
    int progresso6;
    public void atualizaEstatistica6(){

        DaoFirebase dao = new DaoFirebase();

        dao.listarBuracosMaisAntigos(new OnGetFirebaseBuracosListener() {
            @Override

            public void onStart() {

                textoInfo6.setVisibility(View.INVISIBLE);
                textoInfoContinua6.setVisibility(View.INVISIBLE);
                textoEndereco6.setVisibility(View.INVISIBLE);
                textoInforDesde6.setVisibility(View.INVISIBLE);
                textoData6.setVisibility(View.INVISIBLE);
                textoQtdDias6.setVisibility(View.INVISIBLE);
                textoInfoDias6.setVisibility(View.INVISIBLE);
                textoInforRegistro6.setTextSize(20);
                textoInforRegistro6.setText("Carregando...");
                textoInforRegistro6.setVisibility(View.VISIBLE);
                progressBarThread6.setVisibility(View.VISIBLE);
            }

            @Override
            public void onRetornoLista(ArrayList<Buraco> listaRetornada){

                final String dadosEndereco  = listaRetornada.get(0).getRua();

                long dataAtual = System.currentTimeMillis()/1000;
                long dataPassado = Long.parseLong(listaRetornada.get(0).getData_Registro());

                long diferenca = (dataAtual - dataPassado)/ 60 / 60 / 24;

                Log.e("dataAtual", ""+ dataAtual);
                Log.e("dataPassado", ""+ dataPassado);
                Log.e("diferenca", ""+ diferenca);

                long timestamp = Long.parseLong(listaRetornada.get(0).getData_Registro()) * 1000L;
                String data;
                DateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                Date netDate = (new Date(timestamp));
                data =  sdf.format(netDate);

                final String dataAntiga  = data;
                final int qtdBuracos     = ((int) diferenca) + 1;

                textoInforRegistro6 = (TextView) view.findViewById(R.id.textoInfo6);
                textoInfo6          = (TextView) view.findViewById(R.id.textoBuracoComMais6);
                textoInfoContinua6  = (TextView) view.findViewById(R.id.textoTempoAberto6);
                textoEndereco6      = (TextView) view.findViewById(R.id.textoRuaMaiorTempo6);
                textoInforDesde6    = (TextView) view.findViewById(R.id.textoDesde6);
                textoData6          = (TextView) view.findViewById(R.id.textoData6);
                textoQtdDias6       = (TextView) view.findViewById(R.id.textoDias6);
                textoInfoDias6      = (TextView) view.findViewById(R.id.textoInfoDias6);


                progressBarThread6.setVisibility(View.INVISIBLE);
                textoInforRegistro6.setVisibility(View.INVISIBLE);

                textoInfo6.setVisibility(View.VISIBLE);
                textoInfoContinua6.setVisibility(View.VISIBLE);

                textoEndereco6.setText(dadosEndereco);
                textoEndereco6.setVisibility(View.VISIBLE);

                textoInforDesde6.setVisibility(View.VISIBLE);
                textoData6.setText(dataAntiga);
                textoData6.setVisibility(View.VISIBLE);

                textoInfoDias6.setVisibility(View.VISIBLE);

                btnInfo6.setVisibility(View.VISIBLE);

                new Thread(new Runnable() {
                    public void run() {

                        progresso6 = 0;
                        while(progresso6 < qtdBuracos){

                            try {
                                // ---simulate doing some work---
                                Thread.sleep(30);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }

                            handler6.post(new Runnable() {
                                public void run() {

                                    textoQtdDias6.setVisibility(View.VISIBLE);
                                    textoQtdDias6.setText(progresso6 + "");

                                }
                            });

                            progresso6++;
                        }
                    }
                }).start();

                listaBuracosAntigos = listaRetornada;

            }

            @Override
            public void onRetornoDuasLista(ArrayList<Buraco> buracosAbertos, ArrayList<Buraco> buracosTampados){}

            @Override
            public void onFailed(DatabaseError databaseError) {}

            @Override
            public void onRetornoExiste(Boolean existe){}

            @Override
            public void onRetornoBuraco(Buraco buraco) {}

        });

    }



    //----------------------------------------------------------------------------------------
    //                      METODOS DE INFORMAÇOES DAS ESTATISTICAS
    //----------------------------------------------------------------------------------------

    public void mostrarInformacaoEstatitica1(){

        AlertDialog infoEstatistica;
        AlertDialog.Builder mensagemSobre = new AlertDialog.Builder(getContext());

        mensagemSobre.setTitle("Cidades mais registradas");

        String msg = "";
        for (int i = 1; i<=listaDasCidades.size();i++){

            msg = msg + "\n" + i + "ª - " + listaDasCidades.get(i-1).cidade + " - " +  listaDasCidades.get(i-1).estado + "\n       Registros: " + listaDasCidades.get(i-1).qtd + "\n";

        }

        mensagemSobre.setMessage(msg);

        mensagemSobre.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
            }
        });

        infoEstatistica = mensagemSobre.create();
        infoEstatistica.show();

    }

    public void mostrarInformacaoEstatitica3(){

        AlertDialog infoEstatistica;
        AlertDialog.Builder mensagemSobre = new AlertDialog.Builder(getContext());

        mensagemSobre.setTitle("Cidades menos registradas");

        String msg = "";
        for (int i = 1; i<=listaCidadeMenos.size();i++){

            msg = msg + "\n" + i + "ª - " + listaCidadeMenos.get(i-1).cidade + " - " +  listaCidadeMenos.get(i-1).estado + "\n       Registros: " + listaCidadeMenos.get(i-1).qtd + "\n";

        }

        mensagemSobre.setMessage(msg);

        mensagemSobre.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
            }
        });

        infoEstatistica = mensagemSobre.create();
        infoEstatistica.show();

    }

    public void mostrarInformacaoEstatitica4(){


        AlertDialog infoEstatistica;
        AlertDialog.Builder mensagemSobre = new AlertDialog.Builder(getContext());
        mensagemSobre.setTitle("Endereços com mais buracos");

        String msg = "";
        for (int i = 1; i<=listaDeRuas.size();i++){

            msg = msg + "\n" + i + "ª - " + listaDeRuas.get(i-1).rua + "\n       " +  listaDeRuas.get(i-1).cidade + " - " + listaDeRuas.get(i-1).estado + "\n       Buracos: " + listaDeRuas.get(i-1).qtd + "\n";

        }

        mensagemSobre.setMessage(msg);

        mensagemSobre.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
            }
        });

        infoEstatistica = mensagemSobre.create();
        infoEstatistica.show();

    }

    public void mostrarInformacaoEstatitica6(){

        AlertDialog infoEstatistica;
        AlertDialog.Builder mensagemSobre = new AlertDialog.Builder(getContext());

        mensagemSobre.setTitle("Buracos com mais tempo abertos");

        String msg = "";
        for (int i = 1; i<=listaBuracosAntigos.size();i++){

            long dataAtual = System.currentTimeMillis()/1000;
            long dataPassado = Long.parseLong(listaBuracosAntigos.get(i-1).getData_Registro());

            long diferenca = (dataAtual - dataPassado)/ 60 / 60 / 24;

            long timestamp = Long.parseLong(listaBuracosAntigos.get(i-1).getData_Registro()) * 1000L;
            String data;
            DateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
            Date netDate = (new Date(timestamp));
            data =  sdf.format(netDate);

            final int qtdBuracos = ((int) diferenca) + 1;

            msg = msg + "\n" + i + "ª - " + listaBuracosAntigos.get(i-1).getRua() + "\n       " +  listaBuracosAntigos.get(i-1).getCidade() + " - " + listaBuracosAntigos.get(i-1).getEstado() + "\n       Desde: " + data + " - " + qtdBuracos + " dias\n";

        }

        mensagemSobre.setMessage(msg);

        mensagemSobre.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
            }
        });

        infoEstatistica = mensagemSobre.create();
        infoEstatistica.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        infoEstatistica.show();


    }


}
