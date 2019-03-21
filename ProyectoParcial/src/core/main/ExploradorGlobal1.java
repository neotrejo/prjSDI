/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core.main;

import core.connections.multicast.MulticastListener;
import core.connections.multicast.MulticastPolling;
import core.connections.multicast.MulticastServer;
import core.connections.rmi.remote.RMIServer;
import core.connections.sockets.FileServer;
import core.controller.MainController;
import core.data.Classroom;
import core.data.ClientModel;
import core.data.FileModel;
import core.data.ServerFile;
import core.data.Subject;
import core.data.User;
import core.data.Usuario;
import core.gui.admon.SessionI;
import core.gui.admon.SubjectI;
import core.gui.custom.ExplorerCellRenderer;
import core.main.listener.GenericListener;
import core.services.DirectoryIndexCreator;
import core.services.DirectoryObserver;
import core.services.NetworkConnections;
import core.tasks.UserDisconnection;
import core.utils.MyLogger;
import java.util.ArrayList;
import java.util.Stack;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JFileChooser;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Luis
 */
public class ExploradorGlobal1 extends javax.swing.JFrame implements MulticastListener, GenericListener {

    /**
     * Creates new form FileExplorer
     */
    private JList lista;
    private ClientModel client;
    private ArrayList<ArrayList<ServerFile>> stack;
    private int currentIndex = 0;
    private static ExploradorGlobal1 instance;
    private int descargasActivas = 0;
    private int transferenciasActivas = 0;
    private User user;
    private ArrayList<Classroom> classrooms;
    private ArrayList<Subject> subjects;
    private JFileChooser fileChooserSub;
    private JFileChooser fileChooserSes;
    private DefaultTableModel modelSubj;

    private ExploradorGlobal1(ArrayList<ServerFile> rootFiles, ClientModel client, User user) {
        initComponents();

        stack = new ArrayList<>();

        this.client = client;
        this.user = user;
        userLabel.setText(user == null ? "" : user.getName());

        lista = this.archivos;
        lista.setCellRenderer(new ExplorerCellRenderer());

        tabPanel.setTitleAt(0, "Files");
        tabPanel.setTitleAt(1, "Subject");
        tabPanel.setTitleAt(2, "Session");

        fileChooserSub = new JFileChooser();
        fileChooserSub.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        fileChooserSub.setAcceptAllFileFilterUsed(false);

        fileChooserSes = new JFileChooser();
        fileChooserSes.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fileChooserSes.addChoosableFileFilter(new FileNameExtensionFilter("PDF Documents", "pdf"));
        fileChooserSes.addChoosableFileFilter(new FileNameExtensionFilter("MS Office Documents", "docx", "xlsx", "pptx"));
        fileChooserSes.addChoosableFileFilter(new FileNameExtensionFilter("Images", "jpg", "png", "gif", "bmp"));

        fileChooserSes.setAcceptAllFileFilterUsed(false);

        formuPanel.setBorder(BorderFactory.createTitledBorder("Add subject"));
        formuPanel.setVisible(false);
        //--------tables-----------------//
        modelSubj = new DefaultTableModel();
        jTable1.setModel(modelSubj);
        modelSubj.addColumn("Name");
        modelSubj.addColumn("SharedFolder");
        modelSubj.addColumn("Description");
        update_SubjectTable(true);

        //--------subjectTab----//
        classrooms = MainController.getClassrooms();
        if (classrooms != null) {
            classrooms.forEach((n) -> classroomsCB.addItem(n.getName()));
            if (subjects != null) {
                subjects.forEach((n) -> subjectsCB.addItem(n.getName()));
            } else {
                logSubjLabel.setText("There aren´t subjects.");
            }
        } else {
            logSubjLabel.setText("There aren´t classrooms.");
        }
        //-----------------------------//
        savedSubjBtn.enable(false);

        tituloLbl.setText("Explorador de archivos");

        stack.add(rootFiles);

        NetworkConnections.getInstance().start(this);

        UserDisconnection.getInstance().start();
        UserDisconnection.getInstance().register(this);

        setLocationRelativeTo(null);
    }

    public static ExploradorGlobal1 getInstance() {
        if (instance == null) {
            instance = new ExploradorGlobal1(new ArrayList<ServerFile>(), null, null);
        }
        return instance;
    }

    public static ExploradorGlobal1 getInstance(User user) {
        if (instance == null) {
            instance = new ExploradorGlobal1(new ArrayList<ServerFile>(), null, user);
        }
        return instance;
    }

    private void update_SubjectTable(boolean first) {
        if (!first){
            int filas=jTable1.getRowCount();
            for (int i = 0;filas>i; i++) {
                modelSubj.removeRow(0);
            }
        }
        subjects = MainController.getSubjects(user.getId());
        if (subjects != null) {
            subjects.forEach((n) -> modelSubj.addRow(new Object[]{n.getName(), n.getSharedFolder(), n.getDescription()}));
        }
    }
    
    private void cleanFieldsSubject(){
        nameTextF.setText("");
        passwordTextF.setText("");
        sharedFolderTextF.setText("");
        descriptionTextA.setText("");
    }

    private void refresh(ArrayList<ServerFile> file) {

        DefaultListModel model = new DefaultListModel();

        for (ServerFile f : file) {
            model.addElement(new FileModel(f.getName(), f.isIsFile(), f.getFullName(), f.getHost()));
        }

        lista.setModel(model);

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jSeparator1 = new javax.swing.JSeparator();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        tituloLbl = new javax.swing.JLabel();
        tLabel = new javax.swing.JLabel();
        dLabel = new javax.swing.JLabel();
        userLabel = new javax.swing.JLabel();
        tabPanel = new javax.swing.JTabbedPane();
        docusPanel = new javax.swing.JScrollPane();
        archivos = new javax.swing.JList<>();
        subjectPanel = new javax.swing.JPanel();
        formuPanel = new javax.swing.JPanel();
        savedSubjBtn = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        nameTextF = new javax.swing.JTextField();
        sharedFolderTextF = new javax.swing.JTextField();
        passwordTextF = new javax.swing.JPasswordField();
        sharedFolderBtn = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        descriptionTextA = new javax.swing.JTextArea();
        cancelSubjectBtn = new javax.swing.JButton();
        logSubjLabel = new javax.swing.JLabel();
        consultPanel = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        addSubjectBtn = new javax.swing.JButton();
        sessionPanel = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        subjectsCB = new javax.swing.JComboBox<>();
        classroomsCB = new javax.swing.JComboBox<>();
        FilesSharedBtn = new javax.swing.JButton();
        dateTextF = new javax.swing.JTextField();
        durationSpin = new javax.swing.JSpinner();
        savedSessBtn = new javax.swing.JButton();
        logSessLabel = new javax.swing.JLabel();
        sharedFileTextF = new javax.swing.JTextField();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenuItem2 = new javax.swing.JMenuItem();
        jMenuItem3 = new javax.swing.JMenuItem();
        jMenuItem4 = new javax.swing.JMenuItem();
        jMenuItem5 = new javax.swing.JMenuItem();
        subjectMenu = new javax.swing.JMenu();
        addSubjectItem = new javax.swing.JMenuItem();
        updateSubjectItem = new javax.swing.JMenuItem();
        sessionMenu = new javax.swing.JMenu();
        addSessionItem = new javax.swing.JMenuItem();
        updateSessionItem = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("RaspClass");
        setResizable(false);

        jButton1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jButton1.setText("<");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jButton2.setText(">");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        tituloLbl.setText("Explorador de archivos");

        tLabel.setText("Transfiriendo: 0");

        dLabel.setText("Descargando: 0");

        userLabel.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        userLabel.setForeground(new java.awt.Color(0, 51, 153));

        tabPanel.setTabLayoutPolicy(javax.swing.JTabbedPane.SCROLL_TAB_LAYOUT);
        tabPanel.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N

        docusPanel.setBackground(java.awt.SystemColor.controlLtHighlight);

        archivos.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                archivosMouseClicked(evt);
            }
        });
        docusPanel.setViewportView(archivos);

        tabPanel.addTab("tab1", docusPanel);

        subjectPanel.setBackground(java.awt.SystemColor.controlLtHighlight);
        subjectPanel.setPreferredSize(new java.awt.Dimension(258, 130));
        subjectPanel.setRequestFocusEnabled(false);

        formuPanel.setBackground(java.awt.SystemColor.controlLtHighlight);
        formuPanel.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N

        savedSubjBtn.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        savedSubjBtn.setText("Save");
        savedSubjBtn.setMaximumSize(new java.awt.Dimension(71, 27));
        savedSubjBtn.setMinimumSize(new java.awt.Dimension(71, 27));
        savedSubjBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                savedSubjBtnActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        jLabel1.setText("Name:");

        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        jLabel2.setText("Description:");

        jLabel3.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        jLabel3.setText("Password:");

        jLabel4.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        jLabel4.setText("Shared folder:");

        nameTextF.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N

        sharedFolderTextF.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N

        passwordTextF.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N

        sharedFolderBtn.setFont(new java.awt.Font("Tahoma", 1, 15)); // NOI18N
        sharedFolderBtn.setText("...");
        sharedFolderBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sharedFolderBtnActionPerformed(evt);
            }
        });

        descriptionTextA.setColumns(20);
        descriptionTextA.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        descriptionTextA.setRows(5);
        jScrollPane2.setViewportView(descriptionTextA);

        cancelSubjectBtn.setText("Cancel");
        cancelSubjectBtn.setMaximumSize(new java.awt.Dimension(71, 27));
        cancelSubjectBtn.setMinimumSize(new java.awt.Dimension(71, 27));
        cancelSubjectBtn.setPreferredSize(new java.awt.Dimension(71, 27));
        cancelSubjectBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelSubjectBtnActionPerformed(evt);
            }
        });

        logSubjLabel.setForeground(new java.awt.Color(153, 0, 0));
        logSubjLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        javax.swing.GroupLayout formuPanelLayout = new javax.swing.GroupLayout(formuPanel);
        formuPanel.setLayout(formuPanelLayout);
        formuPanelLayout.setHorizontalGroup(
            formuPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(formuPanelLayout.createSequentialGroup()
                .addGap(76, 76, 76)
                .addGroup(formuPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(logSubjLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, formuPanelLayout.createSequentialGroup()
                        .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 114, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(nameTextF, javax.swing.GroupLayout.PREFERRED_SIZE, 361, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, formuPanelLayout.createSequentialGroup()
                        .addGroup(formuPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel3)
                            .addComponent(jLabel4)
                            .addComponent(jLabel2))
                        .addGap(28, 28, 28)
                        .addGroup(formuPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, formuPanelLayout.createSequentialGroup()
                                .addComponent(sharedFolderTextF)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(sharedFolderBtn))
                            .addComponent(passwordTextF)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 361, Short.MAX_VALUE))))
                .addGap(67, 67, 67))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, formuPanelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(savedSubjBtn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(27, 27, 27)
                .addComponent(cancelSubjectBtn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(230, 230, 230))
        );
        formuPanelLayout.setVerticalGroup(
            formuPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, formuPanelLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addGroup(formuPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(nameTextF, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(formuPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(passwordTextF, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(formuPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(sharedFolderTextF, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(sharedFolderBtn)
                    .addComponent(jLabel4))
                .addGap(18, 18, 18)
                .addGroup(formuPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(formuPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(savedSubjBtn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cancelSubjectBtn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(logSubjLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(428, 428, 428))
        );

        consultPanel.setBackground(java.awt.SystemColor.controlLtHighlight);

        jScrollPane3.setBackground(java.awt.SystemColor.controlLtHighlight);
        jScrollPane3.setBorder(null);
        jScrollPane3.setMaximumSize(new java.awt.Dimension(32767, 200));
        jScrollPane3.setMinimumSize(new java.awt.Dimension(27, 100));
        jScrollPane3.setPreferredSize(new java.awt.Dimension(452, 202));

        jTable1.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jScrollPane3.setViewportView(jTable1);

        javax.swing.GroupLayout consultPanelLayout = new javax.swing.GroupLayout(consultPanel);
        consultPanel.setLayout(consultPanelLayout);
        consultPanelLayout.setHorizontalGroup(
            consultPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
            .addGroup(consultPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(consultPanelLayout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 665, Short.MAX_VALUE)
                    .addContainerGap()))
        );
        consultPanelLayout.setVerticalGroup(
            consultPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 275, Short.MAX_VALUE)
            .addGroup(consultPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(consultPanelLayout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 249, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );

        addSubjectBtn.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        addSubjectBtn.setForeground(new java.awt.Color(51, 51, 51));
        addSubjectBtn.setText("+");
        addSubjectBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addSubjectBtnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout subjectPanelLayout = new javax.swing.GroupLayout(subjectPanel);
        subjectPanel.setLayout(subjectPanelLayout);
        subjectPanelLayout.setHorizontalGroup(
            subjectPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, subjectPanelLayout.createSequentialGroup()
                .addGap(37, 37, 37)
                .addComponent(consultPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(addSubjectBtn))
            .addGroup(subjectPanelLayout.createSequentialGroup()
                .addGap(49, 49, 49)
                .addComponent(formuPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 102, Short.MAX_VALUE))
        );
        subjectPanelLayout.setVerticalGroup(
            subjectPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(subjectPanelLayout.createSequentialGroup()
                .addGroup(subjectPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(addSubjectBtn)
                    .addGroup(subjectPanelLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(consultPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, Short.MAX_VALUE)
                .addComponent(formuPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 275, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(73, 73, 73))
        );

        tabPanel.addTab("tab1", subjectPanel);

        sessionPanel.setBackground(java.awt.SystemColor.controlLtHighlight);

        jLabel5.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        jLabel5.setText("Subject:");

        jLabel6.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        jLabel6.setText("Classroom:");

        jLabel7.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        jLabel7.setText("Duration (hrs):");

        jLabel8.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        jLabel8.setText("Date:");

        jLabel9.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        jLabel9.setText("Files:");

        subjectsCB.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N

        classroomsCB.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N

        FilesSharedBtn.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        FilesSharedBtn.setText("...");
        FilesSharedBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                FilesSharedBtnActionPerformed(evt);
            }
        });

        dateTextF.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N

        durationSpin.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N

        savedSessBtn.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        savedSessBtn.setText("Save");
        savedSessBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                savedSessBtnActionPerformed(evt);
            }
        });

        logSessLabel.setForeground(new java.awt.Color(153, 0, 51));

        sharedFileTextF.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        sharedFileTextF.setText("....");
        sharedFileTextF.setEnabled(false);

        javax.swing.GroupLayout sessionPanelLayout = new javax.swing.GroupLayout(sessionPanel);
        sessionPanel.setLayout(sessionPanelLayout);
        sessionPanelLayout.setHorizontalGroup(
            sessionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(sessionPanelLayout.createSequentialGroup()
                .addGap(89, 89, 89)
                .addGroup(sessionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel5)
                    .addComponent(jLabel6)
                    .addComponent(jLabel7)
                    .addComponent(jLabel9)
                    .addComponent(jLabel8))
                .addGap(42, 42, 42)
                .addGroup(sessionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(sessionPanelLayout.createSequentialGroup()
                        .addComponent(sharedFileTextF, javax.swing.GroupLayout.DEFAULT_SIZE, 397, Short.MAX_VALUE)
                        .addGap(18, 18, 18)
                        .addComponent(FilesSharedBtn))
                    .addComponent(dateTextF)
                    .addComponent(classroomsCB, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(durationSpin)
                    .addComponent(subjectsCB, javax.swing.GroupLayout.Alignment.TRAILING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(87, 87, 87))
            .addGroup(sessionPanelLayout.createSequentialGroup()
                .addGap(174, 174, 174)
                .addComponent(logSessLabel)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, sessionPanelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(savedSessBtn)
                .addGap(281, 281, 281))
        );
        sessionPanelLayout.setVerticalGroup(
            sessionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(sessionPanelLayout.createSequentialGroup()
                .addGap(49, 49, 49)
                .addGroup(sessionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(subjectsCB, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(22, 22, 22)
                .addGroup(sessionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(classroomsCB, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(20, 20, 20)
                .addGroup(sessionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(durationSpin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(26, 26, 26)
                .addGroup(sessionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(dateTextF, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(29, 29, 29)
                .addGroup(sessionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(FilesSharedBtn)
                    .addComponent(sharedFileTextF, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(27, 27, 27)
                .addComponent(savedSessBtn)
                .addGap(20, 20, 20)
                .addComponent(logSessLabel)
                .addContainerGap(275, Short.MAX_VALUE))
        );

        tabPanel.addTab("tab3", sessionPanel);

        jMenuBar1.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        jMenu1.setText("Archivo");
        jMenu1.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        jMenuItem1.setText("Conectados");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem1);

        jMenuItem2.setText("Buscar");
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem2);

        jMenuItem3.setText("Descargas");
        jMenuItem3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem3ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem3);

        jMenuItem4.setText("Transferencias");
        jMenuItem4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem4ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem4);

        jMenuItem5.setText("Salir");
        jMenuItem5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem5ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem5);

        jMenuBar1.add(jMenu1);

        subjectMenu.setText("Subject");
        subjectMenu.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        addSubjectItem.setText("Add");
        addSubjectItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addSubjectItemActionPerformed(evt);
            }
        });
        subjectMenu.add(addSubjectItem);

        updateSubjectItem.setText("Update");
        subjectMenu.add(updateSubjectItem);

        jMenuBar1.add(subjectMenu);

        sessionMenu.setText("Session");
        sessionMenu.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        addSessionItem.setText("Add");
        addSessionItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addSessionItemActionPerformed(evt);
            }
        });
        sessionMenu.add(addSessionItem);

        updateSessionItem.setText("Update");
        sessionMenu.add(updateSessionItem);

        jMenuBar1.add(sessionMenu);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(tituloLbl, javax.swing.GroupLayout.PREFERRED_SIZE, 332, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(364, Short.MAX_VALUE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(userLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 146, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(10, 10, 10)
                                .addComponent(tabPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 779, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jSeparator1))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(dLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(tLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 166, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE)))
                        .addContainerGap())))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(userLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tituloLbl))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 2, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tabPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 644, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 27, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(dLabel)
                    .addComponent(tLabel))
                .addGap(9, 9, 9))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void archivosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_archivosMouseClicked
        // TODO add your handling code here:
        JList list = (JList) evt.getSource();
        if (evt.getClickCount() == 2) {

            FileModel cm = (FileModel) list.getSelectedValue();

            if (cm.getIsFile()) {
                MainController.startDownload(cm.getFullPath(), cm.getHost());
            } else {
                ArrayList<ServerFile> rootFiles = MainController.getFolderContent(cm.getHost(), cm.getFullPath());
                stack.add(rootFiles);
                currentIndex++;
                refresh(rootFiles);
            }

        }
    }//GEN-LAST:event_archivosMouseClicked

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        if (currentIndex > 0) {
            --currentIndex;
            ArrayList<ServerFile> files = stack.get(currentIndex);
            refresh(files);
        } else {
            currentIndex = 0;
        }

    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        if (currentIndex < stack.size() - 1) {
            ++currentIndex;
            ArrayList<ServerFile> files = stack.get(currentIndex);
            refresh(files);
        } else {
            currentIndex = stack.size() - 1;
        }
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        // TODO add your handling code here:
        Dashboard.getInstance().setVisible(true);
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
        Buscar.getInstance().setVisible(true);
    }//GEN-LAST:event_jMenuItem2ActionPerformed

    private void jMenuItem3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem3ActionPerformed
        Descargas.getInstance().setVisible(true);
    }//GEN-LAST:event_jMenuItem3ActionPerformed

    private void jMenuItem4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem4ActionPerformed
        Transferencias.getInstance().setVisible(true);
    }//GEN-LAST:event_jMenuItem4ActionPerformed

    private void jMenuItem5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem5ActionPerformed

        if (descargasActivas > 0 || transferenciasActivas > 0) {

            int response = JOptionPane.showConfirmDialog(this, "Tiene descargas o transferencias activas\n¿Desea salir?", "Aceptar",
                    JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

            if (response == JOptionPane.YES_OPTION) {
                System.exit(0);
            }
        } else {
            System.exit(0);
        }
    }//GEN-LAST:event_jMenuItem5ActionPerformed

    private void addSubjectItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addSubjectItemActionPerformed
        SubjectI.getInstance("Add subject", this.user).setVisible(true);

    }//GEN-LAST:event_addSubjectItemActionPerformed

    private void addSessionItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addSessionItemActionPerformed
        SessionI.getInstance("Add session", this.user).setVisible(true);
    }//GEN-LAST:event_addSessionItemActionPerformed

    private void savedSubjBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_savedSubjBtnActionPerformed
        // TODO add your handling code here:
        if (!nameTextF.getText().isEmpty()) {
            if (!passwordTextF.getText().isEmpty()) {
                if (!sharedFolderTextF.getText().isEmpty()) {
                    Subject subject = null;
                    subject = MainController.existSubject(nameTextF.getText(), passwordTextF.getText());
                    if (subject == null) {
                        Boolean saved = false;
                        saved = MainController.addSubject(nameTextF.getText(), passwordTextF.getText(), descriptionTextA.getText(), sharedFolderTextF.getText(), this.user.getId());
                        if (saved) {
                            cleanFieldsSubject();
                            formuPanel.setVisible(false);
                            update_SubjectTable(false);
                        } else {
                            logSubjLabel.setText("Error..");
                        }
                    } else {
                        logSubjLabel.setText("Subject and password already exist.");

                    }
                } else {
                    logSubjLabel.setText("Shared Folder is empty.");
                }
            } else {
                logSubjLabel.setText("Password is empty.");
            }
        } else {
            logSubjLabel.setText("Name is empty.");
        }
    }//GEN-LAST:event_savedSubjBtnActionPerformed

    private void sharedFolderBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sharedFolderBtnActionPerformed
        // TODO add your handling code here:
        if (fileChooserSub.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            sharedFolderTextF.setText(fileChooserSub.getSelectedFile().toString());
            //MainController.updateSharedFolder(sharedFolderTxt.getText());

        } else {
            System.out.println("No Selection ");
        }
    }//GEN-LAST:event_sharedFolderBtnActionPerformed

    private void FilesSharedBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_FilesSharedBtnActionPerformed
        if (fileChooserSes.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            sharedFileTextF.setText(fileChooserSes.getSelectedFile().toString());
            //MainController.updateSharedFolder(sharedFolderTxt.getText());

        } else {
            System.out.println("No Selection ");
        }
    }//GEN-LAST:event_FilesSharedBtnActionPerformed

    private void savedSessBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_savedSessBtnActionPerformed
        // TODO add your handling code here:

    }//GEN-LAST:event_savedSessBtnActionPerformed

    private void addSubjectBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addSubjectBtnActionPerformed
        // TODO add your handling code here:
        formuPanel.setVisible(true);
    }//GEN-LAST:event_addSubjectBtnActionPerformed

    private void cancelSubjectBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelSubjectBtnActionPerformed
        // TODO add your handling code here:
        formuPanel.setVisible(false);
    }//GEN-LAST:event_cancelSubjectBtnActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton FilesSharedBtn;
    private javax.swing.JMenuItem addSessionItem;
    private javax.swing.JButton addSubjectBtn;
    private javax.swing.JMenuItem addSubjectItem;
    private javax.swing.JList<String> archivos;
    private javax.swing.JButton cancelSubjectBtn;
    private javax.swing.JComboBox<String> classroomsCB;
    private javax.swing.JPanel consultPanel;
    private javax.swing.JLabel dLabel;
    private javax.swing.JTextField dateTextF;
    private javax.swing.JTextArea descriptionTextA;
    private javax.swing.JScrollPane docusPanel;
    private javax.swing.JSpinner durationSpin;
    private javax.swing.JPanel formuPanel;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JMenuItem jMenuItem4;
    private javax.swing.JMenuItem jMenuItem5;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTable jTable1;
    private javax.swing.JLabel logSessLabel;
    private javax.swing.JLabel logSubjLabel;
    private javax.swing.JTextField nameTextF;
    private javax.swing.JPasswordField passwordTextF;
    private javax.swing.JButton savedSessBtn;
    private javax.swing.JButton savedSubjBtn;
    private javax.swing.JMenu sessionMenu;
    private javax.swing.JPanel sessionPanel;
    private javax.swing.JTextField sharedFileTextF;
    private javax.swing.JButton sharedFolderBtn;
    private javax.swing.JTextField sharedFolderTextF;
    private javax.swing.JMenu subjectMenu;
    private javax.swing.JPanel subjectPanel;
    private javax.swing.JComboBox<String> subjectsCB;
    private javax.swing.JLabel tLabel;
    private javax.swing.JTabbedPane tabPanel;
    private javax.swing.JLabel tituloLbl;
    private javax.swing.JMenuItem updateSessionItem;
    private javax.swing.JMenuItem updateSubjectItem;
    private javax.swing.JLabel userLabel;
    // End of variables declaration//GEN-END:variables

    @Override
    public void updateList() {

    }

    @Override
    public void disconnectedUser(ClientModel usuario) {

        ArrayList<ServerFile> root = (ArrayList<ServerFile>) stack.get(0).clone();
        ArrayList<ServerFile> rootOr = stack.get(0);
        for (ServerFile sf : root) {
            if (sf.getHost().equals(usuario.getAddress())) {
                rootOr.remove(sf);
            }
        }

        if (currentIndex == 0) { //Si es la vista de todos los folder y no solo del servidor donde se entro
            refresh(rootOr);
        }

    }

    @Override
    public void connectedUser(ClientModel usuario) {

        System.out.println("Se conecto: " + usuario.getAddress());

        ArrayList<ServerFile> files = MainController.getRootContent(usuario.getAddress());

        ArrayList<ServerFile> root = stack.get(0);

        root.addAll(files);

        if (currentIndex == 0) { //Si es la vista de todos los folder y no solo del servidor donde se entro
            refresh(root);
        }
    }

    @Override
    public void startEvent(Object source, String descripcion, int eventId) {
        if (eventId == 0) {
            descargasActivas++;
        } else if (eventId == 1) {
            transferenciasActivas++;
        }

        updateLabels();
    }

    @Override
    public void endEvent(Object source, String descripcion, int eventId) {
        if (eventId == 0) {
            descargasActivas--;
        } else if (eventId == 1) {
            transferenciasActivas--;
        }

        updateLabels();
    }

    private void updateLabels() {
        dLabel.setText("Descargando: " + descargasActivas);
        tLabel.setText("Enviando: " + transferenciasActivas);
    }

}
