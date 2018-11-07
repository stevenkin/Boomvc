package me.stevenkin.boomvc.http.kit;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.regex.Pattern;

public class PathKit {

    public static final  String  VAR_REGEXP          = ":(\\w+)";
    public static final  String  VAR_REPLACE         = "([^#/?.]+)";
    private static final String  SLASH               = "/";
    public static final  Pattern VAR_REGEXP_PATTERN  = Pattern.compile(VAR_REGEXP);
    private static final Pattern VAR_FIXPATH_PATTERN = Pattern.compile("\\s");

    public static String fixPath(String path) {
        if (null == path) {
            return SLASH;
        }
        if (path.charAt(0) != '/') {
            path = SLASH + path;
        }
        if (path.length() > 1 && path.endsWith(SLASH)) {
            path = path.substring(0, path.length() - 1);
        }
        if (!path.contains("\\s")) {
            return path;
        }
        return VAR_FIXPATH_PATTERN.matcher(path).replaceAll("%20");
    }

    public static String cleanPath(String path) {
        if (path == null) {
            return null;
        }
        return path.replaceAll("[/]+", SLASH);
    }

    public static String getCurrentClassPath() {
        URL url = PathKit.class.getResource("/");
        String path;
        if (null == url) {
            File f = new File(PathKit.class.getProtectionDomain().getCodeSource().getLocation().getPath());
            path = f.getPath();
        } else {
            path = url.getPath();
        }
        if (isWindows()) {
            return decode(path.replaceFirst("^/(.:/)", "$1"));
        }
        return decode(path);
    }

    public static boolean isInJar(){
        return getCurrentClassPath().endsWith(".jar");
    }

    public static boolean isWindows(){
        return System.getProperties().getProperty("os.name").toLowerCase().contains("win");
    }

    private static String decode(String path) {
        try {
            return java.net.URLDecoder.decode(path, "utf-8");
        } catch (UnsupportedEncodingException e) {
            return path;
        }
    }

}