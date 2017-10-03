package com.jaz.budgt;

import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.app.DialogFragment;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * Created by jaz on 8/22/17.
 */

public class SettingsFragment extends Fragment {
    public static SettingsFragment newInstance() {
        return new SettingsFragment();
    }
    ArrayList<Transaction> transactionList = new ArrayList<>(0);
    LocalStorage localStorage;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        localStorage  = new LocalStorage(this.getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.settings_fragment, container, false);

        Button clearTransactionsButton = view.findViewById(R.id.clear_transactions_button);
        clearTransactionsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                localStorage.deleteTransactions();
                Toast.makeText(getContext(),getString(R.string.cleared_transactions),Toast.LENGTH_SHORT).show();
            }
        });

        Button addCategoryButton = view.findViewById(R.id.add_category_button);
        addCategoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openNewCategoryFragment();
            }
        });

        Button addPaymentTypeButton = view.findViewById(R.id.add_payment_type_button);
        addPaymentTypeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openNewPaymentTypeFragment();
            }
        });

        Button deletePaymentTypesButton = view.findViewById(R.id.delete_payment_types_button);
        deletePaymentTypesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                localStorage.deletePaymentTypes();
                Toast.makeText(getContext(),getString(R.string.cleared_payment_types),Toast.LENGTH_SHORT).show();
            }
        });

        Button loadFromCSVButton = view.findViewById(R.id.load_from_csv_button);
        loadFromCSVButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(),getString(R.string.loading_from_csv),Toast.LENGTH_SHORT).show();
                csvToTransactions();
            }
        });

        return view;
    }

    public void openNewCategoryFragment() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Add New Category");

        // Set up the input
        final EditText input = new EditText(getContext());
        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        // Set up the buttons
        builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String category = input.getText().toString().trim();
                addNewCategory(category, false);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    public void openNewPaymentTypeFragment() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Add New Payment Type");

        // Set up the input
        final EditText input = new EditText(getContext());
        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        // Set up the buttons
        builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String type = input.getText().toString().trim();
                addNewPaymentType(type,false);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    public void addNewCategory(String newCategory, boolean quiet) {
        ArrayList<String> categoryList = localStorage.loadCategories();
        if(!categoryList.contains(newCategory)) categoryList.add(newCategory);
        else if(!quiet) Toast.makeText(getContext(),R.string.duplicate_category,Toast.LENGTH_SHORT).show();
        localStorage.saveCategories(categoryList);
    }

    public void addNewPaymentType(String newType, boolean quiet) {
        ArrayList<String> paymentTypeList = localStorage.loadPaymentTypes();
        if(!paymentTypeList.contains(newType)) paymentTypeList.add(newType);
        else if(!quiet) Toast.makeText(getContext(),R.string.duplicate_payment_type,Toast.LENGTH_SHORT).show();
        localStorage.savePaymentTypes(paymentTypeList);
    }

    public void csvToTransactions() {
        InputStream inputStream = getResources().openRawResource(R.raw.budget);
        CSVHandler csvFile = new CSVHandler(inputStream);
        ArrayList<String[]> rawList = csvFile.read();

        //TODO make customizeable

        for(String[] row : rawList) {
            if(row[0].equals("Date")) continue;
            Transaction transaction = new Transaction();
            //date
            String[] dateParts = row[0].split("/");
            transaction.setDate(Integer.parseInt(dateParts[1]),Integer.parseInt(dateParts[0]),Integer.parseInt(dateParts[2]));

            //amount
            String[] amountParts = row[1].split("\\.");
            amountParts[0] = amountParts[0].replace("$","");
            int dollar, cent = 0;
            if(amountParts[0].contains("-")) transaction.setIsExpense(1);
            else transaction.setIsExpense(0);
            dollar = Integer.parseInt(amountParts[0].replace("-",""));
            cent = Integer.parseInt(amountParts[1]);
            transaction.setDollarAmount(dollar);
            transaction.setCentAmount(cent);


            //description
            String description = "";
            for(int i = 2; i < row.length - 2; i++) {
                description += row[i];
            }
            description = description.replace("\"","");
            transaction.setDescription(description);

            //category
            String category = "";
            int categoryIndex = row.length-2;
            category = row[categoryIndex];
            transaction.setCategory(category);
            addNewCategory(category, true);

            //payment type
            String paymentType = "";
            int paymentTypeIndex = row.length-1;
            paymentType = row[paymentTypeIndex];
            transaction.setPaymentType(paymentType);
            addNewPaymentType(paymentType, true);

            transactionList.add(transaction);
        }
        localStorage.saveTransactions(transactionList);
        Toast.makeText(getContext(),getString(R.string.finished),Toast.LENGTH_SHORT).show();
    }

    public void loadDefaultCategories() {
        //todo make this pull from a csv
        ArrayList<CategoryGroup> categoryGroups = new ArrayList<>(0);
        String[] transportationList = {"Car Insurance", "Car Payment", "Gas & Fuel", "Parking","Tolls","Taxi/Uber/Lyft","Public Transportation","Service & Parts"};
        categoryGroups.add(new CategoryGroup("Transportation",transportationList));
        String[] utilityList = {"Internet","Mobile Phone","Cable","Electricity","Water & Sewage"};
        categoryGroups.add(new CategoryGroup("Utilities",utilityList));
        String[] entertainmentList = {"Movies","Music","Concerts","Events","Alcohol","Books","Video Games"};
        categoryGroups.add(new CategoryGroup("Entertainment",entertainmentList));
        String[] foodList = {"Groceries","Restaurants"};
        categoryGroups.add(new CategoryGroup("Food",foodList));
        String[] healthList = {"Dental","Medical","Eyecare","Gym","Personal Care","Health Insurance","Medicine"};
        categoryGroups.add(new CategoryGroup("Health",healthList));
        String[] homeList = {"Rent","Home Insurance","Home Supplies","Cleaning Supplies"};
        categoryGroups.add(new CategoryGroup("Home",homeList));
        String[] incomeList = {"Bonus","Cashback","Paycheck","Interest Income","Reimbursement","Returns"};
        categoryGroups.add(new CategoryGroup("Income",incomeList));
        String[] hobbiesList = {"Sports","Electronics","Software","Raw Materials","Parks","Sporting Goods","Memberships","Collectibles"};
        categoryGroups.add(new CategoryGroup("Hobbies",hobbiesList));
        String[] taxesList = {"Federal Tax","Local Tax","Property Tax","State Tax"};
        categoryGroups.add(new CategoryGroup("Taxes",taxesList));
        String[] transferList = {"Credit Card Payment","ATM Cash"};
        categoryGroups.add(new CategoryGroup("Transfer",transferList));
        String[] travelList = {"Airfare","Lodging","Trains","Auto Transport","Rentals"};
        categoryGroups.add(new CategoryGroup("Travel",travelList));
        //todo connect this to load categories in localstorage
    }

}










