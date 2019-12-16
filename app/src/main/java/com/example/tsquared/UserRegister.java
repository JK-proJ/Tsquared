package com.example.tsquared;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class UserRegister extends AppCompatActivity implements View.OnClickListener{

    private EditText emailRegister;
    private EditText passwordRegister;
    private EditText firstNameRegister;
    private EditText lastNameRegister;
    private EditText collegeRegister;
    private EditText descriptionRegister;
    private Button registerButton;
    private TextView loginText;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_register);
        setViews();
        setListeners();
    }
    
    private void setListeners() {
        registerButton.setOnClickListener(this);
        loginText.setOnClickListener(this);
    }
    
    private void setViews() {
        emailRegister = (EditText)findViewById(R.id.emailRegister);
        passwordRegister = (EditText) findViewById(R.id.passwordRegister);
        firstNameRegister = (EditText) findViewById(R.id.firstName);
        lastNameRegister = (EditText) findViewById(R.id.lastName);
        collegeRegister = (EditText) findViewById(R.id.college);
        descriptionRegister = (EditText) findViewById(R.id.description);
        registerButton = (Button)findViewById(R.id.reg_button);
        loginText   = (TextView) findViewById(R.id.reg_link);
    }
    
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.reg_button:

                String emailLoginString = emailRegister.getText().toString().trim();
                String passwordFormString = passwordRegister.getText().toString().trim();
                String firstNameString = firstNameRegister.getText().toString().trim();
                String lastNameString = lastNameRegister.getText().toString();
                String collegeRegisterString = collegeRegister.getText().toString();
                String descriptionRegisterString = descriptionRegister.getText().toString();

                ServerSendRequest request = new ServerSendRequest();
                request.registration(emailLoginString,passwordFormString, firstNameString, lastNameString, collegeRegisterString, descriptionRegisterString);
                break;
            case R.id.reg_link:
                Intent login = new Intent(UserRegister.this, LoginActivity.class);
                startActivity(login);
                break;
        }
    }
}
