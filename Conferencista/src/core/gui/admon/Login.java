/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core.gui.admon;

import core.controller.MainController;
import core.crypt.CryptCipher;
import core.data.User;
import core.db.rqlite.RQLiteConnection;
import core.main.ExploradorGlobal;
import java.beans.PropertyVetoException;

import java.util.logging.Level;
import java.util.logging.Logger;

import core.queue.QueueConfig;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.net.Socket;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.KeyStroke;

/**
 *
 * @author Diana
 */
public class Login extends javax.swing.JFrame {

    /**
     * Creates new form alta
     */
    private User user;

    public Login() {
        try {
            initComponents();
            setLocationRelativeTo(null);
            Mnemonico();
            RQLiteConnection.getInstance().conectar();
            //ToVerifyService();
            //String basePath = System.getProperty("user.dir");
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void ToVerifyService(){
        try {
            Socket socket = new Socket(QueueConfig.ADDRESS, QueueConfig.SERVER_PORT);
            socket.close();
        } catch (Exception ex) {
            System.out.println(ex.toString());
            CompileAndRun();
        }
    }
    
    public void CompileAndRun() {
        try {
            String dir = System.getProperty("user.dir") + "\\"+"FileServiceClient.jar";
            Runtime.getRuntime().exec("java -jar "+dir);            
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void Mnemonico() {
        // create an Action doing what you want
        Action action = new AbstractAction("Entrar") {
            @Override
            public void actionPerformed(ActionEvent e) {
                onClickLogin();
            }
        };
// configure the Action with the accelerator (aka: short cut)
        action.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0));
// create a button, configured with the Action
        loginBtn.setAction(action);
// manually register the accelerator in the button's component input map
        loginBtn.getActionMap().put("myAction", action);
        loginBtn.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put((KeyStroke) action.getValue(Action.ACCELERATOR_KEY), "myAction");
    }
    
    private void onClickLogin(){
         String passCryp = "";
        if (!usuarioTF.getText().isEmpty() && contraseñaPF.getPassword().length != 0) {
            try {
                passCryp = CryptCipher.encrypt(contraseñaPF.getText());
            } catch (Exception ex) {
                Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
            }
            user = MainController.existUser(usuarioTF.getText(), passCryp);
            if (user != null) {
                this.setVisible(false);

                try {
                    ExploradorGlobal.getInstance(user).setVisible(true);
                } catch (PropertyVetoException ex) {
                    Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else {
                logText.setText("El usuario no existe...");
            }
        } else {
            logText.setText("Usuario o contraseña estan vacios...");
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

        topJP = new javax.swing.JPanel();
        altaCtaTF = new javax.swing.JLabel();
        centerJP = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        usuarioTF = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        contraseñaPF = new javax.swing.JPasswordField();
        bottomJP = new javax.swing.JPanel();
        loginBtn = new javax.swing.JButton();
        logText = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("PreDisMaD-Conf");
        setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/core/images/confer.png")) );
        setMinimumSize(new java.awt.Dimension(517, 365));
        setPreferredSize(new java.awt.Dimension(517, 365));
        setSize(new java.awt.Dimension(517, 365));

        topJP.setPreferredSize(new java.awt.Dimension(517, 50));

        altaCtaTF.setFont(new java.awt.Font("Yu Gothic Medium", 1, 14)); // NOI18N
        altaCtaTF.setForeground(new java.awt.Color(51, 51, 255));
        altaCtaTF.setText("Registrarse...");
        altaCtaTF.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        altaCtaTF.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                altaCtaTFMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout topJPLayout = new javax.swing.GroupLayout(topJP);
        topJP.setLayout(topJPLayout);
        topJPLayout.setHorizontalGroup(
            topJPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, topJPLayout.createSequentialGroup()
                .addContainerGap(385, Short.MAX_VALUE)
                .addComponent(altaCtaTF)
                .addGap(32, 32, 32))
        );
        topJPLayout.setVerticalGroup(
            topJPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, topJPLayout.createSequentialGroup()
                .addContainerGap(14, Short.MAX_VALUE)
                .addComponent(altaCtaTF)
                .addContainerGap())
        );

        getContentPane().add(topJP, java.awt.BorderLayout.PAGE_START);

        centerJP.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Login", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Bahnschrift", 0, 15), new java.awt.Color(0, 102, 102))); // NOI18N
        centerJP.setMinimumSize(new java.awt.Dimension(457, 221));
        centerJP.setPreferredSize(new java.awt.Dimension(457, 221));
        centerJP.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setFont(new java.awt.Font("Trebuchet MS", 0, 17)); // NOI18N
        jLabel1.setText("Usuario:");
        centerJP.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(59, 46, 91, 51));

        usuarioTF.setBackground(new java.awt.Color(253, 253, 238));
        usuarioTF.setFont(new java.awt.Font("Trebuchet MS", 0, 18)); // NOI18N
        usuarioTF.setMinimumSize(new java.awt.Dimension(200, 30));
        usuarioTF.setPreferredSize(new java.awt.Dimension(200, 25));
        usuarioTF.setRequestFocusEnabled(false);
        centerJP.add(usuarioTF, new org.netbeans.lib.awtextra.AbsoluteConstraints(155, 54, 250, 33));

        jLabel2.setFont(new java.awt.Font("Trebuchet MS", 0, 17)); // NOI18N
        jLabel2.setText("Contraseña:");
        centerJP.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(59, 115, -1, 44));

        contraseñaPF.setBackground(new java.awt.Color(255, 255, 238));
        contraseñaPF.setFont(new java.awt.Font("Trebuchet MS", 0, 18)); // NOI18N
        contraseñaPF.setMinimumSize(new java.awt.Dimension(200, 30));
        contraseñaPF.setPreferredSize(new java.awt.Dimension(200, 25));
        centerJP.add(contraseñaPF, new org.netbeans.lib.awtextra.AbsoluteConstraints(156, 126, 249, 33));

        getContentPane().add(centerJP, java.awt.BorderLayout.CENTER);

        bottomJP.setPreferredSize(new java.awt.Dimension(517, 80));

        loginBtn.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        loginBtn.setText("Entrar");
        loginBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                loginBtnActionPerformed(evt);
            }
        });

        logText.setForeground(new java.awt.Color(153, 0, 102));
        logText.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        logText.setToolTipText("");

        javax.swing.GroupLayout bottomJPLayout = new javax.swing.GroupLayout(bottomJP);
        bottomJP.setLayout(bottomJPLayout);
        bottomJPLayout.setHorizontalGroup(
            bottomJPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(bottomJPLayout.createSequentialGroup()
                .addGap(217, 217, 217)
                .addComponent(loginBtn)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(bottomJPLayout.createSequentialGroup()
                .addGap(55, 55, 55)
                .addComponent(logText, javax.swing.GroupLayout.DEFAULT_SIZE, 450, Short.MAX_VALUE)
                .addContainerGap())
        );
        bottomJPLayout.setVerticalGroup(
            bottomJPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, bottomJPLayout.createSequentialGroup()
                .addGap(0, 13, Short.MAX_VALUE)
                .addComponent(loginBtn)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(logText, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(11, 11, 11))
        );

        getContentPane().add(bottomJP, java.awt.BorderLayout.PAGE_END);

        jPanel1.setPreferredSize(new java.awt.Dimension(30, 221));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 30, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 221, Short.MAX_VALUE)
        );

        getContentPane().add(jPanel1, java.awt.BorderLayout.LINE_END);

        jPanel2.setPreferredSize(new java.awt.Dimension(30, 221));

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 30, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 221, Short.MAX_VALUE)
        );

        getContentPane().add(jPanel2, java.awt.BorderLayout.LINE_START);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void loginBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_loginBtnActionPerformed
        // TODO add your handling code here:
       onClickLogin();
    }//GEN-LAST:event_loginBtnActionPerformed

    private void altaCtaTFMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_altaCtaTFMouseClicked
        // TODO add your handling code here:
        this.setVisible(false);
        AltaCuenta.getInstance("Agregar usuario").setVisible(true); // Main Form to show after the Login_1 Form..
    }//GEN-LAST:event_altaCtaTFMouseClicked

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
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Login().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel altaCtaTF;
    private javax.swing.JPanel bottomJP;
    private javax.swing.JPanel centerJP;
    private javax.swing.JPasswordField contraseñaPF;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JLabel logText;
    private javax.swing.JButton loginBtn;
    private javax.swing.JPanel topJP;
    private javax.swing.JTextField usuarioTF;
    // End of variables declaration//GEN-END:variables
}
