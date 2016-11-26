package ufps.edu.co.appnotiufps.fragments.inicio;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.loopj.android.image.SmartImageView;

import java.util.ArrayList;

import ufps.edu.co.appnotiufps.R;
import ufps.edu.co.appnotiufps.bd.dto.EventoDTO;

/**
 * Created by omara on 20/11/2016.
 */

public class EventoAdapterList extends ArrayAdapter<EventoDTO> {
    private final Activity context;
    private final ArrayList<EventoDTO> eventoDTOs;
    private String url_server;
    public EventoAdapterList(Activity context, ArrayList<EventoDTO> eventoDTOs) {
        super(context, R.layout.fila_evento, eventoDTOs);
        this.context = context;
        this.eventoDTOs = eventoDTOs;
        //cargamos la url del server
        SharedPreferences config = this.context.getSharedPreferences("preferencias", Context.MODE_PRIVATE);
        this.url_server= config.getString("url_server", "");
    }

    public View getView(int posicion, View view, ViewGroup parent){

        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.fila_evento,null,true);

        TextView titulo = (TextView) rowView.findViewById(R.id.titulo_evento);
        SmartImageView imagenView = (SmartImageView) rowView.findViewById(R.id.img_evento);
        TextView fechaIni= (TextView) rowView.findViewById(R.id.fecha_ini);
        TextView fechaFinal= (TextView) rowView.findViewById(R.id.fecha_final);
        TextView tipoEvento= (TextView) rowView.findViewById(R.id.tipo_evento);

        titulo.setText(this.eventoDTOs.get(posicion).getTitulo());

        //con la libreria SmartImageView cargo las imagenes desde una url
        imagenView.setImageUrl(this.url_server+this.eventoDTOs.get(posicion).getUrl_img());


        fechaIni.setText("Fecha Inicio: "+this.eventoDTOs.get(posicion).getFecha_ini());
        fechaFinal.setText("Fecha Final: "+this.eventoDTOs.get(posicion).getFecha_fin());
        tipoEvento.setText("Tipo: "+this.eventoDTOs.get(posicion).getTipo());

        return rowView;
    }

}
