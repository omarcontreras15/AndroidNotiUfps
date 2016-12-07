package ufps.edu.co.appnotiufps.services;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by omara on 6/12/2016.
 */


public class AutoArranque extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        //Iniciamos el servicio en segundo plano
        Servicio servicio=new Servicio();
        context.startService(new Intent(context, Servicio.class));
    }

}
