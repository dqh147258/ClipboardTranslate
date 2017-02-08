package com.yxf.clipboardtranslate;

import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Build;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by jk on 2017/2/8.
 */
public class FloatViewManager {
    private static Context context=TranslateService.getContext();
    private static WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
    private static View floatView;
    public static void createFloatView(TranslateInfo info) {
        if (manager != null && floatView != null) {
            manager.removeView(floatView);
        }
        floatView = View.inflate(context,R.layout.float_translateview,null);
        initInfo(info);
        /*FloatView view = new FloatView(context);
        int w=view.getLayoutParams().width;
        int h=view.getLayoutParams().height;*/
        int w = WindowManager.LayoutParams.WRAP_CONTENT;
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
        layoutParams.gravity = Gravity.CENTER;
        manager.addView(floatView, layoutParams);
    }

    private static void initInfo(TranslateInfo info) {
        TextView title = (TextView) floatView.findViewById(R.id.float_title);
        TextView ukPronounce = (TextView) floatView.findViewById(R.id.translate_uk);
        TextView usPronounce = (TextView) floatView.findViewById(R.id.translate_us);
        TextView basicExplains = (TextView) floatView.findViewById(R.id.basic_explains);
        TextView webExplains = (TextView) floatView.findViewById(R.id.web_explains);
        TextView translate = (TextView) floatView.findViewById(R.id.translate);
        Button closeButton= (Button) floatView.findViewById(R.id.close_button);
        title.setText(info.getTitle());
        ukPronounce.setText("英:" + info.getUkPhoetic());
        usPronounce.setText("美:" + info.getUsPhonetic());
        basicExplains.setText(info.getExplains());
        webExplains.setText(info.getWebExplain());
        translate.setText(info.getTranslation());
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                manager.removeView(floatView);
                floatView=null;
            }
        });
    }
}
