/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package projectmanagementsoftware.schedule;

import java.util.Date;
import projectmanagementsoftware.Project;
import projectmanagementsoftware.graph.Graph;
import projectmanagementsoftware.graph.GraphVertex;
import projectmanagementsoftware.linkedlist.LinkedList;
import projectmanagementsoftware.linkedlist.LinkedListNode;
import projectmanagementsoftware.utils.IVoidFunction1;
import projectmanagementsoftware.wbs.Deliverable;

/**
 * Clase que representa al cronograma asociado a un proyecto.
 * @author david
 */
public class Schedule extends Graph<Deliverable>{
    private LinkedList<GraphVertex<Deliverable>> referenceVertices;
    private Project project;
    
    public Schedule(Project project) {
        this.project = project;
        this.load();
    }
    
    public void load() {
        LinkedList<GraphVertex<Deliverable>> deliverables = this.project
            .getWbs()
            .getDeliverables()
            .map(deliverable -> {
            return new GraphVertex<>(deliverable);
        });
        
        this.referenceVertices = deliverables.copy();
        
        deliverables.forEach(deliverable -> {
            LinkedList<String> dependenciesPaths = deliverable.get().getDependencies();
            
            dependenciesPaths.forEach(path -> {
                GraphVertex<Deliverable> dependency = deliverables.where(d -> {
                    return path.equals(d.get().getPath());
                });
                
                if (dependency != null) {
                    deliverable.addLinkTo(dependency);
                    referenceVertices.remove(dependency);
                }
            });
        });
        
        LinkedList<GraphVertex> visited = new LinkedList<>();
        
        this.referenceVertices.forEach(vertex -> {
            loadDatesRecursively(vertex, visited);
        });
    }
    
    private Date loadDatesRecursively(GraphVertex<Deliverable> current, LinkedList<GraphVertex> visited) {
        if (visited.contains(current))
            return addDays(current.get().getStart(), current.get().getDuration());

        visited.add(current);
        
        if (current.getLinks().length() == 0)
            return addDays(current.get().getStart(), current.get().getDuration());
        
        LinkedListNode<GraphVertex<Deliverable>> container = new LinkedListNode<>(null);
        
        current.getLinks().forEach(link -> {
            Date dependencyEnd = loadDatesRecursively(link, visited);
            
            if (container.get() == null || dependencyEnd.after(current.get().getStart())) {
                current.get().setStart(dependencyEnd);
            }
        });
        
        return addDays(current.get().getStart(), current.get().getDuration());
    };
    
    private Date addDays(Date date, int days) {
        return new Date(date.getTime() + days * 1000 * 60 * 60 * 24);
    }
}
