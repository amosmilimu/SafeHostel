package com.example.safehostel.adapters.complaints;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.safehostel.ComplaintMore;
import com.example.safehostel.R;
import com.example.safehostel.databinding.ListComplaintsBinding;
import com.example.safehostel.models.ComplaintListModel;

import java.util.List;

public class ComplaintsAdapter extends RecyclerView.Adapter<ComplaintsAdapter.ViewHolder> {

    private Context context;
    private List<ComplaintListModel> complaintListModels;

    public ComplaintsAdapter(Context context, List<ComplaintListModel> complaintListModels) {
        this.context = context;
        this.complaintListModels = complaintListModels;
    }

    @NonNull
    @Override
    public ComplaintsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ListComplaintsBinding binding = DataBindingUtil.
                inflate(LayoutInflater.from(parent.getContext()),
                R.layout.list_complaints,parent,false);
        return new ComplaintsAdapter.ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final ComplaintListModel item = complaintListModels.get(position);

        holder.binding.complaintDate.setText(item.getComplaintDate());
        holder.binding.complaintDesc.setText(item.getComplaintDesc());
        holder.binding.complaintTitle.setText(item.getComplaintTitle());
        holder.binding.cardComplaints.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ComplaintMore.class);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return complaintListModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ListComplaintsBinding binding;

        public ViewHolder(@NonNull ListComplaintsBinding listComplaintsBinding) {
            super(listComplaintsBinding.getRoot());
            this.binding = listComplaintsBinding;
        }
    }
}
