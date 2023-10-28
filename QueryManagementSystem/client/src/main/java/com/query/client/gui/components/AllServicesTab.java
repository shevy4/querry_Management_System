package com.query.client.gui.components;

import java.awt.Color;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTree;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.PieSectionLabelGenerator;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.query.client.gui.components.helper.CategoryJtree;
import com.query.client.service.Client;
import com.query.client.util.IssueStatus;
import com.query.client.util.Response;
import com.query.client.util.Util;

@Service
public class AllServicesTab {
    Logger logger = LoggerFactory.getLogger(AllServicesTab.class);
    private final JPanel panel = new JPanel();
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private JSplitPane splitPane;
    private JButton categoryButton;
    @Autowired
    CategoryJtree categoryJTree;

    private List<String> complaintList = new ArrayList<>();
    private List<String> queryList = new ArrayList<>();

    @Autowired
    Client clientService;

    @Autowired
    SupervisorDashboard supervisorDashboard;

    Util util = new Util();
    private JButton queryButton;
    private JTree jt;

    JPanel createAllServicesTab() {
        panel.removeAll();
        panel.setLayout(new GridLayout(0, 1));
        jScrollPane1 = new javax.swing.JScrollPane();
        jScrollPane2 = new javax.swing.JScrollPane();
        splitPane = new JSplitPane();
        jt = getComplaintAndQueryList();

        jScrollPane1.setViewportView(jt);

        createPane2();
        splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, jScrollPane1, jScrollPane2);
        splitPane.setDividerLocation(300);
        panel.add(splitPane);
        return panel;
    }

    public JPanel getPanel() {
        this.createAllServicesTab();
        return panel;
    }

    private JTree getComplaintAndQueryList() {
        // get all complaints / queries
        clientService.sendRequest(util.parseRequestString("", "GET_ALL_COMPLAINT_CATEGORY"));
        Response complaintListResponse = clientService.getResponse();
        clientService.sendRequest(util.parseRequestString("", "GET_ALL_QUERY_CATEGORY"));
        Response queryListResponse = clientService.getResponse();
        try {
            var complaintList = new ObjectMapper().readValue(complaintListResponse.getData(),
                    new TypeReference<List<String>>() {
                    });
            if (complaintList != null) {
                this.complaintList = complaintList;
            }
            var queryList = new ObjectMapper().readValue(queryListResponse.getData(),
                    new TypeReference<List<String>>() {
                    });
            if (queryList != null) {
                this.queryList = queryList;
            }
        } catch (JsonProcessingException e) {

        }
        return categoryJTree.createJTree(complaintList, queryList);
    }

    private void createPane2() {
        jScrollPane2.setLayout(null);
        categoryButton = createIssueButton("Complaint");
        categoryButton.setBounds(150, 30, 150, 50);
        queryButton = createIssueButton("Query");
        queryButton.setBounds(400, 30, 150, 50);
        JFreeChart chart = createChart(createDataset(getIssueStatus()));
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setBounds(100, 110, 490, 400);
        ;
        chartPanel.setSize(490, 350);
        jScrollPane2.add(categoryButton);
        ;
        jScrollPane2.add(queryButton);
        ;
        jScrollPane2.add(chartPanel);
    }

    private JButton createIssueButton(String type) {
        JButton jButton = new JButton("Add " + type);
        jButton.addActionListener((event) -> {
            String result = (String) JOptionPane.showInputDialog(jButton, "Describe Service", "Add " + type,
                    JOptionPane.PLAIN_MESSAGE, null, null, "");
            if (result == null || result.equals("")) {
                return;
            }
            clientService.sendRequest(util.parseRequestString(result, "ADD_" + type.toUpperCase() + "_CATEGORY"));
            Response complaintListResponse = clientService.getResponse();

            if (complaintListResponse.getResponseType().equals("ERROR")) {
                JOptionPane.showMessageDialog(jButton, complaintListResponse.getMessage(), "Alert",
                        JOptionPane.ERROR_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(jButton, complaintListResponse.getMessage());
                supervisorDashboard.addTabs();
            }
        });
        return jButton;
    }

    private static PieDataset createDataset(IssueStatus issueStatus) {
        DefaultPieDataset dataset = new DefaultPieDataset();
        var resolved = issueStatus.getResolved();
        var total = issueStatus.getTotal();
        var created = issueStatus.getCreated();
        var inProgress = issueStatus.getInProgress();
        dataset.setValue("Resolved", resolved);
        dataset.setValue("Created", created);
        dataset.setValue("In Progress", inProgress);

        return dataset;
    }

    private static JFreeChart createChart(PieDataset dataset) {
        JFreeChart chart = ChartFactory.createPieChart3D("All Issues", // chart title
                dataset, // data
                true, // include legend
                false, false);

        PiePlot plot = (PiePlot) chart.getPlot();
        plot.setSectionPaint("Resolved", Color.GREEN);
        plot.setSectionPaint("Created", Color.RED);
        plot.setSectionPaint("In Progress", new Color(160, 160, 255));
        plot.setExplodePercent("Resolved", 0.30);
        plot.setForegroundAlpha(0.6f);
        PieSectionLabelGenerator labelGenerator = new StandardPieSectionLabelGenerator("{0} {1} ( {2} )");
        plot.setLabelGenerator(labelGenerator);
        plot.setBackgroundPaint(Color.WHITE);
        return chart;
    }

    private IssueStatus getIssueStatus() {
        clientService.sendRequest(util.parseRequestString("", "GET_ISSUE_STATUS"));
        Response statusResponse = clientService.getResponse();
        try {
            return new ObjectMapper().readValue(statusResponse.getData(), IssueStatus.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return new IssueStatus();
        }
    }

}
