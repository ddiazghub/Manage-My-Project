/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package projectmanagementsoftware.utils;

import java.io.File;
import projectmanagementsoftware.linkedlist.LinkedList;


/**
 *
 * @author david
 */
public class Validators {
    private static final char[] ILLEGAL_CHARACTERS = { '/', '\n', '\r', '\t', '\0', '\f', '`', '?', '*', '\\', '<', '>', '|', '\"', ':', '.', ',' };
    
    public static boolean isValidName(String name) {
        for (char c : ILLEGAL_CHARACTERS) {
            if (name.contains("" + c))
                return false;
        }
        
        return true;
    }
    
    public static boolean isUnique(String name, String path) {
        LinkedList<String> filePath = LinkedList.split(path, "/");
        
        if (filePath.length() > 1)
            filePath.add("wbs", 1);
        
        File dir = FileHelpers.get(filePath.join("/"));
        
        for (File file : dir.listFiles()) {
            String filename = file.getName();
            
            if (filename.equals(name) || filename.equals(name + ".txt"))
                return false;
        }
        
        return true;
    }
    
    public static boolean isDefined(String name) {
        if (name == null || name.length() == 0)
            return false;
        
        return true;
    }
}
