Boomvc
===================================
`Boomvc`是一个简洁，优雅，高效的web开发框架
如果你对这个项目感兴趣，可以[star](https://github.com/StevenKin/Boomvc/stargazers):blush::blush:


### 功能特性
* 轻量级MVC框架，不依赖任何web容器
* 可以直接运行jar包启动一个web服务
* 使用jdk原生的nio api实现http server
* Restful风格路由设计
* 支持模板引擎
* 支持JSON输出
* 使用jdk1.8开发

### Hello world
- 下载源码并编译打包
```xml
1. git clone git@github.com:StevenKin/Boomvc.git
2. cd Boomvc & mvn clean install
```
- 新建一个maven工程,使用下面的依赖
```xml
<dependency>
    <groupId>me.stevenkin</groupId>
    <artifactId>boomvc-server</artifactId>
    <version>0.1</version>
</dependency>
```
- 写一个main函数
```java
public static void main(String[] args) {
        Boom.me().start(Main.class, args);
}
```
- 写一个Controller
```java
@RestPath
public class TestController {

    @GetRoute("/")
    public String hello(){
        return "hello world";
    }
}
```
- 用浏览器打开http://127.0.0.1:8000/ 就可以看到hello world了！

### 控制器
控制器类似于spring mvc里的Controller。
```java
@Path
public class TestController {
    
    @GetRoute("user")
    public String user(){
        return "user";
    }

}
```
### 参数
- 表单参数
```java
@Path
public class TestController {
    
    @PostRoute("user")
    public void addUser(@QueryParam String userName, @QueryParam String password){
        System.out.println("userName: " + userName);
        System.out.println("password: " + password);
    }
    
}
```
- 路径参数
```java
@Path
public class TestController {
    
    @PostRoute("user/{userName}")
    public void getUser(@PathParam String userName){
        System.out.println("userName: " + userName);
    }
    
}
```
- Body参数
```java
public class User {
  private String username;
  private Integer age;
  // getter and setter
}

@Path
public class TestController {
    
    @PostRoute("user")
    public void getUser(@BodyParam User user){
        System.out.println("userName: " + userName);
    }
    
}
```
- Cookie参数
```java
@Path
public class TestController {
    
    @PostRoute("user")
    public void getUser(@CookieParam String token){
        System.out.println("token: " + token);
    }
    
}
```
- Header参数
```java
@Path
public class TestController {
    
    @PostRoute("user")
    public void getUser(@HeaderParam String token){
        System.out.println("token: " + token);
    }
    
}
```
### 数据渲染
- Restful
```java
@Path
public class TestController {
    
    @PostRoute("addUser")
    @Restful
    public User addUser(@BodyParam User user){
        return user;
    }
    
}
```
- 模板引擎
>目前框架默认提供FreeMarker作为模板引擎，如果要使用，请先引入依赖
```xml
<dependency>
    <groupId>me.stevenkin</groupId>
    <artifactId>boomvc-template</artifactId>
    <version>0.1</version>
</dependency>
```
```java
@Path
public class TestController {
    
    @GetRoute("users")
    public String users(HttpRequest request){
        //get user list
        List<User> userList = ...;
        request.attribute("userList", userList)
        return "users";
    }

}
```
- 重定向
```java
@Path
public class TestController {
    
    @PostRoute("login")
    public String login(@BodyParam User user){
        // login logic
        return "redirect:index";
    }
    
}
```
- 过滤器和拦截器
> 和spring mvc一样，Boomvc也可以写自己的过滤器和拦截器，可以对请求和响应进行拦截
>只需要实现Filter和interceptor接口
```java
public interface Filter {

    void init(FilterConfig config);

    void doFilter(HttpRequest request, HttpResponse response, FilterChain chain) throws Exception;

    void destroy();

}

public interface Interceptor {

    boolean preHandle(HttpRequest request, HttpResponse response);

    void postHandle(HttpRequest request, HttpResponse response, ModelAndView modelAndView);

    void afterCompletion(HttpRequest request, HttpResponse response, Exception e);

}
```
>那么，如何注册呢？
>需要写一个配置类继承`WebMvcConfigurerAdapter`
```java
@Configuration
public class WebAppConfig extends WebMvcConfigurerAdapter{
    @Override
    public void addInterceptors(WebMvcRegistry registry) {
        Interceptor interceptor = new MyInterceptor();
        registry.addInterceptor(interceptor)
                .order(1)
                .patternUrl("/*");
    }

    @Override
    public void addFilters(WebMvcRegistry registry) {
        Filter filter = new MyFilter();
        registry.addFilter(filter)
                .addFilterInitParameter("hello", "world")
                .addFilterInitParameter("ni", "hao")
                .addFilterPathPattern("/*")
                .order(1);
    }
}
```
>是不是有点像spring boot的风格？
:flushed:

### 特别感谢
>Boomvc 的部分代码风格和灵感来自于一下项目
- [SpringMvc](https://github.com/spring-projects/spring-framework)
- [blade](https://github.com/lets-blade/blade)