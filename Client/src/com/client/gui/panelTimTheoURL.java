/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.client.gui;

import com.client.gui.others.MyComboBoxEditor;
import com.client.gui.others.MyComboBoxRenderer;
import com.client.main.Client;
import com.sun.javafx.scene.control.skin.VirtualFlow;
import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.font.TextAttribute;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
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
    String productName = "";
    int month = 11;
    int year = 2021;
    LinkedHashMap<String, Object> currentproduct;
    String selectedOption1;
    String selectedOption2;
    String selectedOption3;
    ArrayList<LinkedHashMap<String, Object>> reviewsList;
    ArrayList<LinkedHashMap<String, Object>> timelinesList;
    ImageIcon unCheckedRadio = new ImageIcon(getClass().getResource("/com/client/img/unchecked_radio.png"));
    public panelTimTheoURL(Client main) {
        initComponents();
        this.main = main;
        showLineChart(productName, month, year, dates, prices);
        customMonthYearChooser();
        myTextArea();
        
    }
    
    public void setCurrentProduct(LinkedHashMap<String, Object> product){
        this.currentproduct = product;
    }
    
    public void setReviewsList(ArrayList<LinkedHashMap<String, Object>> reviewsList){
        this.reviewsList = reviewsList;
    }
    
    public void setTimelinesList(ArrayList<LinkedHashMap<String, Object>> timelinesList){
        this.timelinesList = timelinesList;
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
    
    public void updateProductInfo(LinkedHashMap<String, Object> recvProduct){
       
            txtTenSP.setText((String) recvProduct.get("name"));
            String path = (String) recvProduct.get("imageUrl");
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
                lblPrice1.setText(String.valueOf(recvProduct.get("price")) + " Đ");
            } else {
                Font font = new Font("helvetica", Font.PLAIN, 12);
                Map  attributes = font.getAttributes();
                attributes.put(TextAttribute.STRIKETHROUGH, TextAttribute.STRIKETHROUGH_ON);
                Font newFont = new Font(attributes); 
                
                lblPrice.setFont(newFont);
                lblPrice.setText(String.valueOf(recvProduct.get("original_price")) + " Đ");
                
                lblPrice1.setText(String.valueOf(recvProduct.get("price")) + " Đ");
            }
            
            txtMota.setText((String) recvProduct.get("description"));
    }
    
    public void setConfigurableProducts(LinkedHashMap<String, Object> configurableOptionsName, List<LinkedHashMap<String, Object>> configurableProducts){
        switch (configurableOptionsName.size()){
            case 1: 
            {    
                 String name1 = String.valueOf(configurableOptionsName.get("optionName1"));
                 List<String> options = new ArrayList<>();
                 for(LinkedHashMap<String, Object> configurableProduct : configurableProducts)
                 {
                     System.out.println(configurableProduct.get("option1"));  
                     options.add(String.valueOf(configurableProduct.get("option1")));
                 }
                  setConfigurableProductRadioButtons(name1, options, pnlOptionName1, pnlOptions1, 1);
                  break;
            }
            
            case 2: 
            {    
                 String name1 = String.valueOf(configurableOptionsName.get("optionName1"));
                 List<String> options1 = new ArrayList<>();
                 for(LinkedHashMap<String, Object> configurableProduct : configurableProducts)
                 {
                     System.out.println(configurableProduct.get("option1"));  
                     options1.add(String.valueOf(configurableProduct.get("option1")));
                 }
                 setConfigurableProductRadioButtons(name1, options1, pnlOptionName1, pnlOptions1, 1);
                
                 String name2 = String.valueOf(configurableOptionsName.get("optionName2"));
                 List<String> options2 = new ArrayList<>();
                 for(LinkedHashMap<String, Object> configurableProduct : configurableProducts)
                 {
                     System.out.println(configurableProduct.get("option2"));  
                     options2.add(String.valueOf(configurableProduct.get("option2")));
                 }
                  setConfigurableProductRadioButtons(name2, options2, pnlOptionName2, pnlOptions2, 1);
                  break;
            }
            
            case 3: 
            {    
                 String name1 = String.valueOf(configurableOptionsName.get("optionName1"));
                 List<String> options1 = new ArrayList<>();
                 for(LinkedHashMap<String, Object> configurableProduct : configurableProducts)
                 {
                     System.out.println(configurableProduct.get("option1"));  
                     options1.add(String.valueOf(configurableProduct.get("option1")));
                 }
                 setConfigurableProductRadioButtons(name1, options1, pnlOptionName1, pnlOptions1, 1);
                
                 String name2 = String.valueOf(configurableOptionsName.get("optionName2"));
                 List<String> options2 = new ArrayList<>();
                 for(LinkedHashMap<String, Object> configurableProduct : configurableProducts)
                 {
                     System.out.println(configurableProduct.get("option2"));  
                     options2.add(String.valueOf(configurableProduct.get("option2")));
                 }
                  setConfigurableProductRadioButtons(name2, options2, pnlOptionName2, pnlOptions2, 2);
                  
                 String name3 = String.valueOf(configurableOptionsName.get("optionName3"));
                 List<String> options3 = new ArrayList<>();
                 for(LinkedHashMap<String, Object> configurableProduct : configurableProducts)
                 {
                     System.out.println(configurableProduct.get("option3"));  
                     options3.add(String.valueOf(configurableProduct.get("option3")));
                 }
                  setConfigurableProductRadioButtons(name3, options3, pnlOptionName3, pnlOptions3, 3);
                  break;
            }
               
            default:
                System.out.println("not supported yet");
                break;
        }
    }
    
    public void setConfigurableProductRadioButtons(String name, List<String> options, JPanel pnlName,JPanel pnlOption, int stateVariable)
    {
        JLabel optionName = new JLabel(name);
        pnlName.add(optionName);
        ButtonGroup buttonGroup = new ButtonGroup();
        GridLayout gridLayout = new GridLayout(options.size(),1);
        pnlOption.setLayout(gridLayout);
        for(int i=0;i<options.size();i++)
        {   
            JRadioButton radioButton = new JRadioButton(options.get(i));
            switch (stateVariable){
                case 1:
                    radioButton.addItemListener(new ItemListener() {
                    @Override
                    public void itemStateChanged(ItemEvent e) {
                        if (e.getStateChange() == ItemEvent.SELECTED)
                        {
                            selectedOption1 = radioButton.getText();
                            System.out.println(selectedOption1);
                        } else {
                            selectedOption1 = "";
                        }                 
                    }
                    });
                    break;
                case 2:
                    radioButton.addItemListener(new ItemListener() {
                    @Override
                    public void itemStateChanged(ItemEvent e) {
                        if (e.getStateChange() == ItemEvent.SELECTED)
                        {
                            selectedOption2 = radioButton.getText();
                            System.out.println(selectedOption2);
                        } else {
                            selectedOption2 = "";
                        }                 
                    }
                    });
                    break;
                case 3:
                    radioButton.addItemListener(new ItemListener() {
                    @Override
                    public void itemStateChanged(ItemEvent e) {
                        if (e.getStateChange() == ItemEvent.SELECTED)
                        {
                            selectedOption3 = radioButton.getText();
                             System.out.println(selectedOption3);
                        } else {
                            selectedOption3 = "";
                        }                 
                    }
                    });
                    break;
                default:
                    System.out.println("not supported yet");
                    break;
            }
            buttonGroup.add(radioButton);
            pnlOption.add(radioButton);
            pnlOption.revalidate();
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
    
    public void myTextArea()
    {
        txtTenSP.setWrapStyleWord(true);
        txtTenSP.setLineWrap(true);
        jScrollPane1.getVerticalScrollBar().setPreferredSize(new Dimension(0, 0));
        txtTenSP.setCaretPosition(0); // scroll to top
        
        txtMota.setWrapStyleWord(true);
        txtMota.setLineWrap(true);
        jScrollPane3.getVerticalScrollBar().setPreferredSize(new Dimension(0, 0));
        txtMota.setCaretPosition(0); // scroll to top
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
        pnlPic = new javax.swing.JPanel();
        lblPrice = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        lblPrice1 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        txtMota = new javax.swing.JTextArea();
        pnlOption = new javax.swing.JPanel();
        pnlOption1 = new javax.swing.JPanel();
        pnlOptions1 = new javax.swing.JPanel();
        pnlOptionName1 = new javax.swing.JPanel();
        pnlOption2 = new javax.swing.JPanel();
        pnlOptions2 = new javax.swing.JPanel();
        pnlOptionName2 = new javax.swing.JPanel();
        pnlOption3 = new javax.swing.JPanel();
        pnlOptions3 = new javax.swing.JPanel();
        pnlOptionName3 = new javax.swing.JPanel();
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
        btnTimKiem.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
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
        btnXemReview.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
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

        jLabel3.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        jLabel3.setText("Nhập URL:");

        jLabel4.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
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
                        .addGap(0, 471, Short.MAX_VALUE)))
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

        jLabel5.setFont(new java.awt.Font("Arial", 0, 16)); // NOI18N
        jLabel5.setText("Giá:");

        jScrollPane1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        txtTenSP.setEditable(false);
        txtTenSP.setColumns(20);
        txtTenSP.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txtTenSP.setLineWrap(true);
        txtTenSP.setRows(5);
        txtTenSP.setFocusable(false);
        jScrollPane1.setViewportView(txtTenSP);

        jLabel6.setFont(new java.awt.Font("Arial", 0, 16)); // NOI18N
        jLabel6.setText("Hình ảnh:");

        pnlPic.setBackground(new java.awt.Color(255, 255, 255));
        pnlPic.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        pnlPic.setPreferredSize(new java.awt.Dimension(300, 199));
        pnlPic.setLayout(new java.awt.BorderLayout());

        lblPrice.setFont(new java.awt.Font("Arial", 0, 16)); // NOI18N
        lblPrice.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);

        jLabel7.setFont(new java.awt.Font("Arial", 0, 16)); // NOI18N
        jLabel7.setText("Tên sp:");

        lblPrice1.setFont(new java.awt.Font("Arial", 0, 16)); // NOI18N
        lblPrice1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);

        jLabel8.setFont(new java.awt.Font("Arial", 0, 16)); // NOI18N
        jLabel8.setText("Mô tả:");

        jScrollPane3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        txtMota.setEditable(false);
        txtMota.setColumns(20);
        txtMota.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txtMota.setLineWrap(true);
        txtMota.setRows(5);
        txtMota.setWrapStyleWord(true);
        txtMota.setFocusable(false);
        jScrollPane3.setViewportView(txtMota);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(pnlPic, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel6))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(lblPrice, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(lblPrice1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel5)
                                            .addComponent(jLabel7))
                                        .addGap(0, 0, Short.MAX_VALUE)))
                                .addGap(10, 10, 10))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 171, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(18, Short.MAX_VALUE))))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel8)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 377, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(jLabel7))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(pnlPic, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(13, 13, 13)
                        .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblPrice, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblPrice1, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addComponent(jLabel8)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 193, Short.MAX_VALUE)
                .addContainerGap())
        );

        pnlLeft.add(jPanel2, java.awt.BorderLayout.LINE_START);

        pnlOption.setBackground(new java.awt.Color(255, 255, 255));
        pnlOption.setPreferredSize(new java.awt.Dimension(150, 475));
        pnlOption.setLayout(new java.awt.GridLayout(3, 1));

        pnlOption1.setBackground(new java.awt.Color(255, 255, 255));
        pnlOption1.setLayout(new java.awt.BorderLayout());

        pnlOptions1.setBackground(new java.awt.Color(255, 255, 255));
        pnlOptions1.setPreferredSize(new java.awt.Dimension(150, 120));
        pnlOptions1.setLayout(new java.awt.GridLayout(5, 1));
        pnlOption1.add(pnlOptions1, java.awt.BorderLayout.PAGE_END);

        pnlOptionName1.setBackground(new java.awt.Color(255, 255, 255));
        pnlOptionName1.setMinimumSize(new java.awt.Dimension(100, 50));
        pnlOptionName1.setPreferredSize(new java.awt.Dimension(150, 40));
        pnlOptionName1.setLayout(new java.awt.GridBagLayout());
        pnlOption1.add(pnlOptionName1, java.awt.BorderLayout.CENTER);

        pnlOption.add(pnlOption1);

        pnlOption2.setBackground(new java.awt.Color(255, 255, 255));
        pnlOption2.setLayout(new java.awt.BorderLayout());

        pnlOptions2.setBackground(new java.awt.Color(255, 255, 255));
        pnlOptions2.setPreferredSize(new java.awt.Dimension(150, 120));
        pnlOptions2.setLayout(new java.awt.GridLayout());
        pnlOption2.add(pnlOptions2, java.awt.BorderLayout.PAGE_END);

        pnlOptionName2.setBackground(new java.awt.Color(255, 255, 255));
        pnlOptionName2.setLayout(new java.awt.GridBagLayout());
        pnlOption2.add(pnlOptionName2, java.awt.BorderLayout.CENTER);

        pnlOption.add(pnlOption2);

        pnlOption3.setBackground(new java.awt.Color(255, 255, 255));
        pnlOption3.setLayout(new java.awt.BorderLayout());

        pnlOptions3.setBackground(new java.awt.Color(255, 255, 255));
        pnlOptions3.setPreferredSize(new java.awt.Dimension(150, 120));
        pnlOptions3.setLayout(new java.awt.GridBagLayout());
        pnlOption3.add(pnlOptions3, java.awt.BorderLayout.PAGE_END);

        pnlOptionName3.setBackground(new java.awt.Color(255, 255, 255));
        pnlOptionName3.setLayout(new java.awt.GridBagLayout());
        pnlOption3.add(pnlOptionName3, java.awt.BorderLayout.CENTER);

        pnlOption.add(pnlOption3);

        pnlLeft.add(pnlOption, java.awt.BorderLayout.LINE_END);

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

        searchURL = txtTimTheoURL.getText();

        try {
            main.getProductHistories(searchURL, monthChooser.getMonth()+1, yearChooser.getYear());
        } catch (IOException ex) {
            Logger.getLogger(panelTimTheoURL.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        LinkedHashMap<String, Object> name =  new LinkedHashMap<>();
        name.put("optionName1", "Màu");
        name.put("optionName2", "Size");
        name.put("optionName3", "Chất liệu");
        
        
         LinkedHashMap<String, Object> option1 =  new LinkedHashMap<>();
         option1.put("option1", "Đỏ");
         option1.put("option2", "35");
         option1.put("option3", "Gỗ");
         
         LinkedHashMap<String, Object> option2 =  new LinkedHashMap<>();
         option2.put("option1", "Xám");
         option2.put("option2", "38");
         option2.put("option3", "Đá");
         
         LinkedHashMap<String, Object> option3 =  new LinkedHashMap<>();
         option3.put("option1", "Đen");
         option3.put("option2", "40");
         option3.put("option3", "Giấy");
         
         
         
         
        List<LinkedHashMap<String, Object>> options = new ArrayList<>();
        
        options.add(option1);
        options.add(option2);
        options.add(option3);
        
        setConfigurableProducts(name, options);
    }//GEN-LAST:event_btnTimKiemActionPerformed

    private void btnXemReviewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnXemReviewActionPerformed
        if(this.currentproduct == null) JOptionPane.showMessageDialog(this, "Bạn chưa tìm kiếm sản phẩm", "Thông báo", JOptionPane.INFORMATION_MESSAGE); 
        else{
            if ((this.reviewsList != null && this.reviewsList.size() > 0))
            {   
                Float rating = Float.valueOf(((Double) this.currentproduct.get("rating_average")).floatValue());
                if (this.popUp == null) {
                    this.popUp = new reviewGUI(rating, this.reviewsList, this.timelinesList);
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
            } else  JOptionPane.showMessageDialog(this, "Bạn chưa tìm kiếm sản phẩm hoặc sản phẩm không có review", "Thông báo", JOptionPane.INFORMATION_MESSAGE); 
        }
        
        
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
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JLabel lblPrice;
    private javax.swing.JLabel lblPrice1;
    private com.toedter.calendar.JMonthChooser monthChooser;
    private javax.swing.JPanel pnlChart;
    private javax.swing.JPanel pnlLeft;
    private javax.swing.JPanel pnlOption;
    private javax.swing.JPanel pnlOption1;
    private javax.swing.JPanel pnlOption2;
    private javax.swing.JPanel pnlOption3;
    private javax.swing.JPanel pnlOptionName1;
    private javax.swing.JPanel pnlOptionName2;
    private javax.swing.JPanel pnlOptionName3;
    private javax.swing.JPanel pnlOptions1;
    private javax.swing.JPanel pnlOptions2;
    private javax.swing.JPanel pnlOptions3;
    private javax.swing.JPanel pnlPic;
    private javax.swing.JPanel pnlSearch;
    private javax.swing.ButtonGroup sizeGroup;
    private javax.swing.JTextArea txtMota;
    private javax.swing.JTextArea txtTenSP;
    private javax.swing.JTextField txtTimTheoURL;
    private com.toedter.calendar.JYearChooser yearChooser;
    // End of variables declaration//GEN-END:variables
}
