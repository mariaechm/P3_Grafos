/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.controller.tda.graph;
//import com.example.controller.exception.ListEmptyException;

/**
 *
 * @author maria-chuico
 */

public class GraphLabelNoDirect<E> extends GraphLabelDirect<E> {
    public GraphLabelNoDirect(Integer nro_vertices, Class<E> clazz) {
        super(nro_vertices, clazz);
    }


    public void insertEdgeL(E v1, E v2, Float weight) throws Exception {
        if (isLabelsGraph()) {
            add_edge(getVerticeL(v1), getVerticeL(v2), weight);
            add_edge(getVerticeL(v2), getVerticeL(v1), weight);
        } else {
            throw new Exception("Grafo no etiquetado");
        }
    }

    public Float getWeight(Integer v1, Integer v2) throws Exception {
      return wiegth_edge(v1, v2);
    }   
}
