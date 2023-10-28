package com.query.client.gui.components;

import java.awt.EventQueue;
import java.awt.event.KeyEvent;
import java.util.List;
import java.util.stream.Collectors;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JTabbedPane;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.query.client.Application;
import com.query.client.service.Client;
import com.query.client.util.Issue;
import com.query.client.util.IssueBrief2;
import com.query.client.util.Response;
import com.query.client.util.Util;

@SuppressWarnings("serial")
@Service
public class SupervisorDashboard extends JFrame {
    Logger logger = LoggerFactory.getLogger(SupervisorDashboard.class);

    @Autowired
    ApplicationContext ctx;

    @Autowired
    Client clientService;

    Util util = new Util();

    private JTabbedPane tabs = new JTabbedPane(JTabbedPane.TOP, JTabbedPane.WRAP_TAB_LAYOUT);

    public SupervisorDashboard() {

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(450, 190, 1000, 600);
        setResizable(false);
        setJMenuBar(createMenuBar());
    }

    public void addTabs() {
        AllIssuesTab tab1 = ctx.getBean(AllIssuesTab.class);
        AllServicesTab tab2 = ctx.getBean(AllServicesTab.class);
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
            tab1.setIssues(issueBrief);
            tab1.setAllIssues(issues);
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        tabs.addTab("All Services", tab2.getPanel());
        tabs.addTab("All Complaints / Query", tab1.getPanel());
        getContentPane().add(tabs);
    }

    public JMenuBar createMenuBar() {

        var menuBar = new JMenuBar();

        var fileMenu = new JMenu("File");
        fileMenu.setMnemonic(KeyEvent.VK_F);

        var eMenuItem = new JMenuItem("Exit (E)");
        eMenuItem.setMnemonic(KeyEvent.VK_E);
        eMenuItem.setToolTipText("Exit application");
        eMenuItem.addActionListener((event) -> System.exit(0));

        var eMenuItem2 = new JMenuItem("Logout");
        eMenuItem.setMnemonic(KeyEvent.VK_L);
        eMenuItem2.setToolTipText("Logout");
        eMenuItem2.addActionListener((event) -> openHome());

        fileMenu.add(eMenuItem2);
        fileMenu.add(eMenuItem);
        menuBar.add(fileMenu);
        return menuBar;
    }

    private void openHome() {
        dispose();
        EventQueue.invokeLater(() -> {
            var ex = ctx.getBean(Application.class);
            ex.setVisible(true);
        });
    }

}
