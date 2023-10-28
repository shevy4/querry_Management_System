package com.query.client.gui.components.helper;

import java.util.List;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;

import org.springframework.stereotype.Service;

@Service
public class CategoryJtree {
    public JTree createJTree(List<String> complaintList, List<String> queryList) {
        DefaultMutableTreeNode issues = new DefaultMutableTreeNode("Issues");
        DefaultMutableTreeNode queries = new DefaultMutableTreeNode("Queries");

        DefaultMutableTreeNode complaints = new DefaultMutableTreeNode("Complaints");

        for (String issue : complaintList) {
            DefaultMutableTreeNode complaint = new DefaultMutableTreeNode(issue);
            complaints.add(complaint);
        }

        for (String issue : queryList) {
            DefaultMutableTreeNode query = new DefaultMutableTreeNode(issue);
            queries.add(query);
        }

        issues.add(complaints);
        issues.add(queries);

        return new JTree(issues);

    }
}
