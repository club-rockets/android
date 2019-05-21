package ca.clubrockets.anirniq.ble.adapters;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import ca.clubrockets.anirniq.ble.R;

public class ScanDeviceListAdapter extends RecyclerView.Adapter<ScanDeviceListAdapter.ScanDeviceViewHolder> {
    private ArrayList<BluetoothDevice> current;
    private ArrayList<BluetoothDevice> devices;
    private ArrayList<BluetoothDevice> devices_named;

    public static class ScanDeviceViewHolder extends RecyclerView.ViewHolder {
        private View view;
        private BluetoothDevice device;

        public ScanDeviceViewHolder(View v) {
            super(v);
            this.view = v;
        }

        public void setDevice(final BluetoothDevice device) {
            String name = device.getName();
            if (name == null || name.isEmpty()) {
                name = "<unnamed device>";
            }
            ((TextView)view.findViewById(R.id.bluetooth_device_name)).setText(name);
            ((TextView)view.findViewById(R.id.bluetooth_device_address)).setText(device.getAddress());
        }
    }

    public ScanDeviceListAdapter() {
        this.devices = new ArrayList<>();
        this.devices_named = new ArrayList<>();
        this.current = devices;
    }

    public void addDevice(BluetoothDevice device) {
        if (!devices.contains(device)) {
            devices.add(device);
            if (device.getName() != null) {
                devices_named.add(device);
            }
        }
        notifyDataSetChanged();
    }

    public void clear() {
        devices.clear();
        devices_named.clear();
    }

    public void setFilterUnnamed(boolean filter) {
          current = (filter ? devices_named : devices);
          notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ScanDeviceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_scan_device, parent, false);
        ScanDeviceViewHolder vh = new ScanDeviceViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ScanDeviceViewHolder holder, int position) {
        holder.setDevice(current.get(position));
    }

    @Override
    public int getItemCount() {
        return current.size();
    }
}
