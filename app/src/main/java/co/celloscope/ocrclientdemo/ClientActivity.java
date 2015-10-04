package co.celloscope.ocrclientdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class ClientActivity extends AppCompatActivity {

    private ClientManager mOcrClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client);
        mOcrClient = new ClientManager(this, (TextView) findViewById(R.id.callbackTextView));
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
            case R.id.action_connect:
                mOcrClient.connectService();
                return true;
            case R.id.action_register:
                mOcrClient.registerClient();
                return true;
            case R.id.action_ocr:
                mOcrClient.doOcr();
                return true;
            case R.id.action_unregister:
                mOcrClient.unregisterClient();
                return true;
            case R.id.action_disconnect:
                mOcrClient.disconnectService();
            default:
                return super.onOptionsItemSelected(item);
        }
    }


}
