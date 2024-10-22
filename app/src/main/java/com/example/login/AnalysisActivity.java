package com.example.login;


import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.LinkedList;


public class AnalysisActivity extends AppCompatActivity {

    TextView txtStandard;
    TextView txtExpenseSpecific;
    TextView txtTotalExpenses;
    TextView txtTotalIncome;
    TextView txtCurrentBalance;
    TextView txtSummary;
    TextView txtTotalSpending;
    View lilo_standard_analysis;
    View lilo_specific_analysis;
    BarChart bcMonthlyAnalysis;
    PieChart pcSpecificAnalysis;
    Spinner spinnerMonth;
    Spinner spinnerYear;
    ArrayList<String> months;
    ArrayList<String> years;
    LinkedList<Transaction> transactions;
    ArrayList<BarEntry> barIncome;
    ArrayList<BarEntry> barExpense;
    int[] weeklyIncome;
    int[] weeklyExpense;
    int monthSelected;
    int yearSelected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_analysis);

        txtStandard = findViewById(R.id.txtStandard);
        txtExpenseSpecific = findViewById(R.id.txtExpenseSpecific);
        txtTotalExpenses = findViewById(R.id.txtTotalExpenses);
        txtTotalIncome = findViewById(R.id.txtTotalIncome);
        txtCurrentBalance = findViewById(R.id.txtCurrentBalance);
        txtSummary = findViewById(R.id.txtSummary);
        txtTotalSpending = findViewById(R.id.txtTotalSpending);
        lilo_standard_analysis = findViewById(R.id.lilo_standard_analysis);
        lilo_specific_analysis = findViewById(R.id.lilo_specific_analysis);
        bcMonthlyAnalysis = findViewById(R.id.bcMonthlyAnalysis);
        pcSpecificAnalysis = findViewById(R.id.pcSpecificAnalysis);
        spinnerMonth = findViewById(R.id.spinnerMonth);
        spinnerYear = findViewById(R.id.spinnerYear);

        loadMonths();
        loadYears();
        loadTransactions();

        // setting the adapter for the spinnerMonth
        ArrayAdapter<String> monthsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, months);
        monthsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMonth.setAdapter(monthsAdapter);


        // default selected month for the spinner (january)
        spinnerMonth.setSelection(0);
        monthSelected = 1;


        spinnerMonth.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                monthSelected = position + 1;
                standardAnalysisFormatter();


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        // for the spinnerYear
        ArrayAdapter<String> yearsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, years);
        yearsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerYear.setAdapter(yearsAdapter);

        // setting the default spinner selections to 2024
        spinnerYear.setSelection(0);
        yearSelected = 2024;


        spinnerYear.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    yearSelected = 2024;
                }

                if (position == 1) {
                    yearSelected = 2023;
                }

                if (position == 2) {
                    yearSelected = 2022;
                }

                standardAnalysisFormatter();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        updateIncomeExpense(monthSelected, yearSelected);



        txtStandard.setSelected(true);

        txtExpenseSpecific.setOnClickListener(v -> {
            txtExpenseSpecific.setSelected(true);
            txtStandard.setSelected(false);

            lilo_specific_analysis.setVisibility(View.VISIBLE);
            lilo_standard_analysis.setVisibility(View.GONE);

            Toast.makeText(this, "expense week 1: " + weeklyIncome[0] + " Week2 : " + weeklyExpense[1], Toast.LENGTH_SHORT).show();
        });

        txtStandard.setOnClickListener(v -> {
            txtStandard.setSelected(true);
            txtExpenseSpecific.setSelected(false);

            lilo_standard_analysis.setVisibility(View.VISIBLE);
            lilo_specific_analysis.setVisibility(View.GONE);
        });

    }


    private void standardAnalysisFormatter() {
        updateIncomeExpense(monthSelected, yearSelected);

        barIncome = new ArrayList<>();
        barExpense = new ArrayList<>();

        int sumIncome = 0;
        int sumExpense = 0;

        for (int i = 1; i <= weeklyIncome.length; i++) {
            barIncome.add(new BarEntry(i, weeklyIncome[i - 1]));
            barExpense.add(new BarEntry(i + 0.3f, weeklyExpense[i - 1]));

            sumIncome += weeklyIncome[i - 1];
            sumExpense += weeklyExpense[i - 1];
        }

        BarDataSet barExpenseDataSet = new BarDataSet(barExpense, "expense");
        BarDataSet barIncomeDataSet = new BarDataSet(barIncome, "income");
        BarData barData = new BarData(barIncomeDataSet, barExpenseDataSet);
        bcMonthlyAnalysis.setData(barData);

        barChartVisualLoader(barExpenseDataSet, barIncomeDataSet, barData);


        // Setting the texts
        txtTotalExpenses.setText(sumExpense + " VND");
        txtTotalIncome.setText(sumIncome + " VND");

        String tempText;
        if (sumIncome > sumExpense) {
            tempText = "You got more money then you spend! Well done! Do it like this next months too.";
        } else if (sumIncome < sumExpense) {
            tempText = "You are spending too much. Your income is lower then your spending. Please reconsider before you spend!";
        } else {
            tempText = "What a coincidence! Your income and your expenses are the same!";
        }

        txtSummary.setText(String.format("In this month, your total expenses is %d VND and your total income is %d VND.%n%s", sumExpense, sumIncome, tempText));

        txtTotalSpending.setText(sumExpense + " VND");
    }

    private void barChartVisualLoader(BarDataSet barExpenseDataSet, BarDataSet barIncomeDataSet, BarData barData) {
        barExpenseDataSet.setColors(Color.parseColor("#DB69A8"));
        barExpenseDataSet.setValueTextColor(Color.WHITE);
        barExpenseDataSet.setValueTextSize(4f);

        barIncomeDataSet.setColors(Color.parseColor("#A2DFA3"));
        barIncomeDataSet.setValueTextSize(4f);
        barIncomeDataSet.setValueTextColor(Color.WHITE);

        bcMonthlyAnalysis.animateY(2000);

        bcMonthlyAnalysis.getLegend().setTextColor(Color.WHITE);
        bcMonthlyAnalysis.getLegend().setYOffset(10f);

        XAxis xAxis = bcMonthlyAnalysis.getXAxis();
        xAxis.setTextColor(Color.WHITE);
        xAxis.setDrawGridLines(false);
        xAxis.setDrawAxisLine(false);
        xAxis.setGranularity(1f);
        xAxis.setCenterAxisLabels(true);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM); // X-axis at the bottom

        Description xDescription = new Description();
        xDescription.setText("(Weeks)");
        xDescription.setPosition(bcMonthlyAnalysis.getWidth() / 1.8f, bcMonthlyAnalysis.getHeight() - 74);
        bcMonthlyAnalysis.setDescription(xDescription);
        xDescription.setTextColor(Color.WHITE);
        xDescription.setTextSize(10f);

        YAxis yAxis = bcMonthlyAnalysis.getAxisLeft();
        yAxis.setTextColor(Color.WHITE);
        yAxis.setGranularity(1f); // Left Y-axis
        yAxis.setDrawAxisLine(false);
        yAxis.setDrawGridLines(false);

        bcMonthlyAnalysis.getAxisRight().setEnabled(false); // Hide right Y-axis

        bcMonthlyAnalysis.setExtraOffsets(0f, 20f, 20f, 10f);

        barData.setBarWidth(0.3f);
        bcMonthlyAnalysis.groupBars(1f, 0.2f, 0.05f);
        bcMonthlyAnalysis.invalidate();
    }

    private void loadTransactions() {
        transactions = new LinkedList<>();

        for (int i = 1; i <= 50; i++) {
            if (i > 9 && i <= 31) {
                if (i % 2 == 0) {
                    transactions.add(new Transaction("user", "Travel", "" + (i * 100000), "Blah Blah", i + "/07/2024", true));
                } else {
                    transactions.add(new Transaction("user", "Shopping", "" + (i * 30000), "This is the note for the testing. Ha Ha.", i + "/07/2024", false));
                }
            } else {
                transactions.add(new Transaction("user", "others", "" + (i * 20000 ), "Nothing to say", "07/07/2024", true));
            }

        }
    }

    private void updateIncomeExpense(int month, int year) {
        if (month != 2) {
            weeklyIncome = new int[5];
            weeklyExpense = new int[5];
        } else {
            weeklyIncome = new int[4];
            weeklyExpense = new int[4];
        }

        for (Transaction transaction: transactions) {
            String date = transaction.getDate();
            int transactionMonth = Integer.parseInt(date.substring(3, 5));
            int transactionYear = Integer.parseInt(date.substring(6, 10));
            int week = (Integer.parseInt(date.substring(0, 2)) - 1) / 7;
            Boolean transactionSign = transaction.isIncome();

            if (transactionMonth == month && transactionYear == year) {
                if (transactionSign) {
                    weeklyIncome[week] += Integer.parseInt(transaction.getAmount());
                } else {
                    weeklyExpense[week] += Integer.parseInt(transaction.getAmount());
                }
            }
        }

    }

    private void loadMonths() {
        months = new ArrayList<>();
        months.add("January");
        months.add("February");
        months.add("March");
        months.add("April");
        months.add("May");
        months.add("June");
        months.add("July");
        months.add("August");
        months.add("September");
        months.add("October");
        months.add("November");
        months.add("December");
    }

    private void loadYears() {
        years = new ArrayList<>();

        years.add("2024");
        years.add("2023");
        years.add("2022");
    }
}