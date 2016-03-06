package com.droid.remoteaccess;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

/**
 * Created by Robson on 06/03/2016.
 */
public class DroidComando extends AppCompatActivity {
    private Context context;
    private TextView tv_comando;
    private Button btn_fechar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.telacomando);

        String message = getIntent().getStringExtra("message");

        context = getBaseContext();
        tv_comando = (TextView) findViewById(R.id.telacomando_tv_comando);

        tv_comando.setText(message);
        btn_fechar = (Button) findViewById(R.id.telacomando_btn_fechar);

        btn_fechar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });



    }
}
