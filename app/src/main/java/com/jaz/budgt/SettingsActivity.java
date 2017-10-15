package com.jaz.budgt;

import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by jaz on 8/22/17.
 */

public class SettingsActivity extends AppCompatActivity
        implements SelectCategoryGroupFragment.OnSelectedListener {

    ArrayList<Transaction> transactionList = new ArrayList<>(0);
    ArrayList<Account> accounts = new ArrayList<>(0);
    LocalStorage localStorage;
    //todo clean this page up and organize by categories, accounts, transactions, etc

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
        localStorage  = new LocalStorage(this);

        accounts = localStorage.loadAccounts();

        Button clearTransactionsButton = (Button) findViewById(R.id.clear_transactions_button);
        clearTransactionsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                localStorage.deleteTransactions();
                Toast.makeText(getApplicationContext(),getString(R.string.cleared_transactions),Toast.LENGTH_SHORT).show();
            }
        });

        Button addCategoryButton = (Button) findViewById(R.id.add_category_button);
        addCategoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment newFragment = new SelectCategoryGroupFragment();
                newFragment.show(getFragmentManager(),"category group");
            }
        });

        Button addAccountButton = (Button) findViewById(R.id.add_account_button);
        addAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openNewAccountFragment();
            }
        });

        Button deleteAccountsButton = (Button) findViewById(R.id.delete_accounts_button);
        deleteAccountsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                localStorage.deleteAccounts();
                Toast.makeText(getApplicationContext(),getString(R.string.deleted_accounts),Toast.LENGTH_SHORT).show();
            }
        });

        Button loadFromCSVButton = (Button) findViewById(R.id.load_from_csv_button);
        loadFromCSVButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),getString(R.string.loading_from_csv),Toast.LENGTH_SHORT).show();
                csvToTransactions();
            }
        });

        Button loadDefaultCategoriesButton = (Button) findViewById(R.id.load_default_categories_button);
        loadDefaultCategoriesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadDefaultCategories();
            }
        });

        Button loadDefaultAccountsButton = (Button) findViewById(R.id.load_default_accounts_button);
        loadDefaultAccountsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadDefaultAccounts();
            }
        });
    }

    //todo fix this! make it show a list of existing groups with option to make new group, then add category
//    public void openNewCategoryFragment() {
//        AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());
//        builder.setTitle("Add New Category");
//
//        // Set up the input
//        final EditText input = new EditText(getApplicationContext());
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
        AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());
        builder.setTitle("Add New Account");

        // Set up the input
        final EditText input = new EditText(getApplicationContext());
        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        // Set up the buttons
        builder.setPositiveButton("Add Account", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String name = input.getText().toString().trim();
                if((name.length() > 0)) addAccount(new Account(name),false);
                else Toast.makeText(getApplicationContext(),R.string.please_enter_name,Toast.LENGTH_SHORT).show();
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

    public void addNewCategory(String newCategoryGroup, String newCategory) {
        Map<String,ArrayList<String>> categories = localStorage.loadCategories();
        ArrayList<String> categoryList = categories.get(newCategoryGroup);
        if(!categoryList.contains(newCategory)) {
            categories.get(newCategoryGroup).add(newCategory);
        } else Toast.makeText(getApplicationContext(),R.string.duplicate_category,Toast.LENGTH_SHORT).show();
        localStorage.saveCategories(categories);
    }

    public void categoryGroupSelected(final String categoryGroup){
        AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());
        builder.setTitle("Add category within " + categoryGroup);

        // Set up the input
        final EditText input = new EditText(getApplicationContext());
        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        // Set up the buttons
        builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String category = input.getText().toString().trim();
                addNewCategory(categoryGroup, category);
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

    public void addAccount(Account newAccount, boolean quiet) {
        ArrayList<Account> accounts = localStorage.loadAccounts();
        if(!accounts.contains(newAccount)) accounts.add(newAccount);
        else if(!quiet) Toast.makeText(getApplicationContext(),R.string.duplicate_account,Toast.LENGTH_SHORT).show();
        localStorage.saveAccounts(accounts);
    }

    public void csvToTransactions() {
        InputStream inputStream = getResources().openRawResource(R.raw.budget);
        CSVHandler csvFile = new CSVHandler(inputStream);
        ArrayList<String[]> rawList = csvFile.read(",");

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
            category = row[categoryIndex].trim();
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
        Toast.makeText(getApplicationContext(),getString(R.string.finished),Toast.LENGTH_SHORT).show();
    }

    public void loadDefaultCategories() {
        InputStream inputStream = getResources().openRawResource(R.raw.categories);
        CSVHandler csvFile = new CSVHandler(inputStream);
        ArrayList<String[]> rawList = csvFile.read(",");

        final Map<String,ArrayList<String>> categories = new HashMap<>();

        for(String[] row : rawList) {
            ArrayList<String> subcategories = new ArrayList<>(0);
            for(int i = 1; i < row.length; i++) {
                subcategories.add(row[i]);
            }
            categories.put(row[0],subcategories);
        }

        // double check with the user
        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.myDialog));
        builder.setTitle("Set Default Categories?")
        .setMessage("This will delete all current categories and load the default categories.")
        .setPositiveButton("Reset to Defaults", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                localStorage.saveCategories(categories);
            }
        })
        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        })
        .show();

    }

    public void loadDefaultAccounts() {
        InputStream inputStream = getResources().openRawResource(R.raw.accounts);
        CSVHandler csvFile = new CSVHandler(inputStream);
        ArrayList<String[]> rawList = csvFile.read(",");

        final ArrayList<Account> defaultAccountList = new ArrayList<>(0);
        for(String[] row : rawList) {
            for(int i = 0; i < row.length; i++) {
                defaultAccountList.add(new Account(row[i]));
            }
        }
        // double check with the user
        AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());
        builder.setTitle("Set Default Accounts?");
        builder.setMessage("This will delete ALL accounts and ALL associated transaction data.");
        builder.setPositiveButton("Reset to Defaults", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                localStorage.saveAccounts(defaultAccountList);
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

}










