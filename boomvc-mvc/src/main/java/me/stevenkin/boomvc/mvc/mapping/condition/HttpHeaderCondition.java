package me.stevenkin.boomvc.mvc.mapping.condition;

import me.stevenkin.boomvc.http.HttpHeader;
import me.stevenkin.boomvc.http.HttpRequest;

import java.util.Map;
import java.util.regex.Pattern;

public class HttpHeaderCondition implements Condition {

    private String headerNamePattern;

    private String headerValuePattern;

    public HttpHeaderCondition(String headerNamePattern, String headerValuePattern) {
        this.headerNamePattern = headerNamePattern;
        this.headerValuePattern = headerValuePattern;
    }

    @Override
    public boolean determine(HttpRequest request) {
        Pattern namePattern = Pattern.compile(this.headerNamePattern);
        Pattern valuePattern = Pattern.compile(this.headerValuePattern);
        for(HttpHeader header : request.headers()){
            if(namePattern.matcher(header.name()).matches() && valuePattern.matcher(header.value()).matches())
                return true;
        }
        return false;
    }
}
