package com.example.safehostel.fragments;

import android.Manifest;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.safehostel.R;
import com.example.safehostel.adapters.complaints.ComplaintViewers;
import com.example.safehostel.constants.Constants;
import com.example.safehostel.databinding.FagmentFileComplaintBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.app.Activity.RESULT_OK;


public class FileComplaintsFrag extends Fragment {
    private FagmentFileComplaintBinding binding;
    private static final int REQUEST_CODE_STORAGE_PERMISSION = 1;
    private static final int REQUEST_CODE_SELECT_IMAGE = 2;
    private StorageReference mStorage;
    private FirebaseFirestore mDatabase = FirebaseFirestore.getInstance();
    private Context context;
    private int chosenImage = 0;
    private Map<Object, String> complaintMap = new HashMap<>();
    private String timeStamp;
    private static final String TAG = "FileComplaintsFrag";
    private CollectionReference reference = mDatabase.collection("users");
    private ListenerRegistration listener;
    private List<String> myList = new ArrayList<>();
    private ArrayList<String> myUidList = new ArrayList<>();
    private ArrayList<String> viewers;
    private FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();
    private SharedPreferences sharedPreferences;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater,
                R.layout.fagment_file_complaint, container, false);
        View v = binding.getRoot();
        context = this.getContext();

        sharedPreferences = context.getSharedPreferences("profile",Context.MODE_PRIVATE);

        //firebase
        mStorage = FirebaseStorage.getInstance().getReference("uploads");
        timeStamp = new SimpleDateFormat("dd/MMM/yyyy")
                .format(Calendar.getInstance().getTime());
        Log.e(TAG, "onCreateView: " + timeStamp);

        binding.imviewOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectLogo();
                chosenImage = 1;
            }
        });

        binding.imviewTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectLogo();
                chosenImage = 2;
            }
        });

        binding.imviewThree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectLogo();
                chosenImage = 3;
            }
        });

        binding.imviewFour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectLogo();
                chosenImage = 4;
            }
        });

        binding.btnSubmitDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //showViewersDialog();
                uploadComplaintToFirestore();
            }
        });


        binding.fileComplaint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogInfo();
            }
        });
        binding.btnViewers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showViewersDialog();
            }
        });

        LocalBroadcastManager.getInstance(getContext()).registerReceiver(
                mMessageReceiver, new IntentFilter("message")
        );


        return v;

    }

    public BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            viewers = intent.getStringArrayListExtra("viewers");
            Log.e(TAG, "onReceive: "+ viewers);

        }
    };

    @Override
    public void onStart() {
        listener = reference.whereEqualTo("role","admin").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Toast.makeText(getContext(), "Error while loading", Toast.LENGTH_SHORT).show();
                    Log.e(TAG, "onEvent: " + error.getMessage());
                } else {
                    if (value != null){
                        iterateThroughAdmins(value);
                    }

                }
            }
        });
        super.onStart();
    }

    private void iterateThroughAdmins(QuerySnapshot value) {
        for (QueryDocumentSnapshot documentSnapshot: value) {
            myList.add(documentSnapshot.get("username").toString());
            myUidList.add(documentSnapshot.get("user_uid").toString());
            Log.e(TAG, "iterateThroughAdmins: "+myList );
            Log.e(TAG, "iterateThroughAdmins: "+myUidList );
        }
    }

    private void uploadComplaintToFirestore() {
        String post_id = String.valueOf(System.currentTimeMillis());
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        complaintMap.put("title", binding.etComplaintTitle.getText().toString());
        complaintMap.put("description", binding.etComplaintDesc.getText().toString());
        complaintMap.put("date", timeStamp);
        complaintMap.put("state", "private");
        complaintMap.put("post_id", post_id);
        complaintMap.put("viewers",viewers!=null?viewers.toString():null);
        complaintMap.put("user_id",mUser != null ? mUser.getUid() : "");
        complaintMap.put("user_image",sharedPreferences.getString("user_image",null));
        mDatabase.collection("complaints")
                .document(uid)
                .collection("myComplaint")
                .document(post_id).set(complaintMap)//TODO add doc path
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        //show feedback when action completes
                    }
                });
    }

    private void showViewersDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View view = inflater.inflate(R.layout.list_complaint_viewers, null);
        builder.setView(view);
        AlertDialog alertDialog = builder.create();

        ComplaintViewers complaintViewers = new ComplaintViewers(getContext(), myList, myUidList);
        RecyclerView recyclerView = view.findViewById(R.id.recycler_viewers);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(complaintViewers);

        alertDialog.show();
    }

    private void selectLogo() {
        if (ContextCompat.checkSelfPermission(getContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    REQUEST_CODE_STORAGE_PERMISSION);
        } else {
            Intent intent = new Intent(Intent.ACTION_PICK,
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
                startActivityForResult(intent, REQUEST_CODE_SELECT_IMAGE);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_STORAGE_PERMISSION && grantResults.length > 0) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                selectLogo();
            } else {
                Toast.makeText(context, "Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_SELECT_IMAGE && resultCode == RESULT_OK) {
            Uri selectedImageUri = data.getData();
            if (data != null) {
                try {
                    InputStream inputStream = getActivity().getContentResolver().
                            openInputStream(selectedImageUri);
                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                    if (chosenImage == 1) {
                        binding.imviewOne.setImageBitmap(bitmap);
                        uploadImages(selectedImageUri);
                    }
                    if (chosenImage == 2) {
                        binding.imviewTwo.setImageBitmap(bitmap);
                        uploadImages(selectedImageUri);
                    }
                    if (chosenImage == 3) {
                        binding.imviewThree.setImageBitmap(bitmap);
                        uploadImages(selectedImageUri);
                    }
                    if (chosenImage == 4) {
                        binding.imviewFour.setImageBitmap(bitmap);
                        uploadImages(selectedImageUri);
                    }
                    Bitmap resized = Bitmap.createScaledBitmap(bitmap,
                            150, 150, true);
                } catch (Exception exception) {
                    Toast.makeText(context, exception.getMessage(),
                            Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private void showDialogInfo() {
        AlertDialog.Builder mydialog = new AlertDialog.Builder(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        View myview = inflater.inflate(R.layout.add_complaint_info, null);
        mydialog.setView(myview);
        final AlertDialog dialog = mydialog.create();
        dialog.show();
    }

    private String getFileExt(Uri uri) {

        ContentResolver cR = getActivity().getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    private void uploadImages(Uri selectedImageUri) {
        if (selectedImageUri != null) {
            Constants.showProgressDialog(getContext(),"Uploading ...");
            StorageTask<UploadTask.TaskSnapshot> reference = mStorage.child(System.currentTimeMillis() + "." + getFileExt(selectedImageUri))
                    .putFile(selectedImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Constants.cancelDialog();
                            getDownloadUrl(taskSnapshot);

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Constants.cancelDialog();

                        }
                    }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {

                        }
                    });
        } else {
            Toast.makeText(getContext(), "No image was selected", Toast.LENGTH_SHORT).show();
        }
    }

    private void getDownloadUrl(UploadTask.TaskSnapshot taskSnapshot) {
        Constants.cancelDialog();
        Task<Uri> task = taskSnapshot.getMetadata().getReference().getDownloadUrl();
        task.addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                String photoLink = uri.toString();

                if (chosenImage == 1) {
                    complaintMap.put("imageUrl1", photoLink);
                }
                if (chosenImage == 2) {
                    complaintMap.put("imageUrl2", photoLink);
                }
                if (chosenImage == 3) {
                    complaintMap.put("imageUrl3", photoLink);
                }
                if (chosenImage == 4) {
                    complaintMap.put("imageUrl4", photoLink);
                }
            }
        });
    }

    @Override
    public void onStop() {
        listener.remove();
        super.onStop();
    }
}
