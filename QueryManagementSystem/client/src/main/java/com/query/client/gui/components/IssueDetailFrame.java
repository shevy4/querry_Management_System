package com.query.client.gui.components;

import java.awt.Color;
import java.awt.Font;
import java.awt.ScrollPane;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.query.client.service.Client;
import com.query.client.util.Issue;
import com.query.client.util.Response;
import com.query.client.util.Util;

@Service
public class IssueDetailFrame {

    Logger logger = LoggerFactory.getLogger(IssueDetailFrame.class);

    private JSplitPane splitpane;

    private JPanel detailspanel;

    private JTextField advisorField;

    Util util = new Util();
    private JFrame jFrame = new JFrame();

    @Autowired
    Client clientService;

    @Autowired
    AdvisorDashboard advisorDashboard;

    ObjectMapper mapper = new ObjectMapper();

    private JComboBox<String> statusCombo;

    public JFrame addData(Issue issue, boolean editable) {
        jFrame = new JFrame();
        jFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        jFrame.setBounds(450, 190, 1000, 600);
        jFrame.setResizable(false);
        JLabel newLabel = new JLabel();
        JTextField textField = new JTextField();
        Font font = new Font("Times New Roman", Font.PLAIN, 15);
        var pane1 = new ScrollPane();
        var pane2 = new ScrollPane();

        // left side
        var issueDescriptionPane = new JPanel();
        issueDescriptionPane.setLayout(null);
        issueDescriptionPane.setBorder(
                new CompoundBorder(BorderFactory.createTitledBorder("Issue"), new EmptyBorder(20, 10, 20, 10)));
        issueDescriptionPane.setFont(font);
        newLabel = new JLabel("Issue Id");
        newLabel.setFont(font);
        newLabel.setBounds(30, 20, 100, 30);
        issueDescriptionPane.add(newLabel);

        textField = new JTextField(issue.getId());
        textField.setFont(font);
        textField.setEditable(false);
        textField.setBackground(Color.WHITE);
        textField.setBounds(160, 20, 250, 30);
        issueDescriptionPane.add(textField);

        newLabel = new JLabel("Student Id");
        newLabel.setFont(font);
        newLabel.setBounds(30, 80, 100, 30);
        issueDescriptionPane.add(newLabel);

        textField = new JTextField(issue.getStudent().getStudentId());
        textField.setFont(font);
        textField.setEditable(false);
        textField.setBackground(Color.WHITE);
        textField.setBounds(160, 80, 250, 30);
        issueDescriptionPane.add(textField);

        newLabel = new JLabel("Student Name");
        newLabel.setFont(font);
        newLabel.setBounds(30, 140, 100, 30);
        issueDescriptionPane.add(newLabel);

        textField = new JTextField(issue.getStudent().getName());
        textField.setFont(font);
        textField.setEditable(false);
        textField.setBackground(Color.WHITE);
        textField.setBounds(160, 140, 250, 30);
        issueDescriptionPane.add(textField);

        newLabel = new JLabel("Category");
        newLabel.setFont(font);
        newLabel.setBounds(30, 200, 100, 30);
        issueDescriptionPane.add(newLabel);

        textField = new JTextField(issue.getIssueType());
        textField.setFont(font);
        textField.setEditable(false);
        textField.setBounds(160, 200, 250, 30);
        textField.setBackground(Color.WHITE);

        issueDescriptionPane.add(textField);

        newLabel = new JLabel("Nature");
        newLabel.setFont(font);
        newLabel.setBounds(30, 260, 100, 30);
        issueDescriptionPane.add(newLabel);

        textField = new JTextField(issue.getNature());
        textField.setFont(font);
        textField.setEditable(false);
        textField.setBounds(160, 260, 250, 30);
        textField.setBackground(Color.WHITE);

        issueDescriptionPane.add(textField);

        newLabel = new JLabel("Description");
        newLabel.setFont(font);
        newLabel.setBounds(30, 320, 100, 30);
        issueDescriptionPane.add(newLabel);

        var descriptionTextField = new JTextArea(issue.getDescription());
        descriptionTextField.setFont(font);
        descriptionTextField.setEditable(false);
        descriptionTextField.setBackground(Color.WHITE);
        var sbrText = new JScrollPane(descriptionTextField);
        sbrText.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        sbrText.setBounds(160, 320, 250, 100);
        issueDescriptionPane.add(sbrText);

        pane1.add(issueDescriptionPane);

        // right side
        detailspanel = new JPanel();
        detailspanel.setBorder(
                new CompoundBorder(BorderFactory.createTitledBorder("Resolution"), new EmptyBorder(20, 10, 20, 10)));

        detailspanel.setLayout(null);

        newLabel = new JLabel("Resolution Status :");
        newLabel.setFont(font);
        newLabel.setBounds(30, 20, 150, 30);
        detailspanel.add(newLabel);

        if (editable) {
            String options[] = { "CREATED", "IN PROGRESS", "RESOLVED" };
            // create checkbox
            statusCombo = new JComboBox<String>(options);
            statusCombo.setBounds(200, 20, 200, 30);
            statusCombo.setSelectedItem(issue.getStatus());
            detailspanel.add(statusCombo);
        } else {
            var status = new JTextField(issue.getStatus());
            status.setFont(font);
            status.setEditable(editable);
            status.setBounds(200, 20, 200, 30);
            detailspanel.add(status);
        }

        newLabel = new JLabel("Last Response Date : ");
        newLabel.setFont(font);
        newLabel.setBounds(30, 80, 150, 30);
        detailspanel.add(newLabel);

        DateFormat dateFormat = new SimpleDateFormat("MMM d, yyyy");
        var date = new JFormattedTextField(dateFormat);
        date.setFont(font);
        date.setEditable(false);
        date.setBounds(200, 80, 200, 30);
        date.setValue(issue.getLastResponseDate());
        detailspanel.add(date);

        newLabel = new JLabel("Advisor Name : ");
        newLabel.setFont(font);
        newLabel.setBounds(30, 140, 150, 30);
        detailspanel.add(newLabel);

        advisorField = new JTextField(issue.getAdvisor() == null ? null : issue.getAdvisor().getAdvisorName());
        advisorField.setFont(font);
        advisorField.setEditable(false);
        advisorField.setBounds(200, 140, 200, 30);
        detailspanel.add(advisorField);

        newLabel = new JLabel("Resolution Comments : ");
        newLabel.setFont(font);
        newLabel.setBounds(30, 200, 150, 30);
        detailspanel.add(newLabel);

        var resolutionArea = new JTextArea(issue.getComments());
        resolutionArea.setFont(font);
        resolutionArea.setEditable(editable);
        resolutionArea.setWrapStyleWord(true);
        resolutionArea.setLineWrap(true);
        sbrText = new JScrollPane(resolutionArea);
        sbrText.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        sbrText.setBounds(200, 200, 200, 150);
        detailspanel.add(sbrText);

        if (editable) {
            var jButton = new JButton("Save");
            jButton.setBounds(150, 410, 100, 30);
            jButton.addActionListener((event) -> {
                try {
                    issue.setComments(resolutionArea.getText());
                    issue.setStatus(statusCombo.getSelectedItem().toString());
                    clientService
                            .sendRequest(util.parseRequestString(mapper.writeValueAsString(issue), "SAVE_COMMENTS"));
                } catch (Exception e) {
                    logger.error(e.getMessage());
                    JOptionPane.showMessageDialog(jButton, "Something Went Wrong", "Alert", JOptionPane.ERROR_MESSAGE);
                }

                Response response = clientService.getResponse();
                if (response.getResponseType().equals("ERROR")) {
                    JOptionPane.showMessageDialog(jButton, response.getMessage(), "Alert", JOptionPane.ERROR_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(jButton, response.getMessage());
                    advisorDashboard.addTabs();
                }
                jFrame.dispose();
            });
            detailspanel.add(jButton);
        }

        pane2.add(detailspanel);

        splitpane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, pane1, pane2);
        splitpane.setDividerLocation(500);
        jFrame.add(splitpane);
        return jFrame;
    }

}
