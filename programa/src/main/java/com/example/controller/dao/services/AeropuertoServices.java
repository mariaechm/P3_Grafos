/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.controller.dao.services;
import com.example.controller.dao.AeropuertoDao;
import com.example.controller.tda.graph.GraphLabelNoDirect;
import com.example.controller.tda.list.LinkedList;
import com.example.models.Aeropuerto;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

/**
 *
 * @author maria-chuico
 */

public class AeropuertoServices {
    private AeropuertoDao obj;

    public AeropuertoServices(){
        obj = new AeropuertoDao();
    }

    public Boolean save() throws Exception{
        return obj.save();
    }

    public LinkedList<Aeropuerto> listAll() {
        return obj.getListAll();
    }

    public Aeropuerto getAeropuertos() {
        return obj.getAeropuertos();
    }

    public void setAeropuertos(Aeropuerto aeropuertos) {
        obj.setAeropuertos(aeropuertos);
    }

    public Aeropuerto get(Integer id) throws Exception{
        return obj.get(id);
    }

    public Boolean update() throws Exception{
        return obj.update();
    }

    public Boolean delete(Integer id) throws Exception{
        return obj.delete(id);
    }

    public String caminoMasCorto(int algorithm, int origin, int destiny) throws Exception{
        return obj.caminoShort(origin, destiny, algorithm);
    }

    public JsonArray obtenerPesos() throws Exception {
        return obj.obtenerWeights();
    }

    public JsonObject vistaGrafo() throws Exception {
        return obj.getGraphData();
    }
    public GraphLabelNoDirect <String> crearGrafo() throws Exception {
        return obj.crearGraph();
    }

    public void guardargrafo( ) throws Exception {
        obj.saveGraph();
    }

    public GraphLabelNoDirect <String> ramdomAdyacencias() throws Exception {
        return obj.conexionesRandom();
    }

    public String busquedaAnchura(int origen) throws Exception {
        return obj.busquedaAnchura(origen);
    }
}
