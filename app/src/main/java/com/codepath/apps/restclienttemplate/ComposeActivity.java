package com.codepath.apps.restclienttemplate;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;

import org.json.JSONException;
import org.parceler.Parcels;

import okhttp3.Headers;

public class ComposeActivity extends AppCompatActivity {

    public static final int MAX_TWEET_LENGTH = 140;
    public static final String TAG = "ComposeActivity";

    TwitterClient client;

    EditText etCompose;
    Button btnTweet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose);

        //Get twitter client
        client = TwitterApp.getRestClient(this);

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
                //Toast.makeText(ComposeActivity.this, "Good length tweet!", Toast.LENGTH_LONG).show();

                //API call to Twitter to publish the tweet
                client.publishTweet(tweetContent, new JsonHttpResponseHandler() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onSuccess(int statusCode, Headers headers, JSON json) {
                        Log.i(TAG, "onSuccess to publish tweet.");
                        try {
                            Tweet tweet = Tweet.fromJson(json.jsonObject);
                            Log.i(TAG, "Published Tweet: " + tweet.body);

                            //Create intent to send data back to the parent
                            Intent intent = new Intent();

                            //Wrap tweet with Parceler
                            intent.putExtra("tweet", Parcels.wrap(tweet));

                            //Set result code and bundle data for response
                            setResult(RESULT_OK, intent);

                            //Close activity
                            finish();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                        Log.e(TAG, "onFailure to publish tweet, " + response, throwable);
                    }
                });
            }
        });

    }
}