package me.stevenkin.boomvc.mvc.filter.imp;

import me.stevenkin.boomvc.mvc.kit.AntPathMatcher;
import me.stevenkin.boomvc.mvc.kit.PathMatcher;

import java.util.regex.Pattern;

public class PatternThreadLocal extends ThreadLocal<PathMatcher> {

    public PatternThreadLocal() {
    }

    @Override
    public PathMatcher initialValue(){
        return new AntPathMatcher();
    }
}
