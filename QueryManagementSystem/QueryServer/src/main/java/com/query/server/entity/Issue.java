package com.query.server.entity;

import java.sql.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
@Entity
public class Issue {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int Id;

    private String description;

    private String issueType;

    private String nature;

    private String status = "CREATED";

    @ManyToOne
    @JoinColumn(name = "advisor_id")
    private StudentAdvisor advisor;

    private String comments;

    @DateTimeFormat(pattern = "Month d, yyyy")
    private Date lastResponseDate;

    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student student;

}
