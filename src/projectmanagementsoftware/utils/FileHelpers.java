/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package projectmanagementsoftware.utils;

import java.io.File;

/**
 *
 * @author david
 */
public class FileHelpers {
    public static String BASEPATH = System.getenv("HOMEPATH") + "/pms-projects/";
    
    public static File get(String path) {
        return new File(BASEPATH + path);
    }
    
    public static void clearDirectory(File dir) {
        for (File file : dir.listFiles()) {
            clearRecursively(file);
        }
    }
    
    private static void clearRecursively(File file) {
        if (file.isDirectory()) {
            for (File child : file.listFiles()) {
                clearRecursively(child);
            }
        }
        
        file.delete();
    }
}
