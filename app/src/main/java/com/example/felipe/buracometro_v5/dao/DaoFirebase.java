package com.example.felipe.buracometro_v5.dao;


import android.util.Log;

import com.example.felipe.buracometro_v5.listeners.OnGetFirebaseBuracosListener;
import com.example.felipe.buracometro_v5.listeners.OnGetFirebaseDados;
import com.example.felipe.buracometro_v5.listeners.OnGetFirebaseUsuarioListener;
import com.example.felipe.buracometro_v5.modelo.Buraco;
import com.example.felipe.buracometro_v5.modelo.Cidade;
import com.example.felipe.buracometro_v5.modelo.Rua;
import com.example.felipe.buracometro_v5.modelo.Usuario;
import com.example.felipe.buracometro_v5.util.Truncar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class DaoFirebase {

    DatabaseReference databaseUsuarios;

    public DaoFirebase(){

        databaseUsuarios = FirebaseDatabase.getInstance().getReference("Usuarios");
    }


    //----------------------------------------------------------------------------------------
    //                          METODOS PARA INSERCAO DE BURACOS
    //----------------------------------------------------------------------------------------

    OnGetFirebaseBuracosListener listenerBuraco;
    ArrayList<Buraco> listaBuraco;

    public void inserirBuraco(final Buraco mburaco, final Usuario mUsuario){

        final double latitude = Double.parseDouble(mburaco.getLatitude());
        final double longitude = Double.parseDouble(mburaco.getLongitude());
        final Truncar t = new Truncar();
        Log.e("lat_1", mburaco.getLatitudeLongitude());

        //Rever este metodo, pq nao esta nada legal... TODO

        //-----------------------------------------------------------------------------------//
        //-----------------------------------------------------------------------------------//
        isBuracoExiste(mburaco.getLatitudeLongitude(), new OnGetFirebaseBuracosListener() {

            @Override
            public void onStart() {
            }

            @Override
            public void onRetornoLista(ArrayList<Buraco> buracos){
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
            public void onRetornoBuraco(Buraco buracoComID) {

                if (buracoComID == null){

                    //-----------------------------------------------------------------------------------//
                    //-----------------------------------------------------------------------------------//
                    double lat = (latitude + 0.0001);
                    double log = longitude;
                    lat = t.truncate(lat, 4);
                    log = t.truncate(log, 4);
                    String latLong = String.valueOf(lat) + "_" + String.valueOf(log);
                    Log.e("lat_ultimo", latLong);

                    isBuracoExiste(latLong, new OnGetFirebaseBuracosListener() {

                        @Override
                        public void onStart() {
                        }

                        @Override
                        public void onRetornoLista(ArrayList<Buraco> buracos){
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
                        public void onRetornoBuraco(Buraco buracoComID) {

                            if (buracoComID == null){

                                //-----------------------------------------------------------------------------------//
                                //-----------------------------------------------------------------------------------//
                                double lat = (latitude + 0.0001);
                                double log = (longitude + 0.0001);
                                lat = t.truncate(lat, 4);
                                log = t.truncate(log, 4);
                                String latLong = String.valueOf(lat) + "_" + String.valueOf(log);
                                Log.e("lat_ultimo", latLong);

                                isBuracoExiste(latLong, new OnGetFirebaseBuracosListener() {

                                    @Override
                                    public void onStart() {
                                    }

                                    @Override
                                    public void onRetornoLista(ArrayList<Buraco> buracos){
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
                                    public void onRetornoBuraco(Buraco buracoComID) {

                                        if (buracoComID == null){

                                            //-----------------------------------------------------------------------------------//
                                            //-----------------------------------------------------------------------------------//
                                            double lat = (latitude - 0.0001);
                                            double log = longitude;
                                            lat = t.truncate(lat, 4);
                                            log = t.truncate(log, 4);
                                            String latLong = String.valueOf(lat) + "_" + String.valueOf(log);
                                            Log.e("lat_ultimo", latLong);

                                            isBuracoExiste(latLong, new OnGetFirebaseBuracosListener() {

                                                @Override
                                                public void onStart() {
                                                }

                                                @Override
                                                public void onRetornoLista(ArrayList<Buraco> buracos){
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
                                                public void onRetornoBuraco(Buraco buracoComID) {

                                                    if (buracoComID == null){

                                                        //-----------------------------------------------------------------------------------//
                                                        //-----------------------------------------------------------------------------------//
                                                        double lat = (latitude - 0.0001);
                                                        double log = (longitude - 0.0001);
                                                        lat = t.truncate(lat, 4);
                                                        log = t.truncate(log, 4);
                                                        String latLong = String.valueOf(lat) + "_" + String.valueOf(log);
                                                        Log.e("lat_ultimo", latLong);

                                                        isBuracoExiste(latLong, new OnGetFirebaseBuracosListener() {

                                                            @Override
                                                            public void onStart() {
                                                            }

                                                            @Override
                                                            public void onRetornoLista(ArrayList<Buraco> buracos){
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
                                                            public void onRetornoBuraco(Buraco buracoComID) {

                                                                if (buracoComID == null){

                                                                    //-----------------------------------------------------------------------------------//
                                                                    //-----------------------------------------------------------------------------------//
                                                                    double lat = latitude;
                                                                    double log = (longitude + 0.0001);
                                                                    lat = t.truncate(lat, 4);
                                                                    log = t.truncate(log, 4);
                                                                    String latLong = String.valueOf(lat) + "_" + String.valueOf(log);
                                                                    Log.e("lat_ultimo", latLong);

                                                                    isBuracoExiste(latLong, new OnGetFirebaseBuracosListener() {

                                                                        @Override
                                                                        public void onStart() {
                                                                        }

                                                                        @Override
                                                                        public void onRetornoLista(ArrayList<Buraco> buracos){
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
                                                                        public void onRetornoBuraco(Buraco buracoComID) {

                                                                            if (buracoComID == null){

                                                                                //-----------------------------------------------------------------------------------//
                                                                                //-----------------------------------------------------------------------------------//
                                                                                double lat = latitude;
                                                                                double log = (longitude - 0.0001);
                                                                                lat = t.truncate(lat, 4);
                                                                                log = t.truncate(log, 4);
                                                                                String latLong = String.valueOf(lat) + "_" + String.valueOf(log);
                                                                                Log.e("lat_ultimo", latLong);
                                                                                Log.e("lat_log", "" + lat + log);
                                                                                Log.e("longitude", "" + longitude);

                                                                                isBuracoExiste(latLong, new OnGetFirebaseBuracosListener() {

                                                                                    @Override
                                                                                    public void onStart() {
                                                                                    }

                                                                                    @Override
                                                                                    public void onRetornoLista(ArrayList<Buraco> buracos){
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
                                                                                    public void onRetornoBuraco(Buraco buracoComID) {

                                                                                        if (buracoComID == null){

                                                                                            inserirOnlyBuraco(mburaco);
                                                                                            inserirBuracoUsuario(mburaco, mUsuario);
                                                                                            atualizaCidadeQtdBuracos(true, mburaco);
                                                                                            atualizaRuaQtdBuracos(true, mburaco);
                                                                                            atualizaTotalDeBuracosAbertos(true);
                                                                                            validaSeTemBuracoIndevidoTampado(mburaco);

                                                                                        }else{

                                                                                            temBuracoParaUsuario(buracoComID, mUsuario);
                                                                                        }

                                                                                    }

                                                                                });


                                                                            }else{

                                                                                temBuracoParaUsuario(buracoComID, mUsuario);
                                                                            }

                                                                        }

                                                                    });


                                                                }else{

                                                                    temBuracoParaUsuario(buracoComID, mUsuario);
                                                                }

                                                            }

                                                        });


                                                    }else{

                                                        temBuracoParaUsuario(buracoComID, mUsuario);
                                                    }

                                                }

                                            });



                                        }else{

                                            temBuracoParaUsuario(buracoComID, mUsuario);
                                        }

                                    }

                                });


                            }else{

                                temBuracoParaUsuario(buracoComID, mUsuario);
                            }

                        }

                    });


                }else{

                    temBuracoParaUsuario(buracoComID, mUsuario);
                }

            }

        });


    }

    private void inserirOnlyBuraco(Buraco buraco){

        DatabaseReference databaseBura;

        databaseBura = FirebaseDatabase.getInstance().getReference("Buracos").child("Abertos");

        String id = databaseBura.push().getKey();
        buraco.setIdBuraco(id);

        databaseBura.child(id).setValue(buraco);

    }

    private void inserirBuracoUsuario(Buraco buraco, Usuario usuario){

        DatabaseReference databaseBura;

        databaseBura = FirebaseDatabase.getInstance().getReference("BuracosUsuario").child(usuario.getId());

        String id = buraco.getIdBuraco();

        Map<String, Object> idBuracos = new HashMap<String, Object>();
        idBuracos.put("idBuraco", id);

        databaseBura.child(id).setValue(idBuracos);

    }

    private void inserirBuracoTampado (Buraco buraco){

        DatabaseReference databaseBura;

        Long tsLong = System.currentTimeMillis()/1000;
        String timestamp = tsLong.toString();

        buraco.setStatusBuraco("Tampado");
        buraco.setDataTampado(timestamp);

        databaseBura = FirebaseDatabase.getInstance().getReference("Buracos").child("Tampados");

        String id = buraco.getIdBuraco();

        databaseBura.child(id).setValue(buraco);

    }



    //----------------------------------------------------------------------------------------
    //                         METODOS PARA VALIDAR SE BURACO EXISTE
    //----------------------------------------------------------------------------------------

    private void isBuracoExiste(String LatLong, final OnGetFirebaseBuracosListener listener){

        DatabaseReference databaseBura;

        databaseBura = FirebaseDatabase.getInstance().getReference("Buracos").child("Abertos");
        final Query query = databaseBura.orderByChild("latitudeLongitude").equalTo(LatLong);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if(dataSnapshot.exists()){

                    for (DataSnapshot messageSnapshot: dataSnapshot.getChildren()) {

                        Buraco buraco = messageSnapshot.getValue(Buraco.class);
                        listener.onRetornoBuraco(buraco);
                    }

                }else{
                    listener.onRetornoBuraco(null);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void temBuracoParaUsuario(final Buraco buraquim, final Usuario usuariim){

        DatabaseReference databaseBura;

        databaseBura = FirebaseDatabase.getInstance().getReference("BuracosUsuario").child(usuariim.getId());
        final Query query = databaseBura.orderByChild("idBuraco").equalTo(buraquim.getIdBuraco());

//        final Query query = databaseBura.orderByChild("latitudeLongitude").equalTo(buraquim.getLatitudeLongitude());

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if(!dataSnapshot.exists()){

                    atualizaQtdOcorrencia(buraquim);
                    inserirBuracoUsuario(buraquim,usuariim);

                }else{

                    Log.e("SSSS", "Buraco já existe para usuario");

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void validaSeTemBuracoIndevidoTampado(Buraco buraco){

        DatabaseReference databaseBura;

        databaseBura = FirebaseDatabase.getInstance().getReference("Buracos").child("Tampados");
        final Query query = databaseBura.orderByChild("latitudeLongitude").equalTo(buraco.getLatitudeLongitude());

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if(dataSnapshot.exists()){

                    for (DataSnapshot messageSnapshot: dataSnapshot.getChildren()) {

                        messageSnapshot.getRef().removeValue();
                        //deletarBuraco(buracoComID);
                        //atualizaStatusBuracoUsuarios(buracoComID);

                    }

                }else{
                    Log.e("BuraquimTampNaoEx", "Não existe");
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }



    //----------------------------------------------------------------------------------------
    //                        METODOS PARA ATUALIZAR DADOS DO BURACOS
    //----------------------------------------------------------------------------------------

    public void atualizarStatusParaTampado(Buraco buraco){

        final DatabaseReference databaseBura;

        databaseBura = FirebaseDatabase.getInstance().getReference("Buracos").child("Abertos");
        final Query query = databaseBura.orderByChild("latitudeLongitude").equalTo(buraco.getLatitudeLongitude());

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if(dataSnapshot.exists()){

                    for (DataSnapshot messageSnapshot: dataSnapshot.getChildren()) {

                        Buraco buraco = messageSnapshot.getValue(Buraco.class);
                        messageSnapshot.getRef().removeValue();

                        inserirBuracoTampado(buraco);
                        atualizaCidadeQtdBuracos(false, buraco);
                        atualizaRuaQtdBuracos(false, buraco);
                        atualizaTotalDeBuracosTampados(true);
                        atualizaTotalDeBuracosAbertos(false);
                    }

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void atualizaCidadeQtdBuracos(final boolean somar, Buraco buraco) {

        final Buraco bura = buraco;

        final DatabaseReference databaseCidade = FirebaseDatabase.getInstance().getReference("CidadeQtdBuracos");
        final Query query = databaseCidade.orderByChild("cidade").equalTo(buraco.getCidade());

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if(somar){

                    if (dataSnapshot.exists()){

                        for (DataSnapshot messageSnapshot: dataSnapshot.getChildren()) {

                            Cidade cidade = messageSnapshot.getValue(Cidade.class);
                            cidade.qtd = cidade.qtd + 1;

                            databaseCidade.child(cidade.cidade).setValue(cidade);
                        }

                    }else{

                        Cidade cidade = new Cidade();

                        try{
                            cidade.cidade = bura.getCidade();
                            cidade.estado = bura.getEstado();
                            cidade.qtd = 1;
                            databaseCidade.child(cidade.cidade).setValue(cidade);

                        }catch(Exception e){
                            e.printStackTrace();
                        }
                    }

                }else{

                    for (DataSnapshot messageSnapshot: dataSnapshot.getChildren()) {

                        Cidade cidade = messageSnapshot.getValue(Cidade.class);

                        if(cidade.qtd == 1){

                            messageSnapshot.getRef().removeValue();

                        }else{

                            cidade.qtd = cidade.qtd - 1;
                            databaseCidade.child(cidade.cidade).setValue(cidade);
                        }
                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void atualizaRuaQtdBuracos(final boolean somar, Buraco buraco){

        final Buraco bura = buraco;

        final DatabaseReference databaseRua = FirebaseDatabase.getInstance().getReference("RuaMaisBuracos");
        final Query query = databaseRua.orderByChild("rua").equalTo(buraco.getRua());

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if(somar){

                    if (dataSnapshot.exists()){

                        for (DataSnapshot messageSnapshot: dataSnapshot.getChildren()) {

                            Rua rua  = messageSnapshot.getValue(Rua.class);
                            rua.qtd = rua.qtd + 1;

                            databaseRua.child(rua.rua).setValue(rua);
                        }

                    }else{

                        Rua rua = new Rua();

                        try{

                            rua.rua = bura.getRua();
                            rua.cidade = bura.getCidade();
                            rua.estado = bura.getEstado();
                            rua.qtd = 1;
                            databaseRua.child(rua.rua).setValue(rua);

                        }catch(Exception e){
                            e.printStackTrace();
                        }
                    }


                }else{
                    for (DataSnapshot messageSnapshot: dataSnapshot.getChildren()) {

                        Rua rua  = messageSnapshot.getValue(Rua.class);

                        if(rua.qtd == 1){

                            messageSnapshot.getRef().removeValue();

                        }else{

                            rua.qtd = rua.qtd - 1;
                            databaseRua.child(rua.rua).setValue(rua);
                        }

                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



    }

    private void atualizaQtdOcorrencia(Buraco buraco){

        final DatabaseReference databaseBura = FirebaseDatabase.getInstance().getReference("Buracos").child("Abertos");
        final Query query = databaseBura.orderByChild("idBuraco").equalTo(buraco.getIdBuraco());

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot messageSnapshot: dataSnapshot.getChildren()) {

                    Buraco bura = messageSnapshot.getValue(Buraco.class);
                    bura.setQtdOcorrencia(bura.getQtdOcorrencia() + 1);

                    databaseBura.child(bura.getIdBuraco()).setValue(bura);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }


    //----------------------------------------------------------------------------------------
    //                           METODOS DE TOTAIS E ESTATISTICAS
    //----------------------------------------------------------------------------------------

    private void atualizaTotalDeBuracosAbertos(final boolean somar) {

        final DatabaseReference databaseTotal = FirebaseDatabase.getInstance().getReference("Totais").child("Abertos");

        databaseTotal.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()){

                    int totalAberto = dataSnapshot.getValue(Integer.class);

                    if(somar){
                        totalAberto++;
                    }else{
                        totalAberto--;
                    }

                    databaseTotal.setValue(totalAberto);

                }else{

                    databaseTotal.setValue(1);

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void atualizaTotalDeBuracosTampados(final boolean somar) {

        final DatabaseReference databaseTotal = FirebaseDatabase.getInstance().getReference("Totais").child("Tampados");

        databaseTotal.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()){

                    int totalTampado = dataSnapshot.getValue(Integer.class);

                    if(somar){
                        totalTampado++;
                    }else{
                        totalTampado--;
                    }

                    databaseTotal.setValue(totalTampado);

                }else{

                    databaseTotal.setValue(1);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public void pegarTotaldeBuracos (boolean buscarTampado, final OnGetFirebaseDados listener){

        listener.onStart();

        DatabaseReference databaseTotal;

        if (buscarTampado){
            databaseTotal = FirebaseDatabase.getInstance().getReference("Totais").child("Abertos");
        }else{
            databaseTotal = FirebaseDatabase.getInstance().getReference("Totais").child("Tampados");
        }

        databaseTotal.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()){

                    int totalAberto = dataSnapshot.getValue(Integer.class);
                    listener.onRetornoDados(totalAberto);

                }else{

                    listener.onRetornoDados(0);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public int totalDeBuracosAbertosHoje (){

        int total = 0;

        return total;
    }

    public void buscarRegistrosDaCidade (final boolean buscarComMais, final OnGetFirebaseDados listener){

        listener.onStart();

        final DatabaseReference databaseTotal = FirebaseDatabase.getInstance().getReference("Totais").child("Abertos");

        databaseTotal.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()){

                    final int totalAberto = dataSnapshot.getValue(Integer.class);

                    if (totalAberto > 0){

                        final DatabaseReference databaseCidade = FirebaseDatabase.getInstance().getReference("CidadeQtdBuracos");

                        Query query;

                        if(buscarComMais){
                            query = databaseCidade.orderByChild("qtd").limitToLast(3);
                        }else{
                            query = databaseCidade.orderByChild("qtd");
                        }

                        query.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {

                                ArrayList<Cidade> listaCidades = new ArrayList<Cidade>();

                                for (DataSnapshot messageSnapshot: dataSnapshot.getChildren()) {

                                    Cidade cidade = messageSnapshot.getValue(Cidade.class);
                                    listaCidades.add(cidade);
                                }

                                if(buscarComMais){
                                    Collections.reverse(listaCidades);
                                }
                                listener.registrosDaCidade(totalAberto,listaCidades);

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

                    }else{
                        listener.ruaComMaisRegistro(0,null);
                    }

                }else{

                    listener.ruaComMaisRegistro(0,null);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public void ruaComMaisBuracos (final OnGetFirebaseDados listener){

        listener.onStart();

        final DatabaseReference databaseRua = FirebaseDatabase.getInstance().getReference("RuaMaisBuracos");
        final Query query = databaseRua.orderByChild("qtd").limitToLast(3);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                ArrayList<Rua> listaRuas = new ArrayList<Rua>();

                for (DataSnapshot messageSnapshot: dataSnapshot.getChildren()) {

                    Rua rua  = messageSnapshot.getValue(Rua.class);
                    listaRuas.add(rua);
                }

                Collections.reverse(listaRuas);
                listener.ruaComMaisRegistro(0,listaRuas);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    public void listarBuracosMaisAntigos (final OnGetFirebaseBuracosListener listener){

        listener.onStart();

        DatabaseReference databaseBura;

        listaBuraco = new ArrayList<Buraco>();

        databaseBura = FirebaseDatabase.getInstance().getReference("Buracos").child("Abertos");
        final Query query = databaseBura.orderByChild("data_Registro").limitToFirst(3);

        query.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot messageSnapshot: dataSnapshot.getChildren()) {

                    Buraco bura = messageSnapshot.getValue(Buraco.class);
                    listaBuraco.add(bura);
                }

                //Collections.reverse(listaBuraco);
                listener.onRetornoLista(listaBuraco);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }


    //----------------------------------------------------------------------------------------
    //                          METODOS PARA LISTAGEM DE BURACOS
    //----------------------------------------------------------------------------------------


    ArrayList<String> listaDeIdsDoUsuario;
    private int i = 0;
    private int y;
    private int z;
    public void listarBuracosPorUsuario (Usuario usuario, final OnGetFirebaseBuracosListener listener){

        listaDeIdsDoUsuario = new ArrayList<String>();
        listaBuraco = new ArrayList<Buraco>();
        listener.onStart();

        DatabaseReference databaseBura;
        databaseBura = FirebaseDatabase.getInstance().getReference("BuracosUsuario").child(usuario.getId());
        final DatabaseReference databaseBuraAbertos = FirebaseDatabase.getInstance().getReference("Buracos").child("Abertos");
        final DatabaseReference databaseBuraTampados= FirebaseDatabase.getInstance().getReference("Buracos").child("Tampados");


        databaseBura.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                for (DataSnapshot messageSnapshot: dataSnapshot.getChildren()) {

                    String idBuraco = messageSnapshot.child("idBuraco").getValue().toString();
                    listaDeIdsDoUsuario.add(idBuraco);

                }


                if(!listaDeIdsDoUsuario.isEmpty()){

                    z = 0;
                    y = 0;
                    for(i = 0; i<listaDeIdsDoUsuario.size(); i++) {

                        Query query = databaseBuraAbertos.orderByChild("idBuraco").equalTo(listaDeIdsDoUsuario.get(i).toString());
                        query.addListenerForSingleValueEvent(new ValueEventListener() {

                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {

                                for (DataSnapshot messageSnapshot: dataSnapshot.getChildren()) {

                                    Buraco bura = messageSnapshot.getValue(Buraco.class);
                                    listaBuraco.add(bura);
                                }

                                Query query2 = databaseBuraTampados.orderByChild("idBuraco").equalTo(listaDeIdsDoUsuario.get(z).toString());
                                z++;

                                query2.addListenerForSingleValueEvent(new ValueEventListener() {

                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {

                                        for (DataSnapshot messageSnapshot: dataSnapshot.getChildren()) {

                                            Buraco bura = messageSnapshot.getValue(Buraco.class);
                                            listaBuraco.add(bura);
                                        }

                                        y++;

                                        if(z == listaDeIdsDoUsuario.size() && y == listaDeIdsDoUsuario.size()){
                                            listener.onRetornoLista(listaBuraco);
                                        }

                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

                    }

                }else{
                    listener.onRetornoLista(listaBuraco);
                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    ArrayList<Buraco> listaBuracoAbertos = new ArrayList<Buraco>();
    ArrayList<Buraco> listaBuracosTampados = new ArrayList<Buraco>();
    public void listarBuracosRecentes (int pagina, final OnGetFirebaseBuracosListener listener){

        final int indice = pagina * 20;
        DatabaseReference databaseBura;

        listaBuracoAbertos = new ArrayList<Buraco>();
        listaBuracosTampados = new ArrayList<Buraco>();

        listener.onStart();

        databaseBura = FirebaseDatabase.getInstance().getReference("Buracos").child("Abertos");
        final Query query = databaseBura.orderByChild("data_Registro").startAt(indice).limitToFirst(20);


        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                for (DataSnapshot messageSnapshot: dataSnapshot.getChildren()) {

                    Buraco bura = messageSnapshot.getValue(Buraco.class);
                    listaBuracoAbertos.add(bura);
                }


                DatabaseReference databaseBura2 = FirebaseDatabase.getInstance().getReference("Buracos").child("Tampados");
                final Query query2 = databaseBura2.orderByChild("dataTampado").startAt(indice).limitToFirst(20);

                query2.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {


                        for (DataSnapshot messageSnapshot: dataSnapshot.getChildren()) {

                            Buraco bura = messageSnapshot.getValue(Buraco.class);

                            listaBuracosTampados.add(bura);

                        }

                        Collections.reverse(listaBuracoAbertos);
                        Collections.reverse(listaBuracosTampados);
                        listener.onRetornoDuasLista(listaBuracoAbertos, listaBuracosTampados);

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public void listarBuracosCriticos (final OnGetFirebaseBuracosListener listener){

        listener.onStart();

        DatabaseReference databaseBura;

        listaBuraco = new ArrayList<Buraco>();

        databaseBura = FirebaseDatabase.getInstance().getReference("Buracos").child("Abertos");
        final Query query = databaseBura.orderByChild("qtdOcorrencia").limitToLast(5);

        query.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot messageSnapshot: dataSnapshot.getChildren()) {

                    Buraco bura = messageSnapshot.getValue(Buraco.class);
                    listaBuraco.add(bura);
                }

                Collections.reverse(listaBuraco);
                listener.onRetornoLista(listaBuraco);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }



    //----------------------------------------------------------------------------------------
    //                          METODOS PARA INSERCAO DE USUARIOS
    //----------------------------------------------------------------------------------------


    public void inserirUsuario (Usuario usuario){

        String id = databaseUsuarios.push().getKey();
        usuario.setId(id);

        databaseUsuarios.child(id).setValue(usuario);

    }

    Usuario usuarioRecuperado;
    OnGetFirebaseUsuarioListener listener;
    public void pegarDadosUsuario(Usuario user, OnGetFirebaseUsuarioListener listener2){

        usuarioRecuperado = new Usuario();
        listener = listener2;
        listener.onStart();

        final Query query = databaseUsuarios.orderByChild("email").equalTo(user.getEmail());

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot messageSnapshot: dataSnapshot.getChildren()) {

                    usuarioRecuperado.setEmail((String) messageSnapshot.child("email").getValue().toString());
                    usuarioRecuperado.setId((String) messageSnapshot.child("id").getValue().toString());
                    usuarioRecuperado.setNome((String) messageSnapshot.child("nome").getValue().toString());

                    Log.e("Usuario_1", "" + usuarioRecuperado.getEmail() + usuarioRecuperado.getId() + usuarioRecuperado.getNome());

                    listener.onSuccess(usuarioRecuperado);

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }


}
