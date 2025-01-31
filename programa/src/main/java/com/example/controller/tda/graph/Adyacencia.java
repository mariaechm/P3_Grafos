/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.controller.tda.graph;

/**
 *
 * @author maria-chuico
 */

public class Adyacencia {
    private Integer destination;
    private Float weight;
    
    public Adyacencia(Integer destination, Float weight) {
        this.destination = destination;
        this.weight = weight; //peso
    }

    public Adyacencia(){}

    public Integer getDestination() {
        return this.destination;
    }

    public void setDestination(Integer destination) {
        this.destination = destination;
    }

    public Float getWeight() {
        return this.weight;
    }

    public void setWeight(Float weight) {
        this.weight = weight;
    }

    @Override
    public String toString() {
        return "Adyacencia{" + "destination=" + destination + ", weight=" + weight + '}';
    }
}

