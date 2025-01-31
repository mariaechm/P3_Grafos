package com.example.models;

/**
 *
 * @author maria-chuico
 */

public class Aeropuerto {
    private Integer id;
    private String nombre;
   // private String ciudad;
    private Double longitud;
    private Double latitud;


    //getters y setters

    public Aeropuerto() {
    }

    

    public String getNombre() {
        return this.nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

   /* public String getCiudad() {
        return this.ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }*/

    public Double getLongitud() {
        return this.longitud;
    }

    public void setLongitud(Double longitud) {
        this.longitud = longitud;
    }

    public Double getLatitud() {
        return this.latitud;
    }

    public void setLatitud(Double latitud) {
        this.latitud = latitud;
    }

    public Aeropuerto (Integer id, String nombre, Double longitud, Double latitud){
        this.id = id;
        this.nombre = nombre;
        //this.ciudad = ciudad;
        this.longitud = longitud;
        this.latitud = latitud;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
