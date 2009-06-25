package com.thoughtworks.webrest.webadapter.servlet;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import com.thoughtworks.webinterface.Request;
import com.thoughtworks.webinterface.Response;
import com.thoughtworks.webinterface.Header;
import com.thoughtworks.webinterface.WebServiceFactory;
import com.thoughtworks.webinterface.WebService;

public class ContainerManagerServletAdapter extends HttpServlet {
    private WebService webService;

    public void init(ServletConfig servletConfig) throws ServletException {
        super.init(servletConfig);
        String appClassName = servletConfig.getInitParameter("WebServiceFactory");
        try {
            WebServiceFactory factory = (WebServiceFactory) Class.forName(appClassName).newInstance();
            webService = factory.create();
        } catch (InstantiationException e) {
            throw new ServletException(e);
        } catch (IllegalAccessException e) {
            throw new ServletException(e);
        } catch (ClassNotFoundException e) {
            throw new ServletException(e);
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse httpServletResponse) throws ServletException, IOException {

        Response response = webService.service(new HttpServletRequestAdapter(request));
        new HttpServletResponseAdapter(response).pushInto(httpServletResponse);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse httpServletResponse) throws ServletException, IOException {

        Response response = webService.service(new HttpServletRequestAdapter(request));
        new HttpServletResponseAdapter(response).pushInto(httpServletResponse);
    }

    public static class HttpServletResponseAdapter {
        private Response response;

        public HttpServletResponseAdapter(Response response) {
            this.response = response;
        }

        public void pushInto(HttpServletResponse httpServletResponse) throws IOException {
            httpServletResponse.setStatus(response.getStatus().getCode());
            for (Header header : response.getHeaders()) {
                httpServletResponse.setHeader(header.getName(), header.getValue());
            }
            response.writeBodyToStream(httpServletResponse.getOutputStream());
        }
    }
    public static class HttpServletRequestAdapter implements Request {
        private HttpServletRequest request;
        private Map<String, String> requestParameters;

        public HttpServletRequestAdapter(HttpServletRequest request) {
            this.request = request;
            requestParameters = new HashMap<String, String>();
            Enumeration parameterNames = request.getParameterNames();

            while (parameterNames.hasMoreElements()) {
                String parameterName = (String) parameterNames.nextElement();
                requestParameters.put(parameterName, request.getParameter(parameterName));
            }
        }

        public Method getMethod() {
            return Method.valueOf(request.getMethod());
        }

        public Map<String, String> getRequestParameters() {
            return requestParameters;
        }

        public String getPath() {
            return request.getServletPath() + request.getPathInfo();
        }

    }

}
