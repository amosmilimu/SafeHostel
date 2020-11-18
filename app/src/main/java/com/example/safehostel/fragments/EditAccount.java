package com.example.safehostel.fragments;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
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

import com.bumptech.glide.Glide;
import com.example.safehostel.R;
import com.example.safehostel.constants.Constants;
import com.example.safehostel.databinding.FagmentEditAccountBinding;
import com.example.safehostel.models.ComplaintListModel;
import com.example.safehostel.models.ProfileModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
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
import java.util.HashMap;
import java.util.Map;

import static android.app.Activity.RESULT_OK;

public class EditAccount extends Fragment {
    private FagmentEditAccountBinding binding;
    private static final int REQUEST_CODE_STORAGE_PERMISSION = 1;
    private static final int REQUEST_CODE_SELECT_IMAGE = 2;
    private StorageReference mStorage;
    private DocumentReference reference;
    private ListenerRegistration listener;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private Map<String, Object> user = new HashMap<>();
    private FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();
    private static final String TAG = "EditAccount";
    private ProfileModel profileModel = new ProfileModel();
    private SharedPreferences pref;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil
                .inflate(inflater,R.layout.fagment_edit_account,container,false);
        View view = binding.getRoot();

        mStorage = FirebaseStorage.getInstance().getReference("uploads");
        binding.profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectLogo();
            }
        });
        pref = getContext().getSharedPreferences("profile", Context.MODE_PRIVATE);
        binding.userUname.setText(pref.getString("user_name","not provided"));

        return view;
    }

    @Override
    public void onStart() {
        reference = db.collection("users")
                .document(mUser != null ? mUser.getUid() : "");

        listener = reference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                profileModel = value.toObject(ProfileModel.class);
                pref = getContext().getSharedPreferences("profile", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit();
                editor.putString("user_name",profileModel.getUsername());
                editor.putString("user_image",profileModel.getProfile_image());

                editor.apply();
                Glide.with(getContext())
                        .load(profileModel.getProfile_image())
                        .centerCrop()
                        .placeholder(R.drawable.ic_image_place)
                        .into(binding.profileImage);
            }
        });

        super.onStart();
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
                Toast.makeText(getContext(), "Permission Denied", Toast.LENGTH_SHORT).show();
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

                        binding.profileImage.setImageBitmap(bitmap);
                        uploadImages(selectedImageUri);

                    Bitmap resized = Bitmap.createScaledBitmap(bitmap,
                            150, 150, true);
                } catch (Exception exception) {
                    Toast.makeText(getContext(), exception.getMessage(),
                            Toast.LENGTH_SHORT).show();
                }
            }
        }
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

                user.put("profile_image", photoLink);
                updateProfile(photoLink);

            }
        });
    }

    private void updateProfile(String profileImage) {

        reference = db.collection("users")
                .document(mUser != null ? mUser.getUid() : "");
        reference.update("profile_image",profileImage)
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
    public void onStop() {
        listener.remove();
        super.onStop();
    }
}
