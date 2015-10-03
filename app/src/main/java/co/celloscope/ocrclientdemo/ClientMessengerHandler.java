package co.celloscope.ocrclientdemo;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.TextView;

public class ClientMessengerHandler extends Handler {
    private final TextView mCallbackText;

    public ClientMessengerHandler(TextView mCallbackText) {
        this.mCallbackText = mCallbackText;
    }

    @Override
    public void handleMessage(Message msg) {
        Bundle mBundle = (Bundle) msg.obj;
        switch (msg.what) {
            case ServiceOperations.MSG_DO_OCR:
                mCallbackText.setText("OCRService: " + mBundle.getString("text"));
                break;
            case ServiceOperations.MSG_OCR_RESULT:
                mCallbackText.setText("OCRService: " + mBundle.getString("text"));
                break;
            case ServiceOperations.MSG_REGISTER_CLIENT:
                mCallbackText.setText("OCRService: " + mBundle.getString("text"));
                break;
            case ServiceOperations.MSG_UNREGISTER_CLIENT:
                mCallbackText.setText("OCRService: " + mBundle.getString("text"));
                break;

            default:
                super.handleMessage(msg);
        }
    }
}
