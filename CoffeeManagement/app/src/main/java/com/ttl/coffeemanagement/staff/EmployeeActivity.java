package com.ttl.coffeemanagement.staff;

import android.os.Bundle;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ttl.coffeemanagement.data.DatabaseHelper;
import com.ttl.coffeemanagement.R;

import java.util.ArrayList;

public class EmployeeActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private EmployeeAdapter adapter;
    private ArrayList<Employee> employees;
    private ImageButton btnAdd, btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee);

        recyclerView = findViewById(R.id.recyclerView);
        btnAdd = findViewById(R.id.btnAdd);
        btnBack = findViewById(R.id.back_button);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        btnAdd.setOnClickListener(v -> new AddEmployeeDialog().show(getSupportFragmentManager(), "AddEmployee"));

        loadEmployees();
        btnBack.setOnClickListener(v -> finish());
    }

    public void loadEmployees() {
        employees = DatabaseHelper.getEmployees();
        adapter = new EmployeeAdapter(employees);
        recyclerView.setAdapter(adapter);
    }
}
