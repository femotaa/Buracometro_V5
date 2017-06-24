package com.example.felipe.buracometro_v5.util;

import android.content.Context;

import com.example.felipe.buracometro_v5.dao.BuracoLocalDao;
import com.example.felipe.buracometro_v5.dao.BuracoWebDao;
import com.example.felipe.buracometro_v5.modelo.Buraco;

import java.util.ArrayList;

public class FuncoesDoSistema {
    protected Context context;
    BuracoWebDao daoWeb = new BuracoWebDao();
    BuracoLocalDao daoLocal;

    public FuncoesDoSistema(Context context){
        this.context = context;
        daoLocal = new BuracoLocalDao(context);
    }

    public int sincronizaComBDExterno(String identificador){

        ArrayList<Buraco> lista = new ArrayList<Buraco>();
        ArrayList<Buraco> listaBakcup = new ArrayList<Buraco>();

        try
        {
            lista = daoWeb.buscarBuracosPorIdentificador(identificador);

            if (lista.isEmpty()){
                return 2;
            }

            excluirTodosBuracosLocais();
            for(int i = 0; i<lista.size(); i++) {

                daoLocal.inserirBuraco(lista.get(i));

            }
            return 0;

        }catch (Exception e){
            return 1;
        }
    }

    public void excluirTodosBuracosLocais(){
        try{
            daoLocal.deletarTodos();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
