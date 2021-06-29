package com.codepath.apps.restclienttemplate;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ComposeActivity extends AppCompatActivity {

    public static final int MAX_TWEET_LENGTH = 140;

    EditText etCompose;
    Button btnTweet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose);

        etCompose = findViewById(R.id.etCompose);
        btnTweet = findViewById(R.id.btnTweet);

        //Set click listener on button
        btnTweet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tweetContent = etCompose.getText().toString();
                //Empty tweet
                if(tweetContent.isEmpty()) {
                    Toast.makeText(ComposeActivity.this, "Tweet is empty!", Toast.LENGTH_LONG).show();
                    return;
                }
                //Tweet is too long
                if(tweetContent.length() > MAX_TWEET_LENGTH) {
                    Toast.makeText(ComposeActivity.this, "Tweet is too long!", Toast.LENGTH_LONG).show();
                    return;
                }
                //Normal tweet
                Toast.makeText(ComposeActivity.this, "Good length tweet!", Toast.LENGTH_LONG).show();
                //API call to Twitter to publish the tweet
            }
        });

    }
}