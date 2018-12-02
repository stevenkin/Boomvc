package me.stevenkin.boomvc.server.session;

import me.stevenkin.boomvc.http.session.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.util.Collection;
import java.util.concurrent.TimeUnit;

public class SessionCleaner implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(SessionCleaner.class);

    private SessionManager sessionManager;

    public SessionCleaner(SessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }

    @Override
    public void run() {
        while (true) {
            try {
                Collection<HttpSession> sessions = sessionManager.sessionMap().values();
                sessions.stream().filter(this::expires).forEach(sessionManager::destorySession);
            } catch (Exception e) {
                logger.error("", e);
            } finally {
                try {
                    TimeUnit.MILLISECONDS.sleep(500);
                } catch (InterruptedException e) {
                    logger.error("", e);
                }
            }

        }
    }

    private boolean expires(HttpSession session) {
        long now = Instant.now().getEpochSecond();
        return session.expired() < now;
    }

}
