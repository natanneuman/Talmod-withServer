package com.example.myapp.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.myapp.R;
import com.example.myapp.databinding.ActivitySignUpLoginBinding;
import com.example.myapp.network.RequestsManager;
import com.example.myapp.network.requests.SignUpRequest;
import com.example.myapp.network.responses.userResponses.UserResponse;
import com.example.myapp.utils.ManageSharedPreferences;

import static com.example.myapp.utils.UnitsSignUp.emailValidation;
import static com.example.myapp.utils.UnitsSignUp.passwordValidation;

public class SignUpLoginActivity extends AppCompatActivity {

    private ActivitySignUpLoginBinding binding;
    boolean isSignUp = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignUpLoginBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        if(ManageSharedPreferences.getToken( this)!= null){
            goToSplashActivity();
        }
        initListeners();
    }

    private void initListeners() {
        binding.logInTV.setOnClickListener(v -> {
            isSignUp = !isSignUp;
            binding.title.setText(isSignUp ? getString(R.string.sign_up) : getString(R.string.log_in));
            binding.alreadyHaveAccountLyTV.setText(isSignUp ? "already have account?" : "do not have an account?" );
            binding.logInTV.setText(isSignUp ? "log in" : getString(R.string.sign_up));
            binding.signUpBtn.setText(isSignUp ? "sign up" : "log in");
        });

        binding.signUpBtn.setOnClickListener(v -> {
            if(!emailValidation(binding.emailET.getText())){
                binding.emailET.setError("Oh, this is wrong");
            }else if(!passwordValidation(binding.passwordET.getText())){
                binding.passwordET.setError("Oh, this is wrong");
            }else {
                if (isSignUp) {
                    signUp(new SignUpRequest(binding.emailET.getText().toString(), binding.passwordET.getText().toString()));
                }else {
                    login(new SignUpRequest(binding.emailET.getText().toString(), binding.passwordET.getText().toString()));
                }
            }
        });
    }

    private void signUp(SignUpRequest signUpRequest) {
        binding.ASULProgressBarPB.setVisibility(View.VISIBLE);
        RequestsManager.getInstance().signUp(signUpRequest, new RequestsManager.OnRequestManagerResponseListener<UserResponse>() {
            @Override
            public void onRequestSucceed(UserResponse result) {
                saveToken(result);
                binding.ASULProgressBarPB.setVisibility(View.GONE);
                goToSplashActivity();
            }

            @Override
            public void onRequestFailed(String error) {
                binding.ASULProgressBarPB.setVisibility(View.GONE);
//                         log thet we cant
//                        finish app

            }
        });

    }

    private void login(SignUpRequest signUpRequest) {
        binding.ASULProgressBarPB.setVisibility(View.VISIBLE);
        RequestsManager.getInstance().login(signUpRequest, new RequestsManager.OnRequestManagerResponseListener<UserResponse>() {
            @Override
            public void onRequestSucceed(UserResponse result) {
                saveToken(result);
                binding.ASULProgressBarPB.setVisibility(View.GONE);
                goToSplashActivity();


            }

            @Override
            public void onRequestFailed(String error) {
                binding.ASULProgressBarPB.setVisibility(View.GONE);
//                         log thet we cant
//                        finish app

            }
        });

    }

    private void goToSplashActivity() {
        startActivity(new Intent(this, SplashActivity.class));
        finish();
    }

    private void saveToken(UserResponse userResponse) {
        ManageSharedPreferences.setToken(userResponse, this);
    }


}