package me.stevenkin.boomvc.server.session;

import me.stevenkin.boomvc.http.HttpRequest;
import me.stevenkin.boomvc.http.session.HttpSession;

import java.time.Instant;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class SessionManager {

    public static final String SESSION_KEY = "bsessionid";
    public static final int timeout = 60*60*24;

    private Map<String, HttpSession> sessionMap = new ConcurrentHashMap<>();


    public HttpSession getSession(String id) {
        return sessionMap.get(id);
    }

    public String createSession(HttpRequest request) {
        Optional<String> sessionId = sessionId(request);
        HttpSession session;
        String id;
        if(!sessionId.isPresent() || !sessionMap.containsKey(sessionId.get())){
            long now = Instant.now().getEpochSecond();
            long expired = now + timeout;
            session = new HttpSession();
            session.id(UUID.randomUUID().toString());
            session.created(now);
            session.expired(expired);
            sessionMap.put(session.id(), session);
            id = session.id();
        }else{
            session = sessionMap.get(sessionId.get());
            id = null;
        }
        request.session(session);
        return id;
    }

    public void clear() {
        sessionMap.clear();
    }

    public void destorySession(HttpSession session) {
        session.attributes().clear();
        sessionMap.remove(session.id());
    }

    public Map<String, HttpSession> sessionMap(){
        return this.sessionMap;
    }

    private Optional<String> sessionId(HttpRequest request){
        return request.cookieValue(SESSION_KEY);
    }

}
