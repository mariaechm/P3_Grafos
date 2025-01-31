/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.controller.tda.queque;

/**
 *
 * @author maria-chuico
 */
public class QueQue {

    public class Queque<E> {

        private QuequeOperation<E> quequeOperation;

        public Queque(Integer cant) {
            this.quequeOperation = new QuequeOperation<>(cant);
        }

        public void queque(E dato) throws Exception {
            quequeOperation.queque(dato);
        }

        public Integer getSize() {
            return this.quequeOperation.getSize();
        }

        public void clear() {
            this.quequeOperation.reset();
        }

        public class QueQueOperation {

        }

        public Integer getTop() {
            return this.quequeOperation.getTop();
        }

        public void print() {
            System.out.println("Cola");
            System.out.println(quequeOperation.toString());
            System.out.println("******");
        }

        //TODO.. falta push
        public E dequeque() throws Exception {
            return quequeOperation.dequeque();
        }

    }

}
