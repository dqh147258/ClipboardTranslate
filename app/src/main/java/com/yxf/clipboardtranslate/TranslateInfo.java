package com.yxf.clipboardtranslate;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jk on 2017/2/7.
 */
public class TranslateInfo implements Serializable{
    private String translation="";//翻译
    private boolean isBasicEeist=false;//基本解释是否存在
    private boolean isWebExist=false;//网络释义是否存在
    private String usPhonetic="";//美国发音
    private String ukPhoetic="";//英国发音
    private String explains="";//解释
    private int errorCode=0;//错误码
    private String webExplain="";//网络释义

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    private String title="";//查询的主要内容
    public TranslateInfo(String json){
        if (json.indexOf("basic") > -1) {
            isBasicEeist=true;
        }
        if (json.indexOf("web") > -1) {
            isWebExist=true;
        }
        try {
            JSONObject js = new JSONObject(json);
            translation = getJSONArrayJoin(js, "translation", " ");
            errorCode = getJSONKEY(js, "errorCode");
            title = getJSONKey(js, "query");
            if (isBasicEeist) {
                JSONObject basic = js.getJSONObject("basic");
                usPhonetic = getJSONKey(basic, "us-phonetic");
                ukPhoetic = getJSONKey(basic, "uk-phonetic");
                explains = getJSONArrayJoin(basic, "explains",".\n");
            }
            if (isWebExist) {
                JSONArray array = js.getJSONArray("web");
                for(int i=0;i<array.length();i++) {
                    JSONObject jb = (JSONObject) array.get(i);
                    String key = getJSONKey(jb,"key");
                    String value = getJSONArrayJoin(jb, "value", " \\ ");
                    webExplain=webExplain+key+"; "+value+"\n";
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public boolean isBasicEeist() {
        return isBasicEeist;
    }

    public void setBasicEeist(boolean basicEeist) {
        isBasicEeist = basicEeist;
    }

    public String getTranslation() {
        return translation;
    }

    public void setTranslation(String translation) {
        this.translation = translation;
    }

    public boolean isWebExist() {
        return isWebExist;
    }

    public void setWebExist(boolean webExist) {
        isWebExist = webExist;
    }

    public String getUsPhonetic() {
        return usPhonetic;
    }

    public void setUsPhonetic(String usPhonetic) {
        this.usPhonetic = usPhonetic;
    }

    public String getUkPhoetic() {
        return ukPhoetic;
    }

    public void setUkPhoetic(String ukPhoetic) {
        this.ukPhoetic = ukPhoetic;
    }

    public String getExplains() {
        return explains;
    }

    public void setExplains(String explains) {
        this.explains = explains;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getWebExplain() {
        return webExplain;
    }

    public void setWebExplain(String webExplain) {
        this.webExplain = webExplain;
    }

    public static String getJSONKey(JSONObject js,String key) {
        if(js.toString().indexOf(key)>-1){
            try {
                return js.getString(key);
            } catch (JSONException e) {
                // TODO 自动生成的 catch 块
                e.printStackTrace();
            }
        }else{

        }
        return "";
    }

    public static int getJSONKEY(JSONObject js, String key) {
        if(js.toString().indexOf(key)>-1){
            try {
                return js.getInt(key);
            } catch (JSONException e) {
                // TODO 自动生成的 catch 块
                e.printStackTrace();
            }
        }else{

        }
        return 0;
    }

    public static List<String> getJSONArray(JSONObject js, String key) {
        List<String> list = new ArrayList<String>();
        if(js.toString().indexOf(key)>-1){
            try {
                JSONArray array = js.getJSONArray(key);
                for (int i=0;i<array.length();i++) {
                    String str = (String) array.get(i);
                    list.add(str);
                }
            } catch (JSONException e) {
                // TODO 自动生成的 catch 块
                e.printStackTrace();
            }
        }
        return list;
    }

    public static String getJSONArrayJoin(JSONObject js, String key,String sp) {
        List<String> list = getJSONArray(js, key);
        String result="";
        for (String str : list) {
            result = result + str + sp;
        }
        return result;
    }

}
