package com.qiyei.sdk.dc.impl;



import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;

import com.qiyei.sdk.common.RuntimeEnv;
import com.qiyei.sdk.dc.DCConstant;
import com.qiyei.sdk.log.LogUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Email: 1273482124@qq.com
 * Created by qiyei2015 on 2017/8/20.
 * Version: 1.0
 * Description: 数据中心
 */
public class DataCenter{

    /**
     * 存储观察者数据
     */
    private List<IDataCenterObserver> mCenterObservers = new ArrayList<>();

    private Map<String,IDataBuffer> mBufferMap = new HashMap<>();

    /**
     * 数据buffer
     */
    private IDataBuffer mDataBuffer;

    /**
     * 内部类方式构造单例
     */
    private static class SingleHolder{
        static final DataCenter sInstance = new DataCenter(RuntimeEnv.appContext);
    }

    /**
     * 构造方法私有化
     */
    private DataCenter(Context context){
        IDataBuffer memoryBuffer = new MemoryDataBuffer(context);
        IDataBuffer spBuffer = new SPDataBuffer(context);
        IDataBuffer sqlBuffer = new SQLDataBuffer(context);

        mBufferMap.put(DCConstant.MEM_DATA,memoryBuffer);
        mBufferMap.put(DCConstant.SP_DATA,spBuffer);
        mBufferMap.put(DCConstant.SQL_DATA,sqlBuffer);
    }

    /**
     * 内部类方式获取单例
     * @return
     */
    public static DataCenter getInstance(){
        return DataCenter.SingleHolder.sInstance;
    }

    /**
     * 注册DataCenter数据观察者
     * @param observer
     */
    public void registerDataObserver(IDataCenterObserver observer){
        if (!mCenterObservers.contains(observer)){
            mCenterObservers.add(observer);
        }
    }

    /**
     * 取消观察者注册
     * @param observer
     */
    public void unregisterDataObserver(IDataCenterObserver observer){
        if (mCenterObservers.contains(observer)){
            mCenterObservers.remove(observer);
        }
    }

    /**
     * 根据uri返回相应的DataBuffer
     * @param pathUri
     * @return
     */
    private IDataBuffer getDataBuffer(String pathUri){
        if (TextUtils.isEmpty(pathUri)){
            return null;
        }
        IDataBuffer dataBuffer = null;

        //根据uri的path来解析是使用的那个type
        Uri uri = Uri.parse(pathUri);
        List<String> paths = uri.getPathSegments();
        if (paths.size() > 0){
            dataBuffer =  mBufferMap.get(paths.get(0));
        }

        //如果没找到，就默认保存为MEM类型数据
        if (dataBuffer == null){
            dataBuffer = mBufferMap.get(DCConstant.MEM_DATA);
        }
        return dataBuffer;
    }

    /**
     * 存储String类型数据
     * @param uri
     * @param value
     */
    public void setValue(String uri,String value){
        LogUtil.i(DCConstant.TAG,"setValue,uri --> " + uri + " value --> " + value);

        mDataBuffer = getDataBuffer(uri);

        mDataBuffer.setValue(uri,value);
        //更新数据
        Set<String> uris = new HashSet<>();
        uris.add(uri);
        for (IDataCenterObserver observer : mCenterObservers){
            observer.dataUpdate(uris);
        }
    }

    /**
     * 存储String类型数据
     * @param uri
     */
    public String getValue(String uri){
        LogUtil.i(DCConstant.TAG,"getValue,uri --> " + uri);
        mDataBuffer = getDataBuffer(uri);
        return mDataBuffer.getValue(uri);
    }

}