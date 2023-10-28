package com.query.client.gui.components;

import java.awt.Color;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.Border;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.query.client.service.Client;
import com.query.client.util.Issue;
import com.query.client.util.Response;
import com.query.client.util.Util;

@Service
public class RaiseComplaintTab {
    Logger logger = LoggerFactory.getLogger(RaiseComplaintTab.class);

    private JComboBox<String> categoryCombo;
    private JComboBox<String> natureCombo;
    private JTextArea descriptionTextField;
    @SuppressWarnings("unchecked")
    private ComboBoxModel<String>[] models = new ComboBoxModel[2];
    private JScrollPane sbrText;
    private final JPanel panel = new JPanel();
    private JButton submitBtn;
    String[] complaintList = {};
    String[] queryList = {};

    public void setComplaintList(String[] complaintList) {
        this.complaintList = complaintList;
    }

    public void setQueryList(String[] queryList) {
        this.queryList = queryList;
    }

    @Autowired
    Client clientService;

    @Autowired
    StudentDashboard studentDashboard;

    ObjectMapper mapper = new ObjectMapper();

    Util util = new Util();

    public JPanel createRaiseComplaintTab() {
        panel.removeAll();
        var categoryLabel = new JLabel("Choose the type of Service : ");
        categoryLabel.setFont(new Font("Times New Roman", Font.PLAIN, 20));
        categoryLabel.setBounds(200, 10, 250, 50);

        String options[] = { "Complaint", "Query" };
        // create checkbox
        categoryCombo = new JComboBox<String>(options);
        categoryCombo.setBounds(470, 20, 250, 30);
        categoryCombo.addItemListener((event) -> {
            int i = categoryCombo.getSelectedIndex();
            natureCombo.setModel(models[i]);
        });

        var natureLabel = new JLabel("Nature / Category : ");
        natureLabel.setFont(new Font("Times New Roman", Font.PLAIN, 20));
        natureLabel.setBounds(200, 50, 250, 50);

        models[0] = new DefaultComboBoxModel<String>(complaintList);
        models[1] = new DefaultComboBoxModel<String>(queryList);

        natureCombo = new JComboBox<String>();
        natureCombo.setBounds(470, 60, 250, 30);
        natureCombo.setModel(models[0]);

        var descriptionLabel = new JLabel("Description : ");
        descriptionLabel.setFont(new Font("Times New Roman", Font.PLAIN, 20));
        descriptionLabel.setBounds(200, 90, 250, 50);

        descriptionTextField = new JTextArea();
        descriptionTextField.setFont(new Font("Times New Roman", Font.PLAIN, 20));
        descriptionTextField.setWrapStyleWord(true);
        descriptionTextField.setLineWrap(true);
        sbrText = new JScrollPane(descriptionTextField);
        sbrText.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        sbrText.setBounds(200, 140, 520, 200);

        Border border = BorderFactory.createLineBorder(Color.BLACK);
        descriptionTextField
                .setBorder(BorderFactory.createCompoundBorder(border, BorderFactory.createEmptyBorder(10, 10, 10, 10)));

        submitBtn = new JButton("Submit");
        submitBtn.setBounds(385, 400, 150, 30);
        submitBtn.addActionListener((event) -> {
            addIssue();
            studentDashboard.addIssuesTab();
        });

        panel.setLayout(null);
        panel.add(categoryLabel);
        panel.add(categoryCombo);
        panel.add(natureLabel);
        panel.add(natureCombo);
        panel.add(descriptionLabel);
        panel.add(sbrText);
        panel.add(submitBtn);
        return panel;
    }

    public JPanel getPanel() {
        this.createRaiseComplaintTab();
        return panel;
    }

    private void addIssue() {
        Issue issue = new Issue();
        issue.setDescription(descriptionTextField.getText());
        issue.setIssueType(categoryCombo.getSelectedItem().toString());
        issue.setNature(natureCombo.getSelectedItem().toString());
        issue.setStatus("CREATED");
        JSONObject parseRequestString;
        try {
            parseRequestString = util.parseRequestString(mapper.writeValueAsString(issue), "ADD_ISSUE");
            clientService.sendRequest(parseRequestString);
        } catch (JsonProcessingException e) {
            logger.info(e.getMessage());
        }

        Response response = clientService.getResponse();
        if (response.getResponseType().equals("ERROR")) {
            JOptionPane.showMessageDialog(submitBtn, response.getMessage(), "Alert", JOptionPane.ERROR_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(submitBtn,
                    "Your " + categoryCombo.getSelectedItem() + " has been submitted successfully !");
        }

    }

}
