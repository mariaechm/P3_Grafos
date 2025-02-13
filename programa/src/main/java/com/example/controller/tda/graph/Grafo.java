package com.example.controller.tda.graph;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import com.example.controller.dao.EnergiaDao;
import com.example.controller.exception.ListEmptyException;
import com.example.controller.tda.list.LinkedList;
import com.example.models.Energia;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public abstract class Grafo {
    private static String archivo = "data/"; // Directorio donde se guardará el archivo JSON
    private Map<Integer, Energia> verticeModelo = new HashMap<>();

    public abstract Integer num_vertices();
    public abstract Integer num_aristas();
    public abstract Boolean existe_arista(Integer v1, Integer v2) throws Exception;
    public abstract Float peso_arista(Integer v1, Integer v2) throws Exception;
    public abstract void insertar_arista(Integer v1, Integer v2) throws Exception;
    public abstract void insertar_arista(Integer v1, Integer V2, Float peso) throws Exception;
    public abstract LinkedList<Adyacencia> adyacencias(Integer v1);
    public Integer getVertice(Integer v) throws Exception {
        return v;
    }

    @Override
    public String toString() { // muestra el vertice con su adyacencia y el peso de ella 
        StringBuilder grafo = new StringBuilder();
        try {
            for (int i = 1; i < this.num_vertices(); i++) {
                grafo.append("Vertice: ").append(i).append("\n");
                LinkedList<Adyacencia> lista = this.adyacencias(i);
                if (!lista.isEmpty()) {
                    Adyacencia[] ady = lista.toArray();
                    for (Adyacencia adyacencia : ady) {
                        grafo.append("Adyacencia: V").append(adyacencia.getDestino())
                                .append(" Peso: ").append(adyacencia.getPeso()).append("\n");
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return grafo.toString();
    }

    public void guardarGrafoDirigido(String fileName) throws Exception {
        JsonArray grafoArray = new JsonArray();// creo json para la vertices y aristas
        for (int i = 1; i <= this.num_vertices(); i++) {
            JsonObject verticesObject = new JsonObject();
            verticesObject.addProperty("labelId", this.getVertice(i));

            JsonArray destionArray = new JsonArray();
            LinkedList<Adyacencia> adyacencias = this.adyacencias(i);
            if (!adyacencias.isEmpty()) {
                for (int j = 0; j < adyacencias.getSize(); j++) {
                    Adyacencia ady1 = adyacencias.get(j);
                    JsonObject destiJsonObject = new JsonObject();
                    destiJsonObject.addProperty("from", this.getVertice(i));
                    destiJsonObject.addProperty("to", ady1.getDestino());
                    destionArray.add(destiJsonObject);
                }
            }
            verticesObject.add("destinations", destionArray);
            grafoArray.add(verticesObject);
        }
        Gson gson = new Gson();
        String json = gson.toJson(grafoArray);

        File directory = new File(archivo);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        try (FileWriter fileWriter = new FileWriter(archivo + fileName)) {
            fileWriter.write(json);
        }
    }

    public void cargaModelosDao() throws ListEmptyException {
        EnergiaDao energiaDao = new EnergiaDao();
        LinkedList<Energia> aeropuertoList = energiaDao.getListAll();

        for (int i = 0; i < aeropuertoList.getSize(); i++) {
            Energia energia = aeropuertoList.get(i);
            verticeModelo.put(energia.getId(), energia);
        }
    }

    public void cargarGrafo(String fileName) throws Exception {
        try (FileReader fileReader = new FileReader(archivo + fileName)) {
            Gson gson = new Gson();
            JsonArray grafoArray = gson.fromJson(fileReader, JsonArray.class);

            for (JsonElement jsonElement : grafoArray) {
                JsonObject vertiObject = jsonElement.getAsJsonObject();

                Integer labelID = vertiObject.get("labelId").getAsInt();

                Energia modelo = verticeModelo.get(labelID);

                if (modelo == null) {
                    continue;
                }
                this.addVerticeModelo(labelID, modelo);
                JsonArray destiJsonArray = vertiObject.getAsJsonArray("destinations");

                for (JsonElement destinoElement : destiJsonArray) {
                    JsonObject destiJsonObject = destinoElement.getAsJsonObject();

                    Integer from = destiJsonObject.get("from").getAsInt();
                    Integer to = destiJsonObject.get("to").getAsInt();

                    Energia modelFrom = verticeModelo.get(from);
                    Energia modelTo = verticeModelo.get(to);
                    if (modelFrom == null || modelTo == null) {

                    } else {
                        Float peso = (float) calcularDistancia(modelFrom, modelTo);

                        this.insertar_arista(from, to, peso);
                        System.out.println("Arista añadida de " + from + " a " + to + " con peso calculado: " + peso);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    // Método para borrar todas las adyacencias de todos los vértices
    public void limpiarAdyacencias() {
        for (int i = 1; i <= this.num_vertices(); i++) {
            this.adyacencias(i).reset(); // Limpiar la lista de adyacencias para cada vértice
        }
    }

    public void grafoArista(String filename) throws Exception {
        // Primero, cargamos el grafo desde el archivo JSON
        cargarGrafo(filename);
        
        cargaModelosDao();

        limpiarAdyacencias(); // Función para borrar las adyacencias

        // Para cada vértice, agregamos al menos 3 conexiones aleatorias
        Random random = new Random();
        for (int i = 1; i <= this.num_vertices(); i++) {
            LinkedList<Adyacencia> existingEdges = this.adyacencias(i);
            int connectionsCount = existingEdges.getSize();            
            while (connectionsCount < 3) {                
                int randomVertex = random.nextInt(this.num_vertices()) + 1;
                while (randomVertex == i || existe_arista(i, randomVertex)) {
                    randomVertex = random.nextInt(this.num_vertices()) + 1;
                }
                
                Energia modelFrom = verticeModelo.get(i);
                Energia modelTo = verticeModelo.get(randomVertex);

               
                float weight = (float) calcularDistancia(modelFrom, modelTo); 
                insertar_arista(i, randomVertex, weight); 
                connectionsCount++;
            }
        }        
        guardarGrafoDirigido(filename);
    }

    // Método para agregar un vértice con su modelo asociado
    public void addVerticeModelo(Integer vertexId, Energia model) {
        verticeModelo.put(vertexId, model); // Asociar el vértice con su modelo
    }

    public boolean existsFile(String filename) {
        File file = new File(archivo + filename);
        return file.exists();
    }

    public static double calcularDistancia(Energia energia1, Energia energia2) {
        Double mediaTransmisionEquipo1 = (energia1.getVelocidadBaja() + energia1.getVelocidadBaja()) / 2;
        Double mediaTransmisionEquipo2 = (energia2.getVelocidadBaja() + energia2.getVelocidadBaja()) / 2;
        Double mediaCapacidadTransmision = (mediaTransmisionEquipo1 + mediaTransmisionEquipo2) / 2;
        Double coste = 3600/mediaCapacidadTransmision;
        return Math.round(coste * 100.0) / 100.0;
    }

    public JsonArray obtainWeights() throws Exception {
        JsonArray result = new JsonArray();

        // Iterar sobre todos los vértices del grafo
        for (int i = 1; i <= this.num_vertices(); i++) {
            JsonObject verticeInfo = new JsonObject();
            Energia modelo = verticeModelo.get(i);
            if (modelo != null) {
                verticeInfo.addProperty("nombre", modelo.getNombre());
            }
            verticeInfo.addProperty("labelId", this.getVertice(i)); // ID del vértice actual

            JsonArray destinations = new JsonArray(); // Lista de conexiones para el vértice
            LinkedList<Adyacencia> adyacencias = this.adyacencias(i);

            for (int j = 0; j < adyacencias.getSize(); j++) { // Corregido el rango del bucle
                Adyacencia adj = adyacencias.get(j);

                JsonObject destinationInfo = new JsonObject();
                destinationInfo.addProperty("from", this.getVertice(i)); // Desde el vértice actual
                destinationInfo.addProperty("to", adj.getDestino()); // Al destino
                destinationInfo.addProperty("weight", adj.getPeso()); // Peso de la arista
                destinations.add(destinationInfo);

            }

            verticeInfo.add("destinations", destinations); // Agregar las conexiones al vértice
            result.add(verticeInfo); // Agregar la información del vértice al resultado
        }

        return result;
    }

    public void guardarGrafo() {
        try {
            // Asigna el nombre del archivo como "grafo.json"
            String filename = "grafo.json"; // El nombre que deseas para el archivo
            guardarGrafoDirigido(filename); // Llamas a tu método con el nombre del archivo
        } catch (Exception e) {
            e.printStackTrace(); // En caso de error, imprime la traza del error
        }
    }

    public JsonObject getVisGraphData() throws Exception {
        JsonObject vistaGrafo = new JsonObject();

        // Arrays para los nodos y las aristas
        JsonArray nodes = new JsonArray();
        JsonArray edges = new JsonArray();

        // Iteramos sobre los vértices
        for (int i = 1; i <= this.num_vertices(); i++) {
            JsonObject node = new JsonObject();
            Energia model = verticeModelo.get(i);
            if (model != null) {
                node.addProperty("name", model.getNombre()); // Nombre del vértice
            }
            node.addProperty("id", i); // ID del nodo
            node.addProperty("label", "V" + i); // Etiqueta del nodo (puedes personalizarlo)

            // Opcional: Agregar un color o más propiedades si lo deseas           
            nodes.add(node);

            // Obtener las adyacencias de este vértice
            LinkedList<Adyacencia> adyacencias = this.adyacencias(i);
            if (!adyacencias.isEmpty()) {
                for (int j = 0; j < adyacencias.getSize(); j++) {
                    Adyacencia adj = adyacencias.get(j);
                    JsonObject edge = new JsonObject();
                    edge.addProperty("from", i); // Nodo origen
                    edge.addProperty("to", adj.getDestino()); // Nodo destino
                    edge.addProperty("weight", adj.getPeso()); // Peso de la arista                   
                    edges.add(edge);
                }
            }
        }

        vistaGrafo.add("nodes", nodes);
        vistaGrafo.add("edges", edges);

        return vistaGrafo;
    }

}
