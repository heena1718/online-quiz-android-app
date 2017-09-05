package com.heenasharma.playndwin;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class Wallet extends AppCompatActivity {

    String id;
    String line=null;
    String result=null;
    InputStream is=null;
    String wallet;
    TextView points;
    Button redeem;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet);

        points = (TextView) findViewById(R.id.points);
        redeem = (Button) findViewById(R.id.redeem);

        redeem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int x = Integer.parseInt(wallet);
                if(x<50)
                {
                    Toast.makeText(Wallet.this, "Insufficient points.. ", Toast.LENGTH_SHORT).show();
                }
            }
        });

        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);

        id = pref.getString("id","");

        new Wall().execute("");
        Toast.makeText(this, "Wallet "+wallet, Toast.LENGTH_SHORT).show();

        points.setText(wallet);




    }
    class Wall extends AsyncTask<String,String,String>
    {

        @Override
        protected String doInBackground(String... strings) {

            ArrayList<NameValuePair> values = new ArrayList<NameValuePair>();
            values.add(new BasicNameValuePair("id", id));
            try {
                DefaultHttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost(Config.ip+"wallet.php");
                httpPost.setEntity(new UrlEncodedFormEntity(values));
                HttpResponse Response = httpClient.execute(httpPost);
                HttpEntity entity = Response.getEntity();
                is = entity.getContent();
                Log.i("Tag", "Connection successful");
            } catch (Exception e) {
                Log.i("Tag", "not connected");
            }
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"), 8);
                StringBuilder sb = new StringBuilder();
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }
                is.close();
                result = sb.toString();
                Log.i("TAG", "result retrived " + result);
            } catch (Exception e) {
                Log.i("Tag", "result not retrived" + e.toString());

            }
            try {
                JSONObject obj = new JSONObject(result);
                wallet = obj.getString("wallet");
            }
            catch(Exception e)
            {

            }
            return null;
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.detail,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch(item.getItemId())
        {
            case R.id.log:
            {
                SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit();

                editor.putString("login", null);

                editor.commit();
                Intent i = new Intent(getApplication(),MainActivity.class);
                startActivity(i);
                finish();
                break;
            }
            case R.id.wallet:
            {
                Intent i = new Intent(getApplication(),Wallet.class);
                startActivity(i);
                break;
            }
        }
        return true;
    }
}
