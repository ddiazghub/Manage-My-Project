/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package projectmanagementsoftware.wbs;

import java.io.File;
import projectmanagementsoftware.utils.FileHelpers;

/**
 * Paquete de trabajo en un proyecto.
 * @author david
 */
public class WorkPackage extends WBSNode {
    /**
     * Crea un nuevo paquete de trabajo.
     * @param name Nombre del paquete de trabajo.
     */
    public WorkPackage(String name) {
        super(name);
    }
    
    public WorkPackage(String name, String filePath) {
        super(name, filePath);
    }
}
