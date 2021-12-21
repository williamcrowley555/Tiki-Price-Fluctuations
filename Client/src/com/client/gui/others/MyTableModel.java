/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.client.gui.others;

import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Hi
 */
public class MyTableModel extends DefaultTableModel{
    public MyTableModel(String[] colNames){
        super();
        for (String colName : colNames)
            this.addColumn(colName);
    }
    
    @Override
    public boolean isCellEditable(int row, int column)
    {
        return false;
    }
    
    @Override
    public Class getColumnClass(int column)
    {
        switch (column){
            case 0:
                return String.class;
            case 1:
                return String.class;
            default:
                return String.class;
        }
    }
    
}
