/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core.gui.admon;

import java.util.Base64;
import core.controller.MainController;
import core.data.ConfigModel;
import core.data.User;
import core.main.ExploradorGlobal1;
import core.utils.GenericUtils;
import java.beans.PropertyVetoException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.JFileChooser;
import java.io.File;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import javax.swing.ImageIcon;
import core.fingerprint.auth.FingerPrintAuth;
import core.fingerprint.auth.readFingerPrintEvent;
import java.awt.Image;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import javax.swing.event.*;
import java.awt.image.*;
import javax.imageio.*;
import java.io.File;
import java.lang.Exception ;
import java.awt.Color;
import java.awt.event.*;
import Biometrics.CFingerPrint;
import Biometrics.CFingerPrintGraphics;
import java.sql.SQLException;

/**
 *
 * @author Diana
 */
public class CreateAccount extends javax.swing.JFrame implements readFingerPrintEvent {

    /**
     * Creates new form createAccount
     */
    private JFileChooser fileChooser;
    private static ConfigModel config;
    private static CreateAccount instance;
    private BufferedImage fingerprintImage;
    private String fingerprintFile;
    private String fingerprintFile1;
    private String fingerprintFile2;
    private FingerPrintAuth fPrintAuth;
    private readFingerPrintEvent fpEvent;
    private Thread sensorThread;    
    private Boolean firstFingerPrint;
    private Boolean secondFingerPrint; 
    private Boolean createFingerTemplate;
    private String fingerprintChar1;
    private String fingerprintChar2;
    private boolean presentation;
    //uses our finger print libery
    private CFingerPrint m_finger1 = new CFingerPrint();
    private CFingerPrint m_finger2 = new CFingerPrint();
    private CFingerPrintGraphics m_fingergfx = new CFingerPrintGraphics();    
    private BufferedImage m_bimage1 = new BufferedImage(m_finger1.FP_IMAGE_WIDTH ,m_finger1.FP_IMAGE_HEIGHT,BufferedImage.TYPE_INT_RGB );
    private BufferedImage m_bimage2 = new BufferedImage(m_finger2.FP_IMAGE_WIDTH ,m_finger2.FP_IMAGE_HEIGHT,BufferedImage.TYPE_INT_RGB );
    private double finger1[] = new double[m_finger1.FP_TEMPLATE_MAX_SIZE];
    private double finger2[] = new double[m_finger2.FP_TEMPLATE_MAX_SIZE];   
    
    
    @Override
    public void readFingerprintSensorEvent(){
                
        String basePath = System.getProperty("user.dir");
        
        fingerprintFile = fPrintAuth.getCapturedValue().split(";")[0];
        
        if(firstFingerPrint){
            try{
                
                //rename fingerprint file name to allow a second finger image.
                File file1 = new File(fingerprintFile);                
                // File (or directory) with new name
                File file2 = new File(fingerprintFile1);                
                if(file2.exists())
                    file2.delete();                
                 // Rename file (or directory)
                boolean success = file1.renameTo(file2);
                if (!success) {
                    throw new java.io.IOException(fingerprintFile + " file could not be renamed. Verify the disk access permissions.");
                }
                 
                this.fingerPrintImageLabel1.setText("");
                
                fingerprintImage = ImageIO.read(new File(fingerprintFile1));
                Image scaledfingerprintImage = fingerprintImage.getScaledInstance(
                                                    this.fingerPrintImageLabel1.getWidth(),
                                                    this.fingerPrintImageLabel1.getHeight(), 
                                                    java.awt.Image.SCALE_DEFAULT);
                this.fingerPrintImageLabel1.setIcon(new ImageIcon(scaledfingerprintImage));            

                fingerprintChar1 = fPrintAuth.getCapturedValue().split(";")[1];                
                System.out.println("Output from fingerprint sensor: " + fingerprintChar1);
                fPrintAuth.terminate();
                
                try {
                    sensorThread.join(250);
                    
                } catch (InterruptedException ex) {
                    //Logger.getLogger(CreateAccount.class.getName()).log(Level.SEVERE, null, ex);
                }  
                                
                firstFingerPrint  = false;
                secondFingerPrint = true;               
                
                //Launch the second request for fingerprint.
                this.fingerPrintImageLabel2.setText("<html>Step (2):<br><br>Put your finger<br>on the sensor.</html>");
                this.fingerPrintImageLabel2.setIcon(null);            
                this.fingerPrintImageLabel2.repaint();
                System.out.println("Trying to listen the Fingerprint Sensor...");

                this.fPrintAuth = new FingerPrintAuth(this);

                this.fPrintAuth.setPyDownloadFingerPath(basePath+"/src/pythonCode/download_fingerprint.py");

                //this.fPrintAuth.execPySearchFinger();
                //this.fPrintAuth.execPyEnrollFinger();
                this.fPrintAuth.execPyDownloadFinger();         

                this.sensorThread = new Thread(this.fPrintAuth);
                this.sensorThread.start();
            
            }catch (IOException ex) {
                Logger.getLogger(CreateAccount.class.getName()).log(Level.SEVERE, null, ex);
            }

        }else if(secondFingerPrint){            
            
            try{
                //rename fingerprint file name to allow a second finger image.
                File file1 = new File(fingerprintFile);                
                // File (or directory) with new name
                File file2 = new File(fingerprintFile2);                
                if(file2.exists())
                    file2.delete();                
                 // Rename file (or directory)
                boolean success = file1.renameTo(file2);
                if (!success) {
                    throw new java.io.IOException(fingerprintFile + " file could not be renamed. Verify the disk access permissions.");
                }
                
                this.fingerPrintImageLabel2.setText("");
                fingerprintImage = ImageIO.read(new File(fingerprintFile2));
                Image scaledfingerprintImage = fingerprintImage.getScaledInstance(
                                                    this.fingerPrintImageLabel1.getWidth(),
                                                    this.fingerPrintImageLabel1.getHeight(), 
                                                    java.awt.Image.SCALE_DEFAULT);
                this.fingerPrintImageLabel2.setIcon(new ImageIcon(scaledfingerprintImage));            

                fingerprintChar2 = fPrintAuth.getCapturedValue().split(";")[1];  
                System.out.println("Output from fingerprint sensor: " + fingerprintChar1);
                
                //processFingerPrint();
                fPrintAuth.terminate();
                try {
                    sensorThread.join(250);
                } catch (InterruptedException ex) {
                    //Logger.getLogger(CreateAccount.class.getName()).log(Level.SEVERE, null, ex);
                }                                
                firstFingerPrint  = true;
                secondFingerPrint = false;               
            
            }catch (IOException ex) {
                Logger.getLogger(CreateAccount.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        }
        else if(createFingerTemplate){
            this.logLabel.setText("Wait, we are creting your finger template...");
            
        }
    }        
    
     private void processFingerPrint() throws IOException{
        m_bimage1=ImageIO.read(new File(fingerprintFile1)) ;
            
        //m_bimage2 = m_fingergfx.getGreyFingerPrintImage(m_bimage2); 
        
        System.out.println("Before BinerizeImage");
        m_bimage1 = m_fingergfx.BinerizeImage(m_bimage1, 250,180 );         
        //sm_panel1.setBufferedImage(m_bimage1);            
        
    
    
        //Send image for skeletinization
        m_finger1.setFingerPrintImage(m_bimage1) ;
        finger1=m_finger1.getFingerPrintTemplate();
         //See what skeletinized image looks like
        //m_bimage1 = m_finger1.getFingerPrintImageDetail();
        
            //m_panel1.setBufferedImage(m_bimage1);
        fingerprintChar1 = m_finger1.ConvertFingerPrintTemplateDoubleToString(finger1);
        System.out.println(fingerprintChar1);
        Image scaledfingerprintImage = m_bimage1.getScaledInstance(
                                                  this.fingerPrintImageLabel1.getWidth(),
                                                  this.fingerPrintImageLabel1.getHeight(), 
                                                  java.awt.Image.SCALE_DEFAULT);
              this.fingerPrintImageLabel1.setIcon(new ImageIcon(scaledfingerprintImage));            
              
        
        m_bimage2=ImageIO.read(new File(fingerprintFile2)) ;                       
        m_bimage2 = m_fingergfx.BinerizeImage(m_bimage2, 250,200 );                    
        //Send image for skeletinization
        m_finger2.setFingerPrintImage(m_bimage2) ;
        finger2=m_finger2.getFingerPrintTemplate();
         //See what skeletinized image looks like
        //m_bimage2 = m_finger2.getFingerPrintImageDetail();
        //m_panel1.setBufferedImage(m_bimage1);
        fingerprintChar2 = m_finger2.ConvertFingerPrintTemplateDoubleToString(finger2);
        System.out.println(fingerprintChar2);
        Image scaledfingerprintImage2 = m_bimage2.getScaledInstance(
                                                  this.fingerPrintImageLabel2.getWidth(),
                                                  this.fingerPrintImageLabel2.getHeight(), 
                                                  java.awt.Image.SCALE_DEFAULT);
             this.fingerPrintImageLabel2.setIcon(new ImageIcon(scaledfingerprintImage2));                   
          
        try
        {
            double match = m_finger1.Match(finger1, finger2, 65, false);
            this.logLabel.setText("Match %: " + Double.toString(match) );
            if (match <= 50)
                JOptionPane.showMessageDialog(this, "Match below threshold (< 50%), please try again.", "Fingerprint match", JOptionPane.PLAIN_MESSAGE);
        }
        catch (Exception ex)
        {
            JOptionPane.showMessageDialog (null,ex.getMessage() ,"Error Message",JOptionPane.PLAIN_MESSAGE);
        }     
    }
     
    
    public CreateAccount(String title) {
        initComponents();
        setLocationRelativeTo(null);
        fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        fileChooser.setAcceptAllFileFilterUsed(false);

        hostnameTextF.setText(GenericUtils.getHostname());
        jPanel1.setOpaque(true);
        jPanel1.setBorder(BorderFactory.createTitledBorder(title));
        
        firstFingerPrint = true;
        secondFingerPrint = false;
        
        this.fingerprintFile1= "fingerprint01.bmp";
        this.fingerprintFile2= "fingerprint02.bmp";
        

    }
    
    public static CreateAccount getInstance(String title){
        if(instance == null){
            instance = new CreateAccount(title);
        }
        return instance;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jSpinner1 = new javax.swing.JSpinner();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        nameTextF = new javax.swing.JTextField();
        usernameTextF = new javax.swing.JTextField();
        passwordTextF = new javax.swing.JPasswordField();
        emailTextF = new javax.swing.JTextField();
        hostnameTextF = new javax.swing.JTextField();
        createAccountBtn = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();
        sharedfolderTextF = new javax.swing.JTextField();
        sharedFolderBtn = new javax.swing.JButton();
        logLabel = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        takeFingerPrintBtn = new javax.swing.JButton();
             takeFingerPrintBtn = new javax.swing.JButton();
   backLk = new javax.swing.JLabel();
        fingerPrintImageLabel2 = new javax.swing.JLabel();
        fingerPrintImageLabel1 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("RaspClass");

        backLk.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        backLk.setForeground(new java.awt.Color(0, 51, 204));
        backLk.setText("<< back");
        backLk.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        backLk.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                backLkMouseClicked(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        jLabel1.setText("Name:");

        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        jLabel2.setText("Username:");

        jLabel3.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        jLabel3.setText("Password:");

        jLabel4.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        jLabel4.setText("Email:");

        jLabel5.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        jLabel5.setText("Hostname:");

        nameTextF.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N

        usernameTextF.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N

        passwordTextF.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N

        emailTextF.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N

        hostnameTextF.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N

        createAccountBtn.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        createAccountBtn.setText("Create");
        createAccountBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                createAccountBtnActionPerformed(evt);
            }
        });

        jLabel6.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        jLabel6.setText("Shared Folder:");

        sharedfolderTextF.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N

        sharedFolderBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/core/images/folder2.png"))); // NOI18N
        sharedFolderBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sharedFolderBtnActionPerformed(evt);
            }
        });

        logLabel.setForeground(new java.awt.Color(153, 0, 51));
        logLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        jLabel7.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        jLabel7.setText("Fingerprint #1:");

        takeFingerPrintBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/core/images/FingerPrintLogo.png"))); // NOI18N
        takeFingerPrintBtn.setToolTipText("Take fingerprint");
        takeFingerPrintBtn.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        takeFingerPrintBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                takeFingerPrintBtnActionPerformed(evt);
            }
        });

        backLk.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        backLk.setForeground(new java.awt.Color(0, 51, 204));
        backLk.setText("<< back");
        backLk.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        backLk.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                backLkMouseClicked(evt);
            }
        });

        fingerPrintImageLabel2.setForeground(java.awt.Color.red);
        fingerPrintImageLabel2.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        fingerPrintImageLabel2.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        fingerPrintImageLabel1.setForeground(java.awt.Color.red);
        fingerPrintImageLabel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        fingerPrintImageLabel1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        jLabel8.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        jLabel8.setText("Fingerprint #2:");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(logLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 303, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(createAccountBtn)
                .addGap(491, 491, 491))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(25, 25, 25)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addComponent(jLabel2)
                            .addComponent(jLabel4)
                            .addComponent(jLabel5)
                            .addComponent(jLabel3)
                            .addComponent(jLabel6))
                        .addGap(18, 18, 18))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(hostnameTextF)
                    .addComponent(emailTextF)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(backLk)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addComponent(fingerPrintImageLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel8)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(fingerPrintImageLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(takeFingerPrintBtn))
                            .addComponent(passwordTextF)
                            .addComponent(usernameTextF)
                            .addComponent(nameTextF)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(sharedfolderTextF)
                        .addGap(18, 18, 18)
                        .addComponent(sharedFolderBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(backLk)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(nameTextF, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel1))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(usernameTextF, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel2))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(passwordTextF, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel3))
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(31, 31, 31)
                                .addComponent(takeFingerPrintBtn)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(fingerPrintImageLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(fingerPrintImageLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jLabel8))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(jLabel7)
                                        .addGap(0, 0, Short.MAX_VALUE)))))
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(emailTextF, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel4))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(hostnameTextF, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel5))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel6)
                                    .addComponent(sharedfolderTextF, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addComponent(sharedFolderBtn, javax.swing.GroupLayout.Alignment.TRAILING))
                        .addGap(1, 1, 1)
                        .addComponent(logLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 83, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(createAccountBtn)
                        .addGap(25, 25, 25))))
        );

        fingerPrintImageLabel1.getAccessibleContext().setAccessibleName("fingerPrintImagLabel");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 623, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(32, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(34, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void backLkMouseClicked(java.awt.event.MouseEvent evt) {                                    
        // TODO add your handling code here:
        this.setVisible(false);
        try {
            new Login().setVisible(true);
        } catch (SQLException ex) {
            Logger.getLogger(CreateAccount.class.getName()).log(Level.SEVERE, null, ex);
        }
    }                                   

    private void takeFingerPrintBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_takeFingerPrintBtnActionPerformed
        String basePath = System.getProperty("user.dir");
        if (firstFingerPrint){  
            
            this.fingerPrintImageLabel1.setText("<html>Step (1):<br><br>Put your finger<br>on the sensor.</html>");
            this.fingerPrintImageLabel1.setIcon(null);            
            this.fingerPrintImageLabel1.repaint();
            System.out.println("Trying to listen the Fingerprint Sensor...");
           
            this.fPrintAuth = new FingerPrintAuth(this);
            
            this.fPrintAuth.setPyDownloadFingerPath(basePath+"/src/pythonCode/download_fingerprint.py");

            //this.fPrintAuth.execPySearchFinger();
            //this.fPrintAuth.execPyEnrollFinger();
            this.fPrintAuth.execPyDownloadFinger();         

            this.sensorThread = new Thread(this.fPrintAuth);
            this.sensorThread.start();
        }            
    }//GEN-LAST:event_takeFingerPrintBtnActionPerformed
      
    
    private void sharedFolderBtnActionPerformed(java.awt.event.ActionEvent evt) {                                                
        // TODO add your handling code here:
        if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            sharedfolderTextF.setText(fileChooser.getSelectedFile().toString());
            //MainController.updateSharedFolder(sharedFolderTxt.getText());

        } else {
            System.out.println("No Selection ");
        }
    }                                               

        
    private void createAccountBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_createAccountBtnActionPerformed
        // TODO add your handling code here:
                  
        System.out.print(this.fPrintAuth.convertImageFiletoBase64(fingerprintFile1));
               
        if (!nameTextF.getText().isEmpty()) {
            if (!usernameTextF.getText().isEmpty()) {
                if (!passwordTextF.getText().isEmpty()) {
                    if (!emailTextF.getText().isEmpty()) {
                        if (!hostnameTextF.getText().isEmpty()) {
                            if (!sharedfolderTextF.getText().isEmpty()) {
                                User user;
                                user = MainController.
                                existUser(usernameTextF.getText(), passwordTextF.getText());
                                if (user == null) {
                                    MainController.addUser(
                                        nameTextF.getText(),
                                        usernameTextF.getText(),
                                        passwordTextF.getText(),
                                        emailTextF.getText(),
                                        hostnameTextF.getText(),
                                        sharedfolderTextF.getText(),
                                        (fingerprintChar1.length()>0 ? fPrintAuth.convertImageFiletoBase64(fingerprintFile1) : ""),
                                        ( fingerprintChar2.length()>0 ? fPrintAuth.convertImageFiletoBase64(fingerprintFile1) : ""),
                                        (fingerprintFile1.length()>0 ? fPrintAuth.convertImageFiletoBase64(fingerprintFile1) : ""),
                                        (fingerprintFile2.length()>0 ? fPrintAuth.convertImageFiletoBase64(fingerprintFile2) : ""));
                                    this.setVisible(false);
                                    try {
                                        ExploradorGlobal1.getInstance(user,presentation).setVisible(true);
                                    } catch (PropertyVetoException ex) {
                                        Logger.getLogger(CreateAccount.class.getName()).log(Level.SEVERE, null, ex);
                                    }
                                } else {
                                    logLabel.setText("Username and password already exist.");
                                }

                            } else {
                                logLabel.setText("Shared folder is empty.");
                            }
                        } else {
                            logLabel.setText("Hostname is empty.");
                        }
                    } else {
                        logLabel.setText("Email is empty.");
                    }
                } else {
                    logLabel.setText("Password is empty.");
                }
            } else {
                logLabel.setText("Username is empty.");
            }
        } else {
            logLabel.setText("Name is empty.");
        }


    }//GEN-LAST:event_createAccountBtnActionPerformed

 //   private void backLkMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_backLkMouseClicked
 //       // TODO add your handling code here:
 //       this.setVisible(false);
 //       new Login().setVisible(true);
 //   }//GEN-LAST:event_backLkMouseClicked

 //   private void sharedFolderBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sharedFolderBtnActionPerformed
        // TODO add your handling code here:
 //       if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
 //           sharedfolderTextF.setText(fileChooser.getSelectedFile().toString());
            //MainController.updateSharedFolder(sharedFolderTxt.getText());         

  //      } else {
  //          System.out.println("No Selection ");
  //      }
  //  }//GEN-LAST:event_sharedFolderBtnActionPerformed

//    /**
//     * @param args the command line arguments
//     */
//    public static void main(String args[]) {
//        /* Set the Nimbus look and feel */
//        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
//        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
//         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
//         */
//        try {
//            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
//                if ("Nimbus".equals(info.getName())) {
//                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
//                    break;
//                }
//            }
//        } catch (ClassNotFoundException ex) {
//            java.util.logging.Logger.getLogger(CreateAccount.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (InstantiationException ex) {
//            java.util.logging.Logger.getLogger(CreateAccount.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (IllegalAccessException ex) {
//            java.util.logging.Logger.getLogger(CreateAccount.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
//            java.util.logging.Logger.getLogger(CreateAccount.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        }
//        //</editor-fold>
//        //</editor-fold>
//        //</editor-fold>
//        //</editor-fold>
//
//        /* Create and display the form */
//        java.awt.EventQueue.invokeLater(new Runnable() {
//            public void run() {
//                new CreateAccount().setVisible(true);
//            }
//        });
//    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel backLk;
    private javax.swing.JButton createAccountBtn;
    private javax.swing.JTextField emailTextF;
    private javax.swing.JLabel fingerPrintImageLabel1;
    private javax.swing.JLabel fingerPrintImageLabel2;
    private javax.swing.JTextField hostnameTextF;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JSpinner jSpinner1;
    private javax.swing.JLabel logLabel;
    private javax.swing.JTextField nameTextF;
    private javax.swing.JPasswordField passwordTextF;
    private javax.swing.JButton sharedFolderBtn;
    private javax.swing.JTextField sharedfolderTextF;
    private javax.swing.JButton takeFingerPrintBtn;
    private javax.swing.JTextField usernameTextF;
    // End of variables declaration//GEN-END:variables
}
