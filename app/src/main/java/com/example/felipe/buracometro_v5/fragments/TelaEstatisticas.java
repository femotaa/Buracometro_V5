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
import com.example.felipe.buracometro_v5.dao.BuracoWebDao;
import com.example.felipe.buracometro_v5.modelo.DadosEstatisticos;

import java.util.ArrayList;

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

    ImageButton btnInfo1;
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
    BuracoWebDao dao = new BuracoWebDao();
    private static final String TEXTO_TOOLBAR = "Estatísticas";


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fr_tela_estatisticas,container,false);

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
    }



    //----------------------------------------------------------------------------------------
    //                     METODOS PARA ATUALIZAR CAMPOS DE ESTATISTICAS
    //----------------------------------------------------------------------------------------

    ArrayList<DadosEstatisticos> listaCidadeMais = new ArrayList<DadosEstatisticos>();
    int progresso;
    public void atualizaEstatistica1(){

        new Thread(new Runnable() {
            public void run() {

                handler1.post(new Runnable() {
                    public void run() {
                        progressBarThread1.setVisibility(View.VISIBLE);
                        progressEstatistica1.setVisibility(View.INVISIBLE);

                        textoPorcentagem1.setVisibility(View.INVISIBLE);
                        textoInfo1.setText("Carregando...");
                        textoCidade1.setVisibility(View.INVISIBLE);
                    }
                });


                try{
                    listaCidadeMais = dao.buscaCidadeMaisRegistrada();
                    final String cidade = listaCidadeMais.get(0).getDados();

                    //Valor da porcentagem arredondado
                    final double porcentagem = Math.round(listaCidadeMais.get(0).getPorcertagem());

                    handler1.post(new Runnable() {
                        public void run() {
                            progressBarThread1.setVisibility(View.INVISIBLE);
                            progressEstatistica1.setVisibility(View.VISIBLE);

                            textoInfo1.setText("Cidade mais registrada:");

                            textoCidade1.setVisibility(View.VISIBLE);
                            textoCidade1.setText(cidade);

                            btnInfo1.setVisibility(View.VISIBLE);

                        }
                    });

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


                }catch(Exception e){
                    e.printStackTrace();
                    Log.d("Erro na estatistica: ", e + "");

                    handler1.post(new Runnable() {
                        public void run() {
                            progressBarThread1.setVisibility(View.INVISIBLE);
                            imgErro.setVisibility(View.VISIBLE);
                            textoInfo1.setText("Falha de Conexão");
                        }
                    });
                }

            }
        }).start();

    }


    int progresso2;
    public void atualizaEstatistica2(){

        new Thread(new Runnable() {
            public void run() {

                handler2.post(new Runnable() {
                    public void run() {

                        progressBarThread2.setVisibility(View.VISIBLE);

                        textoInfo2.setVisibility(View.INVISIBLE);
                        textoQtdBuraco2.setVisibility(View.INVISIBLE);
                        textoInforRegistro2.setText("Carregando...");
                    }
                });

                int qtdBuracos = dao.buscaTotalDeBuracos();

                if(qtdBuracos == -1 || qtdBuracos == -2)
                {

                    handler2.post(new Runnable() {
                        public void run() {
                            progressBarThread2.setVisibility(View.INVISIBLE);
                            imgErro2.setVisibility(View.VISIBLE);
                            textoInforRegistro2.setText("Falha de Conexão");
                        }
                    });

                }else
                {
                    handler2.post(new Runnable() {
                        public void run() {
                            progressBarThread2.setVisibility(View.INVISIBLE);

                            textoInfo2.setText("Total de Buracos");
                            textoInfo2.setVisibility(View.VISIBLE);
                            textoInforRegistro2.setTextSize(27);
                            textoInforRegistro2.setText("Registros");
                            //btnInfo2.setVisibility(View.VISIBLE);

                        }
                    });

                    progresso2 = 0;
                    while(progresso2 < qtdBuracos){

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

            }
        }).start();

    }


    ArrayList<DadosEstatisticos> listaCidadeMenos = new ArrayList<DadosEstatisticos>();
    int progresso3;
    public void atualizaEstatistica3(){

        new Thread(new Runnable() {
            public void run() {

                handler3.post(new Runnable() {
                    public void run() {

                        progressBarThread3.setVisibility(View.VISIBLE);

                        progressEstatistica3.setVisibility(View.INVISIBLE);
                        textoPorcentagem3.setVisibility(View.INVISIBLE);
                        textoInfo3.setText("Carregando...");
                        textoCidade4.setVisibility(View.INVISIBLE);
                    }
                });

                try{

                    listaCidadeMenos = dao.buscaCidadeMenosRegistrada();

                    //de.setDados("São Paulo");
                    //de.setPorcertagem(71.7);
                    //de.setQtdDados(56);

                    final String cidade = listaCidadeMenos.get(0).getDados();

                    //Valor da porcentagem arredondado
                    final double porcentagem = Math.round(listaCidadeMenos.get(0).getPorcertagem());

                    handler3.post(new Runnable() {
                        public void run() {
                            progressBarThread3.setVisibility(View.INVISIBLE);
                            progressEstatistica3.setVisibility(View.VISIBLE);

                            textoInfo3.setText("Cidade menos registrada:");

                            textoCidade4.setText(cidade);
                            textoCidade4.setVisibility(View.VISIBLE);
                            btnInfo3.setVisibility(View.VISIBLE);


                        }
                    });

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


                }catch(Exception e){
                    e.printStackTrace();
                    Log.d("Erro na estatistica: ", e + "");

                    handler3.post(new Runnable() {
                        public void run() {
                            progressBarThread3.setVisibility(View.INVISIBLE);
                            imgErro3.setVisibility(View.VISIBLE);
                            textoInfo3.setText("Falha de Conexão");
                        }
                    });
                }

            }
        }).start();

    }


    ArrayList<DadosEstatisticos> listaRuaMais = new ArrayList<DadosEstatisticos>();
    int progresso4;
    public void atualizaEstatistica4(){

        new Thread(new Runnable() {
            public void run() {

                handler4.post(new Runnable() {
                    public void run() {

                        textoInfo4.setVisibility(View.INVISIBLE);
                        textoInfoContinua4.setVisibility(View.INVISIBLE);
                        textoEndereco4.setVisibility(View.INVISIBLE);
                        TextoQtdBuraco4.setVisibility(View.INVISIBLE);

                        textoInforRegistro4.setTextSize(20);
                        textoInforRegistro4.setText("Carregando...");
                        textoInforRegistro4.setVisibility(View.VISIBLE);

                        progressBarThread4.setVisibility(View.VISIBLE);
                    }
                });


                try{

                    listaRuaMais = dao.buscaRuaMaisEsburacada();

                    final String dadosEndereco = listaRuaMais.get(0).getDados();
                    final int qtdBuracos = listaRuaMais.get(0).getQtdDados();


                    handler4.post(new Runnable() {
                        public void run() {

                            progressBarThread4.setVisibility(View.INVISIBLE);

                            textoInfo4.setVisibility(View.VISIBLE);
                            textoInfoContinua4.setVisibility(View.VISIBLE);

                            textoEndereco4.setText(dadosEndereco);
                            textoEndereco4.setVisibility(View.VISIBLE);

                            textoInforRegistro4.setTextSize(27);
                            textoInforRegistro4.setText("registros");
                            textoInforRegistro4.setVisibility(View.VISIBLE);

                            btnInfo4.setVisibility(View.VISIBLE);


                        }
                    });

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

                }catch(Exception e){
                    e.printStackTrace();
                    Log.d("Erro na estatistica: ", e + "");

                    handler4.post(new Runnable() {
                        public void run() {
                            progressBarThread4.setVisibility(View.INVISIBLE);
                            imgErro4.setVisibility(View.VISIBLE);
                            textoInforRegistro4.setTextSize(20);
                            textoInforRegistro4.setGravity(View.TEXT_ALIGNMENT_CENTER);
                            textoInforRegistro4.setText("Falha de Conexão");
                        }
                    });
                }

            }
        }).start();
    }


    int progresso5;
    public void atualizaEstatistica5(){

        new Thread(new Runnable() {
            public void run() {

                handler5.post(new Runnable() {
                    public void run() {

                        progressBarThread5.setVisibility(View.VISIBLE);

                        textoInfo5.setVisibility(View.INVISIBLE);
                        textoBuracoTampados5.setVisibility(View.INVISIBLE);
                        textoInforRegistro5.setText("Carregando...");
                    }
                });

                int qtdBuracos = dao.buscaTotalDeBuracosTampados();

                if(qtdBuracos == -1 || qtdBuracos == -2)
                {

                    handler5.post(new Runnable() {
                        public void run() {
                            progressBarThread5.setVisibility(View.INVISIBLE);
                            imgErro5.setVisibility(View.VISIBLE);
                            textoInforRegistro5.setText("Falha de Conexão");
                        }
                    });

                }else
                {
                    handler5.post(new Runnable() {
                        public void run() {
                            progressBarThread5.setVisibility(View.INVISIBLE);

                            textoInfo5.setText("Buracos Tampados");
                            textoInfo5.setVisibility(View.VISIBLE);
                            textoInforRegistro5.setTextSize(27);
                            textoInforRegistro5.setText("Registros");
                            //btnInfo2.setVisibility(View.VISIBLE);

                        }
                    });

                    progresso5 = 0;
                    while(progresso5 < qtdBuracos){

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

            }
        }).start();

    }


    ArrayList<DadosEstatisticos> listaMaisAntigos = new ArrayList<DadosEstatisticos>();
    int progresso6;
    public void atualizaEstatistica6(){

        new Thread(new Runnable() {
            public void run() {

                handler6.post(new Runnable() {
                    public void run() {

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
                });


                try{

                    listaMaisAntigos = dao.buscaBuracoMaisAntigo();

                    final String dadosEndereco  = listaMaisAntigos.get(0).getDados();
                    final String dataAntiga     = listaMaisAntigos.get(0).getDados2();
                    final int qtdBuracos        = listaMaisAntigos.get(0).getQtdDados();


                    handler6.post(new Runnable() {
                        public void run() {


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


                        }
                    });

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

                }catch(Exception e){
                    e.printStackTrace();
                    Log.d("Erro na estatistica: ", e + "");

                    handler6.post(new Runnable() {
                        public void run() {
                            progressBarThread6.setVisibility(View.INVISIBLE);
                            imgErro6.setVisibility(View.VISIBLE);
                            textoInforRegistro6.setTextSize(20);
                            textoInforRegistro6.setGravity(View.TEXT_ALIGNMENT_CENTER);
                            textoInforRegistro6.setText("Falha de Conexão");
                        }
                    });
                }

            }
        }).start();
    }



    //----------------------------------------------------------------------------------------
    //                      METODOS DE INFORMAÇOES DAS ESTATISTICAS
    //----------------------------------------------------------------------------------------

    public void mostrarInformacaoEstatitica1(){

        AlertDialog infoEstatistica;
        AlertDialog.Builder mensagemSobre = new AlertDialog.Builder(getContext());

        mensagemSobre.setTitle("Cidades mais registradas");

        String mensagem = (
                "1ª - " + listaCidadeMais.get(0).getDados() + " - " +  listaCidadeMais.get(0).getDados2()
                        + "\n       Registros: " + listaCidadeMais.get(0).getQtdDados() + "\n" +

                        "2ª - " + listaCidadeMais.get(1).getDados() + " - " +  listaCidadeMais.get(1).getDados2()
                        + "\n       Registros: " + listaCidadeMais.get(1).getQtdDados() + "\n" +

                        "3ª - " + listaCidadeMais.get(2).getDados() + " - " +  listaCidadeMais.get(2).getDados2()
                        + "\n       Registros: " + listaCidadeMais.get(2).getQtdDados() + "\n" +

                        "4ª - " + listaCidadeMais.get(3).getDados() + " - " +  listaCidadeMais.get(3).getDados2()
                        + "\n       Registros: " + listaCidadeMais.get(3).getQtdDados() + "\n" +

                        "5ª - " + listaCidadeMais.get(4).getDados() + " - " +  listaCidadeMais.get(4).getDados2()
                        + "\n       Registros: " + listaCidadeMais.get(4).getQtdDados());


        mensagemSobre.setMessage(mensagem);

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

        String mensagem = (
                "1ª - " + listaCidadeMenos.get(0).getDados() + " - " +  listaCidadeMenos.get(0).getDados2()
                        + "\n       Registros: " + listaCidadeMenos.get(0).getQtdDados() + "\n" +

                        "2ª - " + listaCidadeMenos.get(1).getDados() + " - " +  listaCidadeMenos.get(1).getDados2()
                        + "\n       Registros: " + listaCidadeMenos.get(1).getQtdDados() + "\n" +

                        "3ª - " + listaCidadeMenos.get(2).getDados() + " - " +  listaCidadeMenos.get(2).getDados2()
                        + "\n       Registros: " + listaCidadeMenos.get(2).getQtdDados() + "\n" +

                        "4ª - " + listaCidadeMenos.get(3).getDados() + " - " +  listaCidadeMenos.get(3).getDados2()
                        + "\n       Registros: " + listaCidadeMenos.get(3).getQtdDados() + "\n" +

                        "5ª - " + listaCidadeMenos.get(4).getDados() + " - " +  listaCidadeMenos.get(4).getDados2()
                        + "\n       Registros: " + listaCidadeMenos.get(4).getQtdDados());


        mensagemSobre.setMessage(mensagem);

        mensagemSobre.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
            }
        });

        infoEstatistica = mensagemSobre.create();
        infoEstatistica.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        infoEstatistica.show();

    }

    public void mostrarInformacaoEstatitica4(){


        AlertDialog infoEstatistica;
        AlertDialog.Builder mensagemSobre = new AlertDialog.Builder(getContext());

        mensagemSobre.setTitle("Endereços com mais buracos");

        String mensagem = (
                "1ª - " + listaRuaMais.get(0).getDados()
                        + "\n       " + listaRuaMais.get(0).getDados2() + " - " +  listaRuaMais.get(0).getDados3()
                        + "\n       Registro: " + listaRuaMais.get(0).getQtdDados() + "\n" +

                        "2ª - " + listaRuaMais.get(1).getDados()
                        + "\n       " + listaRuaMais.get(1).getDados2() + " - " +  listaRuaMais.get(1).getDados3()
                        + "\n       Registro: " + listaRuaMais.get(1).getQtdDados() + "\n" +

                        "3ª - " + listaRuaMais.get(2).getDados()
                        + "\n       " + listaRuaMais.get(2).getDados2() + " - " +  listaRuaMais.get(2).getDados3()
                        + "\n       Registro: " + listaRuaMais.get(2).getQtdDados() + "\n" +

                        "4ª - " + listaRuaMais.get(3).getDados()
                        + "\n       " + listaRuaMais.get(3).getDados2() + " - " +  listaRuaMais.get(3).getDados3()
                        + "\n       Registro: " + listaRuaMais.get(3).getQtdDados() + "\n" +

                        "5ª - " + listaRuaMais.get(4).getDados()
                        + "\n       " + listaRuaMais.get(4).getDados2() + " - " +  listaRuaMais.get(4).getDados3()
                        + "\n       Registro: " + listaRuaMais.get(4).getQtdDados());

        mensagemSobre.setMessage(mensagem);

        mensagemSobre.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
            }
        });

        infoEstatistica = mensagemSobre.create();
        infoEstatistica.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        infoEstatistica.show();

    }

    public void mostrarInformacaoEstatitica6(){
/*
        AlertDialog infoEstatistica;
        AlertDialog.Builder mensagemSobre = new AlertDialog.Builder(this);

        mensagemSobre.setTitle("Buracos com mais tempo abertos");

        String mensagem = (
                        "1ª - " + listaMaisAntigos.get(0).getDados()
                        + "\n       " + listaMaisAntigos.get(0).getDados3() + " - " +  listaMaisAntigos.get(0).getDados4()
                        + "\n       Desde: " + listaMaisAntigos.get(0).getDados2() + " " + listaMaisAntigos.get(0).getQtdDados() + " dias" + "\n" +

                        "2ª - " + listaMaisAntigos.get(1).getDados()
                        + "\n       " + listaMaisAntigos.get(1).getDados3() + " - " +  listaMaisAntigos.get(1).getDados4()
                        + "\n       Desde: " + listaMaisAntigos.get(1).getDados2() + " " + listaMaisAntigos.get(1).getQtdDados() + " dias" + "\n" +

                        "3ª - " + listaMaisAntigos.get(2).getDados()
                        + "\n       " + listaMaisAntigos.get(2).getDados3() + " - " +  listaMaisAntigos.get(2).getDados4()
                        + "\n       Desde: " + listaMaisAntigos.get(2).getDados2() + " " + listaMaisAntigos.get(2).getQtdDados() + " dias" + "\n" +

                        "4ª - " + listaMaisAntigos.get(3).getDados()
                        + "\n       " + listaMaisAntigos.get(3).getDados3() + " - " +  listaMaisAntigos.get(3).getDados4()
                        + "\n       Desde: " + listaMaisAntigos.get(3).getDados2() + " " + listaMaisAntigos.get(3).getQtdDados() + " dias" + "\n" +

                        "5ª - " + listaMaisAntigos.get(4).getDados()
                        + "\n       " + listaMaisAntigos.get(4).getDados3() + " - " +  listaMaisAntigos.get(4).getDados4()
                        + "\n       Desde: " + listaMaisAntigos.get(4).getDados2() + " " + listaMaisAntigos.get(4).getQtdDados() + " dias");

        mensagemSobre.setMessage(mensagem);

        mensagemSobre.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
            }
        });

        infoEstatistica = mensagemSobre.create();
        infoEstatistica.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        infoEstatistica.show();

*/
    }


}
