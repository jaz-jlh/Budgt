package com.jaz.budgt;

import android.content.DialogInterface;
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


import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by jaz on 8/22/17.
 */

public class SettingsFragment extends Fragment {
    public static SettingsFragment newInstance() {
        return new SettingsFragment();
    }
    ArrayList<Transaction> transactionList = new ArrayList<>(0);
    ArrayList<Account> accounts = new ArrayList<>(0);
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

        accounts = localStorage.loadAccounts();

        Button clearTransactionsButton = view.findViewById(R.id.clear_transactions_button);
        clearTransactionsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                localStorage.deleteTransactions();
                Toast.makeText(getContext(),getString(R.string.cleared_transactions),Toast.LENGTH_SHORT).show();
            }
        });

        Button addCategoryButton = view.findViewById(R.id.add_category_button);
//        addCategoryButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                openNewCategoryFragment();
//            }
//        });

        Button addAccountButton = view.findViewById(R.id.add_account_button);
        addAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openNewAccountFragment();
            }
        });

        Button deleteAccountsButton = view.findViewById(R.id.delete_accounts_button);
        deleteAccountsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                localStorage.deleteAccounts();
                Toast.makeText(getContext(),getString(R.string.deleted_accounts),Toast.LENGTH_SHORT).show();
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

        Button loadDefaultCategoriesButton = view.findViewById(R.id.load_default_categories_button);
        loadDefaultCategoriesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadDefaultCategories();
            }
        });

        Button loadDefaultAccountsButton = view.findViewById(R.id.load_default_accounts_button);
        loadDefaultAccountsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadDefaultAccounts();
            }
        });

        return view;
    }

    //todo fix this! make it show a list of existing groups with option to make new group, then add category
//    public void openNewCategoryFragment() {
//        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
//        builder.setTitle("Add New Category");
//
//        // Set up the input
//        final EditText input = new EditText(getContext());
//        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
//        input.setInputType(InputType.TYPE_CLASS_TEXT);
//        builder.setView(input);
//
//        // Set up the buttons
//        builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                String category = input.getText().toString().trim();
//                addNewCategory(category, false);
//            }
//        });
//        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                dialog.cancel();
//            }
//        });
//
//        builder.show();
//    }

    public void openNewAccountFragment() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Add New Account");

        // Set up the input
        final EditText input = new EditText(getContext());
        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        // Set up the buttons
        builder.setPositiveButton("Add Account", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String name = input.getText().toString().trim();
                if((name.length() > 0)) addAccount(new Account(name),false);
                else Toast.makeText(getContext(),R.string.please_enter_name,Toast.LENGTH_SHORT).show();
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

    //todo fix this so you can use it!
//    public void addNewCategory(String newCategory, boolean quiet) {
//        ArrayList<> categoryList = localStorage.loadCategories();
//        Category c = new Category(newCategory.trim());
//        if(!categoryList.contains(c)) categoryList.add(c);
//        else if(!quiet) Toast.makeText(getContext(),R.string.duplicate_category,Toast.LENGTH_SHORT).show();
//        localStorage.saveCategories(categoryList);
//    }

    public void addAccount(Account newAccount, boolean quiet) {
        ArrayList<Account> accounts = localStorage.loadAccounts();
        if(!accounts.contains(newAccount)) accounts.add(newAccount);
        else if(!quiet) Toast.makeText(getContext(),R.string.duplicate_account,Toast.LENGTH_SHORT).show();
        localStorage.saveAccounts(accounts);
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
            if(amountParts[0].contains("-")) transaction.setIsExpense(true);
            else transaction.setIsExpense(false);
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
            //todo unbreak this!!!
            //addNewCategory(category, true);

            //account
            int accountIndex = row.length-1;
            String accountName = row[accountIndex];
            boolean accountFound = false;
            for(Account account : accounts) {
                if(account.getName().trim().toLowerCase().equals(accountName.trim().toLowerCase())){
                    transaction.setAccount(account.getName());
                    account.addTransaction(transaction);
                    accountFound = true;
                    break;
                }
            }
            if(!accountFound) {
                accounts.add(new Account(accountName));
            }

            transactionList.add(transaction);
        }
        localStorage.saveTransactions(transactionList);
        localStorage.saveAccounts(accounts);
        Toast.makeText(getContext(),getString(R.string.finished),Toast.LENGTH_SHORT).show();
    }

    public void loadDefaultCategories() {
        //todo make this pull from a csv
        Map<String,ArrayList<String>> categories = new HashMap<>();

        String[] transportationList = {"Car Insurance", "Car Payment", "Gas & Fuel", "Parking","Tolls","Taxi/Uber/Lyft","Public Transportation","Service & Parts","Registration/Tax"};
        String[] utilityList = {"Internet","Mobile Phone","Cable","Electricity","Water & Sewage"};
        String[] entertainmentList = {"Movies","Music","Concerts","Events","Alcohol","Books","Video Games"};
        String[] foodList = {"Groceries","Restaurants"};
        String[] healthList = {"Dental","Medical","Eyecare","Gym","Personal Care","Health Insurance","Medicine"};
        String[] homeList = {"Rent","Home Insurance","Home Supplies","Cleaning Supplies"};
        String[] incomeList = {"Bonus","Cashback","Paycheck","Interest Income","Reimbursement","Returns","Contribution"};
        String[] hobbiesList = {"Sports","Electronics","Software","Raw Materials","Parks","Sporting Goods","Memberships","Collectibles"};
        String[] taxesList = {"Federal Tax","Local Tax","Property Tax","State Tax"};
        String[] transferList = {"Credit Card Payment","ATM Cash"};
        String[] travelList = {"Airfare","Lodging","Trains","Auto Transport","Rentals"};
        categories.put("Transportation",new ArrayList<>(Arrays.asList(transportationList)));
        categories.put("Utilities",new ArrayList<>(Arrays.asList(utilityList)));
        categories.put("Entertainment",new ArrayList<>(Arrays.asList(entertainmentList)));
        categories.put("Food",new ArrayList<>(Arrays.asList(foodList)));
        categories.put("Health",new ArrayList<>(Arrays.asList(healthList)));
        categories.put("Home",new ArrayList<>(Arrays.asList(homeList)));
        categories.put("Income",new ArrayList<>(Arrays.asList(incomeList)));
        categories.put("Hobbies",new ArrayList<>(Arrays.asList(hobbiesList)));
        categories.put("Taxes",new ArrayList<>(Arrays.asList(taxesList)));
        categories.put("Transfer",new ArrayList<>(Arrays.asList(transferList)));
        categories.put("Travel",new ArrayList<>(Arrays.asList(travelList)));
        //todo make this show an "are you sure?" dialog
        localStorage.saveCategories(categories);
    }

    public void loadDefaultAccounts() {
        //todo make this show an "are you sure?" dialog
        accounts = new ArrayList<>(0);
        accounts.add(new Account("PNC Checking"));
        accounts.add(new Account("PNC Savings"));
        accounts.add(new Account("PNC Reserve"));
        accounts.add(new Account("Discover"));
        accounts.add(new Account("Venmo Balance"));
        accounts.add(new Account("Vanguard Roth IRA"));
        accounts.add(new Account("Fidelity 401(k)"));
        accounts.add(new Account("Cash"));
        accounts.add(new Account("Union Checking"));
        accounts.add(new Account("HSA"));
        localStorage.saveAccounts(accounts);
    }

}










