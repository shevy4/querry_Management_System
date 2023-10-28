package com.query.server.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.query.server.dao.IssueCategoryRepository;
import com.query.server.entity.IssueCategory;
import com.query.server.util.Response;

@Service
public class IssueCategoryService {
    @Autowired
    IssueCategoryRepository issueCategoryRepository;

    @Autowired
    ObjectMapper mapper;

    public Response getCategoryByType(String type) {
        List<IssueCategory> categoryList = issueCategoryRepository.findByType(type);
        Response response = new Response();
        if (!categoryList.isEmpty()) {
            List<String> list = categoryList.stream().map(e -> e.getDetail()).collect(Collectors.toList());
            response.setResponseType("SUCCESS");
            response.setMessage(list.size() + " " + type + "(s) found.");
            try {
                response.setData(mapper.writeValueAsString(list));
            } catch (JsonProcessingException e1) {

            }
        } else {
            response.setResponseType("ERROR");
            response.setMessage("Something Went wrong");
        }
        return response;
    }

    public Response addIssue(String type, String detail) {
        IssueCategory issue = new IssueCategory();
        issue.setType(type);
        issue.setDetail(detail);
        issueCategoryRepository.save(issue);
        Response response = new Response();
        response.setResponseType("SUCCESS");
        response.setMessage(type + " added Successfully");
        return response;
    }

}
