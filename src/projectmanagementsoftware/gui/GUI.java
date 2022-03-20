/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package projectmanagementsoftware.gui;

import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import projectmanagementsoftware.linkedlist.LinkedList;
import projectmanagementsoftware.Project;

/**
 *
 * @author david
 */
public class GUI extends javax.swing.JFrame {
    private LinkedList<Project> projects;
    private DefaultListModel<String> memberListModel;
    private DefaultTreeModel treeModel;
    /**
     * Creates new form GUI
     */
    public GUI() {
        initComponents();
        
        this.projects = Project.load();
        this.memberListModel = new DefaultListModel<>();
        this.projectMembersList.setModel(this.memberListModel);
        this.mainContentTabPane.removeAll();
        DefaultMutableTreeNode treeRoot = new DefaultMutableTreeNode("Proyectos");
        this.treeModel = new DefaultTreeModel(new DefaultMutableTreeNode("Proyectos"));
        this.wbsTree.setModel(this.treeModel);
        this.updateUI();
    }

    public void updateUI() {
        DefaultMutableTreeNode treeRoot = (DefaultMutableTreeNode) this.treeModel.getRoot();
        treeRoot.removeAllChildren();
        
        this.projects.forEach(project -> {
            treeRoot.add(project.getWbs().toJTree());
        });
        
        this.treeModel.reload();
        this.wbsTree.expandRow(0);
    }
    
    public void showCreateProjectDialog() {
        this.projectNameField.setText("");
        this.addMemberField.setText("");
        this.memberListModel.clear();
        this.newProjectDialog.setLocationRelativeTo(this);
        this.newProjectDialog.setVisible(true);
    }
    
    public void showError(String message) {
        JOptionPane.showMessageDialog(null, message, "Error", JOptionPane.ERROR_MESSAGE);
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        newProjectDialog = new javax.swing.JDialog();
        addMemberField = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        projectMembersList = new javax.swing.JList<>();
        jLabel2 = new javax.swing.JLabel();
        projectNameField = new javax.swing.JTextField();
        addMemberButton = new javax.swing.JButton();
        cancelNewProjectButton = new javax.swing.JButton();
        confirmNewProjectButton = new javax.swing.JButton();
        removeMemberButton = new javax.swing.JButton();
        header = new javax.swing.JPanel();
        newProjectButton = new javax.swing.JButton();
        wbsButton = new javax.swing.JButton();
        scheduleButton = new javax.swing.JButton();
        addWorkPackage = new javax.swing.JButton();
        addDeliverable = new javax.swing.JButton();
        panel1 = new javax.swing.JPanel();
        sidebar = new javax.swing.JPanel();
        fileExplorerPanel = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        wbsTree = new javax.swing.JTree();
        idkPanel = new javax.swing.JPanel();
        panel2 = new javax.swing.JPanel();
        mainContentTabPane = new javax.swing.JTabbedPane();
        jScrollPane1 = new javax.swing.JScrollPane();
        jPanel1 = new javax.swing.JPanel();

        newProjectDialog.setTitle("Nuevo Proyecto");
        newProjectDialog.setAlwaysOnTop(true);
        newProjectDialog.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        newProjectDialog.setMaximumSize(new java.awt.Dimension(365, 364));
        newProjectDialog.setMinimumSize(new java.awt.Dimension(365, 364));
        newProjectDialog.setModal(true);
        newProjectDialog.setPreferredSize(new java.awt.Dimension(365, 364));
        newProjectDialog.setResizable(false);
        newProjectDialog.getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        addMemberField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addMemberFieldActionPerformed(evt);
            }
        });
        newProjectDialog.getContentPane().add(addMemberField, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 110, 180, 30));

        jLabel1.setText("Integrantes");
        newProjectDialog.getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 90, -1, -1));

        jScrollPane3.setViewportView(projectMembersList);

        newProjectDialog.getContentPane().add(jScrollPane3, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 140, 260, 120));

        jLabel2.setText("Nombre del proyecto");
        newProjectDialog.getContentPane().add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 30, -1, -1));
        newProjectDialog.getContentPane().add(projectNameField, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 50, 260, 30));

        addMemberButton.setText("+");
        addMemberButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addMemberButtonActionPerformed(evt);
            }
        });
        newProjectDialog.getContentPane().add(addMemberButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 110, 40, 30));

        cancelNewProjectButton.setText("Cancelar");
        cancelNewProjectButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelNewProjectButtonActionPerformed(evt);
            }
        });
        newProjectDialog.getContentPane().add(cancelNewProjectButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 280, 120, 40));

        confirmNewProjectButton.setText("Confirmar");
        confirmNewProjectButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                confirmNewProjectButtonActionPerformed(evt);
            }
        });
        newProjectDialog.getContentPane().add(confirmNewProjectButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 280, 120, 40));

        removeMemberButton.setText("-");
        removeMemberButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                removeMemberButtonActionPerformed(evt);
            }
        });
        newProjectDialog.getContentPane().add(removeMemberButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 110, 40, 30));

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        header.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        header.setPreferredSize(new java.awt.Dimension(1280, 80));
        header.setLayout(new javax.swing.BoxLayout(header, javax.swing.BoxLayout.LINE_AXIS));

        newProjectButton.setText("Nuevo Proyecto");
        newProjectButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                newProjectButtonActionPerformed(evt);
            }
        });
        header.add(newProjectButton);

        wbsButton.setText("EDT");
        header.add(wbsButton);

        scheduleButton.setText("Cronograma");
        header.add(scheduleButton);

        addWorkPackage.setText("Nuevo Paquete de trabajo");
        addWorkPackage.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addWorkPackageActionPerformed(evt);
            }
        });
        header.add(addWorkPackage);

        addDeliverable.setText("Nuevo Entregable");
        addDeliverable.setToolTipText("");
        header.add(addDeliverable);

        getContentPane().add(header, java.awt.BorderLayout.NORTH);

        panel1.setLayout(new javax.swing.BoxLayout(panel1, javax.swing.BoxLayout.LINE_AXIS));

        sidebar.setMaximumSize(new java.awt.Dimension(250, 640));
        sidebar.setMinimumSize(new java.awt.Dimension(200, 0));
        sidebar.setPreferredSize(new java.awt.Dimension(250, 640));
        sidebar.setLayout(new javax.swing.BoxLayout(sidebar, javax.swing.BoxLayout.Y_AXIS));

        fileExplorerPanel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        fileExplorerPanel.setLayout(new javax.swing.BoxLayout(fileExplorerPanel, javax.swing.BoxLayout.LINE_AXIS));

        jScrollPane2.setPreferredSize(new java.awt.Dimension(250, 316));

        javax.swing.tree.DefaultMutableTreeNode treeNode1 = new javax.swing.tree.DefaultMutableTreeNode("Proyectos");
        wbsTree.setModel(new javax.swing.tree.DefaultTreeModel(treeNode1));
        wbsTree.setPreferredSize(new java.awt.Dimension(250, 316));
        jScrollPane2.setViewportView(wbsTree);

        fileExplorerPanel.add(jScrollPane2);

        sidebar.add(fileExplorerPanel);

        idkPanel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        javax.swing.GroupLayout idkPanelLayout = new javax.swing.GroupLayout(idkPanel);
        idkPanel.setLayout(idkPanelLayout);
        idkPanelLayout.setHorizontalGroup(
            idkPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 248, Short.MAX_VALUE)
        );
        idkPanelLayout.setVerticalGroup(
            idkPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 307, Short.MAX_VALUE)
        );

        sidebar.add(idkPanel);

        panel1.add(sidebar);

        panel2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        panel2.setLayout(new java.awt.BorderLayout());

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1026, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 603, Short.MAX_VALUE)
        );

        jScrollPane1.setViewportView(jPanel1);

        mainContentTabPane.addTab("tab1", jScrollPane1);

        panel2.add(mainContentTabPane, java.awt.BorderLayout.CENTER);

        panel1.add(panel2);

        getContentPane().add(panel1, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void newProjectButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_newProjectButtonActionPerformed
        //String projectName = JOptionPane.showInputDialog(this, "Nombre del proyecto", "Nuevo proyecto", JOptionPane.QUESTION_MESSAGE);
        //projects.add(new Project(projectName));
        this.showCreateProjectDialog();
    }//GEN-LAST:event_newProjectButtonActionPerformed

    private void addMemberFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addMemberFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_addMemberFieldActionPerformed

    private void confirmNewProjectButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_confirmNewProjectButtonActionPerformed
        String projectName = this.projectNameField.getText();
        LinkedList<String> team = new LinkedList<>();
        
        for (Object member : this.memberListModel.toArray()) {
            team.add((String) member);
        }
        
        this.projects.add(Project.create(projectName, team));
        this.newProjectDialog.setVisible(false);
        this.updateUI();
    }//GEN-LAST:event_confirmNewProjectButtonActionPerformed

    private void cancelNewProjectButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelNewProjectButtonActionPerformed
        this.newProjectDialog.setVisible(false);
    }//GEN-LAST:event_cancelNewProjectButtonActionPerformed

    private void addMemberButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addMemberButtonActionPerformed
        String newMember = this.addMemberField.getText();
        
        if (newMember.equals("")) {
            showError("No se ha ingresado un nombre");
            return;
        }
        
        this.memberListModel.addElement(newMember);
        this.addMemberField.setText("");
    }//GEN-LAST:event_addMemberButtonActionPerformed

    private void removeMemberButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_removeMemberButtonActionPerformed
        for (String member : this.projectMembersList.getSelectedValuesList()) {
            this.memberListModel.removeElement(member);
        }
    }//GEN-LAST:event_removeMemberButtonActionPerformed

    private void addWorkPackageActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addWorkPackageActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_addWorkPackageActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(GUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(GUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(GUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(GUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new GUI().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addDeliverable;
    private javax.swing.JButton addMemberButton;
    private javax.swing.JTextField addMemberField;
    private javax.swing.JButton addWorkPackage;
    private javax.swing.JButton cancelNewProjectButton;
    private javax.swing.JButton confirmNewProjectButton;
    private javax.swing.JPanel fileExplorerPanel;
    private javax.swing.JPanel header;
    private javax.swing.JPanel idkPanel;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTabbedPane mainContentTabPane;
    private javax.swing.JButton newProjectButton;
    private javax.swing.JDialog newProjectDialog;
    private javax.swing.JPanel panel1;
    private javax.swing.JPanel panel2;
    private javax.swing.JList<String> projectMembersList;
    private javax.swing.JTextField projectNameField;
    private javax.swing.JButton removeMemberButton;
    private javax.swing.JButton scheduleButton;
    private javax.swing.JPanel sidebar;
    private javax.swing.JButton wbsButton;
    private javax.swing.JTree wbsTree;
    // End of variables declaration//GEN-END:variables
}
