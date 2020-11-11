package com.example.safehostel.adapters.hostels;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;
import com.example.safehostel.R;
import com.example.safehostel.databinding.HostelListBinding;


public class HostelRatingsAdapter extends RecyclerView.Adapter<HostelRatingsAdapter.ViewHolder> {


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        HostelListBinding binding = DataBindingUtil.inflate(
          LayoutInflater.from(parent.getContext()),
                R.layout.hostel_list,parent,false
        );
        return new HostelRatingsAdapter.ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        HostelListBinding binding;
        public ViewHolder(@NonNull HostelListBinding hostelListBinding) {
            super(hostelListBinding.getRoot());
            this.binding = hostelListBinding;
        }
    }
}
