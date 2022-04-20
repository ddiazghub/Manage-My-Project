/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package projectmanagementsoftware.graph;

import projectmanagementsoftware.linkedlist.IQueue;
import projectmanagementsoftware.linkedlist.LinkedList;
import projectmanagementsoftware.utils.IVoidFunction1;

/**
 *
 * @author david
 */
public class Graph<T> {
    protected LinkedList<GraphVertex<T>> vertices;
    protected GraphVertex<T> referenceVertex;
    
    public Graph() {
        this.vertices = new LinkedList<>();
        this.referenceVertex = null;
    }
    
    public GraphVertex<T> getVertex(int index) {
        return this.vertices.get(index);
    }
    
    public void addVertex(GraphVertex<T> vertex) {
        if (this.vertices.length() == 0)
            this.referenceVertex = vertex;
        
        this.vertices.add(vertex);
    }
    
    public void removeVertex(GraphVertex<T> vertex) {
        int index = this.vertices.indexOf(vertex);
        
        this.vertices.forEach(v -> {
            v.getLinks().remove(vertex);
        });
        
        this.vertices.remove(index);
        
        if (vertex == this.referenceVertex) {
            if (this.getVertexCount() == 0)
                this.referenceVertex = null;
            else {
                this.referenceVertex = this.vertices.get(0);
            }
        }
        
    }
    
    public void removeVertex(int index) {
        GraphVertex<T> toRemove = this.vertices.get(index);
        
        this.vertices.forEach(vertex -> {
            vertex.getLinks().remove(toRemove);
        });
        
        this.vertices.remove(index);
        
        if (this.getVertexCount() == 0)
            this.referenceVertex = null;
    }
    
    public void clear() {
        this.vertices.clear();
        this.referenceVertex = null;
    }
    
    public GraphVertex<T> getReferenceVertex() {
        return this.referenceVertex;
    }
    
    public void setReferenceVertex(GraphVertex<T> vertex) {
        int index = this.vertices.indexOf(vertex);
        
        if (index == -1)
            return;
        
        this.referenceVertex = vertex;
    }
    
    public void setReferenceVertex(int index) {
        this.referenceVertex = this.vertices.get(index);
    }
    
    public void addEdge(GraphVertex<T> vertex1, GraphVertex<T> vertex2) {
        vertex1.addLinkTo(vertex2);
        vertex2.addLinkTo(vertex1);
    }
    
    public void addEdge(int index1, int index2) {
        this.addEdge(this.getVertex(index1), this.getVertex(index2));
    }
    
    public void addDirectedEdge(GraphVertex<T> vertex1, GraphVertex<T> vertex2) {
        vertex1.addLinkTo(vertex2);
    }
    
    public int getVertexCount() {
        return this.vertices.length();
    }
    
    public boolean isEmpty() {
        return this.vertices.isEmpty();
    }
    
    /*
    public void traverse(TraversalAlgorithm algorithm, IVoidFunction1<T> function) {
        switch (algorithm) {
            case BFS:
                this.bfs(function);
                break;
                
            case DFS:
                this.dfs(function);
                break;
        }
    }
*/
    
    public void bfs(IVoidFunction1<T> function) {
        if (this.isEmpty())
            return;
        
        bfs(this.referenceVertex, new LinkedList<>(), function);
    }
    
    public void bfsNodes(IVoidFunction1<GraphVertex<T>> function) {
        if (this.isEmpty())
            return;
        
        bfsNodes(this.referenceVertex, new LinkedList<>(), function);
    }
    
    public void dfs(IVoidFunction1<T> function) {
        if (this.isEmpty())
            return;
        
        LinkedList<GraphVertex<T>> visited = new LinkedList<>();
        IQueue<GraphVertex<T>> queue = new LinkedList<>();
        visited.add(this.referenceVertex);
        queue.enqueue(this.referenceVertex);
        
        while (!queue.isEmpty()) {
            GraphVertex<T> vertex = queue.dequeue();
            function.action(vertex.get());
            
            vertex.getLinks().forEach(link -> {
                if (visited.contains(link))
                    return;
                
                queue.enqueue(link);
                visited.add(link);
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
    
    private static <E> void bfsNodes(GraphVertex<E> current, LinkedList<GraphVertex> visited, IVoidFunction1<GraphVertex<E>> function) {
        if (visited.contains(current))
            return;
        
        visited.add(current);
        function.action(current);
        
        current.getLinks().forEach(link -> {
            bfsNodes(link, visited, function);
        });
    }
}
