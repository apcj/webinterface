package com.thoughtworks.webinterface.responses;

import com.thoughtworks.webinterface.Response;
import com.thoughtworks.webinterface.Header;

import java.io.OutputStream;
import java.util.List;
import java.util.ArrayList;

public class RedirectResponse implements Response {
    private String uri;

    public RedirectResponse(String uri) {
        this.uri = uri;
    }

    public Status getStatus() {
        return Status.MOVED_TEMPORARILY;
    }

    public List<Header> getHeaders() {
        ArrayList<Header> list = new ArrayList<Header>();
        list.add(new Header("Location", uri));
        return list;
    }

    public void writeBodyToStream(OutputStream outputStream) {
    }
}
