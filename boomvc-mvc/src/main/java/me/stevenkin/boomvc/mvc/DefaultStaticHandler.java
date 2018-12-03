package me.stevenkin.boomvc.mvc;

import me.stevenkin.boomvc.http.HttpRequest;
import me.stevenkin.boomvc.http.HttpResponse;
import me.stevenkin.boomvc.mvc.exception.InternalErrorException;
import me.stevenkin.boomvc.mvc.exception.NoFoundException;

import java.io.File;
import java.net.URL;
import java.util.Optional;

public class DefaultStaticHandler implements StaticHandler {

    private boolean showDir = false;

    public DefaultStaticHandler(boolean showDir) {
        this.showDir = showDir;
    }

    @Override
    public void handleStatic(String staticPath, HttpRequest request, HttpResponse response) throws Exception {
        if (staticPath.startsWith("/"))
            staticPath = staticPath.substring(1);
        String dirPath = request.uri();
        Optional<URL> urlOptional = Optional.ofNullable(this.getClass().getClassLoader().getResource(staticPath));
        URL url = urlOptional.orElseThrow(NoFoundException::new);
        File file = new File(url.getPath());
        if (!file.exists())
            throw new NoFoundException();
        if (file.isDirectory()) {
            if (!request.uri().endsWith("/")) {
                StringBuilder newUrl = new StringBuilder(request.uri()).append("/");
                if (request.queryString() != null && request.queryString().length() > 0)
                    newUrl.append("?").append(request.queryString());
                response.redirect(newUrl.toString());
                return;
            }
            StringBuilder buf = new StringBuilder();
            if (showDir) {
                buf.append("<!DOCTYPE html>\r\n")
                        .append("<html><head><meta charset='utf-8' /><title>")
                        .append("File list: ")
                        .append(dirPath)
                        .append("</title></head><body>\r\n")
                        .append("<h3>File list: ")
                        .append(dirPath)
                        .append("</h3>\r\n")
                        .append("<ul>")
                        .append("<li><a href=\"../\">..</a></li>\r\n");
                for (File f1 : file.listFiles()) {
                    if (f1.isHidden() || !f1.canRead()) {
                        continue;
                    }
                    String name = f1.getName();
                    buf.append("<li><a href=\"")
                            .append(name)
                            .append("\">")
                            .append(name)
                            .append("</a></li>\r\n");
                }

                buf.append("</ul></body></html>\r\n");
            }else{
                buf.append("currect path '")
                        .append(dirPath)
                        .append("' is directory and can not show it, if you want show, please open 'mvc.statics.show-list' in config file");
            }
            response.html(buf.toString());
            return;
        }
        try {
            response.download(file);
        }catch (Exception e){
            throw new InternalErrorException("read file '" + file.getPath() + "' happen error", e);
        }
    }
}
