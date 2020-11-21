package com.example.safehostel.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.safehostel.R;
import com.example.safehostel.adapters.hostels.HostelRatingsAdapter;
import com.example.safehostel.databinding.FagmentHostleRatingsBinding;
import com.example.safehostel.models.HostelRatingsModel;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class HostelRatings extends Fragment {
    private FagmentHostleRatingsBinding binding;
    private ArrayList<HostelRatingsModel> hostelRatingsModels;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private ListenerRegistration listener;
    private CollectionReference reference = db.collection("hostels");
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater,
                R.layout.fagment_hostle_ratings,
                container,false);
        View view = binding.getRoot();
        return view;
    }

    @Override
    public void onStart() {

        listener = reference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                hostelRatingsModels = new ArrayList<>();
                initiateRecycler(value);
            }
        });

        super.onStart();
    }


    private void initiateRecycler(QuerySnapshot value) {
        HostelRatingsModel model;

        for (QueryDocumentSnapshot documentSnapshot: value) {
            model = documentSnapshot.toObject(HostelRatingsModel.class);
            hostelRatingsModels.add(model);
        }


        RecyclerView recyclerView = binding.recyclerRatings;
        HostelRatingsAdapter adapter = new HostelRatingsAdapter(getContext(),hostelRatingsModels);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);

    }

    @Override
    public void onStop() {
        listener.remove();
        super.onStop();
    }
}
