package com.query.server.entity;

import lombok.Data;

@Data
public class IssueStatus {
    private int created;
    private int resolved;
    private int inProgress;
    private int total;

}
