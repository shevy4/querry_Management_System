package com.query.client.util;

import java.sql.Date;

import lombok.Data;

@Data
public class Issue {

    private String id;

    private String description;

    private String issueType;

    private String nature;

    private String status;

    private StudentAdvisor advisor;

    private String comments;

    private Date lastResponseDate;

    private Student student;
}
