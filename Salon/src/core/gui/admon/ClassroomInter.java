/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core.gui.admon;

import core.controller.MainController;
import core.data.Classroom;
import core.data.MessageACL;
import core.data.Session;
import core.data.User;
import core.data.YellowPage;
import core.queue.QueueConfig;
import core.queue.QueueEventWriter;
import core.utils.FileTreeModel;
import core.utils.MyFile;
import core.webservices.FileUtils;
import java.beans.PropertyVetoException;
import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import core.webservices.OpenFileService;
import java.awt.ComponentOrientation;
import java.awt.Toolkit;
import java.util.Calendar;
import java.util.Date;
import javax.swing.Box;
import javax.swing.tree.TreePath;
import org.json.simple.JSONObject;

/**
 *
 * @author Diana
 */
public class ClassroomInter extends javax.swing.JFrame {

    private static ClassroomInter instance;
    private User user = new User();
    private String path;
    private Classroom classroom;
    private ArrayList<Session> sessionToday;

    /**
     * Creates new form Classroom
     */
    public ClassroomInter(User user, Classroom classroom) throws PropertyVetoException {
        File root;
        this.classroom = classroom;
        initComponents();
        jMenuBar1.add(Box.createHorizontalGlue());
        jMenuBar1.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        this.user = user;
        panelTab.setTitleAt(0, "Archivos");
        menuUser.setText(user == null ? "" : user.getName() + "  ");
        FileUtils.createPath(this.classroom.getRootFolder() + "/" + user.getUsername());
        path = this.classroom.getRootFolder() + "/" + user.getUsername();
        root = new File(this.classroom.getRootFolder() + "/" + user.getUsername());
        // Create a TreeModel object to represent our tree of files
        FileTreeModel model = new FileTreeModel(root);
        treeDirectory.setModel(model);
        toProjectSession();
    }

    private void setlogTextArea(String line) {
        logTextA.setText(logTextA.getText() + "\n" + line);
    }

    public void toProjectSession() {

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime firstDate = LocalDateTime.now().minusMinutes(10);
        LocalDateTime lastDate = LocalDateTime.now().plusHours(2);

        sessionToday = MainController.getSessionsDates(user.getId(), dtf.format(firstDate), dtf.format(lastDate));

        for (Session s : sessionToday) {
            System.out.println(path + "/" + s.getFile());
            
            File file = new File(path +"/"+ s.getCourseId().replace(" ", "")+"/" + s.getFile());
            if (file.exists()) {
                OpenFileService.openFile(path +"/"+ s.getCourseId().replace(" ", "")+"/" + s.getFile());
            } else {
                setlogTextArea("El siguiente archivo no se encontró:" +path +"/"+ s.getCourseId().replace(" ", "")+"/" + s.getFile());
            }
        }
        if (sessionToday.size() == 0) {
            setlogTextArea("No hay sesiones activas...");
        }
    }

    public static ClassroomInter getInstance(User user, Classroom classroom) throws PropertyVetoException {
        if (instance == null) {
            instance = new ClassroomInter(user, classroom);
        }
        return instance;
    }

    private void CheckModifyToFiles() {

        Date currentDate = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(currentDate);
        cal.add(Calendar.MINUTE, -90);
        Date alertDate = cal.getTime();

        for (Session s : sessionToday) {
//            System.out.println(path + "/" + s.getFile());
            File file = new File(path +"/"+ s.getCourseId().replace(" ", "")+"/" + s.getFile());
            Date modifiedDate = null;
            if (file.exists()) {
                modifiedDate = new Date(file.lastModified());

                if (modifiedDate != null && modifiedDate.before(alertDate)) {
//        return true;JSONObject cont = new JSONObject();
                    MessageACL msgACL = new MessageACL();
                    msgACL.setPerformative(msgACL.INFORM_IF);
                    msgACL.setSender(classroom.getLocation());
                    msgACL.setReceiver("multicast");
                    msgACL.setTypeSender(YellowPage.SALON);
                    msgACL.setOntology(msgACL.UPDATE);
                    
                    JSONObject cont = new JSONObject();
                    cont.put("idSession", s.getId());
                    cont.put("fileName", s.getFile());
                    cont.put("size", file.lastModified());
                    
                    msgACL.setContent(cont.toJSONString());                    
                    
                    new QueueEventWriter(QueueConfig.ADDRESS, Integer.parseInt(classroom.getPort())).writeToQueue(msgACL.toJSONString());
                }
            }
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

        panelLog = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        logTextA = new javax.swing.JTextArea();
        panelTab = new javax.swing.JTabbedPane();
        panelFiles = new javax.swing.JScrollPane();
        treeDirectory = new javax.swing.JTree();
        jMenuBar1 = new javax.swing.JMenuBar();
        menuUser = new javax.swing.JMenu();
        itemToProyect = new javax.swing.JMenuItem();
        itemExit = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("PreDisMaD-Clas");
        setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/core/images/class.png")));
        setPreferredSize(new java.awt.Dimension(883, 750));
        setSize(new java.awt.Dimension(883, 750));
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });
        getContentPane().setLayout(new java.awt.BorderLayout(0, 3));

        panelLog.setPreferredSize(new java.awt.Dimension(651, 125));

        logTextA.setColumns(20);
        logTextA.setFont(new java.awt.Font("Monospaced", 0, 14)); // NOI18N
        logTextA.setRows(5);
        logTextA.setFocusable(false);
        logTextA.setPreferredSize(new java.awt.Dimension(164, 100));
        jScrollPane1.setViewportView(logTextA);

        javax.swing.GroupLayout panelLogLayout = new javax.swing.GroupLayout(panelLog);
        panelLog.setLayout(panelLogLayout);
        panelLogLayout.setHorizontalGroup(
            panelLogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 651, Short.MAX_VALUE)
            .addGroup(panelLogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelLogLayout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 627, Short.MAX_VALUE)
                    .addContainerGap()))
        );
        panelLogLayout.setVerticalGroup(
            panelLogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 140, Short.MAX_VALUE)
            .addGroup(panelLogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelLogLayout.createSequentialGroup()
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(21, 21, 21)))
        );

        getContentPane().add(panelLog, java.awt.BorderLayout.SOUTH);

        panelTab.setTabPlacement(javax.swing.JTabbedPane.LEFT);
        panelTab.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        panelTab.setPreferredSize(new java.awt.Dimension(651, 600));

        treeDirectory.setFont(new java.awt.Font("Trebuchet MS", 0, 16)); // NOI18N
        treeDirectory.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                treeDirectoryMouseClicked(evt);
            }
        });
        panelFiles.setViewportView(treeDirectory);

        panelTab.addTab("tab2", panelFiles);

        getContentPane().add(panelTab, java.awt.BorderLayout.CENTER);

        jMenuBar1.setBorder(null);

        menuUser.setIcon(new javax.swing.ImageIcon(getClass().getResource("/core/images/user.png"))); // NOI18N
        menuUser.setText("Usuario");
        menuUser.setToolTipText("");
        menuUser.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        menuUser.setFont(new java.awt.Font("Segoe UI", 1, 17)); // NOI18N

        itemToProyect.setFont(new java.awt.Font("Trebuchet MS", 0, 17)); // NOI18N
        itemToProyect.setText("Proyectar");
        itemToProyect.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        itemToProyect.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                itemToProyectActionPerformed(evt);
            }
        });
        menuUser.add(itemToProyect);

        itemExit.setFont(new java.awt.Font("Trebuchet MS", 0, 17)); // NOI18N
        itemExit.setText("Salir");
        itemExit.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        itemExit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                itemExitActionPerformed(evt);
            }
        });
        menuUser.add(itemExit);

        jMenuBar1.add(menuUser);

        setJMenuBar(jMenuBar1);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void itemExitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_itemExitActionPerformed
        System.out.println("---2");
        CheckModifyToFiles();
        System.exit(0);
    }//GEN-LAST:event_itemExitActionPerformed

    private void itemToProyectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_itemToProyectActionPerformed
        toProjectSession();
    }//GEN-LAST:event_itemToProyectActionPerformed

    private void treeDirectoryMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_treeDirectoryMouseClicked
        MyFile mfile;
        String path;
        int selRow = treeDirectory.getRowForLocation(evt.getX(), evt.getY());
        TreePath selPath = treeDirectory.getPathForLocation(evt.getX(), evt.getY());
        if (selRow != -1) {
            if (evt.getClickCount() == 2) {
                Object[] paths = selPath.getPath();
                mfile = (MyFile) paths[paths.length - 1];
                path = mfile.getFile().getPath();
                File file = new File(path);
                if (file.isFile()) {
                    if (file.exists()) {
                        OpenFileService.openFile(path);
                    } else {
                        setlogTextArea("<html>El siguiente archivo no se encontró <br>" + path + "</html>");
                    }
                }
            }
        }

        Object node = treeDirectory.getLastSelectedPathComponent();// TODO add your handling code here:

    }//GEN-LAST:event_treeDirectoryMouseClicked

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        // TODO add your handling code here:
        CheckModifyToFiles();
        System.out.println("---1");
    }//GEN-LAST:event_formWindowClosing

    /**
     * @param args the command line arguments
     */

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuItem itemExit;
    private javax.swing.JMenuItem itemToProyect;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextArea logTextA;
    private javax.swing.JMenu menuUser;
    private javax.swing.JScrollPane panelFiles;
    private javax.swing.JPanel panelLog;
    private javax.swing.JTabbedPane panelTab;
    private javax.swing.JTree treeDirectory;
    // End of variables declaration//GEN-END:variables
}
