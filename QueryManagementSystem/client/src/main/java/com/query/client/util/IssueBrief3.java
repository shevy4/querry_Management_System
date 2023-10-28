package com.query.client.util;

import java.sql.Date;

import lombok.Data;

@Data
public class IssueBrief3 {

    private String id;

    private String description;

    private String issueType;

    private String nature;

    private String status;

    private String advisorName;

    private String comments;

    private Date lastResponseDate;

    private String studentName;

    private String Action;

    public IssueBrief3(String id, String description, String issueType, String nature, String status,
            StudentAdvisor advisor, String comments, Date lastResponseDate, Student student, String action) {
        super();
        this.id = id;
        this.description = description;
        this.issueType = issueType;
        this.nature = nature;
        this.status = status;
        this.advisorName = advisor == null ? null : advisor.getAdvisorName();
        this.comments = comments;
        this.lastResponseDate = lastResponseDate;
        this.studentName = student == null ? null : student.getName();
        Action = action;
    }

}
