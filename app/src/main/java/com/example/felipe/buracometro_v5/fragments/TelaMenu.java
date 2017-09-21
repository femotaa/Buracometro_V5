package com.example.felipe.buracometro_v5.fragments;

import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.felipe.buracometro_v5.R;
import com.example.felipe.buracometro_v5.dao.DaoFirebase;
import com.example.felipe.buracometro_v5.modelo.Buraco;
import com.example.felipe.buracometro_v5.modelo.Usuario;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


public class TelaMenu extends Fragment{

    private static final String TEXTO_TOOLBAR = "";

    View view;
    Button btnAddBuraco;
    Usuario usuarioAtual = new Usuario();
    DaoFirebase daoFirebase = new DaoFirebase();


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.tela_menu, container, false);

        ImageView imgToolbar = (ImageView) getActivity().findViewById(R.id.img_icone);
        imgToolbar.setBackgroundDrawable(getResources().getDrawable(R.drawable.icone_lista));


        TextView textoToolbar = (TextView) getActivity().findViewById(R.id.texto_toolbar);
        textoToolbar.setText(TEXTO_TOOLBAR);

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
                Buraco b = new Buraco();

                Long tsLong = System.currentTimeMillis()/1000;
                String ts = tsLong.toString();

                long timestamp = Long.parseLong(ts) * 1000L;
                String data;
                DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date netDate = (new Date(timestamp));
                data =  sdf.format(netDate);

                b.setBairro("Higienopolis");
                b.setCidade("Jandira");
                b.setData_Registro(ts); //timestamp
                b.setRua("Rua do Ouro");
                b.setEstado("SP");
                b.setLatitude("42.99999");
                b.setLongitude("41.45789");
                b.setStatusBuraco("Aberto");
                b.setQtdOcorrencia(1);

                adicionarBuraco(b);
            }
        });

    }


    public void adicionarBuraco(Buraco buraco){

        //Long tsLong = System.currentTimeMillis()/1000;
        //String ts = tsLong.toString();
        //buraco.setDataTampado(ts);
        //buraco.setStatusBuraco("Tampado");
        //buraco.setIdBuraco("-Kpml_v12vceeeee");
        //daoFirebase.inserirBuracoTampado(buraco);

        //daoFirebase.inserirBuraco(buraco, usuarioAtual);
        //daoFirebase.atualizarStatusParaTampado(buraco);
        //daoFirebase.atualizarStatusParaTampadoUsuarios(buraco);

        //daoFirebase.atualizaTotalDeBuracosTampados(false);

        //buraco.setIdBuraco("-KpmeZRvgximEcwiQMDO");
        //daoFirebase.inserirBuracoTampado(buraco);

//        Toast.makeText(getContext(), "Buraco adicinado", Toast.LENGTH_LONG).show();

        //buraco.setIdBuraco("-Kp6w_mNLV81ng0VDMN_");

        //daoFirebase.atualizaRuaQtdBuracos(buraco);

        //daoFirebase.atualizaQtdOcorrencia(buraco);

        //daoFirebase.atualizaCidadeQtdBuracos(buraco);

        //daoFirebase.inserirOnlyBuraco(buraco);

        /* * /
        new DaoFirebase().listarBuracosPorUsuario(usuarioAtual, new OnGetFirebaseBuracosListener() {
            @Override

            public void onStart() {
                //DO SOME THING WHEN START GET DATA HERE
            }

            @Override
            public void onRetornoLista(ArrayList<Buraco> buracos){


                //Log.e("Buraquim2", "" + buracos.get(0).getIdBuraco());
                //Log.e("Buraquim3", "" + buracos.get(1).getIdBuraco());
                Log.e("Total", "" + buracos.size());
                if(!buracos.isEmpty()){

                    Log.e("IdBura", "" + buracos.get(0).getIdBuraco());
                    Log.e("IdBura", "" + buracos.get(1).getIdBuraco());
                    Log.e("IdBura", "" + buracos.get(2).getIdBuraco());
                }


                //DO SOME THING WHEN GET DATA SUCCESS HERE

            }

            @Override
            public void onRetornoDuasLista(ArrayList<Buraco> buracosAbertos, ArrayList<Buraco> buracosTampados){

            }

            @Override
            public void onFailed(DatabaseError databaseError) {
                //DO SOME THING WHEN GET DATA FAILED HERE
            }

            @Override
            public void onRetornoExiste(Boolean existe){
                //DO SOME THING WHEN GET DATA FAILED HERE
            }

            @Override
            public void onRetornoBuraco(Buraco buraco) {
                //DO SOME THING WHEN GET DATA FAILED HERE
            }


        });
        /* */

        /* * /
        new DaoFirebase().pegarTotaldeBuracosTampados(new OnGetFirebaseDados() {

            @Override
            public void onRetornoDados(int total) {
                Log.e("TOTALTUDO", "" + total);
            }

            @Override
            public void onStart() {
            }


            @Override
            public void onFailed(DatabaseError databaseError) {
            }

        });
        /* */

    }


}
