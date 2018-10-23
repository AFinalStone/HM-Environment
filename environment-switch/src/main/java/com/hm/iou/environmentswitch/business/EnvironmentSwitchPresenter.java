package com.hm.iou.environmentswitch.business;

import android.content.Context;
import android.support.annotation.NonNull;

import com.hm.iou.base.mvp.MvpActivityPresenter;
import com.hm.iou.environmentswitch.Constants;
import com.hm.iou.environmentswitch.bean.ModuleBean;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

/**
 * Created by syl on 2018/10/22.
 */

public class EnvironmentSwitchPresenter extends MvpActivityPresenter<EnvironmentContract.View> implements EnvironmentContract.Presenter {


    public EnvironmentSwitchPresenter(@NonNull Context context, @NonNull EnvironmentContract.View view) {
        super(context, view);
    }

    @Override
    public void init() {
        try {
            Class<?> environmentSwitcherClass = Class.forName(Constants.PACKAGE_NAME + "." + Constants.ENVIRONMENT_SWITCHER_FILE_NAME);
            Method getEnvironmentConfigMethod = environmentSwitcherClass.getMethod(Constants.METHOD_NAME_GET_MODULE_LIST);
            ArrayList<ModuleBean> modules = (ArrayList<ModuleBean>) getEnvironmentConfigMethod.invoke(environmentSwitcherClass.newInstance());
            mView.showList(modules);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setSelectModule(ModuleBean selectModule) {
        try {
            Class<?> environmentSwitcher = Class.forName(Constants.PACKAGE_NAME + "." + Constants.ENVIRONMENT_SWITCHER_FILE_NAME);
            Method method = environmentSwitcher.getMethod(Constants.METHOD_NAME_SET_SELECT_MODULE, Context.class, ModuleBean.class);
            method.invoke(environmentSwitcher.newInstance(), mContext, selectModule);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
