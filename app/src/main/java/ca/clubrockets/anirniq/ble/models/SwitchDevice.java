package ca.clubrockets.anirniq.ble.models;

import android.util.Log;

public class SwitchDevice {
    public static final int POWER = 1;
    public static final int DROGUE = 2;
    public static final int MAIN = 3;

    private String name;

    public SwitchDevice(String name) {
        this.name = name;
    }

    public String getName() { return name; }

    public void setCharge(int charge, boolean armed) {
        switch (charge) {
            case POWER:
                setPowerCharge(armed);
                break;
            case DROGUE:
                setDrogueCharge(armed);
                break;
            case MAIN:
                setMainCharge(armed);
                break;
            default:
                break;
        }
    }

    public void setPowerCharge(boolean armed) {
        Log.i("Switch " + name, "Power " + (armed ? "armed" : "disarmed"));
    }

    public void setDrogueCharge(boolean armed) {
        Log.i("Switch " + name, "Drogue " + (armed ? "armed" : "disarmed"));
    }

    public void setMainCharge(boolean armed) {
        Log.i("Switch " + name, "Main " + (armed ? "armed" : "disarmed"));
    }
}
