package me.stevenkin.boomvc.mvc.view;

import me.stevenkin.boomvc.common.view.ModelAndView;
import me.stevenkin.boomvc.common.view.View;
import me.stevenkin.boomvc.ioc.annotation.ConfigProperties;
import me.stevenkin.boomvc.ioc.annotation.Value;

import java.lang.reflect.Constructor;

import static me.stevenkin.boomvc.http.Const.*;

@ConfigProperties(prefix = "mvc.template")
public class DefaultViewResolver implements ViewResolver {

    private Class<? extends View> viewType;

    @Value
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
    public void registerView(Class<? extends View> viewType) {
        this.viewType = viewType;
    }
}
