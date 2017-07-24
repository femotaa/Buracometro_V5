package com.example.felipe.buracometro_v5.listeners;

import com.example.felipe.buracometro_v5.modelo.Buraco;
import com.google.firebase.database.DatabaseError;

import java.util.ArrayList;

/**
 * Created by felipemota on 13/07/17.
 */

public interface OnGetFirebaseBuracosListener {

    void onStart();
    void onRetornoLista(ArrayList<Buraco> buracos);
    void onFailed(DatabaseError databaseError);
    void onRetornoExiste(Boolean existe);
    void onRetornoBuraco(Buraco buraco);


}
