package ufps.edu.co.appnotiufps.fragments.configuracion;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Vibrator;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import ufps.edu.co.appnotiufps.R;
import ufps.edu.co.appnotiufps.activitys.principal.Inicio;

import static android.content.Context.LOCATION_SERVICE;

/**
 * Created by omara on 16/11/2016.
 */

public class Configuracion extends Fragment {
    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        this.view=inflater.inflate(R.layout.configuracion, container, false);
        this.cargarPreferencias();
        this.eventoBtnGuardar();
        this.eventoSwitchGps();
        this.eventoSwitchNotificacion();
        return this.view;
    }


    private void eventoBtnGuardar(){
        FloatingActionButton btnGuardar=(FloatingActionButton)this.view.findViewById(R.id.btn_guardar);
        btnGuardar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                    guardarPreferencias();
                    //Mostramos un msj en Toast notificando que las preferencias se guardaron
                    Toast.makeText(getActivity(),"Preferencias Guardadas",Toast.LENGTH_LONG).show();
                    //Mandamos a vibrar el movil
                    Vibrator vibrador=(Vibrator)getActivity().getSystemService(Context.VIBRATOR_SERVICE);
                    vibrador.vibrate(250);
                    //Reiniciamos la actividad para reedirigirlo a la pagina de inicio
                    Intent activity=new Intent(getActivity(),Inicio.class);
                    startActivity(activity);
            }
        });
    }

    private void eventoSwitchGps(){

        ((Switch)this.view.findViewById(R.id.checkGPS)).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(final CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    //verifica si el servicio de gps esta activado
                    LocationManager locationManager = (LocationManager)view.getContext().getSystemService(LOCATION_SERVICE);
                    boolean gpsActivo = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
                    locationManager=null;
                    //Verifica si el gps esta desactivado
                    if (!gpsActivo) {
                        final AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                        builder.setMessage("El sistema GPS esta desactivado, tener el GPS activado es necesario " +
                                "para el funcionaiento " +
                                "de las notificaciones por GPS \n ¿Desea activarlo?  " )
                                .setCancelable(false)
                                .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog,
                                                        @SuppressWarnings("unused") final int id) {
                                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                                        //activa el switch de notificaciones
                                        ((Switch)view.findViewById(R.id.checkNotifiacion)).setChecked(true);
                                    }
                                })
                                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    public void onClick(final DialogInterface dialog,
                                                        @SuppressWarnings("unused") final int id) {
                                        dialog.cancel();
                                        buttonView.setChecked(false);
                                    }
                                });
                       AlertDialog alert = builder.create();
                        alert.show();
                    }else{
                        //activa el switch de notificaciones
                        ((Switch)view.findViewById(R.id.checkNotifiacion)).setChecked(true);
                    }
                }
            }
        });
    }

    //este metodo desactiva al switch del gps al momento de desactivar el switch de
    //notificacion
    private void eventoSwitchNotificacion(){

        ((Switch)this.view.findViewById(R.id.checkNotifiacion)).setOnCheckedChangeListener(
                new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(final CompoundButton buttonView, boolean isChecked) {
                if(!isChecked){
                    ((Switch)view.findViewById(R.id.checkGPS)).setChecked(false);
                }
            }});
    }
    private void guardarPreferencias(){

        SharedPreferences config = getActivity().getSharedPreferences("preferencias", Context.MODE_PRIVATE);
        SharedPreferences.Editor editar = config.edit();

        editar.putBoolean("checkAcademico",((CheckBox)this.view.findViewById(R.id.checkAcademico)).isChecked());
        editar.putBoolean("checkCultural",((CheckBox)this.view.findViewById(R.id.checkCultural)).isChecked());
        editar.putBoolean("checkDeportivo",((CheckBox)this.view.findViewById(R.id.checkDeportivo)).isChecked());
        editar.putBoolean("checkCongresos",((CheckBox)this.view.findViewById(R.id.checkCongresos)).isChecked());
        editar.putBoolean("checkEmpresarial",((CheckBox)this.view.findViewById(R.id.checkEmpresarial)).isChecked());
        editar.putBoolean("checkAdministrativo",((CheckBox)this.view.findViewById(R.id.checkAdministrativo)).isChecked());
        editar.putBoolean("checkCiencia_Tecnologia",((CheckBox)this.view.findViewById(R.id.checkCiencia_Tecnologia)).isChecked());
        editar.putBoolean("checkGPS",((Switch)this.view.findViewById(R.id.checkGPS)).isChecked());
        editar.putBoolean("checkNotificacion",((Switch)this.view.findViewById(R.id.checkNotifiacion)).isChecked());
        editar.commit();
    }

    private void cargarPreferencias(){
        SharedPreferences config = getActivity().getSharedPreferences("preferencias", Context.MODE_PRIVATE);
        boolean academico = config.getBoolean("checkAcademico", false);
        ((CheckBox)this.view.findViewById(R.id.checkAcademico)).setChecked(academico);

        boolean cultural = config.getBoolean("checkCultural", false);
        ((CheckBox)this.view.findViewById(R.id.checkCultural)).setChecked(cultural);

        boolean deportivo = config.getBoolean("checkDeportivo", false);
        ((CheckBox)this.view.findViewById(R.id.checkDeportivo)).setChecked(deportivo);

        boolean congresos= config.getBoolean("checkCongresos", false);
        ((CheckBox)this.view.findViewById(R.id.checkCongresos)).setChecked(congresos);

        boolean empresarial= config.getBoolean("checkEmpresarial", false);
        ((CheckBox)this.view.findViewById(R.id.checkEmpresarial)).setChecked(empresarial);

        boolean administrativo=config.getBoolean("checkAdministrativo",false);
        ((CheckBox)this.view.findViewById(R.id.checkAdministrativo)).setChecked(administrativo);

        boolean ciencia_tecnologia= config.getBoolean("checkCiencia_Tecnologia", false);
        ((CheckBox)this.view.findViewById(R.id.checkCiencia_Tecnologia)).setChecked(ciencia_tecnologia);

        boolean GPS=config.getBoolean("checkGPS",false);
        ((Switch)this.view.findViewById(R.id.checkGPS)).setChecked(GPS);

        boolean notificacion=config.getBoolean("checkNotificacion",false);
        ((Switch)this.view.findViewById(R.id.checkNotifiacion)).setChecked(notificacion);

    }
}
