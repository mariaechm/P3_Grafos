/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.controller.tda.list;

import java.io.Serializable;

/**
 *
 * @author maria-chuico
 */
public class Node <E> implements Serializable {
    private E info;
    private Node <E> next;

    //Constructors
    public Node (E info){
        this.info = info;
        this.next = null;   
    }
    
    public Node (E info, Node<E> next){
        this.info = info;
        this.next = next;
    }
    
    //getters and setters

    public E getInfo() {
        return this.info;
    }

    public void setInfo(E info) {
        this.info = info;
    }

    public Node<E> getNext() {
        return this.next;
    }

    public void setNext(Node<E> next) {
        this.next = next;
    }    
}