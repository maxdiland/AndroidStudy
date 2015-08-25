package com.gmail.maxdiland.drebedengireports.activity;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Editable;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.gmail.maxdiland.drebedengireports.R;

import java.io.File;

public class ChooseDatabaseFileActivity extends ActionBarActivity {

    private EditText etPathToDb;
    private Button btnConfirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_database_file);
        initUi();
    }

    private void initUi() {
        etPathToDb = (EditText) findViewById(R.id.etPathToDatabase);
        btnConfirm = (Button) findViewById(R.id.btnConfirm);
    }

    public void confirmPathToDb(View view) {
        String dbFilePath = etPathToDb.getText().toString();
        File dbFile = new File(dbFilePath);
        if (dbFile.isDirectory()) {
            Toast.makeText(this, R.string.choosedbTxtIsNotAFile, Toast.LENGTH_LONG).show();
        } else if (!dbFile.exists()) {
            Toast.makeText(this, R.string.choosedbTxtFileNotFound, Toast.LENGTH_LONG).show();
        } else {
            Intent intent = new Intent(this, SearchFormActivity.class);
            intent.putExtra(SearchFormActivity.DATABASE_PATH_KEY, dbFilePath);
            startActivity(intent);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
