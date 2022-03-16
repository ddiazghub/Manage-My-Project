/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package projectmanagementsoftware;

import projectmanagementsoftware.linkedlist.LinkedList;
import projectmanagementsoftware.schedule.Schedule;
import projectmanagementsoftware.wbs.WorkBreakdownStructure;

/**
 * Clase que representa a un proyecto.
 * @author david
 */
public class Project {
    /**
     * Nombre del proyecto
    */
    private String name;
    
    /**
     * Lista que contiene a los nombres de los integrantes del proyecto
     */
    private LinkedList<String> team;
    
    /**
     * EDT general del proyecto
     */
    private WorkBreakdownStructure wbs;
    
    /**
     * Cronograma del proyecto
     */
    private Schedule schedule;

    /**
     * Crea un nuevo proyecto con el nombre suministrado como parámetro.
     * @param name Nombre del proyecto
    */
    public Project(String name) {
        this.name = name;
        this.team = new LinkedList<>();
        this.wbs = new WorkBreakdownStructure(this);
        this.schedule = new Schedule();
    }

    /**
     * Obtiene el nombre del proyecto.
     * @return Nombre del proyecto
     */
    public String getName() {
        return name;
    }

    /**
     * Le da un nuevo nombre al proyecto.
     * @param name Nombre del proyecto
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Obtiene al equipo asignado para el proyecto.
     * @return El equipo del proyecto
     */
    public LinkedList<String> getTeam() {
        return team;
    }

    /**
     * Obtiene la EDT del proyecto.
     * @return EDT del proyecto
     */
    public WorkBreakdownStructure getWbs() {
        return wbs;
    }

    /**
     * Obtiene el cronograma del proyecto.
     * @return Cronograma del proyecto
     */
    public Schedule getSchedule() {
        return schedule;
    }
}
