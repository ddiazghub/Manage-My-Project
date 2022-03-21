/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package projectmanagementsoftware.wbs;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
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
    
    /**
     * Crea un nuevo entregable.
     * @param name Nombre del entregable.
     * @param description Descripción del entregable.
     */
    public Deliverable(String name, String description) {
        super(name);
        
        this.description = description;
    }

    public Deliverable(String name, String filePath, String description) {
        super(name, filePath);
        
        this.path = filePath;
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
    
    public static Deliverable create(String name, String path, String description) {
        LinkedList<String> filePath = LinkedList.split(path, "/");
        filePath.add("wbs", 1);
        
        File file = FileHelpers.get(filePath.join("/"));
        
        try {
            file.createNewFile();
            FileWriter writer = new FileWriter(file);
            writer.write(description);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        return new Deliverable(name, path, description);
    }
}
