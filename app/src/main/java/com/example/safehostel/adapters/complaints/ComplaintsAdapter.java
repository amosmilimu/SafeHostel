package com.example.safehostel.adapters.complaints;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.safehostel.ComplaintMore;
import com.example.safehostel.R;
import com.example.safehostel.databinding.ListComplaintsBinding;
import com.example.safehostel.models.ComplaintListModel;

import java.util.List;

public class ComplaintsAdapter extends RecyclerView.Adapter<ComplaintsAdapter.ViewHolder> {

    private Context context;
    private List<ComplaintListModel> complaintListModels;
    private boolean isHome;

    public ComplaintsAdapter(Context context, List<ComplaintListModel> complaintListModels,boolean isHome) {
        this.context = context;
        this.complaintListModels = complaintListModels;
        this.isHome = isHome;
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
        holder.binding.complaintDate.setText(item.getDate());
        holder.binding.complaintDesc.setText(item.getDescription());
        holder.binding.complaintTitle.setText(item.getTitle());
        Glide.with(context)
                .load(item.getImageUrl1())
                .centerCrop()
                .placeholder(R.drawable.ic_image_place)
                .into(holder.binding.complaintImage);
        holder.binding.complaintImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ComplaintMore.class);
                intent.putExtra("post_id",item.getPost_id());
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

            if (isHome) {
                listComplaintsBinding.complaintSwitch.setVisibility(View.INVISIBLE);
                listComplaintsBinding.commentCancel.setVisibility(View.INVISIBLE);
                listComplaintsBinding.switchText.setVisibility(View.INVISIBLE);
            } else {
                listComplaintsBinding.complaintSwitch.setVisibility(View.VISIBLE);
                listComplaintsBinding.commentCancel.setVisibility(View.VISIBLE);
                listComplaintsBinding.switchText.setVisibility(View.VISIBLE);
            }
        }
    }
}
