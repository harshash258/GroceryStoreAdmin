package com.mycompany.grocerystoreadmin.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;
import com.mycompany.grocerystoreadmin.Acitivity.EditProduct;
import com.mycompany.grocerystoreadmin.Model.Products;
import com.mycompany.grocerystoreadmin.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {
    Context mContext;
    List<Products> mList;
    Task<Void> databaseReference;


    public ProductAdapter(Context mContext, List<Products> mList) {
        this.mContext = mContext;
        this.mList = mList;
    }
    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(mContext).inflate(R.layout.display_products, parent, false);
        return new ProductViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        final Products info = mList.get(position);
        holder.product_name.setText(info.getProduct_name());
        holder.product_description.setText(info.getProduct_description());
        holder.product_price.setText(info.getProduct_price());
        Picasso.get()
                .load(info.getImage_url())
                .fit().
                centerCrop()
                .into(holder.imageView);
        holder.editProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, EditProduct.class);
                intent.putExtra("productID", info.getProductID());
                mContext.startActivity(intent);
            }
        });

        holder.deleteProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mContext);
                alertDialogBuilder.setMessage("Are you sure You want to delete this product?");
                alertDialogBuilder.setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                databaseReference = FirebaseDatabase.getInstance().getReference().child("Products").child(info.getProductID()).removeValue();
                                Toast.makeText(mContext, "Product has been Deleted", Toast.LENGTH_SHORT).show();
                            }
                        });

                alertDialogBuilder.setNegativeButton("Cancel",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();

            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    static class ProductViewHolder extends RecyclerView.ViewHolder{
        TextView product_name, product_description, product_price;
        ImageView imageView;
        ImageButton editProduct, deleteProduct;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);

            product_name = itemView.findViewById(R.id.product_name);
            product_description = itemView.findViewById(R.id.product_description);
            product_price = itemView.findViewById(R.id.product_price);
            imageView = itemView.findViewById(R.id.product_image);
            editProduct = itemView.findViewById(R.id.edit_products);
            deleteProduct = itemView.findViewById(R.id.delete_product);
        }
    }
}
