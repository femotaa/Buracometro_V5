package com.example.felipe.buracometro_v5.fragments;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;

import com.example.felipe.buracometro_v5.R;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


public class TelaMapa extends Fragment implements OnMapReadyCallback {

        private static final String TEXTO_TOOLBAR = "Mapas";

        private GoogleMap mMap;
        private Boolean mapReady = false;
        private GoogleApiClient mGoogleApiClient;
        TabHost tabhost;


        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {

            View view = inflater.inflate(R.layout.fr_tela_mapa, container, false);

            ImageView imgToolbar = (ImageView) getActivity().findViewById(R.id.img_icone);
            imgToolbar.setBackgroundDrawable(getResources().getDrawable(R.drawable.icone_registros));
            TextView textoToolbar = (TextView) getActivity().findViewById(R.id.texto_toolbar);
            textoToolbar.setText(TEXTO_TOOLBAR);

            tabhost = (TabHost) view.findViewById(R.id.tabhostmapa);
            tabhost.setup();
            TabHost.TabSpec ts = tabhost.newTabSpec("tag1");
            ts.setContent(R.id.tab1);
            ts.setIndicator("Registros");
            tabhost.addTab(ts);

            ts = tabhost.newTabSpec("tag2");
            ts.setContent(R.id.tab2);
            ts.setIndicator("Críticos");
            tabhost.addTab(ts);

            ts = tabhost.newTabSpec("tag3");
            ts.setContent(R.id.tab3);
            ts.setIndicator("Recentes");
            tabhost.addTab(ts);

            // Obtain the SupportMapFragment and get notified when the map is ready to be used.
            SupportMapFragment mapFragment = (SupportMapFragment) this.getChildFragmentManager().findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);//remember getMap() is deprecated!

            return view;
        }

        @Override
        public void onMapReady(GoogleMap googleMap) {
            mMap = googleMap;
            LatLng sydney = new LatLng(-33.852, 151.211);

            MarkerOptions markerOptions = new MarkerOptions().position(sydney).title("Random position");
            googleMap.addMarker(markerOptions);
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

        }

}