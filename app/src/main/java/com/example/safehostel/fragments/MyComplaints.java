package com.example.safehostel.fragments;

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

import com.example.safehostel.R;
import com.example.safehostel.adapters.complaints.ComplaintsAdapter;
import com.example.safehostel.databinding.FagmentComplaintHomeBinding;
import com.example.safehostel.databinding.FagmentFileComplaintBinding;
import com.example.safehostel.databinding.FagmentMyComplaintsBinding;
import com.example.safehostel.models.ComplaintListModel;
import com.google.android.gms.tasks.OnFailureListener;
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
import java.util.Arrays;
import java.util.List;

public class MyComplaints extends Fragment {
    private FagmentMyComplaintsBinding binding;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseUser mUser = mAuth.getCurrentUser();
    private static final String TAG = "MyComplaints";
    private ListenerRegistration listener;
    private CollectionReference reference = db.collection("complaints").document(mUser != null ? mUser.getUid() : "").collection("myComplaint");
    private Query reference2 = db.collectionGroup("myComplaint");
    private boolean isAdmin = false;
    private String role;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fagment_my_complaints, container, false);
        View v = binding.getRoot();
        Log.e(TAG, "onCreate: oooooooooooooooooooooooooncrrrrrreate");
        checkURoles();
        return v;
    }

    private void checkURoles() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference reference = db.collection("users")
                .document(mAuth.getCurrentUser() != null ? mAuth
                        .getCurrentUser()
                        .getUid() : "document");
        reference.get().addOnSuccessListener(
                new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        role = documentSnapshot.get("role").toString();
                        isAdmin = role.equals("admin");
                        Log.e(TAG, "onSuccess: " + isAdmin);
                        onCustomStart(isAdmin);
                    }
                });
    }

    //@Override
    public void onCustomStart(boolean isAdmin) {
        super.onStart();
        Log.e(TAG, "onStart: ooooooooooooooooooooooooonsttttaaaaaaaaaaaaart");
        Log.e(TAG, "onStart: " + isAdmin);
        if (isAdmin) {
            reference2.addSnapshotListener(new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                    if (error != null) {
                        Log.e(TAG, "onEvent: " + error.getMessage());
                    }

                    if (value != null) {
                        for (QueryDocumentSnapshot documentSnapshot : value) {
                            iterateComplaints(value);
                        }
                    }
                }
            });


        } else {
            reference.addSnapshotListener(new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                    if (error != null) {
                        Log.e(TAG, "onEvent: " + error.getMessage());
                    }

                    if (value != null) {
                        iterateComplaints(value);
                    }

                }
            });
        }

    }

    private void iterateComplaints(QuerySnapshot value) {
        List<ComplaintListModel> list_complaintListModels = new ArrayList<>();
        for (QueryDocumentSnapshot documentSnapshot : value) {
            ComplaintListModel complaintListModel = documentSnapshot.toObject(ComplaintListModel.class);
            Log.e(TAG, "iterateComplaints: " + isAdmin);
            if (isAdmin) {
                if (complaintListModel.getViewers() != null) {
                    String replace = complaintListModel.getViewers().replace("[", "");
                    String replace1 = replace.replace("]", "");
                    List<String> myList = new ArrayList<String>(Arrays.asList(replace1.split(",")));

                    if (myList.contains(mUser != null ? mUser.getUid() : "") || myList.contains(mUser != null ? " "+mUser.getUid() : "")) {
                        list_complaintListModels.add(complaintListModel);
                    }
                }
            } else {
                list_complaintListModels.add(complaintListModel);
            }
        }
        ComplaintsAdapter mAdapter = new ComplaintsAdapter(getContext(), list_complaintListModels, isAdmin);
        binding.recyclerComplaints.setHasFixedSize(true);
        binding.recyclerComplaints.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerComplaints.setAdapter(mAdapter);
    }


}
























