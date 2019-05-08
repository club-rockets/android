package ca.clubrockets.anirniq.ble;

import android.bluetooth.BluetoothDevice;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class DeviceListAdapter extends RecyclerView.Adapter<DeviceListAdapter.DeviceViewHolder> {
    private ArrayList<BluetoothDevice> current;
    private ArrayList<BluetoothDevice> devices;
    private ArrayList<BluetoothDevice> devices_named;

    public static class DeviceViewHolder extends RecyclerView.ViewHolder {
        private View view;

        public DeviceViewHolder(View v) {
            super(v);
            this.view = v;
        }

        public void setDevice(BluetoothDevice device) {
            String name = device.getName();
            if (name == null || name.isEmpty()) {
                name = "<unnamed device>";
            }
            ((TextView)view.findViewById(R.id.bluetooth_device_name)).setText(name);
            ((TextView)view.findViewById(R.id.bluetooth_device_address)).setText(device.getAddress());
        }
    }

    public DeviceListAdapter() {
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
    public DeviceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.device_view, parent, false);
        DeviceViewHolder vh = new DeviceViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull DeviceViewHolder holder, int position) {
        holder.setDevice(current.get(position));
    }

    @Override
    public int getItemCount() {
        return current.size();
    }
}
