package com.example.safehostel.adapters.complaints;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.safehostel.R;
import com.example.safehostel.databinding.ListComplaintsCommentsBinding;
import com.example.safehostel.models.CommentsModel;

import java.util.List;

public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.ViewHolder> {
    private Context context;
    private List<CommentsModel> commentsModels;

    public CommentsAdapter(Context context, List<CommentsModel> commentsModels) {
        this.context = context;
        this.commentsModels = commentsModels;
    }

    @NonNull
    @Override
    public CommentsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ListComplaintsCommentsBinding binding = DataBindingUtil.
                inflate(LayoutInflater.from(parent.getContext()),
                        R.layout.list_complaints_comments,parent,false);
        return new CommentsAdapter.ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final CommentsModel item = commentsModels.get(position);
        holder.binding.commentorName.setText(item.getUser());
        holder.binding.commentorComment.setText(item.getComment());

        Glide.with(context)
                .load(item.getProfile_image())
                .centerCrop()
                .placeholder(R.drawable.ic_image_place)
                .into(holder.binding.commentorProfile);

    }

    @Override
    public int getItemCount() {
        return commentsModels.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private ListComplaintsCommentsBinding binding;
        public ViewHolder(@NonNull ListComplaintsCommentsBinding listComplaintsCommentsBinding) {
            super(listComplaintsCommentsBinding.getRoot());
            this.binding = listComplaintsCommentsBinding;
        }
    }
}
