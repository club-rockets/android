package ca.clubrockets.anirniq.ble;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class ScanningActivity extends AppCompatActivity {

    private ScanDeviceListAdapter scanDeviceListAdapter;
    private BluetoothAdapter bluetoothAdapter;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        scanDeviceListAdapter = new ScanDeviceListAdapter();
        handler = new Handler();

        final BluetoothManager bluetoothManager =  (BluetoothManager) getSystemService(this.BLUETOOTH_SERVICE);
        bluetoothAdapter = bluetoothManager.getAdapter();

        /* Setup recycler view */
        RecyclerView rv = findViewById(R.id.scan_device_list);
        rv.setHasFixedSize(true);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(scanDeviceListAdapter);

        /* Setup FAB*/
        findViewById(R.id.fab_scan).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ( ContextCompat.checkSelfPermission(v.getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions((Activity) v.getContext(), new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},42);
                }
                else {
                    scanLeDevice(true);
                }
            }
        });

    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            scanLeDevice(true);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_scanning, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_filter_unnamed) {
            item.setChecked(!item.isChecked());
            scanDeviceListAdapter.setFilterUnnamed(item.isChecked());
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void scanLeDevice(final boolean enable) {
        findViewById(R.id.scan_progress).setVisibility(View.VISIBLE);
        scanDeviceListAdapter.clear();
        if (enable) {
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    findViewById(R.id.scan_progress).setVisibility(View.GONE);
                    bluetoothAdapter.stopLeScan(leScanCallback);
                }
            }, 10000);
            bluetoothAdapter.startLeScan(leScanCallback);
        } else {
            bluetoothAdapter.stopLeScan(leScanCallback);
        }
    }

    private BluetoothAdapter.LeScanCallback leScanCallback = new BluetoothAdapter.LeScanCallback() {
        @Override
        public void onLeScan(final BluetoothDevice device, int rssi, byte[] scanRecord) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    scanDeviceListAdapter.addDevice(device);
                    scanDeviceListAdapter.notifyDataSetChanged();
                }
            });
        }
    };
}
