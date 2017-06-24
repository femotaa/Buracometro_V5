package com.example.felipe.buracometro_v5.activities;


import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;

import com.example.felipe.buracometro_v5.R;

public class TelaSplash extends Activity
{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tela_splash);

        startHeavyProcessing();
    }

    private void startHeavyProcessing(){
        new LongOperation().execute("");
    }

    private class LongOperation extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            //some heavy processing resulting in a Data String
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                Thread.interrupted();
            }
            return "OK SlashScreen";
        }

        @Override
        protected void onPostExecute(String result) {
            Intent i = new Intent(TelaSplash.this, TelaPrimeiroAcesso.class);
            i.putExtra("data", result);
            startActivity(i);
            finish();
        }

        @Override
        protected void onPreExecute() {}

        @Override
        protected void onProgressUpdate(Void... values) {}
    }

}