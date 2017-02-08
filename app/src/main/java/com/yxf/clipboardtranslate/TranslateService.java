package com.yxf.clipboardtranslate;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by jk on 2017/2/7.
 */
public class TranslateService extends Service {
    private Handler mHandler = new Handler();
    public static final String TAG = "TranslateService";
    private ClipboardManager clipboardManager = null;
    private WindowManager windowManager = null;
    private static Context context = null;
    private Notification notification=null;

    public String action="";//任务指令
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();//获取上下文对象
        windowManager = (WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        clipboardManager.addPrimaryClipChangedListener(new ClipboardManager.OnPrimaryClipChangedListener() {
            @Override
            public void onPrimaryClipChanged() {
                long time=System.currentTimeMillis();
                Log.d(TAG,time+"");
                String clip = clipboardManager.getText().toString();
                if (Data.isChineseChar(clip)) {
                    if (Data.getData("chineseSwitch", getApplicationContext()).equals("true")) {
                        //翻译中文
                        showTranslateView(clip);
                    }
                } else if (Data.isEnglishString(clip)) {
                    //翻译英文
                    showTranslateView(clip);
                }
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), clipboardManager.getText(), Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });

        showMessage("Service first start");

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(action!=null&&action.length()>0&&action.equals("showFloatView")&&info!=null){
            FloatViewManager.createFloatView(info);
            action="";
        }

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        showMessage("Service destroy");
    }

    private TranslateInfo info = null;//翻译返回信息对象
    private View view;//小悬浮窗对象
    private String showTranslateView(final String clip) {
        String res = "";
        if (!Data.getData("notificationSwitch", getApplicationContext()).equals("false")) {
            //发送notification
            info = new TranslateInfo(Data.translate(clip));
            //发送通知,并设置点击通知启动服务
            Intent intent = new Intent(getApplicationContext(), TranslateService.class);
            action="showFloatView";//显示翻译弹窗
            Log.d(TAG,info.getTitle());
            PendingIntent pi = PendingIntent.getService(getContext(), 0, intent, 0);
            notification = new Notification.Builder(getApplicationContext())
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setTicker(info.getTitle() + " -> " + info.getTranslation())
                    .setContentTitle(info.getTitle())
                    .setContentText("result -> "+info.getTranslation())
                    .setContentIntent(pi)
                    .getNotification();
            notification.flags |=Notification.FLAG_AUTO_CANCEL;
            NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            manager.notify(1,notification);
        }
        view = View.inflate(getApplicationContext(), R.layout.float_topview, null);
        TextView textView = (TextView) view.findViewById(R.id.float_topview_text);
        ImageView imageView = (ImageView) view.findViewById(R.id.float_topview_search);
        textView.setText(clip);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (windowManager != null && view != null) {
                    windowManager.removeView(view);
                    view=null;
                }
                if (info == null) {
                    info = new TranslateInfo(Data.translate(clip));
                }
                FloatViewManager.createFloatView(info);
            }
        });
        int w = WindowManager.LayoutParams.MATCH_PARENT;
        int h = WindowManager.LayoutParams.WRAP_CONTENT;

        int flags = 0;
        int type = 0;
        //api版本大于19的时候 TYPE_TOAST用这个参数 可以绕过绝大多数对悬浮窗权限的限制，比如miui
        //在小于19的时候 其实也是可以绕过的，只不过小于19你绕过了以后 点击事件就无效了 所以小于19的时候
        //还是用TYPE_PHONE
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            type = WindowManager.LayoutParams.TYPE_TOAST;
        } else {
            type = WindowManager.LayoutParams.TYPE_PHONE;
        }

        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams(w, h, type, flags, PixelFormat.TRANSLUCENT);
        layoutParams.gravity = Gravity.TOP;
        windowManager.addView(view, layoutParams);
        try {
            Thread.sleep(1200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (view != null && windowManager != null) {
                        windowManager.removeView(view);
                }
            }
        }).start();
        return res;
    }

    public static Context getContext() {
        return context;
    }

    public void showMessage(final String string) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(context,string,Toast.LENGTH_SHORT).show();
            }
        });
    }
}
