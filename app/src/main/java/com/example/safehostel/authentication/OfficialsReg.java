package com.example.safehostel.authentication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Toast;

import com.example.safehostel.R;
import com.example.safehostel.databinding.ActivityOfficialsRegBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class OfficialsReg extends AppCompatActivity {

    private ActivityOfficialsRegBinding bnding;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private Map<String, Object> user = new HashMap<>();
    private FirebaseUser mUser;
    private Context context = OfficialsReg.this;
    private static final String TAG ="OfficialsReg";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bnding = DataBindingUtil.setContentView(this,R.layout.activity_officials_reg);

        mAuth = FirebaseAuth.getInstance();

        bnding.btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUserAuthDetails();
            }
        });

        bnding.iAmAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               startActivity(new Intent(OfficialsReg.this,AdminRegistration.class));
            }
        });

        bnding.tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context,LoginActivity.class));
            }
        });
    }


    private void registerUserAuthDetails(){
        if (validations()){
            mAuth.createUserWithEmailAndPassword(bnding.etRegEmail.getText().toString(),
                    bnding.etRegPassword.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()){
                        mUser = FirebaseAuth.getInstance().getCurrentUser();
                        mUser.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                if (task.isSuccessful()){
                                    user.put("email",bnding.etRegEmail.getText().toString());
                                    user.put("username",bnding.etRegUname.getText().toString());
                                    user.put("hostel",bnding.etRegHostel.getSelectedItem().toString());
                                    user.put("institution",bnding.etRegInstitution.getSelectedItem().toString());
                                    user.put("addmissionNo",bnding.etRegAdmNo.getText().toString());
                                    user.put("phoneNo",bnding.etRegPhone.getText().toString());
                                    user.put("role","student");
                                    user.put("user_uid",mUser.getUid());
                                    addCredentialsToFirestore(user,mUser.getUid());

                                    startActivity(new Intent(OfficialsReg.this,
                                            EmailConfirmation.class));
                                }else {
                                    Toast.makeText(OfficialsReg.this,"Error: "+
                                            task.getException().getMessage(),Toast.LENGTH_SHORT).show();

                                }

                            }
                        });

                    }else {
                        Toast.makeText(OfficialsReg.this,"Error: "+task.
                                getException().getMessage(),Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }else {
            Toast.makeText(OfficialsReg.
                            this,"Please provide all the required information",
                    Toast.LENGTH_SHORT).show();
        }
    }

    private void addCredentialsToFirestore(Map<String, Object> user,String user_uid){
    db.collection("users")
            .document(user_uid).set(user)
            .addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Toast.makeText(context,"Uploaded Successfully",Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
        @Override
        public void onFailure(@NonNull Exception e) {
            Toast.makeText(context,"Error:  "+e.getMessage(),Toast.LENGTH_SHORT).show();
        }
    });

    }


    public void flagAdmin(){

    }

    private boolean validations(){
        boolean valid = true;
        if (TextUtils.isEmpty(bnding.etRegEmail.getText().toString())){
            valid = false;
            bnding.etRegEmail.setError("Required Field");
            bnding.etRegEmail.requestFocus();
        }
        if (!isValidEmail(bnding.etRegEmail.getText().toString()))
        if (TextUtils.isEmpty(bnding.etRegPassword.getText())){
            valid = false;
            bnding.etRegPassword.setError("Required Field");
            bnding.etRegPassword.requestFocus();
        }
        if (TextUtils.isEmpty(bnding.etRegUname.getText().toString())){
            valid = false;
            bnding.etRegUname.setError("Required Field");
            bnding.etRegUname.requestFocus();
        }
        if (TextUtils.isEmpty(bnding.etRegAdmNo.getText().toString())){
            valid = false;
            bnding.etRegAdmNo.setError("Required Field");
            bnding.etRegAdmNo.requestFocus();
        }
        if (TextUtils.isEmpty(bnding.etRegPhone.getText().toString())){
            valid = false;
            bnding.etRegPhone.setError("Required Field");
            bnding.etRegPhone.requestFocus();
        }
        if (bnding.etRegPhone.getText().toString().length() != 10 &&
                !TextUtils.isDigitsOnly(bnding.etRegPhone.getText().toString())){
            bnding.etRegPhone.setError("Enter Valid Phone Number");
        }

        return valid;

    }

    public static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target)
                && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }
}
