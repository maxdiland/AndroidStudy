package com.gmail.maxdiland.drebedengireports.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.widget.EditText;

import com.gmail.maxdiland.drebedengireports.R;

/**
 * author Max Diland
 */
public class EnterPathToDbDialog extends DialogFragment {
    private EditText etEnteredPath;
    private String lastEnteredValue = "";

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Activity activity = getActivity();
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle(R.string.chooseDbLabel);
        etEnteredPath = new EditText(activity);
        etEnteredPath.setText(lastEnteredValue);
        builder.setView(etEnteredPath);
        builder.setNegativeButton(R.string.cancel, null);
        builder.setPositiveButton(R.string.ok, (android.content.DialogInterface.OnClickListener) activity);
        return builder.create();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        lastEnteredValue = etEnteredPath.getText().toString();
    }

    public String getEnteredPath() {
        return etEnteredPath.getText().toString();
    }
}
