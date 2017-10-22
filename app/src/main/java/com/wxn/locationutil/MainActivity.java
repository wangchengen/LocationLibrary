package com.wxn.locationutil;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        myPermissionRequest();

        myLocation();

    }

    private void myPermissionRequest() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // 检查是否拥有权限，申请一个（或多个）权限，并提供用于回调返回的获取码（用户定义)
            if (this.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[] {
                        Manifest.permission.ACCESS_COARSE_LOCATION
                }, 0);
            }
            else {
                // 已拥有权限，作相应处理（调用定位SDK应当确保相关权限均被授权，否则可能引起定位失败）
                myLocation();
            }
        }else {
            // 安卓手机版本在5.0时，配置清单中已申明权限，作相应处理，此处正对sdk版本低于23的手机
            myLocation();
        }
    }

    /**
     * 百度地图定位的请求方法
     */
    private void myLocation() {
        LocationUtil.getLocation(this);
        LocationUtil.setMyLocationListener(new LocationUtil.MyLocationListener() {
            @Override
            public void myLocatin(final double mylongitude, final double mylatitude, final String province, final String city, final String street) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // 得到的定位数据进行处理
                        Log.e("=========", "mylongitude = "+mylongitude+"mylatitude = "+mylatitude+"province = "+province+"city = "+city+"street = "+street);
                    }
                });
            }
        });
    }

    /**
     * 权限请求的返回结果
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            // requestCode即所声明的权限获取码，在checkSelfPermission时传入
            case 0:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // 第一次获取到权限，请求定位
                    myLocation();
                }
                else {
                    // 没有获取到权限，做特殊处理
                    Log.i("=========", "请求权限失败");
                }
                break;

            default:
                break;
        }
    }



}
