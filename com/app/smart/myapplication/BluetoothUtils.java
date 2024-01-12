package com.app.smart.myapplication;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import java.lang.reflect.Method;

public class BluetoothUtils {

    public static final String TAG = BluetoothUtils.class.getSimpleName();
    /**
     * 获取系统蓝牙适配器管理类
     */
    public BluetoothAdapter mBluetoothAdapter;

    public BluetoothManager getBluetoothManager() {
        return bluetoothManager;
    }

    public void setBluetoothManager(BluetoothManager bluetoothManager) {
        this.bluetoothManager = bluetoothManager;
    }

    private BluetoothManager bluetoothManager;



    private static class Holder {
        public static BluetoothUtils INSTANCE = new BluetoothUtils();
    }

    public static BluetoothUtils getInstance() {
        return Holder.INSTANCE;
    }

    public void init(Activity context) {
        if (bluetoothManager == null) {
            bluetoothManager = (BluetoothManager) context.getSystemService(Context.BLUETOOTH_SERVICE);
        }
        mBluetoothAdapter = bluetoothManager.getAdapter();
        // 询问打开蓝牙
        System.out.println(isBlueEnable()+"=isBlueEnable");
        if (!isBlueEnable()) {
            openBlueSync(context, 1);
        }
    }

    public BluetoothAdapter getBlueAdapter() {
        return mBluetoothAdapter;
    }

    /**
     * 设备是否支持蓝牙  true为支持
     *
     * @return
     */
    public boolean isSupportBlue() {
        return mBluetoothAdapter != null;
    }

    /**
     * 蓝牙是否打开   true为打开
     *
     * @return
     */
    public boolean isBlueEnable() {
        return isSupportBlue() && mBluetoothAdapter.isEnabled();
    }

    /**
     * 开启广播
     */
    public void openBroadCast(Activity activity, BroadcastReceiver scanBlueReceiver) {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothDevice.ACTION_FOUND);//获得扫描结果
        intentFilter.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);//绑定状态变化
        intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);//开始扫描
        intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);//扫描结束
        activity.registerReceiver(scanBlueReceiver, intentFilter);
        System.out.println("openBroadCastopenBroadCastopenBroadCastopenBroadCastopenBroadCast");
    }

    /**
     * 自动打开蓝牙（同步）
     * 这个方法打开蓝牙会弹出提示
     * 需要在onActivityResult 方法中判断resultCode == RESULT_OK  true为成功
     */
    public void openBlueSync(Activity activity, int requestCode)  throws SecurityException{
        Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        activity.startActivityForResult(intent, requestCode);
    }

    /**
     * 配对（配对成功与失败通过广播返回）
     *
     * @param device
     */
    public void pinBlue(BluetoothDevice device) throws SecurityException  {
        if (device == null) {
            Log.e(TAG, "bond device null");
            return;
        }
        if (!isBlueEnable()) {
            Log.e(TAG, "Bluetooth not enable!");
            return;
        }
        //配对之前把扫描关闭
//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED) {
//            return;
//        }
        if (mBluetoothAdapter.isDiscovering()) {
            mBluetoothAdapter.cancelDiscovery();
        }
        //判断设备是否配对，没有配对在配，配对了就不需要配了
        if (device.getBondState() == BluetoothDevice.BOND_NONE) {
            Log.d(TAG, "attemp to bond:" + device.getName());
            try {
                Method createBondMethod = device.getClass().getMethod("createBond");
                createBondMethod.invoke(device);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                Log.e(TAG, "attemp to bond fail!");
            }
        }
    }

    /**
     * 取消配对（取消配对成功与失败通过广播返回 也就是配对失败）
     *
     * @param device
     */
    public void cancelPinBlue(BluetoothDevice device) throws SecurityException {
        if (device == null) {
            Log.d(TAG, "cancel bond device null");

            return;
        }
        if (!isBlueEnable()) {
            Log.e(TAG, "Bluetooth not enable!");
            return;
        }
        //判断设备是否配对，没有配对就不用取消了

//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
//            // TODO: Consider calling
//            //    ActivityCompat#requestPermissions
//            // here to request the missing permissions, and then overriding
//            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//            //                                          int[] grantResults)
//            // to handle the case where the user grants the permission. See the documentation
//            // for ActivityCompat#requestPermissions for more details.
//            return;
//        }
        if (device.getBondState() != BluetoothDevice.BOND_NONE) {
            Log.d(TAG, "attemp to cancel bond:" + device.getName());
            try {
                Method removeBondMethod = device.getClass().getMethod("removeBond");
                removeBondMethod.invoke(device);
            } catch (Exception e) {
                e.printStackTrace();
                Log.e(TAG, "attemp to cancel bond fail!");
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    public Boolean hasPermissions(Activity activity,int requestCode) {
        if (!hasLocationPermissions(activity)) {
            requestLocationPermission(activity,requestCode);
            return false;
        }
        return true;
    }
    @RequiresApi(Build.VERSION_CODES.M)
    public Boolean hasLocationPermissions(Activity activity) {
        return activity.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    @RequiresApi(Build.VERSION_CODES.M)
    public void requestLocationPermission(Activity activity,int requestCode) {
        activity.requestPermissions(
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                requestCode
        );
    }
}

