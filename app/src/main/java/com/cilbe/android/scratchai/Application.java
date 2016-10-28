package com.cilbe.android.scratchai;

/**
 * Created by alabradadiaz.
 */

import android.content.Context;
import android.util.Log;
import ai.api.util.BluetoothController;

public class Application extends android.app.Application {

    private static final String TAG = Application.class.getSimpleName();

    private int activitiesCount;
    private BluetoothControllerImpl bluetoothController;
    private SettingsManager settingsManager;

    @Override
    public void onCreate() {
        super.onCreate();
        bluetoothController = new BluetoothControllerImpl(this);
        settingsManager = new SettingsManager(this);
    }

    public BluetoothController getBluetoothController() {
        return bluetoothController;
    }

    public SettingsManager getSettingsManager() {
        return settingsManager;
    }

    protected void onActivityResume() {
        if (activitiesCount++ == 0) { // on become foreground
            if (settingsManager.isUseBluetooth()) {
                bluetoothController.start();
            }
        }
    }

    protected void onActivityPaused() {
        if (--activitiesCount == 0) { // on become background
            bluetoothController.stop();
        }
    }

    private boolean isInForeground() {
        return activitiesCount > 0;
    }

    private class BluetoothControllerImpl extends BluetoothController {

        public BluetoothControllerImpl(Context context) {
            super(context);
        }

        @Override
        public void onHeadsetDisconnected() {
            Log.d(TAG, "Bluetooth headset disconnected");
        }

        @Override
        public void onHeadsetConnected() {
            Log.d(TAG, "Bluetooth headset connected");

            if (isInForeground() && settingsManager.isUseBluetooth()
                    && !bluetoothController.isOnHeadsetSco()) {
                bluetoothController.start();
            }
        }

        @Override
        public void onScoAudioDisconnected() {
            Log.d(TAG, "Bluetooth sco audio finished");
            bluetoothController.stop();

            if (isInForeground() && settingsManager.isUseBluetooth()) {
                bluetoothController.start();
            }
        }

        @Override
        public void onScoAudioConnected() {
            Log.d(TAG, "Bluetooth sco audio started");
        }

    }

}