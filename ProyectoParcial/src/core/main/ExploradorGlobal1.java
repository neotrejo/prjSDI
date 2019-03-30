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
import core.data.FilesSession;
import core.data.ServerFile;
import core.data.Session;
import core.data.Subject;
import core.data.FileD;
import core.data.Subscription;
import core.data.Subscriptor;
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
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.beans.PropertyVetoException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Stack;
import java.time.*;
import java.io.File;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JSpinner.DateEditor;
import javax.swing.SpinnerDateModel;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.DateFormatter;

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
    private Subject subject;
    private Classroom classroom;
    private FilesSession filesession;
    private int session_id;
    private int file_id;
    private ArrayList<Classroom> classrooms;
    private ArrayList<Subject> subjects;
    private ArrayList<Session> sessions;
    private ArrayList<Subscription> subscriptions;
    private ArrayList<Subject> subjectsSubscriptions;
    private JFileChooser fileChooserSub;
    private JFileChooser fileChooserSes;
    private JFileChooser fileChooserUser;
    private DefaultTableModel modelSubj;
    private DefaultTableModel modelSess;
    private DefaultTableModel modelSubscription;
    private DefaultTableModel modelSubscriptor;
    private SpinnerDateModel modelStar;
    private SpinnerDateModel modelDuration;

    private SimpleDateFormat sourceFormat;
    private DateEditor editorStart;
    private DateEditor editorDuration;
    private Calendar calendarD;
    private Boolean actionAdd; //  true is Add and false is Modify
    private String subject_id; // for update subject

    private ExploradorGlobal1(ArrayList<ServerFile> rootFiles, ClientModel client, User user) throws PropertyVetoException {
        initComponents();

        stack = new ArrayList<>();

        this.client = client;
        this.user = user;
        userLabel.setText(user == null ? "" : user.getName());

        lista = this.archivos;
        lista.setCellRenderer(new ExplorerCellRenderer());
        //---------------------------        
//        jSplitPane1.setRightComponent(subscriptorsPanel);
//        jSplitPane1.setLeftComponent(conSubPanel);

        //----------------tabbedPanel------------//
        tabPanel.setTitleAt(0, "Files");
        tabPanel.setMnemonicAt(0, KeyEvent.VK_1);
        tabPanel.setTitleAt(1, "Subject");
        tabPanel.setMnemonicAt(1, KeyEvent.VK_2);
        tabPanel.setTitleAt(2, "Session");
        tabPanel.setMnemonicAt(2, KeyEvent.VK_3);
        tabPanel.setTitleAt(3, "Subscription");
        tabPanel.setMnemonicAt(3, KeyEvent.VK_4);
        tabPanel.setSelectedIndex(0);

        //-----------JDateChooser----------------//
        datePicker.getJCalendar().setMinSelectableDate(new Date());

        //---------filechooser----------------------//
        fileChooserSub = new JFileChooser(user.getSharedFolder());
        fileChooserSub.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        fileChooserSub.setAcceptAllFileFilterUsed(false);

        fileChooserSes = new JFileChooser();
        fileChooserSes.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fileChooserSes.addChoosableFileFilter(new FileNameExtensionFilter("PDF Documents", "pdf"));
        fileChooserSes.addChoosableFileFilter(new FileNameExtensionFilter("MS Office Documents", "docx", "xlsx", "pptx"));
        fileChooserSes.addChoosableFileFilter(new FileNameExtensionFilter("Images", "jpg", "png", "gif", "bmp"));
        fileChooserSes.setAcceptAllFileFilterUsed(false);

        fileChooserUser = new JFileChooser(user.getSharedFolder());
        fileChooserUser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        fileChooserUser.setAcceptAllFileFilterUsed(false);

        //---------internalFrame----------------//
        formaSubjectIFrame.setClosed(true);
        formaSessionIFrame.setClosed(true);
        formaSubscripIFrame.setClosed(true);
        accountIFrame.setClosed(true);
        //------------spinners--------------//
        calendarD = Calendar.getInstance();
        calendarD.set(Calendar.HOUR_OF_DAY, 24); // 24 == 12 PM == 00:00:00
        calendarD.set(Calendar.MINUTE, 0);
        calendarD.set(Calendar.SECOND, 0);

        modelStar = new SpinnerDateModel();
        modelStar.setValue(calendarD.getTime());
        startTimeSpin.setModel(modelStar);
        editorStart = new DateEditor(startTimeSpin, "HH:mm");
        DateFormatter formatter = (DateFormatter) editorStart.getTextField().getFormatter();
        formatter.setAllowsInvalid(false);
        formatter.setOverwriteMode(true);
        startTimeSpin.setEditor(editorStart);

        modelDuration = new SpinnerDateModel();
        modelDuration.setValue(calendarD.getTime());
        durationSpin.setModel(modelDuration);
        editorDuration = new DateEditor(durationSpin, "HH:mm");
        DateFormatter formatterD = (DateFormatter) editorDuration.getTextField().getFormatter();
        formatterD.setAllowsInvalid(false);
        formatterD.setOverwriteMode(true);
        durationSpin.setEditor(editorDuration);

        //--------tables-----------------//
        String[] headSubject = {"Name", "SharedFolder", "Description", "", "", ""};
        modelSubj = new DefaultTableModel();
        subjectsTable.setModel(modelSubj);
        for (String s : headSubject) {
            modelSubj.addColumn(s);
        }
        updateSubjectTable(false);

        String[] headSession = {"Date", "Subject", "Classroom", "File", "", ""};
        modelSess = new DefaultTableModel();
        sessionsTable.setModel(modelSess);
        for (String s : headSession) {
            modelSess.addColumn(s);
        }
        sourceFormat = new SimpleDateFormat("dd/MM/yyyy");
        updateSessionTable(false);

        String[] headSubscription = {"Subject", "Speaker", ""};
        modelSubscription = new DefaultTableModel();
        subscriptionsTable.setModel(modelSubscription);
        for (String s : headSubscription) {
            modelSubscription.addColumn(s);
        }
        updateSubscriptionsTable(false);

        String[] headSubscriptor = {"Name", "Email"};
        modelSubscriptor = new DefaultTableModel();
        subscriptorsTable.setModel(modelSubscriptor);
        for (String s : headSubscriptor) {
            modelSubscriptor.addColumn(s);
        }

        //------------------------------//
        updateCbSession();
        //-----------------------------//
        stack.add(rootFiles);

        NetworkConnections.getInstance().start(this);
        UserDisconnection.getInstance().start();
        UserDisconnection.getInstance().register(this);
        setLocationRelativeTo(null);
    }

    public static ExploradorGlobal1 getInstance() throws PropertyVetoException {
        if (instance == null) {
            instance = new ExploradorGlobal1(new ArrayList<ServerFile>(), null, null);
        }
        return instance;
    }

    public static ExploradorGlobal1 getInstance(User user) throws PropertyVetoException {
        if (instance == null) {
            instance = new ExploradorGlobal1(new ArrayList<ServerFile>(), null, user);
        }
        return instance;
    }

    private void updateSubjectTable(boolean clean) {
        if (clean) {
            int filas = subjectsTable.getRowCount();
            for (int i = 0; filas > i; i++) {
                modelSubj.removeRow(0);
            }
        }
        subjects = MainController.getSubjectsUser(user.getId());
        if (subjects != null || subjects.size() == 0) {
            subjects.forEach((n) -> modelSubj.addRow(new Object[]{n.getName(), n.getSharedFolder(), n.getDescription(), "Edit", "Deleted", "Subscriptors"}));
        }
    }

    private void updateCbSession() {
        classrooms = MainController.getClassrooms();
        if (classrooms != null || classrooms.size() == 0) {
            classroomsCB.removeAllItems();
            classrooms.forEach((n) -> classroomsCB.addItem(n.getName()));
            if (subjects != null || subjects.size() == 0) {
                subjectsCB.removeAllItems();
                subjects.forEach((n) -> subjectsCB.addItem(n.getName()));
                savedSessBtn.setEnabled(true);
            } else {
                logSessLabel.setText("There aren´t subjects.");
            }
        } else {
            logSessLabel.setText("There aren´t classrooms.");
        }
    }

    private void updateSessionTable(boolean clean) {
        if (clean) {
            int filas = sessionsTable.getRowCount();
            for (int i = 0; filas > i; i++) {
                modelSess.removeRow(0);
            }
        }
        sessions = MainController.getSessions(user.getId());
        if (sessions != null || sessions.size() == 0) {
            sessions.forEach((n) -> {
                try {
                    modelSess.addRow(new Object[]{n.getDate(), n.getSubjectId(), n.getClassRoomId(), n.getFile(), "Edit", "Delete"});
                } catch (ParseException ex) {
                    Logger.getLogger(ExploradorGlobal1.class.getName()).log(Level.SEVERE, null, ex);
                }
            });
        }
    }

    private void updateSubscriptionsTable(boolean clean) {
        if (clean) {
            int filas = subscriptionsTable.getRowCount();
            for (int i = 0; filas > i; i++) {
                modelSubscription.removeRow(0);
            }
        }
        subscriptions = MainController.getSubscriptionUserId(user.getId());
        //ImageIcon edit = new ImageIcon(getClass().getResource("/core/images/edit.png"));
        if (subscriptions.size() != 0 || subscriptions != null) {
            subscriptions.forEach((n) -> {
                modelSubscription.addRow(new Object[]{n.getSubject(), n.getprofessor(), "Cancel"});
            });
        }
    }

    private void updateSubscriptorsTable(ArrayList<Subscriptor> subscriptors) {
        int filas = subscriptorsTable.getRowCount();
        for (int i = 0; filas > i; i++) {
            modelSubscriptor.removeRow(0);
        }
        if (subscriptors.size() != 0 || subscriptors != null) {
            subscriptors.forEach((n) -> {
                modelSubscriptor.addRow(new Object[]{n.getName(), n.getEmail()});
            });
        }
    }

    private void cleanFieldsSubject() {
        nameTextF.setText("");
        passwordTextF.setText("");
        sharedFolderTextF.setText("");
        descriptionTextA.setText("");
        logSubjLabel.setText("");
    }

    private void cleanFieldsSession() {
        datePicker.setCalendar(null);
        sharedFileTextF.setText("");
        classroomsCB.setSelectedIndex(0);
        subjectsCB.setSelectedIndex(0);
        startTimeSpin.setValue(calendarD.getTime());
        durationSpin.setValue(calendarD.getTime());
        logSessLabel.setText("");
    }

    private void cleanFieldsSubscription() {
        subjectsSubsCb.setSelectedIndex(0);
        passwordSubscField.setText("");
        logSubsLabel.setText("");
    }

    private void setFieldsSession(FileD file, Session sesion, String subject, String classroom) throws ParseException {

        formaSessionIFrame.setTitle("Modify session");
        Calendar calendarAux = Calendar.getInstance();

        String[] date = sesion.getDate().split("-");
        datePicker.setDate(new GregorianCalendar(Integer.parseInt(date[2]), Integer.parseInt(date[1]) - 1, Integer.parseInt(date[0])).getTime());

        fileChooserSes.setCurrentDirectory(new File(file.getPath()));
        sharedFileTextF.setText(file.getFileName());

        classroomsCB.setSelectedItem(classroom);
        subjectsCB.setSelectedItem(subject);

        String[] startTime = sesion.getStartTime().split(":");
        String[] duration = sesion.getDurationHrs().split(":");

        calendarAux.set(Calendar.HOUR_OF_DAY, Integer.parseInt(startTime[0])); // 24 == 12 PM == 00:00:00
        calendarAux.set(Calendar.MINUTE, Integer.parseInt(startTime[1]));
        calendarAux.set(Calendar.SECOND, 0);
        startTimeSpin.setValue(calendarAux.getTime());

        calendarAux.set(Calendar.HOUR_OF_DAY, Integer.parseInt(duration[0])); // 24 == 12 PM == 00:00:00
        calendarAux.set(Calendar.MINUTE, Integer.parseInt(duration[1]));
        durationSpin.setValue(calendarAux.getTime());
        formaSessionIFrame.setVisible(true);
    }

    private void setFieldsSubject(Subject subject) {
        formaSubjectIFrame.setTitle("Modify subject");
        nameTextF.setText(subject.getName());
        passwordTextF.setText(subject.getPassword());
        fileChooserSub.setCurrentDirectory(new File(subject.getSharedFolder()));
        sharedFolderTextF.setText(subject.getSharedFolder());
        descriptionTextA.setText(subject.getDescription());
        logSubjLabel.setText("");
        formaSubjectIFrame.setVisible(true);
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
        tLabel = new javax.swing.JLabel();
        dLabel = new javax.swing.JLabel();
        userLabel = new javax.swing.JLabel();
        tabPanel = new javax.swing.JTabbedPane();
        filesPanel = new javax.swing.JPanel();
        docPanel = new javax.swing.JScrollPane();
        archivos = new javax.swing.JList<>();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        subjectPanel = new javax.swing.JPanel();
        conSubPanel = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        subjectsTable = new javax.swing.JTable();
        formaSubjectIFrame = new javax.swing.JInternalFrame();
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
        logSubjLabel = new javax.swing.JLabel();
        addSubjectBtn = new javax.swing.JButton();
        subscriptorsPanel = new javax.swing.JPanel();
        jScrollPane5 = new javax.swing.JScrollPane();
        subscriptorsTable = new javax.swing.JTable();
        sessionPanel = new javax.swing.JPanel();
        conSessPanel = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        sessionsTable = new javax.swing.JTable();
        addSessionBtn = new javax.swing.JButton();
        formaSessionIFrame = new javax.swing.JInternalFrame();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        subjectsCB = new javax.swing.JComboBox<>();
        classroomsCB = new javax.swing.JComboBox<>();
        FilesSharedBtn = new javax.swing.JButton();
        durationSpin = new javax.swing.JSpinner();
        savedSessBtn = new javax.swing.JButton();
        logSessLabel = new javax.swing.JLabel();
        sharedFileTextF = new javax.swing.JTextField();
        datePicker = new com.toedter.calendar.JDateChooser();
        jLabel11 = new javax.swing.JLabel();
        startTimeSpin = new javax.swing.JSpinner();
        subscriptionPanel = new javax.swing.JPanel();
        conSubsPanel = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        subscriptionsTable = new javax.swing.JTable();
        addSubscription = new javax.swing.JButton();
        formaSubscripIFrame = new javax.swing.JInternalFrame();
        jLabel10 = new javax.swing.JLabel();
        subjectsSubsCb = new javax.swing.JComboBox<>();
        savedSubscriptionBtn = new javax.swing.JButton();
        logSubsLabel = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        passwordSubscField = new javax.swing.JPasswordField();
        jLabel12 = new javax.swing.JLabel();
        speakerTextF = new javax.swing.JTextField();
        accountIFrame = new javax.swing.JInternalFrame();
        jPanel1 = new javax.swing.JPanel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        nameUserTextF = new javax.swing.JTextField();
        usernameTextF = new javax.swing.JTextField();
        passwordUserTextF = new javax.swing.JPasswordField();
        emailTextF = new javax.swing.JTextField();
        hostnameTextF = new javax.swing.JTextField();
        modifyAccountBtn = new javax.swing.JButton();
        jLabel19 = new javax.swing.JLabel();
        sharedfolderUserTextF = new javax.swing.JTextField();
        sharedFolderUserBtn = new javax.swing.JButton();
        logUserLabel = new javax.swing.JLabel();
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

        tLabel.setText("Transfiriendo: 0");

        dLabel.setText("Descargando: 0");

        userLabel.setFont(new java.awt.Font("Tahoma", 1, 15)); // NOI18N
        userLabel.setForeground(new java.awt.Color(0, 51, 153));
        userLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        userLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/core/images/user.png"))); // NOI18N
        userLabel.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        userLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                userLabelMouseClicked(evt);
            }
        });

        tabPanel.setTabLayoutPolicy(javax.swing.JTabbedPane.SCROLL_TAB_LAYOUT);
        tabPanel.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N

        filesPanel.setBackground(java.awt.SystemColor.controlLtHighlight);

        docPanel.setBackground(java.awt.SystemColor.controlLtHighlight);

        archivos.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                archivosMouseClicked(evt);
            }
        });
        docPanel.setViewportView(archivos);

        jButton1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/core/images/ant2.png"))); // NOI18N
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/core/images/next2.png"))); // NOI18N
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout filesPanelLayout = new javax.swing.GroupLayout(filesPanel);
        filesPanel.setLayout(filesPanelLayout);
        filesPanelLayout.setHorizontalGroup(
            filesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, filesPanelLayout.createSequentialGroup()
                .addGap(0, 677, Short.MAX_VALUE)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(filesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(filesPanelLayout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(docPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 774, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );
        filesPanelLayout.setVerticalGroup(
            filesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(filesPanelLayout.createSequentialGroup()
                .addGroup(filesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1)
                    .addComponent(jButton2))
                .addGap(0, 677, Short.MAX_VALUE))
            .addGroup(filesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(filesPanelLayout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(docPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 611, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );

        tabPanel.addTab("tab4", filesPanel);

        subjectPanel.setBackground(java.awt.SystemColor.controlLtHighlight);
        subjectPanel.setPreferredSize(new java.awt.Dimension(258, 130));
        subjectPanel.setRequestFocusEnabled(false);

        conSubPanel.setBackground(java.awt.SystemColor.controlLtHighlight);
        conSubPanel.setPreferredSize(new java.awt.Dimension(710, 571));

        jScrollPane3.setBackground(java.awt.SystemColor.controlLtHighlight);
        jScrollPane3.setMaximumSize(new java.awt.Dimension(32767, 200));
        jScrollPane3.setMinimumSize(new java.awt.Dimension(27, 100));
        jScrollPane3.setPreferredSize(new java.awt.Dimension(452, 202));

        subjectsTable.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        subjectsTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        subjectsTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                subjectsTableMouseClicked(evt);
            }
        });
        jScrollPane3.setViewportView(subjectsTable);

        formaSubjectIFrame.setClosable(true);
        formaSubjectIFrame.setTitle("Add subject");
        formaSubjectIFrame.setMaximumSize(new java.awt.Dimension(300, 345));
        formaSubjectIFrame.setPreferredSize(new java.awt.Dimension(300, 345));
        formaSubjectIFrame.setVisible(true);

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
        sharedFolderBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/core/images/folder2.png"))); // NOI18N
        sharedFolderBtn.setMargin(new java.awt.Insets(2, 7, 2, 7));
        sharedFolderBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sharedFolderBtnActionPerformed(evt);
            }
        });

        descriptionTextA.setColumns(20);
        descriptionTextA.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        descriptionTextA.setRows(5);
        jScrollPane2.setViewportView(descriptionTextA);

        logSubjLabel.setForeground(new java.awt.Color(153, 0, 0));
        logSubjLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        javax.swing.GroupLayout formaSubjectIFrameLayout = new javax.swing.GroupLayout(formaSubjectIFrame.getContentPane());
        formaSubjectIFrame.getContentPane().setLayout(formaSubjectIFrameLayout);
        formaSubjectIFrameLayout.setHorizontalGroup(
            formaSubjectIFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(formaSubjectIFrameLayout.createSequentialGroup()
                .addGap(40, 40, 40)
                .addGroup(formaSubjectIFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(formaSubjectIFrameLayout.createSequentialGroup()
                        .addGroup(formaSubjectIFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel3)
                            .addComponent(jLabel4)
                            .addComponent(jLabel2))
                        .addGroup(formaSubjectIFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(formaSubjectIFrameLayout.createSequentialGroup()
                                .addGap(23, 23, 23)
                                .addComponent(passwordTextF, javax.swing.GroupLayout.PREFERRED_SIZE, 361, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(formaSubjectIFrameLayout.createSequentialGroup()
                                .addGap(22, 22, 22)
                                .addGroup(formaSubjectIFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(formaSubjectIFrameLayout.createSequentialGroup()
                                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 361, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(0, 0, Short.MAX_VALUE))
                                    .addGroup(formaSubjectIFrameLayout.createSequentialGroup()
                                        .addComponent(sharedFolderTextF)
                                        .addGap(18, 18, 18)
                                        .addComponent(sharedFolderBtn))))))
                    .addGroup(formaSubjectIFrameLayout.createSequentialGroup()
                        .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(nameTextF, javax.swing.GroupLayout.PREFERRED_SIZE, 359, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(672, 672, 672))
            .addGroup(formaSubjectIFrameLayout.createSequentialGroup()
                .addGroup(formaSubjectIFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(formaSubjectIFrameLayout.createSequentialGroup()
                        .addGap(256, 256, 256)
                        .addComponent(savedSubjBtn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(formaSubjectIFrameLayout.createSequentialGroup()
                        .addGap(93, 93, 93)
                        .addComponent(logSubjLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 411, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        formaSubjectIFrameLayout.setVerticalGroup(
            formaSubjectIFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(formaSubjectIFrameLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(formaSubjectIFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(nameTextF, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(formaSubjectIFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(passwordTextF, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(formaSubjectIFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(sharedFolderBtn, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(formaSubjectIFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(sharedFolderTextF, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel4)))
                .addGap(18, 18, 18)
                .addGroup(formaSubjectIFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(savedSubjBtn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(logSubjLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(331, 331, 331))
        );

        javax.swing.GroupLayout conSubPanelLayout = new javax.swing.GroupLayout(conSubPanel);
        conSubPanel.setLayout(conSubPanelLayout);
        conSubPanelLayout.setHorizontalGroup(
            conSubPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(conSubPanelLayout.createSequentialGroup()
                .addGap(68, 68, 68)
                .addComponent(formaSubjectIFrame, javax.swing.GroupLayout.PREFERRED_SIZE, 566, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(conSubPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 710, Short.MAX_VALUE))
        );
        conSubPanelLayout.setVerticalGroup(
            conSubPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(conSubPanelLayout.createSequentialGroup()
                .addComponent(formaSubjectIFrame, javax.swing.GroupLayout.PREFERRED_SIZE, 323, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(conSubPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(conSubPanelLayout.createSequentialGroup()
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 384, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 187, Short.MAX_VALUE)))
        );

        addSubjectBtn.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        addSubjectBtn.setForeground(new java.awt.Color(51, 51, 51));
        addSubjectBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/core/images/add2.png"))); // NOI18N
        addSubjectBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addSubjectBtnActionPerformed(evt);
            }
        });

        subscriptorsTable.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        subscriptorsTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        jScrollPane5.setViewportView(subscriptorsTable);

        javax.swing.GroupLayout subscriptorsPanelLayout = new javax.swing.GroupLayout(subscriptorsPanel);
        subscriptorsPanel.setLayout(subscriptorsPanelLayout);
        subscriptorsPanelLayout.setHorizontalGroup(
            subscriptorsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 710, Short.MAX_VALUE)
            .addGroup(subscriptorsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(subscriptorsPanelLayout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 686, Short.MAX_VALUE)
                    .addContainerGap()))
        );
        subscriptorsPanelLayout.setVerticalGroup(
            subscriptorsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 249, Short.MAX_VALUE)
            .addGroup(subscriptorsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(subscriptorsPanelLayout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 223, Short.MAX_VALUE)
                    .addContainerGap()))
        );

        javax.swing.GroupLayout subjectPanelLayout = new javax.swing.GroupLayout(subjectPanel);
        subjectPanel.setLayout(subjectPanelLayout);
        subjectPanelLayout.setHorizontalGroup(
            subjectPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(subjectPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(subjectPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(subjectPanelLayout.createSequentialGroup()
                        .addComponent(conSubPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(addSubjectBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(subjectPanelLayout.createSequentialGroup()
                        .addComponent(subscriptorsPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 52, Short.MAX_VALUE))))
        );
        subjectPanelLayout.setVerticalGroup(
            subjectPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(subjectPanelLayout.createSequentialGroup()
                .addGroup(subjectPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(addSubjectBtn)
                    .addGroup(subjectPanelLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(conSubPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 383, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(subscriptorsPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(50, Short.MAX_VALUE))
        );

        tabPanel.addTab("tab1", subjectPanel);

        sessionPanel.setBackground(java.awt.SystemColor.controlLtHighlight);

        conSessPanel.setBackground(java.awt.SystemColor.controlLtHighlight);

        jScrollPane1.setBackground(java.awt.SystemColor.controlLtHighlight);

        sessionsTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        sessionsTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                sessionsTableMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(sessionsTable);

        javax.swing.GroupLayout conSessPanelLayout = new javax.swing.GroupLayout(conSessPanel);
        conSessPanel.setLayout(conSessPanelLayout);
        conSessPanelLayout.setHorizontalGroup(
            conSessPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 704, Short.MAX_VALUE)
            .addGroup(conSessPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 704, Short.MAX_VALUE))
        );
        conSessPanelLayout.setVerticalGroup(
            conSessPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 310, Short.MAX_VALUE)
            .addGroup(conSessPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 310, Short.MAX_VALUE))
        );

        addSessionBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/core/images/add2.png"))); // NOI18N
        addSessionBtn.setToolTipText("");
        addSessionBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addSessionBtnBtnActionPerformed(evt);
            }
        });

        formaSessionIFrame.setClosable(true);
        formaSessionIFrame.setTitle("Create session");
        formaSessionIFrame.setVisible(true);

        jLabel5.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        jLabel5.setText("Subject:");

        jLabel6.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        jLabel6.setText("Classroom:");

        jLabel7.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        jLabel7.setText("Duration:");

        jLabel8.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        jLabel8.setText("Date:");

        jLabel9.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        jLabel9.setText("Files:");

        subjectsCB.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        subjectsCB.setPreferredSize(new java.awt.Dimension(32, 26));
        subjectsCB.addInputMethodListener(new java.awt.event.InputMethodListener() {
            public void caretPositionChanged(java.awt.event.InputMethodEvent evt) {
            }
            public void inputMethodTextChanged(java.awt.event.InputMethodEvent evt) {
                subjectsCBInputMethodTextChanged(evt);
            }
        });
        subjectsCB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                subjectsCBActionPerformed(evt);
            }
        });

        classroomsCB.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        classroomsCB.setPreferredSize(new java.awt.Dimension(32, 26));

        FilesSharedBtn.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        FilesSharedBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/core/images/folder2.png"))); // NOI18N
        FilesSharedBtn.setMargin(new java.awt.Insets(2, 7, 2, 7));
        FilesSharedBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                FilesSharedBtnActionPerformed(evt);
            }
        });

        durationSpin.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        durationSpin.setPreferredSize(new java.awt.Dimension(31, 26));

        savedSessBtn.setBackground(javax.swing.UIManager.getDefaults().getColor("Button.shadow"));
        savedSessBtn.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        savedSessBtn.setText("Save");
        savedSessBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                savedSessBtnActionPerformed(evt);
            }
        });

        logSessLabel.setForeground(new java.awt.Color(153, 0, 51));
        logSessLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        sharedFileTextF.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        sharedFileTextF.setEnabled(false);
        sharedFileTextF.setMinimumSize(new java.awt.Dimension(6, 26));

        datePicker.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        datePicker.setIcon(new javax.swing.ImageIcon(getClass().getResource("/core/images/calendar.png")));
        datePicker.setPreferredSize(new java.awt.Dimension(88, 26));

        jLabel11.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel11.setText("StartTime:");

        startTimeSpin.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        startTimeSpin.setPreferredSize(new java.awt.Dimension(31, 26));

        javax.swing.GroupLayout formaSessionIFrameLayout = new javax.swing.GroupLayout(formaSessionIFrame.getContentPane());
        formaSessionIFrame.getContentPane().setLayout(formaSessionIFrameLayout);
        formaSessionIFrameLayout.setHorizontalGroup(
            formaSessionIFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, formaSessionIFrameLayout.createSequentialGroup()
                .addGap(33, 33, 33)
                .addGroup(formaSessionIFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(formaSessionIFrameLayout.createSequentialGroup()
                        .addGroup(formaSessionIFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel5)
                            .addGroup(formaSessionIFrameLayout.createSequentialGroup()
                                .addGap(4, 4, 4)
                                .addComponent(jLabel9))
                            .addComponent(jLabel8))
                        .addGap(59, 59, 59)
                        .addGroup(formaSessionIFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(subjectsCB, javax.swing.GroupLayout.PREFERRED_SIZE, 346, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(formaSessionIFrameLayout.createSequentialGroup()
                                .addGroup(formaSessionIFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(classroomsCB, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(datePicker, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(39, 39, 39)
                                .addGroup(formaSessionIFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, formaSessionIFrameLayout.createSequentialGroup()
                                        .addComponent(jLabel11)
                                        .addGap(31, 31, 31)))
                                .addGroup(formaSessionIFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(startTimeSpin, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(durationSpin, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(formaSessionIFrameLayout.createSequentialGroup()
                                .addComponent(sharedFileTextF, javax.swing.GroupLayout.PREFERRED_SIZE, 289, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(FilesSharedBtn))))
                    .addComponent(jLabel6))
                .addContainerGap(25, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, formaSessionIFrameLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(formaSessionIFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, formaSessionIFrameLayout.createSequentialGroup()
                        .addComponent(savedSessBtn)
                        .addGap(231, 231, 231))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, formaSessionIFrameLayout.createSequentialGroup()
                        .addComponent(logSessLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 318, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(96, 96, 96))))
        );
        formaSessionIFrameLayout.setVerticalGroup(
            formaSessionIFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(formaSessionIFrameLayout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addGroup(formaSessionIFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(subjectsCB, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(formaSessionIFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(formaSessionIFrameLayout.createSequentialGroup()
                        .addGroup(formaSessionIFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel6)
                            .addComponent(classroomsCB, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel11))
                        .addGap(18, 18, 18)
                        .addGroup(formaSessionIFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel7)
                            .addComponent(datePicker, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel8)))
                    .addGroup(formaSessionIFrameLayout.createSequentialGroup()
                        .addComponent(startTimeSpin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(durationSpin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addGroup(formaSessionIFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(FilesSharedBtn)
                    .addComponent(sharedFileTextF, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(26, 26, 26)
                .addComponent(savedSessBtn)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(logSessLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(77, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout sessionPanelLayout = new javax.swing.GroupLayout(sessionPanel);
        sessionPanel.setLayout(sessionPanelLayout);
        sessionPanelLayout.setHorizontalGroup(
            sessionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, sessionPanelLayout.createSequentialGroup()
                .addGap(0, 729, Short.MAX_VALUE)
                .addComponent(addSessionBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(sessionPanelLayout.createSequentialGroup()
                .addGap(45, 45, 45)
                .addComponent(formaSessionIFrame, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(sessionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(sessionPanelLayout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(conSessPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(58, Short.MAX_VALUE)))
        );
        sessionPanelLayout.setVerticalGroup(
            sessionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, sessionPanelLayout.createSequentialGroup()
                .addComponent(addSessionBtn)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 192, Short.MAX_VALUE)
                .addComponent(formaSessionIFrame, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(107, 107, 107))
            .addGroup(sessionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(sessionPanelLayout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(conSessPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(379, Short.MAX_VALUE)))
        );

        tabPanel.addTab("tab3", sessionPanel);

        subscriptionPanel.setBackground(java.awt.SystemColor.controlLtHighlight);

        conSubsPanel.setBackground(java.awt.SystemColor.controlLtHighlight);

        jScrollPane4.setBackground(java.awt.SystemColor.controlLtHighlight);

        subscriptionsTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        subscriptionsTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                subscriptionsTableMouseClicked(evt);
            }
        });
        jScrollPane4.setViewportView(subscriptionsTable);

        javax.swing.GroupLayout conSubsPanelLayout = new javax.swing.GroupLayout(conSubsPanel);
        conSubsPanel.setLayout(conSubsPanelLayout);
        conSubsPanelLayout.setHorizontalGroup(
            conSubsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 706, Short.MAX_VALUE)
            .addGroup(conSubsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 706, Short.MAX_VALUE))
        );
        conSubsPanelLayout.setVerticalGroup(
            conSubsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 310, Short.MAX_VALUE)
            .addGroup(conSubsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 310, Short.MAX_VALUE))
        );

        addSubscription.setIcon(new javax.swing.ImageIcon(getClass().getResource("/core/images/add2.png"))); // NOI18N
        addSubscription.setToolTipText("");
        addSubscription.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addSubscriptionActionPerformed(evt);
            }
        });

        formaSubscripIFrame.setClosable(true);
        formaSubscripIFrame.setTitle("Add subscription");
        formaSubscripIFrame.setToolTipText("");
        formaSubscripIFrame.setVisible(true);

        jLabel10.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        jLabel10.setText("Subject:");

        subjectsSubsCb.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        subjectsSubsCb.setPreferredSize(new java.awt.Dimension(32, 26));
        subjectsSubsCb.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                subjectsSubsCbActionPerformed(evt);
            }
        });

        savedSubscriptionBtn.setBackground(javax.swing.UIManager.getDefaults().getColor("Button.shadow"));
        savedSubscriptionBtn.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        savedSubscriptionBtn.setText("Save");
        savedSubscriptionBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                savedSubscriptionBtnActionPerformed(evt);
            }
        });

        logSubsLabel.setForeground(new java.awt.Color(153, 0, 51));
        logSubsLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        jLabel17.setText("Password:");

        passwordSubscField.setPreferredSize(new java.awt.Dimension(10, 22));

        jLabel12.setText("Speaker:");
        jLabel12.setToolTipText("");

        speakerTextF.setEnabled(false);

        javax.swing.GroupLayout formaSubscripIFrameLayout = new javax.swing.GroupLayout(formaSubscripIFrame.getContentPane());
        formaSubscripIFrame.getContentPane().setLayout(formaSubscripIFrameLayout);
        formaSubscripIFrameLayout.setHorizontalGroup(
            formaSubscripIFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(formaSubscripIFrameLayout.createSequentialGroup()
                .addGroup(formaSubscripIFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(formaSubscripIFrameLayout.createSequentialGroup()
                        .addGap(226, 226, 226)
                        .addComponent(savedSubscriptionBtn))
                    .addGroup(formaSubscripIFrameLayout.createSequentialGroup()
                        .addGap(33, 33, 33)
                        .addGroup(formaSubscripIFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel10)
                            .addGroup(formaSubscripIFrameLayout.createSequentialGroup()
                                .addGroup(formaSubscripIFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel17)
                                    .addComponent(jLabel12))
                                .addGap(59, 59, 59)
                                .addGroup(formaSubscripIFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(passwordSubscField, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(subjectsSubsCb, 0, 290, Short.MAX_VALUE)
                                    .addComponent(speakerTextF)))))
                    .addGroup(formaSubscripIFrameLayout.createSequentialGroup()
                        .addGap(81, 81, 81)
                        .addComponent(logSubsLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 336, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(45, Short.MAX_VALUE))
        );
        formaSubscripIFrameLayout.setVerticalGroup(
            formaSubscripIFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(formaSubscripIFrameLayout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addGroup(formaSubscripIFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(subjectsSubsCb, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(25, 25, 25)
                .addGroup(formaSubscripIFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel12)
                    .addComponent(speakerTextF, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 78, Short.MAX_VALUE)
                .addGroup(formaSubscripIFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel17)
                    .addComponent(passwordSubscField, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(37, 37, 37)
                .addComponent(savedSubscriptionBtn)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(logSubsLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout subscriptionPanelLayout = new javax.swing.GroupLayout(subscriptionPanel);
        subscriptionPanel.setLayout(subscriptionPanelLayout);
        subscriptionPanelLayout.setHorizontalGroup(
            subscriptionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, subscriptionPanelLayout.createSequentialGroup()
                .addGap(0, 729, Short.MAX_VALUE)
                .addComponent(addSubscription, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(subscriptionPanelLayout.createSequentialGroup()
                .addGap(47, 47, 47)
                .addComponent(formaSubscripIFrame, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(subscriptionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(subscriptionPanelLayout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(conSubsPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(56, Short.MAX_VALUE)))
        );
        subscriptionPanelLayout.setVerticalGroup(
            subscriptionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, subscriptionPanelLayout.createSequentialGroup()
                .addComponent(addSubscription)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 227, Short.MAX_VALUE)
                .addComponent(formaSubscripIFrame, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(109, 109, 109))
            .addGroup(subscriptionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(subscriptionPanelLayout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(conSubsPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(379, Short.MAX_VALUE)))
        );

        tabPanel.addTab("tab3", subscriptionPanel);

        accountIFrame.setClosable(true);
        accountIFrame.setTitle("Modify user");
        accountIFrame.setPreferredSize(new java.awt.Dimension(507, 392));
        accountIFrame.setVisible(true);

        jLabel13.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        jLabel13.setText("Name:");

        jLabel14.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        jLabel14.setText("Username:");

        jLabel15.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        jLabel15.setText("Password:");

        jLabel16.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        jLabel16.setText("Email:");

        jLabel18.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        jLabel18.setText("Hostname:");

        nameUserTextF.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N

        usernameTextF.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N

        passwordUserTextF.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N

        emailTextF.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N

        hostnameTextF.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N

        modifyAccountBtn.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        modifyAccountBtn.setText("Save");
        modifyAccountBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                modifyAccountBtnActionPerformed(evt);
            }
        });

        jLabel19.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        jLabel19.setText("Shared Folder:");

        sharedfolderUserTextF.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N

        sharedFolderUserBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/core/images/folder2.png"))); // NOI18N
        sharedFolderUserBtn.setMargin(new java.awt.Insets(2, 7, 2, 7));
        sharedFolderUserBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sharedFolderUserBtnActionPerformed(evt);
            }
        });

        logUserLabel.setForeground(new java.awt.Color(153, 0, 51));
        logUserLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel13)
                    .addComponent(jLabel14)
                    .addComponent(jLabel16)
                    .addComponent(jLabel18)
                    .addComponent(jLabel15)
                    .addComponent(jLabel19))
                .addGap(27, 27, 27)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(59, 59, 59)
                        .addComponent(modifyAccountBtn)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(sharedfolderUserTextF)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(sharedFolderUserBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(nameUserTextF)
                            .addComponent(usernameTextF)
                            .addComponent(emailTextF)
                            .addComponent(hostnameTextF)
                            .addComponent(passwordUserTextF))
                        .addGap(30, 30, 30))))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(91, Short.MAX_VALUE)
                .addComponent(logUserLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 322, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(51, 51, 51))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(nameUserTextF, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel13))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(usernameTextF, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel14))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(passwordUserTextF, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel15))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(emailTextF, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel16))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(hostnameTextF, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel18))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel19)
                    .addComponent(sharedfolderUserTextF, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(sharedFolderUserBtn))
                .addGap(18, 18, 18)
                .addComponent(modifyAccountBtn)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(logUserLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout accountIFrameLayout = new javax.swing.GroupLayout(accountIFrame.getContentPane());
        accountIFrame.getContentPane().setLayout(accountIFrameLayout);
        accountIFrameLayout.setHorizontalGroup(
            accountIFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 515, Short.MAX_VALUE)
            .addGroup(accountIFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(accountIFrameLayout.createSequentialGroup()
                    .addGap(39, 39, 39)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );
        accountIFrameLayout.setVerticalGroup(
            accountIFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 365, Short.MAX_VALUE)
            .addGroup(accountIFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(accountIFrameLayout.createSequentialGroup()
                    .addGap(8, 8, 8)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGap(8, 8, 8)))
        );

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
                        .addComponent(dLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(tLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 166, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(userLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 263, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(tabPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 779, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jSeparator1, javax.swing.GroupLayout.DEFAULT_SIZE, 1, Short.MAX_VALUE)))
                .addContainerGap())
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(158, 158, 158)
                    .addComponent(accountIFrame, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(156, Short.MAX_VALUE)))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(userLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 2, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tabPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 735, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(dLabel)
                    .addComponent(tLabel))
                .addGap(9, 9, 9))
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(231, 231, 231)
                    .addComponent(accountIFrame, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(182, Short.MAX_VALUE)))
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
        tabPanel.setSelectedIndex(0);
        formaSubjectIFrame.setVisible(true);
    }//GEN-LAST:event_addSubjectItemActionPerformed

    private void addSessionItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addSessionItemActionPerformed
        tabPanel.setSelectedIndex(1);
        formaSessionIFrame.setVisible(true);
        actionAdd = true;
    }//GEN-LAST:event_addSessionItemActionPerformed

    private void savedSubjBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_savedSubjBtnActionPerformed
        // TODO add your handling code here:
        if (!nameTextF.getText().isEmpty()) {
            if (!passwordTextF.getText().isEmpty()) {
                if (!sharedFolderTextF.getText().isEmpty()) {
                    Subject subject = null;
                    if (actionAdd) {
                        subject = MainController.existSubjectName(nameTextF.getText());
                        if (subject == null) {
                            MainController.addSubject(nameTextF.getText(), passwordTextF.getText(), descriptionTextA.getText(), sharedFolderTextF.getText(), this.user.getId());
                            updateCbSession();
                        } else {
                            logSubjLabel.setText("Subject witn same name already exist.");
                        }

                    } else {
                        subject = MainController.existSubjectNameNotId(nameTextF.getText(), subject_id);
                        if (subject == null) {
                            MainController.updateSubject(subject_id, nameTextF.getText(), passwordTextF.getText(), descriptionTextA.getText(), sharedFolderTextF.getText());

                        } else {
                            logSubjLabel.setText("Subject witn same name already exist.");
                        }
                    }
                    cleanFieldsSubject();
                    formaSubjectIFrame.doDefaultCloseAction();
                    updateSubjectTable(true);

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
        } else {
            System.out.println("No Selection ");
        }
    }//GEN-LAST:event_sharedFolderBtnActionPerformed

    private void FilesSharedBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_FilesSharedBtnActionPerformed
        if (fileChooserSes.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            sharedFileTextF.setText(fileChooserSes.getSelectedFile().getName());
        } else {
            System.out.println("No Selection ");
        }
    }//GEN-LAST:event_FilesSharedBtnActionPerformed

    private void savedSessBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_savedSessBtnActionPerformed
        if (!sharedFileTextF.getText().isEmpty()) {
            if (datePicker.getDate().after(new Date())) {
                if (editorStart.getFormat().format(startTimeSpin.getValue()) != "00:00") {
                    String hour = editorStart.getFormat().format(startTimeSpin.getValue()) + ":00";
                    String duration = editorDuration.getFormat().format(durationSpin.getValue()) + ":00";
                    String date = new SimpleDateFormat("yyyy-MM-dd").format(datePicker.getDate()) + " " + hour;
                    classroom = MainController.getClassroomName(classroomsCB.getSelectedItem().toString());
                    if (actionAdd) {
                        session_id = MainController.addSession(date, hour, duration, classroom.getId(), subject.getId());
                        file_id = MainController.addFile(fileChooserSes.getSelectedFile().getName(), "", fileChooserSes.getSelectedFile().toString());
                        MainController.addFileSession(String.valueOf(session_id), String.valueOf(file_id), String.valueOf(false));
                    } else {
                        MainController.updateSession(filesession.getSessionId(), date, hour, duration, classroom.getId(), subject.getId());
                        MainController.updateFile(filesession.getFileId(), fileChooserSes.getSelectedFile().getName(), "", fileChooserSes.getSelectedFile().toString());
                    }
                    formaSessionIFrame.doDefaultCloseAction();
                    cleanFieldsSession();
                    updateSessionTable(true);
                } else {
                    logSessLabel.setText("Startime is empty.");
                }
            } else {
                logSessLabel.setText("Date is empty.");
            }
        } else {
            logSessLabel.setText("SharedFile is empty.");
        }
    }//GEN-LAST:event_savedSessBtnActionPerformed

    private void addSubjectBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addSubjectBtnActionPerformed
        formaSubjectIFrame.setTitle("Add subject");
        actionAdd = true;
        formaSubjectIFrame.setVisible(true);
    }//GEN-LAST:event_addSubjectBtnActionPerformed

    private void addSessionBtnBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addSessionBtnBtnActionPerformed
        formaSessionIFrame.setTitle("Add session");
        actionAdd = true;
        formaSessionIFrame.setVisible(true);
    }//GEN-LAST:event_addSessionBtnBtnActionPerformed

    private void subjectsCBActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_subjectsCBActionPerformed
        if(subjectsCB.getItemCount()>0){
        subject = MainController.getSubjectNameUserId(subjectsCB.getSelectedItem().toString(), user.getId());
        if (subject != null) {
            fileChooserSes.setCurrentDirectory(new File(subject.getSharedFolder()));
        }
        }
    }//GEN-LAST:event_subjectsCBActionPerformed

    private void sessionsTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_sessionsTableMouseClicked
        // TODO add your handling code here:
        int rowIndex = sessionsTable.getSelectedRow();
        int colIndex = sessionsTable.getSelectedColumn();
        System.out.println(rowIndex + "-" + colIndex);
        String session_id = sessions.get(rowIndex).getId();
        filesession = MainController.getFilesSession(session_id);

        if (colIndex == 4) { // edit session      
            FileD file = MainController.getFile(filesession.getFileId());
            Session session = MainController.getSession(filesession.getSessionId());
            DefaultTableModel tm = (DefaultTableModel) sessionsTable.getModel();
            String subjectName = String.valueOf(tm.getValueAt(rowIndex, 1));
            String classroomName = String.valueOf(tm.getValueAt(rowIndex, 2));
            try {
                actionAdd = false;
                setFieldsSession(file, session, subjectName, classroomName);
            } catch (ParseException ex) {
                Logger.getLogger(ExploradorGlobal1.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            if (colIndex == 5) { // delete session                
                int response = JOptionPane.showConfirmDialog(this, "Do you want to delete session?", "Accept",
                        JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                if (response == JOptionPane.YES_OPTION) {
                    MainController.deleteFilesSession(Integer.parseInt(filesession.getId()));
                    updateSessionTable(true);
                }
            }
        }

    }//GEN-LAST:event_sessionsTableMouseClicked

    private void subscriptionsTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_subscriptionsTableMouseClicked
        int rowIndex = subscriptionsTable.getSelectedRow();
        int colIndex = subscriptionsTable.getSelectedColumn();
        subscriptions.get(rowIndex).getId();
        if (colIndex == 2) { // delete session                
            int response = JOptionPane.showConfirmDialog(this, "Do you want to cancel subscription?", "Accept",
                    JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            if (response == JOptionPane.YES_OPTION) {
                MainController.deleteSubscription(Integer.parseInt(subscriptions.get(rowIndex).getId()));
                updateSubscriptionsTable(true);
            }

        }
    }//GEN-LAST:event_subscriptionsTableMouseClicked

    private void addSubscriptionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addSubscriptionActionPerformed
        formaSubscripIFrame.setVisible(true);
        subjectsSubscriptions = MainController.getSubjectsNotUser(user.getId());
        if (subjectsSubscriptions.size() == 0 || subjectsSubscriptions == null) {
            logSubsLabel.setText("There aren't subjects for subscription your.");
            savedSubscriptionBtn.setEnabled(false);
        } else {
            subjectsSubsCb.removeAllItems();
            subjectsSubscriptions.forEach((n) -> subjectsSubsCb.addItem(n.getName()));
            savedSubscriptionBtn.setEnabled(true);
        }
    }//GEN-LAST:event_addSubscriptionActionPerformed

    private void savedSubscriptionBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_savedSubscriptionBtnActionPerformed
        if (!passwordSubscField.getText().isEmpty()) {
            subject = MainController.existSubjectNamePass(subjectsSubsCb.getSelectedItem().toString(), passwordSubscField.getText());
            if (subject != null) {

                Subscription subscription = MainController.getSubscriptionSubjectUser(subjectsSubscriptions.get(subjectsSubsCb.getSelectedIndex()).getId(), user.getId());
                if (subscription == null) {
                    MainController.addSubscription(subject.getId(), user.getId(), String.valueOf(false));
                    updateSubscriptionsTable(true);
                    cleanFieldsSubscription();
                    formaSubscripIFrame.doDefaultCloseAction();
                } else {
                    logSubsLabel.setText("you are already subscribed to this subject.");
                }
            } else {
                logSubsLabel.setText("The password is incorrect.");
            }
        } else {
            logSubsLabel.setText("The password is empty.");
        }

    }//GEN-LAST:event_savedSubscriptionBtnActionPerformed

    private void subjectsTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_subjectsTableMouseClicked
        int rowIndex = subjectsTable.getSelectedRow();
        int colIndex = subjectsTable.getSelectedColumn();
        System.out.println(rowIndex + "-" + colIndex);
        subject_id = subjects.get(rowIndex).getId();
        Subject subject = MainController.getSubjectId(subject_id);
        if (colIndex == 3) { // edit session
            actionAdd = false;
            setFieldsSubject(subject);
        } else {
            if (colIndex == 4) { // delete session                
                int response = JOptionPane.showConfirmDialog(this, "Do you want to delete subject?", "Accept",
                        JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                if (response == JOptionPane.YES_OPTION) {
                    MainController.deleteSubject(Integer.parseInt(subject.getId()));
                    updateSubjectTable(true);
                }
            } else {
                if (colIndex == 5) {
                    ArrayList<Subscriptor> subscriptors;
                    subscriptors = MainController.getSubscriptorToSubject(user.getId(),subject_id);
                    updateSubscriptorsTable(subscriptors);
                    subscriptorsPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, subject.getName() + " (subscriptors)", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Font", 0, 14)));
                    
                }
            }

        }
    }//GEN-LAST:event_subjectsTableMouseClicked

    private void subjectsSubsCbActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_subjectsSubsCbActionPerformed
        if (subjectsSubsCb.getItemCount() > 0) {
            User speaker = MainController.getUserId(subjectsSubscriptions.get(subjectsSubsCb.getSelectedIndex()).getUserId());
            speakerTextF.setText(speaker.getName());
        }
    }//GEN-LAST:event_subjectsSubsCbActionPerformed

    private void userLabelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_userLabelMouseClicked
        try {
            nameUserTextF.setText(user.getName());
            usernameTextF.setEnabled(false);
            usernameTextF.setText(user.getUserName());
            passwordUserTextF.setText(user.getPassword());
            emailTextF.setText(user.getEmail());
            hostnameTextF.setText(user.getHostComputer());
            sharedfolderUserTextF.setText(user.getSharedFolder());
            if (MainController.getSubjectsUser(user.getId()).size() > 0) {
                sharedfolderUserTextF.enable(false);
            }
            accountIFrame.setSelected(true);
            accountIFrame.moveToFront();
            accountIFrame.setVisible(true);
        } catch (PropertyVetoException ex) {
            Logger.getLogger(ExploradorGlobal1.class.getName()).log(Level.SEVERE, null, ex);
        }


    }//GEN-LAST:event_userLabelMouseClicked

    private void modifyAccountBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_modifyAccountBtnActionPerformed
        // TODO add your handling code here:
        if (!nameUserTextF.getText().isEmpty()) {
            if (!passwordUserTextF.getText().isEmpty()) {
                if (!emailTextF.getText().isEmpty()) {
                    if (!hostnameTextF.getText().isEmpty()) {
                        if (!sharedfolderUserTextF.getText().isEmpty()) {
                            MainController.updateUser(user.getId(), nameUserTextF.getText(), passwordUserTextF.getText(), emailTextF.getText(), hostnameTextF.getText(), sharedfolderUserTextF.getText());
                            accountIFrame.doDefaultCloseAction();
                            userLabel.setText(nameUserTextF.getText());
                        } else {
                            logUserLabel.setText("Shared folder is empty.");
                        }
                    } else {
                        logUserLabel.setText("Hostname is empty.");
                    }
                } else {
                    logUserLabel.setText("Email is empty.");
                }
            } else {
                logUserLabel.setText("Password is empty.");
            }
        } else {
            logUserLabel.setText("Name is empty.");
        }

    }//GEN-LAST:event_modifyAccountBtnActionPerformed

    private void sharedFolderUserBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sharedFolderUserBtnActionPerformed
        // TODO add your handling code here:
        if (fileChooserUser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            sharedfolderUserTextF.setText(fileChooserUser.getSelectedFile().toString());
        }
    }//GEN-LAST:event_sharedFolderUserBtnActionPerformed

    private void subjectsCBInputMethodTextChanged(java.awt.event.InputMethodEvent evt) {//GEN-FIRST:event_subjectsCBInputMethodTextChanged
        // TODO add your handling code here:
        User speaker = MainController.getUserId(subjectsSubscriptions.get(subjectsSubsCb.getSelectedIndex()).getUserId());
        speakerTextF.setText(speaker.getName());
    }//GEN-LAST:event_subjectsCBInputMethodTextChanged


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton FilesSharedBtn;
    private javax.swing.JInternalFrame accountIFrame;
    private javax.swing.JButton addSessionBtn;
    private javax.swing.JMenuItem addSessionItem;
    private javax.swing.JButton addSubjectBtn;
    private javax.swing.JMenuItem addSubjectItem;
    private javax.swing.JButton addSubscription;
    private javax.swing.JList<String> archivos;
    private javax.swing.JComboBox<String> classroomsCB;
    private javax.swing.JPanel conSessPanel;
    private javax.swing.JPanel conSubPanel;
    private javax.swing.JPanel conSubsPanel;
    private javax.swing.JLabel dLabel;
    private com.toedter.calendar.JDateChooser datePicker;
    private javax.swing.JTextArea descriptionTextA;
    private javax.swing.JScrollPane docPanel;
    private javax.swing.JSpinner durationSpin;
    private javax.swing.JTextField emailTextF;
    private javax.swing.JPanel filesPanel;
    private javax.swing.JInternalFrame formaSessionIFrame;
    private javax.swing.JInternalFrame formaSubjectIFrame;
    private javax.swing.JInternalFrame formaSubscripIFrame;
    private javax.swing.JTextField hostnameTextF;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
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
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JLabel logSessLabel;
    private javax.swing.JLabel logSubjLabel;
    private javax.swing.JLabel logSubsLabel;
    private javax.swing.JLabel logUserLabel;
    private javax.swing.JButton modifyAccountBtn;
    private javax.swing.JTextField nameTextF;
    private javax.swing.JTextField nameUserTextF;
    private javax.swing.JPasswordField passwordSubscField;
    private javax.swing.JPasswordField passwordTextF;
    private javax.swing.JPasswordField passwordUserTextF;
    private javax.swing.JButton savedSessBtn;
    private javax.swing.JButton savedSubjBtn;
    private javax.swing.JButton savedSubscriptionBtn;
    private javax.swing.JMenu sessionMenu;
    private javax.swing.JPanel sessionPanel;
    private javax.swing.JTable sessionsTable;
    private javax.swing.JTextField sharedFileTextF;
    private javax.swing.JButton sharedFolderBtn;
    private javax.swing.JTextField sharedFolderTextF;
    private javax.swing.JButton sharedFolderUserBtn;
    private javax.swing.JTextField sharedfolderUserTextF;
    private javax.swing.JTextField speakerTextF;
    private javax.swing.JSpinner startTimeSpin;
    private javax.swing.JMenu subjectMenu;
    private javax.swing.JPanel subjectPanel;
    private javax.swing.JComboBox<String> subjectsCB;
    private javax.swing.JComboBox<String> subjectsSubsCb;
    private javax.swing.JTable subjectsTable;
    private javax.swing.JPanel subscriptionPanel;
    private javax.swing.JTable subscriptionsTable;
    private javax.swing.JPanel subscriptorsPanel;
    private javax.swing.JTable subscriptorsTable;
    private javax.swing.JLabel tLabel;
    private javax.swing.JTabbedPane tabPanel;
    private javax.swing.JMenuItem updateSessionItem;
    private javax.swing.JMenuItem updateSubjectItem;
    private javax.swing.JLabel userLabel;
    private javax.swing.JTextField usernameTextF;
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
