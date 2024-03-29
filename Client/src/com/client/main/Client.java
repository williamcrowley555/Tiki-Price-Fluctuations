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
    private static int port = 5004;

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
    
    public boolean isPnlUrl = false;

    PanelTimTheoURL pnlURL;
    PanelTimNangCao pnlAdvanced;

    public Client() {
    }

    public Client(String hostname, int port) {
        initComponents();
        invisibleMenuScrollBar(8);
        this.hostname = hostname;
        this.port = port;
        panelBody.repaint();
        panelBody.revalidate();

        initCardLayout();
        CustomWindow();
        addWindowClosingListener(this);
    }

    public void addWindowClosingListener(Client client) {
        client.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                try {
                    client.closeSocket();
                } catch (IOException ex) {
                    Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
                } catch (InterruptedException ex) {
                    Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
                }
                System.exit(0);
            }
        });
    }
    
    public void usePnlUrl()
    {
        this.isPnlUrl = true;
    }
    
    public void usePnlAdvanced()
    {
        this.isPnlUrl = false;
    }

    public void setReviewsList(ArrayList<LinkedHashMap<String, Object>> reviewsList) {
        if(isPnlUrl)
            this.pnlURL.setReviewsList(reviewsList);
        if(!isPnlUrl)
            this.pnlAdvanced.setReviewsList(reviewsList);
    }

    public void setTimelineList(ArrayList<LinkedHashMap<String, Object>> timelinesList) {
        if(isPnlUrl)
            this.pnlURL.setTimelinesList(timelinesList);
        if(!isPnlUrl)
            this.pnlAdvanced.setTimelinesList(timelinesList);
    }

    public void setCurrentProduct(LinkedHashMap<String, Object> product) {
        if(isPnlUrl)
            this.pnlURL.setCurrentProduct(product);
        if(!isPnlUrl)
            this.pnlAdvanced.setCurrentProduct(product);
    }

    public void updateLineChart(List<LinkedHashMap<String, Object>> productHistories) {
        if (productHistories == null || productHistories.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Không có dữ liệu lịch sử giá", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        ArrayList<String> dates = new ArrayList<String>();
        ArrayList<Integer> prices = new ArrayList<Integer>();
        int month = 1;
        int year = 2000;
        
        for (LinkedHashMap<String, Object> history : productHistories) {
            LinkedHashMap<String, Object> dateObj = (LinkedHashMap<String, Object>) history.get("date");
            dates.add(String.valueOf(dateObj.get("dayOfMonth")));
            month = (int) dateObj.get("monthValue");
            year = (int) dateObj.get("year");
            prices.add((int) history.get("price"));
        }
        
        if(isPnlUrl)
            this.pnlURL.showLineChart(month, year, dates, prices);
        if(!isPnlUrl)
            this.pnlAdvanced.showLineChart(month, year, dates, prices);
    }

    public void setOption(LinkedHashMap<String, Object> name, List<LinkedHashMap<String, Object>> option) throws IOException {
        if(!isPnlUrl)
            this.pnlAdvanced.setConfigurableProducts(name, option);
        if(isPnlUrl)
            this.pnlURL.setConfigurableProducts(name, option);
        
    }

    public void updateBrands(List<LinkedHashMap<String, Object>> recvBrands) {
        Map<Long, String> brands = new HashMap<>();

        for (LinkedHashMap<String, Object> brand : recvBrands) {
            brands.put(Long.valueOf((Integer) brand.get("id")), brand.get("name").toString());
        }
        if(!isPnlUrl)
            pnlAdvanced.updateBrandCheckbox(brands);
    }

    public void updateComboboxCategory(List<LinkedHashMap<String, Object>> categories) {
        ArrayList<String> name = new ArrayList<String>();
        ArrayList<Integer> idCate = new ArrayList<Integer>();
        for (LinkedHashMap<String, Object> category : categories) {
            name.add(String.valueOf(category.get("name")));
            idCate.add((Integer) (category.get("id")));
        }
        if(!isPnlUrl)
            this.pnlAdvanced.listNameCategory(name, idCate);
    }

    public void updateProductInfoURL(LinkedHashMap<String, Object> recvProduct) {
        if(isPnlUrl)
            this.pnlURL.updateProductInfo(recvProduct);
    }

    public void run() {
        try {
            socket = new Socket(hostname, port);
            out = new ObjectOutputStream(new BufferedOutputStream(socket.getOutputStream()));
            out.flush();

            readThread = new Thread(new ReadThread(this, this.socket));
            readThread.start();
        } catch (ConnectException conEx) {
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

        try {
            writeThread.join();
        } catch (InterruptedException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void setTable(List<LinkedHashMap<String, Object>> products)
    {   
        if(products == null || products.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Không tìm thấy bất kì sản phẩm nào", "Thông báo", JOptionPane.INFORMATION_MESSAGE);  
        }
        
        try {
            if(!isPnlUrl)
                pnlAdvanced.setTable(products);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Thao tác quá nhanh, hệ thống không kịp cập nhật", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    public void getProduct(Long productId) throws IOException {
        Map<String, Object> request = new HashMap<>();
        request.put("productId", productId);

        Message requestMsg = new Message(request, MessageType.GET_PRODUCT);
        sendMessage(requestMsg);
    }

    public void getBrandsByCategoryId(Long categoryId) throws IOException {
        Map<String, Object> request = new HashMap<>();
        request.put("categoryId", categoryId);

        Message requestMsg = new Message(request, MessageType.GET_BRANDS_BY_CATEGORY_ID);
        sendMessage(requestMsg);
    }

    public void filterProducts(String productName, Long categoryId, List<Long> brandIds, float ratingAverage, Long minPrice, Long maxPrice) throws IOException {
        Map<String, Object> request = new HashMap<>();
        if(productName != null)
            request.put("productName", productName);
        if(categoryId != null)
            request.put("categoryId", categoryId);
        request.put("brandIds", brandIds);
        request.put("ratingAverage", ratingAverage);
        if (minPrice != null) {
            request.put("minPrice", minPrice);
        }
        if (maxPrice != null) {
            request.put("maxPrice", maxPrice);
        }

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
        PanelTimTheoURL pnlURL1 = new PanelTimTheoURL(this);
        PanelTimNangCao pnlAdvancedURL1 = new PanelTimNangCao(this);
        
        this.pnlURL = pnlURL1;
        this.pnlAdvanced = pnlAdvancedURL1;
        
        pnlTabContent.add("url", pnlURL1);
        pnlTabContent.add("advanced", pnlAdvancedURL1);
        usePnlUrl();
        switchCard("url");
        setTabSelection(btnUrlTab);
    }

    public void switchCard(String name) {
        CardLayout card = (CardLayout) (pnlTabContent.getLayout());
        card.show(pnlTabContent, name);
    }

    public void setTabSelection(JButton button) {
        resetSelection();
        button.setBackground(new Color(77, 77, 77));
        button.setForeground(Color.white);

    }

    public void resetSelection() {
        btnUrlTab.setBorder(new LineBorder(Color.BLACK));
        btnUrlTab.setBackground(Color.white);
        btnUrlTab.setForeground(Color.BLACK);

        btnAdvancedTab.setBorder(new LineBorder(Color.BLACK));
        btnAdvancedTab.setBackground(Color.white);
        btnAdvancedTab.setForeground(Color.BLACK);
    }

    public void invisibleMenuScrollBar(int speed) {
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

    public void center() {
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation(dim.width / 2 - this.getSize().width / 2, dim.height / 2 - this.getSize().height / 2);
    }

    public void CustomWindow() {
        Color flatBlack = new Color(77, 77, 77);//Border
        this.setMinimumSize(new Dimension(1280, 720));
        //this.setSize(new Dimension(1280,720));
        this.getRootPane().setBorder(BorderFactory.createMatteBorder(0, 2, 2, 2, flatBlack));
        center();
        lblMinimize.setText("\u2014");
        lblExit.setText("X");
        lblMaximize_Restore.setText("\u2750");
    }

    public void Maximize_Restore(boolean state) {
        if (state) {
            lblMaximize_Restore.setText("\u2750");
            lblMaximize_Restore.setIcon(null);
            maximized = false;
            this.setPreferredSize(new Dimension(800, 600));
            this.setExtendedState(JFrame.NORMAL);
        } else {
            lblMaximize_Restore.setIcon(iconRestoreDown);
            lblMaximize_Restore.setText("");
            this.setExtendedState(JFrame.MAXIMIZED_BOTH);
            maximized = true;
        }
    }

    public void clearBrandPanel() {
        pnlAdvanced.clearBrandPanel();
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
        try {
            closeSocket();
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InterruptedException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.exit(0);
    }//GEN-LAST:event_lblExitMouseClicked

    private void lblMinimizeMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblMinimizeMouseClicked
        this.setState(JFrame.ICONIFIED);
    }//GEN-LAST:event_lblMinimizeMouseClicked

    private void panelHeaderMouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_panelHeaderMouseDragged
        setLocation(evt.getXOnScreen() - (getWidth() / 2), evt.getYOnScreen() - 10);
    }//GEN-LAST:event_panelHeaderMouseDragged

    private void lblShow_HideMenuMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblShow_HideMenuMousePressed

    }//GEN-LAST:event_lblShow_HideMenuMousePressed

    private void btnUrlTabActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUrlTabActionPerformed
        // TODO add your handling code here:
        usePnlUrl();
        switchCard("url");
        setTabSelection(btnUrlTab);
    }//GEN-LAST:event_btnUrlTabActionPerformed

    private void btnAdvancedTabActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAdvancedTabActionPerformed
        usePnlAdvanced();
        switchCard("advanced");
        setTabSelection(btnAdvancedTab);
        try {
            this.getCategories();
            pnlAdvanced.clearBrandPanel();
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }

    }//GEN-LAST:event_btnAdvancedTabActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        
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
       
        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                Client clientGUI = new Client(hostname, port);
                clientGUI.run();

                System.out.println("Connecting ...");
                while (clientGUI.getSecretKey() == null) {
                    System.out.print("");
                }

                System.out.println("Connected");
                clientGUI.setVisible(true);
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
