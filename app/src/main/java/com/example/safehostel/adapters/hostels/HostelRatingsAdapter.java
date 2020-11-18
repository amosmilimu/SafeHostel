package com.example.safehostel.adapters.hostels;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;
import com.example.safehostel.R;
import com.example.safehostel.databinding.HostelListBinding;
import com.example.safehostel.models.HostelRatingsModel;

import java.util.ArrayList;


public class HostelRatingsAdapter extends RecyclerView
        .Adapter<HostelRatingsAdapter.ViewHolder> {

    Context context;
    ArrayList<HostelRatingsModel> hostelRatingsModels;


    public HostelRatingsAdapter(
            Context context,
            ArrayList<HostelRatingsModel> hostelRatingsModels) {
        this.context = context;
        this.hostelRatingsModels = hostelRatingsModels;
    }

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
        final HostelRatingsModel hostelRatingsModel = hostelRatingsModels.get(position);
        holder.binding.tvHostelName.setText(hostelRatingsModel.getHostelName());
        holder.binding.tvUpvotes.setText(hostelRatingsModel.getHostelUpVotes());
        holder.binding.tvDownvotes.setText(hostelRatingsModel.getHostelDownVotes());
        holder.binding.hosteImage.setBackgroundResource(R.drawable.account);
    }

    @Override
    public int getItemCount() {
        return hostelRatingsModels.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        HostelListBinding binding;
        public ViewHolder(@NonNull HostelListBinding hostelListBinding) {
            super(hostelListBinding.getRoot());
            this.binding = hostelListBinding;
        }
    }
}
