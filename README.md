Boomvc
===================================
`Boomvc`是一个简洁，优雅，高效的web开发框架
如果你对这个项目感兴趣，可以[star](https://github.com/StevenKin/Boomvc/stargazers):blush::blush:


###功能特性
* 轻量级MVC框架，不依赖任何web容器
* 可以直接运行jar包启动一个web服务
* 使用jdk原生的nio api实现http server
* Restful风格路由设计
* 支持模板引擎
* 支持JSON输出
* 使用jdk1.8开发

###Hello world
- 下载源码并编译打包
```xml
1. git clone git@github.com:StevenKin/Boomvc.git
2. cd Boomvc & mvn clean install
```
- 新建一个maven工程,使用下面的依赖
```xml
<dependency>
    <artifactId>boomvc</artifactId>
    <groupId>me.stevenkin</groupId>
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

```

