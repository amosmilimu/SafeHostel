package com.example.safehostel;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toolbar;

import com.example.safehostel.adapters.complaints.CommentsAdapter;
import com.example.safehostel.adapters.complaints.ComplaintsImagesAdapter;
import com.example.safehostel.base.BaseActivity;
import com.example.safehostel.databinding.ActivityComplaintMoreBinding;
import com.example.safehostel.models.CommentsModel;
import com.example.safehostel.models.ComplaintListModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class ComplaintMore extends BaseActivity {
    private ActivityComplaintMoreBinding binding;
    private Toolbar toolbar;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();
    Bundle bundle = new Bundle();
    private DocumentReference reference;
    private String postId;
    private static final String TAG = "ComplaintMore";
    ArrayList<String> complaintImageModels = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this
                ,R.layout.activity_complaint_more);

        bundle = getIntent().getExtras();

        setUpToolBar(binding.toolbar);
        setupCommentsRecycler();

        if (bundle != null){
            postId = bundle.getString("post_id");
        }
        reference = db.collection("complaints").document(mUser != null ? mUser.getUid() : "").collection("myComplaint").document(postId);
    }

    @Override
    protected void onStart() {
        super.onStart();

        reference.get().addOnSuccessListener(this,new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()){
                    ComplaintListModel complaintListModel =  documentSnapshot.toObject(ComplaintListModel.class);

                    if(complaintListModel != null){
                        binding.complaintTitle.setText(complaintListModel.getTitle());
                        binding.complaintMoreDesc.setText(complaintListModel.getDescription());

                        complaintImageModels.add(complaintListModel.getImageUrl1());
                        complaintImageModels.add(complaintListModel.getImageUrl2());
                        complaintImageModels.add(complaintListModel.getImageUrl3());
                        complaintImageModels.add(complaintListModel.getImageUrl4());
                        setupImagesRecycler(complaintImageModels);
                    }

                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
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


    private void setupImagesRecycler(ArrayList<String> complaintImageModels) {
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