/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package projectmanagementsoftware.linkedlist;

/**
 * Nodo de una lista enlazada simple.
 * @author david
 * @param <T> Tipo del dato que el nodo almacena.
 */
public class LinkedListNode<T> {
    /**
     * Dato que el nodo almacena.
     */
    private T data;
    
    /**
     * Siguiente nodo de la lista.
     */
    private LinkedListNode<T> next;

    /**
     * Crea un nuevo nodo.
     * @param data El dato que el nodo va a contener.
     * @param next El apuntador al siguiente nodo si es necesario.
     */
    public LinkedListNode(T data, LinkedListNode<T> next) {
        this.data = data;
        this.next = next;
    }

    /**
     * Crea un nuevo nodo sin nodo siguiente.
     * @param data El dato que el nodo va a contener.
     */
    public LinkedListNode(T data) {
        this(data, null);
    }
    
    /**
     * Obtiene el dato del nodo.
     * @return Dato del nodo.
     */
    public T get() {
        return data;
    }

    /**
     * Cambia el valor del dato del nodo.
     * @param data El nuevo valor que tendrá el dato.
     */
    public void set(T data) {
        this.data = data;
    }

    /**
     * Obtiene el nodo siguiente.
     * @return El siguiente nodo en la lista.
     */
    public LinkedListNode<T> getNext() {
        return next;
    }

    /**
     * Asigna un nuevo nodo como el siguiente.
     * @param next 
     */
    public void setNext(LinkedListNode<T> next) {
        this.next = next;
    }
    
    /**
     * Verifica si hay un nodo siguiente a este nodo.
     * @return Verdadero si hay siguiente y falso en el caso contrario.
     */
    public Boolean hasNext() {
        return this.next != null;
    }
}
