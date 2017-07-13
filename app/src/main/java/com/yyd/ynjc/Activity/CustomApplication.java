package com.yyd.ynjc.Activity;


import android.app.Application;
import android.content.Context;

import com.yyd.ynjc.Service.LocationService;
import com.baidu.mapapi.SDKInitializer;

/**
 * 主Application，所有百度定位SDK的接口说明请参考线上文档：http://developer.baidu.com/map/loc_refer/index.html
 *
 * 百度定位SDK官方网站：http://developer.baidu.com/map/index.php?title=android-locsdk
 *
 * 直接拷贝com.baidu.location.service包到自己的工程下，简单配置即可获取定位结果，也可以根据demo内容自行封装
 */
public class CustomApplication extends Application {
		private static Context context;
    public LocationService locationService;
    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        
        /***
         * 初始化定位sdk，建议在Application中创建
         */
        locationService = new LocationService(context);
        SDKInitializer.initialize(context);
    }    
    
    public static Context getContext(){
        return context;
    }
}
