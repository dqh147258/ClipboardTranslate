package com.yxf.clipboardtranslate;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.widget.CompoundButton;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener {
    private SwitchCompat serviceSwitch =null;//服务开关
    private SwitchCompat notificationSwitch=null;//通知开关
    private SwitchCompat chineseSwitch=null;//翻译中文开关
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        serviceSwitch = (SwitchCompat) findViewById(R.id.switch_compat);
        notificationSwitch = (SwitchCompat) findViewById(R.id.useing_notification);
        chineseSwitch = (SwitchCompat) findViewById(R.id.useing_chinese);
        initSwitch("serviceSwitch",serviceSwitch);
        initSwitch("notificationSwitch", notificationSwitch);
        initSwitch("chineseSwitch",chineseSwitch);
        serviceSwitch.setOnCheckedChangeListener(this);
        notificationSwitch.setOnCheckedChangeListener(this);
        chineseSwitch.setOnCheckedChangeListener(this);

        //判断是否启动服务
        if (!Data.getData("serviceSwitch", getApplicationContext()).equals("false")) {
            Intent intent = new Intent(getApplicationContext(), TranslateService.class);
            startService(intent);
        }
    }

    private void initSwitch(String key,SwitchCompat compat) {
        String value= Data.getData(key,this);
        if (value == null || value.length()<1) {
            if(compat.isChecked()){
                Data.setData(key, "true", this);
            }else{
                Data.setData(key,"false",this);
            }
        }else if (value.equals("true")) {//初始化服务开关设置
            compat.setChecked(true);
        }else if(value.equals("false")){
            compat.setChecked(false);
        }
    }
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.switch_compat:
                Intent intent = new Intent(getApplicationContext(), TranslateService.class);
                if (isChecked) {
                    Data.setData("serviceSwitch", "true", getApplicationContext());
                    //启动服务
                    startService(intent);
                    Toast.makeText(getApplicationContext(), "服务启动", Toast.LENGTH_SHORT).show();
                } else {
                    Data.setData("serviceSwitch", "false", getApplicationContext());
                    //关闭服务
                    stopService(intent);
                    Toast.makeText(getApplicationContext(), "服务关闭", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.useing_notification:
                if (isChecked) {
                    Data.setData("notificationSwitch","true",getApplicationContext());
                }else{
                    Data.setData("notificationSwitch","false",getApplicationContext());
                }
                break;
            case R.id.useing_chinese:
                if (isChecked) {
                    Data.setData("chineseSwitch","true",getApplicationContext());
                }else{
                    Data.setData("chineseSwitch","false",getApplicationContext());
                }
                break;
        }
    }
}
