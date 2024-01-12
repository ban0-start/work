package com.app.smart.myapplication;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.os.Bundle;
import android.util.Log;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.app.smart.myapplication.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private BluetoothAdapter bluetoothAdapter;
    private BluetoothUtils bluetoothUtils;
    private String TAG="MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);
        bluetoothInit();
        startScanBluetooth();
    }

    public void bluetoothInit(){
        bluetoothUtils = BluetoothUtils.getInstance();
        bluetoothUtils.init(this);
        bluetoothAdapter = bluetoothUtils.mBluetoothAdapter;
    }
    public void startScanBluetooth(){
        BluetoothAdapter bluetoothAdapter = bluetoothUtils.mBluetoothAdapter;
        try {
            if (bluetoothAdapter == null) {
                return;
            }
            if (bluetoothAdapter.isDiscovering() == true) {
                bluetoothAdapter.cancelDiscovery();
            }

            boolean b = bluetoothAdapter.startLeScan(mDeviceScanCallback);
            System.out.println("@@@@@@@@@@@@@@@@@");
            //  bluetoothAdapter.getBluetoothLeScanner().startScan(scanCallback);


        }catch (SecurityException e){
            e.printStackTrace();
        }
    }
    private BluetoothAdapter.LeScanCallback mDeviceScanCallback = new BluetoothAdapter.LeScanCallback() {
        @Override
        public void onLeScan(BluetoothDevice bluetoothDevice, int i, byte[] bytes) {
            try {
                Log.d(TAG,"mdevice.name"+bluetoothDevice.getName()+"===============");
//                if(bluetoothDevice == null || bluetoothDevice.getName()==null || bluetoothDevice.getName().equals(""))
//                    return;
//                if(bluetoothDevice.getName().contains("JPUS")){
//                    ScanListDevice scanListDevice = new ScanListDevice();
//                    scanListDevice.setDeviceId(bluetoothDevice.getName());
//                    scanListDevice.setDeviceName(bluetoothDevice.getName());
//                    scanListDevice.setMac(bluetoothDevice.getAddress());
//                    scanListDevice.setType(2);
//                    if(searchDevList.contains(scanListDevice)==false){
//                        if(mLocalList!=null &&  mLocalList.size()>0){
//                            boolean flag = false;
//                            for(DBUserMattress localData:mLocalList){
//                                if(localData.getDeviceId().equals(scanListDevice.getDeviceId())){
//                                    scanListDevice.setOnline(1);
//
//                                }
//                            }
//
//                        }
//                        scanListDevice.setmBluetoothDevice(bluetoothDevice);
//                        searchDevList.add(scanListDevice);
//                    }
//                }
            }catch (SecurityException e){
                e.printStackTrace();;
            }
        }
    };


}