/**
 * Copyright 2013 Google Inc. All Rights Reserved.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software distributed under the
 * License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.droid.remoteaccess.gdrive;

import android.content.Intent;
import android.content.IntentSender;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.util.Log;

import com.droid.remoteaccess.R;
import com.droid.remoteaccess.feature.Constantes;
import com.droid.remoteaccess.others.Methods;
import com.droid.remoteaccess.recorder.DroidVideoRecorder;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.drive.Drive;
import com.google.android.gms.drive.DriveApi.DriveContentsResult;
import com.google.android.gms.drive.DriveContents;
import com.google.android.gms.drive.DriveFile;
import com.google.android.gms.drive.DriveFolder.DriveFileResult;
import com.google.android.gms.drive.MetadataChangeSet;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;

/**
 * An activity to illustrate how to create a file.
 */
public class CreateFileActivity extends BaseDemoActivity {



    private static final String TAG = "CreateFileActivity";
    private String chamadaPorComandoTexto;

    private boolean ComandoPorTexto(String cmd) {
        boolean ret = false;
        chamadaPorComandoTexto = getIntent().getStringExtra(Constantes.CHAMADAPORCOMANDOTEXTO);
        if (chamadaPorComandoTexto != null) {
            ret = chamadaPorComandoTexto.equalsIgnoreCase(cmd);
        }

        return ret;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onConnected(Bundle connectionHint) {
        super.onConnected(connectionHint);
        // create new contents resource
        Drive.DriveApi.newDriveContents(getGoogleApiClient())
                .setResultCallback(driveContentsCallback);
    }

    final private ResultCallback<DriveContentsResult> driveContentsCallback = new
            ResultCallback<DriveContentsResult>() {
        @Override
        public void onResult(DriveContentsResult result) {
            if (!result.getStatus().isSuccess()) {
                showMessage("Error while trying to create new file contents");
                return;
            }
            final DriveContents driveContents = result.getDriveContents();

            // Perform I/O off the UI thread.
            new Thread() {
                @Override
                public void run() {
                    // write content to DriveContents

                    /*

                     OutputStream outputStream = driveContents.getOutputStream();
                    Writer writer = new OutputStreamWriter(outputStream);
                    try {
                        writer.write("Hello World!");
                        writer.close();
                    } catch (IOException e) {
                        Log.e(TAG, e.getMessage());
                    }

                    MetadataChangeSet changeSet = new MetadataChangeSet.Builder()
                            .setTitle("New file")
                            .setMimeType("text/plain")
                            .setStarred(true).build();

                    // create a file on root folder
                    Drive.DriveApi.getRootFolder(getGoogleApiClient())
                            .createFile(getGoogleApiClient(), changeSet, driveContents)
                            .setResultCallback(fileCallback);
                            */

                    /*
                    final Bitmap image =  BitmapFactory.decodeFile(DroidVideoRecorder.GetPathStorage() + "/monkey.PNG");

                    OutputStream outputStream = driveContents.getOutputStream();

                    ByteArrayOutputStream bitmapStream = new ByteArrayOutputStream();
                    image.compress(Bitmap.CompressFormat.PNG, 100, bitmapStream);
                    try {
                        outputStream.write(bitmapStream.toByteArray());
                    } catch (IOException e1) {
                        Log.i(TAG, "Unable to write file contents.");
                    }

                    MetadataChangeSet changeSet = new MetadataChangeSet.Builder()
                            .setMimeType("image/jpeg").setTitle("Monkey").build();

                    // create a file on root folder
                    Drive.DriveApi.getRootFolder(getGoogleApiClient())
                            .createFile(getGoogleApiClient(), changeSet, driveContents)
                            .setResultCallback(fileCallback);
                            */



                    try {



                        OutputStream outputStream = driveContents.getOutputStream();
                        byte[] bytes = null;
                        String sMimeType = "";
                        if (ComandoPorTexto("ua")) {
                            sMimeType = "audio/mpeg3";
                            bytes = convert(DroidVideoRecorder.GetPathStorage() + "/audio.mp3");
                        }
                        else {
                            sMimeType = "video/mpeg";
                            bytes = convert(DroidVideoRecorder.GetPathStorage() + "/video.mp4");
                        }

                        try {
                            outputStream.write(bytes);
                        } catch (IOException e1) {
                            Log.i(TAG, "Unable to write file contents.");
                        }


                        MetadataChangeSet changeSet = new MetadataChangeSet.Builder()
                                .setMimeType(sMimeType).setTitle(Methods.getAccount(getBaseContext()) + "_Audio_" + Methods.getDateTimeFormated()).build();

                        // create a file on root folder
                        Drive.DriveApi.getRootFolder(getGoogleApiClient())
                                .createFile(getGoogleApiClient(), changeSet, driveContents)
                                .setResultCallback(fileCallback);


                    }catch (Exception ex)
                    {

                    }

                }
            }.start();
        }
    };

    public byte[] convert(String path) throws IOException {

        FileInputStream fis = new FileInputStream(path);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] b = new byte[1024];

        for (int readNum; (readNum = fis.read(b)) != -1;) {
            bos.write(b, 0, readNum);
        }

        byte[] bytes = bos.toByteArray();

        return bytes;
    }

    final private ResultCallback<DriveFileResult> fileCallback = new
            ResultCallback<DriveFileResult>() {
        @Override
        public void onResult(DriveFileResult result) {
            if (!result.getStatus().isSuccess()) {
                showMessage("Error while trying to create the file");
                return;
            }
         //   showMessage("Created a file with content: " + result.getDriveFile().getDriveId());
            finish();
        }
    };


}
