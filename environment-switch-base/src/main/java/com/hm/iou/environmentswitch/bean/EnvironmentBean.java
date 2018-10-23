package com.hm.iou.environmentswitch.bean;

import com.hm.iou.environmentswitch.annotation.Environment;

import java.io.Serializable;

/**
 * 每个被 {@link Environment} 标记的属性，在编译时都会在
 * EnvironmentSwitcher.java 文件中生成与之一一对应的 {@link EnvironmentBean}。
 * <p>
 * Each attribute marked by {@link Environment}
 * will generate a one-to-one correspondence with {@link EnvironmentBean} in the  EnvironmentSwitcher.java file at compile time.
 */
public class EnvironmentBean implements Serializable {
    private String name;
    private String alias;
    private String url;
    private ModuleBean module;

    public EnvironmentBean() {
    }

    public EnvironmentBean(String name, String url, String alias, ModuleBean module) {
        this.name = name;
        this.url = url;
        this.alias = alias;
        this.module = module;
    }

    public String getName() {
        return name == null ? "" : name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url == null ? "" : url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getAlias() {
        return alias == null ? "" : alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public ModuleBean getModule() {
        return module;
    }

    public void setModule(ModuleBean module) {
        this.module = module;
    }

    @Override
    public String toString() {
        return "EnvironmentBean{" +
                "name='" + name + '\'' +
                ", alias='" + alias + '\'' +
                ", url='" + url + '\'' +
                ", module=" + module +
                '}';
    }
}



