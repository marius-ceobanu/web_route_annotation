package com.codecool.web_router;

import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.OutputStream;

public class Routes {
    @WebRoute(path = "/test")
    public void test1Response(HttpExchange t) throws IOException {
        String response = "This is response for /test path route!";
        t.sendResponseHeaders(200, response.length());
        OutputStream os = t.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }

    @WebRoute(path = "/another_test")
    public void test2Response(HttpExchange t) throws IOException {
        String response =  "This is a /another_test path route!";
        t.sendResponseHeaders(200, response.length());
        OutputStream os = t.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }

    @WebRoute(path = "/test/")
    public void testNameResponse(HttpExchange t, String name) throws IOException {
        String response =  "Hello " + name + ". This is a /test!";
        t.sendResponseHeaders(200, response.length());
        OutputStream os = t.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }
}
