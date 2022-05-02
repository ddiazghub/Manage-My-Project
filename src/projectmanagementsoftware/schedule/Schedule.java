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
import projectmanagementsoftware.utils.DateHelpers;
import projectmanagementsoftware.utils.IVoidFunction1;
import projectmanagementsoftware.wbs.Deliverable;

/**
 * Clase que representa al cronograma asociado a un proyecto.
 * @author david
 */
public class Schedule extends Graph<Deliverable>{
    private LinkedList<GraphVertex<Deliverable>> referenceVertices;
    private Date start;
    private Date end;
    private Project project;
    
    public Schedule(Project project) {
        this.project = project;
        this.start = new Date(Long.MAX_VALUE);
        this.end = new Date(0);
        this.load();
    }
    
    public void load() {
        this.clear();
        
        LinkedList<GraphVertex<Deliverable>> deliverables = this.project
            .getWbs()
            .getDeliverables()
            .map(deliverable -> {
                return new GraphVertex<>(deliverable);
            });
        
        this.referenceVertices = deliverables.copy();
        
        this.referenceVertices.forEach(vertex -> {
            this.addVertex(vertex);
        });
        
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
        LinkedListNode<Date> projectEnd = new LinkedListNode<>(new Date(0));
        
        this.referenceVertices.forEach(vertex -> {
            loadDatesRecursively(vertex, visited);
            Date currentEnd = vertex.get().getEnd();
            
            if (currentEnd.getTime() > projectEnd.get().getTime())
                projectEnd.set(currentEnd);
        });
        
        this.referenceVertices.forEach(vertex -> {
            setDeadlinesRecursively(vertex, projectEnd.get());
        });
        
        this.vertices = this.vertices.sort(deliverable -> {
            return (int) (deliverable.get().getStart().getTime() / 1000);
        });
    }
    
    private Date loadDatesRecursively(GraphVertex<Deliverable> current, LinkedList<GraphVertex> visited) {
        if (visited.contains(current))
            return current.get().getEnd();

        visited.add(current);
        
        if (current.getLinks().length() == 0) {
            Date start = current.get().getStart();
            
            if (start.before(this.start))
                this.start = start;
                
            return current.get().getEnd();
        }
        
        current.getLinks().forEach(link -> {
            Date dependencyEnd = loadDatesRecursively(link, visited);

            if (dependencyEnd.getTime() > current.get().getStart().getTime()) {
                current.get().setStart(dependencyEnd);
            }
        });
        
        Date end = current.get().getEnd();
        
        if (end.after(this.end))
            this.end = end;
        
        return current.get().getEnd();
    };
    
    private void setDeadlinesRecursively(GraphVertex<Deliverable> current, Date deadline) {
        Date currentDeadline = current.get().getDeadline();
        
        if (currentDeadline == null || deadline.getTime() < currentDeadline.getTime())
            current.get().setDeadline(deadline);
        
        Date newDeadline = DateHelpers.addDays(deadline, -current.get().getDuration());
        
        current.getLinks().forEach(link -> {
            setDeadlinesRecursively(link, newDeadline);
        });
    };

    public LinkedList<GraphVertex<Deliverable>> getReferenceVertices() {
        return referenceVertices;
    }

    public Project getProject() {
        return project;
    }

    public Date getStart() {
        return start;
    }

    public Date getEnd() {
        return end;
    }
}
