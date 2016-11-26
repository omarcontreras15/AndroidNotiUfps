package ufps.edu.co.appnotiufps.bd.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by omara on 24/11/2016.
 */

public class NotificacionDAO extends SQLiteOpenHelper {

    private static final int VERSION_BASEDATOS = 1;

    // Nombre de nuestro archivo de base de datos
    private static final String NOMBRE_BASEDATOS = "notificaciones.db";

    // Nombre de la tabla
    private static final String NOMBRE_TABLA = "notificaciones";
    private Context ctx;


    // CONSTRUCTOR de la clase
    public NotificacionDAO(Context ctx) {
        super(ctx, NOMBRE_BASEDATOS, null, VERSION_BASEDATOS);
        this.ctx=ctx;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String insert="CREATE TABLA "+NOMBRE_TABLA+" (id integer, fecha timeStamp, PRIMARY KEY(id))";
        System.out.println(insert);
        db.execSQL(insert);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + NOMBRE_TABLA);
        onCreate(db);
    }

    public boolean insetarNotificacion(int id){

        return false;
    }
}