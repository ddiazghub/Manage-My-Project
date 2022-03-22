/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package projectmanagementsoftware;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import projectmanagementsoftware.linkedlist.LinkedList;
import projectmanagementsoftware.schedule.Schedule;
import projectmanagementsoftware.utils.FileHelpers;
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
    private Project(String name, LinkedList<String> team) {
        this.name = name;
        this.team = team;
        this.schedule = new Schedule();
        this.reload();
    }
    
    private void reload() {
        this.wbs = WorkBreakdownStructure.load(this);
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
    
    public static void changeProps(String name, LinkedList<String> team) {
        try {
            File projectProps = FileHelpers.get(name + "/project.txt");
            FileWriter writer = new FileWriter(projectProps);
            String buffer = "name=" + name + "\n" + "team=" + team.join(",");
            writer.write(buffer);
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static Project create(String name, LinkedList<String> team) {
        File root = FileHelpers.get(name);
        
        if (root.exists())
            return null;
        
        root.mkdir();
            
        try {
            FileHelpers.get(name + "/wbs").mkdir();
            FileHelpers.get(name + "/schedule").mkdir();
            FileHelpers.get(name + "/EDT.txt").createNewFile();
            File projectProps = FileHelpers.get(name + "/project.txt");
            projectProps.createNewFile();
            changeProps(name, team);
        } catch(IOException e) {
            e.printStackTrace();
        }
        
        return new Project(name, team);
    }
    
    public static LinkedList<Project> load() {
        LinkedList<Project> projects = new LinkedList<>();
        File file = FileHelpers.get("");
        
        if (!file.exists())
            file.mkdir();
        
        try {
            for (File child : file.listFiles()) {
                File projectProps = FileHelpers.get(child.getName() + "/project.txt");
                Scanner reader = new Scanner(projectProps);
                
                String name = child.getName();
                LinkedList<String> team = new LinkedList<>();
                
                while (reader.hasNextLine()) {
                    String[] line = reader.nextLine().split("=");
                    
                    if (line.length == 2 && line[0].equals("team")) {
                        team = LinkedList.split(line[1], ",");
                    }
                }
                
                projects.add(new Project(name, team));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        return projects;
    }
}
