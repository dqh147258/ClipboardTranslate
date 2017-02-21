package com.yxf.clipboardtranslate;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.widget.Toast;

/**
 * Created by jk on 2017/2/8.
 */
public class StartReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (!TranslateService.isAlarmOn(context)) {
            TranslateService.setServiceAlarm(context, true);
        }
    }
}
