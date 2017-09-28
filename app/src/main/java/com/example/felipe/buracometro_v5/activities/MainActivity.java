package com.example.felipe.buracometro_v5.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.felipe.buracometro_v5.R;
import com.example.felipe.buracometro_v5.fragments.TelaAdicionarBuraco;
import com.example.felipe.buracometro_v5.fragments.TelaConfiguracoes;
import com.example.felipe.buracometro_v5.fragments.TelaEstatisticas;
import com.example.felipe.buracometro_v5.fragments.TelaMapa;
import com.example.felipe.buracometro_v5.fragments.TelaMenu;
import com.example.felipe.buracometro_v5.fragments.TelaRegistros;
import com.example.felipe.buracometro_v5.modelo.Buraco;
import com.google.android.gms.maps.MapView;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FragmentManager fr = getSupportFragmentManager();

        fr.beginTransaction().replace(R.id.bau_de_fragments,new TelaMenu()).commit();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    MapView mv = new MapView(getApplicationContext());
                    mv.onCreate(null);
                    mv.onPause();
                    mv.onDestroy();
                }catch (Exception ignored){

                }
            }
        }).start();


    }


    //----------------------------------------------------------------------------------------
    //                     METODOS DE BOTÃO DE VOLTAR DO ANDROID
    //----------------------------------------------------------------------------------------

    @Override
    public void onBackPressed()
    {

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START))
        {
            drawer.closeDrawer(GravityCompat.START);
        }
        else
        {
            if (getFragmentManager().getBackStackEntryCount() > 0) {
                getFragmentManager().popBackStack();
            } else {
                super.onBackPressed();
            }

        }

    }



    //----------------------------------------------------------------------------------------
    //                     METODOS DE MANIPULACAO DO POPOUP MENU
    //----------------------------------------------------------------------------------------

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_settings)
        {
            mudarTelaConfiguracoes(null);
        }

        if(id == R.id.action_deslogar){

            AlertDialog alertar;
            AlertDialog.Builder mensagemInfo = new AlertDialog.Builder(this);
            mensagemInfo.setTitle("Deseja deslogar do Buracometro?");

            mensagemInfo.setPositiveButton(" Sim ", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {


                    SharedPreferences settings = getBaseContext().getSharedPreferences("preferencias", 0);
                    SharedPreferences.Editor editor = settings.edit();
                    editor.putBoolean("isLogado", false);
                    editor.apply();

                    editor.putString("login", "");
                    editor.apply();

                    Toast.makeText(getBaseContext(), "Deslogado", Toast.LENGTH_LONG).show();

                    Intent mudarDeTela = new Intent(getBaseContext(), TelaLogin.class);
                    startActivity(mudarDeTela);
                    finish();

                }
            });

            mensagemInfo.setNegativeButton(" Cancelar ", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });

            alertar = mensagemInfo.create();
            alertar.show();


        }

        if (id == R.id.action_mapear)
        {
            TelaRegistros tr = new TelaRegistros();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }



    //----------------------------------------------------------------------------------------
    //                     METODOS DE MANIPULACAO DO NAVIGATION VIEW
    //----------------------------------------------------------------------------------------

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item)
    {

        int id = item.getItemId();
        Fragment f = getSupportFragmentManager().findFragmentById(R.id.bau_de_fragments);

        if (id == R.id.inicio && !(f instanceof TelaMenu)) {

            mudarTelaInicio(null);

        } else if (id == R.id.registrados && !(f instanceof TelaRegistros)) {

            mudarTelaRegistrados(null);

        } else if (id == R.id.mapas && !(f instanceof TelaMapa)) {

            mudarTelaMapas(null);

        } else if (id == R.id.estatisticas && !(f instanceof TelaEstatisticas)) {

            mudarTelaEstatisticas(null);

        } else if (id == R.id.notifica_buraco && !(f instanceof TelaAdicionarBuraco)) {

            mudarTelaNotificaBuraco(null);

        } else if (id == R.id.configuracoes && !(f instanceof TelaConfiguracoes)) {

            mudarTelaConfiguracoes(null);

        } else if (id == R.id.ajuda) {


        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void mostraMenuDoLado(View view)
    {
        DrawerLayout mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START))
        {
            mDrawerLayout.closeDrawer(GravityCompat.START);

        } else
        {
            mDrawerLayout.openDrawer(GravityCompat.START);
        }

    }



    //----------------------------------------------------------------------------------------
    //                     METODOS DE MUDANÇA DE TELAS
    //----------------------------------------------------------------------------------------

    public void mudarTelaRegistrados (View view){

        FragmentManager fr = getSupportFragmentManager();
        TelaRegistros objetoDaTela = new TelaRegistros();
        FragmentTransaction fragmentTransaction = fr.beginTransaction();
        fragmentTransaction.replace(R.id.bau_de_fragments, objetoDaTela,"TelaRegistrados");

        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    public void mudarTelaMapas (View view){

        /**/
        FragmentManager fr = getSupportFragmentManager();
        TelaMapa objetoDaTela = new TelaMapa();
        FragmentTransaction fragmentTransaction = fr.beginTransaction();
        fragmentTransaction.replace(R.id.bau_de_fragments,objetoDaTela,"TelaMapa");

        fragmentTransaction.addToBackStack("TelaMapa");
        fragmentTransaction.commit();
        /**/

    }


    public void mudarTelaMapasComRegistros(ArrayList<Buraco> listaRegistros, ArrayList<Buraco> listaRecentes, ArrayList<Buraco> listaCriticos ){

        /** /
        FragmentManager fr = getSupportFragmentManager();
        TelaMapa objetoDaTela = new TelaMapa();
        FragmentTransaction fragmentTransaction = fr.beginTransaction();
        fragmentTransaction.replace(R.id.bau_de_fragments,objetoDaTela,"TelaMapa");

        fragmentTransaction.addToBackStack("TelaMapa");
        fragmentTransaction.commit();

        TelaMapa fragment = new TelaMapa();
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("listaRegistros", listaRegistros);
        bundle.putParcelableArrayList("listaRecentes", listaRecentes);
        bundle.putParcelableArrayList("listaCriticos", listaCriticos);

        fragment.setArguments(bundle);
        getFragmentManager().beginTransaction().replace(R.id.bau_de_fragments, fragment).commit();
        /* */
    }


    public void mudarTelaEstatisticas (View view){

        FragmentManager fr = getSupportFragmentManager();
        TelaEstatisticas objetoDaTela = new TelaEstatisticas();
        FragmentTransaction fragmentTransaction = fr.beginTransaction();
        fragmentTransaction.replace(R.id.bau_de_fragments,objetoDaTela,"TelaEstatisticas");

        fragmentTransaction.addToBackStack("TelaEstatisticas");
        fragmentTransaction.commit();
    }

    public void mudarTelaInicio (View view){

        FragmentManager fr = getSupportFragmentManager();
        TelaMenu objetoDaTela = new TelaMenu();
        FragmentTransaction fragmentTransaction = fr.beginTransaction();
        fragmentTransaction.replace(R.id.bau_de_fragments,objetoDaTela,"TelaMenu");

        fragmentTransaction.addToBackStack("TelaMenu");
        fragmentTransaction.commit();
    }

    public void mudarTelaNotificaBuraco (View view){

        FragmentManager fr = getSupportFragmentManager();
        TelaAdicionarBuraco objetoDaTela = new TelaAdicionarBuraco();
        FragmentTransaction fragmentTransaction = fr.beginTransaction();
        fragmentTransaction.replace(R.id.bau_de_fragments,objetoDaTela,"TelaAdicionarBuraco");

        fragmentTransaction.addToBackStack("TelaAdicionarBuraco");
        fragmentTransaction.commit();
    }

    public void mudarTelaConfiguracoes (View view){

        FragmentManager fr = getSupportFragmentManager();
        TelaConfiguracoes objetoDaTela = new TelaConfiguracoes();
        FragmentTransaction fragmentTransaction = fr.beginTransaction();
        fragmentTransaction.replace(R.id.bau_de_fragments,objetoDaTela,"TelaConfiguracoes");

        fragmentTransaction.addToBackStack("TelaConfiguracoes");
        fragmentTransaction.commit();
    }


}
