package ca.clubrockets.anirniq.ble.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;

import ca.clubrockets.anirniq.ble.R;
import ca.clubrockets.anirniq.ble.models.SwitchDevice;
import ca.clubrockets.anirniq.ble.adapters.SwitchDeviceListAdapter;

public class MainActivity extends AppCompatActivity {

    private SwitchDeviceListAdapter switchDeviceListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ArrayList<SwitchDevice> devices = new ArrayList<>();
        devices.add(new SwitchDevice("Anirniq"));
        devices.add(new SwitchDevice("StratoLogger CF"));
        switchDeviceListAdapter = new SwitchDeviceListAdapter(devices);

        /* Setup recycler view */
        RecyclerView rv = findViewById(R.id.switch_device_list);
        rv.setHasFixedSize(true);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(switchDeviceListAdapter);
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
