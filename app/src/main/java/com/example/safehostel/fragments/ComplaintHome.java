package com.example.safehostel.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.example.safehostel.R;
import com.example.safehostel.adapters.complaints.ComplaintsAdapter;
import com.example.safehostel.databinding.FagmentComplaintHomeBinding;
import com.example.safehostel.databinding.FagmentFileComplaintBinding;
import com.example.safehostel.models.ComplaintListModel;
import com.example.safehostel.models.ProfileModel;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ComplaintHome extends Fragment {
    private FagmentComplaintHomeBinding binding;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseUser mUser = mAuth.getCurrentUser();
    private static final String TAG = "MyComplaints";
    private ListenerRegistration listener;
    private Query reference =  db.collectionGroup("myComplaint");
    private boolean isAdmin = false;
    private String role;
    private ProfileModel profileModel = new ProfileModel();
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil
                .inflate(inflater,R.layout.fagment_complaint_home,container,false);
        View v = binding.getRoot();
        return v;
    }


    @Override
    public void onStart() {
        listener = reference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null){
//                    Toast.makeText(getContext(),"Error while loading",Toast.LENGTH_SHORT).show();
                    Log.e(TAG, "onEvent: "+error.getMessage() );
                }

                if (value != null){
                    for(QueryDocumentSnapshot documentSnapshot:value){
                        iterateComplaints(value);
                    }
                }
            }
        });


        super.onStart();
    }

    private void iterateComplaints(QuerySnapshot value) {
        List<ComplaintListModel> list_complaintListModels = new ArrayList<>();
        for(QueryDocumentSnapshot documentSnapshot: value){
            ComplaintListModel complaintListModel = documentSnapshot
                    .toObject(ComplaintListModel.class);
            if (complaintListModel.getState().equals("public")){
                list_complaintListModels.add(complaintListModel);
            }

        }
        ComplaintsAdapter mAdapter = new ComplaintsAdapter(getContext(), list_complaintListModels,true,"any");
        binding.recyclerComplaints.setHasFixedSize(true);
        binding.recyclerComplaints.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerComplaints.setAdapter(mAdapter);
    }

    @Override
    public void onStop() {
        listener.remove();
        super.onStop();
    }
}
























