package com.mycompany.grocerystoreadmin.Acitivity;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.mycompany.grocerystoreadmin.Adapter.SliderAdapterExample;
import com.mycompany.grocerystoreadmin.Model.Products;
import com.mycompany.grocerystoreadmin.Model.SliderItem;
import com.mycompany.grocerystoreadmin.R;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;
import java.util.List;

public class ImageSlider extends AppCompatActivity {

    public static final int PICK_IMAGE = 100;

    SliderView sliderView;
    Uri image_uri;
    ImageView imageView;
    Button chooseImage, uploadImage;
    SliderAdapterExample adapter;
    List<SliderItem> mList;
    ProgressDialog progressDialog;

    DatabaseReference databaseReference, databaseReference2;
    StorageReference storageReference;

    String download_url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_slider);

        sliderView = findViewById(R.id.imageSlider);
        chooseImage = findViewById(R.id.btn_chooseImage);
        uploadImage = findViewById(R.id.btn_Upload);
        imageView = findViewById(R.id.Sliderimage);
        mList = new ArrayList<>();

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Image Slider");
        storageReference = FirebaseStorage.getInstance().getReference().child("Image Slider");
        progressDialog = new ProgressDialog(this);

        sliderView.setIndicatorAnimation(IndicatorAnimationType.WORM); //set indicator animation by using SliderLayout.IndicatorAnimations. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
        sliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
        sliderView.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH);
        sliderView.setIndicatorSelectedColor(Color.WHITE);
        sliderView.setIndicatorUnselectedColor(Color.GRAY);
        sliderView.setScrollTimeInSec(3);
        sliderView.setAutoCycle(true);
        sliderView.startAutoCycle();

        databaseReference2 = FirebaseDatabase.getInstance().getReference().child("Image Slider");
        databaseReference2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot child : dataSnapshot.getChildren()){
                    SliderItem sliderItem = child.getValue(SliderItem.class);
                    mList.add(sliderItem);
                }
                adapter = new SliderAdapterExample(ImageSlider.this, mList);
                sliderView.setSliderAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        chooseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });
        uploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadImage();
            }
        });
    }

    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Image"), PICK_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && data != null) {
            image_uri = data.getData();
            imageView.setImageURI(image_uri);

        }
    }

    public String GetFileExtension(Uri uri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }
    private void uploadImage() {
        if (image_uri != null) {
            progressDialog.setTitle("Image is Uploading...");
            progressDialog.show();
            final StorageReference reference = storageReference.child(System.currentTimeMillis() + "."
                    + GetFileExtension(image_uri));
            final UploadTask uploadTask = reference.putFile(image_uri);
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(ImageSlider.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {


                    Task<Uri> uriTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                        @Override
                        public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                            if (!task.isSuccessful()) {
                                throw task.getException();
                            }
                            download_url = reference.getDownloadUrl().toString();
                            return reference.getDownloadUrl();
                        }
                    }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            progressDialog.dismiss();
                            Toast.makeText(ImageSlider.this, "Image Uploaded Successfully",
                                    Toast.LENGTH_SHORT).show();
                            if (task.isSuccessful()) {
                                download_url = task.getResult().toString();
                                String sliderID = databaseReference.push().getKey();
                                SliderItem sliderItem = new SliderItem("", download_url, sliderID);

                                databaseReference.child(sliderID).setValue(sliderItem);
                                Intent intent = new Intent(ImageSlider.this, MainActivity.class);
                                startActivity(intent);
                            }
                        }
                    });
                }
            });
        }
    }

}