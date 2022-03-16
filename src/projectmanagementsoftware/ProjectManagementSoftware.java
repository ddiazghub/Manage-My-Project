/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package projectmanagementsoftware;

import projectmanagementsoftware.linkedlist.LinkedList;

/**
 * Main
 * @author david
 */
public class ProjectManagementSoftware {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        LinkedList<Integer> list;
    
        list = new LinkedList<>();
        
        for (int i = 0; i < 10; i++)
            list.add(i);
        
        
        list.forEachBetween(0, 10, e -> System.out.println(e*e));
    }
}
