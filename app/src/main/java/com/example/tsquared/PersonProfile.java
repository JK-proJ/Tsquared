package com.example.tsquared;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;


public class PersonProfile extends AppCompatActivity {

    private TextView name;
    private TextView college;
    private TextView desc;
    private TextView userQuestions;
    private TextView userAnswers;
    private TextView email;
    private TextView follow;

    private Toolbar toolbar;
    private String firstName;

    RequestParams params;
    AsyncHttpClient client;
    String URL = "http://207.237.59.117:8080/TSquared/platform?todo=addToFollowing";

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile);
        setViews();
        loadViews();
        setUpToolBar();
    }

    private void setViews(){
        name    = findViewById(R.id.profilePersonName);
        college = findViewById(R.id.profilePersonCollege);
        desc    = findViewById(R.id.profileDescription);
        email   = findViewById(R.id.profileEmail);
        userQuestions = findViewById(R.id.profilePersonQuestions);
        userAnswers   = findViewById(R.id.profilePersonAnswers);
        follow = findViewById(R.id.tv_follow);
    }

    private void loadViews(){
        Intent intent = getIntent();
        String profileName = intent.getStringExtra("name");
        // Extract the First Name of the user
        firstName = profileName.substring(0, profileName.indexOf(' '));

        // User is studying/ has studied at what institution
        String profileCollege   = intent.getStringExtra("college");
        String studyingCollege  = "Studying at " + profileCollege;

        String profileDesc = intent.getStringExtra("desc");
        String profileQuestions = firstName + "'s Questions";
        String profileAnswers   = firstName + "'s Answers";
        Log.e("User to follow data", "Name: " + profileName + "; College: " + profileCollege + "; Profile Description: " + profileDesc + " || " + intent.getStringExtra("email"));

        name.setText(profileName);
        college.setText(studyingCollege);
        desc.setText(profileDesc);
        userQuestions.setText(profileQuestions);
        userAnswers.setText(profileAnswers);

        // Obtaining the User's email address, not good programming practice
        //final String emailAddress = DrawerActivity.getEmail();
        email.setText(intent.getStringExtra("email"));
        follow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addToFollowing(email.getText().toString());
            }
        });
    }

    private void setUpToolBar(){
        toolbar = findViewById(R.id.personProfileToolbar);
        toolbar.setTitle(firstName);
        //toolbar.setTitleTextColor();
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void addToFollowing(String profileEmail){
        params = new RequestParams();
        params.put("currentUserEmail", DrawerActivity.getEmail());
        Log.e("DATA to Add Following", DrawerActivity.getEmail() +" follows "+ profileEmail);
        client = new AsyncHttpClient();

        client.get(URL, params, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response){
                Log.e("Adding Following", DrawerActivity.getEmail() +" follows another user");
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.e("TAG", "Failed to add user to follow");
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.i("ws", "---->>onFailure" + throwable.toString());
            }
        });
    }
}