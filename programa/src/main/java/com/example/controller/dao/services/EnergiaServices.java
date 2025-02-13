/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.controller.dao.services;
//import java.util.Map;

import com.example.controller.dao.EnergiaDao;
import com.example.controller.tda.list.LinkedList;
import com.example.models.Energia;

public class EnergiaServices {
    private EnergiaDao obj;

    public EnergiaServices(){
        obj = new EnergiaDao();
    }

     public Boolean save() throws Exception {
        return obj.save();
    }

    public Boolean update() throws Exception {
        return obj.update();
    }

    public LinkedList<Energia> listAll() {
        return obj.getListAll();
    }
    
    public Energia getEnergia() {
        return obj.getEnergia();
    }

    public void setEnergia(Energia energia) {
        obj.setEnergia(energia);
    }

    public Energia get(Integer id) throws Exception {
        return obj.get(id);
    }
    
    // Método para calcular el camino corto entre dos habitaciones usando el algoritmo seleccionado
    public String calcularCaminoCorto(int origen, int destino, int algoritmo) throws Exception {
        return obj.caminoCorto(origen, destino, algoritmo);
    }
}

