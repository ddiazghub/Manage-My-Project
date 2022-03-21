/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package projectmanagementsoftware.gui;

import java.awt.Color;
import java.awt.Component;
import java.net.URL;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeCellRenderer;
import projectmanagementsoftware.wbs.Deliverable;
import projectmanagementsoftware.wbs.WBSNode;
import projectmanagementsoftware.wbs.WorkPackage;

/**
 *
 * @author david
 */
public class WBSTreeCellRenderer extends DefaultTreeCellRenderer {
    @Override
    public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus) {
        DefaultTreeCellRenderer renderer = (DefaultTreeCellRenderer) super.getTreeCellRendererComponent(tree, value, leaf, expanded, leaf, row, hasFocus);
        
        WBSNode node = (WBSNode) ((DefaultMutableTreeNode) value).getUserObject();
        
        if (node instanceof WorkPackage) {
            Icon icon = new ImageIcon(this.getClass().getResource("/res/icons/folder.png"));
            renderer.setIcon(icon);
        }
        
        if (node instanceof Deliverable) {
            Icon icon = new ImageIcon(this.getClass().getResource("/res/icons/file.png"));
            renderer.setIcon(icon);
        }
        
        renderer.setForeground(Color.BLACK);
        
        return renderer;
    }
}
