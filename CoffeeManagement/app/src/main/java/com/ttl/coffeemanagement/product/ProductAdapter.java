package com.ttl.coffeemanagement.product;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ttl.coffeemanagement.R;
import com.ttl.coffeemanagement.data.DatabaseHelper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {
    private List<Product> productList;
    private Context context;

    public ProductAdapter(Context context, List<Product> productList) {
        this.context = context;
        this.productList = productList;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product = productList.get(position);
        holder.txtName.setText(product.getName());
        holder.txtPrice.setText(product.getPrice() + " đ");

        // Sự kiện nhấn nút xóa (luôn hiển thị theo XML)
        holder.btnDelete.setOnClickListener(v -> deleteProduct(product.getId(), position));
    }

    private void deleteProduct(int productId, int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Xác nhận")
                .setMessage("Bạn có chắc muốn xóa sản phẩm này?")
                .setPositiveButton("Có", (dialog, which) -> {
                    try (Connection conn = DatabaseHelper.connect();
                         PreparedStatement stmt = conn.prepareStatement("EXEC sp_DeleteProduct ?")) {
                        stmt.setInt(1, productId);
                        stmt.executeUpdate();
                        productList.remove(position);
                        notifyItemRemoved(position);
                        Toast.makeText(context, "Đã xóa sản phẩm", Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(context, "Lỗi khi xóa sản phẩm", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Không", null)
                .show();
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder {
        TextView txtName, txtPrice;
        ImageView productImage;
        ImageButton btnDelete;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.txtName);
            txtPrice = itemView.findViewById(R.id.txtPrice);
            productImage = itemView.findViewById(R.id.productImage);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}