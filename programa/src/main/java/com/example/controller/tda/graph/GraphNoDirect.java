/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.controller.tda.graph;
import com.example.controller.exception.ListEmptyException;

/**
 *
 * @author maria-chuico
 */

public class GraphNoDirect extends GraphDirect {
    public GraphNoDirect(Integer nro_vertices) {
        super(nro_vertices);
    }

    public void add_edge(Integer v1, Integer v2, Float weight) throws Exception {
        if (v1.intValue() <= nro_vertices() && v2.intValue() <= nro_vertices()) {
            if (!is_edge(v1, v2)) {
                //nro_edges++;
                setNro_edges(nro_edges() + 1);
                Adyacencia aux = new Adyacencia();
                aux.setWeight(weight);
                aux.setDestination(v2);
                getListaAdycencias()[v1].add(aux);

                Adyacencia auxD = new Adyacencia();
                auxD.setWeight(weight);
                auxD.setDestination(v1);
                getListaAdycencias()[v2].add(auxD);
            }
        } else {
            throw new ListEmptyException ("Vertices fuera de rango");
        }
        //super.add_edge(v1, v2, weight);
    }
}
