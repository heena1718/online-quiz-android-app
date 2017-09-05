package com.heenasharma.playndwin;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

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

public class Signup extends Activity {

    EditText f_name, l_name, pass, c_pass, email, c_no;
    Button register;
    String name1,name2,pas,c_pas,em,contact;
    String line=null;
    String result=null;
    InputStream is=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        f_name = (EditText) findViewById(R.id.f_name);
        l_name = (EditText) findViewById(R.id.l_name);
        email = (EditText) findViewById(R.id.email);
        pass = (EditText) findViewById(R.id.pwd);
        c_pass = (EditText) findViewById(R.id.c_pwd);
        c_no = (EditText) findViewById(R.id.c_no);
        register = (Button) findViewById(R.id.reg);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                name1 = f_name.getText().toString();
                name2 = l_name.getText().toString();
                pas = pass.getText().toString();
                c_pas = c_pass.getText().toString();
                em = email.getText().toString();
                contact = c_no.getText().toString();

                if (name1.length() > 0)
                {
                    f_name.setError(null);
                    if(name2.length() > 0 )
                    {
                        l_name.setError(null);
                        if(pas.length() >0)
                        {
                            pass.setError(null);
                            if(c_pas.length() >0)
                            {
                                c_pass.setError(null);
                                if (pas.equals(c_pas))
                                {
                                    if (contact.length() !=0) {
                                        c_no.setError(null);
                                        if (contact.length() == 10)
                                        {
                                            c_no.setError(null);
                                            new Register().execute("");
                                        } else
                                        {
                                            c_no.setFocusable(true);
                                            c_no.setError("Invalid Number");

                                        }
                                    }
                                    else
                                    {
                                        c_no.setFocusable(true);
                                        c_no.setError("Required Feild");
                                    }
                                }
                                else
                                {
                                    c_no.setFocusable(true);
                                    c_no.setError("Password donot match");
                                }
                            }
                            else
                            {
                                c_pass.setFocusable(true);
                                c_pass.setError("Required Feild");
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
                        l_name.setFocusable(true);
                        l_name.setError("Required Feild");
                    }
                }
                else
                {
                    f_name.setFocusable(true);
                    f_name.setError("Required FEild");
                }

            }
        });
    }

    class Register extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... strings)
        {
            ArrayList<NameValuePair> values = new ArrayList<NameValuePair>();
            values.add(new BasicNameValuePair("f_name", name1));
            values.add(new BasicNameValuePair("l_name", name2));
            values.add(new BasicNameValuePair("email", em));
            values.add(new BasicNameValuePair("pass", pas));
            values.add(new BasicNameValuePair("contact", contact));

            try {
                DefaultHttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost(Config.ip+"api/register.php");
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
                if(obj.getString("status").equalsIgnoreCase("true"))
                {
                    Intent i = new Intent(getApplication(),MainActivity.class);
                    startActivity(i);

                }
            }
            catch (Exception e) {
                Log.i("tag", e.toString());
            }

            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }
    }
}