/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core.gui.custom;

import java.awt.Color;
import java.awt.Component;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

/**
 *
 * @author SDI
 */
public class CustomTableCellRenderer extends DefaultTableCellRenderer{

  public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
      boolean hasFocus, int rowIndex, int vColIndex) {
    
      super.getTableCellRendererComponent(table, value, isSelected, hasFocus, rowIndex, vColIndex);
    
    setText(value.toString());

    if(value.toString().equals("Incompleta")){
        setForeground(Color.red);
    }else if(value.toString().equals("Completada")){
        setForeground(new Color(0, 102, 0));
    }else if(value.toString().equals("Descargando")){
        setForeground(Color.blue);
    }else if(value.toString().equals("Enviando")){
        setForeground(Color.blue);
    }else{
        setForeground(Color.black);
    }
    
    if(isSelected){
        setForeground(Color.white);
    }
    
    return this;
  }
    
}
