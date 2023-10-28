package com.query.client.gui.components;

import java.awt.BorderLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.PatternSyntaxException;
import java.util.stream.Collectors;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.RowFilter;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import javax.swing.tree.DefaultMutableTreeNode;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.query.client.gui.components.helper.ButtonRenderer;
import com.query.client.gui.components.helper.CategoryJtree;
import com.query.client.gui.components.helper.TableModelCreator;
import com.query.client.service.Client;
import com.query.client.util.AddAdvisor;
import com.query.client.util.Issue;
import com.query.client.util.IssueBrief2;
import com.query.client.util.Response;
import com.query.client.util.Util;

@Service
public class AllIssuesTab {
    Logger logger = LoggerFactory.getLogger(ViewPastIssueTab.class);
    private final JPanel panel = new JPanel();
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable jTable1;

    private List<IssueBrief2> issues;

    List<Issue> allIssues;

    private List<String> complaintList = new ArrayList<>();
    private List<String> queryList = new ArrayList<>();

    public void setAllIssues(List<Issue> issues) {
        this.allIssues = issues;
    }

    public void setIssues(List<IssueBrief2> issueBrief) {
        this.issues = issueBrief;
    }

    @Autowired
    Client clientService;

    @Autowired
    CategoryJtree categoryJTree;

    @Autowired
    SupervisorDashboard supervisorDashboard;

    @Autowired
    IssueDetailFrame issueDetailFrame;

    ObjectMapper mapper = new ObjectMapper();

    Util util = new Util();
    private TableRowSorter<TableModel> sorter;
    private JTree jt;

    public JPanel createViewPastIssueTab() {
        panel.removeAll();
        panel.setLayout(new BorderLayout());
        jScrollPane1 = new javax.swing.JScrollPane();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jt = getComplaintAndQueryList();

        jScrollPane1.setViewportView(jt);

        var rightPanel = new JPanel();
        rightPanel.setLayout(new BorderLayout());

        TableModel tableModel = TableModelCreator.createTableModel(IssueBrief2.class, issues);
        jTable1 = new JTable(tableModel);
        sorter = new TableRowSorter<TableModel>(tableModel);
        jTable1.setRowSorter(sorter);
        jTable1.getColumn(jTable1.getColumnName(0)).setCellRenderer(new ButtonRenderer());
        jTable1.getColumn(jTable1.getColumnName(1)).setCellRenderer(new ButtonRenderer());
        jTable1.addMouseListener(new MouseClick());
        jScrollPane2.setViewportView(jTable1);
        rightPanel.add(jScrollPane2, BorderLayout.CENTER);
        // add search bar
        JPanel selectPanel = new JPanel(new BorderLayout());

        var btnJpanel = new JPanel();
        JButton searchBtn = new JButton("Search");
        searchBtn.setToolTipText("Search for Particular Issue");
        btnJpanel.add(searchBtn);
        JButton refreshBtn = new JButton("Refresh");
        refreshBtn.setToolTipText("Remove Search Criterion");
        btnJpanel.add(refreshBtn);
        selectPanel.add(btnJpanel, BorderLayout.EAST);

        Image image = new ImageIcon(getClass().getClassLoader().getResource("search.png")).getImage();
        image = image.getScaledInstance(20, 20, java.awt.Image.SCALE_SMOOTH);
        JButton iconButton = new JButton(new ImageIcon(image));
        selectPanel.add(iconButton, BorderLayout.WEST);
        final JTextField filterText = new JTextField("");
        selectPanel.add(filterText, BorderLayout.CENTER);
        searchBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String text = filterText.getText();
                if (text.length() == 0) {
                    sorter.setRowFilter(null);
                } else {
                    try {
                        sorter.setRowFilter(RowFilter.regexFilter("(?i)" + text));
                    } catch (PatternSyntaxException pse) {
                        System.out.println("Bad regex pattern");
                    }
                }
            }
        });

        refreshBtn.addActionListener((event) -> {
            sorter.setRowFilter(null);
            filterText.setText("");
        });

        rightPanel.add(selectPanel, BorderLayout.NORTH);

        jt.addTreeSelectionListener(new TreeSelectionListener() {
            @Override
            public void valueChanged(TreeSelectionEvent evt) {
                DefaultMutableTreeNode node = (DefaultMutableTreeNode) jt.getLastSelectedPathComponent();
                if (node == null)
                    return;
                var query = node.getUserObject().toString();
                try {
                    if (query.equals("Issues")) {
                        query = "";
                        sorter.setRowFilter(RowFilter.regexFilter("(?i)" + query));
                    } else if (query.equals("Complaints")) {
                        query = "Complaint";
                        sorter.setRowFilter(RowFilter.regexFilter("(?i)" + query, 3));
                    } else if (query.equals("Queries")) {
                        query = "Query";
                        sorter.setRowFilter(RowFilter.regexFilter("(?i)" + query, 3));
                    } else {

                        sorter.setRowFilter(RowFilter.regexFilter("(?i)" + query, 6));
                    }
                } catch (PatternSyntaxException pse) {
                    System.out.println("Bad regex pattern");
                }
            }
        });

        JSplitPane splitpane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, jScrollPane1, rightPanel);
        splitpane.setDividerLocation(180);
        panel.add(splitpane);
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

    public JPanel getPanel() {
        this.createViewPastIssueTab();
        return panel;
    }

    public class MouseClick extends MouseAdapter {
        @Override
        public void mouseClicked(MouseEvent event) {
            JTable target = (JTable) event.getSource();
            int row = target.getSelectedRow();
            int column = target.getSelectedColumn();
            if (column == 0) {
                Issue issue = allIssues.stream().filter(e -> e.getId() == target.getValueAt(row, 5))
                        .collect(Collectors.toList()).get(0);
                JFrame frame = issueDetailFrame.addData(issue, false);
                frame.setVisible(true);

            }
            if (column == 1) {
                Object[] options = addAdvisor();
                Icon icon = null;
                String s = (String) JOptionPane.showInputDialog(target, "Choose Advisor", "Assign Advisor",
                        JOptionPane.PLAIN_MESSAGE, icon, options, options[0]);
                if (s != null) {
                    saveAdvisor(s, target.getValueAt(row, 5).toString());
                    refreshData();
                    supervisorDashboard.addTabs();

                }
            }
        }

        private void saveAdvisor(String advisorName, String issueId) {
            try {
                clientService.sendRequest(util.parseRequestString(
                        mapper.writeValueAsString(new AddAdvisor(advisorName, Integer.valueOf(issueId))),
                        "ASSIGN_ADVISOR"));
                Response response = clientService.getResponse();
                if (response.getResponseType().equals("ERROR")) {
                    JOptionPane.showMessageDialog(jTable1, response.getMessage(), "Alert", JOptionPane.ERROR_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(jTable1, response.getMessage());
                }

            } catch (JsonProcessingException e) {
                e.printStackTrace();
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }

        }
    }

    private String[] addAdvisor() {
        clientService.sendRequest(util.parseRequestString("", "GET_ALL_ADVISORS"));
        Response response = clientService.getResponse();
        String[] advisorNameList = {};
        if (!response.getResponseType().equals("ERROR")) {
            try {
                advisorNameList = new ObjectMapper().readValue(response.getData(), String[].class);
            } catch (JsonMappingException e) {
                e.printStackTrace();
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }
        return advisorNameList;
    }

    private void refreshData() {
        clientService.sendRequest(util.parseRequestString("", "GET_ALL_ISSUES"));
        Response issueResponse = clientService.getResponse();

        try {
            List<Issue> issues = new ObjectMapper().readValue(issueResponse.getData(),
                    new TypeReference<List<Issue>>() {
                    });

            List<IssueBrief2> issueBrief = issues.stream()
                    .map(a -> new IssueBrief2(a.getId(), a.getNature(), a.getIssueType(), a.getDescription(),
                            a.getAdvisor() == null ? null : a.getAdvisor().getAdvisorName(), "View", "Add Advisor",
                            a.getStudent() == null ? null : a.getStudent().getName()))
                    .collect(Collectors.toList());
            setIssues(issueBrief);
            setAllIssues(issues);
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        TableModel tableModel = TableModelCreator.createTableModel(IssueBrief2.class, issues);
        jTable1.setModel(tableModel);
        jTable1.getColumn(jTable1.getColumnName(0)).setCellRenderer(new ButtonRenderer());
        jTable1.getColumn(jTable1.getColumnName(1)).setCellRenderer(new ButtonRenderer());
    }
}
