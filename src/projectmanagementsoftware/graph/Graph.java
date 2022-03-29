/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package projectmanagementsoftware.graph;

import projectmanagementsoftware.linkedlist.IQueue;
import projectmanagementsoftware.linkedlist.LinkedList;
import projectmanagementsoftware.linkedlist.LinkedListOfLists;
import projectmanagementsoftware.utils.IVoidFunction1;

/**
 *
 * @author david
 */
public class Graph<T> {
    private LinkedList<GraphVertex<T>> vertices;
    
    public Graph() {
        this.vertices = new LinkedList<>();
    }
    
    public GraphVertex<T> getVertex(int index) {
        return this.vertices.get(index);
    }
    
    public void addVertex(GraphVertex<T> vertex) {
        this.vertices.add(vertex);
    }
    
    public void setReferenceVertex(GraphVertex<T> vertex) {
        int index = this.vertices.indexOf(vertex);
        this.setReferenceVertex(index);
    }
    
    public void setReferenceVertex(int index) {
        this.vertices.swap(0, index);
    }
    
    public void addEdge(GraphVertex<T> vertex1, GraphVertex<T> vertex2) {
        vertex1.addLinkTo(vertex2);
        vertex2.addLinkTo(vertex1);
    }
    
    public void addEdge(int index1, int index2) {
        this.addEdge(this.getVertex(index1), this.getVertex(index2));
    }
    
    public int getVertexCount() {
        return this.vertices.length();
    }
    
    public boolean isEmpty() {
        return this.vertices.isEmpty();
    }
    
    public void bfs(IVoidFunction1<T> function) {
        if (this.isEmpty())
            return;
        
        bfs(this.vertices.get(0), new LinkedList<>(), function);
    }
    
    public void dfs(IVoidFunction1<T> function) {
        if (this.isEmpty())
            return;
        
        LinkedList<GraphVertex<T>> visited = new LinkedList<>();
        IQueue<GraphVertex<T>> queue = new LinkedList<>();
        queue.enqueue(this.vertices.get(0));
        
        while (!queue.isEmpty()) {
            GraphVertex<T> vertex = queue.dequeue();
            function.action(vertex.get());
            visited.add(vertex);
            
            vertex.getLinks().forEach(link -> {
                queue.enqueue(link);
            });
        }
    }
    
    private static <E> void bfs(GraphVertex<E> current, LinkedList<GraphVertex> visited, IVoidFunction1<E> function) {
        if (visited.contains(current))
            return;
        
        visited.add(current);
        function.action(current.get());
        
        current.getLinks().forEach(link -> {
            bfs(link, visited, function);
        });
    }
}
