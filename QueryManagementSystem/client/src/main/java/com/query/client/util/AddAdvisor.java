package com.query.client.util;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AddAdvisor {
    private String advisorId;
    private int issueId;
}
