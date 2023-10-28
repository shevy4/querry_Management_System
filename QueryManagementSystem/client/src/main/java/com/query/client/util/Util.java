package com.query.client.util;

import org.json.JSONObject;
import org.springframework.stereotype.Service;

@Service
public class Util {

    public JSONObject parseRequestString(String data, String requestType) {
        JSONObject request = new JSONObject();
        request.put("requestType", requestType);
        request.put("data", data);
        return request;
    }
}
