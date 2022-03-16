/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package projectmanagementsoftware.tree;

import projectmanagementsoftware.linkedlist.LinkedListNode;
import projectmanagementsoftware.utils.IForEachFunction;

/**
 * Arbol N-Ario
 * @author david
 * @param <T> El tipo de datos que van a estar almacenados en el árbol.
 */
public class Tree<T> {
    /**
     * Raíz del árbol.
     */
    private TreeNode<T> root;

    /**
     * Crea un nuevo árbol vacío.
     */
    public Tree() {
        this(null);
    }
    
    /**
     * Crea un nuevo árbol a partir del nodo raíz suministrado
     * @param root Raíz que tendrá el árbol.
     */
    public Tree(TreeNode<T> root) {
        this.root = root;
    }
    
    /**
     * Recorre el árbol en preorden y ejecuta la función suministrada como parámetro para cada elemento durante el recorrido.
     * <br>
     * <pre>
     * {@code arbol.preorder((elemento) -> {
     *     // Aqui se escribe el cuerpo de la función. Ejemplo:
     *     System.out.println(elemento);
     * });
     * }
     * </pre>
     * @param function El procedimiento a realizar para cada nodo.
     */
    public void preorder(IForEachFunction<T> function) {
        preorder(this.root, function);
    }
    
    /**
     * Recorre el árbol en inorden y ejecuta la función suministrada como parámetro para cada elemento durante el recorrido.
     * <br>
     * <pre>
     * {@code arbol.inorder((elemento) -> {
     *     // Aqui se escribe el cuerpo de la función. Ejemplo:
     *     System.out.println(elemento);
     * });
     * }
     * </pre>
     * @param function El procedimiento a realizar para cada nodo.
     */
    public void inorder(IForEachFunction<T> function) {
        inorder(this.root, function);
    }
    
    /**
     * Recorre el árbol en postorden y ejecuta la función suministrada como parámetro para cada elemento durante el recorrido.
     * <br>
     * <pre>
     * {@code arbol.postorder((elemento) -> {
     *     // Aqui se escribe el cuerpo de la función. Ejemplo:
     *     System.out.println(elemento);
     * });
     * }
     * </pre>
     * @param function El procedimiento a realizar para cada nodo.
     */
    public void postorder(IForEachFunction<T> function) {
        postorder(this.root, function);
    }
    
    /**
     * Recorre el un subárbol cuya raíz es el nodo pasado como parámetro en preorden y ejecuta la función suministrada como parámetro para cada elemento durante el recorrido.
     * @param node Raíz del subárbol actual.
     * @param function El procedimiento a realizar para cada nodo.
     */
    public static <E> void preorder(TreeNode<E> node, IForEachFunction<E> function) {
        if (node == null)
            return;
        
        function.action(node.get());
        node.getChildren().forEach(
            child -> preorder(child, function)
        );
    }
    
    /**
     * Recorre el un subárbol cuya raíz es el nodo pasado como parámetro en inorden y ejecuta la función suministrada como parámetro para cada elemento durante el recorrido.
     * @param node Raíz del subárbol actual.
     * @param function El procedimiento a realizar para cada nodo.
     */
    public static <E> void inorder(TreeNode<E> node, IForEachFunction<E> function) {
        if (node == null)
            return;
        
        node.getChildren().forEachBetween(0, node.getChildCount() / 2, 
            child -> inorder(child, function)
        );
        
        function.action(node.get());
        
        node.getChildren().forEachBetween(node.getChildCount() / 2, node.getChildCount(), 
            child -> inorder(child, function)
        );
    }
    
    /**
     * Recorre el un subárbol cuya raíz es el nodo pasado como parámetro en preorden y ejecuta la función suministrada como parámetro para cada elemento durante el recorrido.
     * @param node Raíz del subárbol actual.
     * @param function El procedimiento a realizar para cada nodo.
     */
    public static <E> void postorder(TreeNode<E> node, IForEachFunction<E> function) {
        if (node == null)
            return;
        
        node.getChildren().forEach(
            child -> postorder(child, function)
        );
        
        function.action(node.get());
    }
}
