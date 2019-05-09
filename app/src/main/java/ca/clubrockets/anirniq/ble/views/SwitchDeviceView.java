package ca.clubrockets.anirniq.ble.views;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.CompoundButton;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;

import ca.clubrockets.anirniq.ble.R;
import ca.clubrockets.anirniq.ble.models.SwitchDevice;

public class SwitchDeviceView extends ConstraintLayout {
    private ProgressBar progress;
    private Switch switch_power, switch_drogue, switch_main;
    private SwitchDevice device;

    public SwitchDeviceView(Context context) {
        super(context);
        this.init(context);
    }

    public SwitchDeviceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.init(context);
    }

    public SwitchDeviceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.init(context);
    }

    private void init(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.view_main_device, this, true);

        if (getLayoutParams() == null) {
            setLayoutParams(new ConstraintLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        }
        else {
            getLayoutParams().width = LayoutParams.MATCH_PARENT;
        }


        progress = findViewById(R.id.device_refresh_progress);
        switch_power = findViewById(R.id.switch_charge_power);
        switch_drogue = findViewById(R.id.switch_charge_drogue);
        switch_main = findViewById(R.id.switch_charge_main);

        progress.setVisibility(INVISIBLE);
        switch_power.setOnCheckedChangeListener(new SwitchChangeListener("power"));
        switch_drogue.setOnCheckedChangeListener(new SwitchChangeListener("drogue"));
        switch_main.setOnCheckedChangeListener(new SwitchChangeListener("main"));
    }

    public void bind(SwitchDevice device) {
        this.device = device;
        ((TextView) findViewById(R.id.switch_device_name)).setText(device.getName());
    }

    private class SwitchChangeListener implements CompoundButton.OnCheckedChangeListener {
        private String charge;

        public SwitchChangeListener(String charge) {
            super();
            this.charge = charge;
        }

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            Log.i("Switch", charge + " is " + (isChecked ? "armed" : "disarmed"));
        }
    }
}
