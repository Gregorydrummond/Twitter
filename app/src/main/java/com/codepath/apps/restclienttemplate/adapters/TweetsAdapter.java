package com.codepath.apps.restclienttemplate.adapters;

import android.content.Context;
import android.graphics.drawable.Icon;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.codepath.apps.restclienttemplate.R;
import com.codepath.apps.restclienttemplate.TwitterApp;
import com.codepath.apps.restclienttemplate.TwitterClient;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import okhttp3.Headers;

public class TweetsAdapter extends RecyclerView.Adapter<TweetsAdapter.ViewHolder> {

    public static final String TAG = "TweetsAdapter";

    Context context ;
    List<Tweet> tweets;
    TwitterClient client;


    //Pass in the context and list of tweets
    public TweetsAdapter(Context context, List<Tweet> tweets) {
        this.context = context;
        this.tweets = tweets;
        client = TwitterApp.getRestClient(context);
    }

    //For each row, inflate the layout
    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_tweet, parent, false);
        return new ViewHolder(view);
    }

    //Bind values based on the position of the element
    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
        //Get the data at the position
        Tweet tweet = tweets.get(position);

        //Bind the tweet with the view holder
        holder.bind(tweet);

    }

    @Override
    public int getItemCount() {
        return tweets.size();
    }

    //Clear all elements of the recycler
    public void clear() {
        tweets.clear();
        notifyDataSetChanged();
    }

    //Add a list of items
    public void addAll(List<Tweet> list) {
        tweets.addAll(list);
        notifyDataSetChanged();
    }

    //Define a viewholder
    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView ivProfileImage;
        ImageView ivTweetImage;
        ImageView ivLike;
        ImageView ivRetweet;
        ImageView ivComment;
        TextView tvName;
        TextView tvScreenName;
        TextView tvTweetBody;
        TextView tvTimeAgo;

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            ivProfileImage = itemView.findViewById(R.id.ivProfileImage);
            tvScreenName = itemView.findViewById(R.id.tvScreenName);
            tvName = itemView.findViewById(R.id.tvName);
            tvTweetBody = itemView.findViewById(R.id.tvTweetBody);
            tvTimeAgo = itemView.findViewById(R.id.tvTimeAgo);
            ivTweetImage = itemView.findViewById(R.id.ivTweetImage);
            ivLike = itemView.findViewById(R.id.ivLike);
            ivRetweet = itemView.findViewById(R.id.ivRetweet);
            ivComment = itemView.findViewById(R.id.ivComment);
        }

        public void bind(final Tweet tweet) {
            tvTweetBody.setText(tweet.body);
            tvScreenName.setText("@" + tweet.user.screenName);
            tvName.setText(tweet.user.name);
            tvTimeAgo.setText(tweet.relativeTimeAgo);
            //Profile image
            Glide.with(context)
                    .load(tweet.user.profileImageUrl)
                    .transform(new CircleCrop())
                    .into(ivProfileImage);
            //Tweet media
            if(tweet.imageURL != null) {
                Glide.with(context)
                        .load(tweet.imageURL)
//                        .override(900, 1400)
                        .into(ivTweetImage);
            }
            else {
                ivTweetImage.setMinimumWidth(0);
                ivTweetImage.setMinimumHeight(0);
            }
            //Like button image
            if(tweet.isLiked) {
                ivLike.setBackgroundResource(R.drawable.ic_vector_heart);
            }
            else {
                ivLike.setBackgroundResource(R.drawable.ic_vector_heart_stroke);
            }

            //Retweet button image
            if(tweet.isRetweeted) {
                ivRetweet.setBackgroundResource(R.drawable.ic_vector_retweet);
            }
            else {
                ivRetweet.setBackgroundResource(R.drawable.ic_vector_retweet_stroke);
            }

            //Liking/unliking tweet
            ivLike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(!tweet.isLiked) {
                        client.likeTweet(tweet.tweetID, new JsonHttpResponseHandler() {
                            @Override
                            public void onSuccess(int statusCode, Headers headers, JSON json) {
                                Log.i(TAG, "Tweet liked!");
                                ivLike.setBackgroundResource(R.drawable.ic_vector_heart);
                                tweet.isLiked = true;
                            }

                            @Override
                            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                                Log.i(TAG, "Error: " + response, throwable);
                            }
                        });
                    }
                    else {
                        client.unlikeTweet(tweet.tweetID, new JsonHttpResponseHandler() {
                            @Override
                            public void onSuccess(int statusCode, Headers headers, JSON json) {
                                Log.i(TAG, "Tweet unliked!");
                                ivLike.setBackgroundResource(R.drawable.ic_vector_heart_stroke);
                                tweet.isLiked = false;
                            }

                            @Override
                            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                                Log.i(TAG, "Error: " + response, throwable);
                            }
                        });
                    }
                }
            });

            //Retweet/Unretweet
            ivRetweet.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(!tweet.isRetweeted) {
                        client.retweetTweet(tweet.tweetID, new JsonHttpResponseHandler() {
                            @Override
                            public void onSuccess(int statusCode, Headers headers, JSON json) {
                                Log.i(TAG, "Tweet retweeted!");
                                ivRetweet.setBackgroundResource(R.drawable.ic_vector_retweet);
                                tweet.isRetweeted = true;
                            }

                            @Override
                            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                                Log.i(TAG, "Error: " + response, throwable);
                            }
                        });
                    }
                    else {
                        client.unretweetTweet(tweet.tweetID, new JsonHttpResponseHandler() {
                            @Override
                            public void onSuccess(int statusCode, Headers headers, JSON json) {
                                Log.i(TAG, "Tweet unretweeted!");
                                ivRetweet.setBackgroundResource(R.drawable.ic_vector_retweet_stroke);
                                tweet.isRetweeted = false;
                            }

                            @Override
                            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                                Log.i(TAG, "Error: " + response, throwable);
                            }
                        });
                    }
                }
            });
        }
    }
}
