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

import java.util.Calendar;


/**
 * Created by jaz on 8/23/17.
 */

public class AddTransactionActivity extends AppCompatActivity
        implements View.OnClickListener,
        SelectCategoryFragment.OnSelectedListener,
        SelectPaymentTypeFragment.OnSelectedListener {
    String[] categories = {"Groceries","Transportation","Meals Out"};
    String[] paymentTypes = {"Discover card","PNC Debit card","Cash"};
    static final int RESULT_OK = 2;

    Button dateButton, todayButton, doneButton, categoryButton, paymentTypeButton;
    TextView selectedDate, transactionAmount, transactionDescription;
    RadioButton expenseButton, incomeButton;
    private int mYear, mMonth, mDay;
    final Calendar c = Calendar.getInstance();
    Transaction transaction = new Transaction();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_transaction);

        // add back button to action bar
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
        transaction.setDate(mDay,mMonth,mYear);
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
                            transaction.setDate(mDay,mMonth,mYear);
                            Log.d("AddTransactionActivity",transaction.toString());
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
                intent.putExtra("NewTransaction", transaction.toStringArray());
                setResult(RESULT_OK, intent);
                Log.d("AddTransactionActivity", "Sending this transation... " + transaction.toString());
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
        DialogFragment newFragment = new SelectPaymentTypeFragment();
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
                if (transaction.getCategory().length() > 0) {
                    if (transaction.getPaymentType().length() > 0) {
                        // Transaction has required fields, continue:
                        ret = true;
                        if (expenseButton.isChecked())
                            transaction.setTransactionType(Transaction.TransactionType.EXPENSE);
                        else transaction.setTransactionType(Transaction.TransactionType.INCOME);
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
        if(amt.length() > 0){
            if(amt.contains(".")){
                String[] parts = amt.split("\\.");
                //Log.d("AddTransactionActivity","contains a period, now is in " + parts.length + " pieces");
                if (parts.length > 1) {
                    //Log.d("AddTransactionActivity","has multiple parts");
                    if (parts[1].length() > 2) {
                        // just truncate
                        parts[1] = parts[1].substring(0, 2);
                        //Log.d("AddTransactionActivity","parts[1] now contains: " + parts[1]);
                    }
                    amt = parts[0] + "." + parts[1];
                } else if(parts.length > 0) {
                    if (parts[0].length() == 0) {
                        parts[0] = "0";
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
    public void categorySelected(int index) {
        transaction.setCategory(categories[index]);
        categoryButton.setText(categories[index]);
    }

    @Override
    public void paymentTypeSelected(int index) {
        transaction.setPaymentType(paymentTypes[index]);
        paymentTypeButton.setText(paymentTypes[index]);
    }

}



