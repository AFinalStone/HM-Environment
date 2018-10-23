package com.hm.iou.environmentswitch;


import com.hm.iou.environmentswitch.annotation.Environment;
import com.hm.iou.environmentswitch.annotation.Module;

/**
 * @author syl
 * @time 2018/10/23 上午11:10
 * 环境配置类
 * 建议不要引用该类中的任何子类和成员变量️，一但引用了非正式环境的属性，打包时混淆工具就不会移除该类，导致测试地址泄漏。
 * 而 Environment Switcher 在编译 Release 版本时，会自动隐藏测试环境地址。
 * 通过 EnvironmentSwitcher.
 * isRelease只能有一个
 * Debug模式的isDefault也只能有一个
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
