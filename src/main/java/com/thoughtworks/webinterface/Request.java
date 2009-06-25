package com.thoughtworks.webinterface;

import java.util.Map;

public interface Request {
    String getPath();

    Method getMethod();

    Map<String, String> getRequestParameters();

    public enum Method {
        GET, POST
    }
}
