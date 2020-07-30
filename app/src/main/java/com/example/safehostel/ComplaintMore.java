package com.example.safehostel;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.widget.Toolbar;

import com.example.safehostel.adapters.complaints.CommentsAdapter;
import com.example.safehostel.adapters.complaints.ComplaintsImagesAdapter;
import com.example.safehostel.base.BaseActivity;
import com.example.safehostel.databinding.ActivityComplaintMoreBinding;
import com.example.safehostel.models.CommentsModel;
import com.example.safehostel.models.ComplaintImageModel;

import java.util.ArrayList;
import java.util.List;

public class ComplaintMore extends BaseActivity {
    private ActivityComplaintMoreBinding binding;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this
                ,R.layout.activity_complaint_more);

        setUpToolBar(binding.toolbar);
        setupImagesRecycler();
        setupCommentsRecycler();
    }

    private void setupCommentsRecycler() {
        List<CommentsModel> commentsModels = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            CommentsModel item = new CommentsModel(
                  "Amos",
                  "Url",
                  "lorem_ipsum\"Lorem ipsum is placeholder text commonly used in the graphic, print,\n" +
                          "and publishing industries for previewing layouts and visual mockups."
            );
            commentsModels.add(item);
        }
        CommentsAdapter mAdapter = new CommentsAdapter(this,
                commentsModels);
        binding.recyclerComments.setHasFixedSize(true);
        binding.recyclerComments.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerComments.setAdapter(mAdapter);
    }













    private void setupImagesRecycler() {
        List<ComplaintImageModel> complaintImageModels = new ArrayList<>();
        for (int i = 0; i <5 ; i++) {
            ComplaintImageModel item = new ComplaintImageModel(
              "This is the url",
              ""
            );
            complaintImageModels.add(item);
        }
        ComplaintsImagesAdapter mAdapter = new ComplaintsImagesAdapter(this,complaintImageModels);
        LinearLayoutManager linearLayoutManager =
                new LinearLayoutManager(this,
                        LinearLayoutManager.HORIZONTAL,false);
        binding.recyclerComplaintsImages.
                setLayoutManager(linearLayoutManager);
        binding.recyclerComplaintsImages.setHasFixedSize(true);
        binding.recyclerComplaintsImages.setAdapter(mAdapter);
    }


}