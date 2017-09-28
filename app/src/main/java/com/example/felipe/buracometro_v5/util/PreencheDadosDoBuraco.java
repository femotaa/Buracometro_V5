package com.example.felipe.buracometro_v5.util;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.util.Log;

import com.example.felipe.buracometro_v5.modelo.Buraco;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;


public class PreencheDadosDoBuraco {

    protected Context context;

    //----------------------------------------------------------------------------------------
    //                                     CONSTRTURES
    //----------------------------------------------------------------------------------------

    public PreencheDadosDoBuraco(Context context)
    {
        this.context = context;
    }



    //----------------------------------------------------------------------------------------
    //                                 METODOS DE LOCALIZACAO
    //----------------------------------------------------------------------------------------

    public Buraco preencherDados() throws Exception
    {
        AchaLatitudeLongitude gps = new AchaLatitudeLongitude(context);
        Buraco buracoMarcado = new Buraco();

        double latitude  = 0;
        double longitude = 0;

        // Acha a Longitude e Latitude
        if (gps.canGetLocation())
        {
            latitude  = gps.getLatitude();
            longitude = gps.getLongitude();
        }

        Log.e("Coordenadas_Orig", "latitude: " + latitude + " longitude: " + longitude);

        Truncar t = new Truncar();
        latitude = t.truncate(latitude, 4);
        longitude = t.truncate(longitude, 4);

        Log.e("Coordenadas_Redu", "latitude: " + latitude + " longitude: " + longitude);

        Geocoder mGeocoder = new Geocoder(context, Locale.getDefault());
        List<Address> addresses = mGeocoder.getFromLocation(latitude, longitude, 1);

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

            Long tsLong = System.currentTimeMillis()/1000;
            String ts = tsLong.toString();

            buracoMarcado.setData_Registro(ts);
            buracoMarcado.setQtdOcorrencia(1);
            buracoMarcado.setStatusBuraco("Aberto");
            buracoMarcado.setRua(addresses.get(0).getThoroughfare());
            buracoMarcado.setBairro(addresses.get(0).getSubLocality());
            buracoMarcado.setCidade(addresses.get(0).getLocality());
            buracoMarcado.setLatitude(String.valueOf(latitude));
            buracoMarcado.setLongitude(String.valueOf(longitude));
            Map<String, String> estadoAbreviado = new HashMap<String, String>();
            estadoAbreviado.put("São Paulo","SP");
            buracoMarcado.setEstado(estadoAbreviado.get(addresses.get(0).getAdminArea()));
        }

        return buracoMarcado;
    }

}

