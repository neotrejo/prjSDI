/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core.gui.custom;

import java.awt.Color;
import java.awt.Component;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import javax.swing.border.Border;

/**
 *
 * @author Luis
 */
public class CustomCellRenderer extends JLabel implements ListCellRenderer<Object>{

    private Border border;
    private Border emptyBorder;
    private ImageIcon image;
    
    public CustomCellRenderer(){
        setIconTextGap(10);
        setOpaque(true);
        border = BorderFactory.createLineBorder(Color.BLUE, 1);
        emptyBorder = BorderFactory.createEmptyBorder(3,3,3,3);
    }
    
    public CustomCellRenderer(String image){
        try {
            setIconTextGap(10);
            setOpaque(true);
            border = BorderFactory.createLineBorder(Color.BLUE, 1);
            emptyBorder = BorderFactory.createEmptyBorder(3,3,3,3);
            ImageIcon img = new ImageIcon(ImageIO.read(getClass().getResource(image)));
            this.image = img;
        } catch (IOException ex) {
            Logger.getLogger(CustomCellRenderer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public Component getListCellRendererComponent(JList<? extends Object> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
 
            setText(value.toString());
            setIcon(this.image);
            
            if (isSelected)
            {
               setBackground(list.getSelectionBackground());
               setForeground(list.getSelectionForeground());
            }
            else
            {
               setBackground(list.getBackground());
               setForeground(list.getForeground());
            }

            setFont(list.getFont());

            setEnabled(list.isEnabled());

            if (isSelected && cellHasFocus){
               setBorder(border);
            }else{
               setBorder(emptyBorder);
            }
        
        return this;
    }
    
    
}
