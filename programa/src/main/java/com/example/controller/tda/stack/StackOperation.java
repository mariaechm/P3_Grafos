/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.controller.tda.stack;

import com.example.controller.exception.ListEmptyException;
import com.example.controller.tda.list.LinkedList;
import com.example.controller.exception.OverFlowException;

public class StackOperation <E> extends LinkedList<E>{
    private Integer top;

    public StackOperation(Integer top) {
        this.top = top;
    }
    
    public Boolean verify() {
        return getSize().intValue() <= top.intValue();
    }
    
    public void push(E dato) throws Exception {
        if(verify()) {
            add(dato, 0);
        } else {
            throw new OverFlowException("Pila llena");
        }
    }
    
    public E pop() throws ListEmptyException {
        if(isEmpty()) {
            throw new ListEmptyException("Pila vacia");
        } else {
            return deleteFirst();
        }
    }

    public Integer getTop() {
        return top;
    }

    public void setTop(Integer top) {
        this.top = top;
    }
    
}

