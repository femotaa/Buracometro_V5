package com.example.felipe.buracometro_v5.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
import com.google.firebase.database.DatabaseError;

import static com.google.android.gms.wearable.DataMap.TAG;


public class TelaLogin extends Activity
{

    EditText login;
    EditText senha;
    Button btnEntrar;
    TextView irParaCadastro;
    TextView textoMsgInvalida;

    private FirebaseAuth firebaseAuth;

    private ProgressBar mProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tela_login);

        login = (EditText) findViewById(R.id.inputLogin);
        senha = (EditText) findViewById(R.id.inputSenha);
        btnEntrar = (Button) findViewById(R.id.btnEntrar);
        mProgress = (ProgressBar) findViewById(R.id.progressLogar);
        irParaCadastro = (TextView) findViewById(R.id.txtCadastrar);
        textoMsgInvalida = (TextView) findViewById(R.id.txtMsgInvalida);

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
                logarUsuario();
            }
        });


        irParaCadastro.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent mudarDeTela = new Intent(getBaseContext(), TelaCadastro.class);
                startActivity(mudarDeTela);

            }
        });

    }

    private void logarUsuario(){

        textoMsgInvalida.setText("");
        final String email = login.getText().toString().trim();
        String password  = senha.getText().toString().trim();

        //checking if email and passwords are empty
        if(TextUtils.isEmpty(email)){
            Toast.makeText(this,"Por favor, insira o email",Toast.LENGTH_LONG).show();
            return;
        }

        if(TextUtils.isEmpty(password)){
            Toast.makeText(this,"Por favor, insira a senha",Toast.LENGTH_LONG).show();
            return;
        }

        String processando = "Aguarde um momento...";
        textoMsgInvalida.setText(processando);
        textoMsgInvalida.setVisibility(View.VISIBLE);

        mProgress.setVisibility(View.VISIBLE);


        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                Log.d(TAG, "signInWithEmail:onComplete:" + task.isSuccessful());

                String msgDoCadastro;

                // If sign in fails, display a message to the user.
                if (!task.isSuccessful()) {

                    try {
                        throw task.getException();
                    } catch (FirebaseAuthInvalidUserException e) {
                        msgDoCadastro = "Email inválido ou não cadastrado.";

                    } catch (FirebaseAuthInvalidCredentialsException e) {
                        msgDoCadastro = "Senha inválida.";

                    } catch (FirebaseNetworkException e) {
                        msgDoCadastro = "Erro de conexão.";

                    } catch (Exception e) {

                        Log.e("fIREBASElOG", e.getMessage());
                        msgDoCadastro = "Ocorreu um erro ao realizar o login\n" +
                                "Tente mais tarde...";

                    }

                    textoMsgInvalida.setText(msgDoCadastro);
                    textoMsgInvalida.setTextColor(Color.parseColor("#d75757")); //Vermelho
                    mProgress.setVisibility(View.INVISIBLE);



                }else{


                    Usuario usuarioLogado = new Usuario();
                    usuarioLogado.setEmail(email);

                    new DaoFirebase().pegarDadosUsuario(usuarioLogado, new OnGetFirebaseUsuarioListener() {
                        @Override

                        public void onStart() {
                            //DO SOME THING WHEN START GET DATA HERE
                        }

                        @Override
                        public void onSuccess(Usuario usuarioRecuperado) {


                            Log.e("UsuarioLogado", " " + usuarioRecuperado.getEmail() + " " + usuarioRecuperado.getId() + "" + usuarioRecuperado.getNome());


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

                            String logado = "Logado!";
                            textoMsgInvalida.setTextColor(Color.parseColor("#44d773")); //Verde
                            textoMsgInvalida.setText(logado);
                            textoMsgInvalida.setVisibility(View.VISIBLE);


                            Intent mudarDeTela = new Intent(getBaseContext(), MainActivity.class);
                            startActivity(mudarDeTela);
                            finish();

                            Toast.makeText(getBaseContext(), "Bem vindo!", Toast.LENGTH_LONG).show();
                            mProgress.setVisibility(View.INVISIBLE);

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
