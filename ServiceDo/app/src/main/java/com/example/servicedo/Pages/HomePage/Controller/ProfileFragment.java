package com.example.servicedo.Pages.HomePage.Controller;


import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.servicedo.Config.DialogConfig;
import com.example.servicedo.Pages.ListFeedPage.Controller.ListFeedActivity;
import com.example.servicedo.Pages.SignInPage.Controller.SignInActivity;
import com.example.servicedo.Pages.SignInPage.Model.User;
import com.example.servicedo.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.File;

import cn.pedant.SweetAlert.SweetAlertDialog;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {

    private static final int GALLERY_REQUEST_CODE = 10;
    private static final int PERMISSION_REQUEST_CODE = 1;
    private CircleImageView imgAvatar;
    private TextView tvUserName;
    private TextView tvEmail;
    private TextView tvPhone;
    private TextView tvSex;
    private TextView tvAddress;
    private TextView tvSignOut;
    private TextView tvEdit;
    private Button btnMyFeed;
    private Button btnReceive;

    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;

    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        btnMyFeed = view.findViewById(R.id.btn_my_feed);
        btnReceive = view.findViewById(R.id.btn_receive);
        imgAvatar = view.findViewById(R.id.img_avatar);
        tvUserName = view.findViewById(R.id.tv_user_name);
        tvEmail = view.findViewById(R.id.tv_email);
        tvPhone = view.findViewById(R.id.tv_phone);
        tvSex = view.findViewById(R.id.tv_sex);
        tvAddress = view.findViewById(R.id.tv_address);
        tvSignOut = view.findViewById(R.id.tv_sign_out);
        tvEdit = view.findViewById(R.id.tv_edit_profile);

        tvEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= 23) {
                    if (checkPermission()) {
                        selectImageFromCard();
                    } else {
                        requestPermission();
                    }
                } else {

                }
            }
        });

        tvSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getActivity(), SignInActivity.class);
                startActivity(intent);
            }
        });

        btnMyFeed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ListFeedActivity.class);
                intent.putExtra("isMyFeed", true);
                startActivity(intent);
            }
        });

        btnReceive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ListFeedActivity.class);
                intent.putExtra("isMyFeed", false);
                startActivity(intent);
            }
        });

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();

        String userId = mAuth.getCurrentUser().getUid();
        DatabaseReference userRef = mDatabase.child("user").child(userId);
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                if(user != null){
                    tvUserName.setText(user.getUserName());
                    tvEmail.setText(user.getEmail());
                    tvPhone.setText(user.getPhone());
                    User.EnumSex enumSex = User.EnumSex.valueOf(user.getSex());
                    if(enumSex.equals(User.EnumSex.Male)){
                        tvSex.setText(getString(R.string.rdb_sex_male));
                    } else {
                        tvSex.setText(getString(R.string.rdb_sex_female));
                    }
                    tvAddress.setText(user.getAddress());
                    if(!TextUtils.isEmpty(user.getAvatar())){
                        Picasso.get().load(user.getAvatar()).into(imgAvatar);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return view;
    }

    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(getContext(), android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
//        if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
//            Toast.makeText(getActivity(), "Write External Storage permission allows us to do store images. Please allow this permission in App Settings.", Toast.LENGTH_LONG).show();
//        } else {
//            ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
//        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    selectImageFromCard();
                }
                break;
        }
    }


    public void selectImageFromCard() {
        Intent intent=new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        String[] mimeTypes = {"image/jpeg", "image/png"};
        intent.putExtra(Intent.EXTRA_MIME_TYPES,mimeTypes);
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, false);
        startActivityForResult(intent, GALLERY_REQUEST_CODE);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_REQUEST_CODE && resultCode == Activity.RESULT_OK && null != data) {
            if(data.getData() != null) {
                DialogConfig pDialog = new DialogConfig(getContext(), SweetAlertDialog.PROGRESS_TYPE);
                pDialog.showProgress(getContext().getString(R.string.dialog_progress_upload_avatar));
                Uri selectedImageUri = data.getData();
                final String path = getPathFromURI(selectedImageUri);
                if (path != null) {
                    File f = new File(path);
                    selectedImageUri = Uri.fromFile(f);
                    imgAvatar.setImageURI(selectedImageUri);
                    uploadImage(selectedImageUri, pDialog);
                }
            }
        }
    }

    public void uploadImage(final Uri uri, final DialogConfig dialog){
        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
        final StorageReference ref = storageRef.child("avatars/" + uri.getLastPathSegment());
        UploadTask uploadTask = ref.putFile(uri);
        Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }
                // Continue with the task to get the download URL
                return ref.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    System.out.println("succ");
                    Uri downloadUri = task.getResult();
                    putAvatarUriToDB(downloadUri, dialog);
                } else {
                    // Handle failures
                    // ...
                }
            }
        });
    }

    public void putAvatarUriToDB(Uri uri, final DialogConfig dialog){
        String userId = mAuth.getCurrentUser().getUid();
        DatabaseReference userRef = mDatabase.child("user").child(userId).child("avatar");
        userRef.setValue(uri.toString()).addOnSuccessListener(new OnSuccessListener<Void>() {

            @Override
            public void onSuccess(Void aVoid) {
                dialog.dismissProgress();
                DialogConfig dialogConfig = new DialogConfig(getContext(), SweetAlertDialog.SUCCESS_TYPE);
                dialogConfig.showDialog(getString(R.string.dialog_upload_avatar_success));
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                dialog.dismissProgress();
                DialogConfig dialogConfig = new DialogConfig(getContext(), SweetAlertDialog.ERROR_TYPE);
                dialogConfig.showDialog(getString(R.string.dialog_upload_avatar_fail));
            }
        });
    }

    public String getPathFromURI(Uri contentUri) {
        String res = null;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContext().getContentResolver().query(contentUri, proj, null, null, null);
        if (cursor.moveToFirst()) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            res = cursor.getString(column_index);
        }
        cursor.close();
        return res;
    }
}
