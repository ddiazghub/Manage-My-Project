package projectmanagementsoftware.wbs;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import javax.swing.tree.DefaultMutableTreeNode;
import projectmanagementsoftware.Project;
import projectmanagementsoftware.tree.Tree;
import projectmanagementsoftware.tree.TreeNode;
import projectmanagementsoftware.utils.FileHelpers;

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
    private WorkBreakdownStructure(Project project) {
        super(new TreeNode<>(new WorkPackage(project.getName(), project.getName())));
        
        this.project = project;
    }

    /**
     * Obtiene el proyecto asociado a la EDT.
     * @return 
     */
    public Project getProject() {
        return project;
    }
    
    public void save() {
        File root = FileHelpers.get(this.project.getName() + "/wbs");
        FileHelpers.clearDirectory(root);
        
        this.getRoot().getChildren().forEach(
            child -> save(child, root.getPath() + "/" + child.get().getName())
        );
    }
    
    public DefaultMutableTreeNode toJTree() {
        DefaultMutableTreeNode root = new DefaultMutableTreeNode(this.project.getName());
        
        this.getRoot().getChildren().forEach(child -> {
            root.add(toJTree(child));
        });
        
        return root;
    }
    
    private static DefaultMutableTreeNode toJTree(TreeNode<WBSNode> node) {
        DefaultMutableTreeNode jTreeNode = new DefaultMutableTreeNode(node.get().getName());
        
        node.getChildren().forEach(child -> {
            jTreeNode.add(toJTree(child));
        });
        
        return jTreeNode;
    }
    
    private static void save(TreeNode<WBSNode> node, String path) {
        File file = FileHelpers.get(path);
        
        if (node.get() instanceof WorkPackage) {
            file.mkdir();
            
            node.getChildren().forEach(child -> {
                String childPath = path + "/" + child.get().getName();
                
                if (child.get() instanceof Deliverable)
                    childPath += ".txt";
                
                save(child, childPath);
            });
        }
        
        if (node.get() instanceof Deliverable) {
            try {
                file.createNewFile();
                FileWriter writer = new FileWriter(file);
                writer.write(((Deliverable) node.get()).getDescription());
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    
    public static WorkBreakdownStructure load(Project project) {
        File root = FileHelpers.get(project.getName() + "/wbs");
        WorkBreakdownStructure wbs = new WorkBreakdownStructure(project);
        
        for (File file : root.listFiles()) {
            wbs.getRoot().addChild(buildWBS(file, project.getName() + "/wbs/" + file.getName()));
        }
        
        return wbs;
    }
    
    private static TreeNode<WBSNode> buildWBS(File file, String path) {
        if (file.isDirectory()) {
            TreeNode<WBSNode> node = new TreeNode<>(new WorkPackage(file.getName(), path));
            
            for (File child : file.listFiles()) {
                node.addChild(buildWBS(child, path + "/" + child.getName()));
            }
            
            return node;
        }
        
        try {
            Scanner reader = new Scanner(file);
            String description = "";
            
            while (reader.hasNext()) {
                description += reader.nextLine();
            }
            
            Deliverable deliverable = new Deliverable(file.getName(), path + "/" + file.getName(), description);
            TreeNode<WBSNode> node = new TreeNode<>(deliverable);
            
            return node;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }
}
