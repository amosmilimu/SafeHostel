package com.example.safehostel.adapters.hostels;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.safehostel.R;
import com.example.safehostel.databinding.HostelListBinding;
import com.example.safehostel.fragments.HostelRatings;
import com.example.safehostel.models.HostelRatingsModel;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Source;

import org.w3c.dom.Document;

import java.util.ArrayList;


public class HostelRatingsAdapter extends RecyclerView
        .Adapter<HostelRatingsAdapter.ViewHolder> {

    Context context;
    ArrayList<HostelRatingsModel> hostelRatingsModels;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private DocumentReference reference;
    private static final String TAG = "HostelRatingsAdapter";
    private int counter = 0;


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
        holder.binding.tvHostelName.setText(hostelRatingsModel.getName());
        holder.binding.tvUpvotes.setText(hostelRatingsModel.getUpvotes());
        holder.binding.tvDownvotes.setText(hostelRatingsModel.getDownvotes());
        Glide.with(context)
                .load(hostelRatingsModel.getImage())
                .centerCrop()
                .placeholder(R.drawable.ic_image_place)
                .into(holder.binding.hosteImage);

        holder.binding.icUpvote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                counter ++;
                reference = db.collection("hostels").document(hostelRatingsModel.getDocid());
                        Integer currentUpVote = Integer.parseInt(hostelRatingsModel.getUpvotes());
                        String upvote = String.valueOf(currentUpVote+counter);
                        updateUpVote(upvote,hostelRatingsModel);
            }
        });

        holder.binding.icDownvote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                counter ++;
                reference = db.collection("hostels").document(hostelRatingsModel.getDocid());

                        Integer currentDownVote = Integer.parseInt(hostelRatingsModel.getDownvotes());
                        String downvote = String.valueOf(currentDownVote+counter);
                        updateDownVote(downvote,hostelRatingsModel);

            }
        });
    }

    private void updateUpVote(final String upvote,
                            HostelRatingsModel hostelRatingsModel) {
        reference = db.collection("hostels")
                .document(hostelRatingsModel.getDocid());
            reference.update("upvotes",upvote).addOnSuccessListener(
                    new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Log.e(TAG, "onSuccess: "+upvote );
                }
            });
    }


    private void updateDownVote(final String downvote,
                              HostelRatingsModel hostelRatingsModel) {
        reference = db.collection("hostels")
                .document(hostelRatingsModel.getDocid());
        reference.update("downvotes",downvote).addOnSuccessListener(
                new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.e(TAG, "onSuccess: "+downvote );
                    }
                });
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
