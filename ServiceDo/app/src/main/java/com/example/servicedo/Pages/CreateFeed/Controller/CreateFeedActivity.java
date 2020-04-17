package com.example.servicedo.Pages.CreateFeed.Controller;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;


import com.example.servicedo.Pages.CreateFeed.Model.Feed;
import com.example.servicedo.Pages.HomePage.Controller.HomeActivity;
import com.example.servicedo.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CreateFeedActivity extends AppCompatActivity {

    private static final int GALLERY_REQUEST_CODE = 100;
    LinearLayout layoutImageSelected;
    ImageView imgAdd;
    EditText edtTitle;
    EditText edtDescription;
    EditText edtReward;
    TextInputLayout layoutTitle;
    TextInputLayout layoutReward;
    TextInputLayout layoutDescription;
    RadioButton rdbPeople;
    RadioButton rdbWork;
    Toolbar toolbar;
    FirebaseAuth mAuth;
    DatabaseReference mDatabase;

    private int countSelectedImage = 0;
    private ArrayList<Uri> listImage = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_feed);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        layoutImageSelected = findViewById(R.id.layout_image);
        imgAdd = findViewById(R.id.imageViewAdd);
        edtTitle = findViewById(R.id.edt_title);
        edtDescription = findViewById(R.id.edt_description);
        edtReward = findViewById(R.id.edt_reward);
        rdbPeople = findViewById(R.id.rdb_people_work);
        rdbWork = findViewById(R.id.rdb_work_people);
        toolbar = findViewById(R.id.toolbar);
        layoutTitle = findViewById(R.id.text_input_title);
        layoutReward = findViewById(R.id.text_input_reward);
        layoutDescription = findViewById(R.id.text_input_description);

        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        Drawable drawable = getResources().getDrawable(R.drawable.ic_back_foreground);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(drawable);
        actionBar.setTitle("");

        imgAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImageFromCard();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_toolbar, menu);
        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.done:
                if (checkConditionCreateFeed()) {
                    handleCreateFeed();
                } else {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        handleShowError();
                    }
                }
                break;
            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    public void handleCreateFeed() {
        final String userId = mAuth.getCurrentUser().getUid();
        final String title = edtTitle.getText().toString();
        final String description = edtDescription.getText().toString();
        final String reward = edtReward.getText().toString();
        Date date = new Date();
        final long createAt = date.getTime();
        Feed.EnumType type = Feed.EnumType.People;
        if (rdbWork.isChecked()) {
            type = Feed.EnumType.Work;
        }
        final Feed.EnumStatus status = Feed.EnumStatus.New;
        final String key = mDatabase.child("feeds").push().getKey();

        ArrayList<String> strListImage = new ArrayList<>();
        for (Uri uri : listImage) {
            strListImage.add(uri.toString());
        }

        StorageReference storageRef = FirebaseStorage.getInstance().getReference();

        final ArrayList<String> listDownloadUri = new ArrayList<>();
        ArrayList<Task<Uri>> uploadedImageUrlTasks = new ArrayList<>(listImage.size());
        for (int i = 0; i < listImage.size(); i++) {
            final StorageReference ref = storageRef.child("images/" + listImage.get(i).getLastPathSegment());
            UploadTask uploadTask = ref.putFile(listImage.get(i));
            final int finalI = i;
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
                        Uri downloadUri = task.getResult();
                        System.out.println("get url success" + finalI);
                        listDownloadUri.add(downloadUri.toString());
                    } else {
                        // Handle failures
                        // ...
                    }
                }
            });
            uploadedImageUrlTasks.add(urlTask);
        }

        final Feed.EnumType finalType = type;
        Tasks.whenAllComplete(uploadedImageUrlTasks)
                .addOnCompleteListener(new OnCompleteListener<List<Task<?>>>() {
                    @Override
                    public void onComplete(@NonNull Task<List<Task<?>>> task) {
                        Feed feed = new Feed(key, title, reward, description, finalType, status, createAt, userId, "", listDownloadUri);
                        System.out.println("create: "+feed.toString());
                        mDatabase.child("feeds").child(key).setValue(feed);
                        Intent intent = new Intent(CreateFeedActivity.this, HomeActivity.class);
                        startActivity(intent);
                    }
                });
//        mDatabase.child("feeds").child(key).child("image").setValue(listDownloadUri);

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void handleShowError(){

        String title = edtTitle.getText().toString();
        String description = edtDescription.getText().toString();
        String reward = edtReward.getText().toString();
        if(title.equals("")){
            edtTitle.setError(getString(R.string.layout_title_error));
        }

        if(reward.equals("")){
            edtReward.setError(getString(R.string.layout_reward_error));
        }

        if(description.equals("")){
            edtDescription.setError(getString(R.string.layout_description_error));
        }
    }

    public boolean checkConditionCreateFeed(){
        boolean agreeCreate = true;
        String title = edtTitle.getText().toString();
        String description = edtDescription.getText().toString();
        String reward = edtReward.getText().toString();

        if(title.equals("")){
            agreeCreate = false;
        }

        if(reward.equals("")){
            agreeCreate = false;
        }

        if(description.equals("")){
            agreeCreate = false;
        }

        if(listImage == null || listImage.size() == 0){
            agreeCreate = false;
        }

        return agreeCreate;
    }

    public void selectImageFromCard() {
        Intent intent=new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        String[] mimeTypes = {"image/jpeg", "image/png"};
        intent.putExtra(Intent.EXTRA_MIME_TYPES,mimeTypes);
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        startActivityForResult(intent,GALLERY_REQUEST_CODE);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_REQUEST_CODE && resultCode == RESULT_OK && null != data) {
            if(data.getClipData() != null) {
                int count = data.getClipData().getItemCount();
                if(count + countSelectedImage > 3) {
                    count = 3 - countSelectedImage;
                }
                for(int i = 0; i < count; i++){
                    Uri imageUri = data.getClipData().getItemAt(i).getUri();
                    addImageViewToLayout(imageUri);
                    countSelectedImage+=1;
                }
        } else if(data.getData() != null) {
                Uri selectedImageUri = data.getData();
                final String path = getPathFromURI(selectedImageUri);
                if (path != null) {
                    File f = new File(path);
                    selectedImageUri = Uri.fromFile(f);
                }
                countSelectedImage += 1;
                if (countSelectedImage <= 3) {
                    addImageViewToLayout(selectedImageUri);
                }
            }
            if (countSelectedImage == 3) {
                imgAdd.setVisibility(View.GONE);
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void addImageViewToLayout(final Uri selectedImageUri){
        final ImageView img = new ImageView( this);
        LinearLayout.LayoutParams viewParamsCenter = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);
        viewParamsCenter.weight = 1;
        viewParamsCenter.width = 0;
        viewParamsCenter.setMargins(10, 10, 10, 10);
        img.setLayoutParams(viewParamsCenter);
        img.setImageURI(selectedImageUri);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            final int color = 0x99000000;
            final Drawable drawable = new ColorDrawable(color);
            img.setForeground(drawable);
        }
        img.setScaleType(ImageView.ScaleType.CENTER_CROP);
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layoutImageSelected.removeView(img);
                countSelectedImage-=1;
                imgAdd.setVisibility(View.VISIBLE);
                listImage.remove(selectedImageUri.toString());
            }
        });
        layoutImageSelected.addView(img, 0);
        listImage.add(selectedImageUri);
    }

    public String getPathFromURI(Uri contentUri) {
        String res = null;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
        if (cursor.moveToFirst()) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            res = cursor.getString(column_index);
        }
        cursor.close();
        return res;
    }
}
