package me.stevenkin.boomvc.mvc.mapping.condition;

import me.stevenkin.boomvc.http.HttpRequest;

public interface Condition {

    boolean determine(HttpRequest request);

}
