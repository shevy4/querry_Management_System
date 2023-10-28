package com.query.client.gui.components;

import java.awt.Color;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;

import org.springframework.stereotype.Service;

import com.query.client.util.Student;

@SuppressWarnings("serial")
@Service
public class StudentDetailFrame extends JFrame {

    public StudentDetailFrame() {
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setBounds(650, 300, 600, 300);
        setResizable(false);
    }

    public void addData(Student student) {
        JLabel newLabel = new JLabel();
        JTextField textField = new JTextField();
        Font font = new Font("Times New Roman", Font.PLAIN, 15);

        // left side
        var studentDetailPane = new JPanel();
        studentDetailPane.setLayout(null);
        studentDetailPane.setBorder(new CompoundBorder(BorderFactory.createTitledBorder("Student Details"),
                new EmptyBorder(20, 10, 20, 10)));
        studentDetailPane.setFont(font);
        newLabel = new JLabel("Student Id");
        newLabel.setFont(font);
        newLabel.setBounds(30, 20, 100, 30);
        studentDetailPane.add(newLabel);

        textField = new JTextField(student.getStudentId());
        textField.setFont(font);
        textField.setEditable(false);
        textField.setBackground(Color.WHITE);
        textField.setBounds(160, 20, 250, 30);
        studentDetailPane.add(textField);

        newLabel = new JLabel("Full Name");
        newLabel.setFont(font);
        newLabel.setBounds(30, 80, 100, 30);
        studentDetailPane.add(newLabel);

        textField = new JTextField(student.getName());
        textField.setFont(font);
        textField.setEditable(false);
        textField.setBackground(Color.WHITE);
        textField.setBounds(160, 80, 250, 30);
        studentDetailPane.add(textField);

        newLabel = new JLabel("Email Address");
        newLabel.setFont(font);
        newLabel.setBounds(30, 140, 100, 30);
        studentDetailPane.add(newLabel);

        textField = new JTextField(student.getEmailAddress());
        textField.setFont(font);
        textField.setEditable(false);
        textField.setBackground(Color.WHITE);
        textField.setBounds(160, 140, 250, 30);
        studentDetailPane.add(textField);

        newLabel = new JLabel("Contact");
        newLabel.setFont(font);
        newLabel.setBounds(30, 200, 100, 30);
        studentDetailPane.add(newLabel);

        textField = new JTextField(student.getContact());
        textField.setFont(font);
        textField.setEditable(false);
        textField.setBounds(160, 200, 250, 30);
        textField.setBackground(Color.WHITE);

        studentDetailPane.add(textField);
        add(studentDetailPane);

    }

}
