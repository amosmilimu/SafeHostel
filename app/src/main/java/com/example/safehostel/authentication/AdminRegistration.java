package com.example.safehostel.authentication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import com.example.safehostel.R;
import com.example.safehostel.constants.Constants;
import com.example.safehostel.databinding.ActivityAdminRegistrationBinding;
import com.example.safehostel.databinding.ActivityOfficialsRegBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class AdminRegistration extends AppCompatActivity {

    private ActivityAdminRegistrationBinding binding;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private Map<String, Object> user = new HashMap<>();
    private FirebaseUser mUser;
    private Context context = AdminRegistration.this;
    private static final String TAG ="OfficialsReg";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_admin_registration);

        mAuth = FirebaseAuth.getInstance();

        binding.btnRegisterAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUserAuthDetails();
            }
        });
    }

    private void registerUserAuthDetails(){
        if (validations()){
            Constants.showProgressDialog(context);
            mAuth.createUserWithEmailAndPassword(binding.adminEmail.getText().toString(),
                    binding.etRegPassword.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()){
                        mUser = FirebaseAuth.getInstance().getCurrentUser();
                        mUser.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                if (task.isSuccessful()){
                                    user.put("email",binding.adminEmail.getText().toString());
                                    user.put("username",binding.adminUname.getText().toString());
                                    user.put("hostel",binding.hostel.getSelectedItem().toString());
                                    user.put("institution",binding.institution.getSelectedItem().toString());
                                    user.put("phone",binding.adminPhone.getText().toString().trim());
                                    user.put("role","admin");
                                    user.put("verified","false");
                                    user.put("user_uid",mUser.getUid());
                                    addCredentialsToFirestore(user,mUser.getUid());

                                    Intent intent = new Intent(context,
                                            EmailConfirmation.class);
                                    startActivity(intent);
                                }else {
                                    Constants.cancelDialog();
                                    Toast.makeText(context,"Error: "+
                                            task.getException().getMessage(),Toast.LENGTH_SHORT).show();

                                }

                            }
                        });

                    }else {
                        Toast.makeText(context,"Error: "+task.
                                getException().getMessage(),Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }else {
            Toast.makeText(context,"Please provide all the required information",
                    Toast.LENGTH_SHORT).show();
        }
    }

    private void addCredentialsToFirestore(Map<String, Object> user,String user_uid){
        db.collection("users")
                .document(user_uid).set(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Constants.cancelDialog();
                        Toast.makeText(context,"Uploaded Successfully",Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Constants.cancelDialog();
                Toast.makeText(context,"Error:  "+e.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });

    }

    private boolean validations(){
        boolean valid = true;
        if (TextUtils.isEmpty(binding.adminEmail.getText().toString())){
            valid = false;
            binding.adminEmail.setError("Required Field");
            binding.adminEmail.requestFocus();
        }
        if (!isValidEmail(binding.adminEmail.getText().toString()))
            if (TextUtils.isEmpty(binding.etRegPassword.getText())){
                valid = false;
                binding.etRegPassword.setError("Required Field");
                binding.etRegPassword.requestFocus();
            }
        if (TextUtils.isEmpty(binding.adminUname.getText().toString())){
            valid = false;
            binding.adminUname.setError("Required Field");
            binding.adminUname.requestFocus();
        }
        if (TextUtils.isEmpty(binding.adminPhone.getText().toString())){
            valid = false;
            binding.adminPhone.setError("Required Field");
            binding.adminPhone.requestFocus();
        }
        if (binding.adminPhone.getText().toString().length() != 10 &&
                !TextUtils.isDigitsOnly(binding.adminPhone.getText().toString())){
            binding.adminPhone.setError("Enter Valid Phone Number");
        }

        return valid;

    }


    public static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target)
                && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }
}