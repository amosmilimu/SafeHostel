package com.example.safehostel.adapters.complaints;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.safehostel.R;
import com.example.safehostel.databinding.RecyclerComplaintViewerListBinding;
import com.example.safehostel.models.ComplaintListModel;

import java.util.ArrayList;
import java.util.List;

public class ComplaintViewers extends RecyclerView.Adapter<ComplaintViewers.ViewHolder> {
    Context context;
    List<String> viewers;
    List<String> myUidList;
    ArrayList<String> selectedViewers = new ArrayList<>();
    private static final String TAG = "ComplaintViewers";

    public ComplaintViewers(Context context, List<String> viewers, List<String> myUidList) {
        this.context = context;
        this.viewers = viewers;
        this.myUidList = myUidList;
    }

    @NonNull
    @Override
    public ComplaintViewers.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final RecyclerComplaintViewerListBinding binding = DataBindingUtil.
                inflate(LayoutInflater.from(parent.getContext()),
                R.layout.recycler_complaint_viewer_list,parent,false);
        return new ComplaintViewers.ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.binding.chkAdmins.setText(viewers.get(position));
        holder.binding.chkAdmins.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
               if (b) {
                   selectedViewers.add(myUidList.get(position));
                   Log.e(TAG, "onCheckedChanged: " + selectedViewers);
                   createList(selectedViewers);
               } else  {
                   selectedViewers.remove(myUidList.get(position));
                   Log.e(TAG, "onCheckedChanged: "+ selectedViewers);
                   createList(selectedViewers);
               }
            }
        });


    }

    private void createList(ArrayList<String> selectedViewers) {
        Intent intent = new Intent("message");
        intent.putStringArrayListExtra("viewers", selectedViewers);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }

    @Override
    public int getItemCount() {
        return viewers.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder{

        private RecyclerComplaintViewerListBinding binding;

        public ViewHolder(@NonNull RecyclerComplaintViewerListBinding recyclerComplaintViewerListBinding) {
            super(recyclerComplaintViewerListBinding.getRoot());
            this.binding = recyclerComplaintViewerListBinding;
        }
    }
}
