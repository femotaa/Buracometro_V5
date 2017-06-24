package com.example.felipe.buracometro_v5.fragments;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TabHost;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.example.felipe.buracometro_v5.R;
import com.example.felipe.buracometro_v5.dao.BuracoLocalDao;
import com.example.felipe.buracometro_v5.dao.BuracoWebDao;
import com.example.felipe.buracometro_v5.listeners.RecyclerViewClickListener;
import com.example.felipe.buracometro_v5.modelo.Buraco;
import com.example.felipe.buracometro_v5.util.ListaBuracoRecycleAdapter;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;


public class TelaRegistros extends Fragment implements RecyclerViewClickListener, OnMapReadyCallback {

    private static final String TEXTO_TOOLBAR = "Registros";

    ArrayList<Buraco> listaRegistrosAberto = new ArrayList<Buraco>();
    ArrayList<Buraco> listaRegistrosTampados = new ArrayList<Buraco>();
    ArrayList<Buraco> listaCriticos = new ArrayList<Buraco>();
    ArrayList<Buraco> listaRecentesAberto = new ArrayList<Buraco>();
    ArrayList<Buraco> listaRecentesTampados = new ArrayList<Buraco>();

    boolean inicializadoRegistroAberto = false;
    boolean inicializadoRegistroTampados = false;
    boolean inicializadoCriticos = false;
    boolean inicializadoRecentesAberto = false;
    boolean inicializadoRecentesTampados = false;

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

    BuracoLocalDao daoLocal;
    BuracoWebDao daoWeb = new BuracoWebDao();

    int qualLista = 0;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fr_tela_registros, container, false);

        ImageView imgToolbar = (ImageView) getActivity().findViewById(R.id.img_icone);
        imgToolbar.setBackgroundDrawable(getResources().getDrawable(R.drawable.icone_registros));
        TextView textoToolbar = (TextView) getActivity().findViewById(R.id.texto_toolbar);
        textoToolbar.setText(TEXTO_TOOLBAR);

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

        daoLocal = new BuracoLocalDao(view.getContext());

        listarRegistros();

        return view;
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
                            listarRegistros();
                            break;
                        case "tag2":
                            listarCriticos();
                            break;
                        case "tag3":
                            listarRecentes();
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
                                listarRegistros();
                                break;
                            case "tag3":
                                listarRecentes();
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
                    Log.e("apertado", "apertado 1");
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
                    b = true;
                    btnOpcTampados.setVisibility(View.INVISIBLE);

                    btnOpcAbertos.setBackgroundDrawable(getResources().getDrawable(R.drawable.opn_lista_background));
                    btnOpcAbertos.setTextColor(Color.parseColor("#e9e9e9"));

                    mMapView.setVisibility(View.INVISIBLE);

                    listarCriticos();
                }
                if(tabhost.getCurrentTabTag().equals("tag3")) {

                    Log.e("apertado2", "apertado 2");
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


    //----------------------------------------------------------------------------------------
    //                         METODOS PARA ATUALIZACAO DAS LISTAS
    //----------------------------------------------------------------------------------------

    public void listarRegistros (){

        if(qualLista != 1){

            qualLista = 1;
            if(inicializadoRegistroAberto == false && listaRegistrosAberto.isEmpty()){
                new AsyncCaller().execute();
            }
            else{
                if(inicializadoRegistroAberto == true && listaRegistrosAberto.isEmpty()){
                    adapterListaRegistroAbertos();
                    listaRecycleRegistros.setAdapter(adapter);
                    msgListaVazia("Registro Abertos");

                }else{
                    adapterListaRegistroAbertos();
                    listaRecycleRegistros.setAdapter(adapter);
                }
            }

        }else{

            qualLista = 2;
            if(inicializadoRegistroTampados == false && listaRegistrosTampados.isEmpty()){
                new AsyncCaller().execute();
            }
            else{
                if(inicializadoRegistroTampados == true && listaRegistrosTampados.isEmpty()){
                    adapterListaRegistroTampados();
                    listaRecycleRegistros.setAdapter(adapter);
                    msgListaVazia("Registros Tampados");

                }else{
                    adapterListaRegistroTampados();
                    listaRecycleRegistros.setAdapter(adapter);
                    atualizarTextos();
                }
            }
        }
    }

    public void listarCriticos (){

        if(qualLista != 3){

            qualLista = 3;
            if(inicializadoCriticos == false && listaCriticos.isEmpty()){
                new AsyncCaller().execute();
            }
            else{
                if(inicializadoCriticos == true && listaCriticos.isEmpty()){

                    adapterListaCriticos();
                    listaRecycleRegistros.setAdapter(adapter);
                    msgListaVazia("Registro Criticos");
                }else{

                    adapterListaCriticos();
                    listaRecycleRegistros.setAdapter(adapter);
                    atualizarTextos();
                }
            }
        }
    }

    public void listarRecentes (){

        if(qualLista != 4){

            qualLista = 4;
            if(inicializadoRecentesAberto == false && listaRecentesAberto.isEmpty()){
                new AsyncCaller().execute();
            }
            else{
                if(inicializadoRecentesAberto == true && listaRecentesAberto.isEmpty()){
                    adapterListaRecentesAbertos();
                    listaRecycleRegistros.setAdapter(adapter);
                    msgListaVazia("Registro Abertos");

                }else{
                    adapterListaRecentesAbertos();
                    listaRecycleRegistros.setAdapter(adapter);
                    atualizarTextos();
                }
            }

        }else{

            qualLista = 5;
            if(inicializadoRecentesTampados == true && listaRecentesTampados.isEmpty()){
                adapterListaRecentesTampados();
                listaRecycleRegistros.setAdapter(adapter);
                msgListaVazia("Registros Tampados");

            }else{
                adapterListaRecentesTampados();
                listaRecycleRegistros.setAdapter(adapter);
                atualizarTextos();
            }
        }
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

        new AsyncCaller().execute();
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

    void adapterListaRegistroAbertos(){
        adapter = new ListaBuracoRecycleAdapter(getContext(), listaRegistrosAberto, listaRecycleRegistros, TelaRegistros.this);
    }

    void adapterListaRegistroTampados(){
        adapter = new ListaBuracoRecycleAdapter(getContext(),listaRegistrosTampados, true, listaRecycleRegistros, TelaRegistros.this);
    }

    void adapterListaRecentesAbertos(){
        adapter = new ListaBuracoRecycleAdapter(getContext(), listaRecentesAberto, listaRecycleRegistros, TelaRegistros.this);
    }

    void adapterListaRecentesTampados(){
        adapter = new ListaBuracoRecycleAdapter(getContext(),listaRecentesTampados, true, listaRecycleRegistros, TelaRegistros.this);
    }

    void adapterListaCriticos(){
        adapter = new ListaBuracoRecycleAdapter(getContext(), listaCriticos, listaRecycleRegistros, TelaRegistros.this, true);
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
            Log.d("position", "" + positionDaLista);
        }

        if (item.getTitle() == "Informações")
        {
            mostrarInformacaoDoBuraco();
        }

        if (item.getTitle() == "Tampar buraco")
        {
            Log.d("position", "" + positionDaLista);
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
        AlertDialog alertar;
        AlertDialog.Builder mensagemInfo = new AlertDialog.Builder(getContext());
        mensagemInfo.setTitle("Informações do Registro: ");

        switch (qualLista){
            case 1:
                mensagemInfo.setMessage(
                                "Id: " + buracoDaLista.getIdBuraco() +
                                "\nEndereço: " + buracoDaLista.getRua() +
                                "\nCidade: " + buracoDaLista.getCidade() + " - " + buracoDaLista.getEstado() +
                                "\nData de Registro: " + buracoDaLista.getData_Registro() +
                                "\nLatitude: " + buracoDaLista.getLatitude() +
                                "\nLongitude: " + buracoDaLista.getLongitude() +
                                "\nStatus: " + buracoDaLista.getStatusBuraco());

                mensagemInfo.setNeutralButton(" Tampar ", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        tamparBuraco(buracoDaLista);
                    }
                });

                mensagemInfo.setNeutralButton(" Excluir ", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        msgExcluirBuraco(buracoDaLista);
                    }
                });

                break;

            case 2:
                mensagemInfo.setMessage(
                        "Id: " + buracoDaLista.getIdBuraco() +
                                "\nEndereço: " + buracoDaLista.getRua() +
                                "\nCidade: " + buracoDaLista.getCidade() + " - " + buracoDaLista.getEstado() +
                                "\nData de Registro: " + buracoDaLista.getData_Registro() +
                                "\nLatitude: " + buracoDaLista.getLatitude() +
                                "\nLongitude: " + buracoDaLista.getLongitude() +
                                "\nStatus: " + buracoDaLista.getStatusBuraco() +
                                "\nData Tampado: " + buracoDaLista.getDataTampado());

                mensagemInfo.setNeutralButton(" Excluir ", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        msgExcluirBuraco(buracoDaLista);
                    }
                });

                break;

            case 3:
                mensagemInfo.setMessage(
                        "Id: " + buracoDaLista.getIdBuraco() +
                                "\nEndereço: " + buracoDaLista.getRua() +
                                "\nCidade: " + buracoDaLista.getCidade() + " - " + buracoDaLista.getEstado() +
                                "\nData de Registro: " + buracoDaLista.getData_Registro() +
                                "\nLatitude: " + buracoDaLista.getLatitude() +
                                "\nLongitude: " + buracoDaLista.getLongitude() +
                                "\nStatus: " + buracoDaLista.getStatusBuraco() +
                                "\nOcorrencias: " + buracoDaLista.getQtdOcorrencia() +
                                "\nVezes Reaberto: " + buracoDaLista.getQtdReabertos());
                break;

            case 4:
                mensagemInfo.setMessage(
                        "Id: " + buracoDaLista.getIdBuraco() +
                                "\nEndereço: " + buracoDaLista.getRua() +
                                "\nCidade: " + buracoDaLista.getCidade() + " - " + buracoDaLista.getEstado() +
                                "\nData de Registro: " + buracoDaLista.getData_Registro() +
                                "\nLatitude: " + buracoDaLista.getLatitude() +
                                "\nLongitude: " + buracoDaLista.getLongitude() +
                                "\nStatus: " + buracoDaLista.getStatusBuraco());
                break;

            case 5:
                mensagemInfo.setMessage(
                        "Id: " + buracoDaLista.getIdBuraco() +
                                "\nEndereço: " + buracoDaLista.getRua() +
                                "\nCidade: " + buracoDaLista.getCidade() + " - " + buracoDaLista.getEstado() +
                                "\nData de Registro: " + buracoDaLista.getData_Registro() +
                                "\nLatitude: " + buracoDaLista.getLatitude() +
                                "\nLongitude: " + buracoDaLista.getLongitude() +
                                "\nStatus: " + buracoDaLista.getStatusBuraco() +
                                "\nOcorrencias: " + buracoDaLista.getQtdOcorrencia() +
                                "\nVezes Reaberto: " + buracoDaLista.getQtdReabertos() +
                                "\nData Tampado: " + buracoDaLista.getDataTampado());
                break;
        }


        mensagemInfo.setPositiveButton(" Cancelar ", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        mensagemInfo.setNegativeButton(" Mapear ", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                mapearBuracosSelecionado();
            }
        });

        alertar = mensagemInfo.create();
        alertar.show();
    }

    public void tamparBuraco(Buraco buracoDaLista){

    }

    public void msgExcluirBuraco(Buraco buracoDaLista){

        final Buraco buracoExcluir = buracoDaLista;
        //Exibe mensagem de alerta
        AlertDialog alertar;
        AlertDialog.Builder mensagemExlcuir = new AlertDialog.Builder(getContext());

        mensagemExlcuir.setTitle("ATENÇÃO!");
        mensagemExlcuir.setMessage("Você deseja deletar o buraco selecionado? \n \n Uma vez excluido, não será recuperado, para seus registros");

        mensagemExlcuir.setNeutralButton("Excluir", new DialogInterface.OnClickListener() {
            public void onClick(final DialogInterface dialog, int which) {

                final Handler handler = new Handler();
                new Thread(new Runnable() {
                    public void run() {

                        boolean excluido = false;
                        try{
                            excluido = daoWeb.excluirBuraco(buracoExcluir);

                        }catch (Exception e){
                            e.printStackTrace();
                        }

                        if (excluido) {
                            try {

                                daoLocal.deletarBuraco(buracoExcluir);

                                handler.post(new Runnable() {
                                    public void run() {

                                        Toast.makeText(getContext(), "Registro: " + buracoExcluir.getIdBuraco() + " deletado!", Toast.LENGTH_SHORT).show();
                                    }
                                });

                            } catch (Exception e) {

                                e.printStackTrace();
                                handler.post(new Runnable() {
                                    public void run() {
                                        Toast.makeText(getContext(), "Erro ao exlcuir na base local", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        } else {
                            handler.post(new Runnable() {
                                public void run() {
                                    Toast.makeText(getContext(), "Erro ao exlcuir", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }

                        dialog.dismiss();
                    }
                }).start();


            }
        });


        mensagemExlcuir.setPositiveButton(" Cancelar ", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        alertar = mensagemExlcuir.create();
        alertar.show();

    }


    //----------------------------------------------------------------------------------------
    //                          OPÇÕES PARA VISUALIZAÇÃO NA VIEW
    //----------------------------------------------------------------------------------------

    public void msgListaVazia(String msg){
        Toast.makeText(getContext(), "Nao há " + msg, Toast.LENGTH_SHORT).show();
    }

    boolean conexaoOk = true;
    boolean erroRegistros = false;
    boolean erroCriticos = false;
    boolean erroRecentes = false;
    int totalRecentes;
    int totalHoje;
    int totalTampados;

    public void atualizarTextos(){

        if(qualLista == 1 || qualLista == 2){

            if(!erroRegistros)
            {
                if (!listaRegistrosAberto.isEmpty() || !listaRegistrosTampados.isEmpty())
                {
                    textos[0] = "ABERTOS: " + listaRegistrosAberto.size();
                    textos[1] = "Encontrados hoje: " ;
                    textos[2] = "Tampados: " + listaRegistrosTampados.size();
                }else{
                    textos[0] = "SEM REGISTROS";
                    textos[1] = "Se soube de um buraco, notifique" ;
                    textos[2] = ";D";
                }
            }else{
                textos[0] = "Erro no acesso aos registros";
                textos[1] = "Tente mais tarde..." ;
                textos[2] = ":S";
            }

        }else if(qualLista == 4 || qualLista == 5){

            if(conexaoOk){
                if (!listaRecentesAberto.isEmpty() || !listaRecentesTampados.isEmpty())
                {
                    textos[0] = "Abertos nos ultimos 5 meses: " + totalRecentes;
                    textos[1] = "Encontrados hoje: " + totalHoje;
                    textos[2] = "Tampados nos ultimos 5 meses: " + totalTampados;
                }else{
                    textos[0] = "SEM BURACOS RECENTES";
                    textos[1] = "Se souber de um buraco, notifique" ;
                    textos[2] = ";D";
                }
            }else{

                textos[0] = "SEM CONEXÃO";
                textos[1] = "Tente mais tarde...";
                textos[2] = "BURACOMETRO";

                if(erroRecentes){
                    textos[0] = "Ocorreu um erro...";
                    textos[1] = "Tente mais tarde...";
                    textos[2] = "Erro ao acessar dados recentes";
                }
            }

        }else if(qualLista == 3){

            if(conexaoOk)
            {
                if (!listaCriticos.isEmpty())
                {
                    textos[0] = "Numero de Buracos Criticos: " + listaCriticos.size();
                    textos[1] = "Total de Ocorrências: " + 9999;
                    textos[2] = "MAIS COISA CRITICA";
                }else{
                    textos[0] = "SEM BURACOS CRITICOS";
                    textos[1] = "Se soube de um buraco, notifique" ;
                    textos[2] = ";D";
                }
            }else{
                textos[0] = "SEM CONEXÃO";
                textos[1] = "Tente mais tarde...";
                textos[2] = "BURACOMETRO";

                if(erroCriticos){
                    textos[0] = "Ocorreu um erro...";
                    textos[1] = "Tente mais tarde...";
                    textos[2] = "Erro ao acessar dados criticos";
                }
            }
        }
        textoViewFlip.setText(textos[posFlipper]);
    }

    //----------------------------------------------------------------------------------------
    //                      CLASSE PARA CARREGAR LISTAS EM THREADS
    //----------------------------------------------------------------------------------------
    private class AsyncCaller extends AsyncTask<Void, Void, Void>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            if (!carregaMaisDaLista){
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
        }

        boolean erroCarregarMais = false;
        @Override
        protected Void doInBackground(Void... params) {

            if(!carregaMaisDaLista){
                switch (qualLista){

                    case 1:
                    case 2:
                        try{

                            listaRegistrosAberto = daoLocal.buscaBuracos();
                            adapterListaRegistroAbertos();
                            inicializadoRegistroAberto = true;

                            listaRegistrosTampados = daoLocal.buscaBuracosTampados();
                            inicializadoRegistroTampados = true;

                            erroRegistros = false;

                        }catch (Exception e){
                            e.printStackTrace();
                            erroRegistros = true;
                        }
                        break;

                    case 3:
                        try{
                            listaCriticos = daoWeb.buscarMaisCriticos(0);
                            adapterListaCriticos();

                            conexaoOk = true;
                            erroCriticos = false;
                            inicializadoCriticos = true;

                        }catch (ConnectException e){
                            conexaoOk = false;
                            Log.e("Errouuuuu", "ConnectException");

                        }catch (SocketTimeoutException e){
                            conexaoOk = false;
                            Log.e("Errouuuuu", "SocketTimeoutException");

                        }catch (Exception e) {
                            erroCriticos = true;
                            Log.e("Errouuuuu", "Exception");
                            e.printStackTrace();
                        }
                        break;

                    case 4:
                    case 5:
                        try{
                            listaRecentesAberto = daoWeb.buscarBuracosRecentes(0);
                            listaRecentesTampados = daoWeb.buscarBuracosTampados(0);

                            totalRecentes = daoWeb.buscaTotalDeBuracosRecentes();
                            totalHoje = daoWeb.buscaTotalDeBuracosAbertoHoje();
                            totalTampados = daoWeb.buscaTotalDeBuracosTampados();

                            adapterListaRecentesAbertos();

                            inicializadoRecentesAberto = true;
                            inicializadoRecentesTampados = true;
                            conexaoOk = true;
                            erroRecentes = false;

                        }catch (ConnectException e){
                            conexaoOk = false;
                            Log.e("Errouuuuu", "ConnectException");

                        }catch (SocketTimeoutException e){
                            conexaoOk = false;
                            Log.e("Errouuuuu", "SocketTimeoutException");

                        }catch (Exception e) {
                            erroRecentes = true;
                            Log.e("Errouuuuu", "Exception");
                            e.printStackTrace();
                        }
                        break;
                }
            }else{

                ArrayList<Buraco> listaLoad = new ArrayList<Buraco>();

                switch (qualLista){

                    case 3:
                        try {
                            listaLoad = daoWeb.buscarMaisCriticos(paginaCriticos);
                        } catch (Exception e) {
                            e.printStackTrace();
                            erroCarregarMais = true;
                        }

                        for (int i = 0; i < listaLoad.size(); i++) {
                            listaCriticos.add(listaLoad.get(i));
                        }
                        break;

                    case 4:
                        try {
                            listaLoad = daoWeb.buscarBuracosRecentes(paginaRecente);
                        } catch (Exception e) {
                            e.printStackTrace();
                            erroCarregarMais = true;
                        }

                        for (int i = 0; i < listaLoad.size(); i++) {
                            listaRecentesAberto.add(listaLoad.get(i));
                        }
                        break;

                    case 5:
                        try {
                            listaLoad = daoWeb.buscarBuracosTampados(paginaRecenteTampando);
                        } catch (Exception e) {
                            e.printStackTrace();
                            erroCarregarMais = true;
                        }

                        for (int i = 0; i < listaLoad.size(); i++) {
                            listaRecentesTampados.add(listaLoad.get(i));
                        }
                        break;
                }

            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            if (!carregaMaisDaLista){
                progressBar.setVisibility(View.INVISIBLE);
                listaRecycleRegistros.setAdapter(adapter);
                atualizarTextos();
            }else{
                adapter.notifyDataSetChanged();
                adapter.setLoaded();
                swipyRefreshLayout.setRefreshing(false);
                if(erroCarregarMais){
                    Toast.makeText(getContext(), "Erro ao atualizar dados", Toast.LENGTH_SHORT).show();
                }
            }

            btnOpcTampados.setClickable(true);
            carregaMaisDaLista = false;
            erroCarregarMais = false;
        }
    }

}