package com.example.safehostel;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
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
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ComplaintMore extends BaseActivity {
    private ActivityComplaintMoreBinding binding;
    private Toolbar toolbar;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();
    private Bundle bundle = new Bundle();
    private DocumentReference reference;
    private CollectionReference reference1;
    private String postId;
    private String userId;
    private static final String TAG = "ComplaintMore";
    private ArrayList<String> complaintImageModels = new ArrayList<>();
    private ListenerRegistration listener;
    private CommentsModel commentsModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this
                ,R.layout.activity_complaint_more);

        bundle = getIntent().getExtras();

        setUpToolBar(binding.toolbar);

        if (bundle != null){
            postId = bundle.getString("post_id");
            userId = bundle.getString("user");
        }
        reference = db.collection("complaints").document(userId).collection("myComplaint").document(postId);
        reference1 = db.collection("comment");
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

                        if(complaintListModel.getSolved()) {
                            binding.tlSolved.setVisibility(View.VISIBLE);
                            binding.tvSolvedState.setText(String.valueOf(complaintListModel.getSolved()));
                            binding.tvByState.setText(complaintListModel.getBy());
                        }

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

        listener = reference1.whereEqualTo("post",postId).orderBy("time", Query.Direction.ASCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                setupCommentsRecycler(value);
            }
        });
    }

    private void setupCommentsRecycler(QuerySnapshot value) {
        List<CommentsModel> commentsModels = new ArrayList<>();
        for (QueryDocumentSnapshot documentSnapshots: value) {
            commentsModel = documentSnapshots.toObject(CommentsModel.class);
            commentsModels.add(commentsModel);
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

    @Override
    protected void onStop() {
        listener.remove();
        super.onStop();
    }
}