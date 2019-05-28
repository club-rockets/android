package ca.clubrockets.anirniq.ble.activities;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;

import java.util.Set;

import ca.clubrockets.anirniq.ble.R;
import ca.clubrockets.anirniq.ble.models.SwitchDevice;
import ca.clubrockets.anirniq.ble.adapters.SwitchDeviceListAdapter;

public class MainActivity extends AppCompatActivity {

    private SwitchDeviceListAdapter switchDeviceListAdapter;

    private final int REQUEST_ENABLE_BLUETOOTH = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final SwipeRefreshLayout refreshLayout = findViewById(R.id.refresh_layout);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                switchDeviceListAdapter.refresh();
                refreshLayout.setRefreshing(false);
            }
        });

        /* Setup recycler view */
        switchDeviceListAdapter = new SwitchDeviceListAdapter();
        RecyclerView rv = findViewById(R.id.switch_device_list);
        rv.setHasFixedSize(true);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(switchDeviceListAdapter);

        prepareBluetooth();
    }

    private void prepareBluetooth() {
        BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
        if (!adapter.isEnabled()) {
            Intent enable = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enable, REQUEST_ENABLE_BLUETOOTH);
        }

        switchDeviceListAdapter.add(findDeviceByMac("Anirniq", "00:60:37:14:AD:EB"));
        switchDeviceListAdapter.add(findDeviceByMac("StratoLogger", "00:60:37:A5:99:68"));
    }

    @Override
    protected void onActivityResult(int request, int result, Intent data) {
        switch (request) {
            case REQUEST_ENABLE_BLUETOOTH:
                prepareBluetooth();
                break;

            default:
                break;
        }
    }

    private SwitchDevice findDeviceByMac(String name, String mac) {
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();

        for (BluetoothDevice device : pairedDevices) {
            if (device.getAddress().equals(mac)) {
                return new SwitchDevice(name, device, this);
            }
        }

        return null;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_scan) {
            Intent intent = new Intent(this, ScanActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
