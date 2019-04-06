/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core.gui.admon;

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

/**
 *
 * @author Diana
 */
public class CreateAccount extends javax.swing.JFrame {

    /**
     * Creates new form createAccount
     */
    private JFileChooser fileChooser;
    private static ConfigModel config;
    private static CreateAccount instance;

    public CreateAccount(String title) {
        initComponents();
        setLocationRelativeTo(null);
        fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        fileChooser.setAcceptAllFileFilterUsed(false);

        hostnameTextF.setText(GenericUtils.getHostname());
        jPanel1.setOpaque(true);
        jPanel1.setBorder(BorderFactory.createTitledBorder(title));
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

        backLk = new javax.swing.JLabel();
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

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2)
                    .addComponent(jLabel4)
                    .addComponent(jLabel5)
                    .addComponent(jLabel3)
                    .addComponent(jLabel6))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                        .addComponent(sharedfolderTextF)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(sharedFolderBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(nameTextF, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(usernameTextF, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(emailTextF, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(hostnameTextF, javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(passwordTextF)
                        .addGap(2, 2, 2)))
                .addGap(31, 31, 31))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(77, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(logLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 370, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(56, 56, 56))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(createAccountBtn)
                        .addGap(207, 207, 207))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
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
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(emailTextF, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(hostnameTextF, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(sharedFolderBtn)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel6)
                        .addComponent(sharedfolderTextF, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addComponent(createAccountBtn)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(logLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 28, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(backLk)
                        .addGap(62, 62, 62))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(21, 21, 21))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(backLk)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(23, 23, 23))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void createAccountBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_createAccountBtnActionPerformed
        // TODO add your handling code here:
        if (!nameTextF.getText().isEmpty()) {
            if (!usernameTextF.getText().isEmpty()) {
                if (!passwordTextF.getText().isEmpty()) {
                    if (!emailTextF.getText().isEmpty()) {
                        if (!hostnameTextF.getText().isEmpty()) {
                            if (!sharedfolderTextF.getText().isEmpty()) {
                                User user = null;
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
                                            " ");                           
                                        this.setVisible(false);
                                        try {
                                            ExploradorGlobal1.getInstance(user, false).setVisible(true);
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

    private void backLkMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_backLkMouseClicked
        // TODO add your handling code here:
        this.setVisible(false);
        new Login().setVisible(true);
    }//GEN-LAST:event_backLkMouseClicked

    private void sharedFolderBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sharedFolderBtnActionPerformed
        // TODO add your handling code here:
        if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            sharedfolderTextF.setText(fileChooser.getSelectedFile().toString());
            //MainController.updateSharedFolder(sharedFolderTxt.getText());         

        } else {
            System.out.println("No Selection ");
        }
    }//GEN-LAST:event_sharedFolderBtnActionPerformed

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
    private javax.swing.JTextField hostnameTextF;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JLabel logLabel;
    private javax.swing.JTextField nameTextF;
    private javax.swing.JPasswordField passwordTextF;
    private javax.swing.JButton sharedFolderBtn;
    private javax.swing.JTextField sharedfolderTextF;
    private javax.swing.JTextField usernameTextF;
    // End of variables declaration//GEN-END:variables
}
