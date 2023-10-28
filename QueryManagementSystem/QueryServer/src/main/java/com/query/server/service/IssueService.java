package com.query.server.service;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.query.server.dao.IssueRepository;
import com.query.server.dao.StudentAdvisorRepository;
import com.query.server.dao.StudentRepository;
import com.query.server.entity.AddAdvisor;
import com.query.server.entity.Issue;
import com.query.server.entity.IssueStatus;
import com.query.server.entity.Student;
import com.query.server.entity.StudentAdvisor;
import com.query.server.util.Response;

@Service
public class IssueService {

    Logger logger = LoggerFactory.getLogger(IssueService.class);

    @Autowired
    IssueRepository issueRepository;

    @Autowired
    StudentAdvisorRepository advisorRepository;

    @Autowired
    StudentRepository studentRepository;

    @Autowired
    ObjectMapper mapper;

    public Response addIssue(Issue issue, String clientId) {
        Optional<Student> studentEntity = studentRepository.findById(clientId);
        Response response = new Response();
        if (studentEntity.isPresent()) {
            issue.setStudent(studentEntity.get());
            issueRepository.save(issue);
            response.setResponseType("SUCCESS");
            response.setMessage("Issue Saved Successfully");
        } else {
            response.setResponseType("ERROR");
            response.setMessage("Invalid Student Id");
        }
        return response;
    }

    public Response getAllIssues() {
        List<Issue> issues = issueRepository.findAll();
        Response response = new Response();
        response.setResponseType("SUCCESS");
        try {
            response.setData(mapper.writeValueAsString(issues));
        } catch (JsonProcessingException e) {
            // TODO Auto-generated catch block
            logger.error(e.getMessage());
        }

        response.setMessage(issues.size() + " issues found.");
        return response;
    }

    public Response getAllIssuesByStudentId(String clientId) {
        Issue[] issues = issueRepository.findAllByStudentStudentId(clientId);
        Response response = new Response();
        response.setResponseType("SUCCESS");
        try {
            response.setData(mapper.writeValueAsString(issues));
        } catch (JsonProcessingException e) {
            logger.error(e.getMessage());
        }

        response.setMessage(issues.length + " issues found.");
        return response;
    }

    public Response addAdvisor(AddAdvisor query) {
        Optional<Issue> issue = issueRepository.findById(query.getIssueId());
        StudentAdvisor advisor = advisorRepository.findByAdvisorName(query.getAdvisorId());
        Response response = new Response();
        if (issue.isPresent() && advisor != null) {
            Issue issue2 = issue.get();
            issue2.setAdvisor(advisor);
            issue2.setStatus("IN PROGRESS");
            issueRepository.save(issue2);
            response.setResponseType("SUCCESS");
            response.setMessage("Advisor Assigned Successfully");
        } else {
            response.setResponseType("ERROR");
            response.setMessage("Something Went Wrong !");
        }
        return response;
    }

    public Response getIssueStatus() {
        List<Issue> issues = issueRepository.findAll();
        Response response = new Response();
        var statusDetails = new IssueStatus();
        statusDetails.setTotal(issues.size());
        statusDetails.setResolved((int) issues.stream().filter(e -> e.getStatus().equals("RESOLVED")).count());
        statusDetails.setCreated((int) issues.stream().filter(e -> e.getStatus().equals("CREATED")).count());
        statusDetails.setInProgress((int) issues.stream().filter(e -> e.getStatus().equals("IN PROGRESS")).count());

        response.setMessage("Success");
        response.setResponseType("SUCCESS");
        try {
            response.setData(mapper.writeValueAsString(statusDetails));
        } catch (JsonProcessingException e1) {
            logger.error(e1.getMessage());
        }
        return response;

    }

    public Response getAllAdvisorIssues(String clientId) {
        Issue[] issues = issueRepository.findAllByAdvisorAdvisorId(clientId);
        Response response = new Response();
        response.setResponseType("SUCCESS");
        try {
            response.setData(mapper.writeValueAsString(issues));
        } catch (JsonProcessingException e) {
            logger.error(e.getMessage());
        }

        response.setMessage(issues.length + " issues found.");
        return response;
    }

    public Response getSaveComment(Issue issue) {
        Response response = new Response();
        issue.setLastResponseDate(Date.valueOf(LocalDate.now()));
        issueRepository.save(issue);
        response.setResponseType("SUCCESS");
        response.setMessage("Comment Saved Successfully");
        return response;
    }

}
