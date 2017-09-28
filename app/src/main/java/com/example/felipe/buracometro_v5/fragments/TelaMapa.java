package com.example.felipe.buracometro_v5.fragments;

import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TabHost;
import android.widget.TextView;

import com.example.felipe.buracometro_v5.R;
import com.example.felipe.buracometro_v5.dao.BuracoLocalDao;
import com.example.felipe.buracometro_v5.dao.DaoFirebase;
import com.example.felipe.buracometro_v5.listeners.OnGetFirebaseBuracosListener;
import com.example.felipe.buracometro_v5.modelo.Buraco;
import com.example.felipe.buracometro_v5.modelo.Usuario;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DatabaseError;

import java.util.ArrayList;


public class TelaMapa extends Fragment implements OnMapReadyCallback {

    private static final String TEXTO_TOOLBAR = "Mapas";

    GoogleMap mMap;
    MapView mMapView;

    TabHost tabhost;
    ProgressBar progressBar;
    View view;

    ArrayList<Buraco> listaRegistros = new ArrayList<Buraco>();
    ArrayList<Buraco> listaCriticos = new ArrayList<Buraco>();
    ArrayList<Buraco> listaRecentes = new ArrayList<Buraco>();

    boolean inicializadoRegistros, inicializadoRecentes, inicializadoCriticos;
    Handler handler1 = new Handler();
    Usuario usuarioAtual = new Usuario();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.tela_mapa, container, false);

        ImageView imgToolbar = (ImageView) getActivity().findViewById(R.id.img_icone);
        imgToolbar.setBackgroundDrawable(getResources().getDrawable(R.drawable.icone_mapa));

        TextView textoToolbar = (TextView) getActivity().findViewById(R.id.texto_toolbar);
        textoToolbar.setText(TEXTO_TOOLBAR);

        tabhost = (TabHost) view.findViewById(R.id.tabhostmapa);
        tabhost.setup();

        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);

        SharedPreferences settings = getActivity().getSharedPreferences("preferencias", 0);
        String emailAtual = settings.getString("login","");
        String idUsuarioAtual = settings.getString("IdLogin","");
        String nomeAtual = settings.getString("nome","");

        usuarioAtual.setEmail(emailAtual);
        usuarioAtual.setId(idUsuarioAtual);
        usuarioAtual.setNome(nomeAtual);

        Bundle bundle = this.getArguments();
        if (bundle != null) {

            listaRegistros = bundle.getParcelableArrayList("listaRegistros");
            listaRecentes = bundle.getParcelableArrayList("listaRecentes");
            listaCriticos = bundle.getParcelableArrayList("listaCriticos");

            Log.e("tamanhos", listaCriticos.size() + " " + listaRecentes.size() + " " + listaRegistros.size());

            inicializadoRegistros = true;
            inicializadoRecentes = true;
            inicializadoCriticos = true;

            if(listaRegistros.isEmpty()){
                inicializadoRegistros = false;
            }

            if(listaRecentes.isEmpty()){
                inicializadoRecentes = false;
            }

            if(listaCriticos.isEmpty()){
                inicializadoCriticos = false;
            }

            if (!inicializadoRegistros || !inicializadoRecentes ||  !inicializadoCriticos) {
                tabhost.setVisibility(View.INVISIBLE);
                preencherListas();
            }else{
                mapearLugaresDaLista(listaRegistros);
            }

        }else{
            tabhost.setVisibility(View.INVISIBLE);
            preencherListas();
        }

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mMapView = (MapView) view.findViewById(R.id.map);
        if(mMapView != null){

            mMapView.onCreate(null);
            mMapView.onResume();
            mMapView.getMapAsync(this);
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        tabhost.setOnTabChangedListener(new TabHost.OnTabChangeListener(){
            @Override
            public void onTabChanged(String tabId) {


                if(tabhost.getCurrentTabTag().equals("tag1")) {

                    mapearLugaresDaLista(listaRegistros);
                }

                if(tabhost.getCurrentTabTag().equals("tag2")) {

                    mapearLugaresDaLista(listaCriticos);
                }

                if(tabhost.getCurrentTabTag().equals("tag3")) {

                    mapearLugaresDaLista(listaRecentes);
                }

            }});

    }


    boolean mapaInicializado = true;

    public void mapearLugaresDaLista(ArrayList<Buraco> listaDeBuracos){

        Buraco buracoMapeado;
        double lat;
        double log;

        if (mMap == null){
            mapaInicializado = false;
            return;
        }else{
            mapaInicializado = true;
        }

        mMap.clear();
        for(int i = 0; i < listaDeBuracos.size(); i++)
        {
            try{

                buracoMapeado   = listaDeBuracos.get(i);
                lat             = Double.parseDouble(buracoMapeado.getLatitude());
                log             = Double.parseDouble(buracoMapeado.getLongitude());
                LatLng marca    = new LatLng(lat, log);

                mMap.addMarker(new MarkerOptions().position(marca).title("" + buracoMapeado.toString()));
                CameraUpdate local = CameraUpdateFactory.newLatLngZoom(marca, 11);
                mMap.animateCamera(local);

            }catch (Exception e){
                e.printStackTrace();
                Log.d("Erro ao mapear: ", e + "");
            }
        }


    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        //Se o mapa ainda nao tinha sido inicializado no momento do preenchimento,
        // o metodo sera chamado de novo, logo que, neste ponto o mapa ja esta inicializado
        if(!mapaInicializado){
            Log.e("naoInicializado", "" + mapaInicializado);
            preencherListas();
        }

    }


    public void preencherListas(){

        new Thread(new Runnable() {
            public void run() {

                while (!inicializadoRegistros || !inicializadoRecentes ||  !inicializadoCriticos){
                }

                handler1.post(new Runnable() {
                    public void run() {


                        if (listaRegistros.isEmpty()){

                            Log.e("ListaRegistro", "Vazio: " + listaRegistros.size());

                            TabHost.TabSpec ts;

                            ts = tabhost.newTabSpec("tag2");
                            ts.setContent(R.id.tab2);
                            ts.setIndicator("Críticos");
                            tabhost.addTab(ts);

                            ts = tabhost.newTabSpec("tag3");
                            ts.setContent(R.id.tab3);
                            ts.setIndicator("Recentes");
                            tabhost.addTab(ts);

                            tabhost.setCurrentTabByTag("tag2");

                            progressBar.setVisibility(View.INVISIBLE);
                            tabhost.setVisibility(View.VISIBLE);
                            mMapView.setVisibility(View.VISIBLE);
                            mapearLugaresDaLista(listaCriticos);

                        }else{

                            Log.e("ListaRegistro", "Cheia: " + listaRegistros.size());

                            TabHost.TabSpec ts;

                            ts = tabhost.newTabSpec("tag1");
                            ts.setContent(R.id.tab1);
                            ts.setIndicator("Registros");
                            tabhost.addTab(ts);

                            ts = tabhost.newTabSpec("tag2");
                            ts.setContent(R.id.tab2);
                            ts.setIndicator("Críticos");
                            tabhost.addTab(ts);

                            ts = tabhost.newTabSpec("tag3");
                            ts.setContent(R.id.tab3);
                            ts.setIndicator("Recentes");
                            tabhost.addTab(ts);

                            progressBar.setVisibility(View.INVISIBLE);
                            tabhost.setVisibility(View.VISIBLE);
                            mMapView.setVisibility(View.VISIBLE);

                            mapearLugaresDaLista(listaRegistros);
                        }

                    }
                });

            }
        }).start();


        if(listaRegistros.isEmpty()){
            inicializadoRegistros = false;
            listarRegistros();
        }

        if(listaRecentes.isEmpty()){
            inicializadoRecentes = false;
            listarRecentes();
        }

        if(listaCriticos.isEmpty()){
            inicializadoCriticos = false;
            listarCriticos();
        }


    }

    public void listarRegistros (){

        BuracoLocalDao dao = new BuracoLocalDao(getContext(), usuarioAtual.getEmail());

        try {

            dao.buscarBuracos(new OnGetFirebaseBuracosListener() {

                @Override
                public void onStart() {

                }

                @Override
                public void onRetornoLista(ArrayList<Buraco> listaRetornada){}

                @Override
                public void onRetornoDuasLista(ArrayList<Buraco> buracosAbertos, ArrayList<Buraco> buracosTampados){

                    listaRegistros = buracosAbertos;
                    inicializadoRegistros = true;
                    Log.e("registros", "OK");
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

        } catch (Exception e) {
            e.printStackTrace();
            Log.e("erro????","????");
        }


    }

    public void listarCriticos (){

        DaoFirebase dao = new DaoFirebase();

        dao.listarBuracosCriticos(new OnGetFirebaseBuracosListener() {
            @Override

            public void onStart() {

                //progressBar.setVisibility(View.VISIBLE);

            }

            @Override
            public void onRetornoLista(ArrayList<Buraco> listaRetornada){

                listaCriticos = listaRetornada;
                inicializadoCriticos = true;
                Log.e("criticos", "OK");
            }

            @Override
            public void onRetornoDuasLista(ArrayList<Buraco> buracosAbertos, ArrayList<Buraco> buracosTampados){}

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

    public void listarRecentes (){

        DaoFirebase dao = new DaoFirebase();

        dao.listarBuracosRecentes(0,new OnGetFirebaseBuracosListener() {

            @Override
            public void onStart() {

            }

            @Override
            public void onRetornoLista(ArrayList<Buraco> listaRetornada){}

            @Override
            public void onRetornoDuasLista(ArrayList<Buraco> buracosAbertos, ArrayList<Buraco> buracosTampados){

                inicializadoRecentes = true;
                listaRecentes = buracosAbertos;
                Log.e("recentes", "OK");
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


}