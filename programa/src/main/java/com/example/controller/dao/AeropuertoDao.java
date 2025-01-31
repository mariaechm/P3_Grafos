package com.example.controller.dao;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
    
import com.example.models.Aeropuerto;
import com.example.controller.tda.list.LinkedList;

import java.util.HashMap;
import java.util.Map;

import com.example.controller.dao.implement.AdapterDao;
import com.example.controller.tda.graph.Graph;
import com.example.controller.tda.graph.GraphLabelNoDirect;

import com.example.controller.tda.graph.algoritmos.BusquedaAnchura;
import com.example.controller.tda.graph.algoritmos.BellmanFord;
import com.example.controller.tda.graph.algoritmos.Floyd;

/**
 *
 * @author maria-chuico
 */

public class AeropuertoDao extends AdapterDao<Aeropuerto> {

    private Aeropuerto aeropuertos;
    private LinkedList<Aeropuerto> listAll;
    private GraphLabelNoDirect<String> graph;
    private LinkedList<String> verticeName; 
    private String archivo = "AeropuertoGrafo.json";


    public AeropuertoDao() {
        super(Aeropuerto.class);
    }

    public Aeropuerto getAeropuertos(){
        if (aeropuertos == null) {
            aeropuertos = new Aeropuerto();
        }
        return this.aeropuertos;
    }

    public void setAeropuertos(Aeropuerto aeropuertos){
        this.aeropuertos = aeropuertos;
    }

    public LinkedList<Aeropuerto> getListAll(){
        if (listAll == null) {
            listAll = new LinkedList<>();
        }
        return this.listAll;
    }

    public Boolean save() throws Exception {
        Integer id = getListAll().getSize() + 1;
        getAeropuertos().setId(id);
        persist(getAeropuertos());
        return true;
    }

    public Boolean update() throws Exception {
        this.merge(getAeropuertos(), getAeropuertos().getId() - 1);
        this.listAll = listAll();
        return true;
    }

    public Boolean delete(Integer id) throws Exception {
        for (int i = 0; i < getListAll().getSize(); i++) {
            Aeropuerto pro = getListAll().get(i);
            if (pro.getId().equals(id)) {
                getListAll().delete(i);
                return true;
            }
        }
        throw new Exception("Aeropuerto no encontrado " + id);
    }

    public GraphLabelNoDirect<String> crearGraph() {
        if (verticeName == null) {
            verticeName = new LinkedList<>();
        }
        LinkedList<Aeropuerto> list = this.getListAll();
        if (!list.isEmpty()) {
           if (graph == null) {
                System.out.println("Grafo " + graph);
                graph = new GraphLabelNoDirect<>(list.getSize(), String.class);
           }

           Aeropuerto[] aeropuertos = list.toArray();
           for(int i = 0; i < aeropuertos.length; i++){
            this.graph.labelsVerticeL(i + 1, aeropuertos[i].getNombre());
            System.out.println("Vertices " + verticeName);

            verticeName.add(aeropuertos[i].getNombre());
           }
           this.graph.drawGraph();
        }
        System.out.println("Grafo creado" + graph);
        return this.graph;
    }

    //Save Graph en un archivo
    public void saveGraph() throws Exception {
        this.graph.guardarGraphLabel(archivo);
    }

    public JsonArray obtenerWeights() throws Exception { //Obtener los pesos de los vertices
        if (graph == null){
            crearGraph();
        }
        
        if (graph.existeArchivo(archivo)){
            graph.cargarModelosDesdeDao();
            graph.loadGraph(archivo);

            JsonArray graphData = graph.obtenerWeights();
            System.out.println("Modelo cargado " + graphData);
            return graphData;
        }else {
            throw new Exception("No se ha creado el archivo");  
        }
    }

    public JsonObject getGraphData() throws Exception {
        if (graph == null){
            crearGraph();
        }

        if (graph.existeArchivo(archivo)){
            graph.cargarModelosDesdeDao();
            graph.loadGraph(archivo);

            //Tipo de dato sea el correcto
            JsonObject graphData = graph.vistaGraphData();
            System.out.println("Modelo cargado " + graphData);
            return graphData;
        }else{
            throw new Exception("No se ha creado el archivo");
        }
    } 

    public GraphLabelNoDirect<String> conexionesRandom() throws Exception {
        if (graph == null){
            crearGraph();
        }
        
        if (graph.existeArchivo(archivo)){
            graph.cargarModelosDesdeDao();
            graph.loadGraph(archivo);
            System.out.println("Modelo asociado a: " + archivo);     
    }else{
        throw new Exception("No se ha creado el archivo");
    }
    saveGraph();
    return graph;
    }

    public String busquedaAnchura (int origin) throws Exception {
        if (graph == null) {
            throw new Exception("No se ha creado el grafo");
        }

        //Create la instancia de busqueda en anchura con el grahp el node de origen

        busquedaAnchura  busqAnchuraAlgoritmo = new busquedaAnchura(graph, origin);

        String recorrido = busqAnchuraAlgoritmo.recorridoGraph();
        return recorrido;
    }

    public String caminoShort(int origin, int destiny, int algorithm) throws Exception {
        if (graph == null){
            throw new Exception("No se ha creado el grafo");
        }
        System.out.println("Calculando camino mas corto desde " + origin + " hasta " + destiny);

         Map<String, Object> resultado = new HashMap<>();

        if (algorithm == 1){
            BellmanFord bellmanFord = new BellmanFord(graph, origin, destiny);
            camino = bellmanFord.caminoShort();
        }else{
            Floyd floyd = new Floyd(graph, origin, destiny);
            camino = floyd.caminoShort();
        }

        System.out.println("Camino mas corto es: " + camino);
        return camino; //Retorna el camino mas corto calculado
    }
}