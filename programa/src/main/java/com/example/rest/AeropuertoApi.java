/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.rest;


import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.example.controller.dao.services.AeropuertoServices;
import com.example.controller.tda.graph.GraphLabelNoDirect;
import com.example.controller.tda.list.LinkedList;
import com.example.models.Aeropuerto;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;


/**
 *
 * @author maria-chuico
 */

@Path("/aeropuerto")
public class AeropuertoApi {
    @Path("/list")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllAeropuertos() {
        HashMap<String, Object> map = new HashMap<>();
        AeropuertoServices ps = new AeropuertoServices();
        map.put("msg", "Lista de Aeropuertos");
        map.put("data", ps.listAll().toArray());
        if (ps.listAll().isEmpty()) {
            map.put("data", new Object[]{});
        }
        return Response.ok(map).build();		
    }


    @Path("/save")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response save (HashMap<String, Object> map) {
        HashMap<String, Object> res = new HashMap<>();
        try {
            AeropuertoServices ps = new AeropuertoServices();
            ps.getAeropuertos().setNombre(map.get("nombre").toString());
          
            ps.getAeropuertos().setLongitud(Double.parseDouble(map.get("longitud").toString()));
            ps.getAeropuertos().setLatitud(Double.parseDouble(map.get("latitud").toString()));
            ps.save();

            res.put("msg", "Ok");
            res.put("data", "Guardado con exito");
            return Response.ok(res).build();

        } catch (Exception e) {
            res.put("msg", "Error");
            res.put("data", e.getMessage());
            return Response.status(Response.Status.BAD_REQUEST).entity(res).build();

        }
    }

    @Path("/list/{id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAeropuertos(@PathParam("id") Integer id) {
        HashMap<String, Object> map = new HashMap<>();
        AeropuertoServices ps = new AeropuertoServices();
        try {
            ps.setAeropuertos(ps.get(id));
        } catch (Exception e) {
            
        }
        map.put("msg", "aeropuerto");
        map.put("data", ps.getAeropuertos());
        if(ps.getAeropuertos().getId() == null){
            map.put("data", "No exiten datos");
            return Response.status(Response.Status.NOT_FOUND).entity(map).build();
        }
        return Response.ok(map).build();
       }

    @Path("/update")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response update(HashMap<String, Object> map) {
        HashMap<String, Object> res = new HashMap<>();
        try {
            AeropuertoServices ps = new AeropuertoServices();
            ps.getAeropuertos().setId(Integer.parseInt(map.get("id").toString()));
            ps.getAeropuertos().setNombre(map.get("nombre").toString());

            ps.getAeropuertos().setLongitud(Double.parseDouble(map.get("longitud").toString()));
            ps.getAeropuertos().setLatitud(Double.parseDouble(map.get("latitud").toString()));
            ps.update();
            res.put("msg", "Ok");
            res.put("data", "Actualizado con exito");
            return Response.ok(res).build();
        } catch (Exception e) {
            res.put("msg", "Error");
            res.put("data", e.getMessage());
            return Response.status(Response.Status.BAD_REQUEST).entity(res).build();
        }
    }


    @Path("/delete/{id}")
    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    public Response delete(@PathParam("id") Integer id) {
        HashMap<String, Object> res = new HashMap<>();
        try {
            AeropuertoServices ps = new AeropuertoServices();
            ps.setAeropuertos(ps.get(id));
            ps.delete(id);
            res.put("msg", "Ok");
            res.put("data", "Eliminado con exito");
            return Response.ok(res).build();
        } catch (Exception e) {
            res.put("msg", "Error");
            res.put("data", e.getMessage());
            return Response.status(Response.Status.BAD_REQUEST).entity(res).build();
        }
    }
   

    @Path("/crearGrafo")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response grafoVerAdmin() {
        HashMap<String, Object> res = new HashMap<>();
        try {
            AeropuertoServices aeropuertoDao = new AeropuertoServices();
            LinkedList<Aeropuerto> listaAeropuesto = aeropuertoDao.listAll();         
            aeropuertoDao.crearGrafo();
            res.put("msg", "Grafo generado exitosamente");
            res.put("lista", listaAeropuesto.toArray());
            return Response.ok(res).build();   
        } catch (Exception e) {
            res.put("msg", "Error");
            res.put("data", e.getMessage());
            return Response.status(Response.Status.BAD_REQUEST).entity(res).build();
        }
    } 

@Path("/allGrafos")
@GET
@Produces(MediaType.APPLICATION_JSON)
public Response getGraph() {
    HashMap<String, Object> res = new HashMap<>();
    try {
        AeropuertoServices aeropuertoDao = new AeropuertoServices();
        JsonArray graph = aeropuertoDao.obtenerPesos();
        res.put("msg", "Grafo obtenido exitosamente");
        return Response.ok(graph.toString(), MediaType.APPLICATION_JSON).build();
    } catch (Exception e) {
        res.put("msg", "Error");
        res.put("data", e.getMessage());
        return Response.status(Response.Status.BAD_REQUEST).entity(res).build();
    }
}


@Path("/mapGrafos")
@GET
@Produces(MediaType.APPLICATION_JSON)
public Response getCompleteGraphData() {
    try {
        AeropuertoServices aeropuertoDao = new AeropuertoServices();
        JsonObject graph = aeropuertoDao.vistaGrafo();

        if (graph == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                           .entity("No se ha generado el grafo")
                           .build();
        }

        System.out.println("Contenido del grafo 40  : " + graph.getAsJsonObject());
        return Response.ok(graph.toString(), MediaType.APPLICATION_JSON).build();
    } catch (Exception e) {
        return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
    }
}

@Path("/ramdomAdyacencias")
@GET
@Produces(MediaType.APPLICATION_JSON)
public Response unionesgrafos() {
    HashMap<String, Object> res = new HashMap<>();
    try {
        AeropuertoServices aeropuertoDao = new AeropuertoServices();
        GraphLabelNoDirect<String> graph = aeropuertoDao.ramdomAdyacencias();
        System.out.println(graph.toString());
        aeropuertoDao.guardargrafo();
        res.put("msg", "Grafo actualizado con exito");
        res.put("data", graph.toString());
        return Response.ok(res).build();
    } catch (Exception e) {
        res.put("msg", "Error");
        res.put("data", e.getMessage());
        return Response.status(Response.Status.BAD_REQUEST).entity(res).build();
    }
}


@Path("/caminoCorto/{origen}/{destino}/{algoritmo}")
@GET
@Produces(MediaType.APPLICATION_JSON)
public Response calcularCaminoCorto(@PathParam("origen") int origin, 
                                    @PathParam("destino") int destiny, 
                                    @PathParam("algoritmo") int algorithm) {
    HashMap<String, Object> res = new HashMap<>();
    try {
        AeropuertoServices aeropuertoDao = new AeropuertoServices();
        JsonArray graph = aeropuertoDao.obtenerPesos();   
        Map<String, Object> resultado = aeropuertoDao.caminoMasCorto(origin, destiny, algorithm);     
        res.put("msg", "Camino corto calculado con exito");
        res.put("resultado", resultado);
        return Response.ok(res).build();
    } catch (Exception e) {
        res.put("msg", "Error");
        res.put("data", e.getMessage());
        return Response.status(Response.Status.BAD_REQUEST).entity(res).build();
    }
}


    @Path("/bfs/{origen}") //Busqueda en anchura
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response bfs(@PathParam("origen") int origen) throws Exception {
        HashMap<String, Object> res = new HashMap<>();
        AeropuertoServices aeropuertoDao = new AeropuertoServices();
        JsonArray graph = aeropuertoDao.obtenerPesos();
        String respuesta = aeropuertoDao.busquedaAnchura(origen);


        try {
        res.put("respuesta", respuesta);
        return Response.ok(res).build();          
        } catch (Exception e) {
            res.put("msg", "Error");
            res.put("data", e.getMessage());
            return Response.status(Response.Status.BAD_REQUEST).entity(res).build();
        }
    }
}
