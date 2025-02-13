package com.example.controller.dao;

import com.example.controller.dao.implement.AdapterDao;
import com.example.controller.tda.graph.GrafoEtiquetadoNoDirecto;
import com.example.controller.tda.graph.algoritmos.BFS;
import com.example.controller.tda.graph.algoritmos.BellmanFord;
import com.example.controller.tda.graph.algoritmos.Floyd;
import com.example.controller.tda.list.LinkedList;
import com.example.models.Energia;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class EnergiaDao extends AdapterDao<Energia> {
    private Energia energia;
    private LinkedList<Energia> listAll;
    private GrafoEtiquetadoNoDirecto<String> grafo;
    private LinkedList<String> verticeName;
    private String nombre = "EnergiaGrafo.json";

    public GrafoEtiquetadoNoDirecto<String> crearGrafo() {
        if (verticeName == null) {
            verticeName = new LinkedList<>();
        }

        LinkedList<Energia> list = this.getListAll();
        if (!list.isEmpty()) {
            if (grafo == null) {
                grafo = new GrafoEtiquetadoNoDirecto<>(list.getSize(), String.class);
            }

            Energia[] energia = list.toArray();
            for (int i = 0; i < energia.length; i++) { // Convierte el tipo a String

                // Usa el valor del Enum directamente en lugar de valueOf, si ya lo tienes
                this.grafo.labelsVertices(i + 1, energia[i].getNombre());// CAMBIAR
                verticeName.add(energia[i].getNombre());
            }
            this.grafo.dibujarGrafo();
        }
        return this.grafo;

    }

    public void guardarGrafo() throws Exception {
        this.grafo.guardarGrafoDirigido(nombre);
    }

    public JsonArray obtenerPeso() throws Exception {
        if (grafo == null) {
            crearGrafo();
        }

        if (grafo.existsFile(nombre)) {
            grafo.cargaModelosDao();
            grafo.cargarGrafo(nombre);

            JsonArray graphData = grafo.obtainWeights();            
            return graphData;
        } else {
            throw new Exception("El archivo de grafo no existe.");
        }
    }

    public GrafoEtiquetadoNoDirecto<String> obtenerGrafo() throws Exception {
        if (grafo == null) {
            crearGrafo();
        }
        if (grafo.existsFile(nombre)) {
            grafo.cargaModelosDao();
            grafo.grafoArista(nombre);
            System.out.println("Modelo asociado al grafo: " + nombre);
        } else {
            throw new Exception("El archivo de grafo no existe.");
        }

        guardarGrafo();
        return grafo;
    }

    public String bfs(int origen) throws Exception {
        if (grafo == null) {
            throw new Exception("El grafo no existe");
        }
        BFS bfsAlgoritmo = new BFS(grafo, origen);       
        String recorrido = bfsAlgoritmo.recorrerGrafo();
        return recorrido;
    }

    public String caminoCorto(int origen, int destino, int algoritmo) throws Exception {
        if (grafo == null) {
            throw new Exception("El grafo no existe");
        }

        if (origen < 0 || destino < 0) {
            throw new IllegalArgumentException("Los nodos origen y destino deben ser válidos y no negativos");
        }

        if (algoritmo != 1 && algoritmo != 2) {
            throw new IllegalArgumentException("Algoritmo no válido. Use 1 para Floyd o 2 para Bellman-Ford.");
        }

        System.out.println("Calculando camino corto desde " + origen + " hasta " + destino);

        switch (algoritmo) {
            case 1: // Algoritmo de Floyd
                return new Floyd(grafo, origen, destino).caminoCorto();

            case 2: // Algoritmo de Bellman-Ford
                return new BellmanFord(grafo, origen, destino).caminoCorto(algoritmo);

            default:
                throw new UnsupportedOperationException("Algoritmo no soportado.");
        }
    }

    public JsonObject dataGrafo() throws Exception {
        if (grafo == null) {
            crearGrafo();
        }

        if (grafo.existsFile(nombre)) {
            grafo.cargaModelosDao();
            grafo.cargarGrafo(nombre);
            
            JsonObject graphData = grafo.getVisGraphData(); 
            return graphData; 
        } else {
            throw new Exception("El archivo de grafo no existe.");
        }
    }

    public EnergiaDao() {
        super(Energia.class);
    }

    public Energia getEnergia() {
        if (energia == null) {
            energia = new Energia();
        }
        return this.energia;
    }

    public void setEnergia(Energia energia) {
        this.energia = energia;
    }


    public LinkedList<Energia> getListAll() {
        if (this.listAll == null) {
            this.listAll = listAll();
        }
        return this.listAll;
    }

    // Guardar un nuevo energia
    public Boolean save() throws Exception {
        Integer id = getListAll().getSize() + 1;
        getEnergia().setId(id);
        persist(getEnergia());
        return true;
    }

    // Actualizar energia
    public Boolean update() throws Exception {
        this.merge(getEnergia(), getEnergia().getId() - 1);
        this.listAll = listAll();
        return true;
    }

    public Boolean delete(Integer id) throws Exception {
        for (int i = 0; i < getListAll().getSize(); i++) {
            Energia hab = getListAll().get(i);
            if (hab.getId().equals(id)) {
                getListAll().delete(i);
                return true;
            }

        }
        throw new Exception("Energia no encontrado con ID: " + id);
    }
}

