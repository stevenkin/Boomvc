package me.stevenkin.boomvc.mvc.view;

import me.stevenkin.boomvc.common.view.ModelAndView;
import me.stevenkin.boomvc.common.view.View;

public interface ViewResolver {

    View resolve(ModelAndView modelAndView) throws Exception;

    void init(Class<? extends View> viewType, String templatePath);

}
