package com.droid.remoteaccess;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

/**
 * Created by Robson on 06/03/2016.
 */
public class DroidControleRemoto extends AppCompatActivity {

    private Context context;
    private TextView tv_controlando;
    private Button btn_abrir;
    private Button btn_fechar;
    private Persintencia persintencia;
    private Contato contato;
    private String token;
    private String emailFrom;
    private String emailTo;

    public DroidControleRemoto() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.telacontroleremoto);

        context = getBaseContext();
        tv_controlando = (TextView) findViewById(R.id.telacontroleremoto_tv_controlando);
        emailFrom = getIntent().getStringExtra(Constantes.EMAIL_FROM);
        emailTo = getIntent().getStringExtra(Constantes.EMAIL_TO);
        tv_controlando.setText(emailTo);
        persintencia = new Persintencia(getBaseContext());
        Contato contato = persintencia.obterContato(emailTo);
        token = contato.getToken();

        btn_abrir = (Button) findViewById(R.id.btn_abrir);
        btn_abrir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EnviarMensagem("Abrir");
            }
        });

        btn_fechar = (Button) findViewById(R.id.btn_fechar);
        btn_fechar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EnviarMensagem("Fechar");
            }
        });

    }

    private void EnviarMensagem(String message)
    {
        try {
            Intent intent = new Intent(DroidControleRemoto.this, RegistrationIntentService.class);
            intent.putExtra(Constantes.EMAIL_FROM, emailFrom);
            intent.putExtra(Constantes.EMAIL_TO, emailTo);
            intent.putExtra(Constantes.MESSAGE, message);
            startService(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
