
package co.celloscope.ocrclientdemo;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.widget.TextView;
import android.widget.Toast;


public class ClientManager implements ServiceConnectionStatus {

    private final Context context;
    boolean isRegistered;
    private final OCRServiceConnection connection;


    private final Messenger cMessenger;

    public ClientManager(Context context, final Output output) {
        this.context = context;
        connection = new OCRServiceConnection(this);
        cMessenger = new Messenger(new Handler() {
            @Override
            public void handleMessage(Message msg) {
                Bundle mBundle = ((Bundle) msg.obj);
                switch (msg.what) {

                    case ServiceOperations.MSG_REGISTER_CLIENT:
                        output.show("OCRService: " + mBundle.getString("text"));
                        isRegistered = true;
                        break;
                    case ServiceOperations.MSG_DO_OCR:
                        output.show("OCRService: " + mBundle.getString("text"));
                        break;
                    case ServiceOperations.MSG_OCR_RESULT:
                        output.show("OCRService: " + mBundle.getString("text"));
                        break;
                    case ServiceOperations.MSG_UNREGISTER_CLIENT:
                        output.show("OCRService: " + mBundle.getString("text"));
                        isRegistered = false;
                        break;

                    default:
                        super.handleMessage(msg);
                }
            }
        });
    }

    void connectService() {
        Intent intent = new Intent();
        String svcPackageName = context.getResources().getString(R.string.svcPackageName);
        String svcFQClassName = context.getResources().getString(R.string.svcFQClassName);
        intent.setComponent(new ComponentName(svcPackageName, svcFQClassName));
        context.bindService(intent, connection, Context.BIND_AUTO_CREATE);
    }

    private void registerClient() {
        if (this.connection.isConnected()) {
            this.sendMessage(ServiceOperations.MSG_REGISTER_CLIENT, null);
        } else {
            Toast.makeText(context, "Service is not connected", Toast.LENGTH_LONG).show();
        }
    }

    void doOcr(String filePath) {
        if (this.connection.isConnected() && isRegistered) {
            this.sendMessage(ServiceOperations.MSG_DO_OCR, filePath);
        } else {
            Toast.makeText(context, "Service is not connected or registered", Toast.LENGTH_SHORT).show();
        }
    }


    private void unregisterClient() {
        if (this.connection.isConnected() && isRegistered) {
            this.sendMessage(ServiceOperations.MSG_UNREGISTER_CLIENT, null);
        } else {
            Toast.makeText(context, "Service is not connected or registered", Toast.LENGTH_SHORT).show();
        }
    }

    private void sendMessage(Integer what, String data) {
        try {
            Message msg;
            if (what == ServiceOperations.MSG_REGISTER_CLIENT || what == ServiceOperations.MSG_UNREGISTER_CLIENT) {
                msg = Message.obtain(null, what);
                msg.replyTo = cMessenger;

            } else {
                Bundle mBundle = new Bundle();
                mBundle.putString("text", data);
                msg = Message.obtain(null, what, mBundle);
            }
            this.connection.getMessenger().send(msg);
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    void disconnectService() {

        if (this.connection.isConnected()) {
            if (this.isRegistered) {
                this.unregisterClient();
            }
            context.unbindService(connection);
            this.connection.disconnect();
        } else {
            Toast.makeText(context, "Service already disconnected", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onServiceConnected() {
        this.registerClient();
    }

    @Override
    public void onServiceDisConnected() {

    }
}