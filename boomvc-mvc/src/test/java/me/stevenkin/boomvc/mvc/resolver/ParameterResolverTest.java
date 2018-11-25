package me.stevenkin.boomvc.mvc.resolver;

import me.stevenkin.boomvc.common.resolver.MethodParameter;
import me.stevenkin.boomvc.common.resolver.ParameterResolver;
import me.stevenkin.boomvc.http.HttpQueryParameter;
import me.stevenkin.boomvc.http.HttpRequest;
import me.stevenkin.boomvc.http.HttpResponse;
import me.stevenkin.boomvc.mvc.annotation.QueryParam;
import me.stevenkin.boomvc.mvc.resolver.imp.QueryParamResolver;
import org.junit.Assert;
import org.junit.Test;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Optional;

import static org.mockito.Mockito.*;

public class ParameterResolverTest {

    @Test
    public void queryParamResolverTest() throws Exception {
        MethodParameter methodParameter = mock(MethodParameter.class);
        Method method = ParameterResolverTest.class.getDeclaredMethod("queryParam", new Class[]{String.class});
        Annotation[][] annotations = method.getParameterAnnotations();
        Type[] paramTypes = method.getGenericParameterTypes();
        Annotation queryParam = annotations[0][0];
        Type paramType = paramTypes[0];
        when(methodParameter.getParameterAnnotation()).thenReturn(queryParam);
        when(methodParameter.getParameterType()).thenReturn(paramType);

        HttpRequest request = mock(HttpRequest.class);
        when(request.firstParameter("hello")).thenReturn(Optional.of(new HttpQueryParameter("hello", "boomvc")));

        HttpResponse response = mock(HttpResponse.class);
        ParameterResolver queryParamResolver = new QueryParamResolver();
        Assert.assertTrue(queryParamResolver.support(methodParameter));
        System.out.println(queryParamResolver.resolve(methodParameter, request, response));
        Assert.assertSame("boomvc", queryParamResolver.resolve(methodParameter, request, response));
    }

    public void queryParam(@QueryParam("hello") String hello){

    }
}
