package com.query.server.service;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.query.server.entity.AddAdvisor;
import com.query.server.entity.Issue;
import com.query.server.entity.Login;
import com.query.server.util.Request;
import com.query.server.util.Response;

@Service
public class QueryServer {
    Logger logger = LoggerFactory.getLogger(QueryServer.class);

    @Value("${socket.port}")
    int port;

    private Socket s = null;
    private ServerSocket ss2 = null;
    private ObjectMapper mapper = new ObjectMapper();

    @Autowired
    StudentService studentService;

    @Autowired
    SupervisorService supervisorService;

    @Autowired
    StudentAdvisorService advisorService;

    @Autowired
    IssueService issueService;

    @Autowired
    IssueCategoryService issueCategoryService;

    public void createServer() {
        try {
            // create a socket at user defined port
            ss2 = new ServerSocket(port);
            logger.info("Server is Listening ... ");

        } catch (IOException e) {
            e.printStackTrace();
            logger.error("Server error");

        }

        while (true) {
            try {
                // accept the connection from client
                s = ss2.accept();
                logger.info("Connection Established with client");

                // for each client connection start a new thread
                ServerThread st = new ServerThread(s);
                st.start();

            }

            catch (Exception e) {
                logger.info("Connection Error : " + e.getMessage());
                System.exit(0);

            }
        }
    }

    class ServerThread extends Thread {
        String request = null;
        ObjectInputStream is = null;
        ObjectOutputStream os = null;
        Socket s = null;
        String clientName = null;

        public ServerThread(Socket s) {
            this.s = s;
        }

        @Override
        public synchronized void run() {
            try {

                // create a reader for client socket
                is = new ObjectInputStream(s.getInputStream());

                // create a writer for server to send response
                os = new ObjectOutputStream(s.getOutputStream());

            } catch (IOException e) {
                logger.info("IO error in server thread");
            }

            try {
                while (true) {

                    Response result = null;
                    request = (String) is.readObject();
                    var requestObject = mapper.readValue(request, Request.class);
                    logger.info("Request Recieved : " + requestObject);
                    switch (requestObject.getRequestType()) {
                    case "STUDENT_LOGIN":
                        var readValue = mapper.readValue(requestObject.getData(), Login.class);
                        clientName = readValue.getUsername();
                        result = studentService.verifyPassword(readValue);
                        break;
                    case "SUPERVISOR_LOGIN":
                        var readValue3 = mapper.readValue(requestObject.getData(), Login.class);
                        clientName = readValue3.getUsername();
                        result = supervisorService.verifyPassword(readValue3);
                        break;
                    case "ADVISOR_LOGIN":
                        var readValue4 = mapper.readValue(requestObject.getData(), Login.class);
                        clientName = readValue4.getUsername();
                        result = advisorService.verifyPassword(readValue4);
                        break;
                    case "GET_ALL_COMPLAINT_CATEGORY":
                        result = issueCategoryService.getCategoryByType("Complaint");
                        break;
                    case "GET_ALL_QUERY_CATEGORY":
                        result = issueCategoryService.getCategoryByType("Query");
                        break;
                    case "ADD_COMPLAINT_CATEGORY":
                        result = issueCategoryService.addIssue("Complaint", requestObject.getData());
                        break;
                    case "ADD_QUERY_CATEGORY":
                        result = issueCategoryService.addIssue("Query", requestObject.getData());
                        break;
                    case "ADD_ISSUE":
                        Issue readValue2 = mapper.readValue(requestObject.getData(), Issue.class);
                        result = issueService.addIssue(readValue2, clientName);
                        break;
                    case "GET_ISSUES":
                        result = issueService.getAllIssuesByStudentId(clientName);
                        break;
                    case "GET_ALL_ISSUES":
                        result = issueService.getAllIssues();
                        break;
                    case "GET_ADVISOR_ISSUES":
                        result = issueService.getAllAdvisorIssues(clientName);
                        break;
                    case "GET_ALL_ADVISORS":
                        result = advisorService.getAllAdvisorName();
                        break;
                    case "ASSIGN_ADVISOR":
                        AddAdvisor query = mapper.readValue(requestObject.getData(), AddAdvisor.class);
                        result = issueService.addAdvisor(query);
                        break;
                    case "GET_ISSUE_STATUS":
                        result = issueService.getIssueStatus();
                        break;
                    case "SAVE_COMMENTS":
                        Issue issue = mapper.readValue(requestObject.getData(), Issue.class);
                        result = issueService.getSaveComment(issue);
                        break;

                    }
                    logger.info("Response Sent : " + result);
                    os.writeObject(mapper.writeValueAsString(result));
                }
            } catch (IOException e) {
                logger.info("IO Error/ Client terminated abruptly : " + e.getMessage());
            } catch (NullPointerException e) {
                logger.info("Client Closed");
            } catch (Exception e) {
                System.out.println(e);
            } finally {
                try {
                    // close the connection after quit request is recieved or there is a error
                    logger.info("Closing connection with .. " + clientName);
                    if (is != null) {
                        is.close();
                    }

                    if (os != null) {
                        os.close();
                    }
                    if (s != null) {
                        s.close();
                    }

                } catch (IOException ie) {
                }
            } // end finally

        }
    }

}
