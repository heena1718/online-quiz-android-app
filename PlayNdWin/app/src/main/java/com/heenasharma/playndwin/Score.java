package com.heenasharma.playndwin;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

public class Score extends AppCompatActivity {

    String email;
    TextView txt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);
        Bundle b = getIntent().getExtras();
        String sc = b.getString("score");
        TextView score = (TextView) findViewById(R.id.sco);
        score.setText("Total Score = "+sc);

        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);

        email = pref.getString("email","");

        Toast.makeText(this, email, Toast.LENGTH_SHORT).show();
        txt= (TextView) findViewById(R.id.em);
        txt.setText(email);

    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(getApplication(),Home.class);
        startActivity(i);
        finish();
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
