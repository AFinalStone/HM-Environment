package com.hm.iou.environmentswitch.compiler;

import com.google.auto.service.AutoService;
import com.hm.iou.environmentswitch.Constants;
import com.hm.iou.environmentswitch.annotation.Environment;
import com.hm.iou.environmentswitch.annotation.Module;
import com.hm.iou.environmentswitch.bean.EnvironmentBean;
import com.hm.iou.environmentswitch.bean.ModuleBean;
import com.hm.iou.environmentswitch.listener.OnEnvironmentChangeListener;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;

/**
 * @author syl
 * @time 2018/10/23 下午4:06
 */
@AutoService(Processor.class)
public class EnvironmentSwitcherCompilerDebug extends AbstractProcessor {

    /**
     * 监听方法
     */
    public static final String VAR_PARAMETER_ON_ENVIRONMENT_CHANGE_LISTENER = "onEnvironmentChangeListener";
    public static final String VAR_PARAMETER_MODULE = "module";
    public static final String VAR_PARAMETER_OLD_ENVIRONMENT = "oldEnvironment";
    public static final String VAR_PARAMETER_NEW_ENVIRONMENT = "newEnvironment";
    public static final String METHOD_NAME_ADD_ON_ENVIRONMENT_CHANGE_LISTENER = "addOnEnvironmentChangeListener";
    public static final String METHOD_NAME_REMOVE_ON_ENVIRONMENT_CHANGE_LISTENER = "removeOnEnvironmentChangeListener";
    public static final String METHOD_NAME_REMOVE_ALL_ON_ENVIRONMENT_CHANGE_LISTENER = "removeAllOnEnvironmentChangeListener";
    public static final String METHOD_NAME_ON_ENVIRONMENT_CHANGE = "onEnvironmentChange";
    public static final String VAR_ON_ENVIRONMENT_CHANGE_LISTENERS = "ON_ENVIRONMENT_CHANGE_LISTENERS";

    /**
     * 内容中存储服务器url的属性
     */
    public static final String VAR_MODULE_PREFIX = "MODULE_";
    public static final String VAR_DEFAULT_ENVIRONMENT_PREFIX = "DEFAULT_";
    public static final String VAR_DEFAULT_ENVIRONMENT_SUFFIX = "_ENVIRONMENT";
    public static final String VAR_MODULE_LIST = "MODULE_LIST";

    /**
     * SharedPreferences存储数据需要用到
     */
    public static final TypeName CONTEXT_TYPE_NAME = ClassName.get("android.content", "Context");
    public static final String VAR_CONTEXT = "context";
    public static final String MODE_PRIVATE = "android.content.Context.MODE_PRIVATE";

    /**
     * 存储/获取当前选中的Module
     */
    public static final String VAR_KEY_SELECT_MODULE_NAME = "selectModuleName";
    public static final String VAR_DEFAULT_MODULE = "DEFAULT_MODULE";
    public static final String VAR_MODULE_BEAN = "moduleBean";
    public static final String VAR_PARAMETER_IS_DEBUG = "isDebug";

    /**
     * 根据别名获取Environment对象
     */
    public static final String METHOD_NAME_GET_ENVIRONMENT_BY_ALIAS = "getEnvironmentByAlias";
    public static final String VAR_PARAMETER_ALIAS = "alias";

    /**
     * 获取当前环境服务器的具体链接
     */
    public static final String METHOD_NAME_GET_XX_ENVIRONMENT_URL = "get%sEnvironmentUrl";


    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        /**
         * 获取所有包含Module注解的类对象
         */
        Set<? extends Element> elements = roundEnvironment.getElementsAnnotatedWith(Module.class);

        /**
         * 生成一个名为public final class EnvironmentSwitcher的类
         */
        TypeSpec.Builder environmentSwitcherClassBuilder = TypeSpec
                .classBuilder(Constants.ENVIRONMENT_SWITCHER_FILE_NAME)
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL);

        /**
         * 添加属性
         * private static final ArrayList ON_ENVIRONMENT_CHANGE_LISTENERS = new ArrayList<OnEnvironmentChangeListener>();
         */
        FieldSpec onEnvironmentChangeListenersFiled = FieldSpec
                .builder(ArrayList.class, VAR_ON_ENVIRONMENT_CHANGE_LISTENERS)
                .addModifiers(Modifier.PRIVATE, Modifier.STATIC, Modifier.FINAL)
                .initializer(String.format("new %s<%s>()", ArrayList.class.getSimpleName(), OnEnvironmentChangeListener.class.getSimpleName()))
                .build();
        environmentSwitcherClassBuilder.addField(onEnvironmentChangeListenersFiled);

        /**
         * 添加方法，监听环境是否发生了切换
         * public static void addOnEnvironmentChangeListener(OnEnvironmentChangeListener onEnvironmentChangeListener) {
         *   ON_ENVIRONMENT_CHANGE_LISTENERS.add(onEnvironmentChangeListener);
         *  }
         */
        MethodSpec addOnEnvironmentChangeListenerMethod = MethodSpec
                .methodBuilder(METHOD_NAME_ADD_ON_ENVIRONMENT_CHANGE_LISTENER)
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .returns(void.class)
                .addParameter(OnEnvironmentChangeListener.class, VAR_PARAMETER_ON_ENVIRONMENT_CHANGE_LISTENER)
                .addStatement(String.format("%s.add(%s)", VAR_ON_ENVIRONMENT_CHANGE_LISTENERS, VAR_PARAMETER_ON_ENVIRONMENT_CHANGE_LISTENER))
                .build();
        environmentSwitcherClassBuilder.addMethod(addOnEnvironmentChangeListenerMethod);

        /**
         * 添加方法 移除监听环境是否发生了变化的接口对象
         *public static void removeOnEnvironmentChangeListener(OnEnvironmentChangeListener onEnvironmentChangeListener) {
         *   ON_ENVIRONMENT_CHANGE_LISTENERS.remove(onEnvironmentChangeListener);
         *  }
         */
        MethodSpec removeOnEnvironmentChangeListenerMethod = MethodSpec
                .methodBuilder(METHOD_NAME_REMOVE_ON_ENVIRONMENT_CHANGE_LISTENER)
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .returns(void.class)
                .addParameter(OnEnvironmentChangeListener.class, VAR_PARAMETER_ON_ENVIRONMENT_CHANGE_LISTENER)
                .addStatement(String.format("%s.remove(%s)", VAR_ON_ENVIRONMENT_CHANGE_LISTENERS, VAR_PARAMETER_ON_ENVIRONMENT_CHANGE_LISTENER))
                .build();
        environmentSwitcherClassBuilder.addMethod(removeOnEnvironmentChangeListenerMethod);

        /**
         *添加方法 移除监听环境是否发生了变化的所有接口对象
         * public static void removeAllOnEnvironmentChangeListener() {
         *   ON_ENVIRONMENT_CHANGE_LISTENERS.clear();
         *  }
         */
        MethodSpec removeAllOnEnvironmentChangeListenerMethod = MethodSpec
                .methodBuilder(METHOD_NAME_REMOVE_ALL_ON_ENVIRONMENT_CHANGE_LISTENER)
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .returns(void.class)
                .addStatement(String.format("%s.clear()", VAR_ON_ENVIRONMENT_CHANGE_LISTENERS))
                .build();
        environmentSwitcherClassBuilder.addMethod(removeAllOnEnvironmentChangeListenerMethod);

        /**
         * 添加方法 环境切换的回调方法
         * private static void onEnvironmentChange(ModuleBean module, EnvironmentBean oldEnvironment,
         *      EnvironmentBean newEnvironment) {
         *      for (Object onEnvironmentChangeListener : ON_ENVIRONMENT_CHANGE_LISTENERS) {
         *      if (onEnvironmentChangeListener instanceof OnEnvironmentChangeListener) {
         *      ((OnEnvironmentChangeListener) onEnvironmentChangeListener).onEnvironmentChanged(module, oldEnvironment, newEnvironment);
         *      }
         * }
         *}
         */
        MethodSpec onEnvironmentChangeMethod = MethodSpec
                .methodBuilder(METHOD_NAME_ON_ENVIRONMENT_CHANGE)
                .addModifiers(Modifier.PRIVATE, Modifier.STATIC)
                .returns(void.class)
                .addParameter(ModuleBean.class, VAR_PARAMETER_MODULE)
                .addParameter(EnvironmentBean.class, VAR_PARAMETER_OLD_ENVIRONMENT)
                .addParameter(EnvironmentBean.class, VAR_PARAMETER_NEW_ENVIRONMENT)
                .addCode(String.format(
                        "for (Object onEnvironmentChangeListener : %s) {\n" +
                                "   if (onEnvironmentChangeListener instanceof %s) {\n" +
                                "       ((%s) onEnvironmentChangeListener).onEnvironmentChanged(%s, %s, %s);\n" +
                                "   }\n" +
                                "}\n", VAR_ON_ENVIRONMENT_CHANGE_LISTENERS,
                        OnEnvironmentChangeListener.class.getSimpleName(),
                        OnEnvironmentChangeListener.class.getSimpleName(), VAR_PARAMETER_MODULE, VAR_PARAMETER_OLD_ENVIRONMENT, VAR_PARAMETER_NEW_ENVIRONMENT))
                .build();
        environmentSwitcherClassBuilder.addMethod(onEnvironmentChangeMethod);

        /**
         * 添加属性 Module列表
         *   private static final ArrayList MODULE_LIST = new ArrayList<ModuleBean>();
         */
        FieldSpec moduleListField = FieldSpec
                .builder(ArrayList.class, VAR_MODULE_LIST, Modifier.PRIVATE, Modifier.STATIC, Modifier.FINAL)
                .initializer(String.format("new %s<%s>()", ArrayList.class.getSimpleName(), ModuleBean.class.getSimpleName()))
                .build();
        environmentSwitcherClassBuilder.addField(moduleListField);

        /**
         *  添加方法 获取Module列表
         *  public static ArrayList getModuleList() {
         *   return MODULE_LIST;
         *  }
         */
        MethodSpec getModuleListMethodBuilder = MethodSpec
                .methodBuilder(Constants.METHOD_NAME_GET_MODULE_LIST)
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .returns(ArrayList.class)
                .addStatement(String.format("return %s", VAR_MODULE_LIST))
                .build();
        environmentSwitcherClassBuilder.addMethod(getModuleListMethodBuilder);


        //    public static void setSelectModuleBean(Context context, ModuleBean module) {
        //        android.content.SharedPreferences sharedPreferences = context.getSharedPreferences(context.getPackageName() + ".environmentswitcher", android.content.Context.MODE_PRIVATE);
        //        sharedPreferences
        //                .edit()
        //                .putString("SelectModuleName", module.getName())
        //                .commit();
        //    }
        MethodSpec setSelectModuleBeanMethod = MethodSpec
                .methodBuilder(Constants.METHOD_NAME_SET_SELECT_MODULE)
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC, Modifier.FINAL)
                .returns(void.class)
                .addParameter(CONTEXT_TYPE_NAME, VAR_CONTEXT)
                .addParameter(ModuleBean.class, VAR_MODULE_BEAN)
                .addStatement(String.format(
                        "%s.getSharedPreferences(%s.getPackageName() + \".%s\", %s).edit()\n" +
                                ".putString(\"%s\", %s.getName())\n" +
                                ".apply()",
                        VAR_CONTEXT, VAR_CONTEXT, Constants.ENVIRONMENT_SWITCHER_FILE_NAME.toLowerCase(), MODE_PRIVATE,
                        VAR_KEY_SELECT_MODULE_NAME, VAR_MODULE_BEAN))
                .build();
        environmentSwitcherClassBuilder.addMethod(setSelectModuleBeanMethod);

        MethodSpec getSelectModuleBeanMethod = MethodSpec
                .methodBuilder(Constants.METHOD_NAME_GET_SELECT_MODULE)
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC, Modifier.FINAL)
                .returns(ModuleBean.class)
                .addParameter(CONTEXT_TYPE_NAME, VAR_CONTEXT)
                .addParameter(boolean.class, VAR_PARAMETER_IS_DEBUG)
                .addCode(String.format("if (!%s) {\n" +
                                "            for (ModuleBean moduleBean : (ArrayList<ModuleBean>) %s) {\n" +
                                "                if (moduleBean.isRelease()) {\n" +
                                "                    return moduleBean;\n" +
                                "                }\n" +
                                "            }\n" +
                                "        }\n" +
                                "        android.content.SharedPreferences sharedPreferences = %s.getSharedPreferences(%s.getPackageName() + \".%s\", %s);\n" +
                                "        String name = sharedPreferences.getString(\"%s\", \"\");\n" +
                                "        for (ModuleBean moduleBean : (ArrayList<ModuleBean>) %s) {\n" +
                                "            if (android.text.TextUtils.equals(moduleBean.getName(), name)) {\n" +
                                "                return moduleBean;\n" +
                                "            }\n" +
                                "        }\n" +
                                "        for (ModuleBean moduleBean : (ArrayList<ModuleBean>) %s) {\n" +
                                "            if (moduleBean.isDefault()) {\n" +
                                "                return moduleBean;\n" +
                                "            }\n" +
                                "        }\n" +
                                "        return null;",
                        VAR_PARAMETER_IS_DEBUG,
                        VAR_MODULE_LIST,
                        VAR_CONTEXT, VAR_CONTEXT, Constants.ENVIRONMENT_SWITCHER_FILE_NAME.toLowerCase(), MODE_PRIVATE,
                        VAR_KEY_SELECT_MODULE_NAME,
                        VAR_MODULE_LIST,
                        VAR_MODULE_LIST))
                .build();
        environmentSwitcherClassBuilder.addMethod(getSelectModuleBeanMethod);


        MethodSpec getEnvironmentByAliasMethod = MethodSpec
                .methodBuilder(METHOD_NAME_GET_ENVIRONMENT_BY_ALIAS)
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC, Modifier.FINAL)
                .returns(EnvironmentBean.class)
                .addParameter(CONTEXT_TYPE_NAME, VAR_CONTEXT)
                .addParameter(boolean.class, VAR_PARAMETER_IS_DEBUG)
                .addParameter(String.class, VAR_PARAMETER_ALIAS)
                .addCode(String.format("        ModuleBean moduleBean = getSelectModule(%s, %s);\n" +
                                "        if (moduleBean == null) {\n" +
                                "            return null;\n" +
                                "        }\n" +
                                "        EnvironmentBean environmentBean = null;\n" +
                                "        ArrayList<EnvironmentBean> list = (ArrayList<EnvironmentBean>)moduleBean.getEnvironments();\n" +
                                "        if (list != null) {\n" +
                                "            for (EnvironmentBean bean : list) {\n" +
                                "                if (android.text.TextUtils.equals(bean.getAlias(), %s)) {\n" +
                                "                    environmentBean = bean;\n" +
                                "                    break;\n" +
                                "                }\n" +
                                "            }\n" +
                                "        }\n" +
                                "        return environmentBean;"
                        , VAR_CONTEXT, VAR_PARAMETER_IS_DEBUG
                        , VAR_PARAMETER_ALIAS))
                .build();
        environmentSwitcherClassBuilder.addMethod(getEnvironmentByAliasMethod);

        CodeBlock.Builder staticCodeBlockBuilder = CodeBlock.builder();
        /**
         *  对Module列表进行循环遍历
         */
        for (Element element : elements) {
            Module moduleAnnotation = element.getAnnotation(Module.class);
            if (moduleAnnotation == null) {
                continue;
            }
            /**
             * 获取Module的类名
             */
            String moduleName = element.getSimpleName().toString();
            String moduleUpperCaseName = moduleName.toUpperCase();
            String moduleLowerCaseName = moduleName.toLowerCase();
            String moduleAliasName = moduleAnnotation.alias();
            boolean isRelease = moduleAnnotation.isRelease();
            boolean isDefault = moduleAnnotation.isDefault();
            /**
             * 添加属性
             *    public static final ModuleBean MODULE_ONLINE = new ModuleBean("Online", "");
             *    public static final ModuleBean MODULE_TEST = new ModuleBean("Test", "");
             *    public static final ModuleBean MODULE_DEVELOP = new ModuleBean("Develop", "");
             */
            FieldSpec moduleXXField = FieldSpec
                    .builder(ModuleBean.class, String.format("%s%s", VAR_MODULE_PREFIX, moduleUpperCaseName))
                    .addModifiers(Modifier.PUBLIC, Modifier.STATIC, Modifier.FINAL)
                    .initializer(String.format("new %s(\"%s\", \"%s\", %s, %s)", ModuleBean.class.getSimpleName(), moduleName, moduleAliasName, isRelease, isDefault))
                    .build();
            environmentSwitcherClassBuilder.addField(moduleXXField);

            /**
             *  static {
             * MODULE_LIST.add(MODULE_ONLINE);
             * MODULE_LIST.add(MODULE_TEST);
             * MODULE_LIST.add(MODULE_DEVELOP)
             * }
             */
            staticCodeBlockBuilder
                    .add("\n")
                    .addStatement(String.format("%s.add(%s%s)", VAR_MODULE_LIST, VAR_MODULE_PREFIX, moduleUpperCaseName));

            //获取Module所有属性的集合
            List<? extends Element> allMembers = processingEnv.getElementUtils().getAllMembers((TypeElement) element);

            for (Element member : allMembers) {
                //获取属性当中的Environment属性
                Environment environmentAnnotation = member.getAnnotation(Environment.class);
                if (environmentAnnotation == null) {
                    continue;
                }
                //获取环境名称
                String environmentName = member.getSimpleName().toString();
                String environmentUpperCaseName = environmentName.toUpperCase();
                //环境url
                String url = environmentAnnotation.url();
                //环境别称
                String alias = environmentAnnotation.alias();

                //添加属性
                FieldSpec environmentField = generateEnvironmentField(moduleAnnotation, moduleUpperCaseName, environmentName, environmentUpperCaseName, url, alias);

                environmentSwitcherClassBuilder.addField(environmentField);
//            为MODULE_ONLINE添加属性
//            MODULE_ONLINE.getEnvironments().add(ONLINE_APISERVERURL_ENVIRONMENT);
//            MODULE_ONLINE.getEnvironments().add(ONLINE_H5SERVERURL_ENVIRONMENT);
//            MODULE_ONLINE.getEnvironments().add(ONLINE_FILESERVERURL_ENVIRONMENT);
//
//            MODULE_TEST.getEnvironments().add(TEST_APISERVERURL_ENVIRONMENT);
//            MODULE_TEST.getEnvironments().add(TEST_H5SERVERURL_ENVIRONMENT);
//            MODULE_TEST.getEnvironments().add(TEST_FILESERVERURL_ENVIRONMENT);
//
//            MODULE_DEVELOP.getEnvironments().add(DEVELOP_APISERVERURL_ENVIRONMENT);
//            MODULE_DEVELOP.getEnvironments().add(DEVELOP_H5SERVERURL_ENVIRONMENT);
//            MODULE_DEVELOP.getEnvironments().add(DEVELOP_FILESERVERURL_ENVIRONMENT);
                staticCodeBlockBuilder
                        .addStatement(String.format("%s%s.getEnvironments().add(%s)", VAR_MODULE_PREFIX, moduleUpperCaseName, String.format("%s_%s%s", moduleUpperCaseName, environmentUpperCaseName, VAR_DEFAULT_ENVIRONMENT_SUFFIX)));
            }

//            environmentSwitcherClassBuilder.addField(defaultXXEnvironmentFiledBuilder.build()).build();
        }

//        static {
//            MODULE_LIST.add(MODULE_ONLINE);
//            MODULE_ONLINE.getEnvironments().add(ONLINE_APISERVERURL_ENVIRONMENT);
//            MODULE_ONLINE.getEnvironments().add(ONLINE_H5SERVERURL_ENVIRONMENT);
//            MODULE_ONLINE.getEnvironments().add(ONLINE_FILESERVERURL_ENVIRONMENT);
//
//            MODULE_LIST.add(MODULE_TEST);
//            MODULE_TEST.getEnvironments().add(TEST_APISERVERURL_ENVIRONMENT);
//            MODULE_TEST.getEnvironments().add(TEST_H5SERVERURL_ENVIRONMENT);
//            MODULE_TEST.getEnvironments().add(TEST_FILESERVERURL_ENVIRONMENT);
//
//            MODULE_LIST.add(MODULE_DEVELOP);
//            MODULE_DEVELOP.getEnvironments().add(DEVELOP_APISERVERURL_ENVIRONMENT);
//            MODULE_DEVELOP.getEnvironments().add(DEVELOP_H5SERVERURL_ENVIRONMENT);
//            MODULE_DEVELOP.getEnvironments().add(DEVELOP_FILESERVERURL_ENVIRONMENT);
//        }
        environmentSwitcherClassBuilder.addStaticBlock(staticCodeBlockBuilder.build());

        JavaFile switchEnvironmentJavaFile = JavaFile.builder(Constants.PACKAGE_NAME, environmentSwitcherClassBuilder.build()).build();

        try {
            //把这个类写到具体的文件中
            switchEnvironmentJavaFile.writeTo(processingEnv.getFiler());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }

    protected FieldSpec generateEnvironmentField(Module moduleAnnotation, String moduleUpperCaseName,
                                                 String environmentName,
                                                 String environmentUpperCaseName,
                                                 String url,
                                                 String alias) {
        return FieldSpec
                .builder(EnvironmentBean.class,
                        String.format("%s_%s%s", moduleUpperCaseName, environmentUpperCaseName, VAR_DEFAULT_ENVIRONMENT_SUFFIX),
                        Modifier.PRIVATE, Modifier.STATIC, Modifier.FINAL)
                .initializer(String.format("new %s(\"%s\", \"%s\", \"%s\", %s%s)",
                        EnvironmentBean.class.getSimpleName(), environmentName, url, alias, VAR_MODULE_PREFIX, moduleUpperCaseName))
                .build();
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return Collections.singleton(Module.class.getCanonicalName());
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.RELEASE_7;
    }


}