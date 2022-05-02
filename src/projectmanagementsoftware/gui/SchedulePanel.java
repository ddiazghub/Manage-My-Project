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
import java.util.Date;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import projectmanagementsoftware.Project;
import projectmanagementsoftware.graph.Graph;
import projectmanagementsoftware.graph.GraphVertex;
import projectmanagementsoftware.linkedlist.IStack;
import projectmanagementsoftware.linkedlist.LinkedList;
import projectmanagementsoftware.linkedlist.LinkedListNode;
import projectmanagementsoftware.schedule.Schedule;
import projectmanagementsoftware.tree.Tree;
import projectmanagementsoftware.tree.TreeNode;
import projectmanagementsoftware.utils.ColorHelpers;
import projectmanagementsoftware.utils.DateHelpers;
import static projectmanagementsoftware.utils.DateHelpers.dayDifference;
import projectmanagementsoftware.utils.Tuple;
import projectmanagementsoftware.wbs.Deliverable;
import projectmanagementsoftware.wbs.WBSNode;
import projectmanagementsoftware.wbs.WorkPackage;

/**
 *
 * @author david
 */
public class SchedulePanel extends javax.swing.JPanel {
    private static int NODE_SEPARATION_X = 10;
    private static int NODE_SEPARATION_Y = 40;
    private static Dimension NODESIZE = new Dimension(154, 55);
    
    private Project project;
    private LinkedList<ScheduleVertexBounds> vertexBounds;
    private LinkedList<GraphVertex<Deliverable>> referenceVertices;
    
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
    public SchedulePanel(Project project) {
        initComponents();
        
        this.vertexBounds = new LinkedList<>();
        this.secondaryListener = null;
        this.project = project;
        this.maxX = 0f;
        this.maxY = 0;
        this.selected = null;
        this.tree = null;
        this.revalidateSchedule();
    }

    public WBSDrawableNode getSelected() {
        return this.selected;
    }
    
    public void reload() {
        revalidateSchedule();
    }
    
    public void clear() {
        this.vertexBounds.clear();
        this.monthPanel.removeAll();
        this.animationPanel.removeAll();
        this.dayPanel.removeAll();
    }
    
    private void revalidateSchedule() {
        this.clear();
        Schedule schedule = this.project.getSchedule();
        
        if (schedule.getVertexCount() == 0)
            return;
        
        this.referenceVertices = schedule.getReferenceVertices();
        Date start = schedule.getStart();
        Date end = schedule.getEnd();
        
        System.out.println(start);
        System.out.println(end);
        int days = DateHelpers.dayDifference(start, end) + 1;
        int width =  days * 20;
        Dimension size = new Dimension(width, 40);
        Dimension dayLabelSize = new Dimension(20, 20);
        this.headerPanel.setPreferredSize(size);
        this.headerPanel.setMaximumSize(size);
        this.headerPanel.setMinimumSize(size);
        
        Date current = start;
        int cumulativeDays = 0;
        
        while (cumulativeDays < days) {
            int daysInMonth = DateHelpers.getDaysInMonth(current) - current.getDate() + 1;
            
            System.out.println(daysInMonth);
            Dimension monthLabelSize = end.after(DateHelpers.getStartOfNextMonth(current))
                    ? new Dimension(20 * daysInMonth, 20)
                    : new Dimension(20 * end.getDate() + 1, 20);
            
            JLabel label = new JLabel(DateHelpers.getMonthAsString(current), JLabel.CENTER);
            label.setBackground(new Color(204, 204, 204));
            label.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            label.setPreferredSize(monthLabelSize);
            label.setMinimumSize(monthLabelSize);
            label.setMaximumSize(monthLabelSize);
            label.setOpaque(true);
            this.monthPanel.add(label);
            current = DateHelpers.getStartOfNextMonth(current);
            cumulativeDays += daysInMonth;
        }
         
        current = start;
        
        for (int i = 0; i < days; i++) {
            JLabel label = new JLabel(Integer.toString(current.getDate()), JLabel.CENTER);
            label.setBackground(new Color(204, 204, 204));
            label.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            label.setPreferredSize(dayLabelSize);
            label.setMinimumSize(dayLabelSize);
            label.setMaximumSize(dayLabelSize);
            label.setOpaque(true);
            this.dayPanel.add(label);
            current = new Date(current.getTime() + DateHelpers.MILLISECONDS_PER_DAY);
        }
        
        for (int i = 0; i < schedule.getVertexCount(); i++) {
            GraphVertex<Deliverable> vertex = schedule.getVertex(i);
            ScheduleVertexBounds bounds = new ScheduleVertexBounds(10 + dayDifference(start, vertex.get().getStart()) * 20, 30 * i, vertex.get().getDuration() * 20, 20, vertex);
            this.vertexBounds.add(bounds);
        }       
                
        for (int i = 0; i < schedule.getVertexCount(); i++) {
            final ScheduleVertexBounds bounds = this.vertexBounds.get(i);
            Dimension nodeSize = new Dimension(bounds.getWidth(), bounds.getHeight());
            JLabel label = new JLabel(bounds.getVertex().get().getName(), JLabel.CENTER);
            label.setBackground(ColorHelpers.nextColor(0.5f, 0.95f));
            label.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            label.setPreferredSize(nodeSize);
            label.setMinimumSize(nodeSize);
            label.setMaximumSize(nodeSize);
            label.setBounds(10 + dayDifference(start, bounds.getVertex().get().getStart()) * 20, 30 * i, nodeSize.width, 20);
            label.setToolTipText(bounds.getVertex().get().getName());
            label.setOpaque(true);
            this.animationPanel.add(label);
        }
        
        for (int i = 0; i < this.vertexBounds.length(); i++) {
            final ScheduleVertexBounds bounds = this.vertexBounds.get(i);
            final int i2 = i;
            
            bounds.getVertex().getLinks().forEach(link -> {
                ScheduleVertexBounds linkBounds = this.vertexBounds.where(bound -> {
                    return bound.getVertex() == link;
                });
                
                Color color = ColorHelpers.nextColor(0.99f, 0.99f);
                JPanel line1 = new JPanel();
                JPanel line2 = new JPanel();
                JPanel arrow = new JPanel() {
                    public void paint(Graphics g) {
                        g.setColor(color);
                        g.fillPolygon(new int[] { 0, 12, 6 }, new int[] { 0, 0, 5 }, 3);
                        System.out.println(g);
                     }
                 };
                
                line1.setBackground(color);
                line2.setBackground(color);
                line1.setBounds(bounds.getX() + 10, linkBounds.getY() + 10, 3, bounds.getY() - linkBounds.getY());
                line2.setBounds(linkBounds.getX() + linkBounds.getWidth() - 10, linkBounds.getY() + 10, bounds.getX() - (linkBounds.getX() + linkBounds.getWidth()) + 20, 3);
                arrow.setBounds(bounds.getX() + 5, bounds.getY() - 5, 12, 5);
                
                this.animationPanel.add(line1);
                this.animationPanel.add(line2);
                this.animationPanel.add(arrow);
            });
        }
        
        this.dayPanel.revalidate();
        this.dayPanel.repaint();
        this.monthPanel.revalidate();
        this.monthPanel.repaint();
        this.animationPanel.revalidate();
        this.animationPanel.repaint();
        
    }
    
    private void setInitialPositions() {
        Tree<WBSNode> wbs = project.getWbs();
        this.tree = new Tree<>(this.setInitialPositions(wbs.getRoot(), 0, 0));
    }
    
    private TreeNode<WBSDrawableNode> setInitialPositions(TreeNode<WBSNode> node, float x, float y) {
        TreeNode<WBSDrawableNode> newNode = new TreeNode<>(null);
        newNode.set(new WBSDrawableNode(node.get(), newNode, x, y));
        final SchedulePanel treeComponent = this;
        
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
        this.monthPanel.removeAll();
        this.dayPanel.removeAll();
        
        
        
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
        mainContentPanel = new javax.swing.JPanel();
        animationPanel = new javax.swing.JPanel();
        headerPanel = new javax.swing.JPanel();
        monthPanel = new javax.swing.JPanel();
        dayPanel = new javax.swing.JPanel();

        setPreferredSize(new java.awt.Dimension(1026, 603));
        setLayout(new javax.swing.BoxLayout(this, javax.swing.BoxLayout.LINE_AXIS));

        scrollPane.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        scrollPane.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setPreferredSize(new java.awt.Dimension(1026, 603));

        mainContentPanel.setLayout(new java.awt.BorderLayout());

        animationPanel.setBackground(new java.awt.Color(204, 204, 204));
        animationPanel.setPreferredSize(new java.awt.Dimension(600, 500));
        animationPanel.setRequestFocusEnabled(false);
        animationPanel.setLayout(null);
        mainContentPanel.add(animationPanel, java.awt.BorderLayout.CENTER);

        headerPanel.setMaximumSize(new java.awt.Dimension(10000000, 40));
        headerPanel.setMinimumSize(new java.awt.Dimension(10, 40));
        headerPanel.setPreferredSize(new java.awt.Dimension(1000, 40));
        headerPanel.setLayout(new javax.swing.BoxLayout(headerPanel, javax.swing.BoxLayout.PAGE_AXIS));

        monthPanel.setBackground(new java.awt.Color(51, 51, 51));
        monthPanel.setMaximumSize(new java.awt.Dimension(100000, 100000));
        monthPanel.setPreferredSize(new java.awt.Dimension(100, 20));
        monthPanel.setLayout(new javax.swing.BoxLayout(monthPanel, javax.swing.BoxLayout.LINE_AXIS));
        headerPanel.add(monthPanel);

        dayPanel.setBackground(new java.awt.Color(255, 51, 102));
        dayPanel.setMaximumSize(new java.awt.Dimension(10000000, 1000000));
        dayPanel.setPreferredSize(new java.awt.Dimension(100, 20));
        dayPanel.setLayout(new javax.swing.BoxLayout(dayPanel, javax.swing.BoxLayout.LINE_AXIS));
        headerPanel.add(dayPanel);

        mainContentPanel.add(headerPanel, java.awt.BorderLayout.NORTH);

        scrollPane.setViewportView(mainContentPanel);

        add(scrollPane);
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel animationPanel;
    private javax.swing.JPanel dayPanel;
    private javax.swing.JPanel headerPanel;
    private javax.swing.JPanel mainContentPanel;
    private javax.swing.JPanel monthPanel;
    private javax.swing.JScrollPane scrollPane;
    // End of variables declaration//GEN-END:variables
}
