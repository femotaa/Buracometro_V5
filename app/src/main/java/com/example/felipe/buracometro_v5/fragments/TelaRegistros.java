package com.example.felipe.buracometro_v5.fragments;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.example.felipe.buracometro_v5.R;
import com.example.felipe.buracometro_v5.dao.BuracoLocalDao;
import com.example.felipe.buracometro_v5.dao.DaoFirebase;
import com.example.felipe.buracometro_v5.listeners.OnGetFirebaseBuracosListener;
import com.example.felipe.buracometro_v5.listeners.RecyclerViewClickListener;
import com.example.felipe.buracometro_v5.modelo.Buraco;
import com.example.felipe.buracometro_v5.modelo.Usuario;
import com.example.felipe.buracometro_v5.util.ConnectivityReceiver;
import com.example.felipe.buracometro_v5.util.ListaBuracoRecycleAdapter;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DatabaseError;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class TelaRegistros extends Fragment implements RecyclerViewClickListener, OnMapReadyCallback, ConnectivityReceiver.ConnectivityReceiverListener {

    private static final String TEXTO_TOOLBAR = "Registros";

    ArrayList<Buraco> listaRegistrosAberto = new ArrayList<Buraco>();
    ArrayList<Buraco> listaRegistrosTampados = new ArrayList<Buraco>();
    ArrayList<Buraco> listaCriticos = new ArrayList<Buraco>();
    ArrayList<Buraco> listaRecentesAberto = new ArrayList<Buraco>();
    ArrayList<Buraco> listaRecentesTampados = new ArrayList<Buraco>();


    boolean inicializadoCriticos = false;
    boolean inicializadoRecentes = false;
    boolean inicializadoRegistros = false;

    RecyclerView listaRecycleRegistros;
    ListaBuracoRecycleAdapter adapter;

    Button btnOpcAbertos;
    Button btnOpcTampados;

    ProgressBar progressBar;
    TabHost tabhost;

    SwipyRefreshLayout swipyRefreshLayout;

    ViewFlipper viewFlipper;
    private float lastX;
    short posFlipper = 0;

    LinearLayout conteudoTela;
    View view;

    GoogleMap googleMap;
    MapView mMapView;


    TextView textoViewFlip;
    ImageView imgViewFlip;
    String textos[] = {"Carregando...", "Aguarde um momento", "Buracometro"};

    Usuario usuarioAtual = new Usuario();

    int qualLista = 1;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.tela_registros, container, false);

        ImageView imgToolbar = (ImageView) getActivity().findViewById(R.id.img_icone);
        imgToolbar.setBackgroundDrawable(getResources().getDrawable(R.drawable.icone_registros));
        TextView textoToolbar = (TextView) getActivity().findViewById(R.id.texto_toolbar);
        textoToolbar.setText(TEXTO_TOOLBAR);

        setHasOptionsMenu(true);

        tabhost = (TabHost) view.findViewById(R.id.tabhost);
        tabhost.setup();
        TabHost.TabSpec ts = tabhost.newTabSpec("tag1");
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


        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        conteudoTela = (LinearLayout) view.findViewById(R.id.conteudo_layout);

        viewFlipper = (ViewFlipper) conteudoTela.findViewById(R.id.view_flipper);

        textoViewFlip = (TextView) conteudoTela.findViewById(R.id.textoRegistro);
        imgViewFlip = (ImageView) conteudoTela.findViewById(R.id.imageView);

        listaRecycleRegistros = (RecyclerView) conteudoTela.findViewById(R.id.listaRegistros);
        listaRecycleRegistros.setHasFixedSize(false);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(view.getContext());
        listaRecycleRegistros.setLayoutManager(linearLayoutManager);

        swipyRefreshLayout = (SwipyRefreshLayout) conteudoTela.findViewById(R.id.swipyrefreshlayout);
        swipyRefreshLayout.setColorSchemeColors(Color.parseColor("#FFFBBA04"));
        btnOpcAbertos = (Button) conteudoTela.findViewById(R.id.btnAbertos);
        btnOpcTampados = (Button) conteudoTela.findViewById(R.id.btnTampados);
        progressBar = (ProgressBar) conteudoTela.findViewById(R.id.progressBar);

        SharedPreferences settings = getActivity().getSharedPreferences("preferencias", 0);
        String emailAtual = settings.getString("login","");
        String idUsuarioAtual = settings.getString("IdLogin","");
        String nomeAtual = settings.getString("nome","");

        usuarioAtual.setEmail(emailAtual);
        usuarioAtual.setId(idUsuarioAtual);
        usuarioAtual.setNome(nomeAtual);

        listarRegistros();
        //checkConnection();

        return view;
    }

    private boolean checkConnection() {
        return ConnectivityReceiver.isConnected();
        //Toast.makeText(getContext(), "Conectadooo: " + isConnected, Toast.LENGTH_SHORT).show();
        //Log.e("Conetadooo", "" + isConnected);
    }

    boolean b = true; //Para botao tampado/aberto
    @Override
    public void onResume() {
        super.onResume();

        viewFlipper.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent touchevent) {

                switch (touchevent.getAction())
                {
                    case MotionEvent.ACTION_DOWN:{
                        lastX = touchevent.getX();
                        break;
                    }
                    case MotionEvent.ACTION_UP:{
                        float currentX = touchevent.getX();

                        if (lastX < currentX)
                        {
                            if (posFlipper == 0){

                            }else{
                                posFlipper--;
                                // Show The Previous Screen
                                //viewFlipper.showPrevious();
                            }
                        }

                        // if right to left swipe on screen
                        if (lastX > currentX)
                        {
                            if (posFlipper == 2){

                            }else{
                                posFlipper++;
                                // Show the next Screen
                                //viewFlipper.showNext();
                            }
                        }
                        break;
                    }
                }

                switch (posFlipper){
                    case 0:
                        textoViewFlip.setText(textos[0]);
                        imgViewFlip.setBackgroundDrawable(getResources().getDrawable(R.drawable.view_list_switch1));
                        break;
                    case 1:
                        textoViewFlip.setText(textos[1]);
                        imgViewFlip.setBackgroundDrawable(getResources().getDrawable(R.drawable.view_list_switch2));
                        break;
                    case 2:
                        textoViewFlip.setText(textos[2]);
                        imgViewFlip.setBackgroundDrawable(getResources().getDrawable(R.drawable.view_list_switch3));
                        break;
                }

                return true;
            }
        });


        btnOpcAbertos.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (!b){
                    b = true;
                    btnOpcAbertos.setEnabled(true);
                    btnOpcTampados.setEnabled(true);

                    btnOpcAbertos.setBackgroundDrawable(getResources().getDrawable(R.drawable.opn_lista_background));
                    btnOpcAbertos.setTextColor(Color.parseColor("#e9e9e9"));

                    btnOpcTampados.setBackgroundColor(Color.parseColor("#4a0d0d0d"));
                    btnOpcTampados.setTextColor(Color.parseColor("#e4767871"));

                    String irPara = tabhost.getCurrentTabTag();
                    switch (irPara){

                        case "tag1":
                            qualLista = 1;
                            listarRegistros();
                            break;

                        case "tag2":
                            qualLista = 3;
                            listarCriticos();
                            break;

                        case "tag3":
                            qualLista = 4;
                            listarRecentes();
                            //adapter = new ListaBuracoRecycleAdapter(getContext(), listaRecentesAberto, listaRecycleRegistros, TelaRegistros.this);
                            //listaRecycleRegistros.setAdapter(adapter);
                            break;
                    }
                }
            }
        });

        btnOpcTampados.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (b){

                    String irPara = tabhost.getCurrentTabTag();
                    if(!irPara.equals("tag2")){

                        b = false;
                        btnOpcAbertos.setEnabled(true);
                        btnOpcTampados.setEnabled(true);

                        btnOpcTampados.setBackgroundDrawable(getResources().getDrawable(R.drawable.opn_lista_background));
                        btnOpcTampados.setTextColor(Color.parseColor("#e9e9e9"));

                        btnOpcAbertos.setBackgroundColor(Color.parseColor("#4a0d0d0d"));
                        btnOpcAbertos.setTextColor(Color.parseColor("#e4767871"));

                        switch (irPara){
                            case "tag1":
                                qualLista = 2;
                                adapter = new ListaBuracoRecycleAdapter(getContext(), listaRegistrosTampados, true, listaRecycleRegistros, TelaRegistros.this);
                                listaRecycleRegistros.setAdapter(adapter);
                                break;

                            case "tag3":
                                qualLista = 5;
                                adapter = new ListaBuracoRecycleAdapter(getContext(), listaRecentesTampados, true, listaRecycleRegistros, TelaRegistros.this);
                                listaRecycleRegistros.setAdapter(adapter);
                                break;
                        }
                    }

                }
            }
        });


        tabhost.setOnTabChangedListener(new TabHost.OnTabChangeListener(){
            @Override
            public void onTabChanged(String tabId) {

                if(tabhost.getCurrentTabTag().equals("tag1")) {

                    qualLista = 1;
                    b = true;
                    btnOpcTampados.setVisibility(View.VISIBLE);

                    btnOpcAbertos.setBackgroundDrawable(getResources().getDrawable(R.drawable.opn_lista_background));
                    btnOpcAbertos.setTextColor(Color.parseColor("#e9e9e9"));

                    btnOpcTampados.setBackgroundColor(Color.parseColor("#4a0d0d0d"));
                    btnOpcTampados.setTextColor(Color.parseColor("#e4767871"));
                    mMapView.setVisibility(View.INVISIBLE);

                    listarRegistros();
                }

                if(tabhost.getCurrentTabTag().equals("tag2")) {

                    qualLista = 3;
                    b = true;
                    btnOpcTampados.setVisibility(View.INVISIBLE);

                    btnOpcAbertos.setBackgroundDrawable(getResources().getDrawable(R.drawable.opn_lista_background));
                    btnOpcAbertos.setTextColor(Color.parseColor("#e9e9e9"));

                    mMapView.setVisibility(View.INVISIBLE);

                    listarCriticos();
                }

                if(tabhost.getCurrentTabTag().equals("tag3")) {

                    qualLista = 4;
                    b = true;
                    btnOpcTampados.setVisibility(View.VISIBLE);
                    btnOpcAbertos.setBackgroundDrawable(getResources().getDrawable(R.drawable.opn_lista_background));
                    btnOpcAbertos.setTextColor(Color.parseColor("#e9e9e9"));

                    btnOpcTampados.setBackgroundColor(Color.parseColor("#4a0d0d0d"));
                    btnOpcTampados.setTextColor(Color.parseColor("#e4767871"));
                    mMapView.setVisibility(View.INVISIBLE);

                    listarRecentes();
                }

            }});

        swipyRefreshLayout.setOnRefreshListener(new SwipyRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(SwipyRefreshLayoutDirection direction) {

                if (direction == SwipyRefreshLayoutDirection.TOP){
                    atualizarLista();
                }
                else{
                    carregarMais();
                }
            }
        });
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mMapView = (MapView) conteudoTela.findViewById(R.id.map);
        if(mMapView != null){

            mMapView.onCreate(null);
            mMapView.onResume();
            mMapView.getMapAsync(this);
        }
    }

    @Override
    public void onMapReady(GoogleMap map) {
        MapsInitializer.initialize(getContext());
        googleMap = map;
    }


    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        Toast.makeText(getContext(), "Conectado: " + isConnected, Toast.LENGTH_SHORT).show();
        Log.e("Conetado", "" + isConnected);
    }

    //----------------------------------------------------------------------------------------
    //                         METODOS PARA ATUALIZACAO DAS LISTAS
    //----------------------------------------------------------------------------------------

    public void listarRegistros (){

        if(!inicializadoRegistros){

            BuracoLocalDao dao = new BuracoLocalDao(getContext(), usuarioAtual.getEmail());

            try {

                dao.buscarBuracos(new OnGetFirebaseBuracosListener() {

                    @Override
                    public void onStart() {

                        ArrayList<Buraco> listaBojo = new ArrayList<Buraco>();
                        adapter = new ListaBuracoRecycleAdapter(getContext(), listaBojo, listaRecycleRegistros, TelaRegistros.this);
                        listaRecycleRegistros.setAdapter(adapter);

                        progressBar.setVisibility(View.VISIBLE);

                        textos[0] = "Carregando...";
                        textos[1] = "...";
                        textos[2] = "Aguarde um momento...";
                        textoViewFlip.setText(textos[posFlipper]);

                        btnOpcTampados.setClickable(false);
                    }

                    @Override
                    public void onRetornoLista(ArrayList<Buraco> listaRetornada){}

                    @Override
                    public void onRetornoDuasLista(ArrayList<Buraco> buracosAbertos, ArrayList<Buraco> buracosTampados){

                        inicializadoRegistros = true;
                        listaRegistrosAberto = buracosAbertos;
                        listaRegistrosTampados = buracosTampados;
                        progressBar.setVisibility(View.INVISIBLE);
                        atualizarTextos();
                        btnOpcTampados.setClickable(true);
                        adapter = new ListaBuracoRecycleAdapter(getContext(), listaRegistrosAberto, listaRecycleRegistros, TelaRegistros.this);
                        listaRecycleRegistros.setAdapter(adapter);

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
            }

        }
        else{

            progressBar.setVisibility(View.INVISIBLE);
            atualizarTextos();
            adapter = new ListaBuracoRecycleAdapter(getContext(), listaRegistrosAberto, listaRecycleRegistros, TelaRegistros.this);
            listaRecycleRegistros.setAdapter(adapter);
        }


    }

    public void listarCriticos (){

        if(!inicializadoCriticos){

            DaoFirebase dao = new DaoFirebase();

            dao.listarBuracosCriticos(new OnGetFirebaseBuracosListener() {
                @Override

                public void onStart() {

                    ArrayList<Buraco> listaBojo = new ArrayList<Buraco>();
                    adapter = new ListaBuracoRecycleAdapter(getContext(), listaBojo, listaRecycleRegistros, TelaRegistros.this);
                    listaRecycleRegistros.setAdapter(adapter);

                    progressBar.setVisibility(View.VISIBLE);

                    textos[0] = "Carregando...";
                    textos[1] = "...";
                    textos[2] = "Aguarde um momento...";
                    textoViewFlip.setText(textos[posFlipper]);

                    btnOpcTampados.setClickable(false);

                }

                @Override
                public void onRetornoLista(ArrayList<Buraco> listaRetornada){

                    inicializadoCriticos = true;
                    listaCriticos = listaRetornada;
                    progressBar.setVisibility(View.INVISIBLE);
                    atualizarTextos();
                    btnOpcTampados.setClickable(true);
                    adapter = new ListaBuracoRecycleAdapter(getContext(), listaCriticos, listaRecycleRegistros, TelaRegistros.this, true);
                    listaRecycleRegistros.setAdapter(adapter);

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
        else{

            progressBar.setVisibility(View.INVISIBLE);
            atualizarTextos();
            adapter = new ListaBuracoRecycleAdapter(getContext(), listaCriticos, listaRecycleRegistros, TelaRegistros.this, true);
            listaRecycleRegistros.setAdapter(adapter);
        }

    }

    public void listarRecentes (){

        if(!inicializadoRecentes){

            DaoFirebase dao = new DaoFirebase();

            dao.listarBuracosRecentes(0,new OnGetFirebaseBuracosListener() {

                @Override
                public void onStart() {

                    ArrayList<Buraco> listaBojo = new ArrayList<Buraco>();
                    adapter = new ListaBuracoRecycleAdapter(getContext(), listaBojo, listaRecycleRegistros, TelaRegistros.this);
                    listaRecycleRegistros.setAdapter(adapter);

                    progressBar.setVisibility(View.VISIBLE);

                    textos[0] = "Carregando...";
                    textos[1] = "...";
                    textos[2] = "Aguarde um momento...";
                    textoViewFlip.setText(textos[posFlipper]);

                    btnOpcTampados.setClickable(false);
                }

                @Override
                public void onRetornoLista(ArrayList<Buraco> listaRetornada){}

                @Override
                public void onRetornoDuasLista(ArrayList<Buraco> buracosAbertos, ArrayList<Buraco> buracosTampados){

                    inicializadoRecentes = true;
                    listaRecentesAberto = buracosAbertos;
                    listaRecentesTampados = buracosTampados;
                    progressBar.setVisibility(View.INVISIBLE);
                    atualizarTextos();
                    btnOpcTampados.setClickable(true);
                    adapter = new ListaBuracoRecycleAdapter(getContext(), listaRecentesAberto, listaRecycleRegistros, TelaRegistros.this);
                    listaRecycleRegistros.setAdapter(adapter);

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
        else{

            progressBar.setVisibility(View.INVISIBLE);
            atualizarTextos();
            adapter = new ListaBuracoRecycleAdapter(getContext(), listaRecentesAberto, listaRecycleRegistros, TelaRegistros.this);
            listaRecycleRegistros.setAdapter(adapter);
        }

    }

    public void atualizarTextos(){

        switch (qualLista){
            case 1:
            case 2:

                if(!listaRegistrosAberto.isEmpty()){

                    textos[0] = "Registros: " + listaRegistrosAberto.size();

                }else{

                    textos[0] = "Você não possui registros de buracos abertos";
                    textos[1] = "Se souber de algum buraco, pode nos avisar :)" + 0;
                    textos[2] = ":)";
                }


                if(!listaRegistrosTampados.isEmpty()){

                    textos[1] = "Tampados: " + listaRegistrosTampados.size();
                    textos[2] = ":)";

                }else{

                    textos[1] = "Você não possui registro de buracos tampados";
                    textos[2] = "Se souber de algum buraco, pode nos avisar :)";
                }
                break;

            case 3:
                if(!listaCriticos.isEmpty()){

                    textos[0] = "Críticos: " + listaCriticos.size();
                    textos[1] = "Total de ocorrencias: " + 999;
                    textos[2] = "Colocar algo aqui";

                }else{

                    textos[0] = "Não há registros";
                    textos[1] = "Total de ocorrencias " + 0;
                    textos[2] = ":)";
                }
                break;

            case 4:
            case 5:

                if(!listaRecentesAberto.isEmpty()){

                    textos[0] = "Recentes: " + listaRecentesAberto.size();

                }else{

                    textos[0] = "Não há buracos Abertos";
                    textos[1] = "Total de ocorrencias " + 0;
                    textos[2] = ":)";
                }


                if(!listaRecentesTampados.isEmpty()){

                    textos[1] = "Tampados: " + listaRecentesTampados.size();
                    textos[2] = ":)";

                }else{

                    textos[1] = "Não há buracos tampados";
                    textos[2] = ":)";
                }
                break;

        }

        textoViewFlip.setText(textos[posFlipper]);
    }


    //----------------------------------------------------------------------------------------
    //                          METODOS PARA CONTROLE DAS LISTAS
    //----------------------------------------------------------------------------------------

    int paginaRegistro = 0;
    int paginaRegistroTampado = 0;
    int paginaCriticos = 0;
    int paginaRecente = 0;
    int paginaRecenteTampando = 0;

    boolean carregaMaisDaLista = false;

    private void carregarMais(){

        carregaMaisDaLista = true;

        switch (qualLista){
            case 1:
                paginaRegistro++;
                break;
            case 2:
                paginaRegistroTampado++;
                break;
            case 3:
                paginaCriticos++;
                break;
            case 4:
                paginaRecente++;
                break;
            case 5:
                paginaRecenteTampando++;
                break;
        }
    }

    private void atualizarLista(){

        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                Log.e("haint", "Load More 2");
                //adapter = new ListaBuracoRecycleAdapter(getContext(), listaRegistrosAberto, listaRecycleRegistros, TelaRegistros.this);

                //Load data
                int index = listaRegistrosAberto.size();
                int end = index + 4;

                for (int i = index; i < end; i++) {
                    Buraco testeBuraco = new Buraco();
                    testeBuraco.setCidade("Osasco");
                    testeBuraco.setDataTampado("01/05/2017");
                    testeBuraco.setRua("Rua Portugal");
                    listaRegistrosAberto.add(testeBuraco);
                }

                adapter.notifyDataSetChanged();
                adapter.setLoaded();

                swipyRefreshLayout.setRefreshing(false);

            }
        },3000);
    }



    //----------------------------------------------------------------------------------------
    //                     METODOS DE MANIPULACAO DO CONTEXT E POPOUP MENU
    //----------------------------------------------------------------------------------------

    //Metodo que resitro contexto para o ContextMenu
    public void registraContextMenu(View view)
    {
        registerForContextMenu(listaRecycleRegistros);
        view.showContextMenu();
    }

    //Metodo para criacao do Context Menu
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo)
    {
        super.onCreateContextMenu(menu, v, menuInfo);

        switch (qualLista){
            case 1:
                menu.add("Mapear Lugar");
                menu.add("Excluir Registro");
                menu.add("Tampar buraco");
                menu.add("Informações");
                break;
            case 2:
                menu.add("Mapear Lugar");
                menu.add("Excluir Registro");
                menu.add("Informações");
                break;
            case 3:
            case 4:
            case 5:
                menu.add("Mapear Lugar");
                menu.add("Informações");
                break;
        }
    }

    //Metodo que define acao da opcao selecionada
    public boolean onContextItemSelected(MenuItem item)
    {
        //AdapterView.AdapterContextMenuInfo menuInfo = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();

        if (item.getTitle() == "Mapear Lugar")
        {
            mapearBuracosSelecionado();
        }

        if (item.getTitle() == "Excluir Registro")
        {
            excluirBuraco();
        }

        if (item.getTitle() == "Informações")
        {
            mostrarInformacaoDoBuraco();
        }

        if (item.getTitle() == "Tampar buraco")
        {
            tamparBuraco();
        }

        return true;
    }

    int positionDaLista;
    @Override
    public void recyclerViewListClicked(View v, int position) {
        positionDaLista = position;
        registraContextMenu(v);
    }



    //----------------------------------------------------------------------------------------
    //                     METODOS DE MANIPULACAO DO POPOUP MENU
    //----------------------------------------------------------------------------------------

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.add("Mapear todos");
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getTitle() == "Mapear todos"){
            mapearTodos();
        }

        return super.onOptionsItemSelected(item);
    }


    //----------------------------------------------------------------------------------------
    //                      METODOS DAS OPCOES SELECIONADAS NOS MENUS
    //----------------------------------------------------------------------------------------

    public void mapearBuracosSelecionado()
    {
        Buraco buracoDaLista = adapter.getItem(positionDaLista);

        try {

            LatLng marca = new LatLng(Double.parseDouble(buracoDaLista.getLatitude()), Double.parseDouble(buracoDaLista.getLongitude()));

            mMapView.setVisibility(View.VISIBLE);

            googleMap.addMarker(new MarkerOptions().position(marca).title(buracoDaLista.toString()));
            CameraUpdate local = CameraUpdateFactory.newLatLngZoom(marca, 17);
            googleMap.animateCamera(local);

        }
        catch (Exception e)
        {
            e.printStackTrace();
            Toast.makeText(view.getContext(), "Erro ao mapear", Toast.LENGTH_SHORT).show();
        }

    }

    public void mostrarInformacaoDoBuraco(){

        final Buraco buracoDaLista = adapter.getItem(positionDaLista);

        String dataRegistro = "";
        String dataTampado = "";

        try {
            long timestamp = Long.parseLong(buracoDaLista.getData_Registro()) * 1000L;

            DateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
            Date netDate = (new Date(timestamp));
            dataRegistro =  sdf.format(netDate);

            if(buracoDaLista.getDataTampado() != null){

                long timestamp2 = Long.parseLong(buracoDaLista.getDataTampado()) * 1000L;

                DateFormat sdf2 = new SimpleDateFormat("dd-MM-yyyy");
                Date netDate2 = (new Date(timestamp2));
                dataTampado =  sdf2.format(netDate2);
            }

        }catch (Exception e){
            e.printStackTrace();
        }


        AlertDialog alertar;
        AlertDialog.Builder mensagemInfo = new AlertDialog.Builder(getContext());
        mensagemInfo.setTitle("Informações do Registro: ");

        switch (qualLista){
            case 1:
                mensagemInfo.setMessage(
                                "Endereço: " + buracoDaLista.getRua() +
                                "\nCidade: " + buracoDaLista.getCidade() + " - " + buracoDaLista.getEstado() +
                                "\nData de Registro: " + dataRegistro +
                                "\nLatitude: " + buracoDaLista.getLatitude() +
                                "\nLongitude: " + buracoDaLista.getLongitude() +
                                "\nStatus: " + buracoDaLista.getStatusBuraco());

                mensagemInfo.setNeutralButton(" Tampar ", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        tamparBuraco();
                    }
                });

                break;

            case 2:
                mensagemInfo.setMessage(
                                "Endereço: " + buracoDaLista.getRua() +
                                "\nCidade: " + buracoDaLista.getCidade() + " - " + buracoDaLista.getEstado() +
                                "\nData de Registro: " + dataRegistro +
                                "\nLatitude: " + buracoDaLista.getLatitude() +
                                "\nLongitude: " + buracoDaLista.getLongitude() +
                                "\nStatus: " + buracoDaLista.getStatusBuraco() +
                                "\nData Tampado: " + dataTampado);

                mensagemInfo.setNeutralButton(" Excluir ", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        excluirBuraco();
                    }
                });

                break;

            case 3:
                mensagemInfo.setMessage(
                                "Endereço: " + buracoDaLista.getRua() +
                                "\nCidade: " + buracoDaLista.getCidade() + " - " + buracoDaLista.getEstado() +
                                "\nData de Registro: " + dataRegistro +
                                "\nLatitude: " + buracoDaLista.getLatitude() +
                                "\nLongitude: " + buracoDaLista.getLongitude() +
                                "\nStatus: " + buracoDaLista.getStatusBuraco() +
                                "\nOcorrencias: " + buracoDaLista.getQtdOcorrencia());
                break;

            case 4:
                mensagemInfo.setMessage(
                                "Endereço: " + buracoDaLista.getRua() +
                                "\nCidade: " + buracoDaLista.getCidade() + " - " + buracoDaLista.getEstado() +
                                "\nData de Registro: " + dataRegistro +
                                "\nLatitude: " + buracoDaLista.getLatitude() +
                                "\nLongitude: " + buracoDaLista.getLongitude() +
                                "\nStatus: " + buracoDaLista.getStatusBuraco());
                break;

            case 5:
                mensagemInfo.setMessage(
                                "Endereço: " + buracoDaLista.getRua() +
                                "\nCidade: " + buracoDaLista.getCidade() + " - " + buracoDaLista.getEstado() +
                                "\nData de Registro: " + dataRegistro +
                                "\nLatitude: " + buracoDaLista.getLatitude() +
                                "\nLongitude: " + buracoDaLista.getLongitude() +
                                "\nStatus: " + buracoDaLista.getStatusBuraco() +
                                "\nOcorrencias: " + buracoDaLista.getQtdOcorrencia() +
                                "\nData Tampado: " + dataTampado);
                break;
        }


        mensagemInfo.setPositiveButton(" Cancelar ", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        mensagemInfo.setNegativeButton("Mapear ", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                mapearBuracosSelecionado();
            }
        });

        alertar = mensagemInfo.create();
        alertar.show();
    }

    public void tamparBuraco(){

        AlertDialog alertar;
        AlertDialog.Builder mensagemInfo = new AlertDialog.Builder(getContext());
        mensagemInfo.setTitle("Buraco está tampado?");

        mensagemInfo.setMessage("Deseja realmente notificar que este buraco esta Tampado?");

        mensagemInfo.setPositiveButton(" Sim ", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                Buraco buracoDaLista = adapter.getItem(positionDaLista);
                Long tsLong = System.currentTimeMillis()/1000;
                String timestamp = tsLong.toString();
                buracoDaLista.setDataTampado(timestamp);

                BuracoLocalDao daoLocal = new BuracoLocalDao(getContext(), usuarioAtual.getEmail());
                DaoFirebase dao = new DaoFirebase();
                dao.atualizarStatusParaTampado(buracoDaLista);

                try{

                    daoLocal.tamparBuracoLocal(buracoDaLista);
                    listaRegistrosAberto.remove(positionDaLista);

                    listaRegistrosTampados.add(0,buracoDaLista);
                    adapter = new ListaBuracoRecycleAdapter(getContext(), listaRegistrosAberto, listaRecycleRegistros, TelaRegistros.this);
                    listaRecycleRegistros.setAdapter(adapter);
                    atualizarTextos();

                }catch (Exception e){
                    e.printStackTrace();
                    Toast.makeText(getContext(), "Erro ao atualizar base local", Toast.LENGTH_SHORT).show();
                }
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

    public void excluirBuraco(){

        //Exibe mensagem de alerta
        AlertDialog alertar;
        AlertDialog.Builder mensagemExlcuir = new AlertDialog.Builder(getContext());

        mensagemExlcuir.setTitle("Excluir Registro?");
        mensagemExlcuir.setMessage("Você deseja deletar o buraco selecionado?");

        mensagemExlcuir.setPositiveButton(" Excluir ", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                Buraco buracoDaLista = adapter.getItem(positionDaLista);
                BuracoLocalDao daoLocal = new BuracoLocalDao(getContext(), usuarioAtual.getEmail());

                try{

                    daoLocal.deletarBuraco(buracoDaLista);

                    if(qualLista == 1){

                        listaRegistrosAberto.remove(positionDaLista);
                        adapter = new ListaBuracoRecycleAdapter(getContext(), listaRegistrosAberto, listaRecycleRegistros, TelaRegistros.this);

                    }else{

                        listaRegistrosTampados.remove(positionDaLista);
                        adapter = new ListaBuracoRecycleAdapter(getContext(), listaRegistrosTampados, listaRecycleRegistros, TelaRegistros.this);
                    }

                    listaRecycleRegistros.setAdapter(adapter);
                    atualizarTextos();

                }catch (Exception e){
                    e.printStackTrace();
                    Toast.makeText(getContext(), "Erro ao atualizar base local", Toast.LENGTH_SHORT).show();
                }

            }
        });

        mensagemExlcuir.setNeutralButton("Cancelar", new DialogInterface.OnClickListener() {
            public void onClick(final DialogInterface dialog, int which) {

                dialog.dismiss();
            }
        });

        alertar = mensagemExlcuir.create();
        alertar.show();

    }

    public void mapearTodos(){

        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("listaRegistros", listaRegistrosAberto);
        bundle.putParcelableArrayList("listaRecentes", listaRecentesAberto);
        bundle.putParcelableArrayList("listaCriticos", listaCriticos);

        FragmentManager fr = getFragmentManager();
        TelaMapa objetoDaTela = new TelaMapa();
        FragmentTransaction fragmentTransaction = fr.beginTransaction();

        objetoDaTela.setArguments(bundle);

        fragmentTransaction.replace(R.id.bau_de_fragments,objetoDaTela,"TelaMapa");

        fragmentTransaction.addToBackStack("TelaMapa");
        fragmentTransaction.commit();

    }
}
