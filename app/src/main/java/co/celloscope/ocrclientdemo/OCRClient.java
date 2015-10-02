
package co.celloscope.ocrclientdemo;

import android.content.ComponentName;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.Parcel;
import android.os.RemoteException;
import android.support.annotation.NonNull;
import android.widget.TextView;
import android.widget.Toast;


public class OCRClient {

    static final int MSG_REGISTER_CLIENT = 1;
    static final int MSG_UNREGISTER_CLIENT = 2;
    static final int MSG_DO_OCR = 3;

    final Messenger mClient = new Messenger(new IncomingHandler());
    private final ContextWrapper mContextWrapper;


    Messenger mService = null;
    private ServiceConnection mConnection = new ServiceConnection() {

        public void onServiceConnected(ComponentName className, IBinder service) {
            mService = new Messenger(service);

            try {
                Message msg = Message.obtain(null, MSG_REGISTER_CLIENT);
                msg.replyTo = mClient;
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
    boolean mIsBound;
    final TextView mCallbackText;

    public OCRClient(ContextWrapper mContextWrapper, TextView mCallbackText) {

        this.mContextWrapper = mContextWrapper;

        this.mCallbackText = mCallbackText;
    }

    void doOcr() {
        try {
            if (mService != null) {
                Bundle mBundle = new Bundle();
                mBundle.putString("name", mContextWrapper.getResources().getString(R.string.app_name));
                Message msg = Message.obtain(null, MSG_DO_OCR, mBundle);
                mService.send(msg);
            }
        } catch (RemoteException e) {
            Toast.makeText(mContextWrapper, e.getMessage(), Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Toast.makeText(mContextWrapper, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    void doBindService() {
        Intent intent = new Intent();
        intent.setComponent(new ComponentName(mContextWrapper.getResources().getString(R.string.servicePackageName), mContextWrapper.getResources().getString(R.string.servceFullyQualifiedClassName)));
        mContextWrapper.bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
        mIsBound = true;
    }

    void doUnbindService() {
        if (mIsBound) {
            if (mService != null) {
                try {
                    Message msg = Message.obtain(null,
                            MSG_UNREGISTER_CLIENT);
                    msg.replyTo = mClient;
                    mService.send(msg);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }


            mContextWrapper.unbindService(mConnection);
            mIsBound = false;
        }
    }

    class IncomingHandler extends Handler {
        @Override
        public void dispatchMessage(@NonNull Message msg) {
            super.dispatchMessage(msg);


        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_DO_OCR:
                    Bundle mBundle = (Bundle) msg.obj;
                    mCallbackText.setText("Received from service: " + mBundle.getString("ocrText"));
                    break;
                default:
                    super.handleMessage(msg);
            }
        }
    }
}
