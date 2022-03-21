/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package projectmanagementsoftware.wbs;

import java.io.File;
import java.util.Scanner;

/**
 * Clase abstracta que representa la información de un nodo en una EDT. Estos nodos pueden ser entregables o paquetes de trabajo.
 * @author david
 */
public abstract class WBSNode {
    /**
     * Nombre del entregable/paquete de trabajo.
     */
    protected String name;
    
    /**
     * Archivo asociado al entregable/paquete de trabajo.
     */
    protected String path;
    
    /**
     * Crea un nuevo entregable/paquete de trabajo.
     * @param filePath
     */
    protected WBSNode(String filePath) {
        this.path = filePath;
        this.name = null;
    }
    
    /**
     * Crea un nuevo entregable/paquete de trabajo.
     * @param name Nombre del entregable/paquete de trabajo.
     * @param path
     */
    protected WBSNode(String name, String path) {
        this.name = name;
        this.path = path;
    }
    
    /**
     * Obtiene el nombre del entregable/paquete de trabajo.
     * @return Nombre del entregable/paquete de trabajo.
     */
    public String getName() {
        return this.name;
    }

    /** 
     * Asigna un nuevo nombre al entregable/paquete de trabajo.
     * @param name Nombre del entregable/paquete de trabajo.
     */
    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }
    
    public String getProjectName() {
        return this.path.split("/")[0];
    }
    
    @Override
    public String toString() {
        return this.name;
    }
}
