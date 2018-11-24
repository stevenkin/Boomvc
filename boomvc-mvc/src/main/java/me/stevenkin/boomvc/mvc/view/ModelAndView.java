package me.stevenkin.boomvc.mvc.view;

import java.util.HashMap;
import java.util.Map;

public class ModelAndView {
    private Map<String, Object> model = new HashMap<>();

    private Object view;

    private boolean isRestful;

    public ModelAndView addAttribute(String key, Object value){
        model.put(key, value);
        return this;
    }

    public ModelAndView addAllAttributes(Map<String, Object> attributes){
        model.putAll(attributes);
        return this;
    }

    public ModelAndView mergeAttributes(Map<String, Object> attributes){
        attributes.keySet().forEach(key->{
            if(!model.containsKey(key))
                model.put(key, attributes.get(key));
        });
        return this;
    }

    public Map<String, Object> getModel() {
        return model;
    }

    public void setModel(Map<String, Object> model) {
        this.model = model;
    }

    public Object getView() {
        return view;
    }

    public void setView(Object view) {
        this.view = view;
    }

    public boolean isRestful() {
        return isRestful;
    }

    public void setRestful(boolean restful) {
        isRestful = restful;
    }
}
