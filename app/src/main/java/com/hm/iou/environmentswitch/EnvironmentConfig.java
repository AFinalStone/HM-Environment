package com.hm.iou.environmentswitch;


import com.hm.iou.environmentswitcher.annotation.Environment;
import com.hm.iou.environmentswitcher.annotation.Module;

/**
 * 环境配置类</br>
 * <p>
 * ⚠ 建议不要引用该类中的任何子类和成员变量️，一但引用了非正式环境的属性，打包时混淆工具就不会移除该类，导致测试地址泄漏。</br>
 * 而 Environment Switcher 在编译 Release 版本时，会自动隐藏测试环境地址。</br></br>
 * <p>
 * 建议将该类中所有被 {@link Module} 和 {@link Environment} 修饰的类或成员变量用 private 修饰，</br>
 * Environment Switcher 会在编译期间自动生成相应的 Module_XX 和 Environment_XX 静态常量。</br>
 * 例如：通过 EnvironmentSwitcher.MODULE_APP 就可以获取到 App 模块下相应的所有环境</br>
 */
public class EnvironmentConfig {

    /**
     * 线上正式环境
     */
    @Module(isRelease = true)
    private class Online {
        @Environment(url = "https://api.54jietiao.com")
        private String apiServerUrl;

        @Environment(url = "https://upload.54jietiao.com")
        private String h5ServerUrl;

        @Environment(url = "https://h5.54jietiao.com")
        private String fileServerUrl;
    }

    /**
     * 测试环境
     */
    @Module(isDefault = true)
    private class Test {

        @Environment(url = "http://192.168.1.217")
        private String apiServerUrl;

        @Environment(url = "http://192.168.1.217")
        private String h5ServerUrl;

        @Environment(url = "http://192.168.1.217")
        private String fileServerUrl;
    }

    /**
     * 开发环境
     */
    @Module
    private class Develop {

        @Environment(url = "https://re.54jietiao.com")
        private String apiServerUrl;

        @Environment(url = "https://re.54jietiao.com")
        private String h5ServerUrl;

        @Environment(url = "https://re.54jietiao.com")
        private String fileServerUrl;
    }


}
