package com.query.server.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.query.server.dao.StudentAdvisorRepository;
import com.query.server.entity.Login;
import com.query.server.entity.StudentAdvisor;
import com.query.server.util.Response;

@Service
public class StudentAdvisorService {
    Logger logger = LoggerFactory.getLogger(StudentAdvisorService.class);

    @Autowired
    StudentAdvisorRepository advisorDao;

    @Autowired
    ObjectMapper mapper;

    public Response verifyPassword(Login login) {
        Optional<StudentAdvisor> studentEntity = advisorDao.findById(login.getUsername());
        Response response = new Response();
        if (studentEntity.isPresent() && studentEntity.get().getPassword().equals(login.getPassword())) {
            response.setResponseType("SUCCESS");
            response.setData("Login Successful");
            response.setMessage("Login Successful");
        } else {
            response.setResponseType("ERROR");
            response.setMessage("Invalid Credentials");
        }
        return response;
    }

    public Response getAllAdvisorName() {
        List<StudentAdvisor> advisors = advisorDao.findAll();
        Response response = new Response();
        if (!advisors.isEmpty()) {
            response.setResponseType("SUCCESS");
            List<String> list = advisors.stream().map(e -> e.getAdvisorName()).collect(Collectors.toList());
            try {
                response.setData(mapper.writeValueAsString(list));
            } catch (JsonProcessingException e1) {
                logger.error(e1.getMessage());
            }
            response.setMessage(advisors.size() + " Advisors Found");
        } else {
            response.setResponseType("ERROR");
            response.setMessage("No Advisor Found");
        }
        return response;
    }

}
