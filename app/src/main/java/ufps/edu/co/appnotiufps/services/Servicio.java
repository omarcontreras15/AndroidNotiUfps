package ufps.edu.co.appnotiufps.services;

import android.Manifest;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Looper;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.NotificationCompat;
import java.util.ArrayList;
import ufps.edu.co.appnotiufps.R;
import ufps.edu.co.appnotiufps.activitys.secuendarias.ViewEvento;
import ufps.edu.co.appnotiufps.bd.dao.EventoDAO;
import ufps.edu.co.appnotiufps.bd.dao.NotificacionDAO;
import ufps.edu.co.appnotiufps.bd.dto.EventoDTO;

import static java.lang.Thread.sleep;

public class Servicio extends Service implements Runnable, LocationListener {

   //private int distanciaRango = 300;
    //private double longitud = -72.487597;
    //private double latitud = 7.898177;
    private int distanciaRango = 20;
    private double longitud = -72.505460;
    private double latitud = 7.917908;
    private Location location;
    //definimos el objeto de la clase SQLiteOpenHelper
    private  NotificacionDAO db;
    //definimos el tiempo que tarda en hacer la sincronizacion de los eventos
    private static long tiempo= 15000;


    public Servicio(){
        super();
        //creamos el objeto location que hace referencia a la localizacion de la ufps
        this.location = new Location(LocationManager.GPS_PROVIDER);
        this.location.setLatitude(this.latitud);
        this.location.setLongitude(this.longitud);
    }


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public void onCreate() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Thread hilo = new Thread(this);
        hilo.start();

        //START_STICKY: Crea de nuevo el servicio después de haber sido destruido por el sistema.
        // En este caso llamará a onStartCommand() referenciando un intent nulo.

        //START_REDELIVER_INTENT: Crea de nuevo el servicio si el sistema lo destruyó.
        // A diferencia de START_STICKY, esta vez sí se retoma el último intent que recibió el servicio.
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        System.out.println("servicio destruido");
    }


    @Override
    public void run() {
        while (true) {
            try {
                db=new NotificacionDAO(this);
                sleep(tiempo);
                buscarEventos();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void crearNotificacion(EventoDTO evento) {

        //Construccion de la notificacion;
        NotificationCompat.Builder notificacion = new NotificationCompat.Builder(this);
        //icono pequeño de la notificacion
        notificacion.setSmallIcon(R.drawable.icono_noti_ufps_mono);
        //Configuracion la accion al pulsar sobre la notificacion
        notificacion.setContentIntent(this.pasarActivity(evento.getUrl()));
        //elimina la notificacion luego de abrir esta
        notificacion.setAutoCancel(true);
        //icono grande la notificacion
        notificacion.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.icono_noti_ufps));
        //titulo de la notificacion
        notificacion.setContentTitle(evento.getTitulo());
        notificacion.setContentText("Inicio: " + evento.getFecha_ini());
        notificacion.setSubText(evento.getDescripcion());
        //direccion del sonido de notificacion
        Uri sonido = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        //se utiliza el sonido predeterminado para notificaciones
        notificacion.setSound(sonido);
        //Enciende el led de la notificacion
        notificacion.setLights(Color.RED, 500, 3000);
        //vibra el celular al llegar la notificacion
        notificacion.setVibrate(new long[]{500, 500});
        //Enviar la notificacion al movil
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        //le indicamos la id del evento a la notificacion
        notificationManager.notify(evento.getId(), notificacion.build());

    }


    //metodo que busca los eventos en la base de datos
    private void buscarEventos() {
        SharedPreferences config = this.getSharedPreferences("preferencias", Context.MODE_PRIVATE);
        boolean isGPS=config.getBoolean("checkGPS",false);
        boolean isNotificacion=config.getBoolean("checkNotificacion",false);

        if(isNotificacion){
            if(isGPS){
                if(this.verificarUbicacion()){
                    System.out.println("gps activado");
                    this.listarEventos();
                }else{
                    System.out.println("Ubicacion fuera del rango");
                }
            }else{
                System.out.println("solo notificaciones activadas");
               this.listarEventos();
            }
        }else{
            System.out.println("notificaciones desactivadas");
        }

    }

    private void listarEventos(){
        try {
            ArrayList<EventoDTO> eventos = new EventoDAO().listarEventosNotificaciones(this);
            for (EventoDTO x : eventos) {
                //verfica si la notificacion ya fue enviado al movil, en caso de que no
                //inserta la notificacion a la bd sqlite y envia la notificacion
               if(this.db.insetarNotificacion(x.getId())){
                    this.crearNotificacion(x);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //metodo para crear una activity al tocar la notificacion sobre el evento
    private PendingIntent pasarActivity(String url_evento) {
        Intent activity = new Intent(this.getBaseContext(), ViewEvento.class);
        //mandamos a la otra activity la url del evento
        SharedPreferences config =getSharedPreferences("preferencias", Context.MODE_PRIVATE);
        SharedPreferences.Editor editar = config.edit();
        editar.putString("url_temp",url_evento);
        editar.commit();

        PendingIntent viewEvento = PendingIntent.getActivity(this.getBaseContext(), 0, activity, 0);
        return viewEvento;
    }


    //Este metodo se encarga de validar si la ubicacion del dispositivo corresponde a un rango de
    //de ubicacion cercano a la unicacion de la UFPS
    private boolean verificarUbicacion() {
        //obtenemos el objeto location con la ubicacion del movil
        Location ubicacion=this.obtenerUbicacion();
        if(ubicacion!=null){
            //distancia entre dos coordenadas en el mapa
            float distancia=ubicacion.distanceTo(this.location);
            //verifica que la distancia entre la ubicacion de la universidad y el movil sea menor
            //a la del rango preestablecido
            System.out.println("distacia entre las dos coordenadas: "+distancia+" metros");
            if(this.distanciaRango>0 && this.distanciaRango>=distancia){
                return true;
            }
        }
        return false;
    }

    private Location obtenerUbicacion() {
        Location ubicacion=null;

        try {
            //Creo el locationManager para manejar el servicio de ubicacion
            LocationManager locationManager = (LocationManager) this.getSystemService(LOCATION_SERVICE);
            boolean gpsActivo = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            boolean redActiva = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            //esta condicion es valida si los permisos estan activados correctamente
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) !=
                    PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission
                    (this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            }

            if(gpsActivo){
                //se manda la peticcion al sistema  para que el movil busque la ubicacion por GPS
                locationManager.requestSingleUpdate(LocationManager.GPS_PROVIDER, this, Looper.getMainLooper());
                //dormimos al proceso por 15 segundos con el fin de que cargue la ubicacion GPS actual
                sleep(15000);
                //obtine la ubicacion de locationManager y lo guarda en un objeto de tipo Location
                    ubicacion=locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
             //verifica si el servicio de ubicacion por red esta disponible
            }else if(redActiva){
                //se manda la peticcion al sistema  para que el movil busque la ubicacion por GPS
                locationManager.requestSingleUpdate(LocationManager.NETWORK_PROVIDER, this, Looper.getMainLooper());
                //obtine la ubicacion de locationManager y lo guarda en un objeto de tipo Location
                //dormimos al proceso por 15 segundos con el fin de que cargue la ubicacion GPS actual
                sleep(15000);
                ubicacion=locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            }

        }catch (Exception e){
            e.printStackTrace();
        }
        return ubicacion;
    }



    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }
}
