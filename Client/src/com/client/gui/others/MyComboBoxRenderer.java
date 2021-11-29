/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.client.gui.others;
 
 
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
 
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
 
public class MyComboBoxRenderer extends JLabel implements ListCellRenderer {
    Color matteGrey = new Color(223,230,233);
    Color flatBlack = new Color(77,77,77);
    public MyComboBoxRenderer() {
        setOpaque(true);
        setFont(new Font("SansSerif", Font.BOLD | Font.PLAIN, 12));
        setBackground(flatBlack);
        setForeground(Color.WHITE);
        
    }
     
    @Override
    public Component getListCellRendererComponent(JList list, Object value,
            int index, boolean isSelected, boolean cellHasFocus) {
        if (value!=null)
        setText(value.toString());
        return this;
    }
 
}
