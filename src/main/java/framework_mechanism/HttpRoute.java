package framework_mechanism;

import java.lang.reflect.Method;

public class HttpRoute {
    String route;
    String method;
    Class controller;
    Method methodObject;




    public HttpRoute(String route, String method, Class controller,Method methodObject) {
        this.route = route;
        this.method = method;
        this.controller = controller;
        this.methodObject = methodObject;
    }

    @Override
    public String toString() {
        return "HttpRoute{" +
                "route='" + route + '\'' +
                ", method='" + method + '\'' +
                ", controller='" + controller.getName() + '\'' +
                ", methodName='" + methodObject.getName() + '\'' +
                '}';
    }
}
