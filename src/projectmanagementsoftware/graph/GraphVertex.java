/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package projectmanagementsoftware.graph;

import projectmanagementsoftware.linkedlist.LinkedList;

/**
 *
 * @author david
 */
public class GraphVertex<T> {
    private T data;
    private LinkedList<GraphVertex<T>> links;
    
    public GraphVertex(T data) {
        this(data, new LinkedList<>());
    }
    
    public GraphVertex(T data, LinkedList<GraphVertex<T>> links) {
        this.data = data;
        this.links = links;
    }

    public T get() {
        return data;
    }

    public void set(T data) {
        this.data = data;
    }

    public LinkedList<GraphVertex<T>> getLinks() {
        return links;
    }
    
    public void addLinkTo(GraphVertex<T> vertex) {
        this.links.add(vertex);
    }
}
