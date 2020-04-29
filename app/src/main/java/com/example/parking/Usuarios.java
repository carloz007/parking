package com.example.parking;

import java.io.Serializable;

public class Usuarios implements Serializable {
    private String cod_upc, clave, nombre, apellido, correo, foto, telefono;
    private Integer estado;

    public Usuarios(String cod_upc, String clave, String nombre, String apellido, String correo,
                    String foto, String telefono, Integer estado) {
        this.cod_upc = cod_upc;
        this.clave = clave;
        this.nombre = nombre;
        this.apellido = apellido;
        this.correo = correo;
        this.foto = foto;
        this.telefono = telefono;
        this.estado = estado;
    }

    public String getCod_upc() {
        return cod_upc;
    }

    public void setCod_upc(String cod_upc) {
        this.cod_upc = cod_upc;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public Integer getEstado() {
        return estado;
    }

    public void setEstado(Integer estado) {
        this.estado = estado;
    }
}
