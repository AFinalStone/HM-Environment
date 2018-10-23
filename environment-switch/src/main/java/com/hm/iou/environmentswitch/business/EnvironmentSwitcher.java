//package com.hm.iou.environmentswitch.business;
//
//import android.content.Context;
//import com.xiaomai.environmentswitcher.bean.EnvironmentBean;
//import com.xiaomai.environmentswitcher.bean.ModuleBean;
//import com.xiaomai.environmentswitcher.listener.OnEnvironmentChangeListener;
//import java.lang.String;
//import java.util.ArrayList;
//
//public final class EnvironmentSwitcher {
//  private static final ArrayList ON_ENVIRONMENT_CHANGE_LISTENERS = new ArrayList<OnEnvironmentChangeListener>();
//
//  private static final ArrayList MODULE_LIST = new ArrayList<ModuleBean>();
//
//  public static final ModuleBean MODULE_APISERVER = new ModuleBean("APIServer", "");
//
//  private static EnvironmentBean sCurrentAPIServerEnvironment;
//
//  public static final EnvironmentBean APISERVER_ONLINE_ENVIRONMENT = new EnvironmentBean("online", "https://api.54jietiao.com", "正式", MODULE_APISERVER);
//
//  public static final EnvironmentBean APISERVER_TEST_ENVIRONMENT = new EnvironmentBean("test", "https://re.54jietiao.com", "", MODULE_APISERVER);
//
//  public static final EnvironmentBean APISERVER_DEVELOP_ENVIRONMENT = new EnvironmentBean("develop", "http://192.168.1.217", "", MODULE_APISERVER);
//
//  private static final EnvironmentBean DEFAULT_APISERVER_ENVIRONMENT = APISERVER_ONLINE_ENVIRONMENT;
//
//  public static final ModuleBean MODULE_FILESERVER = new ModuleBean("FileServer", "");
//
//  private static EnvironmentBean sCurrentFileServerEnvironment;
//
//  public static final EnvironmentBean FILESERVER_ONLINE_ENVIRONMENT = new EnvironmentBean("online", "https://upload.54jietiao.com", "正式", MODULE_FILESERVER);
//
//  public static final EnvironmentBean FILESERVER_TEST_ENVIRONMENT = new EnvironmentBean("test", "https://re.54jietiao.com", "", MODULE_FILESERVER);
//
//  public static final EnvironmentBean FILESERVER_DEVELOP_ENVIRONMENT = new EnvironmentBean("develop", "http://192.168.1.217", "", MODULE_FILESERVER);
//
//  private static final EnvironmentBean DEFAULT_FILESERVER_ENVIRONMENT = FILESERVER_ONLINE_ENVIRONMENT;
//
//  public static final ModuleBean MODULE_H5SERVER = new ModuleBean("H5Server", "");
//
//  private static EnvironmentBean sCurrentH5ServerEnvironment;
//
//  public static final EnvironmentBean H5SERVER_ONLINE_ENVIRONMENT = new EnvironmentBean("online", "https://h5.54jietiao.com", "正式", MODULE_H5SERVER);
//
//  public static final EnvironmentBean H5SERVER_TEST_ENVIRONMENT = new EnvironmentBean("test", "https://re.54jietiao.com", "", MODULE_H5SERVER);
//
//  public static final EnvironmentBean H5SERVER_DEVELOP_ENVIRONMENT = new EnvironmentBean("develop", "http://192.168.1.217", "", MODULE_H5SERVER);
//
//  private static final EnvironmentBean DEFAULT_H5SERVER_ENVIRONMENT = H5SERVER_ONLINE_ENVIRONMENT;
//
//  static {
//
//    MODULE_LIST.add(MODULE_APISERVER);
//    MODULE_APISERVER.getEnvironments().add(APISERVER_ONLINE_ENVIRONMENT);
//    MODULE_APISERVER.getEnvironments().add(APISERVER_TEST_ENVIRONMENT);
//    MODULE_APISERVER.getEnvironments().add(APISERVER_DEVELOP_ENVIRONMENT);
//
//    MODULE_LIST.add(MODULE_FILESERVER);
//    MODULE_FILESERVER.getEnvironments().add(FILESERVER_ONLINE_ENVIRONMENT);
//    MODULE_FILESERVER.getEnvironments().add(FILESERVER_TEST_ENVIRONMENT);
//    MODULE_FILESERVER.getEnvironments().add(FILESERVER_DEVELOP_ENVIRONMENT);
//
//    MODULE_LIST.add(MODULE_H5SERVER);
//    MODULE_H5SERVER.getEnvironments().add(H5SERVER_ONLINE_ENVIRONMENT);
//    MODULE_H5SERVER.getEnvironments().add(H5SERVER_TEST_ENVIRONMENT);
//    MODULE_H5SERVER.getEnvironments().add(H5SERVER_DEVELOP_ENVIRONMENT);
//  }
//
//  public static void addOnEnvironmentChangeListener(OnEnvironmentChangeListener onEnvironmentChangeListener) {
//    ON_ENVIRONMENT_CHANGE_LISTENERS.add(onEnvironmentChangeListener);
//  }
//
//  public static void removeOnEnvironmentChangeListener(OnEnvironmentChangeListener onEnvironmentChangeListener) {
//    ON_ENVIRONMENT_CHANGE_LISTENERS.remove(onEnvironmentChangeListener);
//  }
//
//  public static void removeAllOnEnvironmentChangeListener() {
//    ON_ENVIRONMENT_CHANGE_LISTENERS.clear();
//  }
//
//  private static void onEnvironmentChange(ModuleBean module, EnvironmentBean oldEnvironment,
//      EnvironmentBean newEnvironment) {
//    for (Object onEnvironmentChangeListener : ON_ENVIRONMENT_CHANGE_LISTENERS) {
//       if (onEnvironmentChangeListener instanceof OnEnvironmentChangeListener) {
//           ((OnEnvironmentChangeListener) onEnvironmentChangeListener).onEnvironmentChanged(module, oldEnvironment, newEnvironment);
//       }
//    }
//  }
//
//  public static final String getAPIServerEnvironment(Context context, boolean isDebug) {
//    return getAPIServerEnvironmentBean(context, isDebug).getUrl();
//  }
//
//  public static final EnvironmentBean getAPIServerEnvironmentBean(Context context,
//      boolean isDebug) {
//    if (!isDebug) {
//        return DEFAULT_APISERVER_ENVIRONMENT;
//    }
//    if (sCurrentAPIServerEnvironment == null) {
//        android.content.SharedPreferences sharedPreferences = context.getSharedPreferences(context.getPackageName() + ".environmentswitcher", android.content.Context.MODE_PRIVATE);
//        String url = sharedPreferences.getString("apiserverEnvironmentUrl", DEFAULT_APISERVER_ENVIRONMENT.getUrl());
//        String environmentName = sharedPreferences.getString("apiserverEnvironmentName", DEFAULT_APISERVER_ENVIRONMENT.getName());
//        String appAlias = sharedPreferences.getString("apiserverEnvironmentAlias", DEFAULT_APISERVER_ENVIRONMENT.getAlias());
//        for (EnvironmentBean environmentBean : MODULE_APISERVER.getEnvironments()) {
//            if (android.text.TextUtils.equals(environmentBean.getUrl(), url)) {
//                sCurrentAPIServerEnvironment = environmentBean;
//                break;
//            }
//        }
//    }
//    return sCurrentAPIServerEnvironment;
//  }
//
//  public static final void setAPIServerEnvironment(Context context,
//      EnvironmentBean newEnvironment) {
//    context.getSharedPreferences(context.getPackageName() + ".environmentswitcher", android.content.Context.MODE_PRIVATE).edit()
//        .putString("apiserverEnvironmentUrl", newEnvironment.getUrl())
//        .putString("apiserverEnvironmentName", newEnvironment.getName())
//        .putString("apiserverEnvironmentAlias", newEnvironment.getAlias())
//        .apply();
//    if (!newEnvironment.equals(sCurrentAPIServerEnvironment)) {
//        EnvironmentBean oldEnvironment = sCurrentAPIServerEnvironment;
//        sCurrentAPIServerEnvironment = newEnvironment;
//        onEnvironmentChange(MODULE_APISERVER, oldEnvironment, newEnvironment);
//    }
//  }
//
//  public static final String getFileServerEnvironment(Context context, boolean isDebug) {
//    return getFileServerEnvironmentBean(context, isDebug).getUrl();
//  }
//
//  public static final EnvironmentBean getFileServerEnvironmentBean(Context context,
//      boolean isDebug) {
//    if (!isDebug) {
//        return DEFAULT_FILESERVER_ENVIRONMENT;
//    }
//    if (sCurrentFileServerEnvironment == null) {
//        android.content.SharedPreferences sharedPreferences = context.getSharedPreferences(context.getPackageName() + ".environmentswitcher", android.content.Context.MODE_PRIVATE);
//        String url = sharedPreferences.getString("fileserverEnvironmentUrl", DEFAULT_FILESERVER_ENVIRONMENT.getUrl());
//        String environmentName = sharedPreferences.getString("fileserverEnvironmentName", DEFAULT_FILESERVER_ENVIRONMENT.getName());
//        String appAlias = sharedPreferences.getString("fileserverEnvironmentAlias", DEFAULT_FILESERVER_ENVIRONMENT.getAlias());
//        for (EnvironmentBean environmentBean : MODULE_FILESERVER.getEnvironments()) {
//            if (android.text.TextUtils.equals(environmentBean.getUrl(), url)) {
//                sCurrentFileServerEnvironment = environmentBean;
//                break;
//            }
//        }
//    }
//    return sCurrentFileServerEnvironment;
//  }
//
//  public static final void setFileServerEnvironment(Context context,
//      EnvironmentBean newEnvironment) {
//    context.getSharedPreferences(context.getPackageName() + ".environmentswitcher", android.content.Context.MODE_PRIVATE).edit()
//        .putString("fileserverEnvironmentUrl", newEnvironment.getUrl())
//        .putString("fileserverEnvironmentName", newEnvironment.getName())
//        .putString("fileserverEnvironmentAlias", newEnvironment.getAlias())
//        .apply();
//    if (!newEnvironment.equals(sCurrentFileServerEnvironment)) {
//        EnvironmentBean oldEnvironment = sCurrentFileServerEnvironment;
//        sCurrentFileServerEnvironment = newEnvironment;
//        onEnvironmentChange(MODULE_FILESERVER, oldEnvironment, newEnvironment);
//    }
//  }
//
//  public static final String getH5ServerEnvironment(Context context, boolean isDebug) {
//    return getH5ServerEnvironmentBean(context, isDebug).getUrl();
//  }
//
//  public static final EnvironmentBean getH5ServerEnvironmentBean(Context context, boolean isDebug) {
//    if (!isDebug) {
//        return DEFAULT_H5SERVER_ENVIRONMENT;
//    }
//    if (sCurrentH5ServerEnvironment == null) {
//        android.content.SharedPreferences sharedPreferences = context.getSharedPreferences(context.getPackageName() + ".environmentswitcher", android.content.Context.MODE_PRIVATE);
//        String url = sharedPreferences.getString("h5serverEnvironmentUrl", DEFAULT_H5SERVER_ENVIRONMENT.getUrl());
//        String environmentName = sharedPreferences.getString("h5serverEnvironmentName", DEFAULT_H5SERVER_ENVIRONMENT.getName());
//        String appAlias = sharedPreferences.getString("h5serverEnvironmentAlias", DEFAULT_H5SERVER_ENVIRONMENT.getAlias());
//        for (EnvironmentBean environmentBean : MODULE_H5SERVER.getEnvironments()) {
//            if (android.text.TextUtils.equals(environmentBean.getUrl(), url)) {
//                sCurrentH5ServerEnvironment = environmentBean;
//                break;
//            }
//        }
//    }
//    return sCurrentH5ServerEnvironment;
//  }
//
//  public static final void setH5ServerEnvironment(Context context, EnvironmentBean newEnvironment) {
//    context.getSharedPreferences(context.getPackageName() + ".environmentswitcher", android.content.Context.MODE_PRIVATE).edit()
//        .putString("h5serverEnvironmentUrl", newEnvironment.getUrl())
//        .putString("h5serverEnvironmentName", newEnvironment.getName())
//        .putString("h5serverEnvironmentAlias", newEnvironment.getAlias())
//        .apply();
//    if (!newEnvironment.equals(sCurrentH5ServerEnvironment)) {
//        EnvironmentBean oldEnvironment = sCurrentH5ServerEnvironment;
//        sCurrentH5ServerEnvironment = newEnvironment;
//        onEnvironmentChange(MODULE_H5SERVER, oldEnvironment, newEnvironment);
//    }
//  }
//
//  public static ArrayList getModuleList() {
//    return MODULE_LIST;
//  }
//}
