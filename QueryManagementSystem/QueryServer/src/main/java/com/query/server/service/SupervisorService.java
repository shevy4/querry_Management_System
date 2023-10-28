package com.query.server.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.query.server.dao.SupervisorRepository;
import com.query.server.entity.Login;
import com.query.server.entity.StudentSupervisor;
import com.query.server.util.Response;

@Service
public class SupervisorService {
    @Autowired
    SupervisorRepository supervisorDao;

    public Response verifyPassword(Login login) {
        Optional<StudentSupervisor> studentEntity = supervisorDao.findById(login.getUsername());
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
}
