package com.droid.remoteaccess.activitys;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.SwitchPreference;
import android.util.Log;

import com.droid.remoteaccess.R;
import com.droid.remoteaccess.feature.Constantes;
import com.droid.remoteaccess.others.Methods;
import com.droid.remoteaccess.recorder.DroidVideoRecorder;

/**
 * Created by Robson on 12/01/2016.
 */
public class DroidConfigurationActivity extends PreferenceActivity {
    private Context context;
    private ListPreference ltp_qualidadeCameraFrontal;
    private ListPreference ltp_qualidadeCameraTraseira;
    private ListPreference ltp_localGravacaoVideo;
    private boolean canFinish;

    private boolean ExibeTelaInicial() {
        return Methods.exibeTelaInicial(context);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        context = getBaseContext();
        boolean exibeTelaInicial = ExibeTelaInicial();

        if (exibeTelaInicial) {
            setTheme(R.style.DefaultTheme);
        } else {
            setTheme(R.style.TranslucentTheme);
        }
        super.onCreate(savedInstanceState);
        canFinish = true;

        if (exibeTelaInicial) {
            addPreferencesFromResource(R.xml.preferences);

            ltp_qualidadeCameraFrontal = (ListPreference) findPreference("ltp_qualidadeCameraFrontal");
            ltp_qualidadeCameraFrontal.setSummary(Methods.obtemDescricaoPreferencias(context, String.valueOf(Methods.obtemQualidadeCamera(context, Constantes.EnumTypeViewCam.FacingFront)), R.array.qualidadeCameraFrontal, R.array.valor_qualidadeCameraFrontal));
            ltp_qualidadeCameraFrontal.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    preference.setSummary(Methods.obtemDescricaoPreferencias(context, newValue.toString(), R.array.qualidadeCameraFrontal, R.array.valor_qualidadeCameraFrontal));
                    return true;
                }
            });

            ltp_qualidadeCameraTraseira = (ListPreference) findPreference("ltp_qualidadeCameraTraseira");
            ltp_qualidadeCameraTraseira.setSummary(Methods.obtemDescricaoPreferencias(context, String.valueOf(Methods.obtemQualidadeCamera(context, Constantes.EnumTypeViewCam.FacingBack)), R.array.qualidadeCameraTraseira, R.array.valor_qualidadeCameraTraseira));
            ltp_qualidadeCameraTraseira.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    preference.setSummary(Methods.obtemDescricaoPreferencias(context, newValue.toString(), R.array.qualidadeCameraTraseira, R.array.valor_qualidadeCameraTraseira));
                    return true;
                }
            });

            ltp_localGravacaoVideo = (ListPreference) findPreference("ltp_localGravacaoVideo");
            ltp_localGravacaoVideo.setSummary(Methods.obtemDescricaoPreferencias(context, String.valueOf(Methods.obtemLocalGravacao(context)), R.array.localArquivosGravados, R.array.valor_localArquivosGravados));
            ltp_localGravacaoVideo.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    preference.setSummary(Methods.obtemDescricaoPreferencias(context, newValue.toString(), R.array.localArquivosGravados, R.array.valor_localArquivosGravados));
                    DroidVideoRecorder.LocalGravacaoVideo = Integer.parseInt(newValue.toString());
                    return true;
                }
            });





        } else finish();


    }


    @Override
    protected void onResume() {
        super.onResume();
        if (!ExibeTelaInicial() ) {
            finish();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (canFinish) finish();
    }
}
