package co.celloscope.ocrclientdemo;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.Messenger;

public class OCRServiceConnection implements ServiceConnection {

    private Messenger messenger;
    private final ServiceConnectionStatus serviceConnectionStatus;

    public OCRServiceConnection(ServiceConnectionStatus serviceConnectionStatus) {
        this.serviceConnectionStatus = serviceConnectionStatus;
    }

    Messenger getMessenger() {
        return messenger;
    }

    boolean isConnected() {
        return this.messenger != null;
    }

    void disconnect() {
        this.messenger = null;
    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        this.messenger = new Messenger(service);
        serviceConnectionStatus.onServiceConnected();
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
        this.disconnect();
        serviceConnectionStatus.onServiceDisConnected();
    }
}
