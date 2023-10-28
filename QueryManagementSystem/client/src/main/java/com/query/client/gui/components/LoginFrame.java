package com.query.client.gui.components;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.query.client.service.Client;
import com.query.client.util.Response;
import com.query.client.util.Util;

@SuppressWarnings("serial")
@Service
public class LoginFrame extends JFrame implements ItemListener {

    Logger logger = LoggerFactory.getLogger(LoginFrame.class);

    @Autowired
    private Client clientService;

    @Autowired
    StudentDashboard studentDashboard;

    @Autowired
    SupervisorDashboard supervisorDashboard;

    @Autowired
    AdvisorDashboard advisorDashboard;

    @Autowired
    ViewPastIssueTab issueTab;

    @Autowired
    private Util util;

    private JTextField textField;
    private JPasswordField passwordField;
    private JButton btnNewButton;
    private JPanel contentPane;
    private JComboBox<String> categoryCombo;
    private String requestType = "STUDENT_LOGIN";

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    LoginFrame frame = new LoginFrame();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Create the frame.
     */
    public LoginFrame() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(450, 190, 1000, 600);
        setResizable(false);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JLabel lblNewLabel = new JLabel("Login", SwingConstants.CENTER);
        lblNewLabel.setForeground(Color.BLACK);
        lblNewLabel.setFont(new Font("Times New Roman", Font.PLAIN, 40));
        lblNewLabel.setBounds(450, 10, 100, 100);
        contentPane.add(lblNewLabel);

        JLabel lblUserType = new JLabel("Login As");
        lblUserType.setBackground(Color.BLACK);
        lblUserType.setForeground(Color.BLACK);
        lblUserType.setFont(new Font("Tahoma", Font.PLAIN, 25));
        lblUserType.setBounds(250, 110, 250, 50);
        contentPane.add(lblUserType);

        String options[] = { "Student", "Student Services Supervisor", "Student Services Advisor" };
        // create checkbox
        categoryCombo = new JComboBox<String>(options);
        categoryCombo.setBounds(500, 110, 250, 50);
        categoryCombo.addItemListener(this);
        contentPane.add(categoryCombo);

        textField = new JTextField();
        textField.setFont(new Font("Tahoma", Font.PLAIN, 20));
        textField.setBounds(500, 200, 250, 50);
        contentPane.add(textField);
        textField.setColumns(10);

        passwordField = new JPasswordField();
        passwordField.setFont(new Font("Tahoma", Font.PLAIN, 25));
        passwordField.setBounds(500, 290, 250, 50);
        contentPane.add(passwordField);

        JLabel lblUsername = new JLabel("Student / Staff ID");
        lblUsername.setBackground(Color.BLACK);
        lblUsername.setForeground(Color.BLACK);
        lblUsername.setFont(new Font("Tahoma", Font.PLAIN, 25));
        lblUsername.setBounds(250, 200, 250, 50);
        contentPane.add(lblUsername);

        JLabel lblPassword = new JLabel("Password");
        lblPassword.setForeground(Color.BLACK);
        lblPassword.setBackground(Color.CYAN);
        lblPassword.setFont(new Font("Tahoma", Font.PLAIN, 25));
        lblPassword.setBounds(250, 290, 250, 50);
        contentPane.add(lblPassword);

        btnNewButton = new JButton("Login");
        btnNewButton.setFont(new Font("Tahoma", Font.PLAIN, 26));
        btnNewButton.setBounds(425, 392, 150, 73);
        btnNewButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                verifyPassword();

            }
        });

        contentPane.add(btnNewButton);

    }

    @SuppressWarnings("deprecation")
    public void verifyPassword() {
        clientService.createConnection();
        JSONObject login = new JSONObject();
        login.put("username", textField.getText());
        login.put("password", passwordField.getText());
        clientService.sendRequest(util.parseRequestString(login.toString(), requestType));
        Response response = clientService.getResponse();

        if (response.getResponseType().equals("ERROR")) {
            JOptionPane.showMessageDialog(btnNewButton, response.getMessage(), "Alert", JOptionPane.ERROR_MESSAGE);
        } else {
            dispose();
            if (requestType.equals("SUPERVISOR_LOGIN")) {
                supervisorDashboard.addTabs();
                supervisorDashboard.setTitle("Student Services Supervisor Dashboard");
                supervisorDashboard.setVisible(true);
            } else if (requestType.equals("STUDENT_LOGIN")) {
                studentDashboard.addIssuesTab();
                studentDashboard.setTitle("Student Dashboard");
                studentDashboard.setVisible(true);
            } else if (requestType.equals("ADVISOR_LOGIN")) {
                advisorDashboard.addTabs();
                advisorDashboard.setTitle("Student Services Advisor Dashboard");
                advisorDashboard.setVisible(true);
            }
            JOptionPane.showMessageDialog(btnNewButton, "You have successfully logged in");
        }
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        int i = categoryCombo.getSelectedIndex();
        if (i == 0) {
            requestType = "STUDENT_LOGIN";
        } else if (i == 1) {
            requestType = "SUPERVISOR_LOGIN";
        } else {
            requestType = "ADVISOR_LOGIN";
        }
    }
}
