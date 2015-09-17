package com.gmail.maxdiland.drebedengireports.activity;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.gmail.maxdiland.drebedengireports.R;

public class RequestChoiceActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_choice);
    }

    public void chooseAction(View view) {
        Intent intent = null;
        switch (view.getId()) {
            case R.id.btnIncSearch:
//                intent = new Intent(this, null);
                break;
            case R.id.btnIntCatReport:
//                intent = new Intent(this, null);
                break;
            case R.id.btnIntReport:
//                intent = new Intent(this, null);
                break;
            case R.id.btnExpSearch:
                intent = new Intent(this, SearchFormActivity.class);
                break;
            case R.id.btnExpCatReport:
//                intent = new Intent(this, null);
                break;
            case R.id.btnExpReport:
//                intent = new Intent(this, null);
                break;
        }

        if (intent == null) {
            Toast.makeText(this, "Not implemented yet", Toast.LENGTH_SHORT).show();
        } else {
            intent.putExtra(
                    ChooseDbActivity.EXTRA_DB_KEY,
                    getIntent().getStringExtra(ChooseDbActivity.EXTRA_DB_KEY)
            );
            startActivity(intent);
        }
    }
}
