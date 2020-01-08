package com.example.tsquared;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

import cz.msebera.android.httpclient.Header;

import static com.facebook.FacebookSdk.getApplicationContext;
import static java.util.Objects.requireNonNull;


public class PersonProfile extends AppCompatActivity implements View.OnClickListener {

    private TextView name;
    private TextView college;
    private TextView desc;
    private TextView userQuestions;
    private TextView userAnswers;
    private TextView email;
    private TextView follow;
    private TextView following;
    private TextView followers;

    private Toolbar toolbar;
    private String firstName;

    static RequestParams params;
    static AsyncHttpClient client;
    static String URL = "http://207.237.59.117:8080/TSquared/platform?todo=addToFollowing";
    String URL1 = "http://207.237.59.117:8080/TSquared/platform?todo=findUserQ";
    String URL2 = "http://207.237.59.117:8080/TSquared/platform?todo=findUserA";
    String URL3 = "http://207.237.59.117:8080/TSquared/platform?todo=findFollowing";
    String URL4 = "http://207.237.59.117:8080/TSquared/platform?todo=findFollowers";

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
        following = findViewById(R.id.profilePersonFollowing);
        followers = findViewById(R.id.profilePersonFollowers);

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

        String profileEmail = intent.getStringExtra("email");

        name.setText(profileName);
        college.setText(studyingCollege);
        desc.setText(profileDesc);
        userQuestions.setText(profileQuestions);
        userQuestions.setOnClickListener(this);
        userAnswers.setText(profileAnswers);
        userAnswers.setOnClickListener(this);
        email.setText(profileEmail);
        follow.setOnClickListener(this);
        following.setOnClickListener(this);
        followers.setOnClickListener(this);
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_follow:
                addToFollowing(email.getText().toString());
                break;
            case R.id.profilePersonQuestions:
                getUserQuestions(email.getText().toString());
                break;
            case R.id.profilePersonAnswers:
                getUserAnswers(email.getText().toString());
                break;
            case R.id.profilePersonFollowing:
                getUserFollowing(email.getText().toString());
                break;
            case R.id.profilePersonFollowers:
                getUserFollowers(email.getText().toString());
                break;
        }
    }

    public static void addToFollowing(String profileEmail){
        params = new RequestParams();
        params.put("currentUserEmail", DrawerActivity.getEmail());
        params.put("userToFollow", profileEmail);
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

    private void getUserQuestions(String profileEmail){
        params = new RequestParams();
        params.put("email", profileEmail);
        client = new AsyncHttpClient();

        client.get(URL1, params, new JsonHttpResponseHandler(){
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                Log.e("QUESTIONS: ", response.toString());
                ArrayList<QuestionItemModel> questionList = new ArrayList<>();
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject object = response.getJSONObject(i);
                        QuestionItemModel question = QuestionItemModel.fromJson(object);
                        Drawable image = ContextCompat.getDrawable(requireNonNull(getApplicationContext()), R.drawable.blank_profile);
                        question.setProfileImage(image);
                        questionList.add(question);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.e("TAG", "Failed to load Questions");
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.i("ws", "---->>onFailure" + throwable.toString());
            }
        });
    }

    private void getUserAnswers(String profileEmail){
        params = new RequestParams();
        params.put("email", profileEmail);
        client = new AsyncHttpClient();

        client.get(URL2, params, new JsonHttpResponseHandler(){
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response){
                Log.e("ANSWERS: ", response.toString());
                ArrayList<AnswerModel> answerListList = new ArrayList<>();
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject object  = response.getJSONObject(i);
                        AnswerModel answer = AnswerModel.fromJson(object);
                        Drawable image     = ContextCompat.getDrawable(Objects.requireNonNull(getApplication()), R.drawable.blank_profile);
                        answer.setProfileImage(image);
                        answerListList.add(answer);
                        //Log.e("ANSWERS", object.toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.e("TAG", "Failed to load Answer");
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.i("ws", "---->>onFailure" + throwable.toString());
            }
        });
    }

    private void getUserFollowing(String profileEmail){
        params = new RequestParams();
        params.put("email", profileEmail);
        client = new AsyncHttpClient();

        client.get(URL3, params, new JsonHttpResponseHandler(){
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response){
                Log.e("FOLLOWING: ", response.toString());
                ArrayList<PeopleItemModel> peopleList = new ArrayList<>();
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject object = response.getJSONObject(i);
                        PeopleItemModel people = PeopleItemModel.fromJson(object);
                        Drawable image = ContextCompat.getDrawable(getApplicationContext(), R.drawable.blank_profile);
                        people.setProfileImage(image);
                        peopleList.add(people);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.e("TAG", "Failed to load Following");
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.i("ws", "---->>onFailure" + throwable.toString());
            }
        });
    }

    private void getUserFollowers(String profileEmail){
        params = new RequestParams();
        params.put("email", profileEmail);
        client = new AsyncHttpClient();

        client.get(URL4, params, new JsonHttpResponseHandler(){
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response){
                Log.e("FOLLOWERS: ", response.toString());
                ArrayList<PeopleItemModel> peopleList = new ArrayList<>();
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject object = response.getJSONObject(i);
                        PeopleItemModel people = PeopleItemModel.fromJson(object);
                        Drawable image = ContextCompat.getDrawable(getApplicationContext(), R.drawable.blank_profile);
                        people.setProfileImage(image);
                        peopleList.add(people);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.e("TAG", "Failed to load Followers");
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.i("ws", "---->>onFailure" + throwable.toString());
            }
        });
    }
}