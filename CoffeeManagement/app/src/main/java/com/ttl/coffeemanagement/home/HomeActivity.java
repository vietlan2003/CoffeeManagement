package com.ttl.coffeemanagement.home;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.ttl.coffeemanagement.R;
import com.ttl.coffeemanagement.product.ProductActivity;
import com.ttl.coffeemanagement.staff.EmployeeActivity;
import com.ttl.coffeemanagement.statistic.StatisticActivity;
import com.ttl.coffeemanagement.order.OrderActivity;

public class HomeActivity extends AppCompatActivity {
    Button logout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);

        logout = findViewById(R.id.logout);
        LinearLayout orderButton = findViewById(R.id.order_button);
        LinearLayout productButton = findViewById(R.id.product_button);
        LinearLayout statisticsButton = findViewById(R.id.statistics_button);
        LinearLayout staffButton = findViewById(R.id.staff_button);

        orderButton.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, OrderActivity.class);
            startActivity(intent);
        });

        productButton.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, ProductActivity.class);
            startActivity(intent);
        });

        statisticsButton.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, StatisticActivity.class);
            startActivity(intent);
        });

        staffButton.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, EmployeeActivity.class);
            startActivity(intent);
        });

        logout.setOnClickListener(v -> finish());
    }

}
