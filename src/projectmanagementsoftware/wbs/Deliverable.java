/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package projectmanagementsoftware.wbs;

import java.io.File;

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
}
