package com.ttl.coffeemanagement.onboarding;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.ttl.coffeemanagement.login.LoginActivity;
import com.ttl.coffeemanagement.R;

public class OnboardingActivity extends AppCompatActivity {

    private ImageButton button;
    private ImageView arrow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        button = findViewById(R.id.button);
        arrow = findViewById(R.id.arrow);
        View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OnboardingActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        };
        button.setOnClickListener(clickListener);
        arrow.setOnClickListener(clickListener);
    }
}