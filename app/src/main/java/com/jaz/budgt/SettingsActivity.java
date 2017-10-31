package com.jaz.budgt;

import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.PrintWriter;
import java.net.URI;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
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
    static final int SELECT_FILE_REQUEST = 2;
    String[] settingsOptions = {"Add New Category","Add New Account",
            "Load Transactions from CSV","Load Categories from CSV","Load Accounts from CSV",
            "Export Transactions to CSV",
            "Delete Transactions","Delete All Accounts"};
    //todo clean this page up and organize by categories, accounts, transactions, etc

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
        localStorage  = new LocalStorage(this);

        accounts = localStorage.loadAccounts();
        transactionList = localStorage.loadTransactions();

        ListView listview = (ListView) findViewById(R.id.settings_list);
        final ListAdapter adapter = new SettingsListAdapter(getApplicationContext(), settingsOptions);
        listview.setAdapter(adapter);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                switch (settingsOptions[position]) {
                    case "Add New Category":
                        DialogFragment newFragment = new SelectCategoryGroupFragment();
                        newFragment.show(getFragmentManager(),"category group");
                        break;
                    case "Add New Account":
                        openNewAccountFragment();
                        break;
                    case "Load Transactions from CSV":
                        //todo make this allow the user to choose an import file
                        //todo make this popup a dialog with a choice of delimiters
                        // (maybe be super smart and detect the most common character in the file)
                        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                        intent.setType("file/*");
                        startActivityForResult(intent, SELECT_FILE_REQUEST);
                        //Toast.makeText(getApplicationContext(),getString(R.string.loading_from_csv),Toast.LENGTH_SHORT).show();
                        //loadTransactionsFromCSV(";");
                        break;
                    case "Load Categories from CSV":
                        loadDefaultCategories();
                        break;
                    case "Load Accounts from CSV":
                        loadDefaultAccounts();
                        break;
                    case "Export Transactions to CSV":
                        exportTransactionsToCSV();
                        break;
                    case "Delete Transactions":
                        localStorage.deleteTransactions();
                        Toast.makeText(getApplicationContext(),getString(R.string.deleted_transactions),Toast.LENGTH_SHORT).show();
                        break;
                    case "Delete All Accounts":
                        localStorage.deleteAccounts();
                        Toast.makeText(getApplicationContext(),getString(R.string.deleted_accounts),Toast.LENGTH_SHORT).show();
                        break;
                }

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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("SettingsFragment","File selected");
        if(requestCode > 0 && data != null) {
            Log.d("SettingsFragment","Data: " + data.toString());
            loadTransactionsFromCSV(";",data.getData());
        } else {
            Log.d("SettingsFragment","Something wrong with selecting a file");
            Toast.makeText(getApplicationContext(),getString(R.string.no_file_selected),Toast.LENGTH_SHORT).show();
        }
    }

    public void loadTransactionsFromCSV(String delimiter, Uri filepath) {
        ArrayList<String[]> rawList = new ArrayList<>(0);
        try {
            Log.d("ReadingFile","Path: " + filepath.toString());
            BufferedReader reader = new BufferedReader(new FileReader(filepath.toString().substring(6)));
            rawList = CSVHandler.readFromExternalFile(reader,delimiter);
            Log.d("SettingsActivity","File successfully read");
        }
        catch (java.io.FileNotFoundException e) {
            Toast.makeText(getApplicationContext(),getString(R.string.file_not_found),Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }

        //TODO make customizeable

        for(String[] row : rawList) {
            String rowString = "";
            for(int i = 0; i<row.length; i++) rowString += row[i] + " ";
            Log.d("SettingsActivity","Parsing: " + rowString);
            if(row[0].equals("Date") || row[0].length()==0) continue;
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
            description = description.replace(";","");
            transaction.setDescription(description);

            //category
            String category = "";
            int categoryIndex = row.length-2;
            category = row[categoryIndex].trim();
            category = category.replace(";","");
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
        if(transactionList.size() == 0) {
            Toast.makeText(getApplicationContext(),getString(R.string.no_transactions),Toast.LENGTH_SHORT).show();
        } else {
            localStorage.saveTransactions(transactionList);
            localStorage.saveAccounts(accounts);
            Toast.makeText(getApplicationContext(),getString(R.string.finished),Toast.LENGTH_SHORT).show();
        }
    }

    public void exportTransactionsToCSV() {
        String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date()).replace(" ","_").replace(",","").replace(":","");
        String filename = "exported_transactions_" + currentDateTimeString + ".csv";
        Toast.makeText(getApplicationContext(),getString(R.string.starting_export),Toast.LENGTH_SHORT).show();
        String transactionData = "";
        transactionList = localStorage.loadTransactions();
        for(Transaction transaction : transactionList) {
            transactionData += transaction.toCSVString(";") + "\n";
        }
        File file = null;
        if(CSVHandler.isExternalStorageWritable()) {
            file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), filename);
            try {
                FileOutputStream f = new FileOutputStream(file);
                PrintWriter pw = new PrintWriter(f);
                pw.println(transactionData);
                pw.flush();
                pw.close();
                try{ f.close(); }
                catch (java.io.IOException e) { e.printStackTrace(); }
                Toast.makeText(getApplicationContext(),getString(R.string.finished),Toast.LENGTH_SHORT).show();
                Log.d("SettingsActivity","Wrote file. Contents:\n" + transactionData);
                //todo add snackbar "file exported" maybe with open file button
            } catch(java.io.FileNotFoundException e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(),getString(R.string.unable_to_write_file),Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getApplicationContext(),getString(R.string.unable_to_write_file),Toast.LENGTH_SHORT).show();
        }

    }

    public void loadDefaultCategories() {
        ArrayList<String[]> rawList = CSVHandler.readFromResource(getResources().openRawResource(R.raw.categories),",");

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
        ArrayList<String[]> rawList = CSVHandler.readFromResource(getResources().openRawResource(R.raw.accounts),",");

        final ArrayList<Account> defaultAccountList = new ArrayList<>(0);
        for(String[] row : rawList) {
            for(int i = 0; i < row.length; i++) {
                defaultAccountList.add(new Account(row[i].trim()));
            }
        }
        // double check with the user
        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.myDialog));
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










