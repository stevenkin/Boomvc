package me.stevenkin.boomvc.server.session;

import me.stevenkin.boomvc.http.session.HttpSession;

import java.time.Instant;
import java.util.Collection;
import java.util.concurrent.TimeUnit;

public class SessionCleaner implements Runnable {

    private SessionManager sessionManager;

    public SessionCleaner(SessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }

    @Override
    public void run() {
        while (true) {
            try {
                Collection<HttpSession> sessions = sessionManager.sessionMap().values();
                sessions.parallelStream().filter(this::expires).forEach(sessionManager::destorySession);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    TimeUnit.MILLISECONDS.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }
    }

    private boolean expires(HttpSession session) {
        long now = Instant.now().getEpochSecond();
        return session.expired() < now;
    }

}
