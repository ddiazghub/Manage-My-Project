/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package projectmanagementsoftware.wbs;

import java.io.File;

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
    protected File file;
    
    /**
     * Crea un nuevo entregable/paquete de trabajo.
     * @param name Nombre del entregable/paquete de trabajo.
     */
    protected WBSNode(String name) {
        this.name = name;
        this.file = null;
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

    /**
     * Obtiene el archivo asociado al entregable/paquete de trabajo.
     * @return 
     */
    public File getFile() {
        return this.file;
    }
}
