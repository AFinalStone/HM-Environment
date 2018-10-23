package com.hm.iou.environmentswitch.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 被 {@link Module} 修饰的类或接口表示一个模块，编译时会自动生成相应模块的 getXXEnvironment() 和 setXXEnvironment() 方法。
 * <p>
 * 一个被 {@link Module} 修饰的类或接口中，可以有 n (n>0) 个被 {@link Environment} 修饰的属性，表示该模块中有 n 种环境。
 * <p>
 * A class or interface decorated with {@link Module} represents a module that
 * automatically generates the getXXEnvironment() and setXXEnvironment() methods
 * of the corresponding module at compile time.
 * <p>
 * A class or interface decorated with {@link Module} can have n (n>0) attributes modified by {@link Environment},
 * indicating that there are n environments in the module.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.CLASS)
public @interface Module {
    /**
     * @return 用来指定当前 {@link Module} 的别名
     * <p>
     * Used to specify the current alias for {@link Module}
     */
    String alias() default "";

    /**
     * 一个 {@link Module} 中必须有且只有一个 {@link Environment} 的 isRelease 的值为 true，否则会编译失败。
     * <p>
     * There must be one and only one {@link Environment} of isRelease in a {@link Module} with a value of true,
     * otherwise the compilation will fail.
     *
     * @return 默认返回 false，当返回 true 时，当前 {@link Environment} 就是所属 {@link Module} 的默认环境，以及 App 正式发布时的环境。
     * <p>
     * By default, false is returned. When true is returned,
     * the current {@link Environment} is the default environment for the {@link Module}
     * and the environment when the app was officially released.
     */
    boolean isRelease() default false;

    boolean isDefault() default false;
}