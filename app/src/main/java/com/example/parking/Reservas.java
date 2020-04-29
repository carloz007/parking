package com.example.parking;

import java.io.Serializable;

public class Reservas implements Serializable {
    //Enviar, guardar y traer informacion
    private String cod_upc;
    private Integer id_estacionamiento, id_sede;
    private String date_start, time_start, time_end;

    public String getCod_upc() {
        return cod_upc;
    }

    public void setCod_upc(String cod_upc) {
        this.cod_upc = cod_upc;
    }

    public Integer getId_estacionamiento() {
        return id_estacionamiento;
    }

    public void setId_estacionamiento(Integer id_estacionamiento) {
        this.id_estacionamiento = id_estacionamiento;
    }

    public Integer getId_sede() {
        return id_sede;
    }

    public void setId_sede(Integer id_sede) {
        this.id_sede = id_sede;
    }

    public String getDate_start() {
        return date_start;
    }

    public void setDate_start(String date_start) {
        this.date_start = date_start;
    }

    public String getTime_start() {
        return time_start;
    }

    public void setTime_start(String time_start) {
        this.time_start = time_start;
    }

    public String getTime_end() {
        return time_end;
    }

    public void setTime_end(String time_end) {
        this.time_end = time_end;
    }

    public Reservas (String cod_upc, Integer id_estacionamiento, Integer id_sede,
                     String date_start, String time_start, String time_end){
        this.cod_upc = cod_upc;
        this.id_estacionamiento = id_estacionamiento;
        this.id_sede = id_sede;
        this.date_start = date_start;
        this.time_start = time_start;
        this.time_end = time_end;

    }


}
