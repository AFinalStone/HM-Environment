package com.hm.iou.environmentswitch.compiler;

import com.google.auto.service.AutoService;
import com.hm.iou.environmentswitch.bean.EnvironmentBean;
import com.squareup.javapoet.FieldSpec;

import javax.annotation.processing.Processor;
import javax.lang.model.element.Modifier;

@AutoService(Processor.class)
public class EnvironmentSwitcherCompilerRelease extends EnvironmentSwitcherCompilerDebug {

    @Override
    protected FieldSpec generateEnvironmentField(String moduleUpperCaseName,
                                                 String environmentName,
                                                 String environmentUpperCaseName,
                                                 String url,
                                                 String alias) {

        /**
         * 在这里隐藏测试服务器的相关url
         */
        return FieldSpec.builder(EnvironmentBean.class,
                String.format("%s_%s%s", moduleUpperCaseName, environmentUpperCaseName, VAR_DEFAULT_ENVIRONMENT_SUFFIX),
                Modifier.PUBLIC, Modifier.STATIC, Modifier.FINAL)
                .initializer(String.format("new %s(\"%s\", \"%s\", \"%s\", %s%s)",
                        EnvironmentBean.class.getSimpleName(), environmentName, "", alias, VAR_MODULE_PREFIX, moduleUpperCaseName))
                .build();
    }
}