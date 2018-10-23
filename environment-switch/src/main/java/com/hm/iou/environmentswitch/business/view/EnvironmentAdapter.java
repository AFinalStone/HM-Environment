package com.hm.iou.environmentswitch.business.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hm.iou.environmentswitch.Constants;
import com.hm.iou.environmentswitch.R;
import com.hm.iou.environmentswitch.bean.EnvironmentBean;
import com.hm.iou.environmentswitch.bean.ModuleBean;

import java.lang.reflect.Method;
import java.util.List;

/**
 * Created by syl on 2018/10/22.
 */

public class EnvironmentAdapter extends BaseQuickAdapter<ModuleBean, BaseViewHolder> {

    private Context mContext;
    private String mSelectModuleName;

    public EnvironmentAdapter(@NonNull Context context) {
        super(R.layout.environment_switcher_item_environment);
        mContext = context;
        try {
            Class<?> environmentSwitcher = Class.forName(Constants.PACKAGE_NAME + "." + Constants.ENVIRONMENT_SWITCHER_FILE_NAME);
            Method method = environmentSwitcher.getMethod(Constants.METHOD_NAME_GET_SELECT_MODULE, Context.class, boolean.class);
            ModuleBean selectBean = (ModuleBean) method.invoke(environmentSwitcher.newInstance(), mContext, true);
            if (selectBean != null) {
                mSelectModuleName = selectBean.getName();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void convert(BaseViewHolder helper, ModuleBean item) {
        //环境类型名称和是否选中
        helper.setText(R.id.tv_environmentType, item.getName());
        helper.setVisible(R.id.iv_mark, TextUtils.equals(item.getName(), mSelectModuleName));

        //具体的url信息
        LinearLayout llUrlTypeList = helper.getView(R.id.ll_urlTypeList);
        llUrlTypeList.removeAllViews();
        List<EnvironmentBean> list = item.getEnvironments();
        if (list == null) {
            return;
        }
        for (EnvironmentBean bean : list) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.environment_switcher_item_url, llUrlTypeList, false);
            ((TextView) view.findViewById(R.id.tv_urlType)).setText(bean.getName());
            ((TextView) view.findViewById(R.id.tv_url)).setText(bean.getUrl());
            llUrlTypeList.addView(view);
        }
    }

    public void setSelectModuleName(String moduleName) {
        mSelectModuleName = moduleName;
        notifyDataSetChanged();
    }
}
