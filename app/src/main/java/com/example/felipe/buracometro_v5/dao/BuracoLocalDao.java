package com.example.felipe.buracometro_v5.dao;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.felipe.buracometro_v5.modelo.Buraco;

import java.util.ArrayList;


public class BuracoLocalDao extends SQLiteOpenHelper {

    public static final String NOME_BANCO = "buracomentroSQLite";
    public static final int VERSAO = 1;

    public BuracoLocalDao(Context context){
        super(context, NOME_BANCO,null,VERSAO);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {

        String sql = "CREATE TABLE buraco (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "idBuraco INTEGER,"       +
                "rua text,"               +
                "bairro text,"            +
                "cidade text,"            +
                "estado text,"            +
                "data_registro datetime," +
                "latitude text,"          +
                "longitude text,"         +
                "identificador text,"     +
                "qtdocorrencia INTEGER,"  +
                "status text,"            +
                "dataTampado datetime)";

        db.execSQL(sql);

    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }


    //---------------------------------------------------------------------
    //            METODOS DE MANIPULAÇÃO DO BANCO DE DADOS
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
        valores.put("identificador", buraco.getIdentificador());
        valores.put("qtdocorrencia", 1);
        valores.put("status", buraco.getStatusBuraco());
        valores.put("dataTampado", buraco.getDataTampado());

        db.insert("buraco", null, valores);
        db.close();

    }

    public int validaSeBuracoExiste(Buraco bura) throws Exception
    {
        /*
            Retornos dessa função
            0 - Buraco não encontrado
            1 - Buraco encontrado
            2 - Buraco encontrado, com status "Tampado"

         */

        SQLiteDatabase db = getReadableDatabase();

        String query = "SELECT id, status FROM buraco WHERE latitude = " + bura.getLatitude() +
                " and longitude = " + bura.getLongitude() + " LIMIT 1";

        Cursor cursor = db.rawQuery(query, null);

        int qtdRetornados = cursor.getCount();

        Buraco buraco = new Buraco();
        String status = "";
        if(cursor.moveToFirst()) {
            do{

                buraco.setId(cursor.getInt(0));
                bura.setStatusBuraco(cursor.getString(1));
                status = cursor.getString(1);

            }while(cursor.moveToNext());
        }

        db.close();
        cursor.close();

        if (qtdRetornados != 0){
            //Valida se foi um buraco que já estava tampado
            if(status.equals("Tampado")){
                return 2;
            }else{
                return 1;
            }
        }

        return 0;

    }

    public void atualizarQuantidade(Buraco buraco)throws Exception{

        SQLiteDatabase db = getWritableDatabase();
        String queryAtualizarQtd = "UPDATE buraco SET qtdocorrencia = qtdocorrencia + 1 WHERE latitude = " + buraco.getLatitude() + " AND longitude = " + buraco.getLongitude();
        db.execSQL(queryAtualizarQtd);
        //db.rawQuery(queryAtualizarQtd, null);
        db.close();
    }

    public void deletarBuraco (Buraco bura)throws Exception{

        SQLiteDatabase db = getWritableDatabase();

        String where = "idBuraco = " + bura.getIdBuraco();

        db.delete("buraco", where, null);

        db.close();
    }

    public void deletarTodos() throws Exception{

        SQLiteDatabase db = getWritableDatabase();

        db.delete("buraco", null, null);

        db.close();

    }

    public void reabrirBuraco(Buraco buraco)throws Exception{

        SQLiteDatabase db = getWritableDatabase();
        String queryAtualizarQtd = "UPDATE buraco SET status = 'Aberto' WHERE latitude = " + buraco.getLatitude() + " AND longitude = " + buraco.getLongitude();

        db.execSQL(queryAtualizarQtd);
        db.close();
    }

    public void tamparBuracoLocal(Buraco buraco)throws Exception{

        SQLiteDatabase db = getWritableDatabase();
//            String queryAtualizarQtd = "UPDATE buraco SET status = 'Tampado', dataTampado = " + buraco.getDataTampado() + " WHERE latitude = " + buraco.getLatitude() + " AND longitude = " + buraco.getLongitude();
        String queryAtualizarQtd = "UPDATE buraco SET status = 'Tampado', dataTampado = " + "'" + buraco.getDataTampado() + "'" + " WHERE id = " + buraco.getId();

        db.execSQL(queryAtualizarQtd);
        db.close();
    }

    public ArrayList<Buraco> buscaBuracos() throws Exception{

        ArrayList<Buraco> listaBuraco = new ArrayList<Buraco>();

        SQLiteDatabase db = getReadableDatabase();

//        String queryBuscarTodos = "SELECT * FROM buraco WHERE status = 'Aberto' ORDER BY data_sistema DESC";
        String queryBuscarTodos = "SELECT id, idBuraco, rua, bairro, cidade, estado, strftime('%d/%m/%Y', data_registro), latitude, longitude, identificador, qtdocorrencia, status FROM buraco WHERE status = 'Aberto' ORDER BY data_registro DESC";

        Cursor cursor = db.rawQuery(queryBuscarTodos, null);

        if(cursor.moveToFirst()){
            do{
                Buraco bura = new Buraco();

                bura.setId(cursor.getInt(0));
                bura.setIdBuraco(cursor.getInt(1));
                bura.setRua(cursor.getString(2));
                bura.setBairro(cursor.getString(3));
                bura.setCidade(cursor.getString(4));
                bura.setEstado(cursor.getString(5));
                bura.setData_Registro(cursor.getString(6));
                bura.setLatitude(cursor.getString(7));
                bura.setLongitude(cursor.getString(8));
                bura.setIdentificador(cursor.getString(9));
                bura.setQtdOcorrencia(cursor.getInt(10));
                bura.setStatusBuraco(cursor.getString(11));

                listaBuraco.add(bura);

            }while(cursor.moveToNext());

        }
        cursor.close();
        db.close();
        return listaBuraco;
    }

    public ArrayList<Buraco> buscaBuracosTampados() throws Exception{

        ArrayList<Buraco> listaBuraco = new ArrayList<Buraco>();

        SQLiteDatabase db = getReadableDatabase();

//        String queryBuscarTodos = "SELECT * FROM buraco WHERE status = 'Tampado' ORDER BY data_sistema DESC";
        String queryBuscarTodos = "SELECT id, idBuraco, rua, bairro, cidade, estado, strftime('%d/%m/%Y', data_registro), latitude, longitude, identificador, qtdocorrencia, status, strftime('%d/%m/%Y', dataTampado) FROM buraco WHERE status = 'Tampado' ORDER BY dataTampado DESC";
        Cursor cursor = db.rawQuery(queryBuscarTodos, null);

        if(cursor.moveToFirst()){
            do{
                Buraco bura = new Buraco();

                bura.setId(cursor.getInt(0));
                bura.setIdBuraco(cursor.getInt(1));
                bura.setRua(cursor.getString(2));
                bura.setBairro(cursor.getString(3));
                bura.setCidade(cursor.getString(4));
                bura.setEstado(cursor.getString(5));
                bura.setData_Registro(cursor.getString(6));
                bura.setLatitude(cursor.getString(7));
                bura.setLongitude(cursor.getString(8));
                bura.setIdentificador(cursor.getString(9));
                bura.setQtdOcorrencia(cursor.getInt(10));
                bura.setStatusBuraco(cursor.getString(11));
                bura.setDataTampado(cursor.getString(12));

                listaBuraco.add(bura);

            }while(cursor.moveToNext());

        }

        cursor.close();
        db.close();
        return listaBuraco;

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
