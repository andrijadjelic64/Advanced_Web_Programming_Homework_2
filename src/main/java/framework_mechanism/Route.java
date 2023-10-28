package framework_mechanism;

import java.util.Objects;

public class Route {

    String method;
    String route;
    String param;

    public Route(String method, String route) {
        this.method = method;
        this.route = route;
    }

    public Route(String method, String route, String param) {
        this.method = method;
        this.route = route;
        this.param = param;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Route route1 = (Route) o;

        if (!method.equals(route1.method)) return false;
        if (!route.equals(route1.route)) return false;
        return Objects.equals(param, route1.param);
    }

    @Override
    public int hashCode() {
        int result = method.hashCode();
        result = 31 * result + route.hashCode();
        result = 31 * result + (param != null ? param.hashCode() : 0);
        return result;
    }
}
