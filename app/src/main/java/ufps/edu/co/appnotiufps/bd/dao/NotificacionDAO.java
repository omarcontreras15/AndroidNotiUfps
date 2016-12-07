package ufps.edu.co.appnotiufps.bd.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.Date;


/**
 * Created by omara on 24/11/2016.
 */

public class NotificacionDAO extends SQLiteOpenHelper {

    private static final int VERSION_BASEDATOS = 1;

    // Nombre de nuestro archivo de base de datos
    private static final String NOMBRE_BASEDATOS = "Notificaciones.db";

    // Nombre de la tabla
    private static final String NOMBRE_TABLA = "Tabla_notificaciones";
    private Context ctx;
    private SQLiteDatabase db;


    // CONSTRUCTOR de la clase
    public NotificacionDAO(Context ctx) {
        super(ctx, NOMBRE_BASEDATOS, null, VERSION_BASEDATOS);
        this.ctx=ctx;
        //obtiene el archivo .bd que se encuentra dentro de la carpeta databases en la carpeta donde
        // el app se encuentra instalado
        this.db=getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //Crea las tablas en dado caso esta no existan.
        String insert="CREATE TABLE "+NOMBRE_TABLA+" (id INTEGER, fecha DATE, PRIMARY KEY(id,fecha))";
        db.execSQL(insert);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //si la estrctura de la bd se ha cambiado este metodo se ejecuta
        db.execSQL("DROP TABLE IF EXISTS " + NOMBRE_TABLA);
        onCreate(db);
    }

    //Este metodo inserta las notificaciones en la tabla Tabla_notificaciones
    //si esta notificacion ya existe no la inserta
    //si la fecha de la notificacion no corresponde con las demas fechas de la bd.
    //se borran todos los registros de notificacion y se insertara esta nueva notificación
    public boolean insetarNotificacion(int id){
        if(!this.verificarFechaBD()){
            this.db.execSQL("DELETE FROM  " + NOMBRE_TABLA);
        }
            // Contenedor de valores
            ContentValues values = new ContentValues();
            values.put("id",id);
            Date date=new Date();
            values.put("fecha",date.getDate()+"-"+(date.getMonth()+1)+"-"+(date.getYear()+1900));
        try {
            this.db.insertOrThrow(NOMBRE_TABLA, null, values);
            return true;
        }catch (Exception e){
            return false;
        }

    }

    //verifica que las otras notificaciones de la bd tengan la misma fecha que la notificacion que
    //se esta insertado en dado caso no sea asi procedera a borrar todos los datos de la tabla.
    private boolean verificarFechaBD(){
        Date date=new Date();
        Cursor c = db.rawQuery(" SELECT fecha FROM  "+NOMBRE_TABLA+" WHERE fecha='"+date.getDate()+
                "-"+(date.getMonth()+1)+"-"+(date.getYear()+1900)+"'", null);
        if(c.moveToFirst()){
            return true;
        }
        return false;
    }
}