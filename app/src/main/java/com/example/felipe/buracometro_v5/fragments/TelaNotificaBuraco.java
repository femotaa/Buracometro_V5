package com.example.felipe.buracometro_v5.fragments;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.felipe.buracometro_v5.R;


public class TelaNotificaBuraco extends Fragment {


    private static final String TEXTO_TOOLBAR = "Notificar Buraco";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        ImageView imgToolbar = (ImageView) getActivity().findViewById(R.id.img_icone);
        imgToolbar.setBackgroundDrawable(getResources().getDrawable(R.drawable.icone_adiciona));

        TextView textoToolbar = (TextView) getActivity().findViewById(R.id.texto_toolbar);
        textoToolbar.setText(TEXTO_TOOLBAR);

        return inflater.inflate(R.layout.fr_tela_notifica_buraco,container,false);

    }


}
