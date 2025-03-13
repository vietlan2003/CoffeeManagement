package com.ttl.coffeemanagement.staff;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ttl.coffeemanagement.data.DatabaseHelper;
import com.ttl.coffeemanagement.R;

import java.util.ArrayList;

public class EmployeeAdapter extends RecyclerView.Adapter<EmployeeAdapter.ViewHolder> {
    private ArrayList<Employee> employees;

    public EmployeeAdapter(ArrayList<Employee> employees) {
        this.employees = employees;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_employee, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Employee employee = employees.get(position);
        holder.txtUsername.setText(employee.getUsername());
        holder.txtPhone.setText(employee.getPhone());
        holder.txtName.setText(employee.getName());

        holder.btnDelete.setOnClickListener(v -> {
            if (DatabaseHelper.deleteEmployee(employee.getId())) {
                employees.remove(position);
                notifyItemRemoved(position);
                Toast.makeText(v.getContext(), "Employee deleted!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(v.getContext(), "Delete failed!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return employees.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtUsername, txtPhone, txtName;
        ImageButton btnDelete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtUsername = itemView.findViewById(R.id.txtEmployeeUsername);
            txtPhone = itemView.findViewById(R.id.txtEmployeePhone);
            txtName = itemView.findViewById(R.id.txtEmployeeName);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}
