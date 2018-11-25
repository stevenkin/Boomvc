package me.stevenkin.boomvc.mvc.view;

import me.stevenkin.boomvc.common.view.ModelAndView;
import me.stevenkin.boomvc.common.view.View;

import static me.stevenkin.boomvc.http.Const.*;

public class DefaultViewResolver implements ViewResolver {

    private Class<? extends View> viewType;

    @Override
    public View resolve(ModelAndView modelAndView) throws Exception {
        if(modelAndView.isRestful())
            return new RestfulView();
        Object view = modelAndView.getView();
        if(view instanceof View)
            return (View) view;
        String viewName = (String) view;
        if(viewName.startsWith(REDIRECT_PREFIX))
            return new RedirectView();
        return viewType.newInstance();
    }

    @Override
    public void registerView(Class<? extends View> viewType) {
        this.viewType = viewType;
    }
}
