/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core.main;

import core.connections.multicast.MulticastListener;
import core.connections.multicast.MulticastPolling;
import core.connections.multicast.MulticastServer;
import core.connections.sockets.FileServer;
import core.controller.MainController;
import core.data.ClientList;
import core.data.ClientModel;
import core.data.Config;
import core.data.ServerFile;
import core.data.Usuario;
import core.gui.custom.CustomCellRenderer;
import core.main.listener.GenericListener;
import core.services.DirectoryObserver;
import core.services.DirectoryIndexCreator;
import core.services.NetworkConnections;
import core.tasks.UserDisconnection;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Set;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 *
 * @author Luis
 */
public class Dashboard extends javax.swing.JFrame implements MulticastListener{

    /**
     * Creates new form Dashboard
     */
    
    private static JList lista;
    private int descargasActivas = 0;
    private int transferenciasActivas = 0;

    private static Dashboard instance = null;
    
    private Dashboard() {
        initComponents();
        lista = this.listaConectados;
        lista.setCellRenderer(new CustomCellRenderer("node.png"));      
        
        setTitle("Usuarios conectados");
        setLocationRelativeTo(null);
        actualizarLista(ClientList.clients);
        UserDisconnection.getInstance().register(this);

    }
    
    public static Dashboard getInstance(){
        if(instance == null){
            instance = new Dashboard();
        }
        return instance;
    }
    
    public static void actualizarLista(JSONArray array){
  
        DefaultListModel model = new DefaultListModel();
                
        for (int i = 0; i < array.size(); i++) {
            JSONObject o = (JSONObject) array.get(i);
            ClientModel cm = new ClientModel((String)o.get("label"),(String)o.get("address"),(Boolean)o.get("isLeader"), (String) o.get("index"));
            model.addElement(cm);
        }
        
        lista.setModel(model);
    }

    public static void actualizarLista(LinkedHashMap<String,ClientModel> clients){
  
        DefaultListModel model = new DefaultListModel();
        
        Set<String> keys = clients.keySet();
        
        for(String key: keys){
            ClientModel cm = clients.get(key);
            
            if(!cm.getAddress().equals(Config.YOUR_ADDR)){
                model.addElement(cm);
            }           
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

        jPanel1 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        listaConectados = new javax.swing.JList();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Usuarios Conectados"));

        listaConectados.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Esperando usuarios..." };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        listaConectados.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                listaConectadosMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(listaConectados);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 540, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 363, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void listaConectadosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_listaConectadosMouseClicked
    
    JList list = (JList)evt.getSource();
        if (evt.getClickCount() == 2) {
                       
            ClientModel cm = (ClientModel)list.getSelectedValue();
            ArrayList<ServerFile> files= MainController.getRootContent(cm.getAddress());
           
            new FileExplorer(files,cm).setVisible(true);
        }
    }//GEN-LAST:event_listaConectadosMouseClicked


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JList listaConectados;
    // End of variables declaration//GEN-END:variables

    @Override
    public void updateList() {
        actualizarLista(ClientList.clients);
    }

//    @Override
//    public void startEvent(Object source, String descripcion, int eventId) {
//        if(eventId == 0){
//            descargasActivas++;
//        }else if(eventId == 1){
//            transferenciasActivas++;
//        }
//        
//        updateLabels();
//    }
//
//    @Override
//    public void endEvent(Object source, String descripcion, int eventId) {
//        if(eventId == 0){
//            descargasActivas--;
//        }else if(eventId == 1){
//            transferenciasActivas--;
//        }
//        
//        updateLabels();
//    }
//    
//    private void updateLabels(){
//        dLabel.setText("Descargando: "+descargasActivas);
//        tLabel.setText("Enviando: "+transferenciasActivas);
//    }

    @Override
    public void connectedUser(ClientModel usuario) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void disconnectedUser(ClientModel usuario) {

    }
    
    
}