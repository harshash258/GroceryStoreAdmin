package com.mycompany.grocerystoreadmin.Acitivity;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.mycompany.grocerystoreadmin.Model.Products;
import com.mycompany.grocerystoreadmin.R;

import java.util.ArrayList;

public class AddProduct extends AppCompatActivity {

    public static final int PICK_IMAGE = 100;
    EditText product_name, product_description, product_price;
    ImageView product_image;
    Button choose_image, submit;
    Uri image_uri;
    DatabaseReference databaseReference;
    ProgressDialog progressDialog;
    StorageReference storageReference;
    String download_url, category;
    Spinner spinner;
    ArrayAdapter<String> arrayAdapter;
    ArrayList mList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        product_name = findViewById(R.id.product_name);
        product_description = findViewById(R.id.product_description);
        product_price = findViewById(R.id.product_price);
        product_image = findViewById(R.id.product_image);
        choose_image = findViewById(R.id.btn_choose);
        submit = findViewById(R.id.btn_addProduct);
        spinner = findViewById(R.id.spinner);
        progressDialog = new ProgressDialog(AddProduct.this);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Products");
        storageReference = FirebaseStorage.getInstance().getReference().child("Products");

        choose_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });

        mList = new ArrayList<>();
        mList.add("Beauty and Grooming");
        mList.add("Cooking Essentials");
        mList.add("Dairy Products");
        mList.add("Ice-Cream");
        mList.add("Housing Need");
        mList.add("Packaged Food");

        arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, mList);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                category = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(AddProduct.this, "No Category Selected", Toast.LENGTH_SHORT).show();
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addProduct();
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
            product_image.setImageURI(image_uri);

        }
    }

    public String GetFileExtension(Uri uri) {

        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));

    }

    private void addProduct() {
        if (image_uri != null) {
            progressDialog.setTitle("Product is Uploading...");
            progressDialog.show();
            final StorageReference reference = storageReference.child(System.currentTimeMillis() + "."
                    + GetFileExtension(image_uri));
            final UploadTask uploadTask = reference.putFile(image_uri);
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(AddProduct.this, e.getMessage(), Toast.LENGTH_SHORT).show();
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
                            Toast.makeText(AddProduct.this, "Product Uploaded Successfully",
                                    Toast.LENGTH_SHORT).show();
                            if (task.isSuccessful()) {
                                download_url = task.getResult().toString();
                                String productID = databaseReference.push().getKey();
                                Products info = new Products(product_name.getText().toString().trim(),
                                        product_price.getText().toString().trim(),
                                        product_description.getText().toString().trim(),
                                        download_url, productID, category);
                                databaseReference.child(productID).setValue(info);
                                Intent intent = new Intent(AddProduct.this, MainActivity.class);
                                startActivity(intent);
                            }
                        }
                    });
                }
            });
        }
    }
}