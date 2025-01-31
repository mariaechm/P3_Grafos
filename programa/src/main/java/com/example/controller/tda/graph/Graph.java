/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.controller.tda.graph;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import static java.lang.Math.toRadians;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import com.example.controller.dao.AeropuertoDao;
import com.example.controller.exception.ListEmptyException;
import com.example.controller.tda.list.LinkedList;
import com.example.models.Aeropuerto;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

/**
 *
 * @author maria-chuico
 */

//import java.util.LinkedList;

public abstract class Graph {

    //Ruta del archivo donde se guardara 
    public static String filePath = "media/";

    private Map<Integer, Aeropuerto> verticeModels = new HashMap<>();

    public abstract Integer nro_vertices (); //numero de vertices
    public abstract Integer nro_edges();
    public abstract Boolean is_edge(Integer v1, Integer v2) throws Exception;
    public abstract Float wiegth_edge(Integer v1, Integer v2)throws Exception;
    public abstract void add_edge(Integer v1, Integer v2)throws Exception;
    public abstract void add_edge(Integer v1, Integer v2, Float wiegth) throws Exception;
    public abstract LinkedList<Adyacencia> adyacencias(Integer vi); //lista de adyacencias por cada vertice

    public Integer obtenerVertex (Integer label) throws Exception {  //obtener vertice 
        return label;
    }

    @Override
    public String toString() {
        StringBuilder grafo = new StringBuilder();
        try {
            for (int i = 1; i <= this.nro_vertices(); i++) {
                grafo.append("Vertice: ").append(i).append("\n");
                LinkedList<Adyacencia> lista = this.adyacencias(i);
                if (!lista.isEmpty()) {
                    Adyacencia[] ady = lista.toArray();
                    for (Adyacencia a : ady) {
                        grafo.append("ady: V").append(a.getDestination())
                             .append(" weight: ").append(a.getWeight()).append("\n");
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return grafo.toString();
    }

    //  guardar el grafo etiquetado en formato JSON
    public void guardarGraphLabel(String filename) throws Exception {
        JsonArray graphArray = new JsonArray();
        for (int i = 1; i <= this.nro_vertices(); i++) {
            JsonObject verticeObject = new JsonObject();
            verticeObject.addProperty("labelId", this.obtenerVertex(i));

            JsonArray destinationsArray = new JsonArray();
            LinkedList<Adyacencia> adyacencias = this.adyacencias(i);
            if (!adyacencias.isEmpty()) {
                for (int j = 0; j < adyacencias.getSize(); j++) {
                    Adyacencia adj = adyacencias.get(j);
                    JsonObject destinationObject = new JsonObject();
                    destinationObject.addProperty("de", this.obtenerVertex(i));
                    destinationObject.addProperty("a", adj.getDestination());
                    destinationsArray.add(destinationObject);
                }
            }
            verticeObject.add("destinos", destinationsArray);
            graphArray.add(verticeObject);
        }

        Gson gson = new Gson();
        String json = gson.toJson(graphArray);

        // Crear el directorio si no existe
        File directory = new File(filePath);
        if (!directory.exists()) {
            directory.mkdirs();
        }
        try (FileWriter fileWriter = new FileWriter(filePath + filename)) {
            fileWriter.write(json);
        }
    }

    public void cargarModelosDesdeDao() throws  ListEmptyException {
    AeropuertoDao aeropuertoDao = new AeropuertoDao();
    LinkedList<Aeropuerto> aeropuertosList = aeropuertoDao.getListAll();

    for (int i = 0; i < aeropuertosList.getSize(); i++) {
        Aeropuerto aeropuerto = aeropuertosList.get(i);
        verticeModels.put(aeropuerto.getId(), aeropuerto);
    }
}


    // Cargar el grafo desde un archivo JSON
    public void loadGraph(String filename) throws Exception {
        try (FileReader fileReader = new FileReader(filePath + filename)) {
            Gson gson = new Gson();
            JsonArray graphArray = gson.fromJson(fileReader, JsonArray.class);
    
            for (JsonElement verticeElement : graphArray) {
                JsonObject verticeObject = verticeElement.getAsJsonObject();
                Integer labelId = verticeObject.get("labelId").getAsInt();
                Aeropuerto modelo = verticeModels.get(labelId);
                if (modelo == null) {
                    continue; 
                    }
                this.addVerticeModelo(labelId, modelo);
                JsonArray destinationsArray = verticeObject.getAsJsonArray("destinos");
    
                for (JsonElement destinationElement : destinationsArray) {
                    JsonObject destinationObject = destinationElement.getAsJsonObject();
    
                    Integer from = destinationObject.get("de").getAsInt();
                    Integer to = destinationObject.get("a").getAsInt();
    
                    // Obtener los modelos de los vértices 
                    Aeropuerto modeloFrom = verticeModels.get(from);
                    Aeropuerto modeloTo = verticeModels.get(to);
                    if (modeloFrom == null || modeloTo == null) {
                    } else {
                        Float weight = (float) calcularDistance(modeloFrom, modeloTo);
                        this.add_edge(from, to, weight); 
                        System.out.println("Arista añadida de " + from + " a " + to + " con peso de: " + weight);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void clearGrafo() {
      for (int i = 1; i <= this.nro_vertices(); i++) {
        this.adyacencias(i).reset();  
       }
    }

    //Adyacencias aleatorias
    public void ramdomAdyacencias(String filename) throws Exception {
    loadGraph(filename);
    cargarModelosDesdeDao(); 
    clearGrafo();  
    Random random = new Random();
    for (int i = 1; i <= this.nro_vertices(); i++) {
        LinkedList<Adyacencia> existeEdges = this.adyacencias(i);
        int connectionsCount = existeEdges.getSize();
        
        //Cada vertice debe tener como minimo 3 conexiones
        while (connectionsCount < 3) {
            int randomVertex = random.nextInt(this.nro_vertices()) + 1;
            while (randomVertex == i || is_edge(i, randomVertex)) {
                randomVertex = random.nextInt(this.nro_vertices()) + 1;
            }

            Aeropuerto modeloFrom = verticeModels.get(i);
            Aeropuerto modeloTo = verticeModels.get(randomVertex);

            // distancia entre los dos vertices
            float weight = (float) calcularDistance(modeloFrom, modeloTo);  
            add_edge(i, randomVertex, weight);  
            connectionsCount++;
        }
    }    
    guardarGraphLabel(filename);  
}

    public void addVerticeModelo(Integer verticeId, Aeropuerto modelo) {
        verticeModels.put(verticeId, modelo);  
    }
 
    //pesos de los vertices
    public JsonArray obtenerWeights() throws Exception {
        JsonArray result = new JsonArray();
        
        // Iterar sobre los vertices
        for (int i = 1; i <= this.nro_vertices(); i++) {
            JsonObject verticeInfo = new JsonObject();
            Aeropuerto modelo = verticeModels.get(i);
            if (modelo != null) {
                verticeInfo.addProperty("name", modelo.getNombre());  
            }
            verticeInfo.addProperty("labelId", this.getVertice(i)); 

            JsonArray destinations = new JsonArray(); // Lista de conexiones para el vértice
            LinkedList<Adyacencia> adyacencias = this.adyacencias(i);

            if (!adyacencias.isEmpty()) {
                for (int j = 0; j < adyacencias.getSize(); j++) {
                    Adyacencia adj = adyacencias.get(j);
                    JsonObject destinationInfo = new JsonObject();
                    destinationInfo.addProperty("De", this.getVertice(i)); 
                    destinationInfo.addProperty("a", adj.getDestination()); 
                    destinationInfo.addProperty("peso", adj.getWeight());
                    destinations.add(destinationInfo);
                }
            }

            verticeInfo.add("destinos", destinations); 
            result.add(verticeInfo); 
        }
        
        return result;
    }


    public boolean existeArchivo(String filename) {
        File file = new File(filePath + filename);
        return file.exists();
    }

    public static double calcularDistance(Aeropuerto aeropuertoA, Aeropuerto aeropuertoB) {
            // Validación de datos nulos
            if (aeropuertoA.getLatitud() == null || aeropuertoA.getLongitud() == null || 
                aeropuertoB.getLatitud() == null || aeropuertoB.getLongitud() == null) {
                return Double.NaN;
            }
        
            //Formula de Haversine
            final double RADIO_TIERRA = 6371000.0; // Constante del Radio de la Tierra (metros)
            final double FACTOR_CONVERSION_KM = 1000.0; // Factor de conversion de metros a km
        
            // Convertir coordenadas a radianes
            double latitudA = toRadians(aeropuertoA.getLatitud().doubleValue());
            double longitudA = toRadians(aeropuertoA.getLongitud().doubleValue());
            double latitudB = toRadians(aeropuertoB.getLatitud().doubleValue());
            double longitudB = toRadians(aeropuertoB.getLongitud().doubleValue());
        
            // diferencias de coordenadas
            double diffLatitud = latitudB - latitudA;
            double diffLongitud = longitudB - longitudA;
        
            //seno² de latitud
            double senLatitud = Math.sin(diffLongitud / 2);
            double senLatitudSqrt = senLatitud * senLatitud;
        
            //seno²  de longitud
            double senLongitud = Math.sin(diffLongitud / 2);
            double senLongitudSqrt = senLongitud * senLongitud;
        
            // coseno de las latitudes
            double cosLatitudA = Math.cos(latitudA);
            double cosLatitudB = Math.cos(latitudB);
        
            //Formula de Haversine
            double formulaHaversine = senLatitudSqrt + 
                                     (cosLatitudA* cosLatitudB * senLongitudSqrt);
            
            //distancia angular
            double distanciaAngular = 2 * Math.atan2(Math.sqrt(formulaHaversine), 
                                                    Math.sqrt(1 - formulaHaversine));
        
            //distancia en m
            double distanciaM = RADIO_TIERRA * distanciaAngular;
        
            // conversion a kilómetros y redondeo a 2 decimales
            double distanciaKm = distanciaM / FACTOR_CONVERSION_KM;
            return Math.round(distanciaKm * 100.0) / 100.0;
        }
    
public JsonObject vistaGraphData() throws Exception {
    JsonObject vistaGraph = new JsonObject();
    JsonArray nodes = new JsonArray();
    JsonArray edges = new JsonArray();
    
    for (int i = 1; i <= this.nro_vertices(); i++) {
        JsonObject node = new JsonObject();
        Aeropuerto modelo = verticeModels.get(i);
        if (modelo != null) {
            node.addProperty("name", modelo.getNombre()); 
        }
        node.addProperty("id", i); 
        node.addProperty("label", "V" + i);  
        
        LinkedList<Adyacencia> adyacencias = this.adyacencias(i);
        if (!adyacencias.isEmpty()) {
            for (int j = 0; j < adyacencias.getSize(); j++) {
                Adyacencia adj = adyacencias.get(j);
                JsonObject edge = new JsonObject();
                edge.addProperty("from", i); 
                edge.addProperty("to", adj.getDestination());  
                edge.addProperty("weight", adj.getWeight());
            }
        }
    }

        vistaGraph.add("nodes", nodes);
        vistaGraph.add("edges", edges);
        return vistaGraph;
}

    public Integer getVertice(Integer label) throws Exception {
        return label;
    }
}
