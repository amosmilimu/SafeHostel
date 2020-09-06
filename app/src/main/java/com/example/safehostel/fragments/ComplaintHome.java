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

import com.example.safehostel.R;
import com.example.safehostel.adapters.complaints.ComplaintsAdapter;
import com.example.safehostel.databinding.FagmentComplaintHomeBinding;
import com.example.safehostel.databinding.FagmentFileComplaintBinding;
import com.example.safehostel.models.ComplaintListModel;

import java.util.ArrayList;
import java.util.List;

public class ComplaintHome extends Fragment {
    private FagmentComplaintHomeBinding binding;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater,R.layout.fagment_complaint_home,container,false);
        View v = binding.getRoot();
        populateComplaints();
        return v;
    }

    private void populateComplaints() {
        List<ComplaintListModel> complaintListModels = new ArrayList<>();
        for (int i = 0; i <10 ; i++) {
            ComplaintListModel item = new ComplaintListModel(
                    "Theft at Qwetu",
                    "24/Jul/2019",
                    "null",
                    "null",
                    "null",
                    "null",
                    "There was theft at qwetu...",
                    "private",
                    ""
            );
            complaintListModels.add(item);
        }
        ComplaintsAdapter mAdapter = new ComplaintsAdapter(getContext(),complaintListModels);
        binding.recyclerComplaints.setHasFixedSize(true);
        binding.recyclerComplaints.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerComplaints.setAdapter(mAdapter);
    }

}
























