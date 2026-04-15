package com.example.sleepchartdeom;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import android.os.Bundle;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private LineChart lineChart;
    private BarChart barChart;
    private PieChart pieChart;
    private BarChart barChartCourse;
    private PieChart pieChartReminder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 绑定控件
        lineChart = findViewById(R.id.line_chart);
        barChart = findViewById(R.id.bar_chart);
        pieChart = findViewById(R.id.pie_chart);
        barChartCourse = findViewById(R.id.bar_chart_course);
        pieChartReminder = findViewById(R.id.pie_chart_reminder);

        // 初始化所有图表
        initOriginalCharts();
        initCourseBarChart();
        initReminderPieChart();
    }

    // 1. 你原有的3个图表（无报错版）
    private void initOriginalCharts() {
        // 一周睡眠时长趋势 折线图
        ArrayList<Entry> lineEntries = new ArrayList<>();
        lineEntries.add(new Entry(0f, 6.5f));
        lineEntries.add(new Entry(1f, 6.2f));
        lineEntries.add(new Entry(2f, 6.0f));
        lineEntries.add(new Entry(3f, 5.0f));
        lineEntries.add(new Entry(4f, 7.9f));
        lineEntries.add(new Entry(5f, 9.2f));
        lineEntries.add(new Entry(6f, 8.3f));

        LineDataSet lineDataSet = new LineDataSet(lineEntries, "睡眠时长(小时)");
        lineDataSet.setColor(ContextCompat.getColor(this, android.R.color.holo_blue_light));
        LineData lineData = new LineData(lineDataSet);

        lineChart.setData(lineData);
        lineChart.getDescription().setEnabled(false);
        lineChart.invalidate();

        // 有效/无效熬夜时长对比 柱状图
        ArrayList<BarEntry> barEntries = new ArrayList<>();
        barEntries.add(new BarEntry(0f, 10.5f));
        barEntries.add(new BarEntry(1f, 38.2f));

        BarDataSet barDataSet = new BarDataSet(barEntries, "熬夜时长(小时)");
        List<Integer> barColors = new ArrayList<>();
        barColors.add(ContextCompat.getColor(this, android.R.color.holo_green_light));
        barColors.add(ContextCompat.getColor(this, android.R.color.holo_red_light));
        barDataSet.setColors(barColors);

        BarData barData = new BarData(barDataSet);
        barChart.setData(barData);
        barChart.getDescription().setEnabled(false);
        barChart.invalidate();

        // 熬夜类型分布 饼图
        ArrayList<PieEntry> pieEntries = new ArrayList<>();
        pieEntries.add(new PieEntry(30.0f, "学习熬夜"));
        pieEntries.add(new PieEntry(60.0f, "娱乐熬夜"));
        pieEntries.add(new PieEntry(10.0f, "其他"));

        PieDataSet pieDataSet = new PieDataSet(pieEntries, "熬夜类型");
        pieDataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        PieData pieData = new PieData(pieDataSet);

        pieChart.setData(pieData);
        pieChart.getDescription().setEnabled(false);
        pieChart.setUsePercentValues(true);
        pieChart.invalidate();
    }

    // 2. 每日课程时长统计 柱状图（修复X轴标签报错）
    private void initCourseBarChart() {
        ArrayList<BarEntry> entries = new ArrayList<>();
        entries.add(new BarEntry(0f, 4f));
        entries.add(new BarEntry(1f, 6f));
        entries.add(new BarEntry(2f, 3f));
        entries.add(new BarEntry(3f, 5f));
        entries.add(new BarEntry(4f, 2f));
        entries.add(new BarEntry(5f, 0f));
        entries.add(new BarEntry(6f, 1f));

        BarDataSet dataSet = new BarDataSet(entries, "每日课程时长(小时)");
        dataSet.setColor(ContextCompat.getColor(this, android.R.color.holo_purple));
        dataSet.setDrawValues(false);
        BarData barData = new BarData(dataSet);

        barChartCourse.setData(barData);
        barChartCourse.getDescription().setEnabled(false);
        barChartCourse.setTouchEnabled(true);
        barChartCourse.getAxisLeft().setAxisMinimum(0f);
        barChartCourse.getAxisRight().setEnabled(false);

        // 修复：用正确的ValueFormatter实现X轴周一到周日标签
        XAxis xAxis = barChartCourse.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setLabelCount(7);
        xAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                String[] days = {"周一", "周二", "周三", "周四", "周五", "周六", "周日"};
                int index = (int) value;
                if (index >= 0 && index < days.length) {
                    return days[index];
                }
                return "";
            }
        });

        barChartCourse.invalidate();
    }

    // 3. 健康提醒完成率 饼图（修复你截图里的语法错误）
    private void initReminderPieChart() {
        ArrayList<PieEntry> entries = new ArrayList<>();
        entries.add(new PieEntry(70f, "已完成"));
        entries.add(new PieEntry(30f, "未完成"));

        PieDataSet dataSet = new PieDataSet(entries, "提醒完成率");
        List<Integer> pieColors = new ArrayList<>();
        pieColors.add(ContextCompat.getColor(this, android.R.color.holo_green_light));
        pieColors.add(ContextCompat.getColor(this, android.R.color.darker_gray));
        dataSet.setColors(pieColors);
        dataSet.setDrawValues(true);
        PieData pieData = new PieData(dataSet);

        pieChartReminder.setData(pieData);
        pieChartReminder.getDescription().setEnabled(false);
        pieChartReminder.setUsePercentValues(true);
        pieChartReminder.invalidate();
    }
}
