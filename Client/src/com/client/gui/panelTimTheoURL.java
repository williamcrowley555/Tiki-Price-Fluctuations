/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.client.gui;

import com.client.gui.others.MyComboBoxEditor;
import com.client.gui.others.MyComboBoxRenderer;
import com.client.main.Client;
import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.JSpinner;
import javax.swing.border.MatteBorder;
import javax.swing.plaf.basic.BasicArrowButton;
import javax.swing.plaf.basic.BasicComboBoxUI;
import javax.swing.plaf.basic.BasicComboPopup;
import javax.swing.plaf.basic.BasicSpinnerUI;
import javax.swing.plaf.basic.ComboPopup;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.StandardChartTheme;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.labels.ItemLabelAnchor;
import org.jfree.chart.labels.ItemLabelPosition;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.labels.StandardXYToolTipGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.chart.renderer.category.StandardBarPainter;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.chart.ui.RectangleInsets;
import org.jfree.chart.ui.TextAnchor;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

/**
 *
 * @author Hi
 */

public class panelTimTheoURL extends javax.swing.JPanel {
    private reviewGUI popUp = null;
    /**
     * Creates new form panelTimTheoURL
     */
    public static String searchURL = ""; 
    Client main;
    ArrayList<String> dates = new ArrayList<String>();
    ArrayList<Integer> prices = new ArrayList<Integer>();
    String productName = "Laptop A";
    int month = 11;
    int year = 2021;
    ImageIcon checkedRadio = new ImageIcon(getClass().getResource("/com/client/img/checked_radio.png"));
    ImageIcon unCheckedRadio = new ImageIcon(getClass().getResource("/com/client/img/unchecked_radio.png"));
    public panelTimTheoURL(Client main) {
        initComponents();
        for(int i = 1; i < 31; i ++){
            dates.add(String.valueOf(i));
        }
        for (int i = 1; i < 31; i++){
            prices.add(i +100 );
        }
        this.main = main;
       
        showLineChart(productName, month, year, dates, prices);
        customMonthYearChooser();
        
    }
    
    public void customMonthYearChooser(){
        javax.swing.JComboBox boxMonth = (javax.swing.JComboBox) monthChooser.getComboBox();
        boxMonth = myComboBox(boxMonth, new Color(77,77,77));
        boxMonth.setLightWeightPopupEnabled (false);
        hideSpinnerArrow((javax.swing.JSpinner) monthChooser.getSpinner());
        hideSpinnerArrow((javax.swing.JSpinner) yearChooser.getSpinner());
    }
    
    
    
    public void customChart( JFreeChart linechart){
        StandardChartTheme theme = new StandardChartTheme(linechart.toString());
        theme.setTitlePaint( Color.decode( "#4572a7" ) );
        theme.setRangeGridlinePaint( Color.decode("#C0C0C0"));
        theme.setPlotBackgroundPaint( Color.WHITE);
        theme.setChartBackgroundPaint( Color.WHITE );
        theme.setGridBandPaint( Color.red );
        theme.setAxisOffset( new RectangleInsets(0,0,0,0) );
        theme.setBarPainter(new StandardBarPainter());
        theme.setAxisLabelPaint( Color.decode("#000000")  );
        theme.apply( linechart );
        linechart.getCategoryPlot().setOutlineVisible( false );
        linechart.getCategoryPlot().getRangeAxis().setAxisLineVisible( false );
        linechart.getCategoryPlot().getRangeAxis().setTickMarksVisible( false );
        linechart.getCategoryPlot().setRangeGridlineStroke( new BasicStroke() );
        linechart.getCategoryPlot().getRangeAxis().setTickLabelPaint( Color.decode("#000000") );
        linechart.getCategoryPlot().getDomainAxis().setTickLabelPaint( Color.decode("#000000") );
        linechart.setTextAntiAlias( true );
        linechart.setAntiAlias( true );
        linechart.getCategoryPlot().getRenderer().setSeriesPaint( 0, Color.decode( "#4572a7" ));
        //linechart.getCategoryPlot().getRenderer().setSeriesShape(0, new Rectangle(2, 2), true);
        linechart.getCategoryPlot().getRenderer().setDefaultShape(new Rectangle(2, 2), true);
        linechart.getCategoryPlot().getRenderer().setSeriesStroke(0, new BasicStroke(2.0f)); 
    }
    
     
    public void showLineChart(String productName, int month, int year, ArrayList<String> dates, ArrayList<Integer> prices ){
        
        //create dataset for the graph
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        for (int i = 0; i < prices.size(); i++){
            dataset.setValue(prices.get(i), "Giá", dates.get(i).toString());
        }
        //create chart
        JFreeChart linechart = ChartFactory.createLineChart("Lịch sử giá " + productName + " tháng "
                                                            + month + " / " + year ,"Ngày","Giá (vnd)", 
                dataset, PlotOrientation.VERTICAL, true,true,false);
      
 
        
        LineAndShapeRenderer renderer1 = new LineAndShapeRenderer(true, false);
        linechart.getCategoryPlot().setRenderer(renderer1);
        
        // date rotate
//        CategoryAxis domainAxis = linechart.getCategoryPlot().getDomainAxis();
//        domainAxis.setCategoryLabelPositions(CategoryLabelPositions.UP_45);
        renderer1.setDefaultItemLabelGenerator(new StandardCategoryItemLabelGenerator("{2}", NumberFormat.getNumberInstance()));
        renderer1.setDefaultPositiveItemLabelPosition(new ItemLabelPosition( 
             ItemLabelAnchor.CENTER, TextAnchor.BASELINE_RIGHT, TextAnchor.BASELINE_RIGHT, 
             - Math.PI / 8)); 
        
        renderer1.setDefaultItemLabelsVisible(true);
        
        customChart(linechart); 
        ChartPanel lineChartPanel = new ChartPanel(linechart);
        pnlChart.removeAll();
        pnlChart.add(lineChartPanel, BorderLayout.CENTER);
        pnlChart.validate();
        pnlChart.setVisible(true);
    }
    
    public void updateProductInfo(String productName, String productImg){
        try {
            txtTenSP.setText(productName);
            String path =productImg;
            System.out.println("Get Image from " + path);
            URL url = new URL(path);
            BufferedImage image = ImageIO.read(url);
            JLabel label = new JLabel(new ImageIcon(image));
            
        } catch (IOException ex) {
            System.err.println("Image Source Not Found");
        }
       
    }
    
    public void createImage(String str, JLabel label) {
        URL url = null;
        try {
            url = new URL(str);
        } catch (MalformedURLException ex) {
            Logger.getLogger(panelTimTheoURL.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            BufferedImage image = ImageIO.read(url);
            label.setIcon(new ImageIcon(image));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public JComboBox myComboBox(JComboBox box, Color color)
    {   
        box.setRenderer(new MyComboBoxRenderer());
        box.setEditor(new MyComboBoxEditor());
        
        box.setUI(new BasicComboBoxUI() 
        {
            @Override
            protected ComboPopup createPopup() 
            {
                BasicComboPopup basicComboPopup = new BasicComboPopup(comboBox);
                basicComboPopup.setBorder(new MatteBorder(2,2,2,2,color));
                return basicComboPopup;
            }
            
            @Override 
            protected JButton createArrowButton() 
            {
                Color matteGrey = new Color(223,230,233);
                Color flatGreen = new Color(26, 188, 156);
        
                BasicArrowButton custom = new BasicArrowButton(
                BasicArrowButton.SOUTH, null, null, Color.WHITE, null);
                custom.setBorder(new MatteBorder(0,0,0,0,flatGreen));
                return custom;
            }
        }); 

       return box;
    }
    
    public void hideSpinnerArrow(JSpinner spinner) {
        spinner.setUI(new BasicSpinnerUI() {
            protected Component createNextButton() {
                return null;
            }

            protected Component createPreviousButton() {
                return null;
            }
        });
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        colorGroup = new javax.swing.ButtonGroup();
        sizeGroup = new javax.swing.ButtonGroup();
        pnlSearch = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        btnTimKiem = new javax.swing.JButton();
        btnXemReview = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        txtTimTheoURL = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        monthChooser = new com.toedter.calendar.JMonthChooser();
        yearChooser = new com.toedter.calendar.JYearChooser();
        jPanel7 = new javax.swing.JPanel();
        pnlLeft = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtTenSP = new javax.swing.JTextArea();
        jLabel6 = new javax.swing.JLabel();
        lblProductPic = new javax.swing.JLabel();
        pnlChart = new javax.swing.JPanel();

        setBackground(new java.awt.Color(255, 255, 255));

        pnlSearch.setBackground(new java.awt.Color(255, 255, 255));
        pnlSearch.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 0, 1, 0, new java.awt.Color(77, 77, 77)));
        pnlSearch.setPreferredSize(new java.awt.Dimension(853, 120));
        pnlSearch.setLayout(new java.awt.BorderLayout());

        jPanel1.setPreferredSize(new java.awt.Dimension(200, 111));
        jPanel1.setLayout(new java.awt.GridLayout(1, 0));

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));
        jPanel4.setPreferredSize(new java.awt.Dimension(250, 111));

        btnTimKiem.setBackground(new java.awt.Color(77, 77, 77));
        btnTimKiem.setForeground(new java.awt.Color(255, 255, 255));
        btnTimKiem.setText("Tìm Kiếm");
        btnTimKiem.setContentAreaFilled(false);
        btnTimKiem.setOpaque(true);
        btnTimKiem.setPreferredSize(new java.awt.Dimension(130, 32));
        btnTimKiem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTimKiemActionPerformed(evt);
            }
        });

        btnXemReview.setBackground(new java.awt.Color(77, 77, 77));
        btnXemReview.setForeground(new java.awt.Color(255, 255, 255));
        btnXemReview.setText("Xem review");
        btnXemReview.setContentAreaFilled(false);
        btnXemReview.setOpaque(true);
        btnXemReview.setPreferredSize(new java.awt.Dimension(130, 32));
        btnXemReview.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnXemReviewActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnTimKiem, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnXemReview, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(58, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addComponent(btnTimKiem, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnXemReview, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jPanel1.add(jPanel4);

        pnlSearch.add(jPanel1, java.awt.BorderLayout.LINE_END);

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));

        txtTimTheoURL.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)), javax.swing.BorderFactory.createEmptyBorder(1, 5, 1, 1)));
        txtTimTheoURL.setMargin(new java.awt.Insets(2, 30, 2, 2));
        txtTimTheoURL.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtTimTheoURLFocusGained(evt);
            }
        });
        txtTimTheoURL.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTimTheoURLActionPerformed(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel3.setText("Nhập URL:");

        jLabel4.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel4.setText("Tháng:");

        yearChooser.setBackground(new java.awt.Color(204, 204, 204));
        yearChooser.setBorder(new javax.swing.border.MatteBorder(null));

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtTimTheoURL)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(monthChooser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(yearChooser, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 325, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtTimTheoURL, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(14, 14, 14)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(monthChooser, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(yearChooser, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE))
                .addContainerGap(17, Short.MAX_VALUE))
        );

        pnlSearch.add(jPanel3, java.awt.BorderLayout.CENTER);

        jPanel7.setLayout(new java.awt.BorderLayout());

        pnlLeft.setLayout(new java.awt.BorderLayout());

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setPreferredSize(new java.awt.Dimension(400, 366));

        jLabel5.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel5.setText("Tên sp:");

        txtTenSP.setEditable(false);
        txtTenSP.setColumns(20);
        txtTenSP.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        txtTenSP.setLineWrap(true);
        txtTenSP.setRows(5);
        jScrollPane1.setViewportView(txtTenSP);

        jLabel6.setText("Hình ảnh:");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel5, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel6, javax.swing.GroupLayout.Alignment.LEADING))
                        .addGap(0, 334, Short.MAX_VALUE))
                    .addComponent(lblProductPic, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(32, 32, 32)
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lblProductPic, javax.swing.GroupLayout.PREFERRED_SIZE, 195, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(20, Short.MAX_VALUE))
        );

        pnlLeft.add(jPanel2, java.awt.BorderLayout.LINE_START);

        pnlChart.setLayout(new java.awt.BorderLayout());
        pnlLeft.add(pnlChart, java.awt.BorderLayout.CENTER);

        jPanel7.add(pnlLeft, java.awt.BorderLayout.CENTER);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnlSearch, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(pnlSearch, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btnTimKiemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTimKiemActionPerformed
        // TODO add your handling code here:
        searchURL = txtTimTheoURL.getText();
        
        try {
            main.getProductHistories(searchURL, monthChooser.getMonth()+1, yearChooser.getYear());
            System.out.println(monthChooser.getMonth() + " / " + yearChooser.getYear());
        } catch (IOException ex) {
            Logger.getLogger(panelTimTheoURL.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }//GEN-LAST:event_btnTimKiemActionPerformed

    private void btnXemReviewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnXemReviewActionPerformed
        Float rating = 5F;
        if (this.popUp == null) {
            this.popUp = new reviewGUI(rating);
        } else {
            this.popUp.toFront();
            this.popUp.center();
        }
        popUp.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosed(java.awt.event.WindowEvent windowEvent) {
                popUp = null;
            }
        });
    }//GEN-LAST:event_btnXemReviewActionPerformed

    private void txtTimTheoURLActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTimTheoURLActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTimTheoURLActionPerformed

    private void txtTimTheoURLFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtTimTheoURLFocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTimTheoURLFocusGained


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnTimKiem;
    private javax.swing.JButton btnXemReview;
    private javax.swing.ButtonGroup colorGroup;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblProductPic;
    private com.toedter.calendar.JMonthChooser monthChooser;
    private javax.swing.JPanel pnlChart;
    private javax.swing.JPanel pnlLeft;
    private javax.swing.JPanel pnlSearch;
    private javax.swing.ButtonGroup sizeGroup;
    private javax.swing.JTextArea txtTenSP;
    private javax.swing.JTextField txtTimTheoURL;
    private com.toedter.calendar.JYearChooser yearChooser;
    // End of variables declaration//GEN-END:variables
}
