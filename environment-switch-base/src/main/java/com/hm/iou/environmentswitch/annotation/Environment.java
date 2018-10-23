package com.hm.iou.environmentswitch.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 被 {@link Environment} 标记的属性表示一个环境
 * <p>
 * An attribute marked by {@link Environment} represents an environment
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.CLASS)
public @interface Environment {
    /**
     * @return 当前环境的具体地址，必须指定具体的值
     * <p>
     * Specific address of the current environment, you must specify a specific value
     */
    String url();

    /**
     * @return 用来指定当前 {@link Environment} 的别名
     * <p>
     * Used to specify the current alias for {@link Environment}
     */
    String alias() default "";
}
