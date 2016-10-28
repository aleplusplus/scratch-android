package com.cilbe.android.scratchai;

import android.content.Context;
import android.content.SharedPreferences;

import ai.api.util.BluetoothController;

/**
 * Created by alabradadiaz
 */

public class SettingsManager {

    private static final String SETTINGS_PREFS_NAME = "ai.api.APP_SETTINGS";
    private static final String PREF_USE_BLUETOOTH = "USE_BLUETOOTH";

    private final Context context;
    private SharedPreferences prefs;

    private boolean useBluetooth;

    public SettingsManager(final Context context) {
        this.context = context;
        prefs = context.getSharedPreferences(SETTINGS_PREFS_NAME, Context.MODE_PRIVATE);

        useBluetooth = prefs.getBoolean(PREF_USE_BLUETOOTH, true);
    }

    public void setUseBluetooth(final boolean useBluetooth) {
        this.useBluetooth = useBluetooth;

        prefs.edit().putBoolean(PREF_USE_BLUETOOTH, useBluetooth).apply();
        final BluetoothController controller = ((Application) context.getApplicationContext()).getBluetoothController();
        if (useBluetooth) {
            controller.start();
        } else {
            controller.stop();
        }
    }

    public boolean isUseBluetooth() {
        return useBluetooth;
    }

}
