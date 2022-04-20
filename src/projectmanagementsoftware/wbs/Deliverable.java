/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package projectmanagementsoftware.wbs;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import projectmanagementsoftware.linkedlist.LinkedList;
import projectmanagementsoftware.utils.FileHelpers;

/**
 * Entregable en un Proyecto.
 * @author david
 */
public class Deliverable extends WBSNode {
    /**
     * Descripción del entregable.
     */
    private String description;
    private double cost;
    private int duration;
    private LinkedList<String> dependencies;
    private Date start;
    
    /**
     * Crea un nuevo entregable.
     * @param name Nombre del entregable.
     * @param description Descripción del entregable.
     */
    public Deliverable(String name, String description) {
        super(name);
        
        this.description = description;
    }

    public Deliverable(String name, String filePath, String description, double cost, int duration, LinkedList<String> dependencies, Date start) {
        super(name, filePath);
        this.description = description;
        this.path = filePath;
        this.cost = cost;
        this.duration = duration;
        this.dependencies = dependencies;
        this.start = start;
    }
    
    /**
     * Obtiene la descripción del entregable.
     * @return Descripción del entregable.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Asigna la descripción del entregable.
     * @param description Nueva descripción del entregable.
     */
    public void setDescription(String description) {
        this.description = description;
    }

    public double getCost() {
        return cost;
    }

    public int getDuration() {
        return duration;
    }

    public LinkedList<String> getDependencies() {
        return dependencies;
    }
    
    public static Deliverable create(String name, String path, String description, double cost, int duration, LinkedList<String> dependencies, Date start) {
        LinkedList<String> filePath = LinkedList.split(path, "/");
        filePath.add("wbs", 1);
        
        File file = FileHelpers.get(filePath.join("/"));
        
        try {
            file.createNewFile();
            
            try (FileWriter writer = new FileWriter(file)) {
                String buffer = "duration=" + duration + "\n" + "cost=" + cost + "\n";
                
                if (dependencies.length() > 0)
                    buffer += "dependencies=" + dependencies.join(",") + "\n";
                
                buffer += "description=" + description;
                
                writer.write(buffer);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        return new Deliverable(name, path, description, cost, duration, dependencies, start);
    }

    public Date getStart() {
        return start;
    }

    public void setStart(Date start) {
        this.start = start;
    }
    
    
}
