package me.stevenkin.boomvc.http.session;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class HttpSession {

    private Map<String, Object> attributes = new HashMap<>();

    private String id = null;

    private String ip = null;

    private long created = -1;

    private long expired = -1;

    public String id() {
        return id;
    }

    public void id(String id) {
        this.id = id;
    }

    public String ip() {
        return this.ip;
    }

    public void ip(String ip) {
        this.ip = ip;
    }

    public Optional<Object> attribute(String name) {
        Object object = this.attributes.get(name);
        return Optional.ofNullable(object);
    }

    public void attribute(String name, Object value) {
        this.attributes.put(name, value);
    }

    public Map<String, Object> attributes() {
        return attributes;
    }

    public void removeAttribute(String name) {
        this.attributes.remove(name);
    }

    public long created() {
        return this.created;
    }

    public void created(long created) {
        this.created = created;
    }

    public long expired() {
        return this.expired;
    }

    public void expired(long expired) {
        this.expired = expired;
    }
}
