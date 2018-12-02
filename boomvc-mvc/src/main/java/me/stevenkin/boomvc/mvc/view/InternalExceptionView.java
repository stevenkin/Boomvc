package me.stevenkin.boomvc.mvc.view;

import me.stevenkin.boomvc.common.view.ModelAndView;
import me.stevenkin.boomvc.common.view.View;
import me.stevenkin.boomvc.http.Const;
import me.stevenkin.boomvc.http.HttpRequest;
import me.stevenkin.boomvc.http.HttpResponse;

public class InternalExceptionView implements View {
    @Override
    public void render(ModelAndView modelAndView, HttpRequest request, HttpResponse response) throws Exception {
        if(modelAndView.isIs404()){
            response.body(Const.NOT_FOUND_ERROR_HTML);
            return;
        }
        response.body(Const.INTERNAL_SERVER_ERROR_HTML);
    }
}
