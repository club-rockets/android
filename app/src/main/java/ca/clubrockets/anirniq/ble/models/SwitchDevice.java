package ca.clubrockets.anirniq.ble.models;

import android.util.Log;

public class SwitchDevice {
    private String name;

    public SwitchDevice(String name) {
        this.name = name;
    }

    public String getName() { return name; }

    public void setPowerCharge(boolean armed) {
        Log.i("Switch", "Power " + (armed ? "armed" : "disarmed"));
    }

    public void setDrogueCharge(boolean armed) {
        Log.i("Switch", "Drogue " + (armed ? "armed" : "disarmed"));
    }

    public void setMainCharge(boolean armed) {
        Log.i("Switch", "Main " + (armed ? "armed" : "disarmed"));
    }
}
