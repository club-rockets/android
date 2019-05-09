package ca.clubrockets.anirniq.ble.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.ArrayList;

import ca.clubrockets.anirniq.ble.models.SwitchDevice;
import ca.clubrockets.anirniq.ble.views.SwitchDeviceView;

public class SwitchDeviceListAdapter extends RecyclerView.Adapter<SwitchDeviceListAdapter.SwitchDeviceViewHolder> {
    private ArrayList<SwitchDevice> devices;

    public static class SwitchDeviceViewHolder extends RecyclerView.ViewHolder {
        private SwitchDeviceView view;

        public SwitchDeviceViewHolder(SwitchDeviceView v) {
            super(v);
            this.view = v;
        }

        public void bind(SwitchDevice device) {
            view.bind(device);
        }
    }

    public SwitchDeviceListAdapter(ArrayList<SwitchDevice> devices) {
        this.devices = devices;
    }

    @NonNull
    @Override
    public SwitchDeviceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        SwitchDeviceView v = new SwitchDeviceView(parent.getContext());
        return new SwitchDeviceViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull SwitchDeviceViewHolder holder, int position) {
        holder.bind(devices.get(position));
    }

    @Override
    public int getItemCount() {
        return devices.size();
    }
}
