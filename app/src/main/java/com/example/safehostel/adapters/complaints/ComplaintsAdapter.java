package com.example.safehostel.adapters.complaints;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.text.Editable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.safehostel.ComplaintMore;
import com.example.safehostel.R;
import com.example.safehostel.constants.Constants;
import com.example.safehostel.databinding.ListComplaintsBinding;
import com.example.safehostel.models.ComplaintListModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.type.DateTime;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ComplaintsAdapter extends RecyclerView.Adapter<ComplaintsAdapter.ViewHolder> {

    private Context context;
    private List<ComplaintListModel> complaintListModels;
    private boolean isHome;
    private DocumentReference reference;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();
    private SharedPreferences pref;
    private static final String TAG = "ComplaintsAdapter";
    private List<Boolean> mSbStates;
    private Map<String, Object> commentMap = new HashMap();
    public ComplaintsAdapter(Context context,
                             List<ComplaintListModel> complaintListModels,boolean isHome) {
        this.context = context;
        this.complaintListModels = complaintListModels;
        this.isHome = isHome;

    }

    @NonNull
    @Override
    public ComplaintsAdapter.ViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent, int viewType) {
        ListComplaintsBinding binding = DataBindingUtil.
                inflate(LayoutInflater.from(parent.getContext()),
                        R.layout.list_complaints,parent,false);
        pref = context.getSharedPreferences("profile",Context.MODE_PRIVATE);
        return new ComplaintsAdapter.ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder,final int position) {
        final ComplaintListModel item = complaintListModels.get(position);
        holder.binding.complaintDate.setText(item.getDate());
        holder.binding.complaintDesc.setText(item.getDescription());
        holder.binding.complaintTitle.setText(item.getTitle());
        Glide.with(context)
                .load(item.getImageUrl1())
                .centerCrop()
                .placeholder(R.drawable.ic_image_place)
                .into(holder.binding.complaintImage);
        Glide.with(context)
                .load(item.getUser_image())
                .centerCrop()
                .placeholder(R.drawable.ic_image_place)
                .into(holder.binding.complaintProfile);
        holder.binding.complaintImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ComplaintMore.class);
                intent.putExtra("post_id",item.getPost_id());
                intent.putExtra("user",item.getUser_id());
                context.startActivity(intent);
            }
        });


        if(item.getState().equals("public")) {
            holder.binding.btnPublic.setBackgroundTintList(
                    context.getResources().getColorStateList(R.color.orange));
            holder.binding.btnPublic.setTextColor(
                    context.getResources().getColor(R.color.contact_background));
            holder.binding.btnPrivate.setBackgroundTintList(
                    context.getResources().getColorStateList(R.color.grey));
            holder.binding.btnPrivate.setTextColor(
                    context.getResources().getColor(R.color.semi_black));
        } else {
            holder.binding.btnPrivate.setBackgroundTintList(
                    context.getResources().getColorStateList(R.color.orange));
            holder.binding.btnPrivate.setTextColor(
                    context.getResources().getColor(R.color.contact_background));
            holder.binding.btnPublic.setBackgroundTintList(
                    context.getResources().getColorStateList(R.color.grey));
            holder.binding.btnPublic.setTextColor(
                    context.getResources().getColor(R.color.semi_black));
        }

        holder.binding.btnPrivate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateVisibility(item,"private");

                holder.binding.btnPrivate.setBackgroundTintList(
                        context.getResources().getColorStateList(R.color.orange));
                holder.binding.btnPrivate.setTextColor(
                        context.getResources().getColor(R.color.contact_background));

                holder.binding.btnPublic.setBackgroundTintList(
                        context.getResources().getColorStateList(R.color.grey));
                holder.binding.btnPublic.setTextColor(
                        context.getResources().getColor(R.color.semi_black));

            }
        });

        holder.binding.btnPublic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateVisibility(item,"public");

                holder.binding.btnPublic.setBackgroundTintList(
                        context.getResources().getColorStateList(R.color.orange));
                holder.binding.btnPublic.setTextColor(
                        context.getResources().getColor(R.color.contact_background));

                holder.binding.btnPrivate.setBackgroundTintList(
                        context.getResources().getColorStateList(R.color.grey));
                holder.binding.btnPrivate.setTextColor(
                        context.getResources().getColor(R.color.semi_black));

            }
        });

        holder.binding.comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.binding.linearComment.setVisibility(View.VISIBLE);
            }
        });

        holder.binding.sendComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(holder.binding.commentText.getText() != null
                        && !holder.binding.commentText.getText().toString().isEmpty()) {
                    _sendComment(holder.binding.commentText.getText().toString(),item.getPost_id(),holder.binding.linearComment);
                } else {
                    holder.binding.commentText.setError("Kindly provide a comment");
                }
            }
        });

    }


    private void _sendComment(String text, String post_id, final LinearLayout linearComment) {
        Constants.showProgressDialog(context,"Adding comment...");
        commentMap.put("comment",text);
        commentMap.put("post",post_id);
        commentMap.put("user",pref.getString("user_name","not given"));
        commentMap.put("profile_image",pref.getString("user_image",null));
        commentMap.put("time", System.currentTimeMillis());

        db.collection("comment").document().set(commentMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.e(TAG, "onSuccess: successful");
                        Constants.cancelDialog();
                        linearComment.setVisibility(View.GONE);
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e(TAG, "onSuccess: failed"+e.getMessage(),e);
                Constants.cancelDialog();
            }
        });
    }

            private void updateVisibility(ComplaintListModel item, String status) {
        reference = db.collection("complaints")
                .document(mUser != null ? mUser.getUid() : "")
                .collection("myComplaint")
                .document(item.getPost_id());
        reference.update("state",status)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.e(TAG, "onSuccess: successful");
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e(TAG, "onSuccess: faile"+e.getMessage(),e);
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
                listComplaintsBinding.btnPrivate.setVisibility(View.INVISIBLE);
                listComplaintsBinding.btnPublic.setVisibility(View.INVISIBLE);
                listComplaintsBinding.cancel.setVisibility(View.INVISIBLE);
            } else {
                listComplaintsBinding.btnPrivate.setVisibility(View.VISIBLE);
                listComplaintsBinding.btnPublic.setVisibility(View.VISIBLE);
                listComplaintsBinding.cancel.setVisibility(View.VISIBLE);
            }
        }
    }
}