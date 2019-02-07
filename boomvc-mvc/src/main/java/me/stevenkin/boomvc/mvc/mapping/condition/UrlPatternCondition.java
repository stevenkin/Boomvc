package me.stevenkin.boomvc.mvc.mapping.condition;

import com.google.common.base.Splitter;
import me.stevenkin.boomvc.http.HttpRequest;
import me.stevenkin.boomvc.http.kit.PathKit;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class UrlPatternCondition implements Condition {

    private String urlPath;

    private String prefix;

    private String urlPattern;

    private boolean isSimpleModel;

    private List<String> templateNames = new ArrayList<>();

    private ThreadLocal<Pattern> patternThreadLocal;

    public UrlPatternCondition(String urlPath, String prefix) {
        checkPath(urlPath);
        checkPath(prefix);
        this.urlPath = urlPath;
        this.prefix = prefix;
        this.urlPattern = getUrlPattern(urlPath, prefix);
        if(isSimpleModel(this.urlPattern)){
            this.isSimpleModel = true;
        }else{
            this.isSimpleModel = false;
        }
        this.patternThreadLocal = new ThreadLocal<Pattern>(){
            @Override
            public Pattern initialValue(){
                return Pattern.compile(UrlPatternCondition.this.urlPattern);
            }
        };
    }

    private String getUrlPattern(String urlPath, String prefix){
        String completePath = PathKit.cleanPath(parsePath(prefix) + parsePath(urlPath));
        List<String> pathList = Splitter.on('/').omitEmptyStrings().trimResults().splitToList(completePath);
        String regxPath = pathList.stream().map(p->{
            if(isTemplatePath(p)){
                this.templateNames.add(p.substring(1,p.length()-1));
                return "([^/]+)";
            }else
                return p;
        }).collect(Collectors.joining("/", "^/", "$"));
        return regxPath;
    }

    private void checkPath(String path){
        if(path.contains("*"))
            throw new IllegalArgumentException("route path can not contains '*'");
    }

    private boolean isTemplatePath(String path){
        return path.length() > 2 && path.startsWith("{") && path.endsWith("}");
    }

    private boolean isSimpleModel(String urlPattern){
        return !urlPattern.contains("([^/]+)");
    }

    private String parsePath(String path) {
        path = PathKit.fixPath(path);
        try {
            URI uri = new URI(path);
            return uri.getPath();
        } catch (URISyntaxException e) {
            return path;
        }
    }


    @Override
    public boolean determine(HttpRequest request) {
        Matcher matcher = this.patternThreadLocal.get().matcher(parsePath(request.uri()));
        Map<String, String> urlVars = new HashMap<>();
        boolean match = matcher.matches();
        if(!match)
            return false;
        if(isSimpleModel)
            return true;
        String sub;
        int i = 1;
        int j = 0;
        while((sub = matcher.group(i++))!=null){
            urlVars.put(this.templateNames.get(j++), sub);
        }
        request.attribute(HttpRequest.URI_PATTERN_VALUES, urlVars);
        return true;
    }
}
