/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.client.gui.others;
 
 
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Font;
 
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.plaf.basic.BasicComboBoxEditor;
 
public class MyComboBoxEditor extends BasicComboBoxEditor {
    private JLabel label = new JLabel();
    private JPanel panel = new JPanel();
    private Object selectedItem;
    Color matteGrey = new Color(223,230,233);
    Color flatBlack = new Color(77,77,77); 
    public MyComboBoxEditor() {
         
        label.setOpaque(false);
        label.setFont(new Font("SansSerif", Font.BOLD, 12));
        label.setForeground(Color.WHITE);
         
         
        panel.setLayout(new FlowLayout(FlowLayout.LEFT, 4, 2));
        panel.add(label);
        panel.setBackground(flatBlack);
         
    }
     
    public Component getEditorComponent() {
        return this.panel;
    }
     
    public Object getItem() {
       return "" + this.selectedItem.toString() + "";
   }
     
    public void setItem(Object item) {
        this.selectedItem = item;
        label.setText(item.toString());
    }
     
}



