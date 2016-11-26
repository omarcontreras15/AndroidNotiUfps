package ufps.edu.co.appnotiufps.bd.dto;

/**
 * Created by omara on 16/11/2016.
 */

public class EventoDTO {
    private int id;
    private String titulo;
    private String descripcion;
    private String url;
    private String tipo;
    private String fecha_ini;
    private String fecha_fin;
    private String url_img;

    public EventoDTO(int id,String titulo, String descripcion, String url, String tipo, String fecha_ini, String fecha_fin, String url_img) {
        this.id=id;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.url = url;
        this.fecha_ini = fecha_ini;
        this.tipo = tipo;
        this.fecha_fin = fecha_fin;
        this.url_img = url_img;
    }

    public int getId() {
        return id;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public String getUrl() {
        return url;
    }

    public String getTipo() {
        return tipo;
    }

    public String getFecha_ini() {
        return fecha_ini;
    }

    public String getFecha_fin() {
        return fecha_fin;
    }

    public String getUrl_img() {
        return url_img;
    }
}
