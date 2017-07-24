package com.example.felipe.buracometro_v5.listeners;

import com.example.felipe.buracometro_v5.modelo.Usuario;
import com.google.firebase.database.DatabaseError;


public interface OnGetFirebaseUsuarioListener {

    void onStart();
    void onSuccess(Usuario user);
    void onFailed(DatabaseError databaseError);




}
