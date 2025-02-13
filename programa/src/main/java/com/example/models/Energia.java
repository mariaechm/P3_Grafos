package com.example.models;

/**
 *
 * @author maria-chuico
 */

 public class Energia {
    private Integer id;
    private String ip;
    private String nombre;
    private String marca;
    private Double velocidadAlta;
    private Double velocidadBaja;
    
    public String getMarca() {
        return marca;
    }
    public void setMarca(String marca) {
        this.marca = marca;
    }
    public Energia(){

    }
    public Energia (Integer id, String ip, String nombre, Double velocidadAlta, Double velocidadBaja){
        this.id = id;
        this.ip = ip;
        this.nombre = nombre;
        this.velocidadAlta = velocidadAlta;
        this.velocidadBaja = velocidadBaja;
    }

    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public String getIp() {
        return ip;
    }
    public void setIp(String ip) {
        this.ip = ip;
    }
    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    public Double getVelocidadAlta() {
        return velocidadAlta;
    }
    public void setVelocidadAlta(Double velocidadAlta) {
        this.velocidadAlta = velocidadAlta;
    }
    public Double getVelocidadBaja() {
        return velocidadBaja;
    }
    public void setVelocidadBaja(Double velocidadBaja) {
        this.velocidadBaja = velocidadBaja;
    }
}

