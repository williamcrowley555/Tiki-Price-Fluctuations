/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.client.gui;


import com.client.gui.others.MyScrollBarUI;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.border.MatteBorder;
import javax.swing.plaf.basic.BasicArrowButton;
import javax.swing.plaf.basic.BasicComboBoxUI;
import javax.swing.plaf.basic.BasicComboPopup;
import javax.swing.plaf.basic.ComboPopup;
import java.util.Vector;
import java.util.stream.Collectors;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollBar;
import javax.swing.SwingUtilities;
import javax.swing.border.LineBorder;


/**
 *
 * @author Hi
 */
public class ReviewGUI extends javax.swing.JFrame {
    private String action;
    private ArrayList<LinkedHashMap<String, Object>> reviewList;
    public ReviewGUI() {
        initComponents();
        this.action = action;
        CustomWindow();
        
        this.setVisible(true);
    }
    public ReviewGUI(Float averageRating,  ArrayList<LinkedHashMap<String, Object>> reviewList, ArrayList<LinkedHashMap<String, Object>> timelineList) {
        
        initComponents();
        initFilterIcon();
        for (LinkedHashMap<String, Object> review : reviewList)
        {   
            if (review != null)
            {   
                for(LinkedHashMap<String, Object> timeline : timelineList){
                    if (timeline != null && review.get("id").equals(timeline.get("reviewId"))) {
                        review.put("review_created_date", timeline.get("review_created_date"));
                        review.put("date_used", timeline.get("content"));
                    }
                }
                ReviewComponent reviewComp = new ReviewComponent(
                    (String) review.get("title"), 
                     Float.valueOf((int) review.get("rating")), 
                    (String) review.get("content"), 
                    (String) review.get("date_used"),
                    (String) review.get("review_created_date"),
                    (String) review.get("imageUrl"),
                    (String) review.get("fullName")   
                );
                
                reviews.add(reviewComp);
                reviews.revalidate();
            }
        }
        
        this.reviewList = reviewList;

        lblReviewsCount.setText("Sản phẩm có: " + reviewList.size() + " đánh giá");
        lblAverageRating.setText(averageRating.toString());
        this.action = action;
        CustomWindow();
        
        //scroll lên đầu trang sau khi thêm các review
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                reviewScrollPane.getVerticalScrollBar().setValue(0);

            }
        });
        
        reviewScrollPane.getVerticalScrollBar().setUI(new MyScrollBarUI());
        reviewScrollPane.getVerticalScrollBar().setUnitIncrement(16); //scroll speed
        this.setVisible(true); 
    }
    
    public ArrayList<LinkedHashMap<String, Object>> filterReview(int selectedStar){
       ArrayList<LinkedHashMap<String, Object>> fiteredReviewList = new ArrayList<>();
       for (LinkedHashMap<String, Object> review : this.reviewList){
           if ((int) review.get("rating") == selectedStar)
           {
               fiteredReviewList.add(review);
           }
       }
       
       lblReviewsCount.setText("Sản phẩm có: " + fiteredReviewList.size() + " đánh giá");
       return fiteredReviewList;
    }
    public void setReviews(ArrayList<LinkedHashMap<String, Object>> reviewList){
        if (reviewList.isEmpty()) JOptionPane.showMessageDialog(this, "Không có review nào", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
        resetReviewsPanel();
        for (LinkedHashMap<String, Object> review : reviewList)
        {
            if (review != null)
            {
               ReviewComponent reviewComp = new ReviewComponent(
                    (String) review.get("title"), 
                     Float.valueOf((int) review.get("rating")), 
                    (String) review.get("content"), 
                    (String) review.get("date_used"),
                    (String) review.get("review_created_date"), 
                    (String) review.get("imageUrl"),
                    (String) review.get("fullName")
                );
                reviews.add(reviewComp);
                reviews.revalidate();
            }
        }
        
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                reviewScrollPane.getVerticalScrollBar().setValue(0);

            }
        });
        reviews.setVisible(true); 
    }
    
    public void resetReviewsPanel(){
        reviews.removeAll();
        reviews.revalidate();
    }
    
    public void resetFilterSelection(){
       lblOneStar.setBackground(new Color(240,240,240));
       lblOneStar.setOpaque(true);
       lblTwoStar.setBackground(new Color(240,240,240));
       lblTwoStar.setOpaque(true);
       lblThreeStar.setBackground(new Color(240,240,240));
       lblThreeStar.setOpaque(true);
       lblFourStar.setBackground(new Color(240,240,240));
       lblFourStar.setOpaque(true);
       lblFiveStar.setBackground(new Color(240,240,240));
       lblFiveStar.setOpaque(true);
    }
    public void initFilterIcon(){
        
        ImageIcon iconOneStar = new ImageIcon(getClass().getResource("/com/client/img/small_star.png"));
        lblOneStar.setIcon(iconOneStar);
        lblOneStar.setBackground(new Color(240,240,240));
        lblOneStar.setOpaque(true);
        
        setMultipleStarsIcon(iconOneStar, 2, lblTwoStar);
        setMultipleStarsIcon(iconOneStar, 3, lblThreeStar);
        setMultipleStarsIcon(iconOneStar, 4, lblFourStar);
        setMultipleStarsIcon(iconOneStar, 5, lblFiveStar);
    }
    
    public void setMultipleStarsIcon(ImageIcon icon, int times, JLabel label){
        label.setLayout(new BoxLayout(label, BoxLayout.X_AXIS));
       
        for (int i = 0; i < times; i++){
            JLabel iconLabel = new JLabel();
            
            iconLabel.setIcon(icon);
            label.add(iconLabel);
        }
        label.setBackground(new Color(240,240,240));
        label.setOpaque(true);
    }
    
    public void center()
    {
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation(dim.width/2-this.getSize().width/2, dim.height/2-this.getSize().height/2);
    }
    
    public void CustomWindow()
    {   
        Color flatBlack = new Color(77,77,77);  
        
        this.getRootPane().setBorder(BorderFactory.createMatteBorder(0,1,1,1, flatBlack));   
        center();
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

        panelHeader = new javax.swing.JPanel();
        lblMinimize = new javax.swing.JLabel();
        lblExit = new javax.swing.JLabel();
        pnlBody = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        lblAverageRating = new javax.swing.JLabel();
        lblReviewsCount = new javax.swing.JLabel();
        lblTwoStar = new javax.swing.JLabel();
        lblThreeStar = new javax.swing.JLabel();
        lblFourStar = new javax.swing.JLabel();
        lblFiveStar = new javax.swing.JLabel();
        lblOneStar = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        lblReset = new javax.swing.JLabel();
        pnlReviews = new javax.swing.JPanel();
        reviewScrollPane = new javax.swing.JScrollPane();
        reviews = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(255, 255, 255));
        setUndecorated(true);

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
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lblMinimize, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(lblExit, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        panelHeaderLayout.setVerticalGroup(
            panelHeaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(lblMinimize, javax.swing.GroupLayout.DEFAULT_SIZE, 40, Short.MAX_VALUE)
            .addComponent(lblExit, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pnlBody.setBackground(new java.awt.Color(255, 255, 255));
        pnlBody.setLayout(new java.awt.BorderLayout());

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel1.setText("Đánh giá của sản phẩm:");

        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/client/img/star.png"))); // NOI18N

        lblAverageRating.setFont(new java.awt.Font("Tahoma", 0, 36)); // NOI18N
        lblAverageRating.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblAverageRating.setText("5");

        lblReviewsCount.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        lblReviewsCount.setText("review count");

        lblTwoStar.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTwoStar.setBorder(new javax.swing.border.MatteBorder(null));
        lblTwoStar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblTwoStarMouseClicked(evt);
            }
        });

        lblThreeStar.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblThreeStar.setBorder(new javax.swing.border.MatteBorder(null));
        lblThreeStar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblThreeStarMouseClicked(evt);
            }
        });

        lblFourStar.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblFourStar.setBorder(new javax.swing.border.MatteBorder(null));
        lblFourStar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblFourStarMouseClicked(evt);
            }
        });

        lblFiveStar.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblFiveStar.setBorder(new javax.swing.border.MatteBorder(null));
        lblFiveStar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblFiveStarMouseClicked(evt);
            }
        });

        lblOneStar.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblOneStar.setBorder(new javax.swing.border.MatteBorder(null));
        lblOneStar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblOneStarMouseClicked(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Arial", 0, 16)); // NOI18N
        jLabel3.setText("Lọc đánh giá");

        lblReset.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/client/img/refresh.png"))); // NOI18N
        lblReset.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblResetMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 251, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(lblAverageRating, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel2)
                        .addGap(24, 24, 24))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(lblReviewsCount, javax.swing.GroupLayout.PREFERRED_SIZE, 187, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(lblOneStar, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(lblTwoStar, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(lblThreeStar, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(12, 12, 12)
                        .addComponent(lblFourStar, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(lblFiveStar, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(lblReset, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addContainerGap())))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(lblAverageRating, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(lblTwoStar, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblOneStar, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblReset, javax.swing.GroupLayout.DEFAULT_SIZE, 31, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(lblReviewsCount, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(lblFourStar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(lblFiveStar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(lblThreeStar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );

        pnlBody.add(jPanel1, java.awt.BorderLayout.PAGE_START);

        pnlReviews.setPreferredSize(new java.awt.Dimension(400, 400));

        reviewScrollPane.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        reviewScrollPane.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        reviews.setLayout(new javax.swing.BoxLayout(reviews, javax.swing.BoxLayout.PAGE_AXIS));
        reviewScrollPane.setViewportView(reviews);

        javax.swing.GroupLayout pnlReviewsLayout = new javax.swing.GroupLayout(pnlReviews);
        pnlReviews.setLayout(pnlReviewsLayout);
        pnlReviewsLayout.setHorizontalGroup(
            pnlReviewsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(reviewScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 838, Short.MAX_VALUE)
        );
        pnlReviewsLayout.setVerticalGroup(
            pnlReviewsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(reviewScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 560, Short.MAX_VALUE)
        );

        pnlBody.add(pnlReviews, java.awt.BorderLayout.CENTER);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelHeader, javax.swing.GroupLayout.DEFAULT_SIZE, 838, Short.MAX_VALUE)
            .addComponent(pnlBody, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(panelHeader, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(pnlBody, javax.swing.GroupLayout.DEFAULT_SIZE, 687, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );

        pack();
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

    private void lblOneStarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblOneStarMouseClicked
        ArrayList<LinkedHashMap<String, Object>> fiteredReviewList = filterReview(1);
        setReviews(fiteredReviewList);
        resetFilterSelection();
        lblOneStar.setBackground(new Color(77,77,77)); 
        lblOneStar.setOpaque(true);
    }//GEN-LAST:event_lblOneStarMouseClicked

    private void lblTwoStarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblTwoStarMouseClicked
        ArrayList<LinkedHashMap<String, Object>> fiteredReviewList = filterReview(2);
        setReviews(fiteredReviewList);
        resetFilterSelection();
        lblTwoStar.setBackground(new Color(77,77,77));
        lblTwoStar.setOpaque(true);
    }//GEN-LAST:event_lblTwoStarMouseClicked

    private void lblThreeStarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblThreeStarMouseClicked
       ArrayList<LinkedHashMap<String, Object>> fiteredReviewList = filterReview(3);
       setReviews(fiteredReviewList);
       resetFilterSelection();
       lblThreeStar.setBackground(new Color(77,77,77));
       lblThreeStar.setOpaque(true);
    }//GEN-LAST:event_lblThreeStarMouseClicked

    private void lblFourStarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblFourStarMouseClicked
       ArrayList<LinkedHashMap<String, Object>> fiteredReviewList = filterReview(4);
       setReviews(fiteredReviewList);
        resetFilterSelection();
       lblFourStar.setBackground(new Color(77,77,77));
       lblFourStar.setOpaque(true);
    }//GEN-LAST:event_lblFourStarMouseClicked

    private void lblFiveStarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblFiveStarMouseClicked
       ArrayList<LinkedHashMap<String, Object>> fiteredReviewList = filterReview(5);
       setReviews(fiteredReviewList);
       resetFilterSelection();
       lblFiveStar.setBackground(new Color(77,77,77));
       lblFiveStar.setOpaque(true);
    }//GEN-LAST:event_lblFiveStarMouseClicked

    private void lblResetMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblResetMouseClicked
      setReviews(this.reviewList);
       resetFilterSelection();
      lblReviewsCount.setText("Sản phẩm có: " + this.reviewList.size() + " đánh giá");
    }//GEN-LAST:event_lblResetMouseClicked

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
            java.util.logging.Logger.getLogger(ReviewGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ReviewGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ReviewGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ReviewGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
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
                new ReviewGUI().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JLabel lblAverageRating;
    private javax.swing.JLabel lblExit;
    private javax.swing.JLabel lblFiveStar;
    private javax.swing.JLabel lblFourStar;
    private javax.swing.JLabel lblMinimize;
    private javax.swing.JLabel lblOneStar;
    private javax.swing.JLabel lblReset;
    private javax.swing.JLabel lblReviewsCount;
    private javax.swing.JLabel lblThreeStar;
    private javax.swing.JLabel lblTwoStar;
    private javax.swing.JPanel panelHeader;
    private javax.swing.JPanel pnlBody;
    private javax.swing.JPanel pnlReviews;
    private javax.swing.JScrollPane reviewScrollPane;
    private javax.swing.JPanel reviews;
    // End of variables declaration//GEN-END:variables

}
