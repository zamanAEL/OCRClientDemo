package co.celloscope.ocrclientdemo;

import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ClientActivity extends AppCompatActivity {

    private ClientManager mOcrClient;
    private final String testFilePath = Environment.getExternalStorageDirectory()
            + "/ocr.jpg";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client);
        mOcrClient = new ClientManager(this, new Output() {
            @Override
            public void show(String result) {
                ((TextView) findViewById(R.id.callbackTextView)).setText(result);
            }
        });
        mOcrClient.connectService();
        ((Button)this.findViewById(R.id.ocrButton)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mOcrClient.doOcr(testFilePath);
//                mOcrClient.disconnectService();
            }
        });

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
                //mOcrClient.registerClient();
                return true;
            case R.id.action_ocr:
                mOcrClient.doOcr(testFilePath);
                return true;
            case R.id.action_unregister:
                //mOcrClient.unregisterClient();
                return true;
            case R.id.action_disconnect:
                mOcrClient.disconnectService();
            default:
                return super.onOptionsItemSelected(item);
        }
    }


}
