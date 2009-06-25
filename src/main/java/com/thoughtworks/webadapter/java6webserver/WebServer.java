package com.thoughtworks.webadapter.java6webserver;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import com.thoughtworks.webinterface.Header;
import com.thoughtworks.webinterface.Request;
import com.thoughtworks.webinterface.Response;
import com.thoughtworks.webinterface.WebService;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;

public class WebServer {

    WebService webService;

    public WebServer(WebService webService) {
        this.webService = webService;
    }

    public void start() {
        class MyHandler implements HttpHandler {
            public void handle(HttpExchange t) throws IOException {
                try {
                    Response response = webService.service(new HttpExchnageRequestAdapter(t));
                    new HttpExchangeResponseAdapter(response).pushInto(t);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        HttpServer server = null;
        try {
            server = HttpServer.create(new InetSocketAddress(8000), 8000);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        server.createContext("/", new MyHandler());
        server.setExecutor(null); // creates a default executor
        server.start();
    }

    private class HttpExchnageRequestAdapter implements Request {
        private HttpExchange httpExchange;

        public HttpExchnageRequestAdapter(HttpExchange httpExchange) {
            this.httpExchange = httpExchange;
        }

        public String getPath() {
            return httpExchange.getRequestURI().getPath();
        }

        public Method getMethod() {
            return Method.valueOf(httpExchange.getRequestMethod());
        }

        public Map<String, String> getRequestParameters() {
            HashMap<String, String> map = new HashMap<String, String>();
            BufferedReader inputStream = new BufferedReader(new InputStreamReader(httpExchange.getRequestBody()));
            try {
                String line;
                while ((line = inputStream.readLine()) != null) {
                    System.out.println("line = " + line);
                    String[] keyValuePairs = line.split("&");
                    for (String keyValuePair : keyValuePairs) {
                        if (keyValuePair.contains("=")) {
                            String[] tokens = keyValuePair.split("=");
                            map.put(tokens[0], tokens[1]);
                        }
                    }
                }
                ;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return map;
        }
    }

    private class HttpExchangeResponseAdapter {
        private Response response;

        public HttpExchangeResponseAdapter(Response response) {
            this.response = response;
        }

        public void pushInto(HttpExchange httpExchange) {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            response.writeBodyToStream(outputStream);
            byte[] responseBody = outputStream.toByteArray();

            try {
                for (Header header : response.getHeaders()) {
                    httpExchange.getResponseHeaders().add(header.getName(), header.getValue());
                }
                httpExchange.sendResponseHeaders(response.getStatus().getCode(), responseBody.length);
                OutputStream responseBodyOutputStream = httpExchange.getResponseBody();
                responseBodyOutputStream.write(responseBody);
                responseBodyOutputStream.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
