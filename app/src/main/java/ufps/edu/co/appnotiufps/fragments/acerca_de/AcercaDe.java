package ufps.edu.co.appnotiufps.fragments.acerca_de;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Date;

import ufps.edu.co.appnotiufps.R;

/**
 * Created by omara on 18/11/2016.
 */

public class AcercaDe extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.acerca_de, container, false);
        Date date=new Date();
        ((TextView)view.findViewById(R.id.fecha_acerca_de)).setText(""+(date.getYear()+1900));

        //activar evento del boton
        ((FloatingActionButton)view.findViewById(R.id.btn_enviar_email)).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //redirigimos al usuario a su cliente de correo y le indicamos
                        // el correo de soporte tecnico y el asunto del correo
                        Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                                "mailto", "omaarturocv@ufps.edu.co", null));
                        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Soporte Tecnico NotiUFPS");
                        startActivity(Intent.createChooser(emailIntent, null));
                    }
                }
        );
        return view;
    }


}
