package me.stevenkin.boomvc.mvc.mapping.condition;

import me.stevenkin.boomvc.http.HttpMethod;
import me.stevenkin.boomvc.http.HttpRequest;
import me.stevenkin.boomvc.mvc.mapping.condition.Condition;

public class HttpMethodCondition implements Condition {

    private HttpMethod httpMethod;

    public HttpMethodCondition(HttpMethod httpMethod) {
        this.httpMethod = httpMethod;
    }

    @Override
    public boolean determine(HttpRequest request) {
        return this.httpMethod == request.httpMethod();
    }
}
