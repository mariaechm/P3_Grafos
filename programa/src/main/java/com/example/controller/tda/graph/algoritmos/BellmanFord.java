/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.controller.tda.graph.algoritmos;

import java.util.HashMap;
import java.util.Map;

import com.example.controller.tda.graph.Adyacencia;
import com.example.controller.tda.graph.GraphLabelNoDirect;
import com.example.controller.tda.list.LinkedList;

/**
 *
 * @author maria-chuico
 */
public class BellmanFord {

  private GraphLabelNoDirect<String> graph;
  private int origin;
  private int destiny;
  private Map<Integer, Float> distance;
  private Map<Integer, Integer> predecessor;

  public BellmanFord(GraphLabelNoDirect<String> graph, int origin, int destiny) {
    this.graph = graph;
    this.origin = origin;
    this.destiny = destiny;
    this.distance = new HashMap<>();
    this.predecessor = new HashMap<>();
  }

  public String caminoMasCorto() throws Exception{
    int n = this.graph.nro_vertices();

    for (int i = 0; i < n; i++) { // Inicializar los valores de los vertices
      this.distance.put(i, Float.MAX_VALUE);
      this.predecessor.put(i, -1);
    }

    this.distance.put(this.origin, 0f);

    for (int i = 1; i < n; i++) { // Relajacion de las aristas
      for (int j = 0; j < n; j++) {
        LinkedList<Adyacencia> adyacencias = this.graph.adyacencias(i);
        for (int k = 0; k < adyacencias.getSize(); k++) {
          Adyacencia adyacente = adyacencias.get(k);
          int v = adyacente.getDestination();
          float peso = adyacente.getWeight();

          if (this.distance.get(j) != Float.MAX_VALUE && this.distance.get(j) + peso < this.distance.get(v)) {
              this.distance.put(v, this.distance.get(j) + peso);
              this.predecessor.put(v, j);
          }
        }
      }
    }

    for (int i = 0; i < n; i++) { // Verificar ciclos negativos
      LinkedList<Adyacencia> adyacencias = this.graph.adyacencias(i);
      for (int j = 0; j < adyacencias.getSize(); j++) {
        Adyacencia adyacente = adyacencias.get(j);
        int v = adyacente.getDestination();
        float peso = adyacente.getWeight();

        if (this.distance.get(i) != Float.MAX_VALUE && this.distance.get(i) + peso < this.distance.get(v)) {
          throw new Exception("El grafo contiene un ciclo negativo");
        }
      }
    }
    return reconstruirCamino(this.origin, this.destiny).get("camino").toString();
  }

  private Map<String, Object> reconstruirCamino(int origin, int destiny)throws Exception {
    Map<String, Object> resultado = new HashMap<>();
    if (distance.get(destiny) == Float.MAX_VALUE) {
        resultado.put("camino", "No hay camino");
        resultado.put("distancia", Float.MAX_VALUE);
        return resultado;
    }
      StringBuilder camino = new StringBuilder();
      int actual = destiny;
      float distanciaTotal = 0;

      while (actual != -1) {
          if (predecessor.get(actual) != -1) {
              distanciaTotal += graph.getWeight(predecessor.get(actual), actual);
          }
          camino.insert(0, actual + " -> ");
          actual = predecessor.get(actual);
      }
      camino.delete(camino.length() - 4, camino.length());

      // Guardar los resultados en el mapa
      resultado.put("camino", camino.toString());
      resultado.put("distancia", distanciaTotal);
      
      return resultado;
  }
    
}
