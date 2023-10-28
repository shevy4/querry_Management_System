package com.query.server;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.query.server.service.QueryServer;

@SpringBootApplication
public class QueryServerApplication implements CommandLineRunner {

    @Autowired
    QueryServer server;

    public static void main(String[] args) {
        SpringApplication.run(QueryServerApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        server.createServer();
    }

}
