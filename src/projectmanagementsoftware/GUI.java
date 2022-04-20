/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package projectmanagementsoftware;

import java.awt.CardLayout;
import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListModel;
import javax.swing.DefaultListSelectionModel;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import projectmanagementsoftware.linkedlist.LinkedList;
import projectmanagementsoftware.Project;
import projectmanagementsoftware.gui.ProjectFileSystemTree;
import projectmanagementsoftware.gui.ProjectFileSystemTreeNode;
import projectmanagementsoftware.gui.SchedulePanel;
import projectmanagementsoftware.gui.WBSAnimationPanel;
import projectmanagementsoftware.gui.WBSDrawableNode;
import projectmanagementsoftware.linkedlist.LinkedListNode;
import projectmanagementsoftware.utils.FileHelpers;
import projectmanagementsoftware.utils.Validators;
import projectmanagementsoftware.wbs.Deliverable;
import projectmanagementsoftware.wbs.WBSNode;
import projectmanagementsoftware.wbs.WorkPackage;

/**
 *
 * @author david
 */
public class GUI extends javax.swing.JFrame {
    private LinkedList<Project> projects;
    private DefaultListModel<String> memberListModel;
    private ProjectFileSystemTree tree;
    private CardLayout cards;
    private LinkedList<String> tabs;
    private LinkedList<WBSAnimationPanel> wbsPanels;
    private Thread activeTraversalThread;
    private LinkedList<SchedulePanel> schedules;
    private DefaultListModel<String> dependenciesModel;
    
    /**
     * Creates new form GUI
     */
    public GUI() {
        initComponents();
        this.tabs = new LinkedList<>();
        this.projects = Project.load();
        this.memberListModel = new DefaultListModel<>();
        this.projectMembersList.setModel(this.memberListModel);
        this.mainContentTabPane.removeAll();
        this.tree = new ProjectFileSystemTree();
        this.fileExplorerPanel.add(this.tree);
        this.cards = (CardLayout) this.nodeDataPanel.getLayout();
        this.cards.show(this.nodeDataPanel, "none");
        this.wbsPanels = new LinkedList<>();
        this.schedules = new LinkedList<>();
        this.savePathLabel.setText("Los archivos se guardan en: C:" + Paths.get(FileHelpers.BASEPATH));
        this.activeTraversalThread = null;
        this.dependenciesModel = new DefaultListModel();
        
        this.dependenciesList.setModel(this.dependenciesModel);
        
        this.dependenciesList.setSelectionModel(new DefaultListSelectionModel() {
            @Override
            public void setSelectionInterval(int index0, int index1) {
                if(super.isSelectedIndex(index0)) {
                    super.removeSelectionInterval(index0, index1);
                }
                else {
                    super.addSelectionInterval(index0, index1);
                }
            }
        });
        
        if (this.projects.length() > 0)
            this.setProjectData(this.projects.get(0).getName());
        
        final GUI gui = this;
        
        this.tree.setSecondaryClickListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                WBSNode node = (WBSNode) e.getSource();
                
                if (node == null)
                    return;
                
                wbsPanels.forEach(panel -> {
                    panel.setSelected(null);
                });
                
                addWbsTab(node);
                focusWbsTab(node);
                showHeight();
                
                if (node instanceof Deliverable)
                    gui.setDeliverableData(node.getName(), ((Deliverable) node).getDescription(), node.getPath(), ((Deliverable) node).getDuration(), ((Deliverable) node).getCost(), ((Deliverable) node).getStart(), ((Deliverable) node).getDependencies());
                else if (node.isProject())
                    gui.setProjectData(node.getName());
                else
                    gui.setWorkPackageData(node.getName(), node.getPath());
            }
        });
        
        this.updateUI();
    }

    
    private void updateUI() {
        this.tree.setProjects(this.projects);
        
        this.wbsPanels.forEach(panel -> {
            panel.revalidateTree();
            panel.reloadComponent();
        });
        
        showHeight();
    }
    
    private void focusWbsTab(WBSNode node) {
        LinkedListNode<WBSAnimationPanel> wbsPanel = new LinkedListNode<>(null);
        
        this.wbsPanels.forEach(panel -> {
            if (panel.getProject().getName().equals(node.getProjectName()))
                wbsPanel.set(panel);
        });
        
        if (wbsPanel.get() == null)
            return;
        
        this.mainContentTabPane.setSelectedComponent(wbsPanel.get());
    }
    
    private void resetTraversal(int algorithm, WBSAnimationPanel wbs) {
        Thread newTraversal = new Thread() {
            public void run() {
                reportsConsole.setText("");
                wbs.setVisited(null);
                
                try {
                    wbs.getTree().traverse(algorithm, node -> {
                        if (this.isInterrupted())
                            throw new IndexOutOfBoundsException();
                        
                        reportsConsole.setText(reportsConsole.getText() + node.get().getPath() + "\n");
                        wbs.setVisited(node);
                        
                        try {
                            Thread.sleep(800);
                        } catch (InterruptedException ex) {
                            throw new IndexOutOfBoundsException();
                        }
                        
                        wbs.setVisited(null);
                    });
                    
                    wbs.setVisited(null);
                } catch (Exception e) {
                    return;
                }
            }
        };
        
        if (this.activeTraversalThread != null) {
            this.activeTraversalThread.interrupt();
        }
        
        this.activeTraversalThread = newTraversal;
        newTraversal.start();
    }
    
    private void showHeight() {
        WBSNode selected = this.getSelectedNode();
        
        if (selected != null) {
            Project project = getProject(selected.getProjectName());
            
            if (project != null)
                this.heightLabel.setText("Altura: " + Integer.toString(project.getWbs().height()));
        }
    }
    
    private void addWbsTab(WBSNode node) {
        String tabname = "EDT " + node.getProjectName();
        
        if (this.tabs.contains(tabname)) {
            this.focusWbsTab(node);
            return;
        }
        
        WBSAnimationPanel tab =  new WBSAnimationPanel(this.getProject(node.getProjectName()));
        GUI gui = this;
        ProjectFileSystemTree treeComponent = this.tree;
        
        tab.addSecondaryMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                WBSDrawableNode source = (WBSDrawableNode) e.getSource();
                WBSNode node = source.get();
                
                if (node == null)
                    return;
                
                treeComponent.setSelected(null);
                showHeight();
                
                wbsPanels.forEach(panel -> {
                    if (panel != tab)
                        panel.setSelected(null);
                });
                
                if (node instanceof Deliverable)
                    gui.setDeliverableData(node.getName(), ((Deliverable) node).getDescription(), node.getPath(), ((Deliverable) node).getDuration(), ((Deliverable) node).getCost(), ((Deliverable) node).getStart(), ((Deliverable) node).getDependencies());
                else if (node.isProject())
                    gui.setProjectData(node.getName());
                else
                    gui.setWorkPackageData(node.getName(), node.getPath());
            }
        });
        
        this.mainContentTabPane.addTab(tabname, tab);
        this.tabs.add(tabname);
        this.wbsPanels.add(tab);
        this.mainContentTabPane.setSelectedIndex(this.mainContentTabPane.getSelectedIndex() - 1);
        this.updateUI();
    }
    
    public void focusScheduleTab(WBSNode node) {
        LinkedListNode<SchedulePanel> s = new LinkedListNode<>(null);
        
        this.schedules.forEach(schedule -> {
            if (schedule.getProject().getName().equals(node.getProjectName()))
                s.set(schedule);
        });
        
        if (s.get() == null)
            return;
        
        this.mainContentTabPane.setSelectedComponent(s.get());
    }
    
    public void addScheduleTab(WBSNode node) {
        String tabname = "Cronograma " + node.getProjectName();
        
        if (this.tabs.contains(tabname)) {
            this.focusScheduleTab(node);
            return;
        }
        
        SchedulePanel schedule = new SchedulePanel(getProject(node.getProjectName()));
        this.schedules.add(schedule);
        this.mainContentTabPane.addTab(tabname, schedule);
        this.tabs.add(tabname);
        this.mainContentTabPane.setSelectedIndex(this.mainContentTabPane.getComponentCount() - 1);
    }
    
    public void showCreateProjectDialog() {
        this.projectNameField.setText("");
        this.addMemberField.setText("");
        this.memberListModel.clear();
        this.newProjectDialog.setLocationRelativeTo(this);
        this.newProjectDialog.setVisible(true);
    }
    
    public void showAddWorkPackageDialog() {
        WorkPackage parent = (WorkPackage) this.getSelectedWorkPackage();
        
        if (parent == null)
            return;
        
        this.workPackageParentField.setText(parent.getPath());
        this.workPackageNameField.setText("");
        this.newWorkPackageDialog.setLocationRelativeTo(this);
        this.newWorkPackageDialog.setVisible(true);
    }
    
    public void showAddDeliverableDialog() {
        WorkPackage parent = (WorkPackage) this.getSelectedWorkPackage();
        
        if (parent == null)
            return;
        
        Project project = getProject(parent.getProjectName());
        this.dependenciesModel.clear();
        
        project.getWbs().getDeliverables().forEach(deliverable -> {
            this.dependenciesModel.addElement(deliverable.getPath());
        });
        
        this.deliverableParentField.setText(parent.getPath());
        this.deliverableNameField.setText("");
        this.deliverableDescriptionArea.setText("");
        this.newDeliverableDialog.setLocationRelativeTo(this);
        this.newDeliverableDialog.setVisible(true);
    }
    
    public boolean validateName(String name) {
        if (Validators.isValidName(name))
            return true;
        
        showError("El nombre contiene caracteres no permitidos");
        return false;
    }
    
    public boolean validateUnique(String name, String path) {
        if (Validators.isUnique(name, path))
            return true;
        
        showError("Ya existe un elemento con el mismo nombre");
        return false;
    }
    
    public boolean validateDefined(String name) {
        if (Validators.isDefined(name))
            return true;
        
        showError("No se ha ingresado un nombre");
        return false;
    }
    
    public boolean validateInteger(String number) {
        if (Validators.isInteger(number))
            return true;
        
        showError("La duración debe ser un número entero");
        return false;
    }
    
    public boolean validateDouble(String number) {
        if (Validators.isDouble(number))
            return true;
        
        showError("El costo debe ser un valor numérico");
        return false;
    }
    
    public WorkPackage getSelectedWorkPackage() {
        WBSNode node = this.getSelectedNode("Selecciona un paquete de trabajo en el árbol de la izquierda para agregar un nodo");
        
        if (node == null)
            return null;
        
        if (node instanceof Deliverable) {
            showError("El nodo padre debe ser un paquete de trabajo");
            return null;
        }
        
        return (WorkPackage) node;
    }
    
    public WBSNode getSelectedNode(String errorMsg) {
        ProjectFileSystemTreeNode selected = this.tree.getSelected();
        
        if (selected == null) {
            LinkedListNode<WBSNode> node = new LinkedListNode<>(null);
            
            this.wbsPanels.forEach(panel -> {
                WBSDrawableNode selectedNode = panel.getSelected();
                
                if (selectedNode != null) {
                    node.set(selectedNode.get());
                }
            });
            
            if (node.get() == null)
                showError(errorMsg);
            
            return node.get();
        }
            
        return selected.get();
    }
    
    public WBSNode getSelectedNode() {
        ProjectFileSystemTreeNode selected = this.tree.getSelected();
        
        if (selected == null) {
            LinkedListNode<WBSNode> node = new LinkedListNode<>(null);
            
            this.wbsPanels.forEach(panel -> {
                WBSDrawableNode selectedNode = panel.getSelected();
                
                if (selectedNode != null) {
                    node.set(selectedNode.get());
                }
            });
            
            return node.get();
        }
            
        return selected.get();
    }
    
    public void setDeliverableData(String name, String description, String path, int duration, double cost, Date start, LinkedList<String> dependencies) {
        this.cards.show(this.nodeDataPanel, "deliverable");
        this.deliverableNameLabel.setText(name);
        this.deliverableDescrArea.setText(description);
        this.deliverablePathArea.setText(path);
        this.deliverableDurationLabel.setText(duration + " días");
        DecimalFormat format = new DecimalFormat("$###,###.###");
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE MMM dd yyyy");
        this.deliverableStartDateLabel.setText(dateFormat.format(start));
        this.deliverableCostLabel.setText(format.format(cost));
        String projectName = LinkedList.split(path, "/").get(0);
        this.deliverableProjectLabel.setText(projectName);
        
        this.dependenciesShowArea.setText("");
        
        dependencies.forEach(dependency -> {
            System.out.println(dependency);
            this.dependenciesShowArea.setText(this.dependenciesShowArea.getText() + dependency);
        });
    }
    
    public void setWorkPackageData(String name, String path) {
        this.cards.show(this.nodeDataPanel, "workPackage");
        this.workPkgNameLabel.setText(name);
        this.workPkgPathLabel.setText(path);
        String projectName = LinkedList.split(path, "/").get(0);
        this.workPkgProjectLabel.setText(projectName);
    }
    
    public void setProjectData(String name) {
        Project project = this.getProject(name);
        
        if (project == null)
            return;
        
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE MMM dd yyyy");
        
        this.cards.show(this.nodeDataPanel, "project");
        this.projectNameLabel.setText(name);
        this.projectStartDateLabel.setText(dateFormat.format(project.getStart()));
        this.projectMembersArea.setText("");
        
        project.getTeam().forEach(member -> {
            this.projectMembersArea.setText(this.projectMembersArea.getText() + member + "\n");
        });
    }
    
    public void showError(String message) {
        JFrame frame = new JFrame();
        frame.setAlwaysOnTop(true);
        JOptionPane.showMessageDialog(frame, message, "Error", JOptionPane.ERROR_MESSAGE);
        frame.dispose();
    }
    
    public Project getProject(String projectName) {
        LinkedListNode<Project> container = new LinkedListNode<>(null);
        
        projects.forEach(project -> {
            if (project.getName().equals(projectName)) {
                container.set(project);
            }
        });
        
        return container.get();
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
        jLabel14 = new javax.swing.JLabel();
        projectStartChooser = new com.toedter.calendar.JDateChooser();
        newWorkPackageDialog = new javax.swing.JDialog();
        workPackageParentField = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        workPackageNameField = new javax.swing.JTextField();
        cancelNewWorkPackageButton = new javax.swing.JButton();
        confirmNewWorkPackageButton = new javax.swing.JButton();
        newDeliverableDialog = new javax.swing.JDialog();
        deliverableParentField = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        deliverableNameField = new javax.swing.JTextField();
        cancelNewDeliverableButton = new javax.swing.JButton();
        confirmNewDeliverableButton = new javax.swing.JButton();
        jScrollPane5 = new javax.swing.JScrollPane();
        deliverableDescriptionArea = new javax.swing.JTextArea();
        jLabel8 = new javax.swing.JLabel();
        durationSpinner = new javax.swing.JSpinner();
        jLabel18 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        costSpinner = new javax.swing.JSpinner();
        jLabel21 = new javax.swing.JLabel();
        jScrollPane10 = new javax.swing.JScrollPane();
        dependenciesList = new javax.swing.JList<>();
        jLabel26 = new javax.swing.JLabel();
        header = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        newProjectButton = new javax.swing.JButton();
        addWorkPackage = new javax.swing.JButton();
        addDeliverable = new javax.swing.JButton();
        wbsButton = new javax.swing.JButton();
        scheduleButton = new javax.swing.JButton();
        deleteButton = new javax.swing.JButton();
        addDeliverable1 = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        savePathLabel = new javax.swing.JLabel();
        panel1 = new javax.swing.JPanel();
        sidebar = new javax.swing.JPanel();
        fileExplorerPanel = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        nodeDataPanel = new javax.swing.JPanel();
        reportsPanel = new javax.swing.JPanel();
        jScrollPane9 = new javax.swing.JScrollPane();
        reportsConsole = new javax.swing.JTextArea();
        confirmShowReport = new javax.swing.JButton();
        chooseReport = new javax.swing.JComboBox<>();
        heightLabel = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        noProjectPanel = new javax.swing.JPanel();
        workPackageCard = new javax.swing.JPanel();
        jLabel12 = new javax.swing.JLabel();
        workPkgNameLabel = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        workPkgProjectLabel = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jScrollPane7 = new javax.swing.JScrollPane();
        workPkgPathLabel = new javax.swing.JTextArea();
        projectCard = new javax.swing.JPanel();
        jLabel16 = new javax.swing.JLabel();
        projectNameLabel = new javax.swing.JLabel();
        jScrollPane6 = new javax.swing.JScrollPane();
        projectMembersArea = new javax.swing.JTextArea();
        jLabel17 = new javax.swing.JLabel();
        jLabel25 = new javax.swing.JLabel();
        projectStartDateLabel = new javax.swing.JLabel();
        deliverableCard = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        jPanel4 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        deliverableNameLabel = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        deliverableDescrArea = new javax.swing.JTextArea();
        jLabel10 = new javax.swing.JLabel();
        deliverableProjectLabel = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jScrollPane8 = new javax.swing.JScrollPane();
        deliverablePathArea = new javax.swing.JTextArea();
        jLabel22 = new javax.swing.JLabel();
        deliverableStartDateLabel = new javax.swing.JLabel();
        deliverableDurationLabel = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        deliverableCostLabel = new javax.swing.JLabel();
        jLabel27 = new javax.swing.JLabel();
        jScrollPane11 = new javax.swing.JScrollPane();
        dependenciesShowArea = new javax.swing.JTextArea();
        panel2 = new javax.swing.JPanel();
        mainContentTabPane = new javax.swing.JTabbedPane();

        newProjectDialog.setTitle("Nuevo Proyecto");
        newProjectDialog.setAlwaysOnTop(true);
        newProjectDialog.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        newProjectDialog.setMinimumSize(new java.awt.Dimension(365, 424));
        newProjectDialog.setModal(true);
        newProjectDialog.setPreferredSize(new java.awt.Dimension(365, 424));
        newProjectDialog.setResizable(false);
        newProjectDialog.getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        addMemberField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addMemberFieldActionPerformed(evt);
            }
        });
        newProjectDialog.getContentPane().add(addMemberField, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 170, 180, 30));

        jLabel1.setText("Fecha de inicio");
        newProjectDialog.getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 90, -1, -1));

        jScrollPane3.setViewportView(projectMembersList);

        newProjectDialog.getContentPane().add(jScrollPane3, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 200, 260, 120));

        jLabel2.setText("Nombre del proyecto");
        newProjectDialog.getContentPane().add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 30, -1, -1));
        newProjectDialog.getContentPane().add(projectNameField, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 50, 260, 30));

        addMemberButton.setText("+");
        addMemberButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addMemberButtonActionPerformed(evt);
            }
        });
        newProjectDialog.getContentPane().add(addMemberButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 170, 40, 30));

        cancelNewProjectButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/res/icons/cancel.png"))); // NOI18N
        cancelNewProjectButton.setText("Cancelar");
        cancelNewProjectButton.setToolTipText("");
        cancelNewProjectButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelNewProjectButtonActionPerformed(evt);
            }
        });
        newProjectDialog.getContentPane().add(cancelNewProjectButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 330, 120, 40));

        confirmNewProjectButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/res/icons/confirm2.png"))); // NOI18N
        confirmNewProjectButton.setText("Confirmar");
        confirmNewProjectButton.setToolTipText("");
        confirmNewProjectButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                confirmNewProjectButtonActionPerformed(evt);
            }
        });
        newProjectDialog.getContentPane().add(confirmNewProjectButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 330, 120, 40));

        removeMemberButton.setText("-");
        removeMemberButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                removeMemberButtonActionPerformed(evt);
            }
        });
        newProjectDialog.getContentPane().add(removeMemberButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 170, 40, 30));

        jLabel14.setText("Integrantes");
        newProjectDialog.getContentPane().add(jLabel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 150, -1, -1));
        newProjectDialog.getContentPane().add(projectStartChooser, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 110, 260, 30));

        newWorkPackageDialog.setTitle("Nuevo Paquete de Trabajo");
        newWorkPackageDialog.setAlwaysOnTop(true);
        newWorkPackageDialog.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        newWorkPackageDialog.setMinimumSize(new java.awt.Dimension(365, 240));
        newWorkPackageDialog.setModal(true);
        newWorkPackageDialog.setResizable(false);
        newWorkPackageDialog.getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        workPackageParentField.setEditable(false);
        workPackageParentField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                workPackageParentFieldActionPerformed(evt);
            }
        });
        newWorkPackageDialog.getContentPane().add(workPackageParentField, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 110, 260, 30));

        jLabel3.setText("Padre");
        newWorkPackageDialog.getContentPane().add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 90, -1, -1));

        jLabel4.setText("Nombre de paquete de trabajo");
        newWorkPackageDialog.getContentPane().add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 30, -1, -1));
        newWorkPackageDialog.getContentPane().add(workPackageNameField, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 50, 260, 30));

        cancelNewWorkPackageButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/res/icons/cancel.png"))); // NOI18N
        cancelNewWorkPackageButton.setText("Cancelar");
        cancelNewWorkPackageButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelNewWorkPackageButtonActionPerformed(evt);
            }
        });
        newWorkPackageDialog.getContentPane().add(cancelNewWorkPackageButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 160, 120, 40));

        confirmNewWorkPackageButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/res/icons/confirm2.png"))); // NOI18N
        confirmNewWorkPackageButton.setText("Confirmar");
        confirmNewWorkPackageButton.setToolTipText("");
        confirmNewWorkPackageButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                confirmNewWorkPackageButtonActionPerformed(evt);
            }
        });
        newWorkPackageDialog.getContentPane().add(confirmNewWorkPackageButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 160, -1, 40));

        newDeliverableDialog.setTitle("Nuevo Entregable");
        newDeliverableDialog.setAlwaysOnTop(true);
        newDeliverableDialog.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        newDeliverableDialog.setMinimumSize(new java.awt.Dimension(670, 380));
        newDeliverableDialog.setModal(true);
        newDeliverableDialog.setPreferredSize(new java.awt.Dimension(670, 380));
        newDeliverableDialog.getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        deliverableParentField.setEditable(false);
        deliverableParentField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deliverableParentFieldActionPerformed(evt);
            }
        });
        newDeliverableDialog.getContentPane().add(deliverableParentField, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 110, 260, 30));

        jLabel5.setText("Predecesoras");
        newDeliverableDialog.getContentPane().add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 140, -1, -1));

        jLabel6.setText("Nombre del Entregable");
        newDeliverableDialog.getContentPane().add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 30, -1, -1));
        newDeliverableDialog.getContentPane().add(deliverableNameField, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 50, 260, 30));

        cancelNewDeliverableButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/res/icons/cancel.png"))); // NOI18N
        cancelNewDeliverableButton.setText("Cancelar");
        cancelNewDeliverableButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelNewDeliverableButtonActionPerformed(evt);
            }
        });
        newDeliverableDialog.getContentPane().add(cancelNewDeliverableButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 280, 260, 40));

        confirmNewDeliverableButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/res/icons/confirm2.png"))); // NOI18N
        confirmNewDeliverableButton.setText("Confirmar");
        confirmNewDeliverableButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                confirmNewDeliverableButtonActionPerformed(evt);
            }
        });
        newDeliverableDialog.getContentPane().add(confirmNewDeliverableButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 280, 260, 40));

        deliverableDescriptionArea.setColumns(20);
        deliverableDescriptionArea.setRows(5);
        jScrollPane5.setViewportView(deliverableDescriptionArea);

        newDeliverableDialog.getContentPane().add(jScrollPane5, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 60, 260, 70));

        jLabel8.setText("Duración (Días)");
        newDeliverableDialog.getContentPane().add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 150, -1, -1));
        newDeliverableDialog.getContentPane().add(durationSpinner, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 170, 260, 30));

        jLabel18.setText("Padre");
        newDeliverableDialog.getContentPane().add(jLabel18, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 90, -1, -1));

        jLabel20.setText("Costo");
        newDeliverableDialog.getContentPane().add(jLabel20, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 210, -1, -1));

        costSpinner.setModel(new javax.swing.SpinnerNumberModel(0.0d, 0.0d, null, 1.0d));
        newDeliverableDialog.getContentPane().add(costSpinner, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 230, 240, 30));

        jLabel21.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel21.setText("$");
        newDeliverableDialog.getContentPane().add(jLabel21, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 230, 70, 30));

        jScrollPane10.setViewportView(dependenciesList);

        newDeliverableDialog.getContentPane().add(jScrollPane10, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 160, 260, 100));

        jLabel26.setText("Descripción");
        newDeliverableDialog.getContentPane().add(jLabel26, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 40, -1, -1));

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        header.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        header.setPreferredSize(new java.awt.Dimension(1280, 80));
        header.setLayout(new java.awt.GridLayout(1, 0, 10, 0));

        jPanel3.setLayout(new javax.swing.BoxLayout(jPanel3, javax.swing.BoxLayout.LINE_AXIS));

        newProjectButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/res/icons/projectBig.png"))); // NOI18N
        newProjectButton.setToolTipText("Crear Proyecto");
        newProjectButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                newProjectButtonActionPerformed(evt);
            }
        });
        jPanel3.add(newProjectButton);

        addWorkPackage.setIcon(new javax.swing.ImageIcon(getClass().getResource("/res/icons/folderBig.png"))); // NOI18N
        addWorkPackage.setToolTipText("Nuevo Paquete de trabajo");
        addWorkPackage.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addWorkPackageActionPerformed(evt);
            }
        });
        jPanel3.add(addWorkPackage);

        addDeliverable.setIcon(new javax.swing.ImageIcon(getClass().getResource("/res/icons/fileBig.png"))); // NOI18N
        addDeliverable.setToolTipText("Nuevo Entregable");
        addDeliverable.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addDeliverableActionPerformed(evt);
            }
        });
        jPanel3.add(addDeliverable);

        wbsButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/res/icons/wbs.png"))); // NOI18N
        wbsButton.setToolTipText("Ver EDT");
        wbsButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                wbsButtonActionPerformed(evt);
            }
        });
        jPanel3.add(wbsButton);

        scheduleButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/res/icons/schedule.png"))); // NOI18N
        scheduleButton.setToolTipText("Cronograma");
        scheduleButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                scheduleButtonActionPerformed(evt);
            }
        });
        jPanel3.add(scheduleButton);

        deleteButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/res/icons/delete.png"))); // NOI18N
        deleteButton.setToolTipText("Eliminar Nodo");
        deleteButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteButtonActionPerformed(evt);
            }
        });
        jPanel3.add(deleteButton);

        addDeliverable1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/res/icons/reports.png"))); // NOI18N
        addDeliverable1.setToolTipText("Reportes");
        addDeliverable1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addDeliverable1ActionPerformed(evt);
            }
        });
        jPanel3.add(addDeliverable1);

        header.add(jPanel3);

        jPanel2.setLayout(new java.awt.BorderLayout());

        savePathLabel.setText("jLabel14");
        jPanel2.add(savePathLabel, java.awt.BorderLayout.CENTER);

        header.add(jPanel2);

        getContentPane().add(header, java.awt.BorderLayout.NORTH);

        panel1.setLayout(new javax.swing.BoxLayout(panel1, javax.swing.BoxLayout.LINE_AXIS));

        sidebar.setMaximumSize(new java.awt.Dimension(250, 640));
        sidebar.setMinimumSize(new java.awt.Dimension(200, 0));
        sidebar.setPreferredSize(new java.awt.Dimension(250, 640));
        sidebar.setLayout(new javax.swing.BoxLayout(sidebar, javax.swing.BoxLayout.Y_AXIS));

        fileExplorerPanel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        fileExplorerPanel.setMaximumSize(new java.awt.Dimension(4356345, 45634564));
        fileExplorerPanel.setMinimumSize(new java.awt.Dimension(250, 0));
        fileExplorerPanel.setPreferredSize(new java.awt.Dimension(250, 336));
        fileExplorerPanel.setLayout(new javax.swing.BoxLayout(fileExplorerPanel, javax.swing.BoxLayout.PAGE_AXIS));
        sidebar.add(fileExplorerPanel);

        jPanel1.setMaximumSize(new java.awt.Dimension(32767, 308));
        jPanel1.setMinimumSize(new java.awt.Dimension(0, 308));
        jPanel1.setPreferredSize(new java.awt.Dimension(250, 400));

        nodeDataPanel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        nodeDataPanel.setLayout(new java.awt.CardLayout());

        reportsPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        reportsConsole.setColumns(20);
        reportsConsole.setRows(5);
        jScrollPane9.setViewportView(reportsConsole);

        reportsPanel.add(jScrollPane9, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 80, 210, 210));

        confirmShowReport.setIcon(new javax.swing.ImageIcon(getClass().getResource("/res/icons/confirm.png"))); // NOI18N
        confirmShowReport.setToolTipText("Mostrar Reporte");
        confirmShowReport.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                confirmShowReportActionPerformed(evt);
            }
        });
        reportsPanel.add(confirmShowReport, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 40, 40, 40));

        chooseReport.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Preorden", "Inorden", "Postorden", "Nodos Terminales", "Nodos con 1 solo entregable" }));
        chooseReport.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chooseReportActionPerformed(evt);
            }
        });
        reportsPanel.add(chooseReport, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 40, 170, 40));

        heightLabel.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        heightLabel.setText("Altura:");
        reportsPanel.add(heightLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 300, 210, -1));

        jLabel19.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel19.setText("Reportes");
        reportsPanel.add(jLabel19, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 10, -1, -1));

        nodeDataPanel.add(reportsPanel, "reports");

        javax.swing.GroupLayout noProjectPanelLayout = new javax.swing.GroupLayout(noProjectPanel);
        noProjectPanel.setLayout(noProjectPanelLayout);
        noProjectPanelLayout.setHorizontalGroup(
            noProjectPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 330, Short.MAX_VALUE)
        );
        noProjectPanelLayout.setVerticalGroup(
            noProjectPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 434, Short.MAX_VALUE)
        );

        nodeDataPanel.add(noProjectPanel, "none");

        workPackageCard.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel12.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel12.setText("Nombre:");
        workPackageCard.add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, -1, -1));

        workPkgNameLabel.setText("j");
        workPackageCard.add(workPkgNameLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 40, 210, -1));

        jLabel13.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel13.setText("Proyecto:");
        workPackageCard.add(jLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 70, -1, -1));

        workPkgProjectLabel.setText("j");
        workPackageCard.add(workPkgProjectLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 90, 210, -1));

        jLabel15.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel15.setText("Ruta:");
        workPackageCard.add(jLabel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 120, -1, -1));

        jScrollPane7.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);

        workPkgPathLabel.setColumns(20);
        workPkgPathLabel.setRows(5);
        jScrollPane7.setViewportView(workPkgPathLabel);

        workPackageCard.add(jScrollPane7, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 140, 200, 40));

        nodeDataPanel.add(workPackageCard, "workPackage");

        projectCard.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel16.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel16.setText("Nombre:");
        projectCard.add(jLabel16, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, -1, -1));

        projectNameLabel.setText("j");
        projectCard.add(projectNameLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 40, 220, -1));

        projectMembersArea.setColumns(20);
        projectMembersArea.setRows(5);
        jScrollPane6.setViewportView(projectMembersArea);

        projectCard.add(jScrollPane6, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 140, 210, 90));

        jLabel17.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel17.setText("Integrantes:");
        projectCard.add(jLabel17, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 120, -1, -1));

        jLabel25.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel25.setText("Fecha de Inicio");
        projectCard.add(jLabel25, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 70, -1, -1));

        projectStartDateLabel.setText("j");
        projectCard.add(projectStartDateLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 90, 220, -1));

        nodeDataPanel.add(projectCard, "project");

        deliverableCard.setPreferredSize(new java.awt.Dimension(262, 500));
        deliverableCard.setLayout(new javax.swing.BoxLayout(deliverableCard, javax.swing.BoxLayout.LINE_AXIS));

        jScrollPane4.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        jPanel4.setPreferredSize(new java.awt.Dimension(250, 432));
        jPanel4.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel7.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel7.setText("Nombre:");
        jPanel4.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 10, -1, -1));

        deliverableNameLabel.setText("j");
        jPanel4.add(deliverableNameLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 30, 100, -1));

        jLabel9.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel9.setText("Descripción:");
        jPanel4.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 280, -1, -1));

        deliverableDescrArea.setColumns(20);
        deliverableDescrArea.setRows(5);
        deliverableDescrArea.setFocusable(false);
        jScrollPane2.setViewportView(deliverableDescrArea);

        jPanel4.add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 300, 210, 50));

        jLabel10.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel10.setText("Proyecto:");
        jPanel4.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 10, -1, -1));

        deliverableProjectLabel.setText("j");
        jPanel4.add(deliverableProjectLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 30, 120, -1));

        jLabel11.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel11.setText("Ruta:");
        jPanel4.add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 220, -1, -1));

        deliverablePathArea.setColumns(20);
        deliverablePathArea.setRows(5);
        deliverablePathArea.setFocusable(false);
        jScrollPane8.setViewportView(deliverablePathArea);

        jPanel4.add(jScrollPane8, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 240, 210, 40));

        jLabel22.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel22.setText("Fecha de inicio");
        jPanel4.add(jLabel22, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 50, -1, -1));

        deliverableStartDateLabel.setText("j");
        jPanel4.add(deliverableStartDateLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 70, 210, -1));

        deliverableDurationLabel.setText("j días");
        jPanel4.add(deliverableDurationLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 110, 100, -1));

        jLabel23.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel23.setText("Duración");
        jPanel4.add(jLabel23, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 90, -1, -1));

        jLabel24.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel24.setText("Costo");
        jPanel4.add(jLabel24, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 90, -1, -1));

        deliverableCostLabel.setText("j");
        jPanel4.add(deliverableCostLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 110, 100, -1));

        jLabel27.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel27.setText("Predecesoras:");
        jPanel4.add(jLabel27, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 130, -1, -1));

        dependenciesShowArea.setColumns(20);
        dependenciesShowArea.setRows(5);
        dependenciesShowArea.setFocusable(false);
        jScrollPane11.setViewportView(dependenciesShowArea);

        jPanel4.add(jScrollPane11, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 150, 210, 70));

        jScrollPane4.setViewportView(jPanel4);

        deliverableCard.add(jScrollPane4);

        nodeDataPanel.add(deliverableCard, "deliverable");

        jScrollPane1.setViewportView(nodeDataPanel);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        sidebar.add(jPanel1);

        panel1.add(sidebar);

        panel2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        panel2.setLayout(new java.awt.BorderLayout());
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
        Date start = this.projectStartChooser.getDate();
        
        if (!validateDefined(projectName) || !validateName(projectName) || !validateUnique(projectName, "/"))
            return;
        
        LinkedList<String> team = new LinkedList<>();
        
        for (Object member : this.memberListModel.toArray()) {
            team.add((String) member);
        }
        
        this.projects.add(Project.create(projectName, team, start));
        this.newProjectDialog.setVisible(false);
        this.updateUI();
    }//GEN-LAST:event_confirmNewProjectButtonActionPerformed

    private void cancelNewProjectButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelNewProjectButtonActionPerformed
        this.newProjectDialog.setVisible(false);
    }//GEN-LAST:event_cancelNewProjectButtonActionPerformed

    private void addMemberButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addMemberButtonActionPerformed
        String newMember = this.addMemberField.getText();
        
        if (!validateDefined(newMember) || !validateName(newMember))
            return;
        
        this.memberListModel.addElement(newMember);
        this.addMemberField.setText("");
    }//GEN-LAST:event_addMemberButtonActionPerformed

    private void removeMemberButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_removeMemberButtonActionPerformed
        for (String member : this.projectMembersList.getSelectedValuesList()) {
            this.memberListModel.removeElement(member);
        }
    }//GEN-LAST:event_removeMemberButtonActionPerformed

    private void addWorkPackageActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addWorkPackageActionPerformed
        this.showAddWorkPackageDialog();
    }//GEN-LAST:event_addWorkPackageActionPerformed

    private void workPackageParentFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_workPackageParentFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_workPackageParentFieldActionPerformed

    private void cancelNewWorkPackageButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelNewWorkPackageButtonActionPerformed
        this.newWorkPackageDialog.setVisible(false);
    }//GEN-LAST:event_cancelNewWorkPackageButtonActionPerformed

    private void confirmNewWorkPackageButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_confirmNewWorkPackageButtonActionPerformed
        String name = this.workPackageNameField.getText();
        String parentPath = this.workPackageParentField.getText();
        
        if (!validateDefined(name) || !validateName(name) || !validateUnique(name, parentPath))
            return;
        
        String projectName = parentPath.split("/")[0];

        projects.forEach(project -> {
            if (project.getName().equals(projectName)) {
                project.getWbs().add(WorkPackage.create(name, parentPath + "/" + name));
            }
        });

        this.newWorkPackageDialog.setVisible(false);
        this.updateUI();
    }//GEN-LAST:event_confirmNewWorkPackageButtonActionPerformed

    private void deliverableParentFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deliverableParentFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_deliverableParentFieldActionPerformed

    private void cancelNewDeliverableButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelNewDeliverableButtonActionPerformed
        this.newDeliverableDialog.setVisible(false);
    }//GEN-LAST:event_cancelNewDeliverableButtonActionPerformed

    private void confirmNewDeliverableButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_confirmNewDeliverableButtonActionPerformed
        String name = this.deliverableNameField.getText();
        String parentPath = this.deliverableParentField.getText();
        Integer duration = (Integer) this.durationSpinner.getValue();
        Double cost = (Double) this.costSpinner.getValue();
        LinkedList<String> dependencies = new LinkedList<>();
        
        for (String dependency : this.dependenciesList.getSelectedValuesList()) {
            dependencies.add(dependency);
        }
        
        if (!validateDefined(name) || !validateName(name) || !validateUnique(name, parentPath))
            return;
        
        String description = this.deliverableDescriptionArea.getText();
        String projectName = parentPath.split("/")[0];

        projects.forEach(project -> {
            if (project.getName().equals(projectName)) {
                project.getWbs().add(Deliverable.create(name, parentPath + "/" + name + ".txt", description, cost, duration, dependencies, project.getStart()));
                project.getSchedule().load();
            }
        });
        this.newDeliverableDialog.setVisible(false);
        this.updateUI();
    }//GEN-LAST:event_confirmNewDeliverableButtonActionPerformed

    private void addDeliverableActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addDeliverableActionPerformed
        this.showAddDeliverableDialog();
    }//GEN-LAST:event_addDeliverableActionPerformed

    private void wbsButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_wbsButtonActionPerformed
        WBSNode selected = this.getSelectedNode("Selecciona un elemento que pertenezca a un proyecto en el árbol de la izquierda para ver su EDT");
        
        if (selected == null)
            return;
        
        this.addWbsTab(selected);
    }//GEN-LAST:event_wbsButtonActionPerformed

    private void scheduleButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_scheduleButtonActionPerformed
        WBSNode selected = this.getSelectedNode("Selecciona un elemento que pertenezca a un proyecto en el árbol de la izquierda para ver su Cronograma");
        
        if (selected == null)
            return;
        
        this.addScheduleTab(selected);
    }//GEN-LAST:event_scheduleButtonActionPerformed

    private void addDeliverable1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addDeliverable1ActionPerformed
        this.cards.show(this.nodeDataPanel, "reports");
        this.reportsConsole.setText("");
    }//GEN-LAST:event_addDeliverable1ActionPerformed

    private void deleteButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteButtonActionPerformed
        WBSNode selected = this.getSelectedNode("No se ha seleccionado ningún elemento, seleccione algún nodo en el árbol de la izquierda o en el EDT");
        
        if (selected == null)
            return;
        
        int option = JOptionPane.showConfirmDialog(null, "Desea eliminar el nodo? Si este tiene hijos, estos también será eliminados", "Confirmar eliminar", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
     
        if (option == 0) {
            LinkedList<String> filePath = LinkedList.split(selected.getPath(), "/");
        
            if (filePath.length() > 1)
                filePath.add("wbs", 1);

            File file = FileHelpers.get(filePath.join("/"));
            
            if (file.isDirectory()) {
                FileHelpers.clearDirectory(file);
            }
            
            file.delete();
            
            if (filePath.length() == 1) {
                Project project = getProject(filePath.get(0));
                
                this.projects.remove(project);
                LinkedListNode<WBSAnimationPanel> toRemove = new LinkedListNode<>(null);
                
                this.wbsPanels.forEach((p) -> {
                    if (p.getProject().getName().equals(project.getName())) {
                        toRemove.set(p);
                    }
                });
                
                if (toRemove.get() != null)
                    this.mainContentTabPane.remove(toRemove.get());
                
                this.wbsPanels.remove(toRemove.get());
            }
            
            this.projects = Project.load();
            
            this.wbsPanels.forEach(panel -> {
                panel.setProject(getProject(panel.getProject().getName()));
            });
            
            this.updateUI();
        }
    }//GEN-LAST:event_deleteButtonActionPerformed

    private void confirmShowReportActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_confirmShowReportActionPerformed
        WBSNode selected = getSelectedNode("Seleccione algún proyecto o nodo de un proyecto");
        
        if (selected == null) {
            return;
        }
        
        LinkedListNode<WBSAnimationPanel> panel = new LinkedListNode<>(null);

        String projectName = selected.getProjectName();

        this.wbsPanels.forEach(wbsPanel -> {
            if (wbsPanel.getSelected() != null)
                panel.set(wbsPanel);
        });

        if (panel.get() == null) {
            this.addWbsTab(selected);
            panel.set(this.wbsPanels.get(this.wbsPanels.length() - 1));
        }
        
        this.mainContentTabPane.setSelectedComponent(panel.get());
        this.resetTraversal(this.chooseReport.getSelectedIndex(), panel.get());
    }//GEN-LAST:event_confirmShowReportActionPerformed

    private void chooseReportActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chooseReportActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_chooseReportActionPerformed

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
    private javax.swing.JButton addDeliverable1;
    private javax.swing.JButton addMemberButton;
    private javax.swing.JTextField addMemberField;
    private javax.swing.JButton addWorkPackage;
    private javax.swing.JButton cancelNewDeliverableButton;
    private javax.swing.JButton cancelNewProjectButton;
    private javax.swing.JButton cancelNewWorkPackageButton;
    private javax.swing.JComboBox<String> chooseReport;
    private javax.swing.JButton confirmNewDeliverableButton;
    private javax.swing.JButton confirmNewProjectButton;
    private javax.swing.JButton confirmNewWorkPackageButton;
    private javax.swing.JButton confirmShowReport;
    private javax.swing.JSpinner costSpinner;
    private javax.swing.JButton deleteButton;
    private javax.swing.JPanel deliverableCard;
    private javax.swing.JLabel deliverableCostLabel;
    private javax.swing.JTextArea deliverableDescrArea;
    private javax.swing.JTextArea deliverableDescriptionArea;
    private javax.swing.JLabel deliverableDurationLabel;
    private javax.swing.JTextField deliverableNameField;
    private javax.swing.JLabel deliverableNameLabel;
    private javax.swing.JTextField deliverableParentField;
    private javax.swing.JTextArea deliverablePathArea;
    private javax.swing.JLabel deliverableProjectLabel;
    private javax.swing.JLabel deliverableStartDateLabel;
    private javax.swing.JList<String> dependenciesList;
    private javax.swing.JTextArea dependenciesShowArea;
    private javax.swing.JSpinner durationSpinner;
    private javax.swing.JPanel fileExplorerPanel;
    private javax.swing.JPanel header;
    private javax.swing.JLabel heightLabel;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane10;
    private javax.swing.JScrollPane jScrollPane11;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JScrollPane jScrollPane8;
    private javax.swing.JScrollPane jScrollPane9;
    private javax.swing.JTabbedPane mainContentTabPane;
    private javax.swing.JDialog newDeliverableDialog;
    private javax.swing.JButton newProjectButton;
    private javax.swing.JDialog newProjectDialog;
    private javax.swing.JDialog newWorkPackageDialog;
    private javax.swing.JPanel noProjectPanel;
    private javax.swing.JPanel nodeDataPanel;
    private javax.swing.JPanel panel1;
    private javax.swing.JPanel panel2;
    private javax.swing.JPanel projectCard;
    private javax.swing.JTextArea projectMembersArea;
    private javax.swing.JList<String> projectMembersList;
    private javax.swing.JTextField projectNameField;
    private javax.swing.JLabel projectNameLabel;
    private com.toedter.calendar.JDateChooser projectStartChooser;
    private javax.swing.JLabel projectStartDateLabel;
    private javax.swing.JButton removeMemberButton;
    private javax.swing.JTextArea reportsConsole;
    private javax.swing.JPanel reportsPanel;
    private javax.swing.JLabel savePathLabel;
    private javax.swing.JButton scheduleButton;
    private javax.swing.JPanel sidebar;
    private javax.swing.JButton wbsButton;
    private javax.swing.JPanel workPackageCard;
    private javax.swing.JTextField workPackageNameField;
    private javax.swing.JTextField workPackageParentField;
    private javax.swing.JLabel workPkgNameLabel;
    private javax.swing.JTextArea workPkgPathLabel;
    private javax.swing.JLabel workPkgProjectLabel;
    // End of variables declaration//GEN-END:variables
}
