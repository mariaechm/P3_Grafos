package com.example.rest;

import java.util.HashMap;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.example.controller.dao.EnergiaDao;
import com.example.controller.dao.services.EnergiaServices;
import com.example.controller.tda.graph.GrafoEtiquetadoNoDirecto;
import com.example.controller.tda.list.LinkedList;
import com.example.models.Energia;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

@Path("/aeropuertos")
public class EnergiaApi {

    @Path("/lista")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllAeropuertos() {
        HashMap<String, Object> map = new HashMap<>();
        EnergiaServices es = new EnergiaServices();
        map.put("msg", "Lista de Energia");
        map.put("data", es.listAll().toArray());
        if (es.listAll().isEmpty()) {
            map.put("data", new Object[] {});
        }
        return Response.ok(map).build();
    }

    @Path("/guardar")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response save(HashMap<String, Object> map) {
        HashMap<String, Object> res = new HashMap<>();
        try {
            EnergiaServices es = new EnergiaServices();
            es.getEnergia().setNombre(map.get(("nombre")).toString());
            es.getEnergia().setIp(map.get(("ip")).toString());
            es.getEnergia().setMarca(map.get(("marca")).toString());
            es.getEnergia().setVelocidadAlta(Double.parseDouble(map.get("Velocidad Alta").toString()));
            es.getEnergia().setVelocidadBaja(Double.parseDouble(map.get("Velocidad Baja Y").toString()));
            es.save();

            res.put("msg", "Ok");
            res.put("data", "Energia guardada exitosamente");
            return Response.ok(res).build();

        } catch (Exception e) {
            res.put("msg", "Error");
            res.put("data", e.getMessage());
            return Response.status(Response.Status.BAD_REQUEST).entity(res).build();

        }
    }

    @Path("/lista/{id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getEnergia(@PathParam("id") Integer id) {
        HashMap<String, Object> map = new HashMap<>();
        EnergiaServices es = new EnergiaServices();
        try {
            es.setEnergia(es.get(id));
        } catch (Exception e) {

        }
        map.put("msg", "Energia");
        map.put("data", es.getEnergia());
        if (es.getEnergia().getId() == null) {
            map.put("data", "no exiten datos");
            return Response.status(Response.Status.NOT_FOUND).entity(map).build();
        }
        return Response.ok(map).build();
    }
    
    @Path("/caminoCorto/{origen}/{destino}/{algoritmo}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response calcularCaminoCorto(@PathParam("origen") int origen,
            @PathParam("destino") int destino,
            @PathParam("algoritmo") int algoritmo) {
        HashMap<String, Object> res = new HashMap<>();
        try {
            EnergiaDao hd = new EnergiaDao();
            JsonArray grafo = hd.obtenerPeso();
            String resultado = hd.caminoCorto(origen, destino, algoritmo);

            res.put("msg", "Camino corto calculado");
            res.put("data", resultado);

            return Response.ok(res).build();
        } catch (Exception e) {
            res.put("msg", "Error");
            res.put("data", e.getMessage());
            return Response.status(Response.Status.BAD_REQUEST).entity(res).build();
        }
    }
    
    @Path("/uniongrafos")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response unionesgrafos() {
        HashMap<String, Object> res = new HashMap<>();
        try {
            EnergiaDao hd = new EnergiaDao();
            // Obtener el grafo antes de la modificación
            GrafoEtiquetadoNoDirecto<String> grafo = hd.obtenerGrafo();
            hd.guardarGrafo();
            res.put("msg", "Grafo actualizado exitosamente");
            res.put("data", grafo.toString());
            return Response.ok(res).build();
        } catch (Exception e) {
            res.put("msg", "Error");
            res.put("data", e.getMessage());
            return Response.status(Response.Status.BAD_REQUEST).entity(res).build();
        }
    }

    @Path("/mapadegrafos")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCompletegrafoData() {
        try {
            EnergiaDao hd = new EnergiaDao();
            JsonObject grafo = hd.dataGrafo();               
            return Response.ok(grafo.toString(), MediaType.APPLICATION_JSON).build();
        } catch (Exception e) {            
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }

    @Path("/mostrarGrafos")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getgrafo() {
        HashMap<String, Object> res = new HashMap<>();
        try {
            EnergiaDao hd = new EnergiaDao();
            JsonArray grafo = hd.obtenerPeso();
            res.put("msg", "Grafo obtenido exitosamente");
            return Response.ok(grafo.toString(), MediaType.APPLICATION_JSON).build();
        } catch (Exception e) {
            res.put("msg", "Error");
            res.put("data", e.getMessage());
            return Response.status(Response.Status.BAD_REQUEST).entity(res).build();
        }
    }
    
    @Path("/listaGrafo")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response grafoVerAdmin() {
        HashMap<String, Object> res = new HashMap<>();
        try {
            EnergiaDao hd = new EnergiaDao();
            LinkedList<Energia> listaEnergia = hd.getListAll();
            hd.crearGrafo();
            hd.guardarGrafo();
            res.put("msg", "Grafo generado exitosamente");
            res.put("lista", listaEnergia.toArray());
            return Response.ok(res).build();
        } catch (Exception e) {
            res.put("msg", "Error");
            res.put("data", e.getMessage());
            return Response.status(Response.Status.BAD_REQUEST).entity(res).build();
        }
    }
}
