package com.kathryniagodkin.seeyourpopularity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    private LoginButton loginBtn;
    private CallbackManager callbackManager;
 //    private ImageView imageViewPost;
//    private TextView textViewPost;
    RecyclerView recyclerView;
    PostsAdapter postsAdapter;
    RecyclerView.LayoutManager layoutManager;
    String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loginBtn = findViewById(R.id.login_button);
        recyclerView = findViewById(R.id.rv_posts_list);

        callbackManager = CallbackManager.Factory.create();

        loginBtn.setPermissions(Arrays.asList("pages_read_engagement", "pages_show_list"));

        loginBtn.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d("MY_TAG", "Login Successful!");
            }

            @Override
            public void onCancel() {
                Log.d("MY_TAG", "Login Canceled");
            }

            @Override
            public void onError(FacebookException error) {
                Log.d("MY_TAG", "Login Error");
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);

        GraphRequest graphRequest = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(),
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        try {
                            id = object.getString("id");
                            Log.d("MY_TAG", "onCompleted: userID" + id);
                            ArrayList<FBPages> fbPages = new ArrayList<>();
                            GraphRequest graphRequestPages = GraphRequest.newGraphPathRequest(AccessToken.getCurrentAccessToken(),
                                    "/" + id + "/accounts",
                                    new GraphRequest.Callback() {
                                        @Override
                                        public void onCompleted(GraphResponse response) {
                                            Log.d("MY_TAG", "pagesOnCompleted: response" + response.toString());
                                            JSONArray objects;
                                            try {
                                                objects = response.getJSONObject().getJSONArray("data");
                                                for (int i = 0; i < objects.length(); i++) {
                                                    JSONObject object = objects.getJSONObject(i);
                                                    Log.d("MY_TAG", "pagesOnCompleted: objects.getJSONObject " + object.toString());
                                                    fbPages.add(new FBPages(object.getString("id"), object.getString("name")));
                                                    GraphRequest graphRequestPost = GraphRequest.newGraphPathRequest(AccessToken.getCurrentAccessToken(),
                                                            "/" + fbPages.get(i).getPage_id() + "/posts",
                                                            new GraphRequest.Callback() {

                                                                @Override
                                                                public void onCompleted(GraphResponse response) {
                                                                    Log.d("MY_TAG", "postOnCompleted: response" + response.toString());
                                                                    ArrayList<FBPost> fbPosts = new ArrayList<>();
                                                                    JSONArray objects;
                                                                    try {
                                                                        objects = response.getJSONObject().getJSONArray("data");
                                                                        for (int i = 0; i < objects.length(); i++) {
                                                                            JSONObject object = objects.getJSONObject(i);
                                                                            Log.d("MY_TAG", "onCompleted: objects.getJSONObject" + object.toString());
                                                                            fbPosts.add(new FBPost(object.getString("id"), object.getString("full_picture"), object.getBoolean("is_popular")));
                                                                        }

                                                                    } catch (JSONException e) {
                                                                        e.printStackTrace();
                                                                    }

                                                                    layoutManager = new LinearLayoutManager(MainActivity.this);
                                                                    recyclerView.setLayoutManager(layoutManager);

                                                                    ArrayList<FBPost> fbPostsPopular = new ArrayList<>();
                                                                    for (int j = 0; j < fbPosts.size(); j++) {
                                                                        if (fbPosts.get(j).getPopular()) {
                                                                            fbPostsPopular.add(fbPosts.get(j));
                                                                        }
                                                                    }

                                                                    if (fbPostsPopular != null) {
                                                                        Toast.makeText(MainActivity.this, "You don't have any popular posts yet", Toast.LENGTH_LONG).show();
                                                                    } else {
                                                                        postsAdapter = new PostsAdapter(fbPostsPopular);
                                                                    }   recyclerView.setAdapter(postsAdapter);
                                                                }
                                                            });

                                                    Bundle parameters = new Bundle();
                                                    parameters.putString("fields", "full_picture,message,is_popular");
                                                    graphRequestPost.setParameters(parameters);
                                                    graphRequestPost.executeAsync();
                                                }
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }

                                        }
                                    });
                            graphRequestPages.executeAsync();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });

        Bundle bundle = new Bundle();
        bundle.putString("fields", "id");

        graphRequest.setParameters(bundle);
        graphRequest.executeAsync();

    }

    AccessTokenTracker accessTokenTracker = new AccessTokenTracker() {
        @Override
        protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
            if (currentAccessToken == null) {
                if (postsAdapter != null) {
                     postsAdapter.clear();
                }
                LoginManager.getInstance().logOut();
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        accessTokenTracker.stopTracking();
    }
}