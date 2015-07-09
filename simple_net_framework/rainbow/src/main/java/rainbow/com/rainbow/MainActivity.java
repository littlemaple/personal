package rainbow.com.rainbow;

import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import rainbow.com.rainbow.bean.Account;
import rainbow.com.rainbow.bean.AccountProxy;
import rainbow.com.simple_net_framework.base.Request;
import rainbow.com.simple_net_framework.base.Response;
import rainbow.com.rainbow.network.NetworkClient;


public class MainActivity extends ActionBarActivity {

    private TextView tv;
    private Account account = AccountProxy.getInstance().getCurrent();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv = (TextView) findViewById(R.id.textView);

    }

    public void doLogin(View view) {
        NetworkClient.doLogin(account.getUserName(), account.getPassword(), new Request.IRequestListener<JSONObject>() {
            @Override
            public void onComplete(int statusCode, JSONObject result, Response response) {
                Account.parse(account, result);
                tv.setText(">>>\n" + statusCode + "\nresult:" + result + "\n");
                tv.append(">>>\n\n\ntarget:" + account.getUserName() + ",password:" + account.getPassword() + ",access_token:" + account.getAccessToken());
            }
        });

    }

    public void doPut(View view) {
        File file = new File(Environment.getDownloadCacheDirectory() + "tmp.md");
        if (!file.exists()) {
            try {
                file.createNewFile();
                FileWriter fw = new FileWriter(file);
                fw.append(account.toString());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        NetworkClient.doUploadFile(account.getAccessToken(), file, 123,
                new Request.IRequestListener() {

                    @Override
                    public void onComplete(int statusCode, Object result, Response response) {
                        tv.append("\n\n\n");
                        tv.append("" + result.toString());
                    }
                });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
