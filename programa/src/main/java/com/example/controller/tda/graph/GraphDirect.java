/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.controller.tda.graph;
import com.example.controller.exception.OverFlowException;
import com.example.controller.tda.list.LinkedList;

/**
 *
 * @author maria-chuico
 */

 public class GraphDirect extends Graph {
  private Integer nro_vertices;
  private Integer nro_edges;
  private LinkedList<Adyacencia> listaAdyacencias[];


  @SuppressWarnings("unchecked")
  public GraphDirect(Integer nro_vertices) {
    this.nro_vertices = nro_vertices;
    this.nro_edges = 0;
    listaAdyacencias = new LinkedList[nro_vertices + 1];
    for (int i = 1; i <= nro_vertices; i++) {
      listaAdyacencias[i] = new LinkedList<>();
    }
  }

  //@Override
  public Integer nro_edges() {
    return this.nro_edges;
  }

 // @Override
  public Integer nro_vertices() {
    return this.nro_vertices;
  }

  @Override
  public Boolean is_edge(Integer v1, Integer v2) throws Exception {
    Boolean band = false;
    if (v1.intValue() <= nro_vertices && v2.intValue() <= nro_vertices) {
      LinkedList<Adyacencia> lista = listaAdyacencias[v1];
      if (!lista.isEmpty()) {
        Adyacencia[] matrix = lista.toArray();
        for (int i = 0; i < matrix.length; i++) {
          Adyacencia aux = matrix[i];
          if (aux.getDestination().intValue() == v2.intValue()) {
            band = true;
            break;
          }
        }
      }

    } else {
      throw new OverFlowException("Vertices fuera de rango");
    }
    return band;
  }


  public Float weight_edge(Integer v1, Integer v2) throws Exception {
    // TODO Auto-generated method stub
    Float weight = Float.NaN;
    if (is_edge(v1, v2)) {
      LinkedList<Adyacencia> lista = listaAdyacencias[v1];
      if (!lista.isEmpty()) {
        Adyacencia[] matrix = lista.toArray();
        for (int i = 0; i < matrix.length; i++) {
          Adyacencia aux = matrix[i];
          if (aux.getDestination().intValue() == v2.intValue()) {
            weight = aux.getWeight();
            break;
          }
        }
      }
    }
    return weight;
  }

  @Override
  public void add_edge(Integer v1, Integer v2, Float weight) throws Exception{
    if (v1.intValue() <= nro_vertices && v2.intValue() <= nro_vertices) {
      if (!is_edge(v1, v2)) { 
        nro_edges++;
        Adyacencia aux = new Adyacencia();
        aux.setWeight(weight);
        aux.setDestination(v2);
        listaAdyacencias[v1].add(aux);
      }
    } else {
      throw new OverFlowException("Vertices fuera de rango");
    }

  }

  @Override
  public void add_edge(Integer v1, Integer v2) throws Exception {
      // TODO Auto-generated method stub
      this.add_edge(v1, v2, Float.NaN);
  }

  @Override
  public LinkedList<Adyacencia> adyacencias(Integer vi) {
      return listaAdyacencias[vi];
  }

  public LinkedList<Adyacencia>[] getListaAdycencias() {
    return this.listaAdyacencias;
  }

  public void setListaAdyacencias(LinkedList<Adyacencia>[] listaAdyacencias) {
    this.listaAdyacencias = listaAdyacencias;
  }

  public void setNro_edges(Integer nro_edges) {
    this.nro_edges = nro_edges;
  }

  @Override
  public Float wiegth_edge(Integer v1, Integer v2) throws Exception {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'wiegth_edge'");
  }
}
