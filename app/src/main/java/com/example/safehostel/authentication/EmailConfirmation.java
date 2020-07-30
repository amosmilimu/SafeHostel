package com.example.safehostel.authentication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.safehostel.R;
import com.example.safehostel.databinding.ActivityEmailConfirmationBinding;

public class EmailConfirmation extends AppCompatActivity {

    ActivityEmailConfirmationBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_email_confirmation);

        binding.goToEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(EmailConfirmation.this,LoginActivity.class));
            }
        });
    }
}
