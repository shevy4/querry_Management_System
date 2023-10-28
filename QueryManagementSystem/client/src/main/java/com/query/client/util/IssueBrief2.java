package com.query.client.util;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class IssueBrief2 {
    private String id;
    private String nature;
    private String category;
    private String description;
    private String advisorName;
    private String action;
    private String addAdvisor;
    private String studentName;
}
