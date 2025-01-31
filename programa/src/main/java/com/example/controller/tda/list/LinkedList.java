/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.controller.tda.list;
import java.lang.reflect.Method;
import java.lang.reflect.Array;

import com.example.controller.exception.ListEmptyException;

public class LinkedList<E> {
    private Node<E> header;
    private Node<E> last;
    private Integer size;

    //Ordenamiento
    public static Integer ASC = 1;
    public static Integer DESC = 0;

    //Constructor
    public LinkedList() {
        this.header = null;
        this.last = null;
        this.size = 0;
    }

    //Verificar si la lista esta vacia
    public Boolean isEmpty() {
        return (this.header == null || this.size == 0);
    }

    //Agregar un nodo al inicio
    private void addHeader(E dato) {
        Node<E> help;
        if (isEmpty()) {
            help = new Node<>(dato);
            header = help;
            this.last = help;
        } else {
            Node<E> healpHeader = this.header;
            help = new Node<>(dato, healpHeader);
            this.header = help;
        }
        this.size++;
    }

    //Agregar un nodo al final
    private void addLast(E info) {
        Node<E> help;
        if (isEmpty()) {
            addHeader(info);
        } else {
            help = new Node<>(info, null);
            last.setNext(help);
            last = help;
            this.size++;
        }
    }

    //Agregar
    public void add(E info) {
        addLast(info);
    }

    // Obtener un nodo
    private Node<E> getNode(Integer index) throws ListEmptyException, IndexOutOfBoundsException {
        if (isEmpty()) {
            throw new ListEmptyException("Error, List empty");
        } else if (index.intValue() < 0 || index.intValue() >= this.size) {
            throw new IndexOutOfBoundsException("Error, fuera de rango");
        } else if (index.intValue() == 0) {
            return header;
        } else if (index.intValue() == (this.size - 1)) {
            return last;
        } else {
            Node<E> search = header;
            int cont = 0;
            while (cont < index.intValue()) {
                cont++;
                search = search.getNext();
            }
            return search;
        }
    }

    //Obtener
    public E get(Integer index) throws ListEmptyException, IndexOutOfBoundsException {
        if (isEmpty()) {
            throw new ListEmptyException("Error, List empty");
        } else if (index.intValue() < 0 || index.intValue() >= this.size.intValue()) {
            throw new IndexOutOfBoundsException("Error, fuera de rango");
        } else if (index.intValue() == 0) {
            return header.getInfo();
        } else if (index.intValue() == (this.size - 1)) {
            return last.getInfo();
        } else {
            Node<E> search = header;
            int cont = 0;
            while (cont < index.intValue()) {
                cont++;
                search = search.getNext();
            }
            return search.getInfo();
        }

    }

    //Agregar un nodo una posicion especifica en la lista
    public void add(E info, Integer index) throws ListEmptyException, IndexOutOfBoundsException {
        if (isEmpty() || index.intValue() == 0) {
            addHeader(info);
        } else if (index.intValue() == this.size.intValue()) {
            addLast(info);
        } else {

            Node<E> search_preview = getNode(index - 1);
            Node<E> search = getNode(index);
            Node<E> help = new Node<>(info, search);
            search_preview.setNext(help);
            this.size++;
        }
    }

    public void reset() {
        this.header = null;
        this.last = null;
        this.size = 0;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("List Data \n");
        try {

            Node<E> help = header;
            while (help != null) {
                sb.append(help.getInfo()).append(" -> ");
                help = help.getNext();
            }
        } catch (Exception e) {
            sb.append(e.getMessage());
        }

        return sb.toString();
    }

    public Integer getSize() {
        return this.size;
    }

    public E[] toArray() {
        E[] matrix = null;
        if (!isEmpty()) {
            Class clazz = header.getInfo().getClass();
            matrix = (E[]) java.lang.reflect.Array.newInstance(clazz, size);
            Node<E> aux = header;
            for (int i = 0; i < this.size; i++) {
                matrix[i] = aux.getInfo();
                aux = aux.getNext();
            }
        }
        return matrix;
    }

    public LinkedList<E> toList(E[] matrix) {
        reset();
        for (int i = 0; i < matrix.length; i++) {
            this.add(matrix[i]);
        }
        return this;
    }

    //Actualizar un nodo
    public void update(E data, Integer post) throws ListEmptyException {
        if (isEmpty()) {
            throw new ListEmptyException("Error, la lista esta vacia");
        } else if (post < 0 || post >= size) {
            throw new IndexOutOfBoundsException("Error, esta fuera de los limites de la lista");
        } else if (post == 0) {
            header.setInfo(data);
        } else if (post == (size - 1)) {
            last.setInfo(data);
        } else {
            Node<E> search = header;
            Integer cont = 0;
            while (cont < post) {
                cont++;
                search = search.getNext();
            }
            search.setInfo(data);
        }
    }

    //Eliminar el primer nodo
    public E deleteFirst() throws ListEmptyException {
        if (isEmpty()) {
            throw new ListEmptyException("Lista vacia");
        } else {
            E element = header.getInfo();
            Node<E> aux = header.getNext();
            header = aux;
            if (size.intValue() == 1) {
                last = null;
            }
            size--;
            return element;
        }
    }

    //Eliminar el ultimo nodo
    public E deleteLast() throws ListEmptyException {
        if (isEmpty()) {
            throw new ListEmptyException("Lista vacia");
        } else {
            E element = last.getInfo();
            Node<E> aux = getNode(size - 2);
            if (aux == null) {
                last = null;
                if (size == 2) {
                    last = header;
                } else {
                    header = null;
                }
            } else {
                last = null;
                last = aux;
                last.setNext(null);
            }
            size--;
            return element;
        }
    }

    //Eliminar un nodo en una posicion especifica
    public E delete(Integer post) throws ListEmptyException {
        if (isEmpty()) {
            throw new ListEmptyException("Error, la lista esta vacia");
        } else if (post < 0 || post >= size) {
            throw new IndexOutOfBoundsException("Error, esta fuera de los limites de la lista");
        } else if (post == 0) {
            return deleteFirst();
        } else if (post == (size - 1)) {
            return deleteLast();
        } else {
            Node<E> preview = getNode(post - 1);
            Node<E> actually = getNode(post);
            E element = preview.getInfo();
            Node<E> next = actually.getNext();
            actually = null;
            preview.setNext(next);
            size--;
            return element;
        }
    }

    public LinkedList<E> order(String attribute, Integer type) throws Exception {
        if (!isEmpty()) { //Si lista no esta vacia
            E data = this.header.getInfo();
            if (data instanceof Object) {
                E[] lista = this.toArray();
                reset();
                for (int i = 1; i < lista.length; i++) {
                    E aux = lista[i]; // valor a ordenar
                    int j = i - 1; // índice anterior
                    while (j >= 0 && atrribute_compare(attribute, lista[j], aux, type)) {
                        lista[j + 1] = lista[j--]; // desplaza elementos hacia la derecha
                    }
                    lista[j + 1] = aux; // inserta el valor en su posición correcta
                }
                this.toList(lista);
            }
        }
        return this;
    }


    //Comparar 
    private Boolean compare(Object a, Object b, Integer type) {
        switch (type) {
            case 0:
                if (a instanceof Number) {
                    Number a1 = (Number) a;
                    Number b1 = (Number) b;
                    return a1.doubleValue() > b1.doubleValue();
                } else {
                    return (a.toString()).compareTo(b.toString()) > 0;
                }
            default:
                // mayor a menor
                if (a instanceof Number) {
                    Number a1 = (Number) a;
                    Number b1 = (Number) b;
                    return a1.doubleValue() < b1.doubleValue();
                } else {
                    return (a.toString()).compareTo(b.toString()) < 0;
                }
        }

    }

    //Comparar atributos
    private Boolean atrribute_compare(String attribute, E a, E b, Integer type) throws Exception {
        return compare(exist_attribute(a, attribute), exist_attribute(b, attribute), type);
    }

    //Comparar objetos
    private Boolean compareObjects(Object a, Object b) {
        if (a instanceof Number && b instanceof Number) {
            Number a1 = (Number) a;
            Number b1 = (Number) b;
            return a1.doubleValue() == b1.doubleValue();
        } else if (a instanceof String && b instanceof String) {
            return ((String) a).contains ((String)b);
        } else{
            return false;
        }
    }

    //Verificar si un atributo existe
    private Object exist_attribute(E a, String attribute) throws Exception {
        Method method = null;
        attribute = attribute.substring(0, 1).toUpperCase() + attribute.substring(1);
        attribute = "get" + attribute;
        for (Method aux : a.getClass().getMethods()) {
            if (aux.getName().contains(attribute)) {
                method = aux;
                break;
            }
        }
        if (method == null) {
            for (Method aux : a.getClass().getSuperclass().getMethods()) {
                if (aux.getName().contains(attribute)) {
                    method = aux;
                    break;
                }
            }
        }
        if (method != null) {
            return method.invoke(a);
        }

        return null;
    }

    //Buscar por atributo
    public LinkedList <E> buscarByAtributo(String attribute, Object x) throws Exception{
        LinkedList<E> list = new LinkedList<>();
        if(isEmpty())return list;
            E[] array = this.toArray();
        for(int i= 0; i < array.length;i++){
            if (compareObjects(exist_attribute(array[i], attribute), x)){
                list.add(array[i]);   
            }
        }
        return list;
    }

    //Metodo QuickSort
    private Integer particionArray(String attribute, E[] elementoArray, Integer elementoMenor, Integer elementoMayor, Integer tipoOrden) throws Exception{
        E elementParticion = elementoArray[elementoMayor];
        Integer j = elementoMenor - 1;
        for (int i = elementoMenor; i <= elementoMayor-1; i++){
            if (atrribute_compare(attribute, elementoArray[i], elementParticion, tipoOrden)){
                j++;
                E x = elementoArray[i];
                elementoArray[i] = elementoArray[j];
                elementoArray[j] = x;
            }
        }
        E x = elementoArray[j+1];
        elementoArray[j+1] = elementoArray[elementoMayor];
        elementoArray[elementoMayor] = x;
        return j+1;
    }

    private void quickSort(String attribute, E[] elementoArray, Integer elementoMenor, Integer elementoMayor, Integer tipoOrden) throws Exception{
        if (elementoMenor < elementoMayor){
            Integer elementParticion = particionArray(attribute, elementoArray, elementoMenor, elementoMayor, tipoOrden);
            quickSort(attribute, elementoArray, elementoMenor, elementParticion -1, tipoOrden);
            quickSort(attribute, elementoArray, elementParticion + 1, elementoMayor, tipoOrden);
        }
    }

    public LinkedList<E> quickSort(String attribute, Integer tipoOrden) throws Exception {
        if (isEmpty()) return this;
        E[] elementoArray = this.toArray();
        final Integer elementoMayor = elementoArray.length - 1;
        final Integer elementoMenor = 0;
        quickSort(attribute,elementoArray,elementoMenor, elementoMayor, tipoOrden);
        return this.toList(elementoArray);
    }
    
    //Metodo MergeSort
    private void merge(String attribute, E[] elementoArray, int izq, int elementoMedio, int der, Integer tipoOrden) throws Exception{
        Class<?> classs = this.header.getInfo().getClass();

        int n1 = elementoMedio - izq + 1;
        int n2 = der - elementoMedio;

        @SuppressWarnings("unchecked")
        E ArrayIzq[] = (E[]) Array.newInstance(classs, n1);
        @SuppressWarnings("unchecked")
        E ArrayDer[] = (E[]) Array.newInstance(classs, n2);

        for (int i = 0; i < n1; i++){
            ArrayIzq[i] = elementoArray[izq + i];
        }
        for (int j = 0; j < n2; j++){
            ArrayDer[j] = elementoArray[elementoMedio + 1 + j];
        }

        int i = 0, j = 0;
        int k = izq;

        while (i < n1 && j < n2){
            if (atrribute_compare(attribute, ArrayIzq[i], ArrayDer[j], tipoOrden)){
                elementoArray[k] = ArrayIzq[i];
                i++;
            } else {
                elementoArray[k] = ArrayDer[j];
                j++;
            }
            k++;
        }

        while (i < n1){
            elementoArray[k] = ArrayIzq[i];
            i++;
            k++;
        }

        while (j < n2){
            elementoArray[k] = ArrayDer[j];
            j++;
            k++;
        }   
    }

    private void mergeSort(String attribute, E[] elementoArray, int izq, int der, Integer tipoOrden) throws Exception{
        if (izq < der){
            int elementoMedio = (izq + der) / 2;
            mergeSort(attribute, elementoArray, izq, elementoMedio, tipoOrden);
            mergeSort(attribute, elementoArray, elementoMedio + 1, der, tipoOrden);
            merge(attribute, elementoArray, izq, elementoMedio, der, tipoOrden);
        }
    }

    public LinkedList<E> mergeSort(String attribute, Integer tipoOrden) throws Exception {
        if (isEmpty()) return this;
        E[] elementoArray = this.toArray();
        final Integer izq = 0;
        final Integer der = elementoArray.length - 1;
        mergeSort(attribute, elementoArray, izq, der, tipoOrden);
        reset();
        return this.toList(elementoArray);
    }

    //Metodo ShellSort
    private int shellSort(String attribute, E[] elementoArray,Integer tipoOrden) throws Exception {
        int longitud = elementoArray.length;
        for(int space = longitud/2; space > 0; space /= 2){
            for(int i = space; i < longitud; i++){
                E temp = elementoArray[i];
                int j;
                for(j = i; j >= space && atrribute_compare(attribute, elementoArray[j - space], temp, tipoOrden); j -= space)
                    elementoArray[j] = elementoArray[j - space];
                elementoArray[j] = temp;    
            }
        }
        return 0;
    }

    public LinkedList<E> shellSort (String attribute, Integer tipoOrden) throws Exception{
        if (isEmpty()) return this;
        E[] elementoArray = this.toArray();
        shellSort(attribute, elementoArray, tipoOrden);
        return this.toList(elementoArray);
            
    }

    //Metodo busquedaLinealBinaria

    public Integer getIndice(String attribute, Object x) throws Exception{
        if (isEmpty()) return -1;
        E[] elementoArray = this.toArray();
        return busquedaBinaria (elementoArray, x, attribute);       
    }

    public LinkedList<E> busquedaLinealBinaria (String attribute, Object x){
        if (isEmpty()) return new LinkedList<>();
  try {
            this.mergeSort(attribute, 1);
            E[] elementoArray = this.toArray(); 
            Integer indice = getIndice(attribute, x);
            Integer i = indice.intValue();
            E object = this.get(indice);
            LinkedList<E> list = new LinkedList<>();

            while (indice >= 0 && compareObjects(
                exist_attribute(elementoArray[indice], attribute),
                exist_attribute(object, attribute))) {
            list.add(elementoArray[indice]);
            indice--;
        }
        indice = i + 1;
        while (indice < this.size && compareObjects(
                exist_attribute(elementoArray[indice], attribute),
                exist_attribute(object, attribute))) {
            list.add(elementoArray[indice]);
            indice++;
        }
        return list;
    } catch (Exception e) {
        e.printStackTrace();
        return new LinkedList<>();
    }
}

    //Metodo busquedaBinaria
    private int busquedaBinaria(E elementoArray[],Object x, String attribute) throws Exception {
        int elementoMenor = 0, elementoMayor = elementoArray.length - 1;

        while (elementoMenor <= elementoMayor) {
            int mid = elementoMenor + (elementoMayor - elementoMenor) / 2;
            if (exist_attribute(elementoArray[mid], attribute).equals(x)) return mid;
            if (compare (exist_attribute(elementoArray[mid], attribute), x, 1)) {
                elementoMenor = mid + 1;
            }else{
                elementoMayor = mid - 1;
            } 
        }
        return -1;
    }

    public E busquedaBinaria(String attribute, Object x) throws Exception {
        if (isEmpty()) return null;
        try{
            E[] elementoArray = this.toArray();
            return elementoArray[busquedaBinaria(elementoArray, x, attribute)];
        } catch (Exception e){
            e.printStackTrace();
            throw new Exception("El objeto no se encuentra en la lista");  
        }
    }
}