package ufps.edu.co.appnotiufps;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.image.SmartImageView;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by omara on 20/11/2016.
 */

public class EventoAdapterList extends ArrayAdapter<Evento> {
    private final Activity context;
    private final ArrayList<Evento> eventos;
    private String url_server;
    public EventoAdapterList(Activity context, ArrayList<Evento> eventos) {
        super(context,R.layout.fila_evento, eventos);
        this.context = context;
        this.eventos = eventos;
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

        titulo.setText(this.eventos.get(posicion).getTitulo());

        //con la libreria SmartImageView cargo las imagenes desde una url
        imagenView.setImageUrl(this.url_server+this.eventos.get(posicion).getUrl_img());


        fechaIni.setText("Fecha Inicio: "+this.eventos.get(posicion).getFecha_ini());
        fechaFinal.setText("Fecha Final: "+this.eventos.get(posicion).getFecha_fin());
        tipoEvento.setText("Tipo: "+this.eventos.get(posicion).getTipo());

        return rowView;
    }

}
