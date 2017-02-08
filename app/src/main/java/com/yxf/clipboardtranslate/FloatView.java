package com.yxf.clipboardtranslate;

import android.content.Context;
import android.view.View;
import android.widget.RelativeLayout;

/**
 * Created by jk on 2017/2/8.
 */
public class FloatView extends RelativeLayout {
    public static int height,width;
    public FloatView(Context context) {
        super(context);
        View view = findViewById(R.id.float_translateview);
        height=view.getLayoutParams().height;
        width=view.getLayoutParams().width;
    }
}
