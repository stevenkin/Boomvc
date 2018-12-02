package me.stevenkin.boomvc.mvc.view;

import me.stevenkin.boomvc.common.view.ModelAndView;
import me.stevenkin.boomvc.common.view.View;
import me.stevenkin.boomvc.mvc.exception.NoFoundTemplateException;

import java.lang.reflect.Constructor;

import static me.stevenkin.boomvc.http.Const.*;

public class DefaultViewResolver implements ViewResolver {

    private Class<? extends View> viewType;

    private String path;

    @Override
    public View resolve(ModelAndView modelAndView) throws Exception {
        if(modelAndView.isRestful())
            return new RestfulView();
        if(modelAndView.isIs404() || modelAndView.isIs500()){
            if(modelAndView.getView() != null && this.viewType != null){
                Constructor constructor = this.viewType.getDeclaredConstructor(String.class);
                return (View) constructor.newInstance(path);
            }
            return new InternalExceptionView();
        }
        Object view = modelAndView.getView();
        if(view instanceof View)
            return (View) view;
        String viewName = (String) view;
        if(viewName.startsWith(REDIRECT_PREFIX))
            return new RedirectView();
        if(this.viewType == null)
            throw new NoFoundTemplateException();
        Constructor constructor = this.viewType.getDeclaredConstructor(String.class);
        return (View) constructor.newInstance(path);
    }

    @Override
    public void init(Class<? extends View> viewType, String templatePath) {
        this.viewType = viewType;
        this.path = templatePath;
    }
}
