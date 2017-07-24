package com.example.felipe.buracometro_v5.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.IntentCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.felipe.buracometro_v5.R;
import com.example.felipe.buracometro_v5.dao.DaoFirebase;
import com.example.felipe.buracometro_v5.listeners.OnGetFirebaseUsuarioListener;
import com.example.felipe.buracometro_v5.modelo.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.database.DatabaseError;


public class TelaCadastro extends Activity{


    EditText login;
    EditText senha;
    EditText nome;
    TextView textoMsgInvalida;
    Button btnEntrar;

    private FirebaseAuth firebaseAuth;
    private ProgressBar mProgress;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tela_cadastro);

        login = (EditText) findViewById(R.id.inputLogin);
        senha = (EditText) findViewById(R.id.inputSenha);
        nome = (EditText) findViewById(R.id.inputNome);
        btnEntrar = (Button) findViewById(R.id.btnEntrar);
        textoMsgInvalida = (TextView) findViewById(R.id.txtMsgInvalida);
        mProgress = (ProgressBar) findViewById(R.id.progressCadastro);

        firebaseAuth = FirebaseAuth.getInstance();
    }


    @Override
    protected void onResume() {
        super.onResume();

        btnEntrar.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                cadastrarUsuario();
            }
        });

    }

    private void cadastrarUsuario(){

        final String email = login.getText().toString().trim();
        String password  = senha.getText().toString().trim();
        final String mNome = nome.getText().toString().trim();

        //checking if email and passwords are empty
        if(TextUtils.isEmpty(mNome)){
            Toast.makeText(this,"Por favor, insira o seu Nome",Toast.LENGTH_LONG).show();
            return;
        }

        if(TextUtils.isEmpty(email)){
            Toast.makeText(this,"Por favor, insira o Email",Toast.LENGTH_LONG).show();
            return;
        }

        if(TextUtils.isEmpty(password)){
            Toast.makeText(this,"Por favor, insira a Senha",Toast.LENGTH_LONG).show();
            return;
        }

        String processando = "Aguarde um momento...";
        textoMsgInvalida.setText(processando);
        textoMsgInvalida.setVisibility(View.VISIBLE);

        mProgress.setVisibility(View.VISIBLE);



        //creating a new user
        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                //checking if success
                Log.e("TASK_FIREBASE", "Verification : signIn With Email:onComplete:" + task.isSuccessful());

                String msgDoCadastro;

                // If sign in fails, display a message to the user.
                if (!task.isSuccessful()) {

                    try {
                        throw task.getException();
                    } catch (FirebaseAuthWeakPasswordException e) {
                        Log.e("fIREBASElOG", "Erro senha invalida");
                        msgDoCadastro = "Senha invalida:\n" +
                                "A senha dever ter pelo menos 6 caracteres.";

                    } catch (FirebaseAuthInvalidUserException e) {
                        Log.e("fIREBASElOG", "Erro Email");
                        msgDoCadastro = "Email inválido.";

                    } catch (FirebaseAuthInvalidCredentialsException e) {
                        Log.e("fIREBASElOG", "Erro Senha");
                        msgDoCadastro = "Email inválido.";

                    } catch (FirebaseNetworkException e) {
                        Log.e("fIREBASElOG", "Erro networking");
                        msgDoCadastro = "Erro de conexão.";

                    } catch (FirebaseAuthUserCollisionException e) {
                        Log.e("fIREBASElOG", "Erro networking");
                        msgDoCadastro = "Email já cadastrado.";

                    } catch (Exception e) {

                        Log.e("fIREBASElOG", e.getMessage());
                        msgDoCadastro = "Ocorreu um erro ao realizar o cadastro\n" +
                                "Tente mais tarde...";

                    }

                    textoMsgInvalida.setText(msgDoCadastro);
                    textoMsgInvalida.setTextColor(Color.parseColor("#d75757")); //Vermelho

                    mProgress.setVisibility(View.INVISIBLE);

                }else{

                    DaoFirebase dao = new DaoFirebase();

                    Usuario user = new Usuario();
                    user.setEmail(email);
                    user.setNome(mNome);

                    dao.inserirUsuario(user);

                    new DaoFirebase().pegarDadosUsuario(user, new OnGetFirebaseUsuarioListener() {

                        @Override
                        public void onStart() {
                            //DO SOME THING WHEN START GET DATA HERE
                        }

                        @Override
                        public void onSuccess(Usuario usuarioRecuperado) {

                            SharedPreferences settings = getBaseContext().getSharedPreferences("preferencias", 0);
                            SharedPreferences.Editor editor = settings.edit();
                            editor.putBoolean("isLogado", true);
                            editor.apply();

                            editor.putString("login", usuarioRecuperado.getEmail());
                            editor.apply();

                            editor.putString("IdLogin", usuarioRecuperado.getId());
                            editor.apply();

                            editor.putString("nome", usuarioRecuperado.getNome());
                            editor.apply();


                            String cadastrado = "Cadastrado!";
                            textoMsgInvalida.setTextColor(Color.parseColor("#44d773")); //Verde
                            textoMsgInvalida.setText(cadastrado);

                            Intent mudarDeTela = new Intent(getBaseContext(), MainActivity.class);
                            mudarDeTela.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | IntentCompat.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(mudarDeTela);

                            mProgress.setVisibility(View.INVISIBLE);
                            Toast.makeText(getBaseContext(), "Usuario cadastrado! Seja bem vindo.", Toast.LENGTH_LONG).show();

                            //DO SOME THING WHEN GET DATA SUCCESS HERE

                        }

                        @Override
                        public void onFailed(DatabaseError databaseError) {
                            //DO SOME THING WHEN GET DATA FAILED HERE
                        }
                    });


                }

            }
        });

    }



}
