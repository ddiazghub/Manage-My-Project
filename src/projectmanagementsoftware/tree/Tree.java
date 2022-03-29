/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package projectmanagementsoftware.tree;

import projectmanagementsoftware.linkedlist.IQueue;
import projectmanagementsoftware.linkedlist.LinkedList;
import projectmanagementsoftware.linkedlist.LinkedListNode;
import projectmanagementsoftware.utils.IVoidFunction1;
import projectmanagementsoftware.utils.IFunction1;
import projectmanagementsoftware.utils.IFunction2;

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

    public TreeNode<T> getRoot() {
        return root;
    }

    public void setRoot(TreeNode<T> root) {
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
    public void preorder(IVoidFunction1<T> function) {
        preorder(this.root, function, node -> false);
    }
    
    public void preorder(IVoidFunction1<T> function, IFunction1<T, Boolean> baseCase) {
        preorder(this.root, function, baseCase);
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
    public void inorder(IVoidFunction1<T> function) {
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
    public void postorder(IVoidFunction1<T> function) {
        postorder(this.root, function);
    }
    
    /**
     * Recorre el un subárbol cuya raíz es el nodo pasado como parámetro en preorden y ejecuta la función suministrada como parámetro para cada elemento durante el recorrido.
     * @param node Raíz del subárbol actual.
     * @param function El procedimiento a realizar para cada nodo.
     */
    public static <E> void preorder(TreeNode<E> node, IVoidFunction1<E> function, IFunction1<E, Boolean> baseCase) {
        if (node == null)
            return;
        
        function.action(node.get());
        
        if (baseCase.action(node.get()))
            return;
        
        node.getChildren().forEach(
            child -> preorder(child, function, baseCase)
        );
    }
    
    /**
     * Recorre el un subárbol cuya raíz es el nodo pasado como parámetro en inorden y ejecuta la función suministrada como parámetro para cada elemento durante el recorrido.
     * @param node Raíz del subárbol actual.
     * @param function El procedimiento a realizar para cada nodo.
     */
    public static <E> void inorder(TreeNode<E> node, IVoidFunction1<E> function) {
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
    public static <E> void postorder(TreeNode<E> node, IVoidFunction1<E> function) {
        if (node == null)
            return;
        
        node.getChildren().forEach(
            child -> postorder(child, function)
        );
        
        function.action(node.get());
    }
    
    // Recorrido del árbol por niveles.
    public void levelorder(IVoidFunction1<T> function) {
        IQueue<TreeNode<T>> queue = new LinkedList<>();
        
        if (!this.isEmpty()) {
            queue.enqueue(this.root);
            
            while (!queue.isEmpty()) {
                TreeNode<T> node = queue.dequeue();
                function.action(node.get());
                
                node.getChildren().forEach(child -> {
                    queue.enqueue(child);
                });
            }
        }
    }
    
    public int height() {
        return height(this.root);
    }
    
    public static <E> int height(TreeNode<E> node) {
        if (node == null)
            return -1;
        
        final LinkedListNode<Integer> container = new LinkedListNode<>(0);
        
        node.getChildren().forEach((child) -> {
            container.set(Math.max(container.get(), height(child)));
        });
        
        return 1 + container.get();
    }
    
    public Boolean isEmpty() {
        return this.root == null;
    }
    
    public <E> Tree<E> map(IFunction1<T, E> function) {
        Tree<E> tree = new Tree<>();
        
        if (!this.isEmpty()) {
            TreeNode<E> mapTreeRoot = new TreeNode<>(function.action(this.root.get()));

            this.root.getChildren().forEach(child -> {
                map(child, mapTreeRoot, function);
            });
            
            tree.setRoot(mapTreeRoot);
        }
        
        return tree;
    }
    
    public <E> Tree<E> mapWithLevelCount(IFunction2<T, E> function) {
        Tree<E> tree = new Tree<>();
        
        if (!this.isEmpty()) {
            E transformedElement = function.action(this.root.get(), 0);
            
            TreeNode<E> mapTreeRoot = new TreeNode<>(transformedElement);

            this.root.getChildren().forEach(child -> {
                map(child, mapTreeRoot, function, 1);
            });
            
            tree.setRoot(mapTreeRoot);
        }
        
        return tree;
    }
    
    private static <K, E> void map(TreeNode<K> current, TreeNode<E> mapTreeParent, IFunction1<K, E> function) {
        mapTreeParent.addChild(function.action(current.get()));
        
        current.getChildren().forEach(child -> {
            int lastChildIndex = mapTreeParent.getChildCount() - 1;
            map(child, mapTreeParent.getChild(lastChildIndex), function);
        });
    }
    
    private static <K, E> void map(TreeNode<K> current, TreeNode<E> mapTreeParent, IFunction2<K, E> function, int level) {
        mapTreeParent.addChild(function.action(current.get(), level));
        
        current.getChildren().forEach(child -> {
            int lastChildIndex = mapTreeParent.getChildCount() - 1;
            map(child, mapTreeParent.getChild(lastChildIndex), function, level + 1);
        });
    }
}
