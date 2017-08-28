package com.jaz.budgt;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.app.DatePickerDialog;
import android.widget.DatePicker;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;


/**
 * Created by jaz on 8/23/17.
 */

public class AddTransactionActivity extends AppCompatActivity implements View.OnClickListener {
    Button dateButton, todayButton, doneButton;
    private int mYear, mMonth, mDay;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_transaction);

        dateButton = (Button) findViewById(R.id.date_picker);
        dateButton.setOnClickListener(this);
        dateButton.setText(R.string.date);
        todayButton = (Button) findViewById(R.id.today_button);
        todayButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        final Calendar c = Calendar.getInstance();
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

                            dateButton.setText((monthOfYear+1) + "/" + dayOfMonth + "/" + year);
                        }
                    }, mYear, mMonth, mDay);
            datePickerDialog.show();
        } else if (v == todayButton) {
            mYear = c.get(Calendar.YEAR) - 2000;
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);

            dateButton.setText((mMonth+1) + "/" + mDay + "/" + mYear);

        }
        hideSoftKeyboard(this);
    }

    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(
                activity.getCurrentFocus().getWindowToken(), 0);
    }
}



