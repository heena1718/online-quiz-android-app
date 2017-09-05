package com.heenasharma.playndwin;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class Question extends AppCompatActivity {

    String c_id;
    String line = null;
    String result = null;
    InputStream is = null;
    String ques,optionA,optionB,optionC,optionD,ans;
    RadioGroup rg;
    RadioButton o1,o2,o3,o4;
    TextView quest;
    Button btn;
    int choose_ans;
     int counter=0;
    TextView start;
    int c_ans;
    CountDownTimer c ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);
        Bundle b = getIntent().getExtras();
        c_id = b.getString("c_id");
        Toast.makeText(this, "C_id = "+c_id, Toast.LENGTH_SHORT).show();
        rg = (RadioGroup) findViewById(R.id.rg);
        o1 = (RadioButton) findViewById(R.id.optiona);
        o2 = (RadioButton) findViewById(R.id.optionb);
        o3 = (RadioButton) findViewById(R.id.optionc);
        o4 = (RadioButton) findViewById(R.id.optiond);
        quest = (TextView) findViewById(R.id.ques);
        btn = (Button) findViewById(R.id.btn);
        start = (TextView) findViewById(R.id.start);
        new Ques().execute("");


        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if(i == R.id.optiona)
                {
                    choose_ans = 1;
                }
                else if(i == R.id.optionb)
                {
                    choose_ans = 2;
                }
                else if(i == R.id.optionc)
                {
                    choose_ans = 3;
                }
                else if(i == R.id.optiond)
                {
                    choose_ans = 4;
                }
                else
                {
                    choose_ans=0;
                }
            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                counter++;
//                Toast.makeText(Question.this, "count = "+counter, Toast.LENGTH_SHORT).show();
                int an = Integer.parseInt(ans);
                if(choose_ans == an)
                {
                    c_ans++;
                    Toast.makeText(Question.this, "Correct Answer", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(Question.this, "Wrong Answer", Toast.LENGTH_SHORT).show();
                }
                if(counter == 10)
                {
                    String s = Integer.toString(c_ans);
                    Intent i = new Intent(getApplication(),Score.class);
                    i.putExtra("score",s);
                    startActivity(i);
                    finish();

                }
                new Ques().execute("");
            }
        });


        c = new CountDownTimer(30000,1000)
        {
            @Override
            public void onTick(long l) {
                start.setText("Time Left: "+l/1000);
            }

            @Override
            public void onFinish() {
                start.setText("DONE !!");
                new Ques().execute("");
                start();
            }
        }.start();

//        Toast.makeText(this, "id recievied = "+c_id, Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onBackPressed()
    {
        String s = Integer.toString(c_ans);
        Intent i = new Intent(getApplication(),Score.class);
        i.putExtra("score",s);
        startActivity(i);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.quit,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.q)
        {
            Intent i = new Intent(getApplication(),Home.class);
            startActivity(i);
            finish();
        }
        return true;
    }

    class Ques extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... strings) {
            ArrayList<NameValuePair> values = new ArrayList<NameValuePair>();
            values.add(new BasicNameValuePair("c_id", c_id));

            try {
                DefaultHttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost(Config.ip+"fetch_ques.php");
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
                ques = obj.getString("ques");
                optionA = obj.getString("o1");
                optionB = obj.getString("o2");
                optionC = obj.getString("o3");
                optionD = obj.getString("o4");
                ans = obj.getString("ans");

            } catch (Exception e) {

            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            o1.setText(optionA);
            o2.setText(optionB);
            o3.setText(optionC);
            o4.setText(optionD);
            quest.setText(ques);
            c.start();
        }
    }
}