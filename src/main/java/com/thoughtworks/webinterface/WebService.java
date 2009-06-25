package com.thoughtworks.webinterface;

import com.thoughtworks.webinterface.Response;
import com.thoughtworks.webinterface.Request;

public interface WebService {
    Response service(Request request);
}
