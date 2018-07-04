package com.map_demo;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.navi.AmapNaviPage;
import com.amap.api.navi.AmapNaviParams;


public class MainActivity extends AppCompatActivity {
    MapView mMapView = null;
    AMap aMap;
    MyLocationStyle myLocationStyle;
    private Toolbar mToolbar;
    Intent intent;
    boolean flag =true;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //获取地图控件引用
        mMapView = (MapView) findViewById(R.id.map);
        //在activity执行onCreate时执行mMapView.onCreate(savedInstanceState)，创建地图
        mMapView.onCreate(savedInstanceState);
        if (aMap == null) {
            aMap = mMapView.getMap();
            aMap.moveCamera(CameraUpdateFactory.zoomTo(16));
            aMap.showIndoorMap(true);//true：显示室内地图；false：不显示；
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.d("","onCreate: 申请定位权限");
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
        } else {
            initMap();
        }
        invalidateOptionsMenu();

        //初始化试图
        initView();
        mToolbar.setTitle("高德地图");
        mToolbar.setTitleTextColor(Color.parseColor("#FFFFFF"));
        setSupportActionBar(mToolbar);
        //设置返回键可用
//        getSupportActionBar().setHomeButtonEnabled(true);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //创建返回键，并实现打开/关闭监听

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void initView() {
        mToolbar = (Toolbar)findViewById(R.id.tl_custom);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0) {
                    boolean flag = true;
                    for (int i = 0; i < grantResults.length; i++) {
                        if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                            Log.d("", "onRequestPermissionsResult: 未授权 " + permissions[i]);
                            toast("未给予权限" + permissions[i]);
                            flag=false;
                        }
                    }
                    if(flag){
                        toast("授权通过");
                        initMap();
                    }
                }
                break;
            default:
        }
    }

    private void toast(String s) {
        Toast.makeText(getApplicationContext(),s,Toast.LENGTH_LONG).show();
    }

    private void initMap() {
        myLocationStyle = new MyLocationStyle();//初始化定位蓝点样式类
        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATE);
        myLocationStyle.interval(2000); //设置连续定位模式下的定位间隔，只在连续定位模式下生效，单次定位模式下不会生效。单位为毫秒。
        myLocationStyle.showMyLocation(true);//设置是否显示定位小蓝点，用于满足只想使用定位，不想使用定位小蓝点的场景，设置false以后图面上不再有定位蓝点的概念，但是会持续回调位置信息。
        aMap.setMyLocationStyle(myLocationStyle);//设置定位蓝点的Style
        aMap.getUiSettings().setMyLocationButtonEnabled(true);//设置默认定位按钮是否显示，非必需设置。
        aMap.setMyLocationEnabled(true);// 设置为true表示启动显示定位蓝点，false表示隐藏定位蓝点并不进行定位，默认是false。
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，销毁地图
        mMapView.onDestroy();
    }
    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView.onResume ()，重新绘制加载地图
        mMapView.onResume();
    }
    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView.onPause ()，暂停地图的绘制
        mMapView.onPause();
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //在activity执行onSaveInstanceState时执行mMapView.onSaveInstanceState (outState)，保存地图当前的状态
        mMapView.onSaveInstanceState(outState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu,menu);
        menu.add(Menu.NONE,Menu.FIRST+2,0,"天气");
        menu.add(Menu.NONE,Menu.FIRST+3,0,"导航");
//        menu.add(Menu.NONE,Menu.FIRST+4,0,"夜景地图");
//        menu.add(Menu.NONE,Menu.FIRST+5,0,"正常地图");
//        menu.add(Menu.NONE,Menu.FIRST+6,0,"卫星图");
        menu.add(Menu.NONE,Menu.FIRST+7,0,"实时路况");
        menu.add(Menu.NONE,Menu.FIRST+1,0,"退出");
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case Menu.FIRST+1:
                System.exit(0);
                break;
            case Menu.FIRST+2:
                intent = new Intent(this,WeatherActivity.class);
                startActivity(intent);
                break;
            case Menu.FIRST+7:
                if (flag==true) {
                    aMap.setTrafficEnabled(true);//显示实时路况图层，aMap是地图控制器对象。
                    flag = false;
                }else {
                    aMap.setTrafficEnabled(false);
                    flag = true;
                }
                return flag;
            case R.id.MAP_TYPE_NAVI:
                aMap.setMapType(AMap.MAP_TYPE_NAVI);
                break;
            case R.id.MAP_TYPE_NIGH:
                aMap.setMapType(AMap.MAP_TYPE_NIGHT);
                break;
            case R.id.MAP_TYPE_NORMAL:
                aMap.setMapType(AMap.MAP_TYPE_NORMAL);
                break;
            case R.id.MAP_TYPE_SATELLITE:
                aMap.setMapType(AMap.MAP_TYPE_SATELLITE);
                break;
            case R.id.LOCATION_TYPE_LOCATION_ROTATE:
                myLocationStyle = new MyLocationStyle();//初始化定位蓝点样式类
                myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);
                myLocationStyle.interval(2000); //设置连续定位模式下的定位间隔，只在连续定位模式下生效，单次定位模式下不会生效。单位为毫秒。
                myLocationStyle.showMyLocation(true);//设置是否显示定位小蓝点，用于满足只想使用定位，不想使用定位小蓝点的场景，设置false以后图面上不再有定位蓝点的概念，但是会持续回调位置信息。
                aMap.setMyLocationStyle(myLocationStyle);//设置定位蓝点的Style
                aMap.getUiSettings().setMyLocationButtonEnabled(true);//设置默认定位按钮是否显示，非必需设置。
                aMap.setMyLocationEnabled(true);// 设置为true表示启动显示定位蓝点，false表示隐藏定位蓝点并不进行定位，默认是false。
                break;
            case R.id.LOCATION_TYPE_LOCATE:
                myLocationStyle = new MyLocationStyle();//初始化定位蓝点样式类
                myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATE);
                myLocationStyle.interval(2000); //设置连续定位模式下的定位间隔，只在连续定位模式下生效，单次定位模式下不会生效。单位为毫秒。
                myLocationStyle.showMyLocation(true);//设置是否显示定位小蓝点，用于满足只想使用定位，不想使用定位小蓝点的场景，设置false以后图面上不再有定位蓝点的概念，但是会持续回调位置信息。
                aMap.setMyLocationStyle(myLocationStyle);//设置定位蓝点的Style
                aMap.getUiSettings().setMyLocationButtonEnabled(true);//设置默认定位按钮是否显示，非必需设置。
                aMap.setMyLocationEnabled(true);// 设置为true表示启动显示定位蓝点，false表示隐藏定位蓝点并不进行定位，默认是false。
                break;
            case  Menu.FIRST+3:
                AmapNaviPage.getInstance().showRouteActivity(getApplicationContext(), new AmapNaviParams(null),  null);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public boolean onPrepareOptionsMenu(Menu menu){
        return super.onPrepareOptionsMenu(menu);
    }


}
