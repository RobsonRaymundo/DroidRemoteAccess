package com.droid.remoteaccess.activitys;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.droid.remoteaccess.feature.Constantes;
import com.droid.remoteaccess.feature.Contato;
import com.droid.remoteaccess.dbase.Persintencia;
import com.droid.remoteaccess.R;
import com.droid.remoteaccess.services.RegistrationIntentService;

/**
 * Created by Robson on 06/03/2016.
 */
public class DroidControleRemoto extends AppCompatActivity {

    private Context context;
    private TextView tv_controlando;
    private Button btn_gravar_video;
    private Button btn_parar_video;
    private Button btn_enviar_video;
    private Button btn_gravar_audio;
    private Button btn_parar_audio;
    private Button btn_enviar_audio;
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

        btn_gravar_video = (Button) findViewById(R.id.btn_gravar_video);
        btn_gravar_video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EnviarMensagem("vr");
            }
        });

        btn_parar_video = (Button) findViewById(R.id.btn_parar_video);
        btn_parar_video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EnviarMensagem("vs");
            }
        });

        btn_enviar_video = (Button) findViewById(R.id.btn_enviar_video);
        btn_enviar_video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EnviarMensagem("uv");
            }
        });

        btn_gravar_audio = (Button) findViewById(R.id.btn_gravar_audio);
        btn_gravar_audio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EnviarMensagem("ar");
            }
        });

        btn_parar_audio = (Button) findViewById(R.id.btn_parar_audio);
        btn_parar_audio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EnviarMensagem("as");
            }
        });

        btn_enviar_audio = (Button) findViewById(R.id.btn_enviar_audio);
        btn_enviar_audio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EnviarMensagem("ua");
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