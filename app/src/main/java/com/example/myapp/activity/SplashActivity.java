package com.example.myapp.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import com.example.model.Profile;
import com.example.myapp.R;
import com.example.myapp.fragment.DeleteStudyFragment;
import com.example.myapp.repository.Repository;
import com.example.myapp.utils.Language;
import com.example.myapp.utils.ManageSharedPreferences;

import static com.example.myapp.fragment.DeleteStudyFragment.IS_AFTER_DELETE_STUDY;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        if (getIntent().getExtras() != null && getIntent().getExtras().getBoolean(IS_AFTER_DELETE_STUDY)){
            if (Repository.getInstance(getApplication(), null).getAllLearning().getValue().size()>0){
                startActivity(new Intent(SplashActivity.this, MainActivity.class));
            }else {
                startActivity(new Intent(SplashActivity.this, ProfileActivity.class));

            }
            finish();
        }

        setLanguage();
        startNextActivity();
    }



    private void setLanguage() {
        Profile mProfile = ManageSharedPreferences.getProfile(getBaseContext());
        if (mProfile == null) {
            return;
        }
        Language.setAppLanguage(mProfile.getLanguage(), getBaseContext());
    }

//    private void startNextActivity() {
//        if (isFirstTime()) {
//            startActivity(new Intent(SplashActivity.this, ProfileActivity.class));
//        } else {
//            startActivity(new Intent(SplashActivity.this, MainActivity.class));
//        }
//        finish();
//    }

    private void startNextActivity() {
         Repository.getInstance(getApplication(),isHaveStudyPlans ->{
            if (isHaveStudyPlans){
                startActivity(new Intent(SplashActivity.this, ProfileActivity.class));
            }else {
                startActivity(new Intent(SplashActivity.this, MainActivity.class));
            }
            finish();
        });
    }
}