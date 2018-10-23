package com.hm.iou.environmentswitch.business;

import com.hm.iou.base.mvp.BaseContract;
import com.hm.iou.environmentswitch.bean.ModuleBean;

import java.util.List;

/**
 * Created by syl on 2018/10/22.
 */

public class EnvironmentContract {

    public interface View extends BaseContract.BaseView {

        void showList(List<ModuleBean> list);
    }

    public interface Presenter extends BaseContract.BasePresenter {
        /**
         * 初始化数据
         */
        void init();

        /**
         * 设置当前选中的环境类型
         */
        void setSelectModule(ModuleBean selectModule);
    }
}
