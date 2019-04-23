/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core.gui.admon;

import core.controller.MainController;
import core.data.Classroom;
import core.data.User;
import core.db.sqlite.SQLiteConnection;import core.fileserver.FileServer;
import core.utils.GenericUtils;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import java.awt.event.KeyEvent;
import java.beans.PropertyVetoException;
import core.fingerprint.auth.FingerPrintAuth;
import core.fingerprint.auth.readFingerPrintEvent;
import core.queue.EventQueueNotificationServer;
import core.queue.EventQueueServer;
import core.webservices.OpenFileService;
import core.webservices.QueueConfig;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.ListIterator;
import java.util.concurrent.TimeUnit;
import javax.swing.ImageIcon;

/**
 *
 * @author Diana
 */
public class Login extends javax.swing.JFrame implements readFingerPrintEvent {

    /**
     * Creates new form login
     */
    
    private static Login mainParent;
    private List<User> usersList;
    private BufferedImage fingerprintImage;
    private String fingerprintFile;
    private FingerPrintAuth fPrintAuth;
    private readFingerPrintEvent fpEvent;
    private Thread sensorThread;
    private String fingerprintLogIn;
    private String fingerprintImage1;
    private int    loginStage;
    private boolean waitComparison;
    private ListIterator<User> usersIterator;
    private static boolean presentation;
    
    User user;
    
    @Override    
    public void readFingerprintSensorEvent(){
                
        System.out.println("Output from fingerprint sensor: " + fPrintAuth.getCapturedValue());        
        
        //fingerprintFile = fPrintAuth.getCapturedValue().split(";")[0];      //Read out finger image filename.
        this.fingerprintLogIn =fPrintAuth.getCapturedValue().split(";")[0]; //Read out finger chracteristics.
        System.out.println("fingerprintLogIn: " + this.fingerprintLogIn);


        fPrintAuth.terminate();            
        try {
            sensorThread.join(250);
        } catch (InterruptedException ex) {}
        
        usersIterator = (ListIterator<User>) usersList.listIterator();
        try {                            
            waitComparison = SearchForLogin();
        } catch (SQLException ex) {
            Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InterruptedException ex) {
            Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
        }           
        
    }        
    
    public boolean SearchForLogin() throws SQLException, IOException, InterruptedException{
        BufferedReader in;
        String line;        
        String[] lines;
        boolean status = false;
        String hostNameLocal = GenericUtils.getHostname();          
        
        this.logText.setText("Searching for finger print matches in database...");
                
        while (usersIterator.hasNext()){
            user = (User)usersIterator.next();
            if((user.getFingerPrint1().length() >= 0)&&(user.getFingerPrint1().startsWith("["))){
                System.out.println("Executed SearchFinger for: " + user.getUserName());                
                in = this.fPrintAuth.runPySearchFinger(user.getFingerPrint1(), fingerprintLogIn);

                line = in.readLine();            
                while(line != null){                                       
                    System.out.println("Sensor output: " + line);
                    if (line.contains("Success")){

                    fPrintAuth.terminate();
                    try {
                        sensorThread.join(250);
                    } catch (InterruptedException ex) {
                        //Logger.getLogger(CreateAccount.class.getName()).log(Level.SEVERE, null, ex);
                    } 
                            
                        lines = line.split(";");                                                
                        status = true;  // Wait for next fingerprint comparison from sensor.


                        System.out.println("Succesful loging: User: " + user.getName() + "Match score: " + lines[1]);                    
                        this.logText.setText("User: " + user.getName() + " Match score: " + lines[1]);

                        this.usernameTextF.setText(user.getName());
                        this.passwordTextF.setText(user.getPassword());


                        //Show fingerprint image of identified user.

                        //Copy encoded image to clipboard.
                        //String ctc = encodedimage;
                        //StringSelection stringSelection = new StringSelection(ctc);
                        //Clipboard clpbrd = Toolkit.getDefaultToolkit().getSystemClipboard();
                        //clpbrd.setContents(stringSelection, null);

                        try{
                            BufferedImage fingerImage = fPrintAuth.convertToImage(user.getFingerPrintImage1());
                            Image scaledfingerprintImage = fingerImage.getScaledInstance(
                                                                this.fingerPrintImageLabel.getWidth(),
                                                                this.fingerPrintImageLabel.getHeight(), 
                                                                java.awt.Image.SCALE_DEFAULT);
                            this.fingerPrintImageLabel.setText("");
                            this.fingerPrintImageLabel.setIcon(new ImageIcon(scaledfingerprintImage));                                    
                            this.fingerPrintImageLabel.repaint();
                        }catch(Exception ex){
                            Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
                        }

                        //Wait for a while to show the name of the identifies user.
                        try {
                            TimeUnit.SECONDS.sleep(4);
                        } catch (InterruptedException ex) {
                            Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
                        }

                        this.setVisible(false);
                        try {
                            Classroom classroom = MainController.existClassroomHostName(hostNameLocal);                   
                            presentation = hostNameLocal!= user.getHostComputer() && classroom!=null ? true:false;
                            ClassroomInter.getInstance(user,classroom ,presentation).setVisible(true);
                        } catch (PropertyVetoException ex) {
                            Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
                        }                        

                        break;
                    }  

                    line = in.readLine();
                }            
            }
            if(status){
                break;
            }
                    
        }            
          
        if(!status){ //Inform the user was not found.
        
            System.out.println("Invalid fingerprint. No user match.");
            this.logText.setText("Invalid fingerprint. No user match.");
            
            this.fPrintAuth.execPyReadFinger();
            this.sensorThread = new Thread(this.fPrintAuth);
            this.sensorThread.start();
        }
        return status;
    }
    
    public Login() throws SQLException {
        String basePath = System.getProperty("user.dir");
        loginStage = 0; // Looking for sensor reading finger image.
        
        readConfig();
        
        try {
            initComponents();
            setLocationRelativeTo(null);
            jPanel1.setBorder(BorderFactory.createTitledBorder("Log in"));      
            mainParent = this;
            SQLiteConnection.getInstance().conectar();
           
            //Retive all users from the database.
            usersList = (List<User>) MainController.getAllUsers();       
            
            this.fingerPrintImageLabel.setText("<html>Put your finger<br>on the sensor.</html>");
            this.fingerPrintImageLabel.setIcon(null);            
            this.fingerPrintImageLabel.repaint();
            System.out.println("Trying to listen the Fingerprint Sensor...");
           
            this.fPrintAuth = new FingerPrintAuth(this);
            this.fPrintAuth.setPyReadFingerPath(basePath+"/src/pythonCode/read_fingerprint.py");
            this.fPrintAuth.setPyDownloadFingerPath(basePath+"/src/pythonCode/download_fingerprint.py");
            this.fPrintAuth.setPySearchFingerPath(basePath+"/src/pythonCode/search_fingerprint.py");
            
            this.fPrintAuth.execPyReadFinger();
            //this.fPrintAuth.execPyDownloadFinger();
            
            this.sensorThread = new Thread(this.fPrintAuth);
            this.sensorThread.start();
                      
            
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void readFingerPrintSensor(){
        String basePath = System.getProperty("user.dir");
        loginStage = 0; // Looking for sensor reading finger image.
        this.fPrintAuth = new FingerPrintAuth(this);
        this.fPrintAuth.setPyReadFingerPath(basePath+"/src/pythonCode/read_fingerprint.py");
        this.fPrintAuth.setPyDownloadFingerPath(basePath+"/src/pythonCode/download_fingerprint.py");
        this.fPrintAuth.setPySearchFingerPath(basePath+"/src/pythonCode/search_fingerprint.py");

        this.fPrintAuth.execPyReadFinger();
        //this.fPrintAuth.execPyDownloadFinger();

        this.sensorThread = new Thread(this.fPrintAuth);
        this.sensorThread.start();        
    }

  /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        createAccountLk = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        usernameTextF = new javax.swing.JTextField();
        passwordTextF = new javax.swing.JPasswordField();
        logInBtn = new javax.swing.JButton();
        fingerPrintImageLabel = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        logText = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("RaspClass");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });
        addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                formKeyPressed(evt);
            }
        });

        createAccountLk.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        createAccountLk.setForeground(new java.awt.Color(0, 51, 153));
        createAccountLk.setText("Create account");
        createAccountLk.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        createAccountLk.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                createAccountLkMouseClicked(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/core/images/user.png"))); // NOI18N
        jLabel1.setText("Username:");

        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/core/images/password.png"))); // NOI18N
        jLabel2.setText("Password:");

        usernameTextF.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N

        passwordTextF.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N

        logInBtn.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        logInBtn.setMnemonic(KeyEvent.VK_ENTER);
        logInBtn.setText("Log in");
        logInBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                logInBtnActionPerformed(evt);
            }
        });

        fingerPrintImageLabel.setForeground(java.awt.Color.red);
        fingerPrintImageLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        fingerPrintImageLabel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));
        fingerPrintImageLabel.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        jLabel3.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/core/images/fingerP.png"))); // NOI18N
        jLabel3.setText("Fingerprint:");

        logText.setForeground(new java.awt.Color(153, 0, 51));
        logText.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        logText.setMinimumSize(new java.awt.Dimension(100, 20));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(55, 55, 55)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(fingerPrintImageLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel3)
                            .addComponent(jLabel2)
                            .addComponent(jLabel1))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(passwordTextF, javax.swing.GroupLayout.PREFERRED_SIZE, 169, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(usernameTextF, javax.swing.GroupLayout.PREFERRED_SIZE, 167, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(39, Short.MAX_VALUE))
            .addComponent(logText, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(logInBtn)
                .addGap(62, 62, 62))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(usernameTextF, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(20, 20, 20)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(passwordTextF, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(fingerPrintImageLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(logInBtn)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(logText, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(createAccountLk)
                        .addGap(14, 14, 14)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(createAccountLk)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void createAccountLkMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_createAccountLkMouseClicked
        // TODO add your handling code here:
        
        //Termiante thread waiting on fingerprint sensor reading.
        fPrintAuth.terminate();
        try {
            sensorThread.join(250);
        } catch (InterruptedException ex) {
            Logger.getLogger(CreateAccount.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        this.setVisible(false);
        CreateAccount.getInstance("Create account", this).setVisible(true); // Main Form to show after the Login Form..
    }//GEN-LAST:event_createAccountLkMouseClicked

    private void logInBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_logInBtnActionPerformed
        // TODO add your handling code here:
         String hostNameLocal = GenericUtils.getHostname();
        if (!usernameTextF.getText().isEmpty() && passwordTextF.getPassword().length!=0) {
            User user=null;
            user = MainController.existUser(usernameTextF.getText(), passwordTextF.getText());
            if (user != null) {
                this.setVisible(false);
                
                //Termiante thread waiting on fingerprint sensor reading.
                fPrintAuth.terminate();
                try {
                    sensorThread.join(250);
                } catch (InterruptedException ex) {
                    //Logger.getLogger(CreateAccount.class.getName()).log(Level.SEVERE, null, ex);
                }
                //Termiante thread waiting on fingerprint sensor reading.
                

                try {
                    Classroom classroom = MainController.existClassroomHostName(hostNameLocal);                   
                    presentation = hostNameLocal!= user.getHostComputer() && classroom!=null ? true:false;
                    ClassroomInter.getInstance(user,classroom,presentation).setVisible(true);
                } catch (PropertyVetoException ex) {
                    Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else {
                logText.setText("Username not exist...");
            }
        } else {
            logText.setText("Username or password is empty...");
        }

    }//GEN-LAST:event_logInBtnActionPerformed

    private void formKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_formKeyPressed
        if (evt.getKeyChar() == KeyEvent.VK_ENTER) {
                logInBtn.doClick();
            }
    }//GEN-LAST:event_formKeyPressed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        System.out.println("Main window is closing...");
        
        if(fPrintAuth !=null){
            this.fPrintAuth.terminate();
            try {
                sensorThread.join(250);
            } catch (InterruptedException ex) {
                //Logger.getLogger(CreateAccount.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
    }//GEN-LAST:event_formWindowClosing

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
                if ("Metal".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Login.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Login.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Login.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Login.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
//        EventQueueServer server = new EventQueueServer();
//        EventQueueNotificationServer nserver = new EventQueueNotificationServer();
//        FileServer fileServer = new FileServer();
//        
//        server.start();
//        nserver.start();
//        fileServer.start();
        
        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    new Login().setVisible(true);                    
                } catch (SQLException ex) {
                    Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }
    
    public static String[] readConfig(){
        try {
            BufferedReader br = new BufferedReader(new FileReader("ConfigRaspy.txt"));
            
            String path = br.readLine().split("=")[1].trim();
            String host = br.readLine().split("=")[1].trim();
            String pptApp = br.readLine().split("=")[1].trim();
            String pdfApp = br.readLine().split("=")[1].trim();
            
            OpenFileService.pdfApp = pdfApp;
            OpenFileService.pptApp = pptApp;
            
            System.out.println(path);
            System.out.println(host);
            
            return new String[]{path,host};
            
        } catch (Exception ex) {
            Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return null;
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel createAccountLk;
    private javax.swing.JLabel fingerPrintImageLabel;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JButton logInBtn;
    private javax.swing.JLabel logText;
    private javax.swing.JPasswordField passwordTextF;
    private javax.swing.JTextField usernameTextF;
    // End of variables declaration//GEN-END:variables
}
