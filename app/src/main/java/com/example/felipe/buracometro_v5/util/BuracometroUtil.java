package com.example.felipe.buracometro_v5.util;


import android.content.Context;

import com.example.felipe.buracometro_v5.dao.BuracoLocalDao;
import com.example.felipe.buracometro_v5.dao.DaoFirebase;
import com.example.felipe.buracometro_v5.modelo.Buraco;
import com.example.felipe.buracometro_v5.modelo.Usuario;

public class BuracometroUtil {

    Usuario usuarioAtual;
    Context context;
    BuracoLocalDao daoLocal;
    DaoFirebase daoFirebase;

    public BuracometroUtil(Context context, Usuario usuarioAtual){
        this.context = context;
        this.usuarioAtual = usuarioAtual;

        daoLocal = new BuracoLocalDao(context, usuarioAtual.getEmail());
        daoFirebase = new DaoFirebase();

    }


    public int inserirBuraco (Buraco buraco) throws Exception{

        if (daoLocal.validaSeBuracoExiste(buraco)){

            daoLocal.inserirBuraco(buraco);
            daoFirebase.inserirBuraco(buraco, usuarioAtual);
            return 0;
        }else{

            return 1; //Buraco ja existe localmente
        }


    }



}
