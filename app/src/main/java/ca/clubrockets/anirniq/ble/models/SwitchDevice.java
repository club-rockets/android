package ca.clubrockets.anirniq.ble.models;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableInt;
import android.util.Log;
import android.view.View;

import java.util.Observable;
import java.util.UUID;

public class SwitchDevice {
    public static final int POWER = 1;
    public static final int DROGUE = 2;
    public static final int MAIN = 3;

    private static final int READ = 0;
    private static final int WRITE = 1;

    private final UUID SERVICE_UUID = UUID.fromString("10100101-1010-1010-1010-101010101010");

    private final UUID POWER_WRITE_UUID = UUID.fromString("32323232-3232-3232-3232-323232323232");
    private final UUID POWER_READ_UUID = UUID.fromString("22222222-2222-2222-2222-222222222222");

    private final UUID DROGUE_WRITE_UUID = UUID.fromString("30303030-3030-3030-3030-303030303030");
    private final UUID DROGUE_READ_UUID = UUID.fromString("20202020-2020-2020-2020-202020202020");

    private final UUID MAIN_WRITE_UUID = UUID.fromString("31313131-3131-3131-3131-313131313131");
    private final UUID MAIN_READ_UUID = UUID.fromString("21212121-2121-2121-2121-212121212121");

    private final byte[] ARMED = { (byte) 0xAA };
    private final byte[] DISARMED = { 0x00 };

    private String name;
    private BluetoothDevice device;
    private BluetoothGatt device_gatt;

    public ObservableBoolean power;
    public ObservableBoolean drogue;
    public ObservableBoolean main;
    public ObservableInt progress;

    public SwitchDevice(String name, BluetoothDevice device) {
        this.name = name;
        this.device = device;

        power = new ObservableBoolean(false);
        drogue = new ObservableBoolean(false);
        main = new ObservableBoolean(false);
        progress = new ObservableInt(View.VISIBLE);
    }

    public void connect(Context context) {
        device.connectGatt(context, true, gattCallback);
    }

    public String getName() { return name; }

    public boolean setCharge(int charge, boolean armed) {
        BluetoothGattCharacteristic characteristic = getCharacteristic(charge, WRITE);
        characteristic.setValue(armed ? ARMED : DISARMED);
        progress.set(View.VISIBLE);
        return device_gatt.writeCharacteristic(characteristic);
    }

    private BluetoothGattCharacteristic getCharacteristic(int charge, int mode) {
        UUID uuid = null;
        switch (charge) {
            case POWER:
                uuid = mode == 0 ? POWER_READ_UUID : POWER_WRITE_UUID;
                break;
            case DROGUE:
                uuid = mode == 0 ? DROGUE_READ_UUID : DROGUE_WRITE_UUID;
                break;
            case MAIN:
                uuid = mode == 0 ? MAIN_READ_UUID : MAIN_WRITE_UUID;
                break;
        }

        return device_gatt.getService(SERVICE_UUID).getCharacteristic(uuid);
    }

    private final BluetoothGattCallback gattCallback = new BluetoothGattCallback() {
        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            if (newState == BluetoothProfile.STATE_CONNECTED) {
                gatt.discoverServices();
                device_gatt = gatt;
                Log.w(name, "Connected");
            } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                Log.w(name, "Disconnected");
            }
        }

        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            if (status == BluetoothGatt.GATT_SUCCESS) {
                gatt.readCharacteristic(getCharacteristic(POWER, READ));
            } else {
                Log.w(name, "onServicesDiscovered received: " + status);
            }
        }

        @Override
        public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            if (characteristic.getUuid().equals(POWER_READ_UUID)) {
                power.set(characteristic.getValue()[0] == 1);
                gatt.readCharacteristic(getCharacteristic(DROGUE, READ));
            }
            if (characteristic.getUuid().equals(DROGUE_READ_UUID)) {
                drogue.set(characteristic.getValue()[0] == 1);
                gatt.readCharacteristic(getCharacteristic(MAIN, READ));
            }
            if (characteristic.getUuid().equals(MAIN_READ_UUID)) {
                main.set(characteristic.getValue()[0] == 1);
                progress.set(View.INVISIBLE);
            }
        }

        @Override
        public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            gatt.readCharacteristic(getCharacteristic(POWER, READ));
        }
    };
}
