/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tiki_server.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author HP
 */
public class DBConnectionUtil {
    
    private static final String URL = "jdbc:mysql://localhost:3306/tiki?characterEncoding=utf-8&useConfigs=maxPerformance";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "admin123";
    private static Connection connection = null;
    
    public static Connection getConnection() throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        try {
            Class.forName ("com.mysql.jdbc.Driver").newInstance();
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        return connection;
    }
}
