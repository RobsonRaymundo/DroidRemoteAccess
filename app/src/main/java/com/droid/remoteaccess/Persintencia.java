package com.droid.remoteaccess;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by nalmir on 19/12/2015.
 */
public class Persintencia extends SQLiteOpenHelper {

    //public static final String BANCO = "/storage/extSdCard/BancoDados/contatosdbase.db3";
    public static final String BANCO = GetPathStorage() + "remoteAccess.db3";

    public static final int VERSAO = 15;
    //
    public static final String TABELA = "contatos";

    public static final String EMAIL = "email";
    public static final String TOKEN = "token";
    public static final String DEVICE = "device";

    public Persintencia(Context context) {
        super(context, BANCO, null, VERSAO);
    }

    /* Checks if external storage is available for read and write */
    public static boolean isExternalStorageMediaMounted() {
        return (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()));
    }

    public static String GetPathStorage() {
        String strSDCardPath = "";
        String strDirectory = "";
        String strPaste = "/DBase/";
        try {
            if (isExternalStorageMediaMounted()) {
                strSDCardPath = System.getenv("SECONDARY_STORAGE");
                if ((null == strSDCardPath) || (strSDCardPath.length() == 0)) {
                    strSDCardPath = System.getenv("EXTERNAL_SDCARD_STORAGE");
                }
                strDirectory = CreateGetDirectory(strSDCardPath + strPaste);
            }
        } catch (Exception e) {
        } finally {
            if (strSDCardPath == "" || strDirectory == "") {
                strSDCardPath = Environment.getExternalStorageDirectory().toString();
                strDirectory = CreateGetDirectory(strSDCardPath + strPaste);
            }
        }
        return strDirectory;
    }

    private static void TimeSleep(Integer seg) {
        try {
            Thread.sleep(seg);
        } catch (Exception ex) {
        }
    }

    public static String CreateGetDirectory(String pathStorage) {
        String pathDirectory = "";

        try {

            File myNewFolder = new File(pathStorage);

            if (!myNewFolder.exists()) {
                myNewFolder.mkdir();
                TimeSleep(1000);
            }
            if (myNewFolder.exists()) {
                pathDirectory = pathStorage;
            }
        } catch (Exception e) {

        }
        return pathDirectory;

    }

    @Override
    public void onCreate(SQLiteDatabase db) {


        StringBuilder stringBuilder = new StringBuilder();       //


        stringBuilder.append("CREATE TABLE IF NOT EXISTS [" + TABELA + "] (\n" +
                "  [email] CHAR(100) NOT NULL, \n" +
                "  [token] CHAR(200) NOT NULL, \n" +
                "  [device] CHAR(100));\n" +
                "  CONSTRAINT [] PRIMARY KEY ([email])); ");
        //
        db.execSQL(stringBuilder.toString());
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABELA + ";");
        //
        onCreate(db);
    }

    private void inserirContato(Contato contato) {
        ContentValues cv = new ContentValues();
        //
        cv.put(EMAIL, contato.getEmail());
        cv.put(TOKEN, contato.getToken());
        cv.put(DEVICE, contato.getDevice());
        //
        getWritableDatabase().insert(TABELA, null, cv);
    }

    ;


    public void InserirContato(Contato contato) {
        if (contatoCadastrado(contato.getEmail())) {
            atualizarContato(contato);
        } else {
            inserirContato(contato);
        }
    }

    public boolean contatoCadastrado(String email) {
        boolean cadastrado = false;
        //
        Cursor cursor = null;
        //
        try {
            String[] argumentos = new String[]{email};
            //
            StringBuilder sb = new StringBuilder();
            sb.append("SELECT * FROM " + TABELA + " WHERE email = ?");
            //
            cursor = getWritableDatabase().rawQuery(sb.toString(), argumentos);
            //

            cadastrado = cursor.getCount() > 0;

        } catch (Exception e) {
            Log.d("DBase", e.getMessage());

        } finally {
            if (cursor != null) {
                cursor.close();
                cursor = null;
            }
        }
        //
        return cadastrado;
    }

    private void atualizarContato(Contato contato) {
        ContentValues cv = new ContentValues();
        //
        String[] argumentos = new String[]{contato.getEmail()};
        String FILTRO = EMAIL + " = ?";
        //
        cv.put(TOKEN, contato.getToken());
        cv.put(DEVICE, contato.getDevice());
        //
        getWritableDatabase().update(TABELA, cv, FILTRO, argumentos);
    }

    public void apagarContato(String email) {
        String[] argumentos = new String[]{email};
        String FILTRO = EMAIL + " = ?";
        //
        getWritableDatabase().delete(TABELA, FILTRO, argumentos);
    }

    public Contato obterContato(String email) {
        Contato cAux = null;
        //
        Cursor cursor = null;
        //
        try {
            String[] argumentos = new String[]{email};
            //
            StringBuilder sb = new StringBuilder();
            sb.append("SELECT * FROM " + TABELA + " WHERE email = ?");
            //
            cursor = getWritableDatabase().rawQuery(sb.toString(), argumentos);
            //
            while (cursor.moveToNext()) {
                cAux = new Contato();
                //
                cAux.setEmail(cursor.getString(cursor.getColumnIndex(EMAIL)));
                cAux.setToken(cursor.getString(cursor.getColumnIndex(TOKEN)));
                cAux.setDevice(cursor.getString(cursor.getColumnIndex(DEVICE)));
            }


        } catch (Exception e) {
            Log.d("DBase", e.getMessage());

        } finally {
            if (cursor != null) {
                cursor.close();
                cursor = null;
            }
        }
        //
        return cAux;
    }

    public ArrayList<HMContato> listaContatos() {
        ArrayList<HMContato> contatos = new ArrayList<>();
        //
        Cursor cursor = null;
        //
        try {
            StringBuilder sb = new StringBuilder();
            sb.append("SELECT " + EMAIL + "," + DEVICE + " FROM " + TABELA + " ORDER BY " + EMAIL);
            //
            cursor = getWritableDatabase().rawQuery(sb.toString(), null);
            //
            while (cursor.moveToNext()) {
                HMContato item = new HMContato();
                //
                item.put(HMContato.EMAIL, cursor.getString(cursor.getColumnIndex(EMAIL)));
                item.put(HMContato.DEVICE, cursor.getString(cursor.getColumnIndex(DEVICE)));
                //
                contatos.add(item);
            }


        } catch (Exception e) {
            Log.d("DBase", e.getMessage());

        } finally {
            if (cursor != null) {
                cursor.close();
                cursor = null;
            }
        }

        //
        return contatos;
    }


}
