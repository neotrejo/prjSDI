/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template fileD, choose Tools | Templates
 * and open the template in the editor.
 */
package core.main;

import core.controller.MainController;
import core.crypt.CryptCipher;
import core.data.Classroom;
import core.data.FileSession;
import core.data.Session;
import core.data.Course;
import core.data.FileD;
import core.data.Subscription;
import core.data.Subscriptor;
import core.data.User;
import core.utils.ColorColumnRenderer;
import java.awt.Color;
import java.awt.ComponentOrientation;
import java.awt.Desktop;
import java.awt.event.KeyEvent;
import java.awt.Toolkit;
import java.beans.PropertyVetoException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.io.File;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.swing.Box;
import javax.swing.DefaultListModel;
import javax.swing.JFileChooser;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JSpinner.DateEditor;
import javax.swing.SpinnerDateModel;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.DateFormatter;
import org.json.simple.JSONObject;

/**
 *
 * @author SDI
 */
public class ExploradorGlobal extends javax.swing.JFrame {

    /**
     * Creates new form FileExplorer
     */
    private JList lista;
    private int currentIndex = 0;
    private static ExploradorGlobal instance;
    private User user;
    private Course course;
    private Classroom classroom;
    private FileSession filesession;
    private int session_id;
    private int file_id;
    private ArrayList<Classroom> classrooms;
    private ArrayList<Course> subjects;
    private ArrayList<Session> sessions;
    private ArrayList<Subscription> subscriptions;
    private ArrayList<Course> subjectsSubscriptions;
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
    private String subject_id; // for update course
     private int tabPanUser = 2; // number of user panel

    private ExploradorGlobal(User user) throws PropertyVetoException {
        initComponents();
        jMenuBar1.add(Box.createHorizontalGlue());
        jMenuBar1.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        this.user = user;
        userMenu.setText(user == null ? "" : user.getName());
        //----------------tabbedPanel------------//

        tabPanel.setTitleAt(0, "Cursos");
        tabPanel.setMnemonicAt(0, KeyEvent.VK_1);
        tabPanel.setTitleAt(1, "Sesiones");
        tabPanel.setMnemonicAt(1, KeyEvent.VK_2);
        tabPanel.remove(2);
        //tabPanel.setTitleAt(2, "Subscripciones");
        //tabPanel.setMnemonicAt(2, KeyEvent.VK_3);
        tabPanel.setSelectedIndex(0);
        tabPanel.setEnabledAt(tabPanUser, false);
        tabPanel.setTitleAt(tabPanUser, "Usuario");
        
         
        //-----------JDateChooser----------------//
        datePicker.getJCalendar().setMinSelectableDate(new Date());

        //---------filechooser----------------------//
        fileChooserSub = new JFileChooser(user.getSharedFolder());
        fileChooserSub.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        fileChooserSub.setAcceptAllFileFilterUsed(false);

        fileChooserSes = new JFileChooser();
        fileChooserSes.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fileChooserSes.addChoosableFileFilter(new FileNameExtensionFilter("PDF", "pdf"));
        fileChooserSes.addChoosableFileFilter(new FileNameExtensionFilter("MS Office", "docx", "pptx"));
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
        String[] headSubject = {"Nombre", "SharedFolder", "", "", ""};
        modelSubj = new DefaultTableModel();
        subjectsTable.setModel(modelSubj);
        for (String s : headSubject) {
            modelSubj.addColumn(s);
        }
        subjectsTable.getColumnModel().getColumn(2).setCellRenderer(new ColorColumnRenderer(Color.WHITE, Color.blue));
        subjectsTable.getColumnModel().getColumn(3).setCellRenderer(new ColorColumnRenderer(Color.WHITE, Color.blue));
        subjectsTable.getColumnModel().getColumn(4).setCellRenderer(new ColorColumnRenderer(Color.WHITE, Color.blue));
        updateSubjectTable(false);

        String[] headSession = {"Fecha", "Hora", "Curso", "Salón", "Archivo", "", ""};
        modelSess = new DefaultTableModel();
        sessionsTable.setModel(modelSess);
        for (String s : headSession) {
            modelSess.addColumn(s);
        }
        sessionsTable.getColumnModel().getColumn(5).setCellRenderer(new ColorColumnRenderer(Color.WHITE, Color.blue));
        sessionsTable.getColumnModel().getColumn(6).setCellRenderer(new ColorColumnRenderer(Color.WHITE, Color.blue));
        updateSessionTable(false);
        sourceFormat = new SimpleDateFormat("dd/MM/yyyy");

        String[] headSubscription = {"Curso", "Conferencista", ""};
        modelSubscription = new DefaultTableModel();
        subscriptionsTable.setModel(modelSubscription);
        for (String s : headSubscription) {
            modelSubscription.addColumn(s);
        }
        subscriptionsTable.getColumnModel().getColumn(2).setCellRenderer(new ColorColumnRenderer(Color.WHITE, Color.blue));
        updateSubscriptionsTable(false);

        String[] headSubscriptor = {"Nombre", "Correo"};
        modelSubscriptor = new DefaultTableModel();
        subscriptorsTable.setModel(modelSubscriptor);
        for (String s : headSubscriptor) {
            modelSubscriptor.addColumn(s);
        }
        //------------------------------//
        updateCbSession();
        //-----------------------------//
    }

    public static ExploradorGlobal getInstance(User user) throws PropertyVetoException {
        if (instance == null) {
            instance = new ExploradorGlobal(user);
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
            subjects.forEach((n) -> modelSubj.addRow(new Object[]{n.getName(), n.getSharedFolder(), "Editar", "Eliminar", "Subscriptores"}));
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
                logSessLabel.setText("No hay cursos.");
            }
        } else {
            logSessLabel.setText("No hay salones.");
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
                    modelSess.addRow(new Object[]{n.getDate(), n.getStartTime(), n.getCourseId(), n.getClassroomId(), n.getFile(), "Editar", "Eliminar"});
                } catch (ParseException ex) {
                    Logger.getLogger(ExploradorGlobal.class.getName()).log(Level.SEVERE, null, ex);
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
        if (subscriptions.size() != 0 || subscriptions != null) {
            subscriptions.forEach((n) -> {
                modelSubscription.addRow(new Object[]{n.getCourseName(), n.getProfessorName(), "Cancelar"});
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
        logSubjLabel.setText("");
    }

    private void cleanFieldsSession() {
        datePicker.setCalendar(null);
        sharedFileTextF.setText("");
        if (classroomsCB.getItemCount() > 0) {
            classroomsCB.setSelectedIndex(0);
        }
        if (subjectsCB.getItemCount() > 0) {
            subjectsCB.setSelectedIndex(0);
        }
        startTimeSpin.setValue(calendarD.getTime());
        durationSpin.setValue(calendarD.getTime());
        logSessLabel.setText("");
    }

    private void cleanFieldsSubscription() {
        if (subjectsSubsCb.getItemCount() > 0) {
            subjectsSubsCb.setSelectedIndex(0);
        }
        passwordSubscField.setText("");
        logSubsLabel.setText("");
    }

    private void setFieldsSession(FileD file, Session sesion, String subject, String classroom) throws ParseException {

        formaSessionIFrame.setTitle("Modificar sesión");
        Calendar calendarAux = Calendar.getInstance();

        String[] date = sesion.getDate().split("-");
        datePicker.setDate(new GregorianCalendar(Integer.parseInt(date[2]), Integer.parseInt(date[1]) - 1, Integer.parseInt(date[0])).getTime());
        fileChooserSes.setCurrentDirectory(new File(file.getPath()));
        fileChooserSes.setSelectedFile(new File(file.getPath()));
        sharedFileTextF.setText(file.getFileName());

        classroomsCB.setSelectedItem(classroom);
        subjectsCB.setSelectedItem(subject);

        String[] startTime = sesion.getStartTime().split(":");
        String[] duration = sesion.getDuration().split(":");

        calendarAux.set(Calendar.HOUR_OF_DAY, Integer.parseInt(startTime[0])); // 24 == 12 PM == 00:00:00
        calendarAux.set(Calendar.MINUTE, Integer.parseInt(startTime[1]));
        calendarAux.set(Calendar.SECOND, 0);
        startTimeSpin.setValue(calendarAux.getTime());

        calendarAux.set(Calendar.HOUR_OF_DAY, Integer.parseInt(duration[0])); // 24 == 12 PM == 00:00:00
        calendarAux.set(Calendar.MINUTE, Integer.parseInt(duration[1]));
        durationSpin.setValue(calendarAux.getTime());
        formaSessionIFrame.setVisible(true);
    }

    private void setFieldsSubject(Course course) {
        try {
            formaSubjectIFrame.setTitle("Modificar curso");
            nameTextF.setText(course.getName());
            passwordTextF.setText(CryptCipher.decrypt(course.getPasskey()));
            fileChooserSub.setCurrentDirectory(new File(course.getSharedFolder()));
            sharedFolderTextF.setText(course.getSharedFolder());
            //descriptionTextA.setText(course.getDescription());
            logSubjLabel.setText("");
            formaSubjectIFrame.setVisible(true);
        } catch (InvalidAlgorithmParameterException ex) {
            Logger.getLogger(ExploradorGlobal.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvalidKeyException ex) {
            Logger.getLogger(ExploradorGlobal.class.getName()).log(Level.SEVERE, null, ex);
        } catch (BadPaddingException ex) {
            Logger.getLogger(ExploradorGlobal.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalBlockSizeException ex) {
            Logger.getLogger(ExploradorGlobal.class.getName()).log(Level.SEVERE, null, ex);
        }
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
        tabPanel = new javax.swing.JTabbedPane();
        subjectPanel = new javax.swing.JPanel();
        conSubPanel = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        subjectsTable = new javax.swing.JTable();
        formaSubjectIFrame = new javax.swing.JInternalFrame();
        savedSubjBtn = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        nameTextF = new javax.swing.JTextField();
        sharedFolderTextF = new javax.swing.JTextField();
        passwordTextF = new javax.swing.JPasswordField();
        sharedFolderBtn = new javax.swing.JButton();
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
        accountPanel = new javax.swing.JPanel();
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
        logActiveSess = new javax.swing.JLabel();
        jMenuBar1 = new javax.swing.JMenuBar();
        userMenu = new javax.swing.JMenu();
        itemEditPerfil = new javax.swing.JMenuItem();
        itemClosed = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("PreDisMaD-Conf");
        setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/core/images/confer.png")) );
        setPreferredSize(new java.awt.Dimension(841, 890));
        setResizable(false);
        setSize(new java.awt.Dimension(841, 890));

        tabPanel.setTabLayoutPolicy(javax.swing.JTabbedPane.SCROLL_TAB_LAYOUT);
        tabPanel.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        tabPanel.setPreferredSize(new java.awt.Dimension(779, 765));

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
        formaSubjectIFrame.setTitle("Agregar curso");
        formaSubjectIFrame.setMaximumSize(new java.awt.Dimension(300, 345));
        formaSubjectIFrame.setPreferredSize(new java.awt.Dimension(300, 345));
        formaSubjectIFrame.setVisible(true);
        formaSubjectIFrame.getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        savedSubjBtn.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        savedSubjBtn.setText("Guardar");
        savedSubjBtn.setMaximumSize(new java.awt.Dimension(71, 27));
        savedSubjBtn.setMinimumSize(new java.awt.Dimension(71, 27));
        savedSubjBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                savedSubjBtnActionPerformed(evt);
            }
        });
        formaSubjectIFrame.getContentPane().add(savedSubjBtn, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 190, -1, -1));

        jLabel1.setFont(new java.awt.Font("Trebuchet MS", 0, 16)); // NOI18N
        jLabel1.setText("Nombre:");
        formaSubjectIFrame.getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 40, 128, -1));

        jLabel3.setFont(new java.awt.Font("Trebuchet MS", 0, 16)); // NOI18N
        jLabel3.setText("Contraseña:");
        formaSubjectIFrame.getContentPane().add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 90, -1, -1));

        jLabel4.setFont(new java.awt.Font("Trebuchet MS", 0, 16)); // NOI18N
        jLabel4.setText("Carpeta a compartir:");
        formaSubjectIFrame.getContentPane().add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 140, 160, -1));

        nameTextF.setFont(new java.awt.Font("Trebuchet MS", 0, 16)); // NOI18N
        nameTextF.setPreferredSize(new java.awt.Dimension(6, 30));
        formaSubjectIFrame.getContentPane().add(nameTextF, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 30, 300, 33));

        sharedFolderTextF.setFont(new java.awt.Font("Trebuchet MS", 0, 16)); // NOI18N
        formaSubjectIFrame.getContentPane().add(sharedFolderTextF, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 130, 260, 33));

        passwordTextF.setFont(new java.awt.Font("Trebuchet MS", 0, 16)); // NOI18N
        formaSubjectIFrame.getContentPane().add(passwordTextF, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 80, 300, 33));

        sharedFolderBtn.setFont(new java.awt.Font("Tahoma", 1, 15)); // NOI18N
        sharedFolderBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/core/images/folder2.png"))); // NOI18N
        sharedFolderBtn.setMargin(new java.awt.Insets(2, 7, 2, 7));
        sharedFolderBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sharedFolderBtnActionPerformed(evt);
            }
        });
        formaSubjectIFrame.getContentPane().add(sharedFolderBtn, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 130, 40, 33));

        logSubjLabel.setForeground(new java.awt.Color(153, 0, 0));
        logSubjLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        formaSubjectIFrame.getContentPane().add(logSubjLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 220, 411, 25));

        javax.swing.GroupLayout conSubPanelLayout = new javax.swing.GroupLayout(conSubPanel);
        conSubPanel.setLayout(conSubPanelLayout);
        conSubPanelLayout.setHorizontalGroup(
            conSubPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(conSubPanelLayout.createSequentialGroup()
                .addGap(68, 68, 68)
                .addComponent(formaSubjectIFrame, javax.swing.GroupLayout.PREFERRED_SIZE, 537, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(conSubPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        conSubPanelLayout.setVerticalGroup(
            conSubPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(conSubPanelLayout.createSequentialGroup()
                .addGap(40, 40, 40)
                .addComponent(formaSubjectIFrame, javax.swing.GroupLayout.PREFERRED_SIZE, 296, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(235, Short.MAX_VALUE))
            .addGroup(conSubPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(conSubPanelLayout.createSequentialGroup()
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 384, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );

        try {
            formaSubjectIFrame.setMaximum(true);
        } catch (java.beans.PropertyVetoException e1) {
            e1.printStackTrace();
        }

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
            .addGap(0, 0, Short.MAX_VALUE)
            .addGroup(subscriptorsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(subscriptorsPanelLayout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jScrollPane5)
                    .addContainerGap()))
        );
        subscriptorsPanelLayout.setVerticalGroup(
            subscriptorsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
            .addGroup(subscriptorsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(subscriptorsPanelLayout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jScrollPane5)
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
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(addSubjectBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, subjectPanelLayout.createSequentialGroup()
                        .addGroup(subjectPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(subscriptorsPanel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(conSubPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 750, Short.MAX_VALUE))
                        .addContainerGap())))
        );
        subjectPanelLayout.setVerticalGroup(
            subjectPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(subjectPanelLayout.createSequentialGroup()
                .addComponent(addSubjectBtn)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(conSubPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(subscriptorsPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        tabPanel.addTab("tab1", subjectPanel);

        sessionPanel.setBackground(java.awt.SystemColor.controlLtHighlight);

        conSessPanel.setBackground(java.awt.SystemColor.controlLtHighlight);
        conSessPanel.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N

        jScrollPane1.setBackground(java.awt.SystemColor.controlLtHighlight);

        sessionsTable.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
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
            .addGap(0, 0, Short.MAX_VALUE)
            .addGroup(conSessPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jScrollPane1))
        );
        conSessPanelLayout.setVerticalGroup(
            conSessPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
            .addGroup(conSessPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jScrollPane1))
        );

        addSessionBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/core/images/add2.png"))); // NOI18N
        addSessionBtn.setToolTipText("");
        addSessionBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addSessionBtnBtnActionPerformed(evt);
            }
        });

        formaSessionIFrame.setClosable(true);
        formaSessionIFrame.setTitle("Agregar sesión");
        formaSessionIFrame.setPreferredSize(new java.awt.Dimension(500, 340));
        formaSessionIFrame.setVisible(true);
        formaSessionIFrame.getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel5.setFont(new java.awt.Font("Trebuchet MS", 0, 16)); // NOI18N
        jLabel5.setText("Curso:");
        formaSessionIFrame.getContentPane().add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 30, -1, 30));

        jLabel6.setFont(new java.awt.Font("Trebuchet MS", 0, 16)); // NOI18N
        jLabel6.setText("Aula:");
        formaSessionIFrame.getContentPane().add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 80, -1, 30));

        jLabel7.setFont(new java.awt.Font("Trebuchet MS", 0, 16)); // NOI18N
        jLabel7.setText("Duración:");
        formaSessionIFrame.getContentPane().add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 140, 83, -1));

        jLabel8.setFont(new java.awt.Font("Trebuchet MS", 0, 16)); // NOI18N
        jLabel8.setText("Fecha:");
        formaSessionIFrame.getContentPane().add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 130, -1, 30));

        jLabel9.setFont(new java.awt.Font("Trebuchet MS", 0, 16)); // NOI18N
        jLabel9.setText("Archivo:");
        formaSessionIFrame.getContentPane().add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 180, -1, 30));

        subjectsCB.setFont(new java.awt.Font("Trebuchet MS", 0, 16)); // NOI18N
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
        formaSessionIFrame.getContentPane().add(subjectsCB, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 30, 346, 33));

        classroomsCB.setFont(new java.awt.Font("Trebuchet MS", 0, 16)); // NOI18N
        classroomsCB.setPreferredSize(new java.awt.Dimension(32, 26));
        formaSessionIFrame.getContentPane().add(classroomsCB, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 80, 126, 33));

        FilesSharedBtn.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        FilesSharedBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/core/images/folder2.png"))); // NOI18N
        FilesSharedBtn.setMargin(new java.awt.Insets(2, 7, 2, 7));
        FilesSharedBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                FilesSharedBtnActionPerformed(evt);
            }
        });
        formaSessionIFrame.getContentPane().add(FilesSharedBtn, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 180, -1, 33));

        durationSpin.setFont(new java.awt.Font("Trebuchet MS", 0, 16)); // NOI18N
        formaSessionIFrame.getContentPane().add(durationSpin, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 130, 86, 33));

        savedSessBtn.setBackground(javax.swing.UIManager.getDefaults().getColor("Button.shadow"));
        savedSessBtn.setFont(new java.awt.Font("Trebuchet MS", 0, 16)); // NOI18N
        savedSessBtn.setText("Guardar");
        savedSessBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                savedSessBtnActionPerformed(evt);
            }
        });
        formaSessionIFrame.getContentPane().add(savedSessBtn, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 240, -1, -1));

        logSessLabel.setForeground(new java.awt.Color(153, 0, 51));
        logSessLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        formaSessionIFrame.getContentPane().add(logSessLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 270, 318, 26));

        sharedFileTextF.setFont(new java.awt.Font("Trebuchet MS", 0, 16)); // NOI18N
        sharedFileTextF.setMinimumSize(new java.awt.Dimension(6, 26));
        formaSessionIFrame.getContentPane().add(sharedFileTextF, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 180, 310, 33));

        datePicker.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        datePicker.setIcon(new javax.swing.ImageIcon(getClass().getResource("/core/images/calendar.png")));
        datePicker.setPreferredSize(new java.awt.Dimension(88, 26));
        formaSessionIFrame.getContentPane().add(datePicker, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 130, 126, 33));

        jLabel11.setFont(new java.awt.Font("Trebuchet MS", 0, 16)); // NOI18N
        jLabel11.setText("Hora-inicio");
        formaSessionIFrame.getContentPane().add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 80, -1, -1));

        startTimeSpin.setFont(new java.awt.Font("Trebuchet MS", 0, 16)); // NOI18N
        formaSessionIFrame.getContentPane().add(startTimeSpin, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 80, 86, 33));

        javax.swing.GroupLayout sessionPanelLayout = new javax.swing.GroupLayout(sessionPanel);
        sessionPanel.setLayout(sessionPanelLayout);
        sessionPanelLayout.setHorizontalGroup(
            sessionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, sessionPanelLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(addSessionBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(sessionPanelLayout.createSequentialGroup()
                .addGap(100, 100, 100)
                .addComponent(formaSessionIFrame, javax.swing.GroupLayout.PREFERRED_SIZE, 500, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(sessionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(sessionPanelLayout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(conSessPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addContainerGap()))
        );
        sessionPanelLayout.setVerticalGroup(
            sessionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, sessionPanelLayout.createSequentialGroup()
                .addComponent(addSessionBtn)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(formaSessionIFrame, javax.swing.GroupLayout.PREFERRED_SIZE, 340, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(270, 270, 270))
            .addGroup(sessionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(sessionPanelLayout.createSequentialGroup()
                    .addGap(33, 33, 33)
                    .addComponent(conSessPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addContainerGap()))
        );

        tabPanel.addTab("tab3", sessionPanel);

        subscriptionPanel.setBackground(java.awt.SystemColor.controlLtHighlight);

        conSubsPanel.setBackground(java.awt.SystemColor.controlLtHighlight);
        conSubsPanel.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N

        jScrollPane4.setBackground(java.awt.SystemColor.controlLtHighlight);
        jScrollPane4.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N

        subscriptionsTable.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
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
            .addGap(0, 0, Short.MAX_VALUE)
            .addGroup(conSubsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jScrollPane4))
        );
        conSubsPanelLayout.setVerticalGroup(
            conSubsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
            .addGroup(conSubsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jScrollPane4))
        );

        addSubscription.setIcon(new javax.swing.ImageIcon(getClass().getResource("/core/images/add2.png"))); // NOI18N
        addSubscription.setToolTipText("");
        addSubscription.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addSubscriptionActionPerformed(evt);
            }
        });

        formaSubscripIFrame.setClosable(true);
        formaSubscripIFrame.setTitle("Agregar inscripciones");
        formaSubscripIFrame.setToolTipText("");
        formaSubscripIFrame.setVisible(true);
        formaSubscripIFrame.getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel10.setFont(new java.awt.Font("Trebuchet MS", 0, 16)); // NOI18N
        jLabel10.setText("Curso:");
        formaSubscripIFrame.getContentPane().add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 40, -1, 30));

        subjectsSubsCb.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        subjectsSubsCb.setPreferredSize(new java.awt.Dimension(32, 26));
        subjectsSubsCb.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                subjectsSubsCbActionPerformed(evt);
            }
        });
        formaSubscripIFrame.getContentPane().add(subjectsSubsCb, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 40, 311, 33));

        savedSubscriptionBtn.setBackground(javax.swing.UIManager.getDefaults().getColor("Button.shadow"));
        savedSubscriptionBtn.setFont(new java.awt.Font("Trebuchet MS", 0, 16)); // NOI18N
        savedSubscriptionBtn.setText("Guardar");
        savedSubscriptionBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                savedSubscriptionBtnActionPerformed(evt);
            }
        });
        formaSubscripIFrame.getContentPane().add(savedSubscriptionBtn, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 190, -1, -1));

        logSubsLabel.setForeground(new java.awt.Color(153, 0, 51));
        logSubsLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        formaSubscripIFrame.getContentPane().add(logSubsLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 230, 336, 20));

        jLabel17.setFont(new java.awt.Font("Trebuchet MS", 0, 16)); // NOI18N
        jLabel17.setText("Contraseña:");
        formaSubscripIFrame.getContentPane().add(jLabel17, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 140, -1, 30));

        passwordSubscField.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        passwordSubscField.setPreferredSize(new java.awt.Dimension(10, 22));
        formaSubscripIFrame.getContentPane().add(passwordSubscField, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 140, 311, 33));

        jLabel12.setFont(new java.awt.Font("Trebuchet MS", 0, 16)); // NOI18N
        jLabel12.setText("Conferencista:");
        jLabel12.setToolTipText("");
        formaSubscripIFrame.getContentPane().add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 90, -1, 40));

        speakerTextF.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        speakerTextF.setEnabled(false);
        speakerTextF.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                speakerTextFActionPerformed(evt);
            }
        });
        formaSubscripIFrame.getContentPane().add(speakerTextF, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 90, 311, 33));

        javax.swing.GroupLayout subscriptionPanelLayout = new javax.swing.GroupLayout(subscriptionPanel);
        subscriptionPanel.setLayout(subscriptionPanelLayout);
        subscriptionPanelLayout.setHorizontalGroup(
            subscriptionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, subscriptionPanelLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(addSubscription, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(subscriptionPanelLayout.createSequentialGroup()
                .addGap(134, 134, 134)
                .addComponent(formaSubscripIFrame, javax.swing.GroupLayout.PREFERRED_SIZE, 488, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(subscriptionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(subscriptionPanelLayout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(conSubsPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addContainerGap()))
        );
        subscriptionPanelLayout.setVerticalGroup(
            subscriptionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, subscriptionPanelLayout.createSequentialGroup()
                .addComponent(addSubscription)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(formaSubscripIFrame, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(212, 212, 212))
            .addGroup(subscriptionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(subscriptionPanelLayout.createSequentialGroup()
                    .addGap(34, 34, 34)
                    .addComponent(conSubsPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addContainerGap()))
        );

        tabPanel.addTab("tab3", subscriptionPanel);

        accountIFrame.setClosable(true);
        accountIFrame.setTitle("Modificar perfil");
        accountIFrame.setPreferredSize(new java.awt.Dimension(550, 460));
        accountIFrame.setVisible(true);
        accountIFrame.addInternalFrameListener(new javax.swing.event.InternalFrameListener() {
            public void internalFrameActivated(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameClosed(javax.swing.event.InternalFrameEvent evt) {
                accountIFrameInternalFrameClosed(evt);
            }
            public void internalFrameClosing(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameDeactivated(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameDeiconified(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameIconified(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameOpened(javax.swing.event.InternalFrameEvent evt) {
            }
        });

        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel13.setFont(new java.awt.Font("Trebuchet MS", 0, 16)); // NOI18N
        jLabel13.setText("Nombre:");
        jPanel1.add(jLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(25, 21, -1, 40));

        jLabel14.setFont(new java.awt.Font("Trebuchet MS", 0, 16)); // NOI18N
        jLabel14.setText("Usuario:");
        jPanel1.add(jLabel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 70, -1, 40));

        jLabel15.setFont(new java.awt.Font("Trebuchet MS", 0, 16)); // NOI18N
        jLabel15.setText("Contraseña:");
        jPanel1.add(jLabel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 120, -1, 40));

        jLabel16.setFont(new java.awt.Font("Trebuchet MS", 0, 16)); // NOI18N
        jLabel16.setText("Correo:");
        jPanel1.add(jLabel16, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 170, -1, 40));

        jLabel18.setFont(new java.awt.Font("Trebuchet MS", 0, 16)); // NOI18N
        jLabel18.setText("Hostname:");
        jPanel1.add(jLabel18, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 220, -1, 40));

        nameUserTextF.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jPanel1.add(nameUserTextF, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 20, 277, 33));

        usernameTextF.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jPanel1.add(usernameTextF, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 70, 277, 33));

        passwordUserTextF.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jPanel1.add(passwordUserTextF, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 120, 277, 33));

        emailTextF.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jPanel1.add(emailTextF, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 170, 277, 33));

        hostnameTextF.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jPanel1.add(hostnameTextF, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 220, 277, 33));

        modifyAccountBtn.setFont(new java.awt.Font("Trebuchet MS", 0, 16)); // NOI18N
        modifyAccountBtn.setText("Guardar");
        modifyAccountBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                modifyAccountBtnActionPerformed(evt);
            }
        });
        jPanel1.add(modifyAccountBtn, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 320, -1, -1));

        jLabel19.setFont(new java.awt.Font("Trebuchet MS", 0, 16)); // NOI18N
        jLabel19.setText("Folder a compartir:");
        jPanel1.add(jLabel19, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 280, -1, -1));

        sharedfolderUserTextF.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jPanel1.add(sharedfolderUserTextF, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 270, 240, 33));

        sharedFolderUserBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/core/images/folder2.png"))); // NOI18N
        sharedFolderUserBtn.setMargin(new java.awt.Insets(2, 7, 2, 7));
        sharedFolderUserBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sharedFolderUserBtnActionPerformed(evt);
            }
        });
        jPanel1.add(sharedFolderUserBtn, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 270, 34, 33));

        logUserLabel.setForeground(new java.awt.Color(153, 0, 51));
        logUserLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jPanel1.add(logUserLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 360, 322, 20));

        javax.swing.GroupLayout accountIFrameLayout = new javax.swing.GroupLayout(accountIFrame.getContentPane());
        accountIFrame.getContentPane().setLayout(accountIFrameLayout);
        accountIFrameLayout.setHorizontalGroup(
            accountIFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 527, Short.MAX_VALUE)
            .addGroup(accountIFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(accountIFrameLayout.createSequentialGroup()
                    .addGap(22, 22, 22)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 493, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(17, Short.MAX_VALUE)))
        );
        accountIFrameLayout.setVerticalGroup(
            accountIFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 421, Short.MAX_VALUE)
            .addGroup(accountIFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(accountIFrameLayout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 394, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(18, Short.MAX_VALUE)))
        );

        javax.swing.GroupLayout accountPanelLayout = new javax.swing.GroupLayout(accountPanel);
        accountPanel.setLayout(accountPanelLayout);
        accountPanelLayout.setHorizontalGroup(
            accountPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(accountPanelLayout.createSequentialGroup()
                .addGap(112, 112, 112)
                .addComponent(accountIFrame, javax.swing.GroupLayout.PREFERRED_SIZE, 540, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(122, Short.MAX_VALUE))
        );
        accountPanelLayout.setVerticalGroup(
            accountPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(accountPanelLayout.createSequentialGroup()
                .addGap(55, 55, 55)
                .addComponent(accountIFrame, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(217, Short.MAX_VALUE))
        );

        tabPanel.addTab("tab5", accountPanel);

        logActiveSess.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        logActiveSess.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        logActiveSess.setToolTipText("");
        logActiveSess.setFocusable(false);
        logActiveSess.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);

        jMenuBar1.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        userMenu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/core/images/user.png"))); // NOI18N
        userMenu.setText("Usuario");
        userMenu.setFont(new java.awt.Font("Segoe UI", 1, 17)); // NOI18N

        itemEditPerfil.setFont(new java.awt.Font("Trebuchet MS", 0, 17)); // NOI18N
        itemEditPerfil.setText("Editar perfil");
        itemEditPerfil.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        itemEditPerfil.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        itemEditPerfil.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                itemEditPerfilActionPerformed(evt);
            }
        });
        userMenu.add(itemEditPerfil);

        itemClosed.setFont(new java.awt.Font("Trebuchet MS", 0, 17)); // NOI18N
        itemClosed.setText("Salir");
        itemClosed.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        itemClosed.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        itemClosed.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                itemClosedActionPerformed(evt);
            }
        });
        userMenu.add(itemClosed);

        jMenuBar1.add(userMenu);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(604, 604, 604)
                        .addComponent(logActiveSess, javax.swing.GroupLayout.PREFERRED_SIZE, 197, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(22, 22, 22)
                        .addComponent(tabPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(38, 38, 38)
                        .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 2, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(tabPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(logActiveSess)
                .addGap(25, 25, 25))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void savedSubjBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_savedSubjBtnActionPerformed
        try {
            if (!nameTextF.getText().isEmpty()) {
                if (!passwordTextF.getText().isEmpty()) {
                    if (!sharedFolderTextF.getText().isEmpty()) {
                        Course course = null;
                        if (actionAdd) {
                            course = MainController.getSubjectNameUserId(nameTextF.getText(), user.getId());
                            if (course == null) {
                                MainController.addSubject(nameTextF.getText(), CryptCipher.encrypt(passwordTextF.getText()), "", sharedFolderTextF.getText(), this.user.getId());
                                updateSubjectTable(true);
                                updateCbSession();
                                cleanFieldsSubject();
                                formaSubjectIFrame.doDefaultCloseAction();
                            } else {
                                logSubjLabel.setText("Curso con el mismo nombre ya existe");
                            }
                        } else {
                            course = MainController.existSubjectNameNotId(nameTextF.getText(), subject_id, user.getId());
                            if (course == null) {

                                MainController.updateSubject(subject_id, nameTextF.getText(), CryptCipher.encrypt(passwordTextF.getText()), "", sharedFolderTextF.getText());
                                cleanFieldsSubject();
                                formaSubjectIFrame.doDefaultCloseAction();
                                updateSubjectTable(true);

                            } else {
                                logSubjLabel.setText("Curso con el mismo nombre ya existe");
                            }
                        }

                    } else {
                        logSubjLabel.setText("Folder a compartir esta vacio");
                    }
                } else {
                    logSubjLabel.setText("Contraseña esta vacia");
                }
            } else {
                logSubjLabel.setText("Nombre esta vacio");
            }
        } catch (NoSuchPaddingException ex) {
            Logger.getLogger(ExploradorGlobal.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(ExploradorGlobal.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvalidAlgorithmParameterException ex) {
            Logger.getLogger(ExploradorGlobal.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvalidKeyException ex) {
            Logger.getLogger(ExploradorGlobal.class.getName()).log(Level.SEVERE, null, ex);
        } catch (BadPaddingException ex) {
            Logger.getLogger(ExploradorGlobal.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalBlockSizeException ex) {
            Logger.getLogger(ExploradorGlobal.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_savedSubjBtnActionPerformed

    private void sharedFolderBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sharedFolderBtnActionPerformed
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
            if (!datePicker.getDate().toString().isEmpty()) {
                if (editorStart.getFormat().format(startTimeSpin.getValue()) != "00:00") {
                    String hour = editorStart.getFormat().format(startTimeSpin.getValue()) + ":00";
                    String duration = editorDuration.getFormat().format(durationSpin.getValue()) + ":00";
                    String date = new SimpleDateFormat("yyyy-MM-dd").format(datePicker.getDate()) + " " + hour;
                    classroom = MainController.getClassroomName(classroomsCB.getSelectedItem().toString());

//
                    JSONObject fileData = new JSONObject();

                    fileData.put("user_id", user.getId());
                    fileData.put("date", date);
                    fileData.put("startTime", hour);
                    fileData.put("subjectName", course.getName());
                    fileData.put("fileName", fileChooserSes.getSelectedFile().getName());
                    fileData.put("pathFile", fileChooserSes.getSelectedFile().toString());
                    fileData.put("userName", user.getUserName());
                    //

                    if (actionAdd) {
                        session_id = MainController.addSession(date, hour, duration, classroom.getId(), course.getId());
                        file_id = MainController.addFile(fileChooserSes.getSelectedFile().getName(), "", fileChooserSes.getSelectedFile().toString());

//                        //
//                        fileData.put("session_id", session_id + "");
//                        fileData.put("event", "add");
//                        new QueueEventWriter(QueueConfig.ADDRESS).writeToQueue(fileData.toJSONString());
//                        //
                        MainController.addFileSession(String.valueOf(session_id), String.valueOf(file_id), String.valueOf(false));
                    } else {
                        MainController.updateSession(filesession.getSessionId(), date, hour, duration, classroom.getId(), course.getId());
                        MainController.updateFile(filesession.getFileId(), fileChooserSes.getSelectedFile().getName(), "", fileChooserSes.getSelectedFile().toString());

//                        //
//                        fileData.put("session_id", filesession.getSessionId());
//                        fileData.put("event", "update");
//                        new QueueEventWriter(QueueConfig.ADDRESS).writeToQueue(fileData.toJSONString());
//                        //

                    }
                    formaSessionIFrame.doDefaultCloseAction();
                    cleanFieldsSession();
                    updateSessionTable(true);
                } else {
                    logSessLabel.setText("Hora-inicio esta vacio");
                }
            } else {
                logSessLabel.setText("Fecha esta vacia");
            }
        } else {
            logSessLabel.setText("Archivo asociado a la sesión esta vacio");
        }
    }//GEN-LAST:event_savedSessBtnActionPerformed

    private void addSubjectBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addSubjectBtnActionPerformed
        formaSubjectIFrame.setTitle("Agregar curso");
        actionAdd = true;
        cleanFieldsSubject();
        formaSubjectIFrame.setVisible(true);
    }//GEN-LAST:event_addSubjectBtnActionPerformed

    private void addSessionBtnBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addSessionBtnBtnActionPerformed
        formaSessionIFrame.setTitle("Agregar sesión");
        actionAdd = true;
        cleanFieldsSession();
        formaSessionIFrame.setVisible(true);
    }//GEN-LAST:event_addSessionBtnBtnActionPerformed

    private void subjectsCBActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_subjectsCBActionPerformed
        if (subjectsCB.getItemCount() > 0) {
            course = MainController.getSubjectNameUserId(subjectsCB.getSelectedItem().toString(), user.getId());
            if (course != null) {
                fileChooserSes.setCurrentDirectory(new File(course.getSharedFolder()));
            }
        }
    }//GEN-LAST:event_subjectsCBActionPerformed

    private void sessionsTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_sessionsTableMouseClicked
        int rowIndex = sessionsTable.getSelectedRow();
        int colIndex = sessionsTable.getSelectedColumn();
        System.out.println(rowIndex + "-" + colIndex);
        String session_id = sessions.get(rowIndex).getId();
        filesession = MainController.getFilesSession(session_id);
        FileD fileD = MainController.getFile(filesession.getFileId());
        switch (colIndex) {
            case 4: //OPEN FILE
                if (Desktop.isDesktopSupported()) {
                    try {
                        File myFile = new File(fileD.getPath());
                        Desktop.getDesktop().open(myFile);
                    } catch (IOException ex) {
                        System.out.println((ex));
                    }
                }
                break;
            case 5: //EDIT
                //FileD fileD = MainController.getFile(filesession.getFileId());
                Session session = MainController.getSession(filesession.getSessionId());
                DefaultTableModel tm = (DefaultTableModel) sessionsTable.getModel();
                String subjectName = String.valueOf(tm.getValueAt(rowIndex, 2));
                String classroomName = String.valueOf(tm.getValueAt(rowIndex, 3));
                actionAdd = false;
                 {
                    try {
                        setFieldsSession(fileD, session, subjectName, classroomName);
                    } catch (ParseException ex) {
                        Logger.getLogger(ExploradorGlobal.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                break;
            case 6: //DELETE
                int response = JOptionPane.showConfirmDialog(this, "¿Dese eliminar la sesión?", "Accept",
                        JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                if (response == JOptionPane.YES_OPTION) {
                    MainController.deleteFilesSession(Integer.parseInt(filesession.getId()));
                    updateSessionTable(true);

//                    //
//                    JSONObject fileData = new JSONObject();
//
//                    fileData.put("user_id", user.getId());
//                    fileData.put("date", "");
//                    fileData.put("startTime", "");
//                    fileData.put("subjectName", "");
//                    fileData.put("fileName", "");
//                    fileData.put("pathFile", "");
//                    fileData.put("session_id", filesession.getSessionId());
//                    fileData.put("event", "delete");
//                    new QueueEventWriter(QueueConfig.ADDRESS).writeToQueue(fileData.toJSONString());
//                    //
                }
                break;
            default:
        }
    }//GEN-LAST:event_sessionsTableMouseClicked

    private void subscriptionsTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_subscriptionsTableMouseClicked
        int rowIndex = subscriptionsTable.getSelectedRow();
        int colIndex = subscriptionsTable.getSelectedColumn();
        subscriptions.get(rowIndex).getId();
        if (colIndex == 2) { // delete session                
            int response = JOptionPane.showConfirmDialog(this, "¿Desea cancelar la suscripción?", "Accept",
                    JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            if (response == JOptionPane.YES_OPTION) {
                MainController.deleteSubscription(Integer.parseInt(subscriptions.get(rowIndex).getId()));
                updateSubscriptionsTable(true);
            }

        }
    }//GEN-LAST:event_subscriptionsTableMouseClicked

    private void addSubscriptionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addSubscriptionActionPerformed
        subjectsSubscriptions = MainController.getSubjectsNotUser(user.getId());
        if (subjectsSubscriptions.size() == 0 || subjectsSubscriptions == null) {
            logSubsLabel.setText("No tiene subscripciones");
            savedSubscriptionBtn.setEnabled(false);
        } else {
            subjectsSubsCb.removeAllItems();
            subjectsSubscriptions.forEach((n) -> subjectsSubsCb.addItem(n.getName()));
            savedSubscriptionBtn.setEnabled(true);
        }
        cleanFieldsSubscription();
        formaSubscripIFrame.setVisible(true);
    }//GEN-LAST:event_addSubscriptionActionPerformed

    private void savedSubscriptionBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_savedSubscriptionBtnActionPerformed
        if (!passwordSubscField.getText().isEmpty()) {
            try {
                course = MainController.existSubjectNamePass(subjectsSubsCb.getSelectedItem().toString(), CryptCipher.encrypt(passwordSubscField.getText()));
                if (course != null) {
                    Subscription subscription = MainController.getSubscriptionSubjectUser(subjectsSubscriptions.get(subjectsSubsCb.getSelectedIndex()).getId(), user.getId());
                    if (subscription == null) {
                        MainController.addSubscription(course.getId(), user.getId(), String.valueOf(false));
                        updateSubscriptionsTable(true);
                        cleanFieldsSubscription();
                        formaSubscripIFrame.doDefaultCloseAction();
                    } else {
                        logSubsLabel.setText("Ya se encuentra registrado en el curso");
                    }
                } else {
                    logSubsLabel.setText("La contraseña no corresponde");
                }
            } catch (NoSuchPaddingException ex) {
                Logger.getLogger(ExploradorGlobal.class.getName()).log(Level.SEVERE, null, ex);
            } catch (NoSuchAlgorithmException ex) {
                Logger.getLogger(ExploradorGlobal.class.getName()).log(Level.SEVERE, null, ex);
            } catch (InvalidAlgorithmParameterException ex) {
                Logger.getLogger(ExploradorGlobal.class.getName()).log(Level.SEVERE, null, ex);
            } catch (InvalidKeyException ex) {
                Logger.getLogger(ExploradorGlobal.class.getName()).log(Level.SEVERE, null, ex);
            } catch (BadPaddingException ex) {
                Logger.getLogger(ExploradorGlobal.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IllegalBlockSizeException ex) {
                Logger.getLogger(ExploradorGlobal.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            logSubsLabel.setText("Contraseña esta vacia");
        }

    }//GEN-LAST:event_savedSubscriptionBtnActionPerformed

    private void subjectsTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_subjectsTableMouseClicked
        int rowIndex = subjectsTable.getSelectedRow();
        int colIndex = subjectsTable.getSelectedColumn();
        System.out.println(rowIndex + "-" + colIndex);
        subject_id = subjects.get(rowIndex).getId();
        Course course = MainController.getSubjectId(subject_id);
        switch (colIndex) {
            case 2: //EDIT
                actionAdd = false;
                setFieldsSubject(course);
                break;
            case 3: // DELETE
                int response = JOptionPane.showConfirmDialog(this, "¿Desea eliminar el curso?", "Accept",
                        JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                if (response == JOptionPane.YES_OPTION) {
                    MainController.deleteSubject(Integer.parseInt(course.getId()));
                    updateSubjectTable(true);
                }
                break;
            case 4: // SUBSCRIPTORS
                ArrayList<Subscriptor> subscriptors;
                subscriptors = MainController.getSubscriptorToSubject(user.getId(), subject_id);
                updateSubscriptorsTable(subscriptors);
                subscriptorsPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, course.getName() + " (subscriptors)", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Font", 0, 14)));
                break;
            default:
        }
    }//GEN-LAST:event_subjectsTableMouseClicked

    private void subjectsSubsCbActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_subjectsSubsCbActionPerformed
        if (subjectsSubsCb.getItemCount() > 0) {
            User speaker = MainController.getUserId(subjectsSubscriptions.get(subjectsSubsCb.getSelectedIndex()).getUserId());
            speakerTextF.setText(speaker.getName());
        }
    }//GEN-LAST:event_subjectsSubsCbActionPerformed

    private void modifyAccountBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_modifyAccountBtnActionPerformed
        // TODO add your handling code here:
        if (!nameUserTextF.getText().isEmpty()) {
            if (!passwordUserTextF.getText().isEmpty()) {
                if (!emailTextF.getText().isEmpty()) {
                    if (!hostnameTextF.getText().isEmpty()) {
                        if (!sharedfolderUserTextF.getText().isEmpty()) {
                            try {
                                MainController.updateUser(user.getId(), nameUserTextF.getText(), CryptCipher.encrypt(passwordUserTextF.getText()), emailTextF.getText(), hostnameTextF.getText(), sharedfolderUserTextF.getText());
                                accountIFrame.doDefaultCloseAction();
                                tabPanel.setEnabledAt(tabPanUser, false);
                                tabPanel.setSelectedIndex(0);
                                userMenu.setText(nameUserTextF.getText());
                            } catch (NoSuchPaddingException ex) {
                                Logger.getLogger(ExploradorGlobal.class.getName()).log(Level.SEVERE, null, ex);
                            } catch (NoSuchAlgorithmException ex) {
                                Logger.getLogger(ExploradorGlobal.class.getName()).log(Level.SEVERE, null, ex);
                            } catch (InvalidAlgorithmParameterException ex) {
                                Logger.getLogger(ExploradorGlobal.class.getName()).log(Level.SEVERE, null, ex);
                            } catch (InvalidKeyException ex) {
                                Logger.getLogger(ExploradorGlobal.class.getName()).log(Level.SEVERE, null, ex);
                            } catch (BadPaddingException ex) {
                                Logger.getLogger(ExploradorGlobal.class.getName()).log(Level.SEVERE, null, ex);
                            } catch (IllegalBlockSizeException ex) {
                                Logger.getLogger(ExploradorGlobal.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        } else {
                            logUserLabel.setText("Folder a compartir esta vacio");
                        }
                    } else {
                        logUserLabel.setText("Hostname esta vacio");
                    }
                } else {
                    logUserLabel.setText("Correo esta vacio");
                }
            } else {
                logUserLabel.setText("Contraseña esta vacia");
            }
        } else {
            logUserLabel.setText("Nombre esta vacio");
        }

    }//GEN-LAST:event_modifyAccountBtnActionPerformed

    private void sharedFolderUserBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sharedFolderUserBtnActionPerformed
        // TODO add your handling code here:
        if (fileChooserUser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            sharedfolderUserTextF.setText(fileChooserUser.getSelectedFile().toString());
        }
    }//GEN-LAST:event_sharedFolderUserBtnActionPerformed

    private void subjectsCBInputMethodTextChanged(java.awt.event.InputMethodEvent evt) {//GEN-FIRST:event_subjectsCBInputMethodTextChanged
        if (subjectsSubsCb.getItemCount() > 0) {
            User speaker = MainController.getUserId(subjectsSubscriptions.get(subjectsSubsCb.getSelectedIndex()).getUserId());
            speakerTextF.setText(speaker.getName());
        }
    }//GEN-LAST:event_subjectsCBInputMethodTextChanged

    private void accountIFrameInternalFrameClosed(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_accountIFrameInternalFrameClosed
        tabPanel.setEnabledAt(tabPanUser, false);
        tabPanel.setSelectedIndex(0);
    }//GEN-LAST:event_accountIFrameInternalFrameClosed

    private void itemClosedActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_itemClosedActionPerformed
            System.exit(0);
    }//GEN-LAST:event_itemClosedActionPerformed

    private void itemEditPerfilActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_itemEditPerfilActionPerformed
        try {
            nameUserTextF.setText(user.getName());
            usernameTextF.setEnabled(false);
            usernameTextF.setText(user.getUserName());
            passwordUserTextF.setText(CryptCipher.decrypt(user.getPassword()));
            emailTextF.setText(user.getEmail());
            hostnameTextF.setText(user.getHostComputer());
            sharedfolderUserTextF.setText(user.getSharedFolder());
            if (MainController.getSubjectsUser(user.getId()).size() > 0) {
                sharedfolderUserTextF.enable(false);
                fileChooserUser.setControlButtonsAreShown(false);
            } else {
                sharedfolderUserTextF.enable(true);
                fileChooserUser.setControlButtonsAreShown(true);
            }
            tabPanel.setSelectedIndex(tabPanUser);
            tabPanel.setEnabledAt(tabPanUser, true);
            accountIFrame.setVisible(true);
        } catch (InvalidAlgorithmParameterException ex) {
            Logger.getLogger(ExploradorGlobal.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvalidKeyException ex) {
            Logger.getLogger(ExploradorGlobal.class.getName()).log(Level.SEVERE, null, ex);
        } catch (BadPaddingException ex) {
            Logger.getLogger(ExploradorGlobal.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalBlockSizeException ex) {
            Logger.getLogger(ExploradorGlobal.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_itemEditPerfilActionPerformed

    private void speakerTextFActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_speakerTextFActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_speakerTextFActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton FilesSharedBtn;
    private javax.swing.JInternalFrame accountIFrame;
    private javax.swing.JPanel accountPanel;
    private javax.swing.JButton addSessionBtn;
    private javax.swing.JButton addSubjectBtn;
    private javax.swing.JButton addSubscription;
    private javax.swing.JComboBox<String> classroomsCB;
    private javax.swing.JPanel conSessPanel;
    private javax.swing.JPanel conSubPanel;
    private javax.swing.JPanel conSubsPanel;
    private com.toedter.calendar.JDateChooser datePicker;
    private javax.swing.JSpinner durationSpin;
    private javax.swing.JTextField emailTextF;
    private javax.swing.JInternalFrame formaSessionIFrame;
    private javax.swing.JInternalFrame formaSubjectIFrame;
    private javax.swing.JInternalFrame formaSubscripIFrame;
    private javax.swing.JTextField hostnameTextF;
    private javax.swing.JMenuItem itemClosed;
    private javax.swing.JMenuItem itemEditPerfil;
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
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JLabel logActiveSess;
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
    private javax.swing.JPanel sessionPanel;
    private javax.swing.JTable sessionsTable;
    private javax.swing.JTextField sharedFileTextF;
    private javax.swing.JButton sharedFolderBtn;
    private javax.swing.JTextField sharedFolderTextF;
    private javax.swing.JButton sharedFolderUserBtn;
    private javax.swing.JTextField sharedfolderUserTextF;
    private javax.swing.JTextField speakerTextF;
    private javax.swing.JSpinner startTimeSpin;
    private javax.swing.JPanel subjectPanel;
    private javax.swing.JComboBox<String> subjectsCB;
    private javax.swing.JComboBox<String> subjectsSubsCb;
    private javax.swing.JTable subjectsTable;
    private javax.swing.JPanel subscriptionPanel;
    private javax.swing.JTable subscriptionsTable;
    private javax.swing.JPanel subscriptorsPanel;
    private javax.swing.JTable subscriptorsTable;
    private javax.swing.JTabbedPane tabPanel;
    private javax.swing.JMenu userMenu;
    private javax.swing.JTextField usernameTextF;
    // End of variables declaration//GEN-END:variables


}
