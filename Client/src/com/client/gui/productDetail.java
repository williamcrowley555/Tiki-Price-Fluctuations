/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.client.gui;

import com.client.gui.others.MyScrollBarUI;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.font.TextAttribute;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

/**
 *
 * @author Hi
 */
public class productDetail extends javax.swing.JFrame {

    /**
     * Creates new form productDetail
     */
    public productDetail() {
        initComponents();
        CustomWindow();
    }
    
     public productDetail(LinkedHashMap<String, Object> recvProduct) {
        initComponents();
        CustomWindow();
        updateProductInfo(recvProduct);
        myTextArea();
        productNameScrollPane.getVerticalScrollBar().setUI(new MyScrollBarUI());
        descriptionScrollPane.getVerticalScrollBar().setUI(new MyScrollBarUI());
    }
    
    public void myTextArea()
    {
        txtTenSP.setWrapStyleWord(true);
        txtTenSP.setLineWrap(true);
        productNameScrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(0, 0));
        txtTenSP.setCaretPosition(0); // scroll to top
        
        txtMota.setWrapStyleWord(true);
        txtMota.setLineWrap(true);
        descriptionScrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(0, 0));
        txtMota.setCaretPosition(0); // scroll to top
    }
    
    public void center()
    {
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation(dim.width/2-this.getSize().width/2, dim.height/2-this.getSize().height/2);
    } 
     
    public void updateProductInfo(LinkedHashMap<String, Object> recvProduct){
            // Remove old image
            pnlPic.removeAll();
            pnlPic.revalidate();
            if (recvProduct == null){
                this.dispose();
                return;
            }
            txtTenSP.setText((String) recvProduct.get("name"));
            String path = (String) recvProduct.get("imageUrl");
            Long id =  Long.valueOf((int) recvProduct.get("id"));
            URL url = null;
            Image image = null;
            try {
                url = new URL(path);
                image = ImageIO.read(url);
            } catch (MalformedURLException ex) {
                System.out.println("Malformed URL");
            } catch (IOException iox) {
                System.out.println("Can not load file");
            }
            
            //Resize img to fit jpanel
            Image scaledImage = image.getScaledInstance(
                    pnlPic.getWidth(),
                    pnlPic.getHeight(),
                    Image.SCALE_SMOOTH
            );
            
            JLabel label = new JLabel(new ImageIcon(scaledImage));
            pnlPic.add(label, BorderLayout.CENTER);
            pnlPic.revalidate();
            pnlPic.setVisible(true);
            
            if ((int) recvProduct.get("discount_rate") == 0)
            {
                lblPrice.setText(String.valueOf(recvProduct.get("price")) + " Đ");
                lblOriginalPrice.setVisible(false);
            } else {
                Font font = new Font("Arial", Font.PLAIN, 16);
                Map  attributes = font.getAttributes();
                attributes.put(TextAttribute.STRIKETHROUGH, TextAttribute.STRIKETHROUGH_ON);
                Font newFont = new Font(attributes); 
                lblOriginalPrice.setFont(newFont);
                lblOriginalPrice.setText(String.valueOf(recvProduct.get("original_price")) + " Đ");
                lblPrice.setText(String.valueOf(recvProduct.get("price")) + " Đ");
            }
            
            txtMota.setText((String) recvProduct.get("description"));
    } 
    
    public void CustomWindow()
    {   
        Color flatBlack = new Color(77,77,77);  
        this.getRootPane().setBorder(BorderFactory.createMatteBorder(0,1,1,1, flatBlack));   
        lblMinimize.setText("\u2014");
        lblExit.setText("X");
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
        panelHeader = new javax.swing.JPanel();
        lblMinimize = new javax.swing.JLabel();
        lblExit = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        pnlPic = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        descriptionScrollPane = new javax.swing.JScrollPane();
        txtMota = new javax.swing.JTextArea();
        jLabel8 = new javax.swing.JLabel();
        productNameScrollPane = new javax.swing.JScrollPane();
        txtTenSP = new javax.swing.JTextArea();
        jPanel5 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        lblPrice = new javax.swing.JLabel();
        lblOriginalPrice = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setUndecorated(true);
        setPreferredSize(new java.awt.Dimension(787, 360));

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setPreferredSize(new java.awt.Dimension(848, 40));
        jPanel1.setLayout(new java.awt.BorderLayout());

        panelHeader.setBackground(new java.awt.Color(77, 77, 77));
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

        javax.swing.GroupLayout panelHeaderLayout = new javax.swing.GroupLayout(panelHeader);
        panelHeader.setLayout(panelHeaderLayout);
        panelHeaderLayout.setHorizontalGroup(
            panelHeaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelHeaderLayout.createSequentialGroup()
                .addContainerGap(753, Short.MAX_VALUE)
                .addComponent(lblMinimize, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(lblExit, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        panelHeaderLayout.setVerticalGroup(
            panelHeaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(lblMinimize, javax.swing.GroupLayout.DEFAULT_SIZE, 40, Short.MAX_VALUE)
            .addComponent(lblExit, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        jPanel1.add(panelHeader, java.awt.BorderLayout.CENTER);

        getContentPane().add(jPanel1, java.awt.BorderLayout.PAGE_START);

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setLayout(new java.awt.BorderLayout());

        jPanel3.setPreferredSize(new java.awt.Dimension(300, 300));
        jPanel3.setLayout(new java.awt.BorderLayout());

        pnlPic.setBackground(new java.awt.Color(255, 255, 255));
        pnlPic.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 0, 0, 1, new java.awt.Color(0, 0, 0)));
        pnlPic.setPreferredSize(new java.awt.Dimension(300, 199));
        pnlPic.setLayout(new java.awt.BorderLayout());
        jPanel3.add(pnlPic, java.awt.BorderLayout.CENTER);

        jPanel2.add(jPanel3, java.awt.BorderLayout.LINE_START);

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));

        descriptionScrollPane.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        txtMota.setEditable(false);
        txtMota.setColumns(20);
        txtMota.setFont(new java.awt.Font("Arial", 0, 16)); // NOI18N
        txtMota.setLineWrap(true);
        txtMota.setRows(5);
        txtMota.setWrapStyleWord(true);
        txtMota.setFocusable(false);
        descriptionScrollPane.setViewportView(txtMota);

        jLabel8.setFont(new java.awt.Font("Arial", 0, 22)); // NOI18N
        jLabel8.setText("Mô tả:");

        productNameScrollPane.setBorder(null);
        productNameScrollPane.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        txtTenSP.setEditable(false);
        txtTenSP.setColumns(20);
        txtTenSP.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        txtTenSP.setLineWrap(true);
        txtTenSP.setRows(5);
        txtTenSP.setText("abc");
        txtTenSP.setFocusable(false);
        productNameScrollPane.setViewportView(txtTenSP);

        jPanel5.setBackground(new java.awt.Color(255, 255, 255));
        jPanel5.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 15));

        jLabel1.setFont(new java.awt.Font("Arial", 0, 22)); // NOI18N
        jLabel1.setText("Giá:");
        jLabel1.setPreferredSize(new java.awt.Dimension(60, 29));
        jPanel5.add(jLabel1);

        lblPrice.setFont(new java.awt.Font("Arial", 0, 24)); // NOI18N
        lblPrice.setForeground(new java.awt.Color(0, 204, 51));
        lblPrice.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblPrice.setText("80000 Đ");
        jPanel5.add(lblPrice);

        lblOriginalPrice.setFont(new java.awt.Font("Arial", 0, 24)); // NOI18N
        lblOriginalPrice.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lblOriginalPrice.setText("80000Đ");
        jPanel5.add(lblOriginalPrice);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(productNameScrollPane, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 531, Short.MAX_VALUE)
                    .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(descriptionScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 531, Short.MAX_VALUE)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(productNameScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(15, 15, 15)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel8)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 9, Short.MAX_VALUE)
                .addComponent(descriptionScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jPanel2.add(jPanel4, java.awt.BorderLayout.CENTER);

        getContentPane().add(jPanel2, java.awt.BorderLayout.CENTER);

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void lblMinimizeMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblMinimizeMouseClicked
        this.setState(JFrame.ICONIFIED);
    }//GEN-LAST:event_lblMinimizeMouseClicked

    private void lblExitMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblExitMouseClicked
        this.dispose();
    }//GEN-LAST:event_lblExitMouseClicked

    private void panelHeaderMouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_panelHeaderMouseDragged
        setLocation (evt.getXOnScreen()-(getWidth()/2),evt.getYOnScreen()-10);
    }//GEN-LAST:event_panelHeaderMouseDragged

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
            java.util.logging.Logger.getLogger(productDetail.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(productDetail.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(productDetail.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(productDetail.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new productDetail().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane descriptionScrollPane;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JLabel lblExit;
    private javax.swing.JLabel lblMinimize;
    private javax.swing.JLabel lblOriginalPrice;
    private javax.swing.JLabel lblPrice;
    private javax.swing.JPanel panelHeader;
    private javax.swing.JPanel pnlPic;
    private javax.swing.JScrollPane productNameScrollPane;
    private javax.swing.JTextArea txtMota;
    private javax.swing.JTextArea txtTenSP;
    // End of variables declaration//GEN-END:variables
}
