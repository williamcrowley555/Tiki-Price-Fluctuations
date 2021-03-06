/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.client.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

/**
 *
 * @author Hi
 */
public class ReviewComponent extends javax.swing.JPanel {
    String title;
    float rating;
    String content;
    String timeUsed;
    String reviewDate;
    String imageUrl;
    String fullName;
    /**
     * Creates new form reviewComponent
     */
    
    public ReviewComponent(String title, float rating, String content, String timeUsed, String reviewDate, String imageUrl, String fullName) {
        
        initComponents();
        
        this.title = title;
        this.rating = rating;
        this.content = content;
        this.timeUsed = timeUsed;
        this.reviewDate = reviewDate;
        this.imageUrl = imageUrl;
        this.fullName = fullName;
        setReviewData();
    }   
    
    public void setReviewData()
    {
        lblTitle.setText(this.title);
        lblRating.setText(String.valueOf(this.rating));
        if (fullName != null && !fullName.isEmpty()){
            lblReviewBy.setText("Đánh giá bởi: " + fullName);
        } else 
            lblReviewBy.setVisible(false);
        if (!content.trim().isEmpty())
        {
            JTextArea content = new JTextArea(this.content);
            content.setLineWrap(true);
            content.setWrapStyleWord(true);
            content.setEditable(false);
            content.setFocusable(false);
            content.setBorder(new EmptyBorder(10,10,10,10));
            Font contentFont =  new Font("Arial", Font.PLAIN, 14);
            content.setFont(contentFont);
            pnlContent.add(content);
            pnlContent.revalidate();
        } else {
            pnlContent.setBorder(new EmptyBorder(1,1,1,1));
        }
       
        lblTimeUsed.setText(this.timeUsed);
        lblReviewDate.setText(this.reviewDate == null ? "Ngày đánh giá: Không có dữ liệu" : "Ngày đánh giá: " + this.reviewDate);
        
        if (this.imageUrl != null)
        {
            setPicture(this.imageUrl);
        }
        else
            pnlPic.setVisible(false);
        
    }
    
    public void setPicture(String imageUrl){
            URL url = null;
            Image image = null;
            try {
                url = new URL(imageUrl);
                image = ImageIO.read(url);
            } catch (MalformedURLException ex) {
                System.out.println("Malformed URL");
            } catch (IOException iox) {
                System.out.println("Can not load file");
            }
            
            //Resize img to fit jpanel
            Image scaledImage = image.getScaledInstance(
                    200,
                    200,
                    Image.SCALE_SMOOTH
            );
            
            JLabel label = new JLabel(new ImageIcon(scaledImage));
            pnlPic.add(label, BorderLayout.CENTER);
            pnlPic.revalidate();
            pnlPic.setVisible(true);
    }
     

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        contentScrollPane = new javax.swing.JScrollPane();
        txtContent = new javax.swing.JTextArea();
        jPanel2 = new javax.swing.JPanel();
        lblTitle = new javax.swing.JLabel();
        lblRating = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        lblTimeUsed = new javax.swing.JLabel();
        lblReviewDate = new javax.swing.JLabel();
        pnlPic = new javax.swing.JPanel();
        pnlContent = new javax.swing.JPanel();
        lblReviewBy = new javax.swing.JLabel();

        contentScrollPane.setBorder(new javax.swing.border.MatteBorder(null));

        txtContent.setEditable(false);
        txtContent.setColumns(20);
        txtContent.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txtContent.setRows(5);
        txtContent.setFocusable(false);
        contentScrollPane.setViewportView(txtContent);

        setLayout(new java.awt.BorderLayout());

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));

        lblTitle.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        lblTitle.setText("Title");

        lblRating.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        lblRating.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblRating.setText("5");

        jLabel3.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/client/img/small_star.png"))); // NOI18N

        lblTimeUsed.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        lblTimeUsed.setText("Đã dùng 1 tháng");

        lblReviewDate.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        lblReviewDate.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblReviewDate.setText("12/12/2021");

        pnlPic.setLayout(new java.awt.BorderLayout());

        pnlContent.setBackground(new java.awt.Color(255, 255, 255));
        pnlContent.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        pnlContent.setLayout(new java.awt.BorderLayout());

        lblReviewBy.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        lblReviewBy.setText("Đánh giá bởi: Nguyễn văn A");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(lblReviewDate, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(pnlContent, javax.swing.GroupLayout.PREFERRED_SIZE, 766, javax.swing.GroupLayout.PREFERRED_SIZE)))
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(pnlPic, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(lblRating, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel3)
                        .addGap(18, 18, 18)
                        .addComponent(lblTimeUsed, javax.swing.GroupLayout.PREFERRED_SIZE, 245, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(lblTitle, javax.swing.GroupLayout.PREFERRED_SIZE, 824, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblReviewBy, javax.swing.GroupLayout.PREFERRED_SIZE, 173, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addComponent(lblTitle, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblReviewBy, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 38, Short.MAX_VALUE)
                    .addComponent(lblRating, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lblTimeUsed, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lblReviewDate, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(pnlContent, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnlPic, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(31, Short.MAX_VALUE))
        );

        add(jPanel2, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane contentScrollPane;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JLabel lblRating;
    private javax.swing.JLabel lblReviewBy;
    private javax.swing.JLabel lblReviewDate;
    private javax.swing.JLabel lblTimeUsed;
    private javax.swing.JLabel lblTitle;
    private javax.swing.JPanel pnlContent;
    private javax.swing.JPanel pnlPic;
    private javax.swing.JTextArea txtContent;
    // End of variables declaration//GEN-END:variables
}
