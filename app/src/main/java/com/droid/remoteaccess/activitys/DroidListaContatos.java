package com.droid.remoteaccess.activitys;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.droid.remoteaccess.feature.Constantes;
import com.droid.remoteaccess.feature.Contato;
import com.droid.remoteaccess.feature.HMContato;
import com.droid.remoteaccess.dbase.Persintencia;
import com.droid.remoteaccess.R;
import com.droid.remoteaccess.others.Methods;

/**
 * Created by Robson on 06/03/2016.
 */
public class DroidListaContatos extends AppCompatActivity {

    private Context context;
    private TextView tv_nomeAparelho;
    private ListView lv_contatos;
    private Persintencia persintencia;
    private Contato contato;
    private ReceiverResponseListaContatos receiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.telalistacontatos);

        context = getBaseContext();
        tv_nomeAparelho = (TextView) findViewById(R.id.telalistacontatos_tv_nomeAparelho);
        lv_contatos = (ListView) findViewById(R.id.telalistacontatos__lv_contatos);
        persintencia = new Persintencia(context);

        contato = persintencia.ObterContato(Methods.getIDDevice(context));
        tv_nomeAparelho.setText(contato.getEmail());

        atualizaAdapterContatos();


        lv_contatos.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                HMContato item = (HMContato) parent.getItemAtPosition(position);
                //
                //chamarDetalhes(Long.parseLong(item.get(HMContato.EMAIL)));
                //

                persintencia.ApagarContato(item.get(HMContato.ID));
                Methods.showMessage(DroidListaContatos.this, "Registro apagado");
                atualizaAdapterContatos();
                return true;
            }
        });
        lv_contatos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent mIntent = new Intent(context, DroidControleRemoto.class);
                HMContato item = (HMContato) parent.getItemAtPosition(position);
                mIntent.putExtra(Constantes.ID_FROM, contato.getId());
                mIntent.putExtra(Constantes.ID_TO, item.get(HMContato.ID));
                startActivity(mIntent);
            }
        });

        IntentFilter filter = new IntentFilter();
        filter.addAction(Constantes.RECEIVERRESPONSELISTACONTATOS);
        filter.addCategory(Intent.CATEGORY_DEFAULT);

        receiver = new ReceiverResponseListaContatos();
        //
        registerReceiver(receiver, filter);

    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(receiver);
        super.onDestroy();
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


    public class ReceiverResponseListaContatos extends BroadcastReceiver
    {

        @Override
        public void onReceive(Context context, Intent intent) {
            String message = intent.getStringExtra(Constantes.MESSAGE);
            if (message.contentEquals("refresh"))
            {
                atualizaAdapterContatos();
            }

        }
    }
}
