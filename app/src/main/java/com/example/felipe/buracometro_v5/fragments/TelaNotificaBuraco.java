package com.example.felipe.buracometro_v5.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.felipe.buracometro_v5.R;
import com.example.felipe.buracometro_v5.modelo.Buraco;
import com.example.felipe.buracometro_v5.modelo.Usuario;
import com.example.felipe.buracometro_v5.util.BuracometroUtil;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.location.places.ui.SupportPlaceAutocompleteFragment;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static com.google.android.gms.wearable.DataMap.TAG;


public class TelaNotificaBuraco extends Fragment implements OnMapReadyCallback, GoogleApiClient.OnConnectionFailedListener {

    private static final String TEXTO_TOOLBAR = "Notificar Buraco";
    private GoogleMap mMap;
    Marker markerOptions;
    LatLng latLng = new LatLng(0, 0);
    Button btnAdicionar;
    View view;
    Usuario usuarioAtual = new Usuario();

    Buraco buracoMarcado;

    private GoogleApiClient mGoogleApiClient;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.tela_notifica_buraco, container, false);

        ImageView imgToolbar = (ImageView) getActivity().findViewById(R.id.img_icone);
        imgToolbar.setBackgroundDrawable(getResources().getDrawable(R.drawable.icone_adicionar));

        TextView textoToolbar = (TextView) getActivity().findViewById(R.id.texto_toolbar);
        textoToolbar.setText(TEXTO_TOOLBAR);

        btnAdicionar = (Button) view.findViewById(R.id.btnInserir);
        btnAdicionar.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                alertarParaInserirBuraco();
            }
        });

        SupportMapFragment mapFragment = (SupportMapFragment) this.getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        SharedPreferences settings = getActivity().getSharedPreferences("preferencias", 0);
        String emailAtual = settings.getString("login","");
        String idUsuarioAtual = settings.getString("IdLogin","");
        String nomeAtual = settings.getString("nome","");


        usuarioAtual.setEmail(emailAtual);
        usuarioAtual.setId(idUsuarioAtual);
        usuarioAtual.setNome(nomeAtual);

        SupportPlaceAutocompleteFragment autocompleteFragment = (SupportPlaceAutocompleteFragment) getChildFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);

        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {

                Log.e("getName",  "" + place.getName());
                Log.e("getAddress",  "" + place.getAddress());
                Log.e("getLocale",  "" + place.getLocale());

                try{
                    buscarEndereco(place.getLatLng().latitude,place.getLatLng().longitude);
                    mapearBuracosSelecionado(place.getLatLng().latitude,place.getLatLng().longitude);

                }catch (Exception e)
                {
                    e.printStackTrace();
                }

            }

            @Override
            public void onError(Status status) {
                // TODO: Solucionar o erro.
                Log.e(TAG, "Ocorreu um erro: " + status);
            }
        });

        return view;
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng osasco = new LatLng(-23.542358, -46.773769);

        MarkerOptions options = new MarkerOptions().position(osasco);
        markerOptions = mMap.addMarker(options);


        //markerOptions = new MarkerOptions().position(sydney).title("Random position");
        //mMap.addMarker(markerOptions.draggable(true));
        markerOptions.setPosition(osasco);
        markerOptions.setDraggable(true);

        double l = markerOptions.getPosition().latitude;
        double l2 = markerOptions.getPosition().longitude;

        Log.e("LatLong", l + " " + l2);

        mMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {

            @Override
            public void onMarkerDragStart(Marker arg0) {
                //Log.d("System out", "onMarkerDragStart..."+arg0.getPosition().latitude+"..."+arg0.getPosition().longitude);
            }

            @SuppressWarnings("unchecked")
            @Override
            public void onMarkerDragEnd(Marker arg0) {
                mMap.animateCamera(CameraUpdateFactory.newLatLng(arg0.getPosition()));
                latLng = new LatLng(arg0.getPosition().latitude, arg0.getPosition().longitude);
                markerOptions.setPosition(latLng);
                try{
                    buscarEndereco(arg0.getPosition().latitude, arg0.getPosition().longitude);
                }catch (Exception e){
                    e.printStackTrace();
                    Toast.makeText(getContext(), "Marcador não esta em uma Rua ou Avenida", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onMarkerDrag(Marker arg0) {
            }

        });

        CameraUpdate local = CameraUpdateFactory.newLatLngZoom(osasco, 14);
        mMap.animateCamera(local);

    }


    private void buscarEndereco(double lat, double lon) throws IOException {

        Geocoder mGeocoder = new Geocoder(getContext(), Locale.getDefault());

        List<Address> addresses = mGeocoder.getFromLocation(lat, lon, 1);

        if (addresses != null && addresses.size() > 0) {

            /* * /
            Log.e("getLocality", addresses.get(0).getLocality()); //Cidade
            Log.e("getAdminArea", addresses.get(0).getAdminArea()); //Estado
            Log.e("getPostalCode", addresses.get(0).getPostalCode()); //CEP
            Log.e("getSubLocality", addresses.get(0).getSubLocality()); //Bairro
            Log.e("getAddressLine", addresses.get(0).getAddressLine(0)); //Endereço completo
            Log.e("getFeatureName", addresses.get(0).getFeatureName()); //??
            Log.e("getSubAdminArea", addresses.get(0).getSubAdminArea()); //Cidade tbm
            Log.e("getThoroughfare", addresses.get(0).getThoroughfare()); // Rua tbm
            Log.e("getSubThoroughfare", addresses.get(0).getSubThoroughfare()); //??
            Log.e("getPhone", "" + addresses.get(0).getPhone()); //??
            Log.e("getPremises", "" + addresses.get(0).getPremises()); //??
            /* */

            buracoMarcado = new Buraco();

            Long tsLong = System.currentTimeMillis()/1000;
            String ts = tsLong.toString();

            buracoMarcado.setData_Registro(ts);
            buracoMarcado.setQtdOcorrencia(1);
            buracoMarcado.setStatusBuraco("Aberto");
            buracoMarcado.setRua(addresses.get(0).getThoroughfare());
            buracoMarcado.setBairro(addresses.get(0).getSubLocality());
            buracoMarcado.setCidade(addresses.get(0).getLocality());
            buracoMarcado.setLatitude(String.valueOf(lat));
            buracoMarcado.setLongitude(String.valueOf(lon));
            cep = addresses.get(0).getPostalCode();
            Map<String, String> estadoAbreviado = new HashMap<String, String>();
            estadoAbreviado.put("São Paulo","SP");
            buracoMarcado.setEstado(estadoAbreviado.get(addresses.get(0).getAdminArea()));
        }

    }


    public void mapearBuracosSelecionado(double lat, double lng)
    {

        LatLng latLng = new LatLng(lat, lng);

        try {

            //markerOptions = new MarkerOptions().position(latLng);
            markerOptions.setPosition(latLng);
            CameraUpdate local = CameraUpdateFactory.newLatLngZoom(latLng, 15);
            mMap.animateCamera(local);

        }
        catch (Exception e)
        {
            e.printStackTrace();
            Toast.makeText(view.getContext(), "Erro ao mapear", Toast.LENGTH_SHORT).show();
        }

    }


    String cep;
    public void alertarParaInserirBuraco (){

        try{
            AlertDialog alertar;
            AlertDialog.Builder mensagemInfo = new AlertDialog.Builder(getContext());
            mensagemInfo.setTitle("Confirmar buraco neste endereço?");

            mensagemInfo.setMessage(buracoMarcado.getRua() + "\n\nBairro: " + buracoMarcado.getBairro()+ "\nCidade: " + buracoMarcado.getCidade() + " - " + buracoMarcado.getEstado() + "\nCEP: " + cep);

            mensagemInfo.setPositiveButton(" Sim ", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    inserirBuraco();
                }
            });

            mensagemInfo.setNegativeButton(" Cancelar ", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });

            alertar = mensagemInfo.create();
            alertar.show();

        }catch (Exception e){
            e.printStackTrace();
            if(buracoMarcado == null){
                Toast.makeText(getContext(), "Endereço não selecionado", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(getContext(), "Ocorreu um erro, tente mais tarde", Toast.LENGTH_SHORT).show();
            }
        }

    }

    public void inserirBuraco (){

        BuracometroUtil buracometroUtil = new BuracometroUtil(getContext(), usuarioAtual);
        try{
            int jaPosssuiBuraco = buracometroUtil.inserirBuraco(buracoMarcado);

            if(jaPosssuiBuraco == 1){
                Toast.makeText(getContext(), "Você já cadastrou esse buraco", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(getContext(), "Buraco cadastrado!", Toast.LENGTH_SHORT).show();
            }

        }catch (Exception e){
            e.printStackTrace();
            Toast.makeText(getContext(), "Erro ao inserir buraco", Toast.LENGTH_SHORT).show();
        }


    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }


}
