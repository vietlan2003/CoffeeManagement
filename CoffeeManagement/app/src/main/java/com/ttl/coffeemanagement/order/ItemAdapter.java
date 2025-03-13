package com.ttl.coffeemanagement.order;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.ttl.coffeemanagement.R;

import java.util.List;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ProductViewHolder> {
    private List<ItemOrder> productList;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onQuantityChanged(ItemOrder product, int newQuantity);
    }

    public ItemAdapter(List<ItemOrder> productList, OnItemClickListener listener) {
        this.productList = productList;
        this.listener = listener;
    }

    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_order, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ProductViewHolder holder, int position) {
        ItemOrder product = productList.get(position);
        holder.productName.setText(product.getName());
        holder.productPrice.setText(String.format("%,d VND", (int) product.getPrice()));
        holder.productDescription.setText(product.getDescription());
        holder.checkBox.setChecked(product.getQuantity() > 0);
        holder.quantityText.setText(String.valueOf(product.getQuantity()));

        // Xử lý checkbox
        holder.checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                product.setQuantity(1); // Mặc định chọn 1 khi tick
            } else {
                product.setQuantity(0);
            }
            holder.quantityText.setText(String.valueOf(product.getQuantity()));
            listener.onQuantityChanged(product, product.getQuantity());
        });

        // Xử lý nút +
        holder.btnPlus.setOnClickListener(v -> {
            int newQuantity = product.getQuantity() + 1;
            product.setQuantity(newQuantity);
            holder.quantityText.setText(String.valueOf(newQuantity));
            listener.onQuantityChanged(product, newQuantity);
        });

        // Xử lý nút -
        holder.btnMinus.setOnClickListener(v -> {
            int newQuantity = Math.max(0, product.getQuantity() - 1);
            product.setQuantity(newQuantity);
            holder.quantityText.setText(String.valueOf(newQuantity));
            listener.onQuantityChanged(product, newQuantity);
        });

        try {
            String imageName = product.getImagePath();
            imageName = imageName.replace(".png", "").replace(".jpg", "").replace(".jpeg", "");
            int imageResId = holder.itemView.getContext().getResources()
                    .getIdentifier(imageName, "drawable", holder.itemView.getContext().getPackageName());
            if (imageResId != 0) {
                holder.productImage.setImageResource(imageResId);
            } else {
                holder.productImage.setImageResource(android.R.drawable.ic_menu_gallery);
            }
        } catch (Exception e) {
            holder.productImage.setImageResource(android.R.drawable.ic_menu_gallery);
        }
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder {
        public CheckBox checkBox;
        public TextView productName, productPrice, productDescription, quantityText;
        public ImageButton btnPlus, btnMinus;
        public ImageView productImage;

        public ProductViewHolder(View itemView) {
            super(itemView);
            checkBox = itemView.findViewById(R.id.checkbox_product);
            productName = itemView.findViewById(R.id.product_name);
            productPrice = itemView.findViewById(R.id.product_price);
            productDescription = itemView.findViewById(R.id.product_description);
            quantityText = itemView.findViewById(R.id.quantity_text);
            btnPlus = itemView.findViewById(R.id.btn_plus);
            btnMinus = itemView.findViewById(R.id.btn_minus);
            productImage = itemView.findViewById(R.id.product_image);
        }
    }
}
