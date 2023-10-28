package com.query.server.entity;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Data;

@Data
@Entity
public class StudentAdvisor {

    @Id
    private String advisorId;
    private String advisorName;
    private String password;
}
