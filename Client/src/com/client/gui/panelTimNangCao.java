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
import com.client.model.CategoryComboItem;
import com.client.thread.ReadThread;
import com.client.thread.WriteThread;
import com.client.util.InputValidatorUtil;
import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Rectangle;
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
import javax.swing.JRadioButton;
import javax.swing.JTable;
import javax.swing.border.MatteBorder;
import javax.swing.plaf.basic.BasicArrowButton;
import javax.swing.plaf.basic.BasicComboBoxUI;
import javax.swing.plaf.basic.BasicComboPopup;
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
    ArrayList<String> brandList = new ArrayList<>();
    ArrayList<String> selectedBrands = new ArrayList<>();
    ArrayList<String> dates = new ArrayList<String>();
    ArrayList<Integer> prices = new ArrayList<Integer>();
    InputValidatorUtil validator = new InputValidatorUtil();
    String productName = "Laptop A";
    int month = 11;
    int year = 2021;
    ArrayList<String> name = new ArrayList<String>();
    Long categoryId = 2L; //2L là id của Root - toàn bộ danh mục
    Client main;
    public panelTimNangCao(Client main) {
       
        initComponents();
        this.main = main;
        for(int i = 1; i < 31; i ++){
            dates.add(String.valueOf(i));
        }
        for (int i = 1; i < 31; i++){
            prices.add(i +100 );
        }
        
//        brandList.add("a");
//        brandList.add("b");
//        brandList.add("c");
//        brandList.add("e");
//        brandList.add("f");
//        brandList.add("g");
//        brandList.add("h");
//        brandList.add("i");
//        brandList.add("j");
//        initBrandCheckbox(brandList);
        showLineChart(productName, month, year, dates, prices);
        comboboxCategory = myComboBox(comboboxCategory, new Color(14,142,233));
        scrollPaneBrands.getVerticalScrollBar().setUI(new MyScrollBarUI());
    }
    
    // in ra cai code t doc thu
   
    public String selectedButtonGroupStar()
    {
        if(jRadioButton1.isSelected())
            return "5";
        if(jRadioButton2.isSelected())
            return "4";
        if(jRadioButton3.isSelected())
            return "3";
        if(jRadioButton4.isSelected())
            return "2";
        if(jRadioButton5.isSelected())
            return "1";
        return "0";
    }
            
    public void listNameCategory(ArrayList<String> name, ArrayList<Integer> idCate) {
        String[] tourItems = new String[name.size()];
        CategoryComboItem[] list = new CategoryComboItem[name.size()];
        
        CategoryComboItem c;
        for(int i = 0 ; i < name.size(); i++) {
             c = new CategoryComboItem(idCate.get(i), name.get(i).equals("Root") ? "-- Chọn danh mục --" : name.get(i));
             list[i] = c;
            // System.out.println(c.getId());
        }
         setComboBox(comboboxCategory, list);
    }
    
    public void setComboBox(JComboBox<CategoryComboItem> comboBox, CategoryComboItem[] listItems) {
        comboBox.setModel(new DefaultComboBoxModel<>(listItems));
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
         
    }
    
    public void setTable(List<List<LinkedHashMap<String, Object>>> listAdvanceProducts)
    {
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("Id");
        model.addColumn("Tên");
        model.addColumn("Số lượng đã bán");
        model.addColumn("Giá");
        List<Integer> id = new ArrayList<>();
        List<String> name = new ArrayList<>();
        for(List<LinkedHashMap<String, Object>> advanceProduct : listAdvanceProducts)
        {
            for(LinkedHashMap<String, Object> product : advanceProduct)
            {
                Vector row = new Vector();
                row.add(product.get("id"));
                row.add(product.get("name"));
                row.add(product.get("all_time_quantity_sold"));
                row.add(product.get("price"));
                model.addRow(row);
            }
        }
        advanceProductTable.setModel(model);
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
     
    public void updateBrandCheckbox(HashMap<Integer, String> listBrands, int numberOfBrands)
    {
        selectedBrands.clear();
        pnlBrands.removeAll();
        pnlBrands.revalidate();
        pnlBrands.repaint();
        int checkBoxWidth = 200;
        int checkBoxHeight = 40;
        int xColumn1 = 10;
        int xColumn2 = xColumn1 + checkBoxWidth + 40;
        int y1 = 15; 
        for(int i=0;i<numberOfBrands;i++)
        {   
            JCheckBox checkBox = new JCheckBox(String.valueOf(listBrands.get(i)));
           // checkBox.setBounds(xColumn1, y1, checkBoxWidth, checkBoxHeight); 
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
      
        
//        int y2 = 15; 
//        for(int i=numberOfBrands/2; i<numberOfBrands;i++)
//        {   
//            
//            System.out.println(listBrands.get(i));
//            JCheckBox checkBox = new JCheckBox(listBrands.get(i));
//            checkBox.setBounds(xColumn2, y2, checkBoxWidth, checkBoxHeight); // dòng này nó làm gì?
//            checkBox.addItemListener(new ItemListener() {
//
//            @Override
//            public void itemStateChanged(ItemEvent e) {
//                if (e.getStateChange() == ItemEvent.SELECTED)
//                {
//                    selectedBrands.add(checkBox.getText());
//                } else {
//                    selectedBrands.remove(checkBox.getText());
//                }                 
//            }
//            });
//            pnlBrands.add(checkBox);
//            y2 += 25;
//        }
    }
    
    public void initBrandCheckbox(ArrayList<String> list){
        int y = 15; // 15 chu cai dau tien cua brands 
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
             ItemLabelAnchor.CENTER, TextAnchor.BASELINE_LEFT, TextAnchor.BASELINE_LEFT, 
             - Math.PI / 4)); 
        
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
        jRadioButton4 = new javax.swing.JRadioButton();
        jRadioButton5 = new javax.swing.JRadioButton();
        pnlTable = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        advanceProductTable = new javax.swing.JTable();
        pnlChart = new javax.swing.JPanel();

        setBackground(new java.awt.Color(255, 255, 255));
        setBorder(javax.swing.BorderFactory.createMatteBorder(1, 0, 0, 0, new java.awt.Color(0, 0, 0)));
        setPreferredSize(new java.awt.Dimension(1280, 720));
        setLayout(new java.awt.BorderLayout());

        pnlLeft.setPreferredSize(new java.awt.Dimension(600, 493));
        pnlLeft.setLayout(new java.awt.BorderLayout());

        pnlSearchAndFilter.setBackground(new java.awt.Color(255, 255, 255));
        pnlSearchAndFilter.setPreferredSize(new java.awt.Dimension(400, 300));
        pnlSearchAndFilter.setLayout(new java.awt.BorderLayout());

        pnlSearch.setBackground(new java.awt.Color(255, 255, 255));
        pnlSearch.setPreferredSize(new java.awt.Dimension(600, 50));

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
                .addContainerGap()
                .addComponent(lblTitleTenSanPham)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(txtSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(comboboxCategory, javax.swing.GroupLayout.PREFERRED_SIZE, 212, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(27, 27, 27))
        );
        pnlSearchLayout.setVerticalGroup(
            pnlSearchLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlSearchLayout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addGroup(pnlSearchLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblTitleTenSanPham, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtSearch, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(comboboxCategory, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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
        jPanel6.setPreferredSize(new java.awt.Dimension(280, 240));

        jLabel7.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel7.setText("Chọn khoảng giá:");

        jLabel8.setText("Từ:");

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
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(46, 46, 46)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel9)
                    .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel8)
                            .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, 199, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, 199, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jButton2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jButton1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 111, Short.MAX_VALUE))))
                .addContainerGap(35, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(114, 114, 114))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10)
                .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(5, 5, 5)
                .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, 26, Short.MAX_VALUE)
                .addGap(10, 10, 10))
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

        buttonGroupStar.add(jRadioButton4);
        jRadioButton4.setText("Từ 1 sao");

        buttonGroupStar.add(jRadioButton5);
        jRadioButton5.setText("Từ 2 sao");

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jRadioButton4)
                    .addComponent(jRadioButton2)
                    .addComponent(jRadioButton1)
                    .addComponent(jLabel6)
                    .addComponent(jRadioButton5)
                    .addComponent(jRadioButton3))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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
                .addComponent(jRadioButton5)
                .addGap(14, 14, 14)
                .addComponent(jRadioButton4)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pnlFilter.add(jPanel7, java.awt.BorderLayout.CENTER);

        pnlSearchAndFilter.add(pnlFilter, java.awt.BorderLayout.CENTER);

        pnlLeft.add(pnlSearchAndFilter, java.awt.BorderLayout.PAGE_START);

        pnlTable.setBackground(new java.awt.Color(255, 255, 255));
        pnlTable.setLayout(new java.awt.BorderLayout());

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
        });
        jScrollPane1.setViewportView(advanceProductTable);

        pnlTable.add(jScrollPane1, java.awt.BorderLayout.CENTER);

        pnlLeft.add(pnlTable, java.awt.BorderLayout.CENTER);

        add(pnlLeft, java.awt.BorderLayout.LINE_START);

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
            System.out.println(categoryId);
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
        String productName = txtSearch.getText();
        String valid = null;
        String rating = selectedButtonGroupStar();
//        if(rating.equals("0"))
//        {
//            System.out.println("Hiện thông báo lỗi: Bạn chưa chọn đánh giá");
//            return;
//        }
        String fromMoney = jTextField3.getText();
        valid = validator.isValidMoney(fromMoney);
        if(valid.equals("Số tiền không hợp lệ") || valid.equals("Số tiền phải lớn hơn 0"))
        {
            System.out.println("Hiện thông báo lỗi: Giá từ" + valid);
            return;
        }
        if(valid.equals(" không được để trống"))
            fromMoney = "";
        String toMoney = jTextField4.getText();
        valid = validator.isValidMoney(toMoney);
        if(valid.equals("Số tiền không hợp lệ") || valid.equals("Số tiền phải lớn hơn 0"))
        {
            System.out.println("Hiện thông báo lỗi: Giá đến" + valid);
            return;
        }
        if(valid.equals(" không được để trống"))
            toMoney = "";
        Long money = 0l;
        if(!fromMoney.equals("") && !toMoney.equals(""))
        {
            money = Long.parseLong(toMoney) - Long.parseLong(fromMoney);
            if(money < 0)
            {
                System.out.println("Hiện thông báo lỗi: Giá từ phải nhỏ hơn Giá đến");
                return;
            }
        }
        String brands = "";
        if(selectedBrands.size() == 0)
            brands += "a-b-c-d-e-f-g-h-i-j-k-l-m-n-o-p-q-r-s-t-u-v-w-x-y-z\"";
        else if(selectedBrands.size() == 1)
            brands += selectedBrands.get(0);
        else
        {
            for(int i=0;i<selectedBrands.size();i++)
            {
                if(i==selectedBrands.size()-1)
                    brands+= selectedBrands.get(i);
                else
                    brands+= selectedBrands.get(i) + "-";
            }
        }
        String category = comboboxCategory.getSelectedItem().toString();
        try {
            main.sendRequestAdvanceProducts(productName, category, brands, rating, fromMoney, toMoney);
        } catch (IOException ex) {
            Logger.getLogger(panelTimTheoURL.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButton2MouseClicked

    private void advanceProductTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_advanceProductTableMouseClicked

        int row = advanceProductTable.getSelectedRow();
        Long id = Long.parseLong(advanceProductTable.getModel().getValueAt(row, 0).toString());
        try {
            main.getProductHistoriesById(id, month, year);
        } catch (IOException ex) {
            Logger.getLogger(panelTimNangCao.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_advanceProductTableMouseClicked

    private void comboboxCategoryMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_comboboxCategoryMouseClicked

    }//GEN-LAST:event_comboboxCategoryMouseClicked


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTable advanceProductTable;
    private javax.swing.ButtonGroup buttonGroupStar;
    private javax.swing.JComboBox<CategoryComboItem> comboboxCategory;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JRadioButton jRadioButton1;
    private javax.swing.JRadioButton jRadioButton2;
    private javax.swing.JRadioButton jRadioButton3;
    private javax.swing.JRadioButton jRadioButton4;
    private javax.swing.JRadioButton jRadioButton5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField jTextField4;
    private javax.swing.JLabel lblTitleTenSanPham;
    private javax.swing.JPanel pnlBrands;
    private javax.swing.JPanel pnlCategory;
    private javax.swing.JPanel pnlChart;
    private javax.swing.JPanel pnlFilter;
    private javax.swing.JPanel pnlLeft;
    private javax.swing.JPanel pnlSearch;
    private javax.swing.JPanel pnlSearchAndFilter;
    private javax.swing.JPanel pnlTable;
    private javax.swing.JScrollPane scrollPaneBrands;
    private javax.swing.JTextField txtSearch;
    // End of variables declaration//GEN-END:variables
}
