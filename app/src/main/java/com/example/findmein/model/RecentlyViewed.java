package com.example.findmein.model;

import com.example.findmein.R;
import com.squareup.picasso.Picasso;

public class RecentlyViewed {

    String titulo,description,recompensa,categoria,fecha,bigimageurl,latitud,longitud;

    public RecentlyViewed(String titulo, String description, String recompensa,String categoria,String fecha, String latitud,String longitud, String bigimageurl) {
        this.titulo = titulo;
        this.description = description;
        this.recompensa = recompensa;
        this.categoria = categoria;
        this.fecha = fecha;
        this.latitud = latitud;
        this.longitud = longitud;
        this.bigimageurl = bigimageurl;
    }

    public String getBigimageurl() {
        return bigimageurl;
    }
    public void setBigimageurl(String bigimageurl) {this.bigimageurl = bigimageurl; this.bigimageurl = this.bigimageurl + "?type=large";}

    public String getTitulo() {
        return titulo;
    }
    public void setTitulo(String Titulo) {
        this.titulo = titulo;
    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    public String getRecompensa() {
        return recompensa;
    }
    public void setRecompensa(String recompensa) {
        this.recompensa = recompensa;
    }

    public String getCategoria() {
        return categoria;
    }
    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getFecha() {
        return fecha;
    }
    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getLatitud() {
        return latitud;
    }
    public void setLatitud(String latitud) {
        this.latitud = latitud;
    }

    public String getLongitud() {
        return longitud;
    }
    public void setLongitud(String longitud) {
        this.longitud = longitud;
    }

}
