package com.example.safehostel.authentication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.safehostel.MainActivity;
import com.example.safehostel.R;
import com.example.safehostel.databinding.ActivityLoginBinding;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class LoginActivity extends AppCompatActivity {

    ActivityLoginBinding lbinding;
    FirebaseAuth mAuth;


  @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        lbinding = DataBindingUtil.setContentView(this,R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();


        //setting onclick listeners
        lbinding.btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUserToFirebase();
            }
        });

        lbinding.tvRegLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this,OfficialsReg.class));
            }
        });

    }

    private void loginUserToFirebase(){
      String email = lbinding.etLoginEmail.getText().toString();
      String password = lbinding.etLoginPassword.getText().toString().trim();

      if (TextUtils.isEmpty(email)){
          lbinding.etLoginEmail.setError("Email is require!");
      }else if(TextUtils.isEmpty(password)){
          lbinding.etLoginPassword.setError("Password is required");
      }else {

          mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
              @Override
              public void onComplete(@NonNull Task<AuthResult> task) {

                  if (task.isSuccessful()){

                      sendUserToMain();

                  }else {
                      Toast.makeText(LoginActivity.this,"Error: "+task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                  }

              }
          });

      }

    }

    private void sendUserToMain(){
      startActivity(new Intent(LoginActivity.this,MainActivity.class));
      finish();
    }

}
