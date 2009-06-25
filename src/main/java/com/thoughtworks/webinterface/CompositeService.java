package com.thoughtworks.webinterface;

public class CompositeService implements WebService {
    private String descriminator;
    private WebService webService1;
    private WebService webService2;

    public CompositeService(String descriminator, WebService webService1, WebService webService2) {
        this.descriminator = descriminator;
        this.webService1 = webService1;
        this.webService2 = webService2;
    }

    public Response service(Request request) {
        return request.getPath().startsWith(descriminator) ? webService1.service(request) : webService2.service(request);
    }
}
