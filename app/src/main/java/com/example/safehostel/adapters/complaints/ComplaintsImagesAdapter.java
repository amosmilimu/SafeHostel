package com.example.safehostel.adapters.complaints;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.safehostel.R;
import com.example.safehostel.databinding.ListComplaintsImagesBinding;
import com.example.safehostel.models.ComplaintImageModel;

import java.util.List;

public class ComplaintsImagesAdapter extends RecyclerView.
        Adapter<ComplaintsImagesAdapter.ViewHolder> {
    private Context context;
    private List<ComplaintImageModel> complaintImageModels;

    public ComplaintsImagesAdapter(Context context,
                                   List<ComplaintImageModel> complaintImageModels) {
        this.context = context;
        this.complaintImageModels = complaintImageModels;
    }

    @NonNull
    @Override
    public ComplaintsImagesAdapter.ViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent, int viewType) {
        ListComplaintsImagesBinding binding = DataBindingUtil.
                inflate(LayoutInflater.from(parent.getContext()),
                R.layout.list_complaints_images,parent,false);
        return new ComplaintsImagesAdapter.ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final ComplaintImageModel item = complaintImageModels.get(position);
    }

    @Override
    public int getItemCount() {
        return complaintImageModels.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        private ListComplaintsImagesBinding binding;
        public ViewHolder(@NonNull ListComplaintsImagesBinding
                                  listComplaintsImagesBinding) {
            super(listComplaintsImagesBinding.getRoot());
            this.binding = listComplaintsImagesBinding;
        }
    }
}
