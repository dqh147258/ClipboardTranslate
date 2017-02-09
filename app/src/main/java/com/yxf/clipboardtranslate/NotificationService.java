package com.yxf.clipboardtranslate;

import android.app.IntentService;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by jk on 2017/2/9.
 */
public class NotificationService extends Service {
    public static final String TAG="NotificationService";

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        onHandleIntent(intent);
        return super.onStartCommand(intent, flags, startId);
    }

    protected void onHandleIntent(Intent intent) {
        TranslateInfo notificationInfo = (TranslateInfo) intent.getSerializableExtra("info");
        String title=notificationInfo.getTitle();
        String action = intent.getStringExtra("action");
        if (action != null && action.length() > 0 && action.equals("showFloatView") && notificationInfo != null) {
            FloatViewManager.createFloatView(notificationInfo);
            Log.d(TAG, "NotificationService");
        }
    }
}
