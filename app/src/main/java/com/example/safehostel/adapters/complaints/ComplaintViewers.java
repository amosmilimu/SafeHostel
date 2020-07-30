package com.example.safehostel.adapters.complaints;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;
import com.example.safehostel.R;
import com.example.safehostel.databinding.RecyclerComplaintViewerListBinding;
import java.util.List;

public class ComplaintViewers extends RecyclerView.Adapter<ComplaintViewers.ViewHolder> {
    Context context;
    List<String> viewers;

    public ComplaintViewers(Context context, List<String> viewers) {
        this.context = context;
        this.viewers = viewers;
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
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.binding.chkAdmins.setText(viewers.get(position));

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
