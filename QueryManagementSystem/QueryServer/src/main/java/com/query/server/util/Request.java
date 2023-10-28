package com.query.server.util;

import java.io.Serializable;

import lombok.Data;

@SuppressWarnings("serial")
@Data
public class Request implements Serializable {

    private String requestType;
    private String data;
}