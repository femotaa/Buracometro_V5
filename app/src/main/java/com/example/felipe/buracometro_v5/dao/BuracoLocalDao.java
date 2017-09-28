package com.example.felipe.buracometro_v5.dao;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;

import com.example.felipe.buracometro_v5.listeners.OnGetFirebaseBuracosListener;
import com.example.felipe.buracometro_v5.modelo.Buraco;
import com.example.felipe.buracometro_v5.modelo.Usuario;
import com.example.felipe.buracometro_v5.util.Truncar;

import java.util.ArrayList;


public class BuracoLocalDao extends SQLiteOpenHelper {

    private static final String NOME_BANCO = "buracomentroSQLite";
    private static final int VERSAO = 1;

    private String usuario;

    public BuracoLocalDao(Context context, String usuario){
        super(context, NOME_BANCO,null,VERSAO);
        this.usuario = usuario;
    }


    @Override
    public void onCreate(SQLiteDatabase db) {

        String sql = "CREATE TABLE buraco (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "idBuraco text,"           +
                "rua text,"                +
                "bairro text,"             +
                "cidade text,"             +
                "estado text,"             +
                "data_registro text,"      +
                "latitude text,"           +
                "longitude text,"          +
                "qtdocorrencia INTEGER,"   +
                "status text,"             +
                "dataTampado text,"        +
                "usuario text)";

        db.execSQL(sql);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }


    //---------------------------------------------------------------------
    //                      METODOS DE PARA INSERIR
    //---------------------------------------------------------------------

    public void inserirBuraco (Buraco buraco) throws Exception
    {
            SQLiteDatabase db = getWritableDatabase();
            ContentValues valores = new ContentValues();

            valores.put("idBuraco", buraco.getIdBuraco());
            valores.put("rua", buraco.getRua());
            valores.put("bairro", buraco.getBairro());
            valores.put("cidade", buraco.getCidade());
            valores.put("estado", buraco.getEstado());
            valores.put("data_registro", buraco.getData_Registro());
            valores.put("latitude", buraco.getLatitude());
            valores.put("longitude", buraco.getLongitude());
            valores.put("qtdocorrencia", 1);
            valores.put("status", buraco.getStatusBuraco());
            valores.put("dataTampado", buraco.getDataTampado());
            valores.put("usuario", usuario);

            db.insert("buraco", null, valores);
            db.close();
    }

    public boolean validaSeBuracoExiste(Buraco bura) throws Exception
    {

        boolean resultado;

        resultado = procuraBuraco(bura.getLatitude(), bura.getLongitude());

        if (!resultado){
            return  false;
        }

        double latitude,longitude;
        latitude = Double.parseDouble(bura.getLatitude());
        longitude = Double.parseDouble(bura.getLongitude());

        double lat;
        double log;
        final Truncar t = new Truncar();

        lat = (latitude + 0.0001);
        log = longitude;
        lat = t.truncate(lat, 4);
        log = t.truncate(log, 4);
        Log.e("lat_log", "" + lat + log);
        resultado = procuraBuraco(String.valueOf(lat), String.valueOf(log));
        if (!resultado){
            return  false;
        }

        lat = (latitude + 0.0001);
        log = (longitude + 0.0001);
        lat = t.truncate(lat, 4);
        log = t.truncate(log, 4);
        Log.e("lat_log", "" + lat + log);
        resultado = procuraBuraco(String.valueOf(lat), String.valueOf(log));
        if (!resultado){
            return  false;
        }

        lat = (latitude - 0.0001);
        log = longitude;
        lat = t.truncate(lat, 4);
        log = t.truncate(log, 4);
        Log.e("lat_log", "" + lat + log);
        resultado = procuraBuraco(String.valueOf(lat), String.valueOf(log));
        if (!resultado){
            return  false;
        }

        lat = (latitude - 0.0001);
        log = (longitude - 0.0001);
        lat = t.truncate(lat, 4);
        log = t.truncate(log, 4);
        Log.e("lat_log", "" + lat + log);
        resultado = procuraBuraco(String.valueOf(lat), String.valueOf(log));
        if (!resultado){
            return  false;
        }

        lat = latitude;
        log = (longitude + 0.0001);
        lat = t.truncate(lat, 4);
        log = t.truncate(log, 4);
        Log.e("lat_log", "" + lat + log);
        resultado = procuraBuraco(String.valueOf(lat), String.valueOf(log));
        if (!resultado){
            return  false;
        }

        lat = latitude;
        log = (longitude - 0.0001);
        lat = t.truncate(lat, 4);
        log = t.truncate(log, 4);
        Log.e("lat_log", "" + lat + log);
        resultado = procuraBuraco(String.valueOf(lat), String.valueOf(log));
        if (!resultado){
            return  false;
        }

        return true;
    }

    public boolean procuraBuraco(String latitude, String longitude) throws Exception
    {

        SQLiteDatabase db = getReadableDatabase();

        String query = "SELECT id FROM buraco WHERE latitude = '" + latitude +
                "' and longitude = '" + longitude + "' AND status = 'Aberto' LIMIT 1";

        Cursor cursor = db.rawQuery(query, null);

        int qtdRetornados = cursor.getCount();

        db.close();
        cursor.close();

        if (qtdRetornados > 0){
            return false;
        }else{
            return true;
        }

    }


    //---------------------------------------------------------------------
    //                        METODOS DE BUSCA
    //---------------------------------------------------------------------

    private ArrayList<Buraco> listaBuraco = new ArrayList<Buraco>();
    private ArrayList<Buraco> listaBuracoTampados = new ArrayList<Buraco>();
    Handler handler = new Handler();

    public void buscarBuracos (final OnGetFirebaseBuracosListener listener) throws Exception{

        listener.onStart();

        new Thread(new Runnable() {
            public void run() {

                listaBuraco = new ArrayList<Buraco>();
                listaBuracoTampados = new ArrayList<Buraco>();

                SQLiteDatabase db = getReadableDatabase();

                String queryBuscarTodos = "SELECT id, rua, bairro, cidade, estado, data_registro, latitude, longitude, qtdocorrencia, status FROM buraco WHERE status = 'Aberto' AND usuario = '" + usuario + "' ORDER BY data_registro DESC";

                Cursor cursor = db.rawQuery(queryBuscarTodos, null);

                if(cursor.moveToFirst()){
                    do{
                        Buraco bura = new Buraco();

                        bura.setIdBuraco(cursor.getString(0));
                        bura.setRua(cursor.getString(1));
                        bura.setBairro(cursor.getString(2));
                        bura.setCidade(cursor.getString(3));
                        bura.setEstado(cursor.getString(4));
                        bura.setData_Registro(cursor.getString(5));
                        bura.setLatitude(cursor.getString(6));
                        bura.setLongitude(cursor.getString(7));
                        bura.setQtdOcorrencia(cursor.getInt(8));
                        bura.setStatusBuraco(cursor.getString(9));

                        listaBuraco.add(bura);

                    }while(cursor.moveToNext());

                }
                cursor.close();
                db.close();


                SQLiteDatabase db2 = getReadableDatabase();

                String queryBuscarTodosTampados = "SELECT id, rua, bairro, cidade, estado, data_registro, latitude, longitude, qtdocorrencia, status, dataTampado FROM buraco WHERE status = 'Tampado' AND usuario = '" + usuario + "' ORDER BY dataTampado DESC";
                Cursor cursor2 = db2.rawQuery(queryBuscarTodosTampados, null);

                if(cursor2.moveToFirst()){
                    do{
                        Buraco bura = new Buraco();

                        bura.setIdBuraco(cursor2.getString(0));
                        bura.setRua(cursor2.getString(1));
                        bura.setBairro(cursor2.getString(2));
                        bura.setCidade(cursor2.getString(3));
                        bura.setEstado(cursor2.getString(4));
                        bura.setData_Registro(cursor2.getString(5));
                        bura.setLatitude(cursor2.getString(6));
                        bura.setLongitude(cursor2.getString(7));
                        bura.setQtdOcorrencia(cursor2.getInt(8));
                        bura.setStatusBuraco(cursor2.getString(9));
                        bura.setDataTampado(cursor2.getString(10));

                        listaBuracoTampados.add(bura);

                    }while(cursor2.moveToNext());

                }

                cursor2.close();
                db2.close();

                handler.post(new Runnable() {
                    public void run() {
                        listener.onRetornoDuasLista(listaBuraco,listaBuracoTampados);
                    }
                });

            }
        }).start();

    }



    //---------------------------------------------------------------------

    public void reabrirBuraco(Buraco buraco)throws Exception{

        SQLiteDatabase db = getWritableDatabase();
        String queryAtualizarQtd = "UPDATE buraco SET status = 'Aberto' WHERE latitude = " + buraco.getLatitude() + " AND longitude = " + buraco.getLongitude();

        db.execSQL(queryAtualizarQtd);
        db.close();
    }

    public void atualizarQuantidade(Buraco buraco)throws Exception{

        SQLiteDatabase db = getWritableDatabase();
        String queryAtualizarQtd = "UPDATE buraco SET qtdocorrencia = qtdocorrencia + 1 WHERE latitude = " + buraco.getLatitude() + " AND longitude = " + buraco.getLongitude();
        db.execSQL(queryAtualizarQtd);
        //db.rawQuery(queryAtualizarQtd, null);
        db.close();
    }

    //---------------------------------------------------------------------



    //---------------------------------------------------------------------
    //                        METODOS DE EXCLUSÃO
    //---------------------------------------------------------------------
    //                      METODOS DE ATUALIZAÇÃO
    public void tamparBuracoLocal(Buraco buraco)throws Exception{

        Long tsLong = System.currentTimeMillis()/1000;
        String timestamp = tsLong.toString();

        SQLiteDatabase db = getWritableDatabase();
        String queryTampar = "UPDATE buraco SET status = 'Tampado', dataTampado = " + "'" + timestamp + "'" + " WHERE id = '" + buraco.getIdBuraco() + "'";

        db.execSQL(queryTampar);
        db.close();
    }

    public void deletarTodosDoUsuario ()throws Exception{

        SQLiteDatabase db = getWritableDatabase();

        String where = "usuario = '" + usuario + "'";

        db.delete("buraco", where, null);

        db.close();
    }

    public void deletarBuraco (Buraco bura)throws Exception{

        SQLiteDatabase db = getWritableDatabase();

        String where = "id = '" + bura.getIdBuraco() + "' AND usuario = '" + usuario + "'";

        db.delete("buraco", where, null);

        db.close();
    }

    public void deletarTodos() throws Exception{

        SQLiteDatabase db = getWritableDatabase();

        db.delete("buraco", null, null);

        db.close();

    }


    public void ajustarBanco(){
        SQLiteDatabase db = getWritableDatabase();
        String queryAtualizarQtd = "UPDATE buraco SET qtdocorrencia = 1";
        String queryAtualizarStatus = "UPDATE buraco SET status = 'Aberto'";

        //db.rawQuery(queryAtualizarQtd, null);
        db.execSQL(queryAtualizarStatus);

        db.close();
    }

}
