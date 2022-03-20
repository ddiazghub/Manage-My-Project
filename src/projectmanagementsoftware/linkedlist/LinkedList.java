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
public class LinkedList<T> implements IQueue<T> {
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
     * @param element El elemento a agregar
     */
    public void add(T element) {
        LinkedListNode<T> node = new LinkedListNode<>(element);
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
     * Agrega un elemento a una posición de la lista.
     * @param element Elemento que se va a añadir.
     * @param index Posición en la cual se añadirá el elemento.
     * @throws IndexOutOfBoundsException Si se intenta añadir en una posición por fuera del espacio abarcado por la lista.
     */
    public void add(T element, int index) {
        if (index < 0 || index >= this.length)
            throw new IndexOutOfBoundsException();
        
        if (index == 0) {
            LinkedListNode<T> node = new LinkedListNode<>(element, this.head);
            this.head = node;
            this.length++;
            
            return;
        }
        
        LinkedListNode<T> current = this.head;
        
        for (int i = 1; i < index; i++) {
            current = current.getNext();
        }
        
        LinkedListNode<T> node = new LinkedListNode<>(element, current.getNext());
        current.setNext(node);
        this.length++;
    }
    
    /**
     * Elimina un elemento en una posición de la lista.
     * @param index Posición en la cual se eliminará el elemento.
     * @return El elemento extraído de la lista.
     * @throws IndexOutOfBoundsException Si se intenta eliminar en una posición por fuera del espacio abarcado por la lista.
     */
    public T remove(int index) {
        if (index < 0 || index >= this.length)
            throw new IndexOutOfBoundsException();
        
        if (index == 0) {
            T data = this.head.get();
            this.head = this.head.getNext();
            this.length--;
            
            return data;
        }
        
        LinkedListNode<T> current = this.head;
        
        for (int i = 1; i < index; i++) {
            current = current.getNext();
        }
        
        T data = current.getNext().get();
        current.setNext(current.getNext().getNext());
        this.length--;
        
        return data;
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
     * @see projectmanagementsoftware.linkedlist.IQueue#length() 
     */
    @Override
    public int length() {
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
            node = node.getNext();
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
        Boolean endOutOfBounds = end < 0 || end > this.length;
        
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
            i++;
        }
    }

    /**
     * @see projectmanagementsoftware.linkedlist.IQueue#enqueue() 
     */
    @Override
    public void enqueue(T element) {
        this.add(element, 0);
    }

    /**
     * @see projectmanagementsoftware.linkedlist.IQueue#dequeue() 
     */
    @Override
    public T dequeue() {
        return this.remove(this.length - 1);
    }

    /**
     * @see projectmanagementsoftware.linkedlist.IQueue#peek() 
     */
    @Override
    public T peek() {
        return this.tail.get();
    }

    /**
     * @see projectmanagementsoftware.linkedlist.IQueue#isEmpty() 
     */
    @Override
    public Boolean isEmpty() {
        return this.length == 0;
    }
    
    public String join(String joinChar) {
        if (this.length == 0)
            return "";
        
        if (!this.head.get().getClass().getSimpleName().equals("String"))
            throw new UnsupportedOperationException();
        
        LinkedListNode<String> container = new LinkedListNode<>((String) this.head.get());
        
        this.forEachBetween(1, this.length, string -> {
            container.set(container.get() + joinChar + string);
        });
        
        return container.get();
    }
    
    public static LinkedList<String> split(String string, String separator) {
        LinkedList<String> list = new LinkedList<>();
        
        for (String token : string.split(separator)) {
            list.add(token);
        }
        
        return list;
    }
}