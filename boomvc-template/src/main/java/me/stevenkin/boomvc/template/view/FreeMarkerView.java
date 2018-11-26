package me.stevenkin.boomvc.template.view;

import freemarker.template.Configuration;
import freemarker.template.Template;
import me.stevenkin.boomvc.common.view.ModelAndView;
import me.stevenkin.boomvc.common.view.View;
import me.stevenkin.boomvc.http.HttpRequest;
import me.stevenkin.boomvc.http.HttpResponse;

import java.util.Locale;
import java.util.Map;

public class FreeMarkerView implements View {
    private static final String DEFAULT_ENCODING = "UTF-8";
    private  String suffix =".ftl";
    private Configuration configuration = null;
    private String templatePath;

    public FreeMarkerView(String templatePath) {
        this.templatePath = templatePath;
        configuration = new Configuration();
        configuration.setEncoding(Locale.CHINA, DEFAULT_ENCODING);
        configuration.setClassForTemplateLoading(FreeMarkerView.class, this.templatePath);
    }

    @Override
    public void render(ModelAndView modelAndView, HttpRequest request, HttpResponse response) throws Exception {
        String view = (String) modelAndView.getView();
        Map<String, Object> model = modelAndView.getModel();
        Template template = configuration.getTemplate(view+ suffix);
        template.process(model, response.writer());
    }
}
