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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private static final String TAG = "LoginActivity";
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private DocumentReference reference;
    private String uid;


  @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_login);

        //setting onclick listeners
        binding.btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUserToFirebase();
            }
        });

        binding.tvRegLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this,OfficialsReg.class));
            }
        });

    }


    private void loginUserToFirebase(){
      String email = binding.etLoginEmail.getText().toString();
      String password = binding.etLoginPassword.getText().toString().trim();

      if (TextUtils.isEmpty(email)){
          binding.etLoginEmail.setError("Email is require!");
      }else if(TextUtils.isEmpty(password)){
          binding.etLoginPassword.setError("Password is required");
      }else {

          mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
              @Override
              public void onComplete(@NonNull Task<AuthResult> task) {

                  if (task.isSuccessful()){

                      checkUserRole();
                      sendUserToMain();

                  }else {
                      Toast.makeText(LoginActivity.this,"Error: "+task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                  }

              }
          });

      }

    }

    private void checkUserRole() {
        reference = db.collection("users").document(mAuth.getCurrentUser().getUid());
        reference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.get("role").equals("admin")){
                    if(documentSnapshot.get("verified").equals("false")){
                        startActivity(new Intent(LoginActivity.this,AdminWaiting.class));
                        finish();
                    }else {
                        sendUserToMain();
                    }
                }else {
                    sendUserToMain();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }

    private void sendUserToMain(){
      startActivity(new Intent(LoginActivity.this,MainActivity.class));
      finish();
    }

}
