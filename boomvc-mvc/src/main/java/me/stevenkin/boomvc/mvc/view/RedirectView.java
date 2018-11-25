package me.stevenkin.boomvc.mvc.view;

import me.stevenkin.boomvc.common.view.ModelAndView;
import me.stevenkin.boomvc.common.view.View;
import me.stevenkin.boomvc.http.HttpRequest;
import me.stevenkin.boomvc.http.HttpResponse;

import static me.stevenkin.boomvc.http.Const.*;

public class RedirectView implements View {
    @Override
    public void render(ModelAndView modelAndView, HttpRequest request, HttpResponse response) throws Exception {
        response.redirect(((String)modelAndView.getView()).substring(REDIRECT_PREFIX.length()));
        response.flush();
    }
}
