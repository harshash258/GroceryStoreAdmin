package com.mycompany.grocerystoreadmin.Acitivity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.mycompany.grocerystoreadmin.Model.AboutShop;
import com.mycompany.grocerystoreadmin.R;

public class About extends AppCompatActivity {

    TextView name, address, email, phone;
    Button editName, editAddress, editEmail, editPhone;
    DatabaseReference databaseReference;

    String strName, strAddress, strEmail, strPhone;
    String newName, newaAddress, newEmail, newPhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        IntiliaseVariable();




        databaseReference = FirebaseDatabase.getInstance().getReference().child("About Shop");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                AboutShop shop = dataSnapshot.getValue(AboutShop.class);
                strName = shop.getName();
                strAddress = shop.getAddress();
                strEmail = shop.getEmail();
                strPhone = shop.getPhone();

                if (strName.equals(""))
                    name.setText("");
                else
                    name.setText(strName);
                address.setText(strAddress);
                email.setText(strEmail);
                phone.setText(strPhone);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        editName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showUpdateNameDialog();
            }
        });
        editAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showUpdateAddressDialog();
            }
        });
        editEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showUpdateEmailDialog();
            }
        });
        editPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showUpdatePhoneDialog();
            }
        });
    }

    private void showUpdateNameDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.update_name, null);
        builder.setView(view);

        TextView oldname = view.findViewById(R.id.oldPName);
        final EditText newPname1 = view.findViewById(R.id.newPname);
        Button button = view.findViewById(R.id.btn_update);

        builder.setTitle("Update Shop Name");
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();

        oldname.setText(strName);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newName = newPname1.getText().toString().trim();
                updateName(newName);
                alertDialog.dismiss();
            }
        });
    }
    private void showUpdatePhoneDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.update_name, null);
        builder.setView(view);

        TextView oldname = view.findViewById(R.id.oldPName);
        final EditText newPname1 = view.findViewById(R.id.newPname);
        Button button = view.findViewById(R.id.btn_update);

        builder.setTitle("Update Shop Address");
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();

        oldname.setText(strPhone);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newPhone = newPname1.getText().toString().trim();
                updatePhone(newPhone);
                alertDialog.dismiss();
            }
        });
    }
    private void showUpdateEmailDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.update_name, null);
        builder.setView(view);

        TextView oldname = view.findViewById(R.id.oldPName);
        final EditText newPname1 = view.findViewById(R.id.newPname);
        Button button = view.findViewById(R.id.btn_update);

        builder.setTitle("Update Shop Address");
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();

        oldname.setText(strEmail);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newEmail = newPname1.getText().toString().trim();
                updateEmail(newEmail);
                alertDialog.dismiss();
            }
        });
    }
    private void showUpdateAddressDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.update_name, null);
        builder.setView(view);

        TextView oldname = view.findViewById(R.id.oldPName);
        final EditText newPname1 = view.findViewById(R.id.newPname);
        Button button = view.findViewById(R.id.btn_update);

        builder.setTitle("Update Shop Address");
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();

        oldname.setText(strAddress);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newaAddress = newPname1.getText().toString().trim();
                updateAddress(newaAddress);
                alertDialog.dismiss();
            }
        });
    }


    private void updatePhone(String phone) {
        AboutShop info = new AboutShop(strName, strAddress, strEmail, phone);
        databaseReference.setValue(info);
        Toast.makeText(this, "Shop Phone Updated", Toast.LENGTH_SHORT).show();
    }
    private void updateEmail(String email) {
        AboutShop info = new AboutShop(strName, strAddress, email, strPhone);
        databaseReference.setValue(info);
        Toast.makeText(this, "Shop Email Updated", Toast.LENGTH_SHORT).show();
    }
    private void updateAddress(String address) {
        AboutShop info = new AboutShop(strName, address, strEmail, strPhone);
        databaseReference.setValue(info);
        Toast.makeText(this, "Shop Address Updated", Toast.LENGTH_SHORT).show();
    }
    private void updateName(String name) {
        AboutShop info = new AboutShop(name, strAddress, strEmail, strPhone);
        databaseReference.setValue(info);
        Toast.makeText(this, "Shop Name Updated", Toast.LENGTH_SHORT).show();
    }

    private void IntiliaseVariable() {
        name = findViewById(R.id.shopName);
        address = findViewById(R.id.shopAddress);
        email = findViewById(R.id.shopEmail);
        phone = findViewById(R.id.shopPhone);

        editName = findViewById(R.id.editShopName);
        editAddress = findViewById(R.id.editShopAddress);
        editEmail = findViewById(R.id.editShopEmail);
        editPhone = findViewById(R.id.editShopPhone);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("About Shop");
    }
}