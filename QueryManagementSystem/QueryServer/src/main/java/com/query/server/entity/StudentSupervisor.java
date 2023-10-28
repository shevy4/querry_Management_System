package com.query.server.entity;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Data;

@Data
@Entity
public class StudentSupervisor {

    @Id
    private String SupervisorId;

    private String password;

}
