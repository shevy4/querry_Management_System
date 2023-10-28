package com.query.server.util;

import lombok.Data;

@Data
public class Response {

    private String responseType;
    private String data;
    private String message;
}
