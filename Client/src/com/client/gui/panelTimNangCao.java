/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.client.gui;

import com.client.gui.others.MyComboBoxEditor;
import com.client.gui.others.MyComboBoxRenderer;
import com.client.gui.others.MyScrollBarUI;
import com.client.main.Client;
import com.client.model.BrandCheckboxItem;
import com.client.model.CategoryComboItem;
import com.client.thread.ReadThread;
import com.client.thread.WriteThread;
import com.client.util.InputValidatorUtil;
import com.client.util.MapUtil;
import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ButtonModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.border.MatteBorder;
import javax.swing.plaf.basic.BasicArrowButton;
import javax.swing.plaf.basic.BasicComboBoxUI;
import javax.swing.plaf.basic.BasicComboPopup;
import javax.swing.plaf.basic.BasicSpinnerUI;
import javax.swing.plaf.basic.ComboPopup;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.StandardChartTheme;
import org.jfree.chart.labels.ItemLabelAnchor;
import org.jfree.chart.labels.ItemLabelPosition;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.chart.renderer.category.StandardBarPainter;
import org.jfree.chart.ui.RectangleInsets;
import org.jfree.chart.ui.TextAnchor;
import org.jfree.data.category.DefaultCategoryDataset;

/**
 *
 * @author Hi
 */
public class panelTimNangCao extends javax.swing.JPanel {

    /**
     * Creates new form panelTimNangCao
     */
    String searchData;
    String json;
    Map<Long, String> currentBrands = new HashMap<>();
    HashMap<Long, String> brandIdAndName;
    List<String> brandList = new ArrayList<>();
    List<String> selectedBrands = new ArrayList<>();
    List<String> dates = new ArrayList<String>();
    List<Integer> prices = new ArrayList<Integer>();
    LinkedHashMap<String, Object> currentproduct;
    InputValidatorUtil validator = new InputValidatorUtil();
    String productName = "";
    Long selectedProductId;
    boolean found = false;
    int month = 11;
    int year = 2021;
    List<String> name = new ArrayList<String>();
    Long categoryId = 2L; //2L là id của Root - toàn bộ danh mục
    Client main;
    productDetail popup;
    public panelTimNangCao(Client main) {
       
        initComponents();
        this.main = main;
        for(int i = 1; i < 31; i ++){
            dates.add(String.valueOf(i));
        }
        for (int i = 1; i < 31; i++){
            prices.add(i +100 );
        }
        
        showLineChart(productName, month, year, dates, prices);
        comboboxCategory = myComboBox(comboboxCategory, new Color(14,142,233));
        scrollPaneBrands.getVerticalScrollBar().setUI(new MyScrollBarUI());
        initNullTable();
        customMonthYearChooser();
    }
    
    public void customMonthYearChooser(){
        javax.swing.JComboBox boxMonth = (javax.swing.JComboBox) monthChooser.getComboBox();
        boxMonth = myComboBox(boxMonth, new Color(77,77,77));
        boxMonth.setLightWeightPopupEnabled (false);
        hideSpinnerArrow((javax.swing.JSpinner) monthChooser.getSpinner());
        hideSpinnerArrow((javax.swing.JSpinner) yearChooser.getSpinner());
    }
    
    public void initNullTable()
    {
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("Id");
        model.addColumn("Tên");
        model.addColumn("Số lượng đã bán");
        model.addColumn("Giá");
        
        tableScrollPane.getVerticalScrollBar().setUI(new MyScrollBarUI());
        advanceProductTable.setModel(model);
        headerColor(77,77,77, advanceProductTable);
        
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
     
    public void headerColor(int r, int b, int g, JTable table)
    {
        Color color = new Color(r,b,g);
        DefaultTableCellRenderer headerRenderer = new DefaultTableCellRenderer();
        headerRenderer.setBackground(color);
        headerRenderer.setForeground(color.WHITE);
        

        for (int i = 0; i < table.getModel().getColumnCount(); i++) {
        table.getColumnModel().getColumn(i).setHeaderRenderer(headerRenderer);
        }       
         
        table.setFont(new Font("Tahoma", Font.PLAIN, 16));
    }
    
    public float selectedButtonGroupStar()
    {
        if(jRadioButton1.isSelected())
            return 5f;
        if(jRadioButton2.isSelected())
            return 4f;
        if(jRadioButton3.isSelected())
            return 3f;
        if(jRadioButton5.isSelected())
            return 2f;
        return 1f;
    }
            
    public void listNameCategory(ArrayList<String> name, ArrayList<Integer> idCate) {
        CategoryComboItem[] list = new CategoryComboItem[name.size()];
        
        CategoryComboItem c;
        for(int i = 0 ; i < name.size(); i++) {
             c = new CategoryComboItem(idCate.get(i), name.get(i).equals("Root") ? "-- Chọn danh mục --" : name.get(i));
             list[i] = c;
            // System.out.println(c.getId());
        }
         setComboBox(comboboxCategory, list);
    }
    
    public void clearBrandPanel()
    {
        selectedBrands.clear();
        pnlBrands.removeAll();
        pnlBrands.revalidate();
        pnlBrands.repaint();
        JOptionPane.showMessageDialog(this, "Danh mục bạn chọn không có thương hiệu nào", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
    }
    
    public void setComboBox(JComboBox<CategoryComboItem> comboBox, CategoryComboItem[] listItems) {
        comboBox.setModel(new DefaultComboBoxModel<>(listItems));
    }
    
    public void setCurrentProduct(LinkedHashMap<String, Object> product){
        this.currentproduct = product;
    }
    
    public void setTable(List<LinkedHashMap<String, Object>> products)
    {
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("Id");
        model.addColumn("Tên");
        model.addColumn("Số lượng đã bán");
        model.addColumn("Giá");
        
        List<Integer> id = new ArrayList<>();
        List<String> name = new ArrayList<>();
        
        for(LinkedHashMap<String, Object> product : products)
        {
            Vector row = new Vector();
            row.add(product.get("id"));
            row.add(product.get("name"));
            row.add(product.get("all_time_quantity_sold"));
            row.add(product.get("price"));
            model.addRow(row);
        }
        
        advanceProductTable.setModel(model);
        headerColor(77,77,77, advanceProductTable);
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
     
    public void updateBrandCheckbox(Map<Long, String> brands)
    {
        selectedBrands.clear();
        
        currentBrands.clear();
        currentBrands.putAll(brands);
        
        pnlBrands.removeAll();
        pnlBrands.revalidate();
        pnlBrands.repaint();
        
        int checkBoxWidth = 200;
        int checkBoxHeight = 40;
        int xColumn1 = 10;
        int xColumn2 = xColumn1 + checkBoxWidth + 40;
        int y1 = 15;
        int numberOfBrands = 0;
        
        HashMap<Integer, Object> brandNames = new HashMap();
        
        for (Map.Entry<Long, String> brand : currentBrands.entrySet()) {
            JCheckBox checkBox = new JCheckBox(brand.getValue()); 
            checkBox.addItemListener(new ItemListener() {

            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED)
                {
                    selectedBrands.add(checkBox.getText());
                } else {
                    selectedBrands.remove(checkBox.getText());
                }                 
            }
            });
            
            pnlBrands.add(checkBox);
            pnlBrands.revalidate();
            y1 += 25;
        }
    }
    
    public void initBrandCheckbox(ArrayList<String> list){
        int y = 15; 
        for (String brand : list){
            JCheckBox checkBox = new JCheckBox(brand);
            checkBox.setBounds(10, y, 200, 40);
            checkBox.addItemListener(new ItemListener() {

            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED)
                {
                    selectedBrands.add(checkBox.getText());
                } else {
                    selectedBrands.remove(checkBox.getText());
                }                 
            }
            });
            pnlCategory.add(checkBox);
            y += 25;
        }
    }
    
     public void showLineChart(String productName, int month, int year, List<String> dates, List<Integer> prices ){
        
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

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroupStar = new javax.swing.ButtonGroup();
        rightClickMenu = new javax.swing.JPopupMenu();
        itemViewDetail = new javax.swing.JMenuItem();
        pnlLeft = new javax.swing.JPanel();
        pnlSearchAndFilter = new javax.swing.JPanel();
        pnlSearch = new javax.swing.JPanel();
        lblTitleTenSanPham = new javax.swing.JLabel();
        txtSearch = new javax.swing.JTextField();
        comboboxCategory = new javax.swing.JComboBox<>();
        jLabel4 = new javax.swing.JLabel();
        pnlFilter = new javax.swing.JPanel();
        pnlCategory = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        scrollPaneBrands = new javax.swing.JScrollPane();
        pnlBrands = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        jTextField3 = new javax.swing.JTextField();
        jTextField4 = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jPanel7 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jRadioButton1 = new javax.swing.JRadioButton();
        jRadioButton2 = new javax.swing.JRadioButton();
        jRadioButton3 = new javax.swing.JRadioButton();
        jRadioButton5 = new javax.swing.JRadioButton();
        jRadioButton4 = new javax.swing.JRadioButton();
        pnlTable = new javax.swing.JPanel();
        pnlTableTop = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        monthChooser = new com.toedter.calendar.JMonthChooser();
        yearChooser = new com.toedter.calendar.JYearChooser();
        pnlTableBottom = new javax.swing.JPanel();
        tableScrollPane = new javax.swing.JScrollPane();
        advanceProductTable = new javax.swing.JTable();
        jPanel1 = new javax.swing.JPanel();
        pnlOption = new javax.swing.JPanel();
        pnlButton = new javax.swing.JPanel();
        btnCPFilter = new javax.swing.JButton();
        scrollPaneOptions = new javax.swing.JScrollPane();
        pnlOptionRadio = new javax.swing.JPanel();
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

        itemViewDetail.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        itemViewDetail.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/client/img/view_detail_icon.png"))); // NOI18N
        itemViewDetail.setText("Xem chi tiết");
        itemViewDetail.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                itemViewDetailActionPerformed(evt);
            }
        });
        rightClickMenu.add(itemViewDetail);

        setBackground(new java.awt.Color(255, 255, 255));
        setBorder(javax.swing.BorderFactory.createMatteBorder(1, 0, 0, 0, new java.awt.Color(0, 0, 0)));
        setPreferredSize(new java.awt.Dimension(1280, 720));
        setLayout(new java.awt.BorderLayout());

        pnlLeft.setPreferredSize(new java.awt.Dimension(550, 493));
        pnlLeft.setLayout(new java.awt.BorderLayout());

        pnlSearchAndFilter.setBackground(new java.awt.Color(255, 255, 255));
        pnlSearchAndFilter.setPreferredSize(new java.awt.Dimension(400, 350));
        pnlSearchAndFilter.setLayout(new java.awt.BorderLayout());

        pnlSearch.setBackground(new java.awt.Color(255, 255, 255));
        pnlSearch.setPreferredSize(new java.awt.Dimension(600, 80));

        lblTitleTenSanPham.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        lblTitleTenSanPham.setText("Tên sản phẩm:");

        comboboxCategory.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                comboboxCategoryMouseClicked(evt);
            }
        });
        comboboxCategory.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                comboboxCategoryActionPerformed(evt);
            }
        });

        jLabel4.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel4.setText("Danh mục:");

        javax.swing.GroupLayout pnlSearchLayout = new javax.swing.GroupLayout(pnlSearch);
        pnlSearch.setLayout(pnlSearchLayout);
        pnlSearchLayout.setHorizontalGroup(
            pnlSearchLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlSearchLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(pnlSearchLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblTitleTenSanPham)
                    .addComponent(jLabel4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(pnlSearchLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(comboboxCategory, 0, 250, Short.MAX_VALUE)
                    .addComponent(txtSearch))
                .addGap(243, 243, 243))
        );
        pnlSearchLayout.setVerticalGroup(
            pnlSearchLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlSearchLayout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addGroup(pnlSearchLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblTitleTenSanPham, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtSearch, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(pnlSearchLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(comboboxCategory, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        pnlSearchAndFilter.add(pnlSearch, java.awt.BorderLayout.PAGE_START);

        pnlFilter.setLayout(new java.awt.BorderLayout());

        pnlCategory.setBackground(new java.awt.Color(255, 255, 255));
        pnlCategory.setPreferredSize(new java.awt.Dimension(200, 240));
        pnlCategory.setLayout(null);

        jLabel5.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel5.setText("Thương hiệu:");
        pnlCategory.add(jLabel5);
        jLabel5.setBounds(10, 6, 100, 20);

        scrollPaneBrands.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        pnlBrands.setBackground(new java.awt.Color(255, 255, 255));
        pnlBrands.setLayout(new javax.swing.BoxLayout(pnlBrands, javax.swing.BoxLayout.PAGE_AXIS));
        scrollPaneBrands.setViewportView(pnlBrands);

        pnlCategory.add(scrollPaneBrands);
        scrollPaneBrands.setBounds(10, 30, 180, 210);

        pnlFilter.add(pnlCategory, java.awt.BorderLayout.LINE_START);

        jPanel6.setBackground(new java.awt.Color(255, 255, 255));
        jPanel6.setPreferredSize(new java.awt.Dimension(200, 240));

        jLabel7.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel7.setText("Chọn khoảng giá:");

        jLabel8.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel8.setText("Từ:");

        jLabel9.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel9.setText("Đến:");

        jSeparator1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 5));
        jSeparator1.setPreferredSize(new java.awt.Dimension(50, 4));

        jButton1.setText("Xem review");

        jButton2.setText("Tìm kiếm");
        jButton2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButton2MouseClicked(evt);
            }
        });
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel9)
                                .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, 168, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, 168, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel8))))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGap(87, 87, 87)
                        .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(20, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(8, 8, 8)
                .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(1, 1, 1)
                .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 23, Short.MAX_VALUE)
                .addComponent(jButton2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton1)
                .addGap(26, 26, 26))
        );

        pnlFilter.add(jPanel6, java.awt.BorderLayout.LINE_END);

        jPanel7.setBackground(new java.awt.Color(255, 255, 255));

        jLabel6.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel6.setText("Đánh giá:");

        buttonGroupStar.add(jRadioButton1);
        jRadioButton1.setSelected(true);
        jRadioButton1.setText("Từ 5 sao");

        buttonGroupStar.add(jRadioButton2);
        jRadioButton2.setText("Từ 4 sao");

        buttonGroupStar.add(jRadioButton3);
        jRadioButton3.setText("Từ 3 sao");

        buttonGroupStar.add(jRadioButton5);
        jRadioButton5.setText("Từ 1 sao");

        buttonGroupStar.add(jRadioButton4);
        jRadioButton4.setText("Từ 2 sao");

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jRadioButton5)
                    .addComponent(jRadioButton2)
                    .addComponent(jRadioButton1)
                    .addComponent(jLabel6)
                    .addComponent(jRadioButton4)
                    .addComponent(jRadioButton3))
                .addContainerGap(59, Short.MAX_VALUE))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jRadioButton1)
                .addGap(14, 14, 14)
                .addComponent(jRadioButton2)
                .addGap(14, 14, 14)
                .addComponent(jRadioButton3)
                .addGap(14, 14, 14)
                .addComponent(jRadioButton4)
                .addGap(14, 14, 14)
                .addComponent(jRadioButton5)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pnlFilter.add(jPanel7, java.awt.BorderLayout.CENTER);

        pnlSearchAndFilter.add(pnlFilter, java.awt.BorderLayout.CENTER);

        pnlLeft.add(pnlSearchAndFilter, java.awt.BorderLayout.PAGE_START);

        pnlTable.setBackground(new java.awt.Color(255, 255, 255));
        pnlTable.setLayout(new java.awt.BorderLayout());

        pnlTableTop.setBackground(new java.awt.Color(255, 255, 255));
        pnlTableTop.setPreferredSize(new java.awt.Dimension(574, 60));

        jLabel10.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        jLabel10.setText("Xem lịch sử giá :");

        monthChooser.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                monthChooserPropertyChange(evt);
            }
        });

        yearChooser.setBackground(new java.awt.Color(204, 204, 204));
        yearChooser.setBorder(new javax.swing.border.MatteBorder(null));

        javax.swing.GroupLayout pnlTableTopLayout = new javax.swing.GroupLayout(pnlTableTop);
        pnlTableTop.setLayout(pnlTableTopLayout);
        pnlTableTopLayout.setHorizontalGroup(
            pnlTableTopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlTableTopLayout.createSequentialGroup()
                .addGap(194, 194, 194)
                .addComponent(jLabel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(monthChooser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(yearChooser, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(56, 56, 56))
        );
        pnlTableTopLayout.setVerticalGroup(
            pnlTableTopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlTableTopLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlTableTopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(yearChooser, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(monthChooser, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(17, Short.MAX_VALUE))
        );

        pnlTable.add(pnlTableTop, java.awt.BorderLayout.PAGE_START);

        pnlTableBottom.setLayout(new java.awt.BorderLayout());

        advanceProductTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        advanceProductTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                advanceProductTableMouseClicked(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                advanceProductTableMouseReleased(evt);
            }
        });
        tableScrollPane.setViewportView(advanceProductTable);

        pnlTableBottom.add(tableScrollPane, java.awt.BorderLayout.CENTER);

        pnlTable.add(pnlTableBottom, java.awt.BorderLayout.CENTER);

        pnlLeft.add(pnlTable, java.awt.BorderLayout.CENTER);

        add(pnlLeft, java.awt.BorderLayout.LINE_START);

        jPanel1.setPreferredSize(new java.awt.Dimension(140, 719));
        jPanel1.setLayout(new java.awt.BorderLayout());

        pnlOption.setBackground(new java.awt.Color(255, 255, 255));
        pnlOption.setPreferredSize(new java.awt.Dimension(140, 719));
        pnlOption.setLayout(new java.awt.BorderLayout());

        pnlButton.setBackground(new java.awt.Color(255, 255, 255));
        pnlButton.setPreferredSize(new java.awt.Dimension(150, 60));
        pnlButton.setLayout(new java.awt.GridBagLayout());

        btnCPFilter.setBackground(new java.awt.Color(77, 77, 77));
        btnCPFilter.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        btnCPFilter.setForeground(new java.awt.Color(255, 255, 255));
        btnCPFilter.setText("Lọc lựa chọn");
        btnCPFilter.setContentAreaFilled(false);
        btnCPFilter.setOpaque(true);
        btnCPFilter.setPreferredSize(new java.awt.Dimension(140, 32));
        btnCPFilter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCPFilterActionPerformed(evt);
            }
        });
        pnlButton.add(btnCPFilter, new java.awt.GridBagConstraints());

        pnlOption.add(pnlButton, java.awt.BorderLayout.PAGE_END);

        scrollPaneOptions.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        scrollPaneOptions.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPaneOptions.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
        scrollPaneOptions.setPreferredSize(new java.awt.Dimension(162, 650));

        pnlOptionRadio.setLayout(new java.awt.GridLayout(3, 1));

        pnlOption1.setBackground(new java.awt.Color(255, 255, 255));
        pnlOption1.setLayout(new java.awt.BorderLayout());

        pnlOptions1.setBackground(new java.awt.Color(255, 255, 255));
        pnlOptions1.setPreferredSize(new java.awt.Dimension(150, 180));
        pnlOptions1.setLayout(new java.awt.GridLayout(5, 1));
        pnlOption1.add(pnlOptions1, java.awt.BorderLayout.PAGE_END);

        pnlOptionName1.setBackground(new java.awt.Color(255, 255, 255));
        pnlOptionName1.setMinimumSize(new java.awt.Dimension(100, 50));
        pnlOptionName1.setPreferredSize(new java.awt.Dimension(150, 40));
        pnlOptionName1.setLayout(new java.awt.GridBagLayout());
        pnlOption1.add(pnlOptionName1, java.awt.BorderLayout.CENTER);

        pnlOptionRadio.add(pnlOption1);

        pnlOption2.setBackground(new java.awt.Color(255, 255, 255));
        pnlOption2.setLayout(new java.awt.BorderLayout());

        pnlOptions2.setBackground(new java.awt.Color(255, 255, 255));
        pnlOptions2.setPreferredSize(new java.awt.Dimension(150, 180));
        pnlOptions2.setLayout(new java.awt.GridLayout());
        pnlOption2.add(pnlOptions2, java.awt.BorderLayout.PAGE_END);

        pnlOptionName2.setBackground(new java.awt.Color(255, 255, 255));
        pnlOptionName2.setLayout(new java.awt.GridBagLayout());
        pnlOption2.add(pnlOptionName2, java.awt.BorderLayout.CENTER);

        pnlOptionRadio.add(pnlOption2);

        pnlOption3.setBackground(new java.awt.Color(255, 255, 255));
        pnlOption3.setLayout(new java.awt.BorderLayout());

        pnlOptions3.setBackground(new java.awt.Color(255, 255, 255));
        pnlOptions3.setPreferredSize(new java.awt.Dimension(150, 180));
        pnlOptions3.setLayout(new java.awt.GridBagLayout());
        pnlOption3.add(pnlOptions3, java.awt.BorderLayout.PAGE_END);

        pnlOptionName3.setBackground(new java.awt.Color(255, 255, 255));
        pnlOptionName3.setLayout(new java.awt.GridBagLayout());
        pnlOption3.add(pnlOptionName3, java.awt.BorderLayout.CENTER);

        pnlOptionRadio.add(pnlOption3);

        scrollPaneOptions.setViewportView(pnlOptionRadio);

        pnlOption.add(scrollPaneOptions, java.awt.BorderLayout.CENTER);

        jPanel1.add(pnlOption, java.awt.BorderLayout.LINE_END);

        add(jPanel1, java.awt.BorderLayout.LINE_END);

        pnlChart.setPreferredSize(new java.awt.Dimension(700, 719));
        pnlChart.setLayout(new java.awt.BorderLayout());
        add(pnlChart, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        
    }//GEN-LAST:event_jButton2ActionPerformed

    private void comboboxCategoryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_comboboxCategoryActionPerformed
        CategoryComboItem selectedCategory = (CategoryComboItem)comboboxCategory.getSelectedItem(); 
        categoryId = Long.valueOf((int)selectedCategory.getId());
        
        if(categoryId != 2L)  //2 là id của root
        {
            try {
                main.getBrandsByCategoryId(categoryId);
            } catch (IOException ex) {
                Logger.getLogger(panelTimNangCao.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            System.out.println("Chọn tất cả danh mục");
        }
    }//GEN-LAST:event_comboboxCategoryActionPerformed

    private void jButton2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton2MouseClicked
        String productName = txtSearch.getText().isEmpty() ? null : txtSearch.getText();
        
        CategoryComboItem selectedCategory = (CategoryComboItem)comboboxCategory.getSelectedItem(); 
        categoryId = Long.valueOf((int)selectedCategory.getId());
        
        List<Long> brandIds = null;
        if(selectedBrands.size() != 0) {
            brandIds = new ArrayList<>();
            
            for(String brandName : selectedBrands) {
                brandIds.add(MapUtil.getKey(currentBrands, brandName));
            }
        }
        
        float ratingAverage = selectedButtonGroupStar();
        
        String errorMsg;
        Long minPrice = 0L;
        Long maxPrice = null;
        String txtMinPrice = jTextField3.getText();
        String txtMaxPrice = jTextField4.getText();
        
        if(!txtMinPrice.equals("")) {
            errorMsg = validator.isValidMoney(txtMinPrice);
            
            if(!errorMsg.equals(""))
            {
                JOptionPane.showMessageDialog(this, errorMsg, "Thông báo", JOptionPane.ERROR_MESSAGE);
                return;
            }
                
            minPrice = Long.valueOf(txtMinPrice);
        }
        
        if (!txtMaxPrice.equals("")) {
            errorMsg = validator.isValidMoney(txtMaxPrice);
            
            if(!errorMsg.equals(""))
            {
                JOptionPane.showMessageDialog(this, errorMsg, "Thông báo", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            maxPrice = Long.valueOf(txtMaxPrice);
                
            if(minPrice >= maxPrice) {
                JOptionPane.showMessageDialog(this, "Khoảng giá không hợp lệ!", "Thông báo", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }

        try {
            main.filterProducts(productName, categoryId, brandIds, ratingAverage, minPrice, maxPrice);
        } catch (IOException ex) {
            Logger.getLogger(panelTimTheoURL.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButton2MouseClicked
    
    private void advanceProductTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_advanceProductTableMouseClicked

        int row = advanceProductTable.getSelectedRow();
        Long id = Long.parseLong(advanceProductTable.getModel().getValueAt(row, 0).toString());
        this.selectedProductId = id;
        try {
            int month = monthChooser.getMonth()+1;
            int year = yearChooser.getYear();
            main.getProductHistoriesById(id, month, year);
            main.getProduct(id);
            System.out.println(currentproduct);
            found = true;
        } catch (IOException ex) {
            Logger.getLogger(panelTimNangCao.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_advanceProductTableMouseClicked

    private void comboboxCategoryMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_comboboxCategoryMouseClicked
//        try {
//            main.getConfigurableOptionById(249953L);
//        } catch (IOException ex) {
//            Logger.getLogger(panelTimNangCao.class.getName()).log(Level.SEVERE, null, ex);
//        }
    }//GEN-LAST:event_comboboxCategoryMouseClicked

    private void monthChooserPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_monthChooserPropertyChange
        if (selectedProductId != null)
        {
            try {
                int month = monthChooser.getMonth()+1;
                int year = yearChooser.getYear();
                main.getProductHistoriesById(selectedProductId, month, year);
            } catch (IOException ex) {
                Logger.getLogger(panelTimNangCao.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else if (found)
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một sản phẩm", "Thông báo", JOptionPane.ERROR_MESSAGE);
    }//GEN-LAST:event_monthChooserPropertyChange

    private void advanceProductTableMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_advanceProductTableMouseReleased
      int r = advanceProductTable.rowAtPoint(evt.getPoint());
        if (r >= 0 && r < advanceProductTable.getRowCount()) {
            advanceProductTable.setRowSelectionInterval(r, r);
        } else {
           advanceProductTable.clearSelection();
        }
        int rowindex = advanceProductTable.getSelectedRow();
        if (rowindex < 0)
            return;
        if (evt.isPopupTrigger() && evt.getComponent() instanceof JTable ) {
            
            rightClickMenu.show(evt.getComponent(), evt.getX(), evt.getY());
        }
    }//GEN-LAST:event_advanceProductTableMouseReleased

    private void itemViewDetailActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_itemViewDetailActionPerformed
        int rowindex = advanceProductTable.getSelectedRow();
        Long id = Long.parseLong(advanceProductTable.getValueAt(rowindex,0).toString());
        if (this.popup == null) {
            popup = new productDetail(currentproduct);
            popup.setVisible(true);
        } else {
            this.popup.toFront();
            this.popup.center();
        }
        popup.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosed(java.awt.event.WindowEvent windowEvent) {
                popup = null;
            }
        });
    }//GEN-LAST:event_itemViewDetailActionPerformed

    private void btnCPFilterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCPFilterActionPerformed
//        if(currentproduct.get("id") == null){
//            JOptionPane.showMessageDialog(this, "Bạn chưa tìm kiếm sản phẩm", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
//            return;
//        }
//        try {
//            main.getConfigurableProductHistories(Long.valueOf((int) currentproduct.get("id")), selectedOption1, selectedOption2, selectedOption3, monthChooser.getMonth()+1, yearChooser.getYear());
//        } catch (IOException ex) {
//            Logger.getLogger(panelTimTheoURL.class.getName()).log(Level.SEVERE, null, ex);
//        }
    }//GEN-LAST:event_btnCPFilterActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTable advanceProductTable;
    private javax.swing.JButton btnCPFilter;
    private javax.swing.ButtonGroup buttonGroupStar;
    private javax.swing.JComboBox<CategoryComboItem> comboboxCategory;
    private javax.swing.JMenuItem itemViewDetail;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JRadioButton jRadioButton1;
    private javax.swing.JRadioButton jRadioButton2;
    private javax.swing.JRadioButton jRadioButton3;
    private javax.swing.JRadioButton jRadioButton4;
    private javax.swing.JRadioButton jRadioButton5;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField jTextField4;
    private javax.swing.JLabel lblTitleTenSanPham;
    private com.toedter.calendar.JMonthChooser monthChooser;
    private javax.swing.JPanel pnlBrands;
    private javax.swing.JPanel pnlButton;
    private javax.swing.JPanel pnlCategory;
    private javax.swing.JPanel pnlChart;
    private javax.swing.JPanel pnlFilter;
    private javax.swing.JPanel pnlLeft;
    private javax.swing.JPanel pnlOption;
    private javax.swing.JPanel pnlOption1;
    private javax.swing.JPanel pnlOption2;
    private javax.swing.JPanel pnlOption3;
    private javax.swing.JPanel pnlOptionName1;
    private javax.swing.JPanel pnlOptionName2;
    private javax.swing.JPanel pnlOptionName3;
    private javax.swing.JPanel pnlOptionRadio;
    private javax.swing.JPanel pnlOptions1;
    private javax.swing.JPanel pnlOptions2;
    private javax.swing.JPanel pnlOptions3;
    private javax.swing.JPanel pnlSearch;
    private javax.swing.JPanel pnlSearchAndFilter;
    private javax.swing.JPanel pnlTable;
    private javax.swing.JPanel pnlTableBottom;
    private javax.swing.JPanel pnlTableTop;
    private javax.swing.JPopupMenu rightClickMenu;
    private javax.swing.JScrollPane scrollPaneBrands;
    private javax.swing.JScrollPane scrollPaneOptions;
    private javax.swing.JScrollPane tableScrollPane;
    private javax.swing.JTextField txtSearch;
    private com.toedter.calendar.JYearChooser yearChooser;
    // End of variables declaration//GEN-END:variables
}
