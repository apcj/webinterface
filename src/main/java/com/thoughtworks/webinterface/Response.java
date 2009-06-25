package com.thoughtworks.webinterface;

import java.io.OutputStream;
import java.util.List;

public interface Response {
    Status getStatus();

    List<Header> getHeaders();

    void writeBodyToStream(OutputStream outputStream);

    public enum Status {
        OK(200), MOVED_TEMPORARILY(302), NOT_FOUND(404), SERVER_ERROR(500);
        private int code;

        Status(int code) {
            this.code = code;
        }

        public int getCode() {
            return code;
        }
    }
}
