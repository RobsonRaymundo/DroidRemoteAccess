package com.droid.remoteaccess;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Robson on 06/03/2016.
 */
public class DroidListaContatos extends AppCompatActivity {

    private Context context;
    private TextView tv_nomeAparelho;
    private ListView lv_contatos;
    private Persintencia persintencia;
    private Contato contato;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.telalistacontatos);

        context = getBaseContext();
        tv_nomeAparelho = (TextView) findViewById(R.id.telalistacontatos_tv_nomeAparelho);
        lv_contatos = (ListView) findViewById(R.id.telalistacontatos__lv_contatos);
        persintencia = new Persintencia(context);

        contato = persintencia.obterContato(Util.getEmail(context));
        tv_nomeAparelho.setText(contato.getDevice());

        atualizaAdapterContatos();


        lv_contatos.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                HMContato item = (HMContato) parent.getItemAtPosition(position);
                //
                //chamarDetalhes(Long.parseLong(item.get(HMContato.EMAIL)));
                //

                persintencia.apagarContato(item.get(HMContato.EMAIL));
                Util.showMessage(DroidListaContatos.this, "Registro apagado");
                atualizaAdapterContatos();
                return true;
            }
        });
        lv_contatos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent mIntent = new Intent(context, DroidControleRemoto.class);
                HMContato item = (HMContato) parent.getItemAtPosition(position);
                mIntent.putExtra(Constantes.EMAIL_FROM, contato.getEmail());
                mIntent.putExtra(Constantes.EMAIL_TO, item.get(HMContato.EMAIL));
                startActivity(mIntent);
            }
        });

    }

    private void atualizaAdapterContatos() {

        String[] from = {HMContato.EMAIL, HMContato.DEVICE};
        int[] to = {R.id.celula_tv_email, R.id.celula_tv_device};

        lv_contatos.setAdapter(

                //   new ArrayAdapter<HMContato>(
                //           context,
                //           R.layout.celula,
                //           persintencia.listaContatos()
                //   )

                new SimpleAdapter(context, persintencia.listaContatos(), R.layout.celula, from, to)

        );
    }
}
