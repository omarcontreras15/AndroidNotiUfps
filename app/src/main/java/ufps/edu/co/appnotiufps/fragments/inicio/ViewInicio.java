package ufps.edu.co.appnotiufps.fragments.inicio;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import ufps.edu.co.appnotiufps.bd.dto.EventoDTO;
import ufps.edu.co.appnotiufps.bd.dao.EventoDAO;
import ufps.edu.co.appnotiufps.R;
import ufps.edu.co.appnotiufps.activitys.secuendarias.ViewEvento;

/**
 * Created by omara on 19/11/2016.
 */

public class ViewInicio extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    private View view;
    private  String url_server;
    private ArrayList<EventoDTO> eventoDTOs;
    private SwipeRefreshLayout  mSwipeRefreshLayout;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        SharedPreferences config = getActivity().getSharedPreferences("preferencias", Context.MODE_PRIVATE);
        this.url_server= config.getString("url_server", "");
        this.view=inflater.inflate(R.layout.content_inicio, container, false);
        this.cargarEventos();
        //cargamos el layoutRefresh que sirve para refrescar un fragment o activity
        this.mSwipeRefreshLayout = (SwipeRefreshLayout) this.view.findViewById(R.id.content_inicio);
        //Activamos la funcion de refrescar en el layaout
        this.mSwipeRefreshLayout.setOnRefreshListener(this);
        this.mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary,R.color.colorAccent);
        //
        return this.view;
    }


    private void cargarEventos(){
        EventoDAO prueba=new EventoDAO();
        try {
            this.eventoDTOs = prueba.listarAllEventos();
            EventoAdapterList eventoAdapterList= new EventoAdapterList(getActivity(),this.eventoDTOs);
            ListView lista=(ListView)this.view.findViewById(R.id.lista_eventos);
           lista.setAdapter(eventoAdapterList);
            lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int posicion, long id) {
                    pasarActivity(eventoDTOs.get(posicion).getUrl(),ViewEvento.class);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //Este metodo me permite cargar una imagen desde una url y convertirla a bitmap
    private Bitmap cargarImagen(String url){
        Bitmap imagen=null;
        try {
            URL imageUrl = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) imageUrl.openConnection();
            conn.connect();
            imagen = BitmapFactory.decodeStream(conn.getInputStream());
            conn.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return imagen;
    }

    //funcion que se implementa de la interface OnRefreshListener necesaria para que el layaout
    //refresh funcione
    @Override
    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override public void run() {
                cargarEventos();
                mSwipeRefreshLayout.setRefreshing(false);
            }
        }, 1500);
    }

    //metodo para pasar a una siquiete activity
    private void pasarActivity(String url_evento,Class clase){
        Intent activity=new Intent(getActivity(),clase);
        //mandamos a la otra activity la url del evento
        SharedPreferences config =getActivity().getSharedPreferences("preferencias", Context.MODE_PRIVATE);
        SharedPreferences.Editor editar = config.edit();
        editar.putString("url_temp",url_evento);
        editar.commit();
        startActivity(activity);
    }

}
