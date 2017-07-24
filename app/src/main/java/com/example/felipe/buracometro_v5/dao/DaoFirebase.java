package com.example.felipe.buracometro_v5.dao;


import android.util.Log;

import com.example.felipe.buracometro_v5.listeners.OnGetFirebaseBuracosListener;
import com.example.felipe.buracometro_v5.listeners.OnGetFirebaseDados;
import com.example.felipe.buracometro_v5.listeners.OnGetFirebaseUsuarioListener;
import com.example.felipe.buracometro_v5.modelo.Buraco;
import com.example.felipe.buracometro_v5.modelo.Cidade;
import com.example.felipe.buracometro_v5.modelo.Rua;
import com.example.felipe.buracometro_v5.modelo.Usuario;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DaoFirebase {

    DatabaseReference databaseUsuarios;
    DatabaseReference databaseBuracos;


    public DaoFirebase(){

        databaseUsuarios = FirebaseDatabase.getInstance().getReference("Usuarios");

    }


    //----------------------------------------------------------------------------------------
    //                         METODOS PARA INSERCAO DE BURACOS
    //----------------------------------------------------------------------------------------

    OnGetFirebaseBuracosListener listenerBuraco;
    ArrayList<Buraco> listaBuraco;

    public void inserirBuraco(final Buraco mburaco, final Usuario mUsuario){

        isBuracoExiste(mburaco, new OnGetFirebaseBuracosListener() {

            @Override
            public void onStart() {
            }

            @Override
            public void onRetornoLista(ArrayList<Buraco> buracos){
            }

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
                    atualizaCidadeQtdBuracos(mburaco);
                    atualizaRuaQtdBuracos(mburaco);

                    validaSeTemBuracoIndevidoTampado(mburaco);

                }else{

                    temBuracoParaUsuario(buracoComID, mUsuario);

                }

            }

        });

    }

    public void inserirOnlyBuraco(Buraco buraco){

        DatabaseReference databaseBura;

        databaseBura = FirebaseDatabase.getInstance().getReference("Buracos").child("Abertos");

        String id = databaseBura.push().getKey();
        buraco.setIdBuraco(id);

        databaseBura.child(id).setValue(buraco);

    }

    public void inserirBuracoUsuario(Buraco buraco, Usuario usuario){

        DatabaseReference databaseBura;

        databaseBura = FirebaseDatabase.getInstance().getReference("BuracosUsuario").child(usuario.getId());

        String id = buraco.getIdBuraco();

        Map<String, Object> idBuracos = new HashMap<String, Object>();
        idBuracos.put("idBuraco", id);

        databaseBura.child(id).setValue(idBuracos);

    }

    public void inserirBuracoTampado (Buraco buraco){

        DatabaseReference databaseBura;

        buraco.setStatusBuraco("Tampado");

        databaseBura = FirebaseDatabase.getInstance().getReference("Buracos").child("Tampados");

        String id = buraco.getIdBuraco();

        databaseBura.child(id).setValue(buraco);

    }



    //----------------------------------------------------------------------------------------
    //                       METODOS PARA VALIDAR SE BURACO EXISTE
    //----------------------------------------------------------------------------------------

    public void isBuracoExiste(Buraco buraco, OnGetFirebaseBuracosListener listener2){

        listenerBuraco = listener2;
        listenerBuraco.onStart();

        DatabaseReference databaseBura;

        databaseBura = FirebaseDatabase.getInstance().getReference("Buracos").child("Abertos");
        final Query query = databaseBura.orderByChild("latitudeLongitude").equalTo(buraco.getLatitudeLongitude());

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if(dataSnapshot.exists()){

                    for (DataSnapshot messageSnapshot: dataSnapshot.getChildren()) {

                        Buraco buraco = messageSnapshot.getValue(Buraco.class);
                        Log.e("BuraquimExiste", "ID: " + buraco.getIdBuraco());

                        listenerBuraco.onRetornoBuraco(buraco);
                    }

                }else{
                    listenerBuraco.onRetornoBuraco(null);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public void temBuracoParaUsuario(final Buraco buraquim, final Usuario usuariim){

        DatabaseReference databaseBura;

        databaseBura = FirebaseDatabase.getInstance().getReference("BuracosUsuario").child(usuariim.getId());
        final Query query = databaseBura.orderByChild("idBuraco").equalTo(buraquim.getIdBuraco());

//        final Query query = databaseBura.orderByChild("latitudeLongitude").equalTo(buraquim.getLatitudeLongitude());

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Log.e("DataSnapTem", dataSnapshot.toString());
                Log.e("USUER", usuariim.getId());

                if(!dataSnapshot.exists()){

                    atualizaQtdOcorrencia(buraquim);
                    inserirBuracoUsuario(buraquim,usuariim);

                    Log.e("NNNN", "Não tem buraco para usuario");


                }else{

                    Log.e("SSSS", "Buraco já existe para usuario");

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public void validaSeTemBuracoIndevidoTampado(Buraco buraco){

        DatabaseReference databaseBura;

        databaseBura = FirebaseDatabase.getInstance().getReference("Buracos").child("Tampados");
        final Query query = databaseBura.orderByChild("latitudeLongitude").equalTo(buraco.getLatitudeLongitude());

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if(dataSnapshot.exists()){

                    for (DataSnapshot messageSnapshot: dataSnapshot.getChildren()) {

                        messageSnapshot.getRef().removeValue();
                        Log.e("BuraquimDeletado", "DELETADOOOO");

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
    //                       METODOS PARA ATUALIZAR DADOS DO BURACOS
    //----------------------------------------------------------------------------------------

    public void atualizaQtdOcorrencia(Buraco buraco){


        final DatabaseReference databaseBura = FirebaseDatabase.getInstance().getReference("Buracos").child("Abertos");
        final Query query = databaseBura.orderByChild("idBuraco").equalTo(buraco.getIdBuraco());


        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Log.e("DataSnapchot", dataSnapshot.toString());

                for (DataSnapshot messageSnapshot: dataSnapshot.getChildren()) {

                    Buraco bura = messageSnapshot.getValue(Buraco.class);
                    bura.setQtdOcorrencia(bura.getQtdOcorrencia() + 1);

                    databaseBura.child(bura.getIdBuraco()).setValue(bura);
                    Log.e("UpdateBura", bura.getIdBuraco() + " " + bura.getQtdOcorrencia());

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public void atualizaCidadeQtdBuracos(Buraco buraco) {

        final Buraco bura = buraco;

        final DatabaseReference databaseCidade = FirebaseDatabase.getInstance().getReference("CidadeQtdBuracos");
        final Query query = databaseCidade.orderByChild("cidade").equalTo(buraco.getCidade());

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Log.e("DataSnapchot", dataSnapshot.toString());

                if (dataSnapshot.exists()){

                    for (DataSnapshot messageSnapshot: dataSnapshot.getChildren()) {

                        Cidade cidade = messageSnapshot.getValue(Cidade.class);
                        cidade.qtd = cidade.qtd + 1;

                        databaseCidade.child(cidade.cidade).setValue(cidade);
                        Log.e("UptadeCidade", cidade.cidade + " " + cidade.qtd);

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

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public void atualizaRuaQtdBuracos(Buraco buraco){

        final Buraco bura = buraco;

        final DatabaseReference databaseRua = FirebaseDatabase.getInstance().getReference("RuaMaisBuracos");
        final Query query = databaseRua.orderByChild("rua").equalTo(buraco.getRua());

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Log.e("DataSnapRUA", dataSnapshot.toString());

                if (dataSnapshot.exists()){

                    for (DataSnapshot messageSnapshot: dataSnapshot.getChildren()) {

                        Rua rua  = messageSnapshot.getValue(Rua.class);
                        rua.qtd = rua.qtd + 1;

                        databaseRua.child(rua.rua).setValue(rua);
                        Log.e("Uptaderua", rua.rua + " " + rua.qtd);

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

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



    }

    public void removeCidadeQtdBuracos(Buraco buraco) {

        final DatabaseReference databaseCidade = FirebaseDatabase.getInstance().getReference("CidadeQtdBuracos");
        final Query query = databaseCidade.orderByChild("cidade").equalTo(buraco.getCidade());

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()){

                    for (DataSnapshot messageSnapshot: dataSnapshot.getChildren()) {

                        Cidade cidade = messageSnapshot.getValue(Cidade.class);

                        if(cidade.qtd == 1){

                            messageSnapshot.getRef().removeValue();
                            Log.e("CidadeRemovida", "Removida");


                        }else{

                            cidade.qtd = cidade.qtd - 1;
                            databaseCidade.child(cidade.cidade).setValue(cidade);
                            Log.e("UptadeCidade", cidade.cidade + " " + cidade.qtd);
                        }
                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public void removeRuaQtdBuracos(Buraco buraco){

        final DatabaseReference databaseRua = FirebaseDatabase.getInstance().getReference("RuaMaisBuracos");
        final Query query = databaseRua.orderByChild("rua").equalTo(buraco.getRua());

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()){

                    for (DataSnapshot messageSnapshot: dataSnapshot.getChildren()) {

                        Rua rua  = messageSnapshot.getValue(Rua.class);

                        if(rua.qtd == 1){

                            messageSnapshot.getRef().removeValue();
                            Log.e("RuaRemovida", "Removida");

                        }else{

                            rua.qtd = rua.qtd + 1;
                            databaseRua.child(rua.rua).setValue(rua);
                            Log.e("Uptaderua", rua.rua + " " + rua.qtd);

                        }




                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



    }

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
                        removeCidadeQtdBuracos(buraco);
                        removeRuaQtdBuracos(buraco);

                        Log.e("BuraquimTampado", "Removido");

                    }

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public void atualizarStatusParaTampadoUsuarios(Buraco buraco){


        DatabaseReference databaseBura;
        databaseBura = FirebaseDatabase.getInstance().getReference("BuracosUsuario");
        String chave  = FirebaseDatabase.getInstance().getReference("BuracosUsuario").getKey();

        databaseBura = FirebaseDatabase.getInstance().getReference("BuracosUsuario").child(chave);

        final Query query = databaseBura.orderByChild("latitudeLongitude").equalTo(buraco.getLatitudeLongitude());



        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Log.e("dataSnapshot", "" + dataSnapshot);

                for (DataSnapshot messageSnapshot: dataSnapshot.getChildren()) {

                    Log.e("messageSnapshot", "" + messageSnapshot);


                    for (DataSnapshot buracoSnapshot: messageSnapshot.getChildren()) {

                        Log.e("buracoSnapshot", "" + buracoSnapshot);

                        Buraco bura = buracoSnapshot.getValue(Buraco.class);
                        Log.e("Buraquim", "" + bura.getRua());

                    }


                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    //----------------------------------------------------------------------------------------
    //                                 METODOS DE TOTAIS
    //----------------------------------------------------------------------------------------

    public void atualizaTotalDeBuracosAbertos(final boolean somar) {

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
                    Log.e("TotalAberto", ""+ totalAberto);

                }else{

                    Log.e("T", "kjkj");
                    databaseTotal.setValue(1);

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public void atualizaTotalDeBuracosTampados(final boolean somar) {

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
                    Log.e("totalTampado", ""+ totalTampado);

                }else{

                    Log.e("TT", "huhuhu");
                    databaseTotal.setValue(1);

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public void pegarTotaldeBuracosAbertos (final OnGetFirebaseDados listener){

        listener.onStart();

        final DatabaseReference databaseTotal = FirebaseDatabase.getInstance().getReference("Totais").child("Abertos");

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

    public void pegarTotaldeBuracosTampados (final OnGetFirebaseDados listener){

        listener.onStart();

        final DatabaseReference databaseTotal = FirebaseDatabase.getInstance().getReference("Totais").child("Tampados");

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


    //----------------------------------------------------------------------------------------
    //                          METODOS PARA LISTAGEM DE BURACOS
    //----------------------------------------------------------------------------------------


    public void listarBuracosPorUsuario (Usuario usuario, OnGetFirebaseBuracosListener listener2){

        listaBuraco = new ArrayList<Buraco>();
        listenerBuraco = listener2;
        listenerBuraco.onStart();

        databaseBuracos = FirebaseDatabase.getInstance().getReference("BuracosUsuario").child(usuario.getId());


        databaseBuracos.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                for (DataSnapshot messageSnapshot: dataSnapshot.getChildren()) {


                    Buraco bura = messageSnapshot.getValue(Buraco.class);
                    listaBuraco.add(bura);

                    Log.e("Buraquim", "" + bura.getRua());

                }

                listenerBuraco.onRetornoLista(listaBuraco);


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public void listarBuracosRecentesAbertos (int pagina, OnGetFirebaseBuracosListener listener2){

        int indice = pagina * 20;
        DatabaseReference databaseBura;


        listaBuraco = new ArrayList<Buraco>();
        listenerBuraco = listener2;
        listenerBuraco.onStart();

        databaseBura = FirebaseDatabase.getInstance().getReference("Buracos").child("Abertos");
        final Query query = databaseBura.orderByChild("data_Registro").startAt(indice).limitToFirst(20);//.equalTo("Aberto", "statusBuraco");


        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                for (DataSnapshot messageSnapshot: dataSnapshot.getChildren()) {

                    Buraco bura = messageSnapshot.getValue(Buraco.class);
                    listaBuraco.add(bura);

                    Log.e("BuraquimTodos", bura.getIdBuraco());

                }

                listenerBuraco.onRetornoLista(listaBuraco);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public void listarBuracosRecentesTampados (int pagina, OnGetFirebaseBuracosListener listener2){

        int indice = pagina * 20;
        DatabaseReference databaseBura;

        listaBuraco = new ArrayList<Buraco>();
        listenerBuraco = listener2;
        listenerBuraco.onStart();

        databaseBura = FirebaseDatabase.getInstance().getReference("Buracos").child("Tampados");
        final Query query = databaseBura.orderByChild("data_Registro").startAt(indice).limitToFirst(20);//.equalTo("Aberto", "statusBuraco");


        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                for (DataSnapshot messageSnapshot: dataSnapshot.getChildren()) {

                    Buraco bura = messageSnapshot.getValue(Buraco.class);
                    listaBuraco.add(bura);

                    Log.e("BuraquimTodos", bura.getIdBuraco());

                }

                listenerBuraco.onRetornoLista(listaBuraco);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public void listarBuracosCriticos (OnGetFirebaseBuracosListener listener2){

        DatabaseReference databaseBura;

        listaBuraco = new ArrayList<Buraco>();
        listenerBuraco = listener2;
        listenerBuraco.onStart();

        databaseBura = FirebaseDatabase.getInstance().getReference("Buracos").child("Abertos");
        final Query query = databaseBura.orderByChild("qtdOcorrencia").limitToLast(2);//.equalTo("Aberto", "statusBuraco");

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                for (DataSnapshot messageSnapshot: dataSnapshot.getChildren()) {

                    Buraco bura = messageSnapshot.getValue(Buraco.class);
                    listaBuraco.add(bura);

                    Log.e("BuraquimTodos", bura.getIdBuraco());

                }

                listenerBuraco.onRetornoLista(listaBuraco);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }



    //----------------------------------------------------------------------------------------
    //                             METODOS PARA ESTATISTICAS
    // ----------------------------------------------------------------------------------------

    public int totalDeBuracos (){

        int total = 0;

        return total;
    }

    public int totalDeBuracosRecentes (){

        int total = 0;

        return total;
    }

    public int totalDeBuracosAbertosHoje (){

        int total = 0;

        return total;
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


/*                  bura.setCidade((String) messageSnapshot.child("email").getValue().toString());
                    bura.setData_Registro((String) messageSnapshot.child("id").getValue().toString());
                    bura.setQtdReabertos((int) messageSnapshot.child("nome").getValue().toString());
                    bura.setBairro((String) messageSnapshot.child("id").getValue().toString());
                    bura.setLatitude((String) messageSnapshot.child("id").getValue().toString());
                    bura.setEstado((String) messageSnapshot.child("id").getValue().toString());
                    bura.setDataTampado((String) messageSnapshot.child("id").getValue().toString());
                    bura.setIdBuraco((String) messageSnapshot.child("id").getValue().toString());
                    bura.setLongitude((String) messageSnapshot.child("id").getValue().toString());
                    bura.setQtdOcorrencia((String) messageSnapshot.child("id").getValue().toString());
                    bura.setRua((String) messageSnapshot.child("id").getValue().toString());
                    bura.setStatusBuraco((String) messageSnapshot.child("id").getValue().toString());
*/

}
