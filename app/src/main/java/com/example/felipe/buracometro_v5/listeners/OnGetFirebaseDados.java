package com.example.felipe.buracometro_v5.listeners;

import com.example.felipe.buracometro_v5.modelo.Buraco;
import com.example.felipe.buracometro_v5.modelo.Cidade;
import com.example.felipe.buracometro_v5.modelo.Rua;
import com.google.firebase.database.DatabaseError;

import java.util.ArrayList;


public interface OnGetFirebaseDados {

    void onStart();
    void onFailed(DatabaseError databaseError);
    void onRetornoDados(int total);
    void registrosDaCidade(int total, ArrayList<Cidade> listaCidades);
    void ruaComMaisRegistro(int total, ArrayList<Rua> listaRua);

}
