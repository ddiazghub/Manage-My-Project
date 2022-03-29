/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package projectmanagementsoftware.tree;

import projectmanagementsoftware.linkedlist.LinkedList;

/**
 * Nodo de un árbol N-Ario.
 * @author david
 * @param <T> Tipo de dato que almacena el nodo.
 */
public class TreeNode<T> {
    /**
     * El dato del nodo.
     */
    private T data;
    
    /**
     * Lista enlazada que contiene a los hijos del nodo.
     */
    private LinkedList<TreeNode<T>> children;
    
    /**
     * Crea un nuevo nodo y le asigna el dato.
     * @param data El dato que tendrá el nodo.
     */
    public TreeNode(T data) {
        this.data = data;
        this.children = new LinkedList<>();
    }
    
    /**
     * Obtiene el dato almacenado en el nodo.
     * @return 
     */
    public T get() {
        return this.data;
    }
    
    public void set(T data) {
        this.data = data;
    }
    
    /**
     * Obtiene la lista de hijos del nodo.
     * @return 
     */
    public LinkedList<TreeNode<T>> getChildren() {
        return this.children;
    }
    
    /**
     * Obtiene el hijo en la posición index del nodo.
     * @param index La posición del hijo que se está buscando.
     * @return El hijo en la respectiva posición.
     * @throws IndexOutOfBoundsException Si se intenta buscar en una posición por fuera del espacio abarcado por la lista.
     */
    public TreeNode<T> getChild(int index) {
        return this.children.get(index);
    }
    
    public int getChildCount() {
        return this.children.length();
    }
    
    /**
     * Añade un nuevo hijo al nodo.
     * @param data El dato que tendrá el nuevo hijo que se añadirá.
     */
    public void addChild(TreeNode<T> child) {
        this.children.add(child);
    }
    
    public void addChild(T data) {
        this.addChild(new TreeNode<>(data));
    }
}
