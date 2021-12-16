/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.client.main;

import com.client.enums.MessageType;
import com.client.gui.*;
import com.client.model.Message;
import com.client.thread.ReadThread;
import com.client.thread.WriteThread;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JScrollBar;
import static javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER;
import java.awt.Rectangle;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.geom.Area;
import java.awt.geom.RoundRectangle2D;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.net.ConnectException;
import java.net.Socket;
import java.security.PublicKey;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.SecretKey;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.border.LineBorder;

/**
 *
 * @author Hi
 */
public class Client extends javax.swing.JFrame {

    /**
     * Creates new form Main
     */
    private static String hostname = "localhost";
    private static int port = 5002;

    private Socket socket;

    private ObjectOutput out;

    private Thread readThread;

    private PublicKey publicKey;
    private SecretKey secretKey;
    
    private boolean menuHided = false;
    private boolean maximized = false;
    ImageIcon iconShowMenu = new ImageIcon(getClass().getResource("/com/client/img/show_menu_icon.png"));
    ImageIcon iconHideMenu = new ImageIcon(getClass().getResource("/com/client/img/hide_menu_icon.png"));
    ImageIcon iconRestoreDown = new ImageIcon(getClass().getResource("/com/client/img/restore_down.png"));
  
    panelTimTheoURL pnlURL;
    panelTimNangCao pnlAdvanced;
    
    public Client(){}
    public Client(String hostname, int port) {
        initComponents();
        invisibleMenuScrollBar(8);
        this.hostname = hostname;
        this.port = port;
        panelBody.repaint();
        panelBody.revalidate();
       
        initCardLayout();
        CustomWindow();
         
    }
    
    public void setReviewsList(ArrayList<LinkedHashMap<String, Object>> reviewsList){
        this.pnlURL.setReviewsList(reviewsList);
    }
    
    public void setTimelineList(ArrayList<LinkedHashMap<String, Object>> timelinesList){
        this.pnlURL.setTimelinesList(timelinesList);
    }
    
    public void setCurrentProduct(LinkedHashMap<String, Object> product){
        this.pnlURL.setCurrentProduct(product);
    }
    
    public void updateLineChartURL(List<LinkedHashMap<String, Object>> productHistories, String productName){
        if (productHistories == null) {
            JOptionPane.showMessageDialog(this, "Không có dữ liệu lịch sử giá", "Thông báo", JOptionPane.INFORMATION_MESSAGE); 
            return;
        } 

        ArrayList<String> dates = new ArrayList<String>();
        ArrayList<Integer> prices = new ArrayList<Integer>();
        int month = 1;
        int year = 2000;
        for (LinkedHashMap<String, Object> history : productHistories) 
        {   
            LinkedHashMap<String, Object> dateObj= (LinkedHashMap<String, Object>) history.get("date");
            dates.add(String.valueOf(dateObj.get("dayOfMonth")));
            month = (int) dateObj.get("monthValue");
            year = (int)  dateObj.get("year");
            prices.add((int) history.get("price"));
        }
        this.pnlURL.showLineChart(productName, month, year, dates, prices);
    }
    
    public void updateLineChartAdvance(List<LinkedHashMap<String, Object>> productHistories, String productName){
        if (productHistories == null) {
            JOptionPane.showMessageDialog(this, "Không có dữ liệu lịch sử giá", "Thông báo", JOptionPane.INFORMATION_MESSAGE); 
            return;
        } 

        ArrayList<String> dates = new ArrayList<String>();
        ArrayList<Integer> prices = new ArrayList<Integer>();
        int month = 1;
        int year = 2000;
        for (LinkedHashMap<String, Object> history : productHistories) 
        {   
            LinkedHashMap<String, Object> dateObj= (LinkedHashMap<String, Object>) history.get("date");
            dates.add(String.valueOf(dateObj.get("dayOfMonth")));
            month = (int) dateObj.get("monthValue");
            year = (int)  dateObj.get("year");
            prices.add((int) history.get("price"));
        }
        this.pnlAdvanced.showLineChart(productName, month, year, dates, prices);
    }
    
    public void updateBrands(List<LinkedHashMap<String, Object>> recvBrands)
    {  
        HashMap<Integer, String> listBrands = new HashMap<Integer, String>();
        int numberOfBrands = 0;
        for (LinkedHashMap<String, Object> brand : recvBrands)
        {   
            System.out.println(brand.get("name"));
            if(!listBrands.containsValue(brand.get("name")))
            {
                listBrands.put(numberOfBrands, (String) brand.get("name"));
                numberOfBrands++;
            }
        }
        pnlAdvanced.updateBrandCheckbox(listBrands, numberOfBrands);
    }
    
    public void updateComboboxCategory(List<LinkedHashMap<String, Object>> categories){
        ArrayList<String> name = new ArrayList<String>();
        ArrayList<Integer> idCate = new ArrayList<Integer>();
        for (LinkedHashMap<String, Object> category : categories) 
        {   
            name.add(String.valueOf(category.get("name")));
            idCate.add((Integer) (category.get("id")));
        }
       // System.out.println(name);
        this.pnlAdvanced.listNameCategory(name,idCate);
    }
    
    public void updateProductInfoURL(LinkedHashMap<String, Object> recvProduct){
       this.pnlURL.updateProductInfo(recvProduct);
    }
    
    public void run() {
        try {
            socket = new Socket(hostname, port);
            out = new ObjectOutputStream(new BufferedOutputStream(socket.getOutputStream()));

            readThread = new Thread(new ReadThread(this, this.socket));
            readThread.start();
        } catch (ConnectException conEx){
            System.err.println("Can't connect to server");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    
    public void closeSocket() throws IOException, InterruptedException {
        Message message = new Message(null, MessageType.USER_DISCONNECT);
        sendMessage(message);
        readThread.join();

        out.close();
        socket.close();
        System.out.println("Close socket: " + socket.isClosed());
    }

    public void sendMessage(Message message) throws IOException {
        Thread writeThread = new Thread(new WriteThread(this, this.socket, this.out, message));
        writeThread.start();

        if (message.getMessageType().equals(MessageType.GET_PUBLIC_KEY)) {
            try {
                writeThread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void sendPublicKeyRequest() throws IOException {
        Message requestMsg = new Message(null, MessageType.GET_PUBLIC_KEY);
        sendMessage(requestMsg);
    }
    
    public void setTable(List<List<LinkedHashMap<String, Object>>> listAdvanceProducts)
    {
        pnlAdvanced.setTable(listAdvanceProducts);
    }

    public void getProduct(Long productId) throws IOException {
        Map<String, Object> request = new HashMap<>();
        request.put("productId", productId);

        Message requestMsg = new Message(request, MessageType.GET_PRODUCT);
        sendMessage(requestMsg);
    }
    
    public void getBrandsByCategoryId(Long categoryId) throws IOException
    {
        Map<String, Object> request = new HashMap<>();
        request.put("categoryId", categoryId);
        
        Message requestMsg = new Message(request, MessageType.GET_BRANDS_BY_CATEGORY_ID);
        sendMessage(requestMsg);
    }

    public void filterProducts(String productName, Long categoryId, Long brandId, float ratingAverage, Long minPrice, Long maxPrice) throws IOException {
        Map<String, Object> request = new HashMap<>();
        request.put("productName", productName);
        request.put("categoryId", categoryId);
        request.put("brandId", brandId);
        request.put("ratingAverage", ratingAverage);
        request.put("minPrice", minPrice);
        request.put("maxPrice", maxPrice);

        Message requestMsg = new Message(request, MessageType.FILTER_PRODUCTS);
        sendMessage(requestMsg);
    }

    public void getConfigurableProducts(Long productId) throws IOException {
        Map<String, Object> request = new HashMap<>();
        request.put("productId", productId);

        Message requestMsg = new Message(request, MessageType.GET_CONFIGURABLE_PRODUCTS);
        sendMessage(requestMsg);
    }
    
    public void getCategories() throws IOException {
        Map<String, Object> request = new HashMap<>();
        Message requestMsg = new Message(request, MessageType.GET_CATEGORIES);
        sendMessage(requestMsg);
    }
    
    public void sendRequestAdvanceProducts(String productName, String category, String brands, String rating, String fromMoney, String toMoney) throws IOException {
        Map<String, Object> request = new HashMap<>();
        if(!productName.equals(""))
            request.put("productName", productName);
        request.put("category", category);
        if(!brands.equals(""))
            request.put("brands", brands);
        if(rating.equals("0"))
        {
            JOptionPane.showMessageDialog(this, "Không có check", "Thông báo", JOptionPane.INFORMATION_MESSAGE); 
            return;
        }
        else
            request.put("ratingAverage", rating);
        if(!fromMoney.equals(""))
            request.put("fromMoney", fromMoney);
        if(!toMoney.equals(""))
            request.put("toMoney", toMoney);
        Message requestMsg = new Message(request, MessageType.GET_ADVANCE_PRODUCTS);
        sendMessage(requestMsg);
    }
    
    public void getProductHistories(String url, int month, int year) throws IOException {
        Map<String, Object> request = new HashMap<>();
        request.put("url", url);
        request.put("month", month);
        request.put("year", year);

        Message requestMsg = new Message(request, MessageType.GET_PRODUCT_HISTORIES_BY_URL);
        sendMessage(requestMsg);
    }
    
    public void getConfigurableOptionById(Long productId) throws IOException {
        Map<String, Object> request = new HashMap<>();
        request.put("productId", productId);
        
        Message requestMsg = new Message(request, MessageType.GET_CONFIGURABLE_OPTION_BY_PRODUCT_ID);
        sendMessage(requestMsg);
    }

    public void getProductHistoriesById(Long productId, int month, int year) throws IOException {
        Map<String, Object> request = new HashMap<>();
        request.put("productId", productId);
        request.put("month", month);
        request.put("year", year);

        Message requestMsg = new Message(request, MessageType.GET_PRODUCT_HISTORIES_BY_PRODUCT_ID);
        sendMessage(requestMsg);
    }

    public void getConfigurableProductHistories(Long productId, String option1, String option2, String option3, int month, int year) throws IOException {
        Map<String, Object> request = new HashMap<>();
        request.put("productId", productId);
        request.put("option1", option1);
        request.put("option2", option2);
        request.put("option3", option3);
        request.put("month", month);
        request.put("year", year);

        Message requestMsg = new Message(request, MessageType.GET_CONFIGURABLE_PRODUCT_HISTORIES);
        sendMessage(requestMsg);
    }

    public void getReviews(Long productId) throws IOException {
        Map<String, Object> request = new HashMap<>();
        request.put("productId", productId);

        Message requestMsg = new Message(request, MessageType.GET_REVIEWS_BY_PRODUCT_ID);
        sendMessage(requestMsg);
    }
    
    public void getTimeLineByReviewId(Long reviewId) throws IOException {
        Map<String, Object> request = new HashMap<>();
        request.put("reviewId", reviewId);
        Message requestMsg = new Message(request, MessageType.GET_TIMELINE_BY_REVIEWID);
       
        sendMessage(requestMsg);
    }
    
    public SecretKey getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(SecretKey secretKey) {
        this.secretKey = secretKey;
    }

    public PublicKey getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(PublicKey publicKey) {
        this.publicKey = publicKey;
    }
    
    public void initCardLayout(){
        panelTimTheoURL pnlURL = new panelTimTheoURL(this);
        panelTimNangCao pnlAdvanced = new panelTimNangCao(this);
        
        this.pnlURL = pnlURL;
        this.pnlAdvanced = pnlAdvanced;
        
        pnlTabContent.add("url", pnlURL);
        pnlTabContent.add("advanced", pnlAdvanced);
        switchCard("url");
        setTabSelection(btnUrlTab);
    }
    
    public void switchCard(String name){
        CardLayout card = (CardLayout) (pnlTabContent.getLayout());
        card.show(pnlTabContent, name);
    }
    
    public void setTabSelection(JButton button){
        resetSelection();
        button.setBackground(new Color(77,77,77));
        button.setForeground(Color.white);
        
    }
    
    public void resetSelection(){
        btnUrlTab.setBorder(new LineBorder(Color.BLACK));
        btnUrlTab.setBackground(Color.white);
        btnUrlTab.setForeground(Color.BLACK);
        
        btnAdvancedTab.setBorder(new LineBorder(Color.BLACK));
        btnAdvancedTab.setBackground(Color.white);
        btnAdvancedTab.setForeground(Color.BLACK);
    }
    
    
    public void invisibleMenuScrollBar(int speed)
    {
          JScrollBar scrollBar = new JScrollBar(JScrollBar.VERTICAL) {

                    @Override
                    public boolean isVisible() {
                        return true;
                    }
                };
//        menuScroll.setVerticalScrollBar(scrollBar);
//        menuScroll.setVerticalScrollBarPolicy(menuScroll.VERTICAL_SCROLLBAR_NEVER);
//        menuScroll.getVerticalScrollBar().setUnitIncrement(speed);
    }
    public void center()
    {
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation(dim.width/2-this.getSize().width/2, dim.height/2-this.getSize().height/2);
    }
    public void CustomWindow()
    {   
        Color flatBlack = new Color(77,77,77);//Border
        this.setMinimumSize(new Dimension(1280,720));
        //this.setSize(new Dimension(1280,720));
        this.getRootPane().setBorder(BorderFactory.createMatteBorder(0,2,2,2, flatBlack));   
        center();
        lblMinimize.setText("\u2014");
        lblExit.setText("X");
        lblMaximize_Restore.setText("\u2750");
    }
    
    
    
    public void Maximize_Restore(boolean state)
    {
       if (state)
       {
            lblMaximize_Restore.setText("\u2750");
            lblMaximize_Restore.setIcon(null);
            maximized = false;
            this.setPreferredSize(new Dimension(800,600));
            this.setExtendedState(JFrame.NORMAL);
       }
       else 
       {
           lblMaximize_Restore.setIcon(iconRestoreDown);
           lblMaximize_Restore.setText("");
           this.setExtendedState(JFrame.MAXIMIZED_BOTH);
           maximized = true;   
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

        panelHeader = new javax.swing.JPanel();
        lblMinimize = new javax.swing.JLabel();
        lblExit = new javax.swing.JLabel();
        lblMaximize_Restore = new javax.swing.JLabel();
        lblShow_HideMenu = new javax.swing.JLabel();
        panelBody = new javax.swing.JPanel();
        pnlTabs = new javax.swing.JPanel();
        btnUrlTab = new javax.swing.JButton();
        btnAdvancedTab = new javax.swing.JButton();
        pnlTabContent = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);

        panelHeader.setBackground(new java.awt.Color(38, 38, 38));
        panelHeader.setForeground(new java.awt.Color(255, 255, 255));
        panelHeader.setPreferredSize(new java.awt.Dimension(561, 40));
        panelHeader.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                panelHeaderMouseDragged(evt);
            }
        });

        lblMinimize.setBackground(new java.awt.Color(255, 255, 255));
        lblMinimize.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        lblMinimize.setForeground(new java.awt.Color(255, 255, 255));
        lblMinimize.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblMinimize.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblMinimizeMouseClicked(evt);
            }
        });

        lblExit.setBackground(new java.awt.Color(255, 255, 255));
        lblExit.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        lblExit.setForeground(new java.awt.Color(255, 255, 255));
        lblExit.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblExit.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblExitMouseClicked(evt);
            }
        });

        lblMaximize_Restore.setBackground(new java.awt.Color(255, 255, 255));
        lblMaximize_Restore.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        lblMaximize_Restore.setForeground(new java.awt.Color(255, 255, 255));
        lblMaximize_Restore.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblMaximize_Restore.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblMaximize_RestoreMouseClicked(evt);
            }
        });

        lblShow_HideMenu.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblShow_HideMenu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/client/img/show_menu_icon.png"))); // NOI18N
        lblShow_HideMenu.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                lblShow_HideMenuMousePressed(evt);
            }
        });

        javax.swing.GroupLayout panelHeaderLayout = new javax.swing.GroupLayout(panelHeader);
        panelHeader.setLayout(panelHeaderLayout);
        panelHeaderLayout.setHorizontalGroup(
            panelHeaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelHeaderLayout.createSequentialGroup()
                .addComponent(lblShow_HideMenu, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 654, Short.MAX_VALUE)
                .addComponent(lblMinimize, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(lblMaximize_Restore, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(lblExit, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        panelHeaderLayout.setVerticalGroup(
            panelHeaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(lblMinimize, javax.swing.GroupLayout.DEFAULT_SIZE, 40, Short.MAX_VALUE)
            .addComponent(lblExit, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(lblMaximize_Restore, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(lblShow_HideMenu, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        getContentPane().add(panelHeader, java.awt.BorderLayout.PAGE_START);

        panelBody.setBackground(new java.awt.Color(255, 255, 255));
        panelBody.setLayout(new java.awt.BorderLayout());

        pnlTabs.setBackground(new java.awt.Color(255, 255, 255));
        pnlTabs.setPreferredSize(new java.awt.Dimension(853, 40));
        pnlTabs.setRequestFocusEnabled(false);
        pnlTabs.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        btnUrlTab.setBackground(new java.awt.Color(255, 255, 255));
        btnUrlTab.setText("Tìm theo URL");
        btnUrlTab.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        btnUrlTab.setContentAreaFilled(false);
        btnUrlTab.setOpaque(true);
        btnUrlTab.setPreferredSize(new java.awt.Dimension(120, 40));
        btnUrlTab.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUrlTabActionPerformed(evt);
            }
        });
        pnlTabs.add(btnUrlTab);

        btnAdvancedTab.setBackground(new java.awt.Color(255, 255, 255));
        btnAdvancedTab.setText("Tìm Nâng cao");
        btnAdvancedTab.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        btnAdvancedTab.setContentAreaFilled(false);
        btnAdvancedTab.setOpaque(true);
        btnAdvancedTab.setPreferredSize(new java.awt.Dimension(120, 40));
        btnAdvancedTab.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAdvancedTabActionPerformed(evt);
            }
        });
        pnlTabs.add(btnAdvancedTab);

        panelBody.add(pnlTabs, java.awt.BorderLayout.PAGE_START);

        pnlTabContent.setBackground(new java.awt.Color(255, 255, 255));
        pnlTabContent.setLayout(new java.awt.CardLayout());
        panelBody.add(pnlTabContent, java.awt.BorderLayout.CENTER);

        getContentPane().add(panelBody, java.awt.BorderLayout.CENTER);

        setSize(new java.awt.Dimension(853, 566));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void lblMaximize_RestoreMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblMaximize_RestoreMouseClicked
        // TODO add your handling code here:
        Maximize_Restore(maximized);
    }//GEN-LAST:event_lblMaximize_RestoreMouseClicked

    private void lblExitMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblExitMouseClicked
        System.exit(0);
    }//GEN-LAST:event_lblExitMouseClicked

    private void lblMinimizeMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblMinimizeMouseClicked
        this.setState(JFrame.ICONIFIED);
    }//GEN-LAST:event_lblMinimizeMouseClicked

    private void panelHeaderMouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_panelHeaderMouseDragged
           setLocation (evt.getXOnScreen()-(getWidth()/2),evt.getYOnScreen()-10);
    }//GEN-LAST:event_panelHeaderMouseDragged

    private void lblShow_HideMenuMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblShow_HideMenuMousePressed

    }//GEN-LAST:event_lblShow_HideMenuMousePressed

    private void btnUrlTabActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUrlTabActionPerformed
        // TODO add your handling code here:
        switchCard("url");
        setTabSelection(btnUrlTab);
    }//GEN-LAST:event_btnUrlTabActionPerformed

    private void btnAdvancedTabActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAdvancedTabActionPerformed
        switchCard("advanced");
        setTabSelection(btnAdvancedTab);
        try {
            this.getCategories();
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }//GEN-LAST:event_btnAdvancedTabActionPerformed

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
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Client.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Client.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Client.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Client.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                Client clientGUI = new Client(hostname, port);
                
                clientGUI.run();
            if (clientGUI.getPublicKey() == null)
                try {
                    clientGUI.sendPublicKeyRequest();
                } catch (IOException ex) {
                    Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
                }

            System.out.println("Connecting ...");
            while (clientGUI.getSecretKey() == null) {
                System.out.print("");
            }
            System.out.println("connected");
            clientGUI.setVisible(true);
            String input = "";
            Scanner stdIn;
            stdIn = new Scanner(System.in);

            System.out.println("CHOOSE AN OPTION");
            System.out.println("1/ Get product with id = 249953");
            System.out.println("2/ Filter products");
            System.out.println("3/ Get configurable products with product id = 249953");
            System.out.println("4/ Get product histories by URL in 11/2021");
            System.out.println("5/ Get product histories with product id = 249953 in 11/2021");
            System.out.println("6/ Get configurable product histories with product id = 249953 & cpId = 511074 in 11/2021");
            System.out.println("7/ Get reviews with product id = 249953");

//            while (true) {
//                System.out.print("Client input: ");
//                input = stdIn.nextLine();
//
//                try {
//                    if(input.equals("bye")) {
//                        stdIn.close();
//                        clientGUI.closeSocket();
//                        break;
//                    } else if (input.equals("1")) {
//                        Long productId = 249953L;
//                        clientGUI.getProduct(productId);
//                    } else if (input.equals("2")) {
//                        String productName = null;
//                        Long categoryId = 1815L;
//                        Long brandId = 246045L;
//                        Float ratingAverage = 4f;
//                        Long minPrice = 0L;
//                        Long maxPrice = 300000L;
//                        clientGUI.filterProducts(productName, categoryId, brandId, ratingAverage, minPrice, maxPrice);
//                    } else if (input.equals("3")) {
//                        clientGUI.getConfigurableProducts(249953L);
//                    } else if (input.equals("4")) {
//                        String url = "https://tiki.vn/day-cap-sac-micro-usb-anker-powerline-0-9m-a8132-hang-chinh-hang-p249953.html?spid=249956";
//                        int month = 11;
//                        int year = 2021;
//                        clientGUI.getProductHistories(url, month, year);
//                    } else if (input.equals("5")) {
//                        Long productId = 249953L;
//                        int month = 11;
//                        int year = 2021;
//                        clientGUI.getProductHistories(productId, month, year);
//                    } else if (input.equals("6")) {
//                        Long productId = 249953L;
//                        String option1 = "Xám";
//                        String option2 = null;
//                        String option3 = null;
//                        int month = 11;
//                        int year = 2021;
//                        clientGUI.getConfigurableProductHistories(productId, option1, option2, option3, month, year);
//                    } else if (input.equals("7")) {
//                        Long productId = 249953L;
//                        clientGUI.getReviews(productId);
//                    } else {
//                        clientGUI.getProduct(-5L);
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            } 
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAdvancedTab;
    private javax.swing.JButton btnUrlTab;
    private javax.swing.JLabel lblExit;
    private javax.swing.JLabel lblMaximize_Restore;
    private javax.swing.JLabel lblMinimize;
    private javax.swing.JLabel lblShow_HideMenu;
    private javax.swing.JPanel panelBody;
    private javax.swing.JPanel panelHeader;
    private javax.swing.JPanel pnlTabContent;
    private javax.swing.JPanel pnlTabs;
    // End of variables declaration//GEN-END:variables
}
