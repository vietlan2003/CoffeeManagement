package com.ttl.coffeemanagement.product;

import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ttl.coffeemanagement.R;
import com.ttl.coffeemanagement.data.DatabaseHelper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ProductActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ProductAdapter productAdapter;
    private List<Product> productList = new ArrayList<>();
    private TextView totalItemsText, totalPriceText;
    private ImageButton btnAddProduct, btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        // Khởi tạo các thành phần giao diện
        recyclerView = findViewById(R.id.recyclerView);
        totalItemsText = findViewById(R.id.totalItems);
        totalPriceText = findViewById(R.id.totalPrice);
        btnAddProduct = findViewById(R.id.btnAddProduct);
        btnBack = findViewById(R.id.back_button);

        // Cấu hình RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        productAdapter = new ProductAdapter(this, productList);
        recyclerView.setAdapter(productAdapter);

        // Load dữ liệu ban đầu
        loadProducts();
        updateSummary();

        btnAddProduct.setOnClickListener(v -> {
            showAddProductDialog();
        });

        // Xử lý sự kiện nút back
        btnBack.setOnClickListener(v -> finish());
    }

    private void loadProducts() {
        try (Connection conn = DatabaseHelper.connect();
             PreparedStatement stmt = conn.prepareStatement("EXEC sp_GetProducts");
             ResultSet rs = stmt.executeQuery()) {

            productList.clear();
            while (rs.next()) {
                productList.add(new Product(
                        rs.getInt("Id"),
                        rs.getString("ProductName"),
                        rs.getDouble("Price"),
                        rs.getString("Description"),
                        rs.getInt("Quantity")
                        // Không cần Quantity vì XML hiện tại không hiển thị
                ));
            }
            productAdapter.notifyDataSetChanged();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Lỗi khi tải danh sách sản phẩm", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateSummary() {
        try (Connection conn = DatabaseHelper.connect()) {
            // Load tổng số lượng tồn kho
            try (PreparedStatement stockStmt = conn.prepareStatement("EXEC sp_GetTotalStock");
                 ResultSet stockRs = stockStmt.executeQuery()) {
                if (stockRs.next()) {
                    totalItemsText.setText("" + stockRs.getInt("TotalStock") + " sản phẩm");
                }
            }

            // Load tổng giá trị
            try (PreparedStatement valueStmt = conn.prepareStatement("EXEC sp_GetTotalValue");
                 ResultSet valueRs = valueStmt.executeQuery()) {
                if (valueRs.next()) {
                    double totalValue = valueRs.getDouble("TotalValue");
                    totalPriceText.setText("" + String.format("%,.0f đ", totalValue));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Lỗi khi cập nhật tổng hợp", Toast.LENGTH_SHORT).show();
        }
    }

    // Phương thức thêm sản phẩm (chưa được gọi, cần tích hợp nếu cần)
    private void addProduct(String name, double price, String description,  int quantity) {
        try (Connection conn = DatabaseHelper.connect();
             PreparedStatement stmt = conn.prepareStatement("EXEC sp_AddProduct ?, ?, ?, ?")) {
            stmt.setString(1, name);
            stmt.setDouble(2, price);
            stmt.setString(3, description);
            stmt.setInt(4, quantity);
            stmt.executeUpdate();
            loadProducts();
            updateSummary();
            Toast.makeText(this, "Thêm sản phẩm thành công", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Lỗi khi thêm sản phẩm", Toast.LENGTH_SHORT).show();
        }
    }

    private void showAddProductDialog() {
        android.app.Dialog dialog = new android.app.Dialog(this);
        dialog.setContentView(R.layout.dialog_add_product);
        dialog.show();

        android.widget.EditText edtProductName = dialog.findViewById(R.id.edtProductName);
        android.widget.EditText edtPrice = dialog.findViewById(R.id.edtPrice);
        android.widget.EditText edtDescription = dialog.findViewById(R.id.edtDescription);
        android.widget.EditText edtQuantity = dialog.findViewById(R.id.edtQuantity);
        android.widget.Button btnConfirm = dialog.findViewById(R.id.btnConfirm);

        btnConfirm.setOnClickListener(v -> {
            String name = edtProductName.getText().toString().trim();
            String priceStr = edtPrice.getText().toString().trim();
            String description = edtDescription.getText().toString().trim();
            String quantityStr = edtQuantity.getText().toString().trim();

            if (name.isEmpty() || priceStr.isEmpty() || description.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
                return;
            }

            try {
                double price = Double.parseDouble(priceStr);
                int quantity = Integer.parseInt(quantityStr);
                addProduct(name, price, description, quantity);
                dialog.dismiss();
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Giá phải là số!", Toast.LENGTH_SHORT).show();
            }
        });
    }

}