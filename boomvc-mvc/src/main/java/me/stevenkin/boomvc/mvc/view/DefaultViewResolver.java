package me.stevenkin.boomvc.mvc.view;

import me.stevenkin.boomvc.common.view.ModelAndView;
import me.stevenkin.boomvc.common.view.View;

import java.lang.reflect.Constructor;

import static me.stevenkin.boomvc.http.Const.*;

public class DefaultViewResolver implements ViewResolver {

    private Class<? extends View> viewType;

    private String path;

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
        Constructor constructor = this.viewType.getDeclaredConstructor(String.class);
        return (View) constructor.newInstance(path);
    }

    @Override
    public void init(Class<? extends View> viewType, String templatePath) {
        this.viewType = viewType;
        this.path = templatePath;
    }
}
