package com.qiyei.sdk.dc.impl;

import android.content.Context;
import android.text.TextUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Email: 1273482124@qq.com
 * Created by qiyei2015 on 2017/8/22.
 * Version: 1.0
 * Description: 内存数据
 */
public class MemoryDataBuffer implements IDataBuffer {

    /**
     * 保存数据的Map
     */
    private Map<String,String> mDataMap;

    /**
     * 同一个包下可以引用
     * @param context
     */
    MemoryDataBuffer(Context context){
        init();
    }

    @Override
    public void init() {
        mDataMap = new HashMap<>();
    }

    @Override
    public String getValue(String key) {
        return mDataMap.get(key);
    }

    @Override
    public boolean setValue(String key, String value) {
        return !TextUtils.isEmpty(mDataMap.put(key,value));
    }
}