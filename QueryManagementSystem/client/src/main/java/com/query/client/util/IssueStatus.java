package com.query.client.util;

import lombok.Data;

@Data
public class IssueStatus {
    private int created = 0;
    private int resolved = 0;
    private int inProgress = 0;
    private int total = 0;

}
