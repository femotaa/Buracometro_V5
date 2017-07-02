package com.example.felipe.buracometro_v5.fragments;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.felipe.buracometro_v5.R;
import com.example.felipe.buracometro_v5.modelo.Usuario;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class TelaMenu extends Fragment{

    private static final String TEXTO_TOOLBAR = "";

    View view;
    Button btnAddBuraco;
    DatabaseReference databaseUsuarios;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fr_tela_menu, container, false);

        ImageView imgToolbar = (ImageView) getActivity().findViewById(R.id.img_icone);
        imgToolbar.setBackgroundDrawable(getResources().getDrawable(R.drawable.icone_lista));


        TextView textoToolbar = (TextView) getActivity().findViewById(R.id.texto_toolbar);
        textoToolbar.setText(TEXTO_TOOLBAR);

        btnAddBuraco = (Button) view.findViewById(R.id.btnAbertos);

        databaseUsuarios = FirebaseDatabase.getInstance().getReference("Usuarios");


        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        btnAddBuraco.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                adicionarBuraco();
            }
        });

    }


    public void adicionarBuraco(){

        Usuario usuario = new Usuario();
        usuario.setId("1");
        usuario.setUsuario("femotaa");

        databaseUsuarios.child(usuario.getId()).setValue(usuario);
        Toast.makeText(getContext(), "Usuario adicinado", Toast.LENGTH_LONG).show();
    }


}
