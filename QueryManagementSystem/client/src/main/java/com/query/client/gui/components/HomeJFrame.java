package com.query.client.gui.components;

import java.awt.event.KeyEvent;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class HomeJFrame {

    private JFrame jFrame;

    @Autowired
    LoginFrame login;

    public JFrame initHome() {
        jFrame = new JFrame();
        createMenuBar();
        jFrame.setTitle("Query Management System");
        jFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        jFrame.setBounds(450, 190, 1000, 600);
        jFrame.setResizable(false);
        var contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        jFrame.setContentPane(contentPane);
        contentPane.setLayout(null);
        var jLabel1 = new javax.swing.JLabel();
        jLabel1.setBackground(new java.awt.Color(255, 255, 255));
        jLabel1.setFont(new java.awt.Font("Cambria", 0, 18)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Welcome to the Query Management System");
        jLabel1.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(contentPane);
        contentPane.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel1Layout
                        .createSequentialGroup().addContainerGap(300, Short.MAX_VALUE).addComponent(jLabel1,
                                javax.swing.GroupLayout.PREFERRED_SIZE, 400, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(300, Short.MAX_VALUE)));
        jPanel1Layout.setVerticalGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel1Layout
                        .createSequentialGroup().addContainerGap(250, Short.MAX_VALUE).addComponent(jLabel1,
                                javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(250, Short.MAX_VALUE)));
        return jFrame;
    }

    private void openLogin() {
        jFrame.dispose();
        login.setVisible(true);
    }

    private void createMenuBar() {

        var menuBar = new JMenuBar();

        var fileMenu = new JMenu("File");
        fileMenu.setMnemonic(KeyEvent.VK_F);

        var eMenuItem = new JMenuItem("Exit");
        eMenuItem.setMnemonic(KeyEvent.VK_E);
        eMenuItem.setToolTipText("Exit application");
        eMenuItem.addActionListener((event) -> System.exit(0));

        var eMenuItem2 = new JMenuItem("Login");
        eMenuItem2.setToolTipText("Login");
        eMenuItem2.addActionListener((event) -> openLogin());

        fileMenu.add(eMenuItem2);
        fileMenu.add(eMenuItem);
        menuBar.add(fileMenu);
        jFrame.setJMenuBar(menuBar);
    }

}
