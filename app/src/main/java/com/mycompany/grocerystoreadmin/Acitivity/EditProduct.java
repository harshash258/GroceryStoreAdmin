package com.mycompany.grocerystoreadmin.Acitivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.mycompany.grocerystoreadmin.Model.Products;
import com.mycompany.grocerystoreadmin.R;
import com.squareup.picasso.Picasso;

public class EditProduct extends AppCompatActivity {

    TextView name, desc, price;
    ImageButton btnName, btnDesc, btnPrice, btnImage;
    ImageView image;
    Button updateProduct;
    DatabaseReference databaseReference;
    StorageReference storageReference;
    String productId, newPName, newPDesc, newPPrice, newPImage, newCategory;
    String oldName,oldDesc, oldPrice, oldImageUrl, oldCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_product);
        initialiseVariables();

        Intent intent = getIntent();
        productId = intent.getStringExtra("productID");
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Products").child(productId);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Products info = dataSnapshot.getValue(Products.class);
                assert info != null;

                oldName = info.getProduct_name();
                oldDesc = info.getProduct_description();
                oldPrice = info.getProduct_price();
                oldImageUrl = info.getImage_url();
                oldCategory = info.getCategory();

                name.setText(info.getProduct_name());
                desc.setText(info.getProduct_description());
                price.setText(info.getProduct_price());
                Picasso.get()
                        .load(info.getImage_url())
                        .fit()
                        .centerCrop()
                        .into(image);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        btnName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showUpdateDialog();
            }
        });
        btnDesc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDescDialog();
            }
        });
        btnPrice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        btnImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        updateProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    private void showUpdateDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.update_name, null);
        builder.setView(view);

        TextView oldname = view.findViewById(R.id.oldPName);
        final EditText newPname1 = view.findViewById(R.id.newPname);
        Button button = view.findViewById(R.id.btn_update);

        builder.setTitle("Update Product Name");
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();

        oldname.setText(oldName);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newPName = newPname1.getText().toString().trim();
                updateName(newPName);
                alertDialog.dismiss();
            }
        });

    }
    private void showDescDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.update_name, null);
        builder.setView(view);

        TextView oldname = view.findViewById(R.id.oldName);
        TextView oldPname = view.findViewById(R.id.oldPName);
        TextView newDesc = view.findViewById(R.id.newName);
        final EditText newPname1 = view.findViewById(R.id.newPname);
        Button button = view.findViewById(R.id.btn_update);

        builder.setTitle("Update Product Description");
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();

        oldname.setText("Old Description:");
        oldPname.setText(oldDesc);
        newDesc.setText("New Description:");

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newPDesc = newPname1.getText().toString().trim();
                updateDescription(newPDesc);
                alertDialog.dismiss();
            }
        });
    }
    private void showPriceDialog(){

    }
    private void showImageDialog(){}

    private void updateName(String name){
        Products info = new Products(name, oldPrice, oldDesc, oldImageUrl, productId, oldCategory);
        databaseReference.setValue(info);
        Toast.makeText(this, "Product Name Updated", Toast.LENGTH_SHORT).show();
    }
    private void updateDescription(String desc) {
        Products info = new Products(oldName, oldPrice, desc, oldImageUrl, productId, oldCategory);
        databaseReference.setValue(info);
        Toast.makeText(this, "Product Name Updated", Toast.LENGTH_SHORT).show();
    }
    private void updatePrice(String price){}
    private void updateImage(String imageurl){}

    private void initialiseVariables(){
        name = findViewById(R.id.editProductName);
        desc = findViewById(R.id.editProductDescription);
        price = findViewById(R.id.editProductPrice);
        image = findViewById(R.id.oldImage);
        btnName = findViewById(R.id.edit_name);
        btnDesc = findViewById(R.id.edit_description);
        btnPrice = findViewById(R.id.edit_price);
        btnImage = findViewById(R.id.edit_image);
        updateProduct = findViewById(R.id.btnUpdateProduct);
    }
}