package com.jaz.budgt.activities;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.jaz.budgt.TransactionListViewModel;
import com.jaz.budgt.fragments.AccountListFragment;
import com.jaz.budgt.fragments.OverviewFragment;
import com.jaz.budgt.R;
import com.jaz.budgt.fragments.TransactionListFragment;

public class MainActivity extends AppCompatActivity {


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        final BottomNavigationView bottomNavigationView = (BottomNavigationView)
                findViewById(R.id.navigation);

        bottomNavigationView.setOnNavigationItemSelectedListener
                (new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        Fragment selectedFragment = null;
                        switch (item.getItemId()) {
                            case R.id.navigation_overview:
                                selectedFragment = OverviewFragment.newInstance();
                                break;
                            case R.id.navigation_settings:
                                selectedFragment = AccountListFragment.newInstance();
                                break;
                            case R.id.navigation_transactions:
                                selectedFragment = TransactionListFragment.newInstance();
                                break;
                        }
                        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.frame_layout, selectedFragment);
                        fragmentTransaction.commit();
                        return true;
                    }
                });

        //Manually displaying the first fragment - one time only
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, OverviewFragment.newInstance());
        fragmentTransaction.commit();
        bottomNavigationView.setSelectedItemId(R.id.navigation_overview);

        // ViewModel Data
        TransactionListViewModel model = ViewModelProviders.of(this).get(TransactionListViewModel.class);
        model.getTimePeriodTransactions().observe(this, transactions -> {
            //update ui
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
//        if (mDrawerToggle.onOptionsItemSelected(item)) {
//            return true;
//        }
        switch (item.getItemId()) {
            case R.id.options_settings:
                // todo figure out how to make this affect the fragment
                Intent addTransactionIntent = new Intent(getApplicationContext(), SettingsActivity.class);
                startActivity(addTransactionIntent);
                return true;

            case R.id.options_refresh:

                return true;

            case R.id.options_sort:

                return true;
            case R.id.options_filter:

                return true;
        }
        return onOptionsItemSelected(item);
    }

}
