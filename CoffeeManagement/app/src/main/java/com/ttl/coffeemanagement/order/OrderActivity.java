package com.ttl.coffeemanagement.order;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ttl.coffeemanagement.data.DatabaseHelper;
import com.ttl.coffeemanagement.R;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class OrderActivity extends AppCompatActivity implements ItemAdapter.OnItemClickListener {
    private RecyclerView recyclerView;
    private ItemAdapter adapter;
    private List<ItemOrder> productList = new ArrayList<>();
    private TextView totalPriceText;
    private Button payButton;
    ImageButton backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        recyclerView = findViewById(R.id.recycler_view);
        totalPriceText = findViewById(R.id.total_price);
        payButton = findViewById(R.id.pay_button);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ItemAdapter(productList, this);
        recyclerView.setAdapter(adapter);

        // Load sản phẩm từ SQL Server
        loadProducts();

        // Xử lý nút Thanh toán
        payButton.setOnClickListener(v -> submitOrder());

        backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(v -> finish());
    }

    private void loadProducts() {
        new Thread(() -> {
            try {
                Connection connection = DatabaseHelper.connect();
                if (connection != null) {
                    String query = "EXEC GetProducts";
                    Statement stmt = connection.createStatement();
                    ResultSet rs = stmt.executeQuery(query);

                    productList.clear();
                    while (rs.next()) {
                        ItemOrder product = new ItemOrder(
                                rs.getInt("Id"),
                                rs.getString("ProductName"),
                                rs.getDouble("Price"),
                                rs.getString("Description"),
                                rs.getString("ImagePath") // Lấy đường dẫn ảnh từ DB
                        );
                        productList.add(product);
                    }
                    connection.close();

                    runOnUiThread(() -> adapter.notifyDataSetChanged());
                }
            } catch (Exception e) {
                e.printStackTrace();
                runOnUiThread(() -> Toast.makeText(this, "Lỗi tải sản phẩm", Toast.LENGTH_SHORT).show());
            }
        }).start();
    }


    @Override
    public void onQuantityChanged(ItemOrder product, int newQuantity) {
        updateTotalPrice();
    }

    private void updateTotalPrice() {
        double total = 0;
        for (ItemOrder product : productList) {
            total += product.getTotal();
        }
        totalPriceText.setText(String.format("%,d VND", (int) total));
    }

    private void submitOrder() {
        new Thread(() -> {
            try {
                Connection connection = DatabaseHelper.connect();
                if (connection != null) {
                    boolean hasOrder; // Khai báo biến ngoài lambda
                    Statement stmt = connection.createStatement();

                    hasOrder = false; // Gán giá trị ban đầu
                    for (ItemOrder product : productList) {
                        if (product.getQuantity() > 0) {
                            hasOrder = true; // Cập nhật giá trị
                            String query = "EXEC InsertOrder @ProductId=" + product.getId() +
                                    ", @Price=" + product.getPrice() +
                                    ", @Quantity=" + product.getQuantity() +
                                    ", @Total=" + product.getTotal();
                            stmt.execute(query);
                        }
                    }

                    connection.close();

                    // Sử dụng hasOrder trong lambda
                    final boolean finalHasOrder = hasOrder; // Tạo một biến final để sử dụng trong lambda
                    runOnUiThread(() -> {
                        if (finalHasOrder) {
                            showSuccessDialog(true); // Thành công
                        } else {
                            Toast.makeText(OrderActivity.this, "Vui lòng chọn sản phẩm", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            } catch (Exception e) {
                e.printStackTrace();
                runOnUiThread(() -> showSuccessDialog(false)); // Thất bại
            }
        }).start();
    }

    private void showSuccessDialog(boolean success) {
        android.app.Dialog dialog = new android.app.Dialog(this);
        dialog.setContentView(R.layout.dialog_success_order);
        dialog.setCancelable(false);

        ImageView ivIcon = dialog.findViewById(R.id.ivIcon);
        TextView tvMessage = dialog.findViewById(R.id.tvMessage);
        Button btnOk = dialog.findViewById(R.id.btnOk);

        if (success) {;
            tvMessage.setText("Đặt hàng thành công!");
        } else {
            tvMessage.setText("Đặt hàng thất bại!");
        }

        btnOk.setOnClickListener(v -> {
            dialog.dismiss();
            if (success) {
                finish();
            }
        });

        dialog.show();
    }
}