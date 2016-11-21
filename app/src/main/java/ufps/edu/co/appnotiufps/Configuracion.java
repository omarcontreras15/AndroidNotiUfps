package ufps.edu.co.appnotiufps;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.Switch;
import android.widget.Toast;

import java.util.ArrayList;

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
        return this.view;
    }


    private void eventoBtnGuardar(){
        FloatingActionButton btnGuardar=(FloatingActionButton)this.view.findViewById(R.id.btn_guardar);
        btnGuardar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                    guardarPreferencias();
                    Toast.makeText(getActivity(),"Preferencias Guardadas",Toast.LENGTH_LONG).show();
                    Intent activity=new Intent(getActivity(),Inicio.class);
                    startActivity(activity);
            }
        });
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

    }
}
