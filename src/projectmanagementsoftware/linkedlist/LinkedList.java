/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package projectmanagementsoftware.linkedlist;

import projectmanagementsoftware.utils.IForEachFunction;

/**
 * Clase que representa una lista enlazada simple.
 * @author david
 * @param <T> El tipo de dato que va a contener cada nodo de la lista
 */
public class LinkedList<T> {
    /**
     * Primer nodo de la lista
     */
    private LinkedListNode<T> head;
    
    /**
     * Último nodo de la lista
     */
    private LinkedListNode<T> tail;
    
    /**
     * Tamaño actual de la lista
     */
    private int length;

    /**
     * Crea una nueva lista enlazada simple vacía.
     */
    public LinkedList() {
        this.head = null;
        this.tail = null;
        this.length = 0;
    }
    
    /**
     * Agrega un elemento al final de la lista.
     * @param data El elemento a agregar
     */
    public void add(T data) {
        LinkedListNode<T> node = new LinkedListNode<>(data);
        this.length++;
        
        if (this.head == null) {
            this.head = node;
            this.tail = this.head;
            
            return;
        }
        
        this.tail.setNext(node);
        this.tail = this.tail.getNext();
    }
    
    /**
     * Obtiene el elemento en la posición indicada como parámetro.
     * @param index La posición en la cual se va a buscar el elemento.
     * @return El elemento en la posición index de la lista.
     * @throws IndexOutOfBoundsException Si se intenta buscar en una posición por fuera del espacio abarcado por la lista.
     */
    public T get(int index) {
        if (index < 0 || index >= this.length)
            throw new IndexOutOfBoundsException();
        
        if (index == 0)
            return this.head.get();
        
        if (index == this.length - 1)
            return this.tail.get();
        
        LinkedListNode<T> node = this.head.getNext();
        
        for (int i = 1; i < index; i++) {
            node = node.getNext();
        }
        
        return node.get();
    }
    
    /**
     * Obtiene el primer nodo de la lista.
     * @return El primer nodo de la lista.
     */
    public LinkedListNode<T> getHead() {
        return this.head;
    }

    /**
     * Obtiene el último nodo de la lista.
     * @return El último nodo de la lista.
     */
    public LinkedListNode<T> getTail() {
        return tail;
    }

    /**
     * Obtiene la longitud de la lista.
     * @return Longitud de la lista.
     */
    public int getLength() {
        return length;
    }
    
    /**
     * Ejecuta la función que se pasa como parámetro para cada uno de los elementos en la lista enlazada. Se debe usar de la siguiente manera:
     * <br>
     * <pre>
     * {@code lista.forEach((elemento) -> {
     *     // Aqui se escribe el cuerpo de la función. Ejemplo:
     *     System.out.println(elemento);
     * });
     * }
     * </pre>
     * @param function La función a ejecutar para cada elemento de la lista.
     */
    public void forEach(IForEachFunction<T> function) {
        LinkedListNode<T> node = this.head;
        
        while (node != null) {
            function.action(node.get());
        }
    }
    
    /**
     * Ejecuta la función que se pasa como parámetro para cada uno de los elementos en la lista enlazada que se encuentre en el rango establecido. Se debe usar de la siguiente manera:
     * <br>
     * <pre>
     * {@code lista.forEachBetween(1, 5, (elemento) -> {
     *     // Aqui se escribe el cuerpo de la función. Ejemplo:
     *     System.out.println(elemento);
     * });
     * }
     * </pre>
     * @param start Límite inferior del rango, inclusivo.
     * @param end Límite superior del rango, exclusivo.
     * @param function La función a ejecutar para cada elemento de la lista.
     */
    public void forEachBetween(int start, int end, IForEachFunction<T> function) {
        Boolean startOutOfBounds = start < 0 || start >= this.length;
        Boolean endOutOfBounds = end < 0 || end >= this.length;
        
        if (startOutOfBounds || endOutOfBounds)
            throw new IndexOutOfBoundsException();
        
        if (end < start)
            return;
        
        LinkedListNode<T> node = this.head;
        int i = 0;
        
        while (node != null) {
            if (i == end)
                break;
            
            if (i >= start)
                function.action(node.get());
            
            node = node.getNext();
        }
    }
}