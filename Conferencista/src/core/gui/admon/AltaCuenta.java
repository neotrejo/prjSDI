/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core.gui.admon;

import core.controller.MainController;
import core.crypt.CryptCipher;
import core.data.User;
import core.main.ExploradorGlobal;
import core.utils.GenericUtils;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.Toolkit;
import java.beans.PropertyVetoException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.KeyStroke;

/**
 *
 * @author Diana
 */
public class AltaCuenta extends javax.swing.JFrame {

    /**
     * Creates new form login
     */
    private static AltaCuenta instance;
    private JFileChooser fileChooser;

    public AltaCuenta(String title) {
        initComponents();
        Mnemonicos();
        fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        fileChooser.setAcceptAllFileFilterUsed(false);
        hostnameTF.setText(GenericUtils.getHostname());
        centerJP.setOpaque(true);
        //centerJP.setBorder(BorderFactory.createTitledBorder(title));
    }

    public static AltaCuenta getInstance(String title) {
        if (instance == null) {
            instance = new AltaCuenta(title);
        }
        return instance;
    }
    
    private void Mnemonicos() {
        // create an Action doing what you want
        Action action = new AbstractAction("Guardar") {
            @Override
            public void actionPerformed(ActionEvent e) {
                onClickCrearCta();
            }
        };
        action.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0));
        crearCtaBtn.setAction(action);
        crearCtaBtn.getActionMap().put("myAction", action);
        crearCtaBtn.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put((KeyStroke) action.getValue(Action.ACCELERATOR_KEY), "myAction");
    }
    
    private void onClickCrearCta(){
        if (!nombreTF.getText().isEmpty()) {
            if (!usuarioTF.getText().isEmpty()) {
                if (!contraseñaPF.getText().isEmpty()) {
                    if (!correoTF.getText().isEmpty()) {
                        if (!hostnameTF.getText().isEmpty()) {
                            if (!sharedfolderTF.getText().isEmpty()) {
                                try {
                                    User user;
                                    user = MainController.
                                            existUserName(usuarioTF.getText());
                                    if (user == null) {
                                        String passCryp = CryptCipher.encrypt(contraseñaPF.getText());
                                        user = MainController.addUser(nombreTF.getText(),usuarioTF.getText(),
                                                passCryp, correoTF.getText(),hostnameTF.getText(),
                                                sharedfolderTF.getText(), portTF.getText());                                        
                                        
                                        this.setVisible(false);
                                        try {
                                            ExploradorGlobal.getInstance(user).setVisible(true);
                                        } catch (PropertyVetoException ex) {
                                            Logger.getLogger(AltaCuenta.class.getName()).log(Level.SEVERE, null, ex);
                                        }
                                    } else {
                                        logL.setText("Usuario ya existe");
                                    }
                                } catch (NoSuchPaddingException ex) {
                                    Logger.getLogger(AltaCuenta.class.getName()).log(Level.SEVERE, null, ex);
                                } catch (NoSuchAlgorithmException ex) {
                                    Logger.getLogger(AltaCuenta.class.getName()).log(Level.SEVERE, null, ex);
                                } catch (InvalidAlgorithmParameterException ex) {
                                    Logger.getLogger(AltaCuenta.class.getName()).log(Level.SEVERE, null, ex);
                                } catch (InvalidKeyException ex) {
                                    Logger.getLogger(AltaCuenta.class.getName()).log(Level.SEVERE, null, ex);
                                } catch (BadPaddingException ex) {
                                    Logger.getLogger(AltaCuenta.class.getName()).log(Level.SEVERE, null, ex);
                                } catch (IllegalBlockSizeException ex) {
                                    Logger.getLogger(AltaCuenta.class.getName()).log(Level.SEVERE, null, ex);
                                }
                            } else {
                                logL.setText("Folder a compartir esta vacio");
                            }
                        } else {
                            logL.setText("Hostname esta vacio");
                        }
                    } else {
                        logL.setText("Correo esta vacio.");
                    }
                } else {
                    logL.setText("Contraseña esta vacia");
                }
            } else {
                logL.setText("Usuario esta vacio");
            }
        } else {
            logL.setText("Nombre esta vacio");
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

        centerJP = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        nombreTF = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        usuarioTF = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        contraseñaPF = new javax.swing.JPasswordField();
        jLabel4 = new javax.swing.JLabel();
        correoTF = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        hostnameTF = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        sharedfolderTF = new javax.swing.JTextField();
        sharedFolderBtn = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        portTF = new javax.swing.JTextField();
        topJP = new javax.swing.JPanel();
        regresarL = new javax.swing.JLabel();
        bottomJP = new javax.swing.JPanel();
        crearCtaBtn = new javax.swing.JButton();
        logL = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("PreDisMaD-Conf");
        setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/core/images/confer.png")) );
        setPreferredSize(new java.awt.Dimension(565, 620));
        setSize(new java.awt.Dimension(565, 620));

        centerJP.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Registro de usuario", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Bahnschrift", 0, 16), new java.awt.Color(0, 102, 102))); // NOI18N
        centerJP.setPreferredSize(new java.awt.Dimension(420, 410));
        centerJP.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setFont(new java.awt.Font("Trebuchet MS", 0, 16)); // NOI18N
        jLabel1.setText("Nombre:");
        jLabel1.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        centerJP.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 40, 216, 35));

        nombreTF.setFont(new java.awt.Font("Trebuchet MS", 0, 16)); // NOI18N
        nombreTF.setMaximumSize(new java.awt.Dimension(2147483647, 50));
        centerJP.add(nombreTF, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 40, 260, 35));

        jLabel2.setFont(new java.awt.Font("Trebuchet MS", 0, 16)); // NOI18N
        jLabel2.setText("Usuario:");
        jLabel2.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        centerJP.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 90, 216, 35));

        usuarioTF.setFont(new java.awt.Font("Trebuchet MS", 0, 16)); // NOI18N
        centerJP.add(usuarioTF, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 90, 260, 35));

        jLabel3.setFont(new java.awt.Font("Trebuchet MS", 0, 16)); // NOI18N
        jLabel3.setText("Contraseña:");
        centerJP.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 150, 216, 35));

        contraseñaPF.setFont(new java.awt.Font("Trebuchet MS", 0, 16)); // NOI18N
        contraseñaPF.setToolTipText("");
        centerJP.add(contraseñaPF, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 150, 260, 35));

        jLabel4.setFont(new java.awt.Font("Trebuchet MS", 0, 16)); // NOI18N
        jLabel4.setText("Correo:");
        centerJP.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 200, 216, 35));

        correoTF.setFont(new java.awt.Font("Trebuchet MS", 0, 16)); // NOI18N
        centerJP.add(correoTF, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 200, 260, 35));

        jLabel6.setFont(new java.awt.Font("Trebuchet MS", 0, 16)); // NOI18N
        jLabel6.setText("Hostname:");
        centerJP.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 260, 216, 35));

        hostnameTF.setFont(new java.awt.Font("Trebuchet MS", 0, 16)); // NOI18N
        centerJP.add(hostnameTF, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 260, 260, 35));

        jLabel7.setFont(new java.awt.Font("Trebuchet MS", 0, 16)); // NOI18N
        jLabel7.setText("Folder a compartir:");
        jLabel7.setToolTipText("");
        centerJP.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 310, 150, 35));

        sharedfolderTF.setFont(new java.awt.Font("Trebuchet MS", 0, 16)); // NOI18N
        centerJP.add(sharedfolderTF, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 310, 216, 35));

        sharedFolderBtn.setFont(new java.awt.Font("Trebuchet MS", 0, 15)); // NOI18N
        sharedFolderBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/core/images/folder2.png"))); // NOI18N
        sharedFolderBtn.setToolTipText("");
        sharedFolderBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sharedFolderBtnActionPerformed(evt);
            }
        });
        centerJP.add(sharedFolderBtn, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 310, 40, 35));

        jLabel5.setFont(new java.awt.Font("Trebuchet MS", 0, 16)); // NOI18N
        jLabel5.setText("Puerto:");
        centerJP.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 360, -1, -1));
        centerJP.add(portTF, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 360, 260, 35));

        getContentPane().add(centerJP, java.awt.BorderLayout.CENTER);

        topJP.setPreferredSize(new java.awt.Dimension(630, 60));

        regresarL.setFont(new java.awt.Font("Yu Gothic", 1, 14)); // NOI18N
        regresarL.setForeground(new java.awt.Color(0, 51, 255));
        regresarL.setText(" << Regresar");
        regresarL.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                regresarLMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout topJPLayout = new javax.swing.GroupLayout(topJP);
        topJP.setLayout(topJPLayout);
        topJPLayout.setHorizontalGroup(
            topJPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, topJPLayout.createSequentialGroup()
                .addContainerGap(427, Short.MAX_VALUE)
                .addComponent(regresarL)
                .addGap(52, 52, 52))
        );
        topJPLayout.setVerticalGroup(
            topJPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(topJPLayout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addComponent(regresarL)
                .addContainerGap(14, Short.MAX_VALUE))
        );

        getContentPane().add(topJP, java.awt.BorderLayout.PAGE_START);

        crearCtaBtn.setFont(new java.awt.Font("Trebuchet MS", 0, 15)); // NOI18N
        crearCtaBtn.setText("Guardar");
        crearCtaBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                crearCtaBtnActionPerformed(evt);
            }
        });

        logL.setForeground(new java.awt.Color(153, 0, 102));
        logL.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        javax.swing.GroupLayout bottomJPLayout = new javax.swing.GroupLayout(bottomJP);
        bottomJP.setLayout(bottomJPLayout);
        bottomJPLayout.setHorizontalGroup(
            bottomJPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(bottomJPLayout.createSequentialGroup()
                .addGroup(bottomJPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(bottomJPLayout.createSequentialGroup()
                        .addGap(38, 38, 38)
                        .addComponent(logL, javax.swing.GroupLayout.PREFERRED_SIZE, 450, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(bottomJPLayout.createSequentialGroup()
                        .addGap(224, 224, 224)
                        .addComponent(crearCtaBtn)))
                .addContainerGap(78, Short.MAX_VALUE))
        );
        bottomJPLayout.setVerticalGroup(
            bottomJPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(bottomJPLayout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addComponent(crearCtaBtn)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(logL, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        getContentPane().add(bottomJP, java.awt.BorderLayout.PAGE_END);

        jPanel4.setPreferredSize(new java.awt.Dimension(50, 415));

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 50, Short.MAX_VALUE)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 437, Short.MAX_VALUE)
        );

        getContentPane().add(jPanel4, java.awt.BorderLayout.LINE_END);

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 50, Short.MAX_VALUE)
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 437, Short.MAX_VALUE)
        );

        getContentPane().add(jPanel5, java.awt.BorderLayout.LINE_START);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void crearCtaBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_crearCtaBtnActionPerformed
        // TODO add your handling code here:
            onClickCrearCta();
    }//GEN-LAST:event_crearCtaBtnActionPerformed

    private void sharedFolderBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sharedFolderBtnActionPerformed
        // TODO add your handling code here:
        // TODO add your handling code here:
        if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            sharedfolderTF.setText(fileChooser.getSelectedFile().toString());
            //MainController.updateSharedFolder(sharedFolderTxt.getText());

        } else {
            System.out.println("No Selection ");
        }
    }//GEN-LAST:event_sharedFolderBtnActionPerformed

    private void regresarLMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_regresarLMouseClicked
        // TODO add your handling code here:
        this.setVisible(false);
        new Login().setVisible(true);
    }//GEN-LAST:event_regresarLMouseClicked


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel bottomJP;
    private javax.swing.JPanel centerJP;
    private javax.swing.JPasswordField contraseñaPF;
    private javax.swing.JTextField correoTF;
    private javax.swing.JButton crearCtaBtn;
    private javax.swing.JTextField hostnameTF;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JLabel logL;
    private javax.swing.JTextField nombreTF;
    private javax.swing.JTextField portTF;
    private javax.swing.JLabel regresarL;
    private javax.swing.JButton sharedFolderBtn;
    private javax.swing.JTextField sharedfolderTF;
    private javax.swing.JPanel topJP;
    private javax.swing.JTextField usuarioTF;
    // End of variables declaration//GEN-END:variables
}
