package com.heenasharma.playndwin;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

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
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class MainActivity extends Activity
{
    TextView signup;
    EditText email,pass;
    Button login;
    String e,p;
    String line=null;
    String result=null;
    InputStream is=null;
    String status,msg;
    String id,w_money,emi;
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dialog = new ProgressDialog(MainActivity.this);
        signup = (TextView) findViewById(R.id.signup);
        email = (EditText) findViewById(R.id.email);
        pass = (EditText) findViewById(R.id.pass);
        login = (Button) findViewById(R.id.login);

        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);

        String restoredText = pref.getString("login", null);
        if (restoredText != null) {
            if (restoredText.equals("yes")) {
                Intent myIntent = new Intent(MainActivity.this, Home.class);
                MainActivity.this.startActivity(myIntent);
                finish();
            }
        }

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                e = email.getText().toString();
                p = pass.getText().toString();
                if(e.length()>0)
                {

                    email.setError(null);
                    if(p.length()>0)
                    {

                        pass.setError(null);
                        if(emailValidator(e))
                        {

                            email.setError(null);
                            new Login().execute("");
                        }
                        else
                        {
                            email.setFocusable(true);
                            email.setError("Invalid Email");
                        }
                    }
                    else
                    {
                        pass.setFocusable(true);
                        pass.setError("Required Feild");
                    }
                }
                else
                {
                    email.setFocusable(true);
                    email.setError("Required Feild");
                }
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplication(), Signup.class);
                startActivity(i);
            }
        });
    }

    public boolean emailValidator(String email)
    {
        Pattern pattern;
        Matcher matcher;
        final String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(email);
        return matcher.matches();
    }

    class Login extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... strings) {
            ArrayList<NameValuePair> values = new ArrayList<NameValuePair>();
            values.add(new BasicNameValuePair("email", e));
            values.add(new BasicNameValuePair("pass", p));


            try {
                DefaultHttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost(Config.ip+"api/login.php");
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

                if (obj.getString("status").equalsIgnoreCase("true")) {
                    w_money = obj.getString("wallet");
                    id = obj.getString("id");
                    emi = obj.getString("email");

                    SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
                    SharedPreferences.Editor editor = pref.edit();

                    editor.putString("login", "yes");
                    editor.putString("id", id);
                    editor.putString("email", emi);
                    editor.putString("wallet", w_money);
                    editor.commit();

                    Intent i = new Intent(getApplication(), Home.class);
                    startActivity(i);
                    finish();
                } else {
                    status = obj.getString("status");
                    msg = obj.getString("msg");

                    Log.i("TAG",status+" "+msg);

                }
            } catch (Exception e) {
                Log.i("tag", e.toString());
            }
            return null;
        }

        @Override
        protected void onPreExecute()
        {
            dialog.setMessage("please wait...");

            dialog.show();
        }

        @Override
        protected void onPostExecute(String s)
        {
            if (dialog.isShowing())
            {
                dialog.dismiss();
            }
         }
    }
}