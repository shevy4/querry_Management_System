package com.query.server.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Data;

@Data
@Entity
public class IssueCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int Id;
    private String type;
    private String detail;

}
