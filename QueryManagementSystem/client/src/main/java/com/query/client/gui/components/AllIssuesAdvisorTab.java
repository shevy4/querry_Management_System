package com.query.client.gui.components;

import java.awt.BorderLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.regex.PatternSyntaxException;
import java.util.stream.Collectors;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.RowFilter;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.query.client.gui.components.helper.ButtonRenderer;
import com.query.client.gui.components.helper.TableModelCreator;
import com.query.client.service.Client;
import com.query.client.util.Issue;
import com.query.client.util.IssueBrief3;
import com.query.client.util.Util;

@Service
public class AllIssuesAdvisorTab {

    Logger logger = LoggerFactory.getLogger(ViewPastIssueTab.class);
    private final JPanel panel = new JPanel();
    List<IssueBrief3> issues;
    List<Issue> allIssues;

    public void setAllIssues(List<Issue> issues) {
        this.allIssues = issues;
    }

    public void setIssues(List<IssueBrief3> issueBrief) {
        this.issues = issueBrief;
    }

    @Autowired
    IssueDetailFrame issueDetailFrame;

    @Autowired
    Client clientService;

    ObjectMapper mapper = new ObjectMapper();

    Util util = new Util();

    public AllIssuesAdvisorTab() {
        super();
    }

    public JPanel createAllIssueTab() {
        panel.setLayout(new BorderLayout());
        panel.removeAll();

        TableModel tableModel = TableModelCreator.createTableModel(IssueBrief3.class, issues);
        JTable table = new JTable(tableModel);
        table.getColumn(table.getColumnName(0)).setCellRenderer(new ButtonRenderer());
        table.getColumn(table.getColumnName(9)).setCellRenderer(new ButtonRenderer());
        var sorter = new TableRowSorter<TableModel>(tableModel);
        table.setRowSorter(sorter);
        table.addMouseListener(new MouseClick());
        JScrollPane pane = new JScrollPane(table);

        panel.add(pane, BorderLayout.CENTER);

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

        panel.add(selectPanel, BorderLayout.NORTH);

        return panel;
    }

    public JPanel getPanel() {
        this.createAllIssueTab();
        return panel;
    }

    public class MouseClick extends MouseAdapter {
        @Override
        public void mouseClicked(MouseEvent event) {
            JTable target = (JTable) event.getSource();
            int row = target.getSelectedRow();
            int column = target.getSelectedColumn();
            if (column == 0) {
                Issue issue = allIssues.stream().filter(e -> e.getId().equals(target.getValueAt(row, 4)))
                        .collect(Collectors.toList()).get(0);
                JFrame frame = issueDetailFrame.addData(issue, true);
                frame.setVisible(true);
            }

            if (column == 9) {
                Issue issue = allIssues.stream().filter(e -> e.getId().equals(target.getValueAt(row, 4)))
                        .collect(Collectors.toList()).get(0);
                var studentDetailFrame = new StudentDetailFrame();
                studentDetailFrame.addData(issue.getStudent());
                studentDetailFrame.setVisible(true);
            }
        }
    }

}
