/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core.gui.custom;

import core.data.Descarga;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author Luis
 */
public class DownloadsTableModel extends AbstractTableModel{

    private String columnNames[] = new String[]{"Archivo","Estado","Porcentaje","Origen"};
    private HashMap<Integer,Descarga> descargas;
    private ArrayList<Descarga> descargasList;
    
    public DownloadsTableModel(){
        this.descargas = new HashMap<>();
        descargasList = new ArrayList<>();
    }
    
    public void setColumnNames(String columns[]){
        columnNames = columns;
    }
    
    public void setRowCount(int count){
        this.descargas.clear();
        descargasList.clear();
    }
    
    public void addRow(Descarga descarga){
        descargasList.add(descarga);
        descargas.put(descarga.getId(), descarga);
    }

    public Descarga getDescarga(int key){
        return descargas.get(key);
    }
    
    public int indexOf(Descarga descarga){
        return 1;
    }
    
    @Override
    public String getColumnName(int column) {
        return columnNames[column]; //To change body of generated methods, choose Tools | Templates.
    }   

    @Override
    public int getRowCount() {
        return descargasList.size();
    }

    @Override
    public int getColumnCount() {
        return 4;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        
        Descarga descarga = descargasList.get(rowIndex);
        
        if(descarga!=null){
            switch(columnIndex){
                case 0: return descarga.getName();
                case 1: return descarga.getStatus();
                case 2: return descarga.getPercent()+"%";
                case 3: return descarga.getHost();
            }
        }else{
        }
        return "";
    }
    
    public Descarga getDescargaAtIndex(int index){
        return descargasList.get(index);
    }
    
    public void remove(Descarga descarga){
        descargas.remove(descarga.getId());
        descargasList.remove(descarga);
    }
    
}
