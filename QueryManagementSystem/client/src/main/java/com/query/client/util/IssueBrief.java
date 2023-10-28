package com.query.client.util;

import java.sql.Date;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class IssueBrief {
    private String id;
    private String description;
    private String advisorName;
    private Date lastResponseDate;
    private String Action;
}
