/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package projectmanagementsoftware.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import projectmanagementsoftware.Project;
import projectmanagementsoftware.linkedlist.IStack;
import projectmanagementsoftware.linkedlist.LinkedList;
import projectmanagementsoftware.linkedlist.LinkedListNode;
import projectmanagementsoftware.tree.Tree;
import projectmanagementsoftware.tree.TreeNode;
import projectmanagementsoftware.utils.Tuple;
import projectmanagementsoftware.wbs.WBSNode;
import projectmanagementsoftware.wbs.WorkPackage;

/**
 *
 * @author david
 */
public class WBSAnimationPanel extends javax.swing.JPanel {
    private static int NODE_SEPARATION_X = 10;
    private static int NODE_SEPARATION_Y = 40;
    private static Dimension NODESIZE = new Dimension(154, 55);
    
    private Project project;
    private Tree<WBSDrawableNode> tree;
    private WBSDrawableNode selected;
    private WBSDrawableNode visited;
    private MouseListener secondaryListener;
    
    // Los límites del espacio que abarca el árbol
    private float maxX;
    private int maxY;
    
    /**
     * Creates new form WBSAnimationPanel
     */
    public WBSAnimationPanel(Project project) {
        initComponents();
        
        this.secondaryListener = null;
        this.project = project;
        this.maxX = 0f;
        this.maxY = 0;
        this.selected = null;
        this.tree = null;
    }

    public WBSDrawableNode getSelected() {
        return this.selected;
    }
    
    private void setInitialPositions() {
        Tree<WBSNode> wbs = project.getWbs();
        this.tree = new Tree<>(this.setInitialPositions(wbs.getRoot(), 0, 0));
    }
    
    private TreeNode<WBSDrawableNode> setInitialPositions(TreeNode<WBSNode> node, float x, float y) {
        TreeNode<WBSDrawableNode> newNode = new TreeNode<>(null);
        newNode.set(new WBSDrawableNode(node.get(), newNode, x, y));
        final WBSAnimationPanel treeComponent = this;
        
        newNode.get().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Object source = e.getSource();
                WBSDrawableNode node = (WBSDrawableNode) source;
                treeComponent.setSelected(node);
                treeComponent.secondaryListener.mouseClicked(e);
            }
        });
            
        newNode.get().addExpandMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                WBSDrawableNode node = (WBSDrawableNode) ((JButton) e.getSource()).getParent();
                node.setExpanded(!node.isExpanded());
                treeComponent.reloadComponent();
            }

        });
        
        LinkedListNode<Integer> count = new LinkedListNode<>(0);
        
        node.getChildren().forEach(child -> {
            newNode.addChild(setInitialPositions(child, x + count.get(), y + 1));
            count.set(count.get() + 1);
        });
        
        if (y > this.maxY)
            this.maxY = Math.round(y);
        
        return newNode;
    }
    
    public void reloadComponent() {
        this.animationPanel.removeAll();
        Dimension size = this.animationPanel.getPreferredSize();
        
        this.tree.preorder(node -> {
            int x = this.getXInPixels(node.getXPos());
            int y = this.getYInPixels(node.getYPos());
            
            LinkedList<TreeNode<WBSDrawableNode>> children = node.getNode().getChildren();
            
            if (node.isExpanded() && children.length() > 0) {
                float minX = children.getHead().get().get().getXPos();
                float maxX = children.getTail().get().get().getXPos();

                int min = this.getXInPixels(minX);
                int max = this.getXInPixels(maxX);
                
                JPanel line1 = new JPanel();
                JPanel line2 = new JPanel();
                line1.setBackground(Color.BLACK);
                line2.setBackground(Color.BLACK);
                line1.setBounds(x + NODESIZE.width / 2, y + NODESIZE.height, 4, NODE_SEPARATION_Y / 2);
                line2.setBounds(min + NODESIZE.width / 2, y + NODESIZE.height + NODE_SEPARATION_Y / 2, max - min, 4);
                
                children.forEach(child -> {
                    int childX = this.getXInPixels(child.get().getXPos());
                    JPanel line3 = new JPanel();
                    line3.setBackground(Color.BLACK);
                    line3.setBounds(childX + NODESIZE.width / 2, y + NODESIZE.height + NODE_SEPARATION_Y / 2, 4, NODE_SEPARATION_Y / 2);
                    this.animationPanel.add(line3);
                });
                this.animationPanel.add(line1);
                this.animationPanel.add(line2);
            }
            
            this.animationPanel.add(node);
            node.setBounds(x, y, NODESIZE.width, NODESIZE.height);
        }, node -> !node.isExpanded());
        
        this.revalidate();
        this.repaint();
    }
    
    public int getXInPixels(float x) {
        return this.animationPanel.getPreferredSize().width / 2 - NODESIZE.width / 2 + Math.round(x * (NODESIZE.width + NODE_SEPARATION_X));
    }
    
    public int getYInPixels(float y) {
        return 25 + Math.round(y * (NODESIZE.height + NODE_SEPARATION_Y));
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }
    
    
    
    public Tree<WBSDrawableNode> getTree() {
        return this.tree;
    }
    
    public void setVisited(WBSDrawableNode visited) {
        if (this.visited != null) {
            this.visited.setVisited(false);
        }
        
        this.visited = visited;
        
        if (this.visited != null) {
            this.visited.setVisited(true);
        }
    }
    
    // Visita un nuevo nodo y lo pinta de color verde;
    public void setSelected(WBSDrawableNode selected) {
        if (this.selected != null) {
            this.selected.setSelected(false);
        }
        
        this.selected = selected;
        
        if (this.selected != null) {
            this.selected.setSelected(true);
        }
    }
    
    // Procesa el árbol para posteriomente ser dibujado
    public void revalidateTree() {
        this.maxX = 0f;
        this.setInitialPositions();
        this.fixNodeCollisions();
        this.centerTree();
        
        int width = Math.round(200 + (this.maxX + 1) * (NODE_SEPARATION_X + NODESIZE.width));
        int height = Math.round(100 + (this.maxY + 1) * (NODE_SEPARATION_Y + NODESIZE.height));
        this.animationPanel.setPreferredSize(new Dimension(width, height));
    }
    
    public void addSecondaryMouseListener(MouseListener listener) {
        this.secondaryListener = listener;
    }
    
    private void fixNodeCollisions() {
        LinkedList<Float> levelMaxX = new LinkedList<>();
        
        for (int i = 0; i < this.maxY + 1; i++) {
            levelMaxX.add(-1f);
        }
        
        this.fixNodeCollisions(this.tree.getRoot(), levelMaxX);
    }
    
    // Mueve los nodos para que no existan nodos que se posicionen sobre otros nodos y que los nodos estén separados. Al final centra los nodos sobre sus hijos
    private void fixNodeCollisions(TreeNode<WBSDrawableNode> node, LinkedList<Float> levelMaxX) {
        if (node == null)
            return;
        
        float x = node.get().getXPos();
        float y = node.get().getYPos();
        float leftX = levelMaxX.get(Math.round(y));

        if (x < leftX + 1) {
            float shift = leftX - x + 1;
            this.shiftX(node, shift);
            x = node.get().getXPos();
        }
        
        node.getChildren().forEach(child -> {
            this.fixNodeCollisions(child, levelMaxX);
        });
        
        if (node.getChildCount() > 0) {
            LinkedList<TreeNode<WBSDrawableNode>> children = node.getChildren();
            float min = children.getHead().get().get().getXPos();
            float max = children.getTail().get().get().getXPos();
            float newXPos = min + (max - min) / 2;
            node.get().setXPos(newXPos);
            x = newXPos;
        }
        
        levelMaxX.set(x, Math.round(y));
        
        if (x > this.maxX) {
            this.maxX = x;
        }
    }
    
    // Desplaza todos los nodos en el árbol para que este quede centrado
    private void centerTree() {
        float shift = this.maxX / 2;
        
        if (shift > 0) {
            this.shiftX(this.tree.getRoot(), -shift);
        }
    }
    
    // Desplaza un nodo y todos sus hijos
    private void shiftX(TreeNode<WBSDrawableNode> node, float shift) {
        if (node == null || shift == 0)
            return;
        
        node.get().setXPos(node.get().getXPos() + shift);
        
        node.getChildren().forEach(child -> {
            this.shiftX(child, shift);
        });
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        scrollPane = new javax.swing.JScrollPane();
        animationPanel = new javax.swing.JPanel();

        setPreferredSize(new java.awt.Dimension(1026, 603));
        setLayout(new javax.swing.BoxLayout(this, javax.swing.BoxLayout.LINE_AXIS));

        scrollPane.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        scrollPane.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setPreferredSize(new java.awt.Dimension(1026, 603));

        animationPanel.setLayout(null);
        scrollPane.setViewportView(animationPanel);

        add(scrollPane);
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel animationPanel;
    private javax.swing.JScrollPane scrollPane;
    // End of variables declaration//GEN-END:variables
}
