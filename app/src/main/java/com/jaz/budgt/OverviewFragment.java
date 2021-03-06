package com.jaz.budgt;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.LegendEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.MPPointF;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by jaz on 8/22/17.
 */

public class OverviewFragment extends Fragment {
    public static OverviewFragment newInstance() {
        return new OverviewFragment();
    }
    //transaction list
    ArrayList<Transaction> transactionList = new ArrayList<>(0);
    LocalStorage localStorage;
    //variables for totals and stats
    double totalSpent = 0;
    double averagePerDay = 0.0;
    double averagePerMonth = 0.0;
    long totalDays = 0;
    double totalMonths = 0.0;
    Map<String, Double> categoryTotals = new Hashtable<>(60);
    Map<String,ArrayList<String>> categories = new HashMap<>();
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");//, Locale.US);
    private PieChart categoryPieChart;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        localStorage = new LocalStorage(this.getActivity());
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){inflater.inflate(R.menu.transaction_list_options, menu);}


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.overview_fragment, container, false);

        transactionList = localStorage.loadTransactions();
        categories = localStorage.loadCategories();
        calculateTotalDays();
        calculateTotalSpent();

//        TextView totalSpentTextView = view.findViewById(R.id.total_amount_spent);
//        totalSpentTextView.setText(calculateTotalSpent());

        TextView averageMonthlyDeltaTextView = view.findViewById(R.id.average_monthly_delta);
        averageMonthlyDeltaTextView.setText(calculateTotalPerMonth());

//        TextView categoryTotalsTextView = view.findViewById(R.id.category_totals);
        calculateTotalsPerCategory();
//        String categoryTotalString = "";
//        Iterator it = categoryTotals.entrySet().iterator();
//        while (it.hasNext()) {
//            Map.Entry pair = (Map.Entry)it.next();
//            categoryTotalString += "\n" + pair.getKey() + ": $";
//            NumberFormat formatter = new DecimalFormat("#0.00");
//            categoryTotalString += formatter.format(pair.getValue());
//        }
//        categoryTotalsTextView.setText(categoryTotalString);

        categoryPieChart = view.findViewById(R.id.categories_pie_chart);
        setUpCategoryPieChart();

        return view;
    }

    @Override
    public void onPause() {
        super.onPause();
        localStorage.saveTransactions(transactionList);
    }

    @Override
    public void onStop() {
        super.onStop();
        localStorage.saveTransactions(transactionList);
    }

    @Override
    public void onResume() {
        super.onResume();
        transactionList = localStorage.loadTransactions();
        //todo update calculated values for new data if necessary
    }
    
    public String calculateTotalSpent() {
        int dollarSum = 0;
        int centSum = 0;
        for (Transaction transaction: transactionList) {
            if(transaction.isExpense()) {
                dollarSum -= transaction.getDollarAmount();
                centSum -= transaction.getCentAmount();
            } else {
                dollarSum += transaction.getDollarAmount();
                centSum += transaction.getCentAmount();
            }
        }
        dollarSum += centSum / 100;
        centSum = centSum % 100;
        totalSpent = dollarSum + centSum / 100.;
        Log.d("Overview Fragment","total spent: " + totalSpent);
        String total = "";
        if(dollarSum < 0) {
            total += "-";
            dollarSum = Math.abs(dollarSum);
            centSum = Math.abs(centSum);
        }
        total += "$" + dollarSum + ".";
        if(centSum < 10) {
            total += "0" + centSum;
        } else {
            total += centSum;
        }
        total = "Total Spent: " + total;
        return total;
    }

    public void calculateTotalDays() {
        Date minDate = new Date();
        try { minDate = simpleDateFormat.parse("10/10/2100");
        } catch(ParseException e){ e.printStackTrace(); }
        Date maxDate = new Date();

        for (Transaction transaction: transactionList) {
            Date curDate = new Date();
            try {
                curDate = simpleDateFormat.parse(transaction.getDay() + "/" + transaction.getMonth() + "/" + transaction.getYear());
            } catch(ParseException e){
                e.printStackTrace();
            }
            if(curDate.getTime() > maxDate.getTime()) {
                maxDate = curDate;
            } else if(curDate.getTime() < minDate.getTime()) {
                minDate = curDate;
            }
        }
        //todo make this catch exceptions with initialization

        Log.d("OverviewFragment", "Mindate: " + minDate.toString());
        Log.d("OverviewFragment", "Maxdate: " + maxDate.toString());

        totalDays = 0;
        totalMonths = 0.0;
        totalDays = maxDate.getTime() - minDate.getTime();
        //TimeUnit.DAYS.convert(totalDays, TimeUnit.MILLISECONDS);
        totalDays /= 86400000;
    }

    public String calculateAveragePerDay() {
        if(totalDays == 0) { return "$0.00"; }
        averagePerDay = totalSpent / (double) totalDays;
        Log.d("OverviewFragment", "Calculated average spent/day: " + averagePerDay);
        String average = Double.toString(Math.abs(averagePerDay));
        average = average.substring(0,average.indexOf('.')+2);
        if(averagePerDay < 0) average = "-$" + average;
        else average = "$" + average;
        return "Average Spent Per Day: " + average;
    }

    public String calculateTotalPerMonth() {
        totalMonths = (double) totalDays / 30.4;
        if(totalMonths == 0) return "$0.00";
        Log.d("OverviewFragment", "totalMonths: " + totalMonths);
        averagePerMonth = totalSpent / totalMonths;
        String average = Double.toString(Math.abs(averagePerMonth));
        average = average.substring(0,average.indexOf('.')+2);
        average = "$" + average;
        return "Average Delta Per Month: " + average;
    }

    public void calculateTotalsPerCategory() {
        for(Transaction transaction : transactionList) {
            String subcategory = transaction.getCategory().trim();
            String category = "";
            boolean categoryFound = false;
            for(String grouping : categories.keySet()){
                if(categories.get(grouping).contains(subcategory)){
                    category = grouping;
                    categoryFound = true;
                    break;
                }
            }
            if(!categoryFound) category = "Uncategorized";
            if(!(category.trim().toLowerCase().equals("income") || category.trim().toLowerCase().equals("transfer"))){
                if(categoryTotals.containsKey(category)) {
                    categoryTotals.put(category, categoryTotals.get(category) + transaction.getAmount());
                } else {
                    categoryTotals.put(category,transaction.getAmount());
                }
            }
        }
    }
    
    public void setUpCategoryPieChart() {
        categoryPieChart.setUsePercentValues(false);
        categoryPieChart.getDescription().setEnabled(false);
        categoryPieChart.setExtraOffsets(5, 10, 5, 5);
        categoryPieChart.setDragDecelerationFrictionCoef(0.9f);
//        categoryPieChart.setCenterTextTypeface(mTfLight);
        categoryPieChart.setCenterText("Spending by\nCategory");
        categoryPieChart.setDrawHoleEnabled(true);
        categoryPieChart.setHoleRadius(40f);
        categoryPieChart.setHoleColor(Color.WHITE);
        categoryPieChart.setTransparentCircleColor(Color.WHITE);
        categoryPieChart.setTransparentCircleAlpha(110);
        categoryPieChart.setTransparentCircleRadius(44f);
        categoryPieChart.setDrawCenterText(true);
        categoryPieChart.setRotationAngle(-30);
        categoryPieChart.setEntryLabelColor(Color.BLACK);
        categoryPieChart.setEntryLabelTextSize(10f);
        // enable rotation of the chart by touch
        categoryPieChart.setRotationEnabled(true);
        categoryPieChart.setHighlightPerTapEnabled(true);
        categoryPieChart.getLegend().setCustom(new ArrayList<LegendEntry>(0));

        setCategoryPieChartData();

        categoryPieChart.animateY(1200, Easing.EasingOption.EaseOutCirc);
    }

    public void setCategoryPieChartData() {
        ArrayList<PieEntry> entries = new ArrayList<>();

        // NOTE: The order of the entries when being added to the entries array determines their position around the center of
        // the chart.
        categoryTotals = MapSort.sortByValue(categoryTotals);
        List<String> list = new ArrayList<>(categoryTotals.keySet());
        int size = list.size();
        // this interlaces the sizes of the slices to ease readability
        int low = 0;
        int high = size - 1;
        for(int i = 0; i < size; i++) {
            if(i%2 == 0) { //even
                entries.add(new PieEntry(categoryTotals.get(list.get(low)).floatValue(),list.get(low)));
                low++;
            } else { //odd
                entries.add(new PieEntry(categoryTotals.get(list.get(high)).floatValue(), list.get(high)));
                high--;
            }
        }
//        for (Map.Entry<String, Double> entry : categoryTotals.entrySet()){
//            if(!entry.getKey().equals("Payment")) entries.add(new PieEntry(entry.getValue().floatValue(),entry.getKey()));
//        }
        PieDataSet dataSet = new PieDataSet(entries,"Spending by Category");

        dataSet.setDrawIcons(false);
        dataSet.setSliceSpace(0f);
        dataSet.setIconsOffset(new MPPointF(0, 40));
        dataSet.setSelectionShift(5f);

        ArrayList<Integer> colors = new ArrayList<>();
        for (int c : ColorTemplate.JOYFUL_COLORS)
            colors.add(c);
        colors.add(0xFFE57373);
        colors.add(0xFF4FC3F7);
        colors.add(0xFFFF8A65);
        colors.add(0xFFAED581);
        colors.add(0xFFBA68C8);
        colors.add(0xFFF06292);
        colors.add(0xFF64B5F6);
        colors.add(0xFF4DB6AC);
        colors.add(0xFFFFB74D);
        colors.add(0xFFDCE775);
        dataSet.setColors(colors);
        PieData data = new PieData(dataSet);
        data.setValueFormatter(new com.github.mikephil.charting.formatter.LargeValueFormatter());
        data.setValueTextSize(11f);
        data.setValueTextColor(Color.BLACK);
//        data.setValueTypeface(mTfLight);
        categoryPieChart.setData(data);

        // undo all highlights
        categoryPieChart.highlightValues(null);

        categoryPieChart.invalidate();
    }

}






