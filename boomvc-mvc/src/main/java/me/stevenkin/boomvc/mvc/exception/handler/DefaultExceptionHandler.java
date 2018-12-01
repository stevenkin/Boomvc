package me.stevenkin.boomvc.mvc.exception.handler;

import me.stevenkin.boomvc.common.view.ModelAndView;
import me.stevenkin.boomvc.http.HttpRequest;
import me.stevenkin.boomvc.http.HttpResponse;
import me.stevenkin.boomvc.ioc.Ioc;
import me.stevenkin.boomvc.mvc.exception.InternalException;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class DefaultExceptionHandler implements ExceptionHandler {

    private InternalExceptionHandler internalExceptionHandler;

    private List<ExceptionHandler> customExceptionHandlers;

    @Override
    public ModelAndView handleException(Exception e) {
        if(e instanceof InternalException) {
            return this.internalExceptionHandler.handleException(e);
        }
        ExceptionHandler customExceptionHandler = this.customExceptionHandlers.stream()
                .filter(c->{
                    Class clazz = c.getClass();
                    return ((me.stevenkin.boomvc.mvc.annotation.ExceptionHandler) clazz.getAnnotation(me.stevenkin.boomvc.mvc.annotation.ExceptionHandler.class)).exception()
                            .isAssignableFrom(e.getClass());
                }).findFirst().orElse(this.internalExceptionHandler);
        return customExceptionHandler.handleException(e);
    }

    public void registerExceptionHandler(Ioc ioc){
        List<ExceptionHandler> exceptionHandlers = ioc.getBeans(ExceptionHandler.class);
        Optional<ExceptionHandler> internalExceptionHandlerOp = exceptionHandlers.stream().filter(e->InternalExceptionHandler.class.equals(e.getClass())).findFirst();
        this.internalExceptionHandler = (InternalExceptionHandler) internalExceptionHandlerOp.orElseThrow(RuntimeException::new);
        exceptionHandlers.remove(this.internalExceptionHandler);
        this.customExceptionHandlers = exceptionHandlers.stream()
                .filter(e->e.getClass().getAnnotation(me.stevenkin.boomvc.mvc.annotation.ExceptionHandler.class) != null)
                .sorted(Comparator.comparing(e-> (Integer) (e.getClass().getAnnotation(me.stevenkin.boomvc.mvc.annotation.ExceptionHandler.class).order())))
                .collect(Collectors.toList());
    }
}
