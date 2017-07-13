package com.yyd.ynjc.Activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.Poi;
import com.yyd.ynjc.R;
import com.yyd.ynjc.Service.LocationService;

public class Main extends BaseActivity {

    private TextView mTextMessage;
    private DrawerLayout drawerLayout;

    private BottomNavigationView.OnNavigationItemSelectedListener mBottomNav_OnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    mTextMessage.setText(R.string.title_home);
                    return true;
                case R.id.navigation_dashboard:
                    mTextMessage.setText(R.string.title_dashboard);
                    return true;
                case R.id.navigation_notifications:
                    mTextMessage.setText(R.string.title_notifications);
                    return true;
                default:
            }
            return false;
        }

    };

    private NavigationView.OnNavigationItemSelectedListener mNav_OnNavigationItemSelectedListener
            = new NavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.exit:
                    drawerLayout.closeDrawers();
                    return true;
                default:
            }
            return false;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);


        //-------------------------------------顶部ToolBar—代码开始------------------------------------
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //-------------------------------------顶部ToolBar—代码结束------------------------------------

        mTextMessage = (TextView) findViewById(R.id.message);
        drawerLayout = (DrawerLayout)findViewById(R.id.container);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_24dp);
        }

        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(mBottomNav_OnNavigationItemSelectedListener);


        NavigationView navigationView = (NavigationView)findViewById(R.id.navigation);
        navigationView.setNavigationItemSelectedListener(mNav_OnNavigationItemSelectedListener);
    }

    //-------------------------------------顶部ToolBar—代码开始------------------------------------
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.top_toolbar,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                break;
            case R.id.backup:
                GenericClass.ShowToast(Main.this,"上传");
                break;
            case R.id.setup:
                GenericClass.ShowToast(Main.this,"设置");
                break;
            case R.id.about:
                GenericClass.ShowToast(Main.this,"关于");
                break;
            default:
        }
        return true;
    }
    //-------------------------------------顶部ToolBar—代码结束------------------------------------

    //--------------------------------------Baidu定位—代码开始-------------------------------------

    private LocationService locationService;

    /***
     * 开始定位
     */
    @Override
    protected void onStart() {
        super.onStart();
        locationService = ((CustomApplication) getApplication()).locationService;
        locationService.registerListener(mListener);//注册监听
        locationService.setLocationOption(locationService.getDefaultLocationClientOption());//设置参数
        locationService.start();// 定位SDK
    }

    /***
     * 停止定位
     */
    @Override
    protected void onStop() {
        locationService.unregisterListener(mListener); //注销监听
        locationService.stop(); //停止定位服务
        super.onStop();
    }

    /*****
     *
     * 定位结果回调，重写onReceiveLocation方法，可以直接拷贝如下代码到自己工程中修改
     *
     */
    private BDLocationListener mListener = new BDLocationListener() {
        @Override
        public void onReceiveLocation(BDLocation location) {
            if (location != null && location.getLocType() != BDLocation.TypeServerError) {
                String city = "";
                city = location.getCity() + location.getDistrict();
                StringBuffer sb = new StringBuffer(256);
                sb.append("时间 : ");
                sb.append(location.getTime());
                sb.append("\n定位类型 : ");
                sb.append(location.getLocType());
                sb.append("\n定位类型说明 : ");
                sb.append(location.getLocTypeDescription());
                sb.append("\n纬度 : ");
                sb.append(location.getLatitude());
                sb.append("\n经度 : ");
                sb.append(location.getLongitude());
                sb.append("\n半径 : ");
                sb.append(location.getRadius());
                sb.append("\n国家码 : ");
                sb.append(location.getCountryCode());
                sb.append("\n国家名称 : ");
                sb.append(location.getCountry());
                sb.append("\n城市编码 : ");
                sb.append(location.getCityCode());
                sb.append("\n城市 : ");
                sb.append(location.getCity());
                sb.append("\n区 : ");
                sb.append(location.getDistrict());
                sb.append("\n街道 : ");
                sb.append(location.getStreet());
                sb.append("\n地址信息 : ");
                sb.append(location.getAddrStr());
                sb.append("\n用户室内外判断 : ");
                sb.append(location.getUserIndoorState());
                sb.append("\n方向(not all devices have value) : ");
                sb.append(location.getDirection());
                sb.append("\n位置描述 : ");
                sb.append(location.getLocationDescribe());// 位置语义化信息
                sb.append("\nPoi信息: ");// POI信息
                if (location.getPoiList() != null && !location.getPoiList().isEmpty()) {
                    for (int i = 0; i < location.getPoiList().size(); i++) {
                        Poi poi = (Poi) location.getPoiList().get(i);
                        sb.append(poi.getName() + ";");
                    }
                }
                if (location.getLocType() == BDLocation.TypeGpsLocation) {// GPS定位结果
                    sb.append("\n速度（单位：km/h） : ");
                    sb.append(location.getSpeed());// 速度 单位：km/h
                    sb.append("\n卫星数目 : ");
                    sb.append(location.getSatelliteNumber());// 卫星数目
                    sb.append("\n海拔高度（单位：米） : ");
                    sb.append(location.getAltitude());// 海拔高度 单位：米
                    sb.append("\ngps质量 : ");
                    sb.append(location.getGpsAccuracyStatus());// *****gps质量判断*****
                    sb.append("\n描述 : ");
                    sb.append("gps定位成功");
                } else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {// 网络定位结果
                    // 运营商信息
                    if (location.hasAltitude()) {// *****如果有海拔高度*****
                        sb.append("\n海拔高度（单位：米） : ");
                        sb.append(location.getAltitude());// 单位：米
                    }
                    sb.append("\n运营商信息 : ");// 运营商信息
                    sb.append(location.getOperators());
                    sb.append("\n描述 : ");
                    sb.append("网络定位成功");
                } else if (location.getLocType() == BDLocation.TypeOffLineLocation) {// 离线定位结果
                    sb.append("\n描述 : ");
                    sb.append("离线定位成功，离线定位结果也是有效的");
                } else if (location.getLocType() == BDLocation.TypeServerError) {
                    sb.append("\n描述 : ");
                    sb.append("服务端网络定位失败，可以反馈IMEI号和大体定位时间到loc-bugs@baidu.com，会有人追查原因");
                } else if (location.getLocType() == BDLocation.TypeNetWorkException) {
                    sb.append("\n描述 : ");
                    sb.append("网络不同导致定位失败，请检查网络是否通畅");
                } else if (location.getLocType() == BDLocation.TypeCriteriaException) {
                    sb.append("\n描述 : ");
                    sb.append("无法获取有效定位依据导致定位失败，一般是由于手机的原因，处于飞行模式下一般会造成这种结果，可以试着重启手机");
                }
                Log.i("LocationInfo:",sb.toString());
                logMsg(city.toString());
            }
        }
        public void onConnectHotSpotMessage(String s, int i){
        }
    };

    /**
     * 显示请求字符串
     *
     * @param str
     */
    public void logMsg(String str) {
        final String s = str;
        try {
            if (mTextMessage != null){
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        mTextMessage.post(new Runnable() {
                            @Override
                            public void run() {
                                mTextMessage.setText(s);
                            }
                        });
                    }
                }).start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //--------------------------------------Baidu定位—代码结束--------- ---------------------------
}
