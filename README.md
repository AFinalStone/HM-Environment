
提供了动态配置服务器环境地址的功能

在项目中添加一个类对象实现注解：

```java
package com.hm.iou;


import com.hm.iou.environmentswitcher.annotation.Environment;
import com.hm.iou.environmentswitcher.annotation.Module;

/**
* @author syl
* @time 2018/10/23 上午11:10
* 环境配置类
* 建议不要引用该类中的任何子类和成员变量️，一但引用了非正式环境的属性，打包时混淆工具就不会移除该类，导致测试地址泄漏。
* 而 Environment Switcher 在编译 Release 版本时，会自动隐藏测试环境地址。
* 通过 EnvironmentSwitcher.
*/

public class EnvironmentConfig {

public static final String API_SERVER_NAME = "接口服务器地址";
public static final String H5_SERVER_NAME = "H5网页服务器地址";
public static final String FILE_SERVER_NAME = "文件服务器地址";

/**
* 线上正式环境
*/
@Module(isRelease = true)
private class Online {
@Environment(url = "https://api.54jietiao.com", alias = API_SERVER_NAME)
private String apiServerUrl;

@Environment(url = "https://h5.54jietiao.com", alias = H5_SERVER_NAME)
private String h5ServerUrl;

@Environment(url = "https://upload.54jietiao.com", alias = FILE_SERVER_NAME)
private String fileServerUrl;
}

/**
* 测试环境
*/
@Module(isDefault = true)
private class Test {

@Environment(url = "https://re.54jietiao.com", alias = API_SERVER_NAME)
private String apiServerUrl;

@Environment(url = "https://re.54jietiao.com", alias = H5_SERVER_NAME)
private String h5ServerUrl;

@Environment(url = "https://re.54jietiao.com", alias = FILE_SERVER_NAME)
private String fileServerUrl;

}

/**
* 开发环境
*/
@Module
private class Develop {

@Environment(url = "http://192.168.1.217", alias = API_SERVER_NAME)
private String apiServerUrl;

@Environment(url = "http://192.168.1.217", alias = H5_SERVER_NAME)
private String h5ServerUrl;

@Environment(url = "http://192.168.1.217", alias = FILE_SERVER_NAME)
private String fileServerUrl;
}

}

```

然后在代码初始化的时候
```java
String apiServer = EnvironmentSwitcher.getEnvironmentByAlias(instance, BuildConfig.DEBUG, EnvironmentConfig.API_SERVER_NAME).getUrl();
String h5Server = EnvironmentSwitcher.getEnvironmentByAlias(instance, BuildConfig.DEBUG, EnvironmentConfig.H5_SERVER_NAME).getUrl();
String fileServer = EnvironmentSwitcher.getEnvironmentByAlias(instance, BuildConfig.DEBUG, EnvironmentConfig.FILE_SERVER_NAME).getUrl();
```
来动态获取服务器地址


可以通过路由跳转来跳转到动态切换服务器地址的页面

```xml
"environment_switch": [
{
"url": "hmiou://m.54jietiao.com/environment_switch/index",
"iclass": "",
"aclass": "com.hm.iou.environmentswitch.business.view.EnvironmentSwitchActivity"
}
]
```

切换之后需要重启APP才能生效；

依赖：
```
//环境切换
compile 'com.heima.iou:hmenvironmentbase:1.0.0'
compile 'com.heima.iou:hmenvironmentswitch:1.0.0'
debugAnnotationProcessor 'com.heima.iou:hmenvironmentcompilerdebug:1.0.0'
releaseAnnotationProcessor 'com.heima.iou:hmenvironmentcompilerrelease:1.0.0'

```









