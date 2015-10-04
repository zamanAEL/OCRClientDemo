
package co.celloscope.ocrclientdemo;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Environment;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.widget.TextView;
import android.widget.Toast;


public class ClientManager {

    private final String testFilePath = Environment.getExternalStorageDirectory()
            + "/ocr.jpg";
    private final Messenger mClientMessenger;
    private final Context mClientContext;
    private final TextView mTextView;
    boolean mIsRegistered;
    private final OCRServiceConnection mServiceConnection;

    public ClientManager(Context context, TextView textView) {
        this.mClientContext = context;
        this.mTextView = textView;
        mClientMessenger = new Messenger(new ClientMessengerHandler(textView));
        mServiceConnection = new OCRServiceConnection(textView);
    }

    void connectService() {
        Intent intent = new Intent();
        intent.setComponent(new ComponentName(mClientContext.getResources().getString(R.string.servicePackageName), mClientContext.getResources().getString(R.string.serviceFullyQualifiedClassName)));
        mClientContext.bindService(intent, mServiceConnection, Context.BIND_AUTO_CREATE);
    }

    void registerClient() {
        if (this.mServiceConnection.getmServiceMessenger() != null) {
            try {
                Message msg = Message.obtain(null, ServiceOperations.MSG_REGISTER_CLIENT);
                msg.replyTo = mClientMessenger;
                this.mServiceConnection.getmServiceMessenger().send(msg);
                mIsRegistered = true;
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        } else {
            this.mTextView.setText("Service is not connected");
        }
    }

    void doOcr() {
        if (mIsRegistered && this.mServiceConnection.getmServiceMessenger() != null) {
            try {
                Bundle mBundle = new Bundle();
                mBundle.putString("name", testFilePath);
                Message msg = Message.obtain(null, ServiceOperations.MSG_DO_OCR, mBundle);
                this.mServiceConnection.getmServiceMessenger().send(msg);
            } catch (RemoteException e) {
                Toast.makeText(mClientContext, e.getMessage(), Toast.LENGTH_LONG).show();
            } catch (Exception e) {
                Toast.makeText(mClientContext, e.getMessage(), Toast.LENGTH_LONG).show();
            }
        } else {
            this.mTextView.setText("Service is not registered");
        }
    }


    void unregisterClient() {
        if (mIsRegistered && this.mServiceConnection.getmServiceMessenger()!= null) {
            try {
                Message msg = Message.obtain(null,
                        ServiceOperations.MSG_UNREGISTER_CLIENT);
                msg.replyTo = mClientMessenger;
                this.mServiceConnection.getmServiceMessenger().send(msg);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            mIsRegistered = false;
        } else {
            this.mTextView.setText("Service is not registered");
        }
    }


    void disconnectService() {

        if (this.mServiceConnection.getmServiceMessenger()!= null) {
            mClientContext.unbindService(mServiceConnection);
            this.mServiceConnection.setmServiceMessenger(null);
            mIsRegistered = false;
        } else {
            this.mTextView.setText("Service is not connected");
        }
    }

}
