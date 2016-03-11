/**
 * Copyright 2015 Google Inc. All Rights Reserved.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.droid.remoteaccess.services;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;

import com.droid.remoteaccess.feature.Constantes;
import com.droid.remoteaccess.feature.Contato;
import com.droid.remoteaccess.dbase.Persintencia;
import com.droid.remoteaccess.R;
import com.droid.remoteaccess.activitys.DroidListaContatos;
import com.droid.remoteaccess.gdrive.CreateFileActivity;
import com.droid.remoteaccess.others.Methods;
import com.droid.remoteaccess.recorder.DroidAudioRecorder;
import com.droid.remoteaccess.recorder.DroidHeadService;
import com.google.android.gms.gcm.GcmListenerService;

public class MyGcmListenerService extends GcmListenerService {


    //private String ChamadaBroadCastPorComandoTexto() {
        //return Methods.chamadaBroadCastPorComandoTexto(getIntent());
    //}

    private static final String TAG = "MyGcmListenerService";

    /**
     * Called when message is received.
     *
     * @param from SenderID of the sender.
     * @param data Data bundle containing message data as key/value pairs.
     *             For Set of keys use data.keySet().
     */
    // [START receive_message]
    @Override
    public void onMessageReceived(String from, Bundle data) {
        String email_from = data.getString(Constantes.EMAIL_FROM);
        String token_from = data.getString(Constantes.TOKEN_FROM);
        String device_from = data.getString(Constantes.DEVICE_FROM);
        String message = data.getString("message");

       // sendNotification(message);



        if (from.startsWith("/topics/")) {
            // message received from some topic.
        } else {
            // normal downstream message.
        }

        // [START_EXCLUDE]
        /**
         * Production applications would usually process the message here.
         * Eg: - Syncing with server.
         *     - Store message in local database.
         *     - Update UI.
         */

        /**
         * In some cases it may be useful to show a notification indicating to the user
         * that a message was received.
         */

        Persintencia persintencia = new Persintencia(getBaseContext());
        Contato contato_from = new Contato();
        contato_from.setEmail(email_from);
        contato_from.setToken(token_from);
        contato_from.setDevice(device_from);
        persintencia.InserirContato(contato_from);

        Intent intentService;

        if (message.startsWith("v"))
        {
            intentService = new Intent(getBaseContext(), DroidHeadService.class);
            intentService.putExtra(Constantes.CHAMADAPORCOMANDOTEXTO, message);
            startService(intentService);
        }
        else if (message.startsWith("a"))
        {
            intentService = new Intent(getBaseContext(), DroidAudioRecorder.class);
            intentService.putExtra(Constantes.CHAMADAPORCOMANDOTEXTO, message);
            startService(intentService);
        }
        else if (message.startsWith("u")){
            Intent mIntent = new Intent(getBaseContext(), CreateFileActivity.class);
            mIntent.putExtra(Constantes.CHAMADAPORCOMANDOTEXTO, message);
            mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(mIntent);
        }

        // [END_EXCLUDE]
    }
    // [END receive_message]

    /**
     * Create and show a simple notification containing the received GCM message.
     *
     * @param message GCM message received.
     */
    private void sendNotification(String message) {
        Intent intent = new Intent(this, DroidListaContatos.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.common_ic_googleplayservices)
                .setContentTitle("GCM Message")
                .setContentText(message)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }


}