package com.query.client.service;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.query.client.util.Response;

@Component
public class Client {
    Logger logger = LoggerFactory.getLogger(Client.class);

    String ip = "127.0.0.1";
    int port = 6489;
    ObjectOutputStream outToServer = null;
    ObjectInputStream inFromServer = null;
    Socket clientSocket = null;

    public void createConnection() {
        if (clientSocket == null) {
            try {

                // create a connection with the server
                clientSocket = new Socket(ip, port);

                // create a printer for sending request to server
                outToServer = new ObjectOutputStream(clientSocket.getOutputStream());

                // create a reader to read the server's response
                inFromServer = new ObjectInputStream(clientSocket.getInputStream());
                logger.info("Connected to the Server");
            } catch (Exception e) {

                logger.error("Connection with the server Failed ! " + e.getMessage());
            }
        }

    }

    public void sendRequest(JSONObject jsonObject2) {
        try {
            outToServer.writeObject(jsonObject2.toString());
            logger.info("Request Sent : " + jsonObject2.toString());
        } catch (Exception e) {
            logger.info("Client Error");
        }
    }

    public Response getResponse() {
        Response response = null;
        try {
            response = new ObjectMapper().readValue(inFromServer.readObject().toString(), Response.class);
            if (response == null) {
                response = new Response();
                response.setMessage("No Response");
                response.setResponseType("ERROR");
            }
            logger.info("Response Recieved : " + response);
        } catch (Exception e) {
            logger.error(e.getMessage());
            response = new Response();
            response.setMessage(e.getMessage());
            response.setResponseType("ERROR");

        }
        return response;
    }
}
