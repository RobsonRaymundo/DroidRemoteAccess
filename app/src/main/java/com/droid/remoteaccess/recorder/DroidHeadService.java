package com.droid.remoteaccess.recorder;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.PixelFormat;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Vibrator;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.OrientationEventListener;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.droid.remoteaccess.activitys.DroidConfigurationActivity;
import com.droid.remoteaccess.feature.Constantes;
import com.droid.remoteaccess.others.Methods;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Locale;


public class DroidHeadService extends Service implements TextToSpeech.OnInitListener {
    private WindowManager windowManager;

    private SurfaceView mSurfaceView;

    private int orientationEvent;
    private Context context;
    private String chamadaPorComandoTexto;
    private SensorManager sensorManager;
    public static boolean closeSensorProximity;
    public static boolean openSensorProximity;
    public static boolean currentCloseSensorProximity;

    private boolean necessarioComandoDepoisDoInit = false;
    private SensorEventListener sensorEventListener;
    private Intent mIntentService;
    private TextToSpeech tts;
    private ArrayList<Constantes.EnumStateRecVideo> stateRecVideoSTOP;
    private ArrayList<Constantes.EnumStateRecVideo> stateRecVideoVIEW;
    private ArrayList<Constantes.EnumStateRecVideo> stateRecVideoREC;
    private ArrayList<Constantes.EnumStateRecVideo> stateRecVideoCLOSE;

    OrientationEventListener myOrientationEventListener;

    WindowManager.LayoutParams params = new WindowManager.LayoutParams(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.TYPE_PHONE,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
            PixelFormat.TRANSLUCENT);

    private void TimeSleep(Integer seg) {
        try {
            Thread.sleep(seg);
        } catch (Exception ex) {
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        // Not used
        return null;
    }

    @Override
    public void onInit(int status) {
        necessarioComandoDepoisDoInit = true;

        if (ComandoPorTexto("vr")) {
            Gravar();
        } else if (ComandoPorTexto("vc")) {
            AbrirConfig();
        }
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        mIntentService = intent;

        if (ComandoPorTexto("vs")) {
            Parar();
        } else if (ComandoPorTexto("vv")) {
            Visualizar();
        } else if (ComandoPorTexto("vr")) {
            Gravar();
        } else if (ComandoPorTexto("vc")) {
            Fechar();
        } else if (ComandoPorTexto("vq")) {
            Sair();
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        InicializarVariavel();
        InicializarAcao();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        //call widget update methods/services/broadcasts
        if (DroidVideoRecorder.StateRecVideo == Constantes.EnumStateRecVideo.VIEW) {
            DroidVideoRecorder.OnInitRec(getResources().getConfiguration(), orientationEvent, DroidVideoRecorder.TypeViewCam);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mSurfaceView != null) windowManager.removeView(mSurfaceView);
        DisabledSensorPriximity();
        Vibrar(100);
    }

    private void InicializarVariavel() {
        context = getBaseContext();

        windowManager = (WindowManager) context.getSystemService(WINDOW_SERVICE);

        mSurfaceView = new SurfaceView(context);
        mSurfaceView.setLayoutParams(params);
        mSurfaceView.getHolder().setFixedSize(1, 1);

        params.gravity = Gravity.CENTER;
        windowManager.addView(mSurfaceView, params);
        tts = new TextToSpeech(context, this);

        DroidVideoRecorder.StateRecVideo = Constantes.EnumStateRecVideo.STOP;
        DroidVideoRecorder.LocalGravacaoVideo = Methods.obtemLocalGravacao(context);
        sensorEventListener = new sensorEventListener();
        tts.setLanguage(Locale.getDefault());

        stateRecVideoSTOP = new ArrayList<>();
        stateRecVideoSTOP.add(Constantes.EnumStateRecVideo.VIEW);
        stateRecVideoSTOP.add(Constantes.EnumStateRecVideo.RECORD);
        stateRecVideoSTOP.add(Constantes.EnumStateRecVideo.CLOSE);

        stateRecVideoSTOP = new ArrayList<>();
        stateRecVideoSTOP.add(Constantes.EnumStateRecVideo.VIEW);
        stateRecVideoSTOP.add(Constantes.EnumStateRecVideo.RECORD);
        stateRecVideoSTOP.add(Constantes.EnumStateRecVideo.CLOSE);

        stateRecVideoVIEW = new ArrayList<>();
        stateRecVideoVIEW.add(Constantes.EnumStateRecVideo.VIEW);
        stateRecVideoVIEW.add(Constantes.EnumStateRecVideo.RECORD);
        stateRecVideoVIEW.add(Constantes.EnumStateRecVideo.STOP);

        stateRecVideoREC = new ArrayList<>();
        stateRecVideoREC.add(Constantes.EnumStateRecVideo.STOP);

        stateRecVideoCLOSE = new ArrayList<>();
        stateRecVideoCLOSE.add(Constantes.EnumStateRecVideo.CLOSE);
        stateRecVideoCLOSE.add(Constantes.EnumStateRecVideo.STOP);

    }

    private void InicializarAcao() {


        myOrientationEventListener = new OrientationEventListener(context, SensorManager.SENSOR_DELAY_NORMAL) {
            @Override
            public void onOrientationChanged(int arg0) {
                // TODO Auto-generated method stub
                orientationEvent = arg0;
            }
        };
    }

    private void StopService() {
        context.stopService(mIntentService);
        tts.shutdown();
    }

    private boolean ComandoPorTexto(String cmd) {
        boolean ret = false;
        chamadaPorComandoTexto = mIntentService.getStringExtra(Constantes.CHAMADAPORCOMANDOTEXTO);
        if (chamadaPorComandoTexto != null) {
            ret = chamadaPorComandoTexto.equalsIgnoreCase(cmd);
        }

        return ret;
    }

    private void Gravar() {
        Gravacao();
    }

    private void Gravacao() {
        if (necessarioComandoDepoisDoInit) {
            if (Permite(DroidVideoRecorder.StateRecVideo.RECORD)) {
                ShowRec();
            }
        }
    }

    private void AbrirConfig() {
        ShowActivity();
    }

    private void Parar() {
        if (Permite(DroidVideoRecorder.StateRecVideo.STOP)) {
            ShowStopRecord(true);
        }
    }

    private void Visualizar() {
        if (Permite(DroidVideoRecorder.StateRecVideo.VIEW)) {
            ShowView();
        }
    }

    private void VisualizarTrocandoCamera() {
        if (Permite(DroidVideoRecorder.StateRecVideo.VIEW)) {
            ShowStopRecord(false);
            ChangeTypeViewCam();
        }
    }

    private void Fechar() {
        if (Permite(DroidVideoRecorder.StateRecVideo.CLOSE)) {
            ShowClose();
        }
    }

    private void Sair() {
        if (Permite(DroidVideoRecorder.StateRecVideo.CLOSE)) {
            StopService();
        }
    }

    public void SetSensorProximity(boolean turnOn) {
        try {

            if (turnOn && sensorManager == null) {
                sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
                Sensor proximitySensor = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);

                if (proximitySensor != null) {
                    sensorManager.registerListener(sensorEventListener, proximitySensor, SensorManager.SENSOR_DELAY_NORMAL);
                    TimeSleep(1000);
                }
            }

            if (turnOn == false && sensorManager != null) {
                sensorManager.unregisterListener(sensorEventListener);
                //  timeSleep(700);
                sensorManager = null;
            }

        } catch (Exception ex) {
            String msg = ex.getMessage();

        }
    }

    private void EnabledSensorPriximity() {
        SetSensorProximity(true);
    }

    private void DisabledSensorPriximity() {
        SetSensorProximity(false);
    }

    private void ShowView() {
        mSurfaceView.getHolder().setFixedSize(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.MATCH_PARENT);
        DroidVideoRecorder.OnInitRec(context.getResources().getConfiguration(), orientationEvent, Constantes.EnumTypeViewCam.FacingBack);
        DroidVideoRecorder.OnViewRec(mSurfaceView.getHolder());
        DroidVideoRecorder.StateRecVideo = Constantes.EnumStateRecVideo.VIEW;
        Vibrar(100);
    }

    private void ChangeTypeViewCam() {
        mSurfaceView.getHolder().setFixedSize(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.MATCH_PARENT);

        if (DroidVideoRecorder.TypeViewCam == Constantes.EnumTypeViewCam.FacingBack) {
            DroidVideoRecorder.TypeViewCam = Constantes.EnumTypeViewCam.FacingFront;
        } else {
            DroidVideoRecorder.TypeViewCam = Constantes.EnumTypeViewCam.FacingBack;
        }

        DroidVideoRecorder.OnInitRec(getResources().getConfiguration(), orientationEvent, DroidVideoRecorder.TypeViewCam);
        DroidVideoRecorder.OnViewRec(mSurfaceView.getHolder());
        DroidVideoRecorder.StateRecVideo = Constantes.EnumStateRecVideo.VIEW;
        Vibrar(100);
    }

    private void ShowRec() {
        mSurfaceView.getHolder().setFixedSize(1, 1);
        DroidVideoRecorder.OnInitRec(getResources().getConfiguration(), orientationEvent, DroidVideoRecorder.TypeViewCam);
        DroidVideoRecorder.OnStartRecording(mSurfaceView.getHolder(), orientationEvent, Methods.obtemQualidadeCamera(this, Constantes.EnumTypeViewCam.FacingBack));
        DroidVideoRecorder.StateRecVideo = Constantes.EnumStateRecVideo.RECORD;
        Vibrar(50);
    }

    private void ShowStopRecord(boolean record) {
        DroidVideoRecorder.OnStopRecording(record);
        ShowStop();
        Vibrar(50);
    }

    private void GetDefaultStop() {
        mSurfaceView.getHolder().setFixedSize(1, 1);
        DroidVideoRecorder.StateRecVideo = Constantes.EnumStateRecVideo.STOP;
        DroidVideoRecorder.OnInitRec(getResources().getConfiguration(), orientationEvent, Constantes.EnumTypeViewCam.FacingBack);
        DroidVideoRecorder.OnViewRec(mSurfaceView.getHolder());
        DroidVideoRecorder.OnStopRecording(false);
    }

    private void ShowStop() {

        DroidVideoRecorder.StateRecVideo = Constantes.EnumStateRecVideo.STOP;
    }

    private void ShowClose() {
        DroidVideoRecorder.StateRecVideo = Constantes.EnumStateRecVideo.CLOSE;
        Vibrar(50);
    }

    private void ShowActivity() {
        context = getBaseContext();
        Intent mItent = new Intent(context, DroidConfigurationActivity.class);
        mItent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mItent.putExtra(Constantes.CHAMADAPELOSERVICO, true);
        startActivity(mItent);
    }

    private boolean Permite(Constantes.EnumStateRecVideo stateRecVideo) {

        if (DroidVideoRecorder.StateRecVideo == Constantes.EnumStateRecVideo.STOP) {
            return stateRecVideoSTOP.contains(stateRecVideo);
        } else if (DroidVideoRecorder.StateRecVideo == Constantes.EnumStateRecVideo.VIEW) {
            return stateRecVideoVIEW.contains(stateRecVideo);
        } else if (DroidVideoRecorder.StateRecVideo == Constantes.EnumStateRecVideo.RECORD) {
            return stateRecVideoREC.contains(stateRecVideo);
        } else if (DroidVideoRecorder.StateRecVideo == Constantes.EnumStateRecVideo.CLOSE) {
            return stateRecVideoCLOSE.contains(stateRecVideo);
        } else return false;
    }

    private void Vibrar(int valor) {
        try {
            Vibrator v = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
            v.vibrate(valor);
        } catch (Exception ex) {
        }
    }

    public class sensorEventListener implements SensorEventListener {

        @Override
        public void onSensorChanged(SensorEvent event) {
            if (event.sensor.getType() == Sensor.TYPE_PROXIMITY) {
                if (event.values[0] < event.sensor.getMaximumRange()) {
                    closeSensorProximity = true;
                    currentCloseSensorProximity = true;
                } else {
                    openSensorProximity = true;
                    currentCloseSensorProximity = false;
                }
            }

            if (currentCloseSensorProximity && closeSensorProximity && openSensorProximity) {

                if (DroidVideoRecorder.StateRecVideo == Constantes.EnumStateRecVideo.RECORD) {
                    Parar();
                } else {
                    // Inicia o Listener do reconhecimento de voz
                    currentCloseSensorProximity = false;
                    closeSensorProximity = false;
                    openSensorProximity = false;
                }
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    }
}





