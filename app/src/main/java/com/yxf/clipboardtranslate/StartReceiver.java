package com.yxf.clipboardtranslate;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

/**
 * Created by jk on 2017/2/8.
 */
public class StartReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (!Data.getData("serviceSwitch", context).equals("false")) {
            Intent i = new Intent(context, TranslateService.class);
            context.startService(i);
        }
    }
}
