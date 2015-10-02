package co.celloscope.ocrclientdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class OCRClientActivity extends AppCompatActivity {

    TextView mCallbackText;
    private OCRClient mOcrClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client);
        mCallbackText = (TextView) findViewById(R.id.callbackTextView);
        mOcrClient = new OCRClient(this, mCallbackText);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_client, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        switch (item.getItemId()) {
            case R.id.action_bind:
                mOcrClient.doBindService();
                return true;
            case R.id.action_unbind:
                mOcrClient.doUnbindService();
                return true;
            case R.id.action_ocr:
                mOcrClient.doOcr();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


}
