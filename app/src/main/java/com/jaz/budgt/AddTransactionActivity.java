package com.jaz.budgt;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.app.DatePickerDialog;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by jaz on 8/23/17.
 */

public class AddTransactionActivity extends AppCompatActivity
        implements View.OnClickListener,
        SelectCategoryFragment.OnSelectedListener,
        SelectAccountFragment.OnSelectedListener {

    static final int RESULT_OK = 2;
    Map<String,ArrayList<String>> categories = new HashMap<>();
    ArrayList<String> paymentTypeList = new ArrayList<>(0);
    LocalStorage localStorage;

    Button dateButton, todayButton, doneButton, categoryButton, paymentTypeButton;
    TextView selectedDate, transactionAmount, transactionDescription;
    RadioButton expenseButton, incomeButton;
    private int mYear, mMonth, mDay;
    private boolean dateChanged = false;
    final Calendar c = Calendar.getInstance();
    Transaction transaction = new Transaction();
    String account = "";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_transaction);
        localStorage = new LocalStorage(this);

        categories = localStorage.loadCategories();
        paymentTypeList = localStorage.loadAccounts();

        // add back button to action bar
        //todo fix this warning
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Setup items
        dateButton = (Button) findViewById(R.id.date_picker);
        categoryButton = (Button) findViewById(R.id.select_category);
        paymentTypeButton = (Button) findViewById(R.id.select_payment_type);
        doneButton = (Button) findViewById(R.id.done_button);
        dateButton.setOnClickListener(this);
        categoryButton.setOnClickListener(this);
        doneButton.setOnClickListener(this);
        paymentTypeButton.setOnClickListener(this);

        expenseButton = (RadioButton) findViewById(R.id.expense_button);
        incomeButton = (RadioButton) findViewById(R.id.income_button);
        expenseButton.setChecked(true);

        dateButton.setText(R.string.select_date);
        selectedDate = (TextView) findViewById(R.id.selected_date);
        transactionAmount = (EditText) findViewById(R.id.transaction_amount);
        transactionDescription = (EditText) findViewById(R.id.transaction_description);



        // Set date as today by default
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH) + 1;
        mDay = c.get(Calendar.DAY_OF_MONTH);
        selectedDate.setText("Date: " + mMonth + "/" + mDay + "/" + mYear);
    }

    @Override
    public void onClick(View v) {
        constrainDollarInput();
        if (v == dateButton) {
            // Get Current Date
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                    new DatePickerDialog.OnDateSetListener() {

                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {

                            selectedDate.setText("Date: " + (monthOfYear+1) + "/" + dayOfMonth + "/" + year);
                            transaction.setDate(dayOfMonth,monthOfYear+1,year);
                            dateChanged = true;
                            Log.d("AddTransactionActivity","Set date to " + (monthOfYear+1) + "/" + dayOfMonth + "/" + year);
                        }
                    }, mYear, mMonth, mDay);
            datePickerDialog.show();
        } if(v == categoryButton){
            openCategoryPicker();
        } if(v == paymentTypeButton) {
            openPaymentTypePicker();
        } if(v == doneButton) {
            if(checkAndCreateTransaction()) {
                Intent intent = new Intent();
                intent.putExtra("NewTransaction", transaction.toJsonString());
                setResult(RESULT_OK, intent);
                Log.d("AddTransactionActivity", "Sending this transaction... " + transaction.toString());
                finish();
            }
        }
        // hide the keyboard after a button press
        hideSoftKeyboard(this,v);
    }

    public static void hideSoftKeyboard(Activity activity, View view) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(
                view.getApplicationWindowToken(), 0);
    }

    public void openCategoryPicker() {
        DialogFragment newFragment = new SelectCategoryFragment();
        newFragment.show(getFragmentManager(),"category");
    }

    public void openPaymentTypePicker() {
        DialogFragment newFragment = new SelectAccountFragment();
        newFragment.show(getFragmentManager(),"paymentType");
    }

    public boolean checkAndCreateTransaction() {
        boolean ret = false;
        if(transactionDescription.getText().toString().length() > 0) {
            transaction.setDescription(transactionDescription.getText().toString());
            constrainDollarInput();
            if(transactionAmount.getText().toString().length() > 0) {
                String[] parts = transactionAmount.getText().toString().split("\\.");
                transaction.setDollarAmount(Integer.parseInt(parts[0]));
                transaction.setCentAmount(Integer.parseInt(parts[1]));
                if(transaction.getDollarAmount() == 0 && transaction.getCentAmount() == 0) {
                    shortToast(R.string.no_zero);
                    return false;
                }
                if (transaction.getCategory().toString().length() > 0) {
                    if (transaction.getPaymentType().length() > 0) {
                        if(!dateChanged) transaction.setDate(mDay,mMonth,mYear);
                        // Transaction has required fields, continue:
                        ret = true;
                        if (expenseButton.isChecked())
                            transaction.setIsExpense(1);
                        else transaction.setIsExpense(0);
                        Log.d("AddTransactionActivity", transaction.toString());
                        // add transaction to list of transactions, return to transaction list
                    } else { shortToast(R.string.no_payment_type); }
                } else { shortToast(R.string.no_category); }
            } else { shortToast(R.string.no_amount); }
        } else { shortToast(R.string.no_description); }
        return ret;
    }

    public void constrainDollarInput() {
        // constrain input on decimals for dollars
        String amt = transactionAmount.getText().toString();

        if(amt.length() > 0) {
            if(amt.contains(".")) {
                if(amt.equals(".")) {
                    amt = "";
                } else {
                    if(amt.charAt(0) == '.') {
                        amt = "0" + amt;
                    } else if(amt.charAt(amt.length()-1) == '.') {
                        amt += "00";
                    }
                    String[] parts = amt.split("\\.");
                    //Log.d("AddTransactionActivity", "parts[0]=" + parts[0] + "parts[1] = " + parts[1]);
                    if (parts.length == 2) {
                        if (parts[1].length() > 2) {
                            //truncate
                            parts[1] = parts[1].substring(0, 2);
                        } else if (parts[1].length() == 1) {
                            parts[1] += "0";
                        }
                        amt = parts[0] + "." + parts[1];
                    }
                    while (amt.charAt(0) == '0' && amt.charAt(1) != '.') {
                        amt = amt.substring(1);
                    }
                }
            } else {
                amt += ".00";
            }
        }

        transactionAmount.setText(amt);
    }

    public void shortToast(int text) {
        Toast.makeText(getApplicationContext(),text,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void categorySelected(Category category) {
        transaction.setCategory(category);
        categoryButton.setText(category.toString());
    }

    @Override
    public void accountSelected(int index) {
        transaction.setPaymentType(paymentTypeList.get(index));
        paymentTypeButton.setText(paymentTypeList.get(index));
    }

    @Override
    public void onResume() {
        super.onResume();
        categories = localStorage.loadCategories();
        paymentTypeList = localStorage.loadAccounts();
    }

}



