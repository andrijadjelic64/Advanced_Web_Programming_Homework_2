package framework.request;

import framework.request.enums.Method;

import java.util.HashMap;
import java.util.Map;

public class Request {

    private Method method;
    private String location;
    private Header header;
    private HashMap<String, String> parameters;

    public Request() {
        this(Method.GET, "/");
    }

    public Request(Method method, String location) {
        this(method, location, new Header(), new HashMap<String, String>());
    }

    public Request(Method method, String location, Header header, HashMap<String, String> parameters) {
        this.method = method;
        this.location = location;
        this.header = header;
        this.parameters = parameters;
    }

    public void addParameter(String name, String value) {
        this.parameters.put(name, value);
    }

    public String getParameter(String name) {
        return this.parameters.get(name);
    }

    public String getParameters() {
        StringBuilder stringBuilder = new StringBuilder();
        for (Map.Entry<String, String> entry : parameters.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            stringBuilder.append("["+ key + ": " + value + "]");
        }
        return stringBuilder.toString();
    }

    public boolean isMethod(Method method) {
        return this.getMethod().equals(method);
    }

    public Method getMethod() {
        return method;
    }

    public String getLocation() {
        return location;
    }

    public Header getHeader() {
        return header;
    }
}
