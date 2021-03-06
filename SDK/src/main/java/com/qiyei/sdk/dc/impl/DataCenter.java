package com.qiyei.sdk.dc.impl;



import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;

import com.qiyei.sdk.common.RuntimeEnv;
import com.qiyei.sdk.log.LogManager;

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
public final class DataCenter{

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
        IDataBuffer memoryBuffer = new MEMDataBuffer(context);
        IDataBuffer spBuffer = new SPDataBuffer(context);
        IDataBuffer sqlBuffer = new SQLDataBuffer(context);

        mBufferMap.put(DC.MEM_DATA,memoryBuffer);
        mBufferMap.put(DC.STORE_DATA,spBuffer);
        mBufferMap.put(DC.SQL_DATA,sqlBuffer);
        // TODO: 2018/10/12 后期需要保存到用户登录后的数据 ,现在默认用户名为admin
        IDataBuffer userBuffer = new SPDataBuffer(context,"admin");
        mBufferMap.put(DC.USER_DATA,userBuffer);
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
            dataBuffer = mBufferMap.get(DC.MEM_DATA);
        }
        return dataBuffer;
    }


    /**
     * 保存值
     * @param uri
     * @param value
     */
    public void setStringValue(String uri,String value){
        setValue(uri,value);
    }

    /**
     * 获取值
     * @return
     */
    public String getStringValue(String uri){
        return getValue(uri);
    }

    /**
     * 删除指定uri的数据
     * @param uri
     */
    public void deleteStringValue(String  uri){
        deleteValue(uri);
    }

    /**
     * 存储String类型数据
     * @param uri
     * @param value
     */
    private void setValue(String uri,String value){
        LogManager.i(DC.TAG,"setValue,uri --> " + uri + " value --> " + value);

        mDataBuffer = getDataBuffer(uri);
        mDataBuffer.setValue(uri,value);
        //更新数据
        Set<String> uriSet = new HashSet<>();
        uriSet.add(uri);
        for (IDataCenterObserver observer : mCenterObservers){
            observer.onDataChanged(uriSet);
        }
    }

    /**
     * 存储String类型数据
     * @param uri
     */
    private String getValue(String uri){
        mDataBuffer = getDataBuffer(uri);
        String value = mDataBuffer.getValue(uri);
        LogManager.i(DC.TAG,"getValue,uri --> " + uri + ",value:" + value);
        return value;
    }

    /**
     * 删除指定的uri的数据
     * @param uri
     */
    private void deleteValue(String uri){
        LogManager.i(DC.TAG,"deleteValue,uri --> " + uri);

        mDataBuffer = getDataBuffer(uri);
        mDataBuffer.deleteValue(uri);
        //更新数据
        Set<String> uriSet = new HashSet<>();
        uriSet.add(uri);
        for (IDataCenterObserver observer : mCenterObservers){
            observer.onDataDeleted(uriSet);
        }
    }

}
