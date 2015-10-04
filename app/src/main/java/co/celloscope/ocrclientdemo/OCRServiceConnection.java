package co.celloscope.ocrclientdemo;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.Messenger;
import android.widget.TextView;

public class OCRServiceConnection implements ServiceConnection {

    private Messenger mServiceMessenger;
    private TextView textView;

    public void setmServiceMessenger(Messenger mServiceMessenger) {
        this.mServiceMessenger = mServiceMessenger;
    }

    public Messenger getmServiceMessenger() {
        return mServiceMessenger;
    }

    public OCRServiceConnection(TextView textView) {
        this.textView = textView;
    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        mServiceMessenger = new Messenger(service);
        textView.setText("Client: Service connected.");
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
        mServiceMessenger = null;
        textView.setText("Client: Service disconnected.");
    }
}
