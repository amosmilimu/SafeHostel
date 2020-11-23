package com.example.safehostel;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.drawerlayout.widget.DrawerLayout;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.safehostel.authentication.LoginActivity;
import com.example.safehostel.databinding.ActivityMainBinding;
import com.example.safehostel.fragments.FileComplaintsFrag;
import com.example.safehostel.fragments.ComplaintHome;
import com.example.safehostel.fragments.HostelRatings;
import com.example.safehostel.fragments.MyComplaints;
import com.example.safehostel.fragments.EditAccount;
import com.example.safehostel.models.ProfileModel;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    ActivityMainBinding mainBinding;
    private DrawerLayout drawer;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();
    private NavigationView navigationView;
    private String role;
    private TextView navUsername;
    private ListenerRegistration listener;
    private DocumentReference reference2;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private ProfileModel profileModel = new ProfileModel();
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainBinding = DataBindingUtil.setContentView(this,R.layout.activity_main);


        checkURoles();


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setTitle("");
        toolbar.setSubtitle("");


        //firebase
        mAuth = FirebaseAuth.getInstance();

        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);

        View headerView = navigationView.getHeaderView(0);
        navUsername = (TextView) headerView.findViewById(R.id.navHeaderName);

        navigationView.setNavigationItemSelectedListener(this);


        ActionBarDrawerToggle toggle =
                new ActionBarDrawerToggle(this,drawer,toolbar,
                        R.string.navigation_drawer_open,
                        R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        if(savedInstanceState == null){


            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container,
                            new ComplaintHome()).commit();

            navigationView.setCheckedItem(R.id.home_frag);}
    }

    @Override
    protected void onStart() {

        reference2 = db.collection("users")
                .document(mUser != null ? mUser.getUid() : "");

        reference2.addSnapshotListener(this,new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                if(value!=null){
                    profileModel = value.toObject(ProfileModel.class);
                    navUsername.setText(profileModel.getUsername()!=null?profileModel.getUsername():"Default");
                    SharedPreferences pref = MainActivity.this.getSharedPreferences("profile", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = pref.edit();
                    editor.putString("user_name",profileModel.getUsername());
                    editor.putString("user_image",profileModel.getProfile_image());
                    editor.apply();
                }
            }
        });

        super.onStart();
    }

    private void checkURoles() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference reference = db.collection("users")
                .document(mAuth.getCurrentUser()!=null?mAuth
                        .getCurrentUser()
                        .getUid():"document");
        reference.get().addOnSuccessListener(
                new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        role = documentSnapshot.get("role").toString();
                        if (role.equals("super_admin")){
                            navigationView.getMenu().findItem(R.id.settings).setVisible(true);
                        }else {
                            navigationView.getMenu().findItem(R.id.settings).setVisible(false);
                        }

                        if(role.equals("admin")){
                            navigationView.getMenu().findItem(R.id.add_complaint).setVisible(false);
                        }
                    }
                });
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        switch (menuItem.getItemId()){
            case R.id.home_frag:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new ComplaintHome()).commit();
                break;


            case R.id.add_complaint:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new FileComplaintsFrag()).commit();
                break;

            case R.id.ratings:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new HostelRatings()).commit();
                break;
            case R.id.my_complaints:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new MyComplaints()).commit();

                break;

            case R.id.log_out:
                callDialog();
                break;
        }

        drawer.closeDrawer(GravityCompat.START);

        return true;
    }

    private void callDialog(){
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        mAuth.signOut();
                        startActivity(new Intent(MainActivity.this, LoginActivity.class));
                        finish();
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        //No button clicked
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to exit ExtremeClean?").setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener).show();
    }

    //adds onclick listener on the nav_header

    public void onLayoutEditAccountClick(View nav_header_view){

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                new EditAccount()).commit();

        drawer.closeDrawer(GravityCompat.START);
        navigationView.setCheckedItem(R.id.none);

    }
}
