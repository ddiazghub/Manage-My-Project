/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package projectmanagementsoftware.linkedlist;

/**
 * Interfaz que representa una cola de elementos.
 * @author david
 */
public interface IQueue<T> {
    /**
     * Obtiene el tamaño actual de la estructura.
     * @return Tamaño
     */
    public int length();
    
    /**
     * Añade un elemento al inicio de la cola.
     * @param element Elemento a añadir.
     */
    public void enqueue(T element);
    
    /**
     * Extrae el elemento al final de la cola.
     * @return El elemento extraído.
     */
    public T dequeue();
    
    /**
     * Obtiene el elemento al final de la cola sin extraerlo.
     * @return Último elemento en la cola.
     */
    public T peek();
    
    /**
     * Determina si la estructura está vacía.
     * @return Verdadero si el tamaño es 0 y falso si no.
     */
    public Boolean isEmpty();
}
