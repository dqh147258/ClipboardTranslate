package com.yxf.clipboardtranslate;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by jk on 2017/2/7.
 */
public class Data {
    public static void setData(String key,String value,Context context) {
        SharedPreferences.Editor editor = context.getSharedPreferences("data", Context.MODE_PRIVATE).edit();
        editor.putString(key,value);
        editor.commit();
    }

    public static String getData(String key,Context context) {
        SharedPreferences pref = context.getSharedPreferences("data", Context.MODE_PRIVATE);
        return pref.getString(key, "");
    }
    public static boolean isChineseChar(String str){
        boolean temp = false;
        Pattern p= Pattern.compile("[\u4e00-\u9fa5]");
        Matcher m=p.matcher(str);
        if(m.find()){
            temp =  true;
        }
        return temp;
    }
    public static boolean isEnglishString(String string) {
        byte[] bytes=string.getBytes();
        for (byte b : bytes) {
            if((int)b>127||(int)b<1){
                return false;
            }
        }
        return true;
    }
    private static String url,result;
    public static String translate(String string) {
        result="";
        try {
            string = URLEncoder.encode(string, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        url="http://fanyi.youdao.com/openapi.do?keyfrom=comyxftranslate&key=1056784392&type=data&doctype=json&version=1.1&q="+string;
        Thread thread=new Thread(new Runnable() {
            @Override
            public void run() {
                result = HttpPost.HttpGet(url);
            }
        });
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return result;
    }
}
