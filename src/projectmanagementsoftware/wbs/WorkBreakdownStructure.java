package projectmanagementsoftware.wbs;

import projectmanagementsoftware.Project;
import projectmanagementsoftware.tree.Tree;
import projectmanagementsoftware.tree.TreeNode;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 * EDT de un proyecto. Representada mediante un árbol N-Ario.
 * @author david
 */
public class WorkBreakdownStructure extends Tree<WBSNode> {
    /**
     * El proyecto asociado a la EDT.
     */
    private Project project;
    
    /**
     * Crea una nueva EDT para el proyecto pasado como parámetro.
     * @param project Proyecto para el cual es la EDT.
     */
    public WorkBreakdownStructure(Project project) {
        super(new TreeNode<>(new WorkPackage(project.getName())));
        
        this.project = project;
    }

    /**
     * Obtiene el proyecto asociado a la EDT.
     * @return 
     */
    public Project getProject() {
        return project;
    }
}
