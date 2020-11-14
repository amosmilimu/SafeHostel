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

import java.util.ArrayList;

public class HostelRatings extends Fragment {
    private FagmentHostleRatingsBinding binding;
    private ArrayList<HostelRatingsModel> hostelRatingsModels = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater,
                R.layout.fagment_hostle_ratings,
                container,false);
        View view = binding.getRoot();
        initiateRecycler();
        return view;
    }

    private void initiateRecycler() {

        for (int i = 0; i <4; i++) {
            HostelRatingsModel model = new HostelRatingsModel(
                   "Qwetu",
                    "4",
                    "4"
            );
            hostelRatingsModels.add(model);
        }

        RecyclerView recyclerView = binding.recyclerRatings;
        HostelRatingsAdapter adapter = new HostelRatingsAdapter(getContext(),hostelRatingsModels);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);

    }
}
