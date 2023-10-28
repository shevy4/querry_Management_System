package com.query.server.entity;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Data;

@Data
@Entity
public class Student {

    @Id
    private String studentId;

    private String password;

    private String name;

    private String emailAddress;

    private String contact;

}
