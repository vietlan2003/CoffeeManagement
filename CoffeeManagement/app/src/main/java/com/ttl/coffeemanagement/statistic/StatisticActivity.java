package com.ttl.coffeemanagement.statistic;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.ttl.coffeemanagement.R;
import com.ttl.coffeemanagement.data.DatabaseHelper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class StatisticActivity extends AppCompatActivity {
    TextView txtOrders, txtRevenue, txtProfit, txtCustomers;
    BarChart barChart;
    ImageButton backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        txtOrders = findViewById(R.id.txtOrders);
        txtRevenue = findViewById(R.id.txtRevenue);
        txtProfit = findViewById(R.id.txtProfit);
        txtCustomers = findViewById(R.id.txtCustomers);

        barChart = findViewById(R.id.barChart);
        loadChartData();

        loadStatistics();
        backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(v -> finish());
    }

    private void loadStatistics() {
        new Thread(() -> {
            try {
                ResultSet rs = DatabaseHelper.getStatistics();
                if (rs != null && rs.next()) {
                    int orders = rs.getInt("OrderCount");
                    double revenue = rs.getDouble("TotalRevenue");
                    double profit = rs.getDouble("TotalProfit");
                    int customers = rs.getInt("TotalCustomers");

                    runOnUiThread(() -> {
                        txtOrders.setText("" + orders);
                        txtRevenue.setText(revenue + "đ");
                        txtProfit.setText(profit + "đ");
                        txtCustomers.setText("" + customers);
                    });
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }
    private void loadChartData() {
        new Thread(() -> {
            try {
                Connection conn = DatabaseHelper.connect();
                String query = "SELECT CONVERT(DATE, OrderDate) AS OrderDate, SUM(Total) AS TotalSales " +
                        "FROM Orders GROUP BY CONVERT(DATE, OrderDate) ORDER BY OrderDate ASC";
                PreparedStatement stmt = conn.prepareStatement(query);
                ResultSet rs = stmt.executeQuery();

                ArrayList<BarEntry> entries = new ArrayList<>();
                ArrayList<String> labels = new ArrayList<>();
                int index = 0;

                while (rs.next()) {
                    String date = rs.getString("OrderDate");
                    float totalSales = rs.getFloat("TotalSales");

                    entries.add(new BarEntry(index, totalSales));
                    labels.add(date);
                    index++;
                }

                runOnUiThread(() -> drawChart(entries, labels));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    private void drawChart(ArrayList<BarEntry> entries, ArrayList<String> labels) {
        BarDataSet dataSet = new BarDataSet(entries, "Doanh Thu");
        dataSet.setColor(Color.BLUE);
        BarData barData = new BarData(dataSet);

        barChart.setData(barData);
        barChart.invalidate(); // Refresh chart

        XAxis xAxis = barChart.getXAxis();
        xAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return labels.get((int) value);
            }
        });
        xAxis.setGranularity(1f);
    }
}

