package me.stevenkin.boomvc.mvc.view;

import me.stevenkin.boomvc.common.view.ModelAndView;
import me.stevenkin.boomvc.common.view.View;
import me.stevenkin.boomvc.http.HttpRequest;
import me.stevenkin.boomvc.http.HttpResponse;

import java.util.Map;

import static me.stevenkin.boomvc.mvc.resolver.imp.RestfulReturnValueResolver.RESTFUL_RESPONSE;

public class RestfulView implements View {
    @Override
    public void render(ModelAndView modelAndView, HttpRequest request, HttpResponse response) throws Exception {
        Map<String, Object> model = modelAndView.getModel();
        response.json((String) model.get(RESTFUL_RESPONSE));
    }
}
