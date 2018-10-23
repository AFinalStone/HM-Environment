package com.hm.iou.environmentswitch.bean;

import com.hm.iou.environmentswitch.annotation.Module;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 每个被 {@link Module} 标记的属性，在编译时都会在
 * EnvironmentSwitcher.java 文件中生成与之一一对应的 {@link ModuleBean}。
 * <p>
 * Each attribute marked by {@link Module}
 * will generate a one-to-one correspondence with {@link ModuleBean} in the  EnvironmentSwitcher.java file at compile time.
 */
public class ModuleBean implements Serializable {

    private String name;

    private String alias;

    private List<EnvironmentBean> environments;

    private boolean isRelease;
    private boolean isDefault;


    public ModuleBean() {
        this("", "");
    }

    public ModuleBean(String name, String alias) {
        this(name, alias, new ArrayList<EnvironmentBean>());
    }

    public ModuleBean(String name, String alias, List<EnvironmentBean> environments) {
        this(name, alias, environments, false, false);
    }

    public ModuleBean(String name, String alias, boolean isRelease, boolean isDefault) {
        this(name, alias, new ArrayList<EnvironmentBean>(), isRelease, isDefault);
    }

    public ModuleBean(String name, String alias, List<EnvironmentBean> environments, boolean isRelease, boolean isDefault) {
        this.name = name;
        this.alias = alias;
        this.environments = environments;
        this.isRelease = isRelease;
        this.isDefault = isDefault;
    }

    public String getName() {
        return name == null ? "" : name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAlias() {
        return alias == null ? "" : alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public List<EnvironmentBean> getEnvironments() {
        return environments;
    }

    public void setEnvironments(List<EnvironmentBean> environments) {
        this.environments = environments;
    }

    public boolean isRelease() {
        return isRelease;
    }

    public void setRelease(boolean release) {
        isRelease = release;
    }

    public boolean isDefault() {
        return isDefault;
    }

    public void setDefault(boolean aDefault) {
        isDefault = aDefault;
    }

    @Override
    public String toString() {
        return "ModuleBean{" +
                "name='" + name + '\'' +
                ", alias='" + alias + '\'' +
                ", environments=" + environments +
                ", isRelease=" + isRelease +
                ", isDefault=" + isDefault +
                '}';
    }
}