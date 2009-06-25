package com.thoughtworks.webinterface.responses;

import com.thoughtworks.webinterface.Response;
import com.thoughtworks.webinterface.Header;

import java.util.List;
import java.util.ArrayList;
import java.io.OutputStream;

public class NotFoundResponse implements Response {
    public Status getStatus() {
        return Status.NOT_FOUND;
    }

    public List<Header> getHeaders() {
        return new ArrayList<Header>();
    }

    public void writeBodyToStream(OutputStream outputStream) {
    }
}
