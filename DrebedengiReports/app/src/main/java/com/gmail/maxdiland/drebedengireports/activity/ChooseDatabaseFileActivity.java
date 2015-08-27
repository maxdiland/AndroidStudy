package com.gmail.maxdiland.drebedengireports.activity;

import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.gmail.maxdiland.drebedengireports.R;

import java.io.File;
import java.io.FileFilter;
import java.util.regex.Pattern;

public class ChooseDatabaseFileActivity extends AppCompatActivity
        implements DialogInterface.OnClickListener {

    private static final String DIALOG_CHOOSE_DB = "CHOOSE_DB_DIALOG";
    private static final String DIALOG_ENTER_PATH = "DIALOG_ENTER_PATH";

    private ChooseDbDialog chooseDbDialog = new ChooseDbDialog();
    private EnterPathToDbDialog enterPathToDbDialog = new EnterPathToDbDialog();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_database_file);
    }

    public void handleButtonClick(View view) {
        FragmentManager fragmentManager = getFragmentManager();
        if (R.id.btnFoundDbs == view.getId()) {
            chooseDbDialog.setFiles( foundDbFiles() );
            chooseDbDialog.show(fragmentManager, DIALOG_CHOOSE_DB);
        } else if (R.id.btnSpecifyDbPath == view.getId()) {
            enterPathToDbDialog.show(fragmentManager, DIALOG_ENTER_PATH);
        }
    }

    private File[] foundDbFiles() {
        File folder = new File(getResources().getString(R.string.drebedengiFolderPath));
        if ( !folder.exists() ) {
            return new File[0];
        }

        return folder.listFiles(new FileFilter() {
            @Override
            public boolean accept(File file) {
                return file.isFile() && Pattern.matches(
                        getResources().getString(R.string.dbFileNamePattern), file.getName()
                );
            }
        });
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        if (chooseDbDialog.getDialog() == dialog) {
            validateFileAndRunNextActivity(
                chooseDbDialog.getFiles()[which]
            );
        }

        if (enterPathToDbDialog.getDialog() == dialog) {
            validateFileAndRunNextActivity(new File(enterPathToDbDialog.getEnteredPath()));
        }
    }

    public void validateFileAndRunNextActivity(File dbFile) {
        if (dbFile.isDirectory()) {
            Toast.makeText(this, R.string.choosedbTxtIsNotAFile, Toast.LENGTH_LONG).show();
        } else if (!dbFile.exists()) {
            Toast.makeText(this, R.string.choosedbTxtFileNotFound, Toast.LENGTH_LONG).show();
        } else {
            Intent intent = new Intent(this, SearchFormActivity.class);
            intent.putExtra(SearchFormActivity.DATABASE_PATH_KEY, dbFile.getAbsolutePath());
            startActivity(intent);
        }
    }
}
