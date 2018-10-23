package com.hm.iou.environmentswitch.business.view;


import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hm.iou.base.BaseActivity;
import com.hm.iou.environmentswitch.R;
import com.hm.iou.environmentswitch.bean.ModuleBean;
import com.hm.iou.environmentswitch.business.EnvironmentContract;
import com.hm.iou.environmentswitch.business.EnvironmentSwitchPresenter;

import java.util.List;

public class EnvironmentSwitchActivity extends BaseActivity<EnvironmentSwitchPresenter> implements EnvironmentContract.View {

    private RecyclerView mRecyclerView;
    private EnvironmentAdapter mAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.environment_switcher_activity;
    }

    @Override
    protected EnvironmentSwitchPresenter initPresenter() {
        return new EnvironmentSwitchPresenter(this, this);
    }

    @Override
    protected void initEventAndData(Bundle bundle) {
        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(mContext, LinearLayout.VERTICAL));
        mAdapter = new EnvironmentAdapter(mContext);
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                ModuleBean selectModule = mAdapter.getData().get(position);
                mAdapter.setSelectModuleName(selectModule.getName());
                mPresenter.setSelectModule(selectModule);
            }
        });
        mRecyclerView.setAdapter(mAdapter);
        mPresenter.init();
    }


    @Override
    public void showList(List<ModuleBean> list) {
        mAdapter.setNewData(list);
    }


}
