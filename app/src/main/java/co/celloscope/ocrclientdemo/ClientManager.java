
package co.celloscope.ocrclientdemo;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.widget.TextView;
import android.widget.Toast;


public class ClientManager {

    private final String testFilePath = Environment.getExternalStorageDirectory()
            + "/ocr.jpg";
    private final Messenger mClientMessenger;
    Messenger mService = null;
    private final Context mClientContext;
    boolean mIsBound;
    final TextView mCallbackText;

    public ClientManager(Context context, TextView textView) {
        this.mClientContext = context;
        this.mCallbackText = textView;
        mClientMessenger = new Messenger(new ClientMessengerHandler(this.mCallbackText));
    }


    private ServiceConnection mConnection = new ServiceConnection() {

        public void onServiceConnected(ComponentName className, IBinder service) {
            mService = new Messenger(service);

            try {
                Message msg = Message.obtain(null, ServiceOperations.MSG_REGISTER_CLIENT);
                msg.replyTo = mClientMessenger;
                mService.send(msg);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            mCallbackText.setText("Service connected and register request sent.");

        }


        public void onServiceDisconnected(ComponentName className) {
            mService = null;
            mCallbackText.setText("Disconnected from service.");
        }
    };

    void doOcr() {
        try {
            if (mService != null) {
                Bundle mBundle = new Bundle();
                mBundle.putString("name", testFilePath);
                Message msg = Message.obtain(null, ServiceOperations.MSG_DO_OCR, mBundle);
                mService.send(msg);
            }
        } catch (RemoteException e) {
            Toast.makeText(mClientContext, e.getMessage(), Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Toast.makeText(mClientContext, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    void doBindService() {
        Intent intent = new Intent();
        intent.setComponent(new ComponentName(mClientContext.getResources().getString(R.string.servicePackageName), mClientContext.getResources().getString(R.string.serviceFullyQualifiedClassName)));
        mClientContext.bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
        mIsBound = true;
    }

    void doUnbindService() {
        if (mIsBound) {
            if (mService != null) {
                try {
                    Message msg = Message.obtain(null,
                            ServiceOperations.MSG_UNREGISTER_CLIENT);
                    msg.replyTo = mClientMessenger;
                    mService.send(msg);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }


            mClientContext.unbindService(mConnection);
            mIsBound = false;
        }
    }

}
