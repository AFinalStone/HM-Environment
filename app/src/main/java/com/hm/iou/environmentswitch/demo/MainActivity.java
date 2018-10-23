package com.hm.iou.environmentswitch.demo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.hm.iou.environmentswitch.business.view.EnvironmentSwitchActivity;
import com.hm.iou.environmentswitch.EnvironmentSwitcher;
import com.hm.iou.environmentswitcher.bean.EnvironmentBean;
import com.hm.iou.environmentswitcher.bean.ModuleBean;
import com.hm.iou.environmentswitcher.listener.OnEnvironmentChangeListener;
import com.hm.iou.logger.Logger;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button btnSwitch = findViewById(R.id.btn_switch);
        if (BuildConfig.DEBUG) {
            btnSwitch.setVisibility(View.VISIBLE);
            btnSwitch.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(MainActivity.this, EnvironmentSwitchActivity.class));
                }
            });
            EnvironmentSwitcher.addOnEnvironmentChangeListener(new OnEnvironmentChangeListener() {
                @Override
                public void onEnvironmentChanged(ModuleBean module, EnvironmentBean oldEnvironment, EnvironmentBean newEnvironment) {
                    Logger.d("module" + module.toString());
                    Logger.d("oldEnvironment" + oldEnvironment.toString());
                    Logger.d("newEnvironment" + newEnvironment.toString());
                }
            });
        }

    }
}
