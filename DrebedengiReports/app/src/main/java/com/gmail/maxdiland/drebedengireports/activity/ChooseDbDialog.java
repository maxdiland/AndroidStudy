package com.gmail.maxdiland.drebedengireports.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

import com.gmail.maxdiland.drebedengireports.R;
import org.apache.commons.lang3.ArrayUtils;

import java.io.File;

/**
 * author Max Diland
 */
public class ChooseDbDialog extends DialogFragment {
    private File[] files;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Activity activity = getActivity();
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle(R.string.chooseDbLabel);
        builder.setCancelable(true);
        if (ArrayUtils.isEmpty(files)) {
            builder.setMessage(R.string.nothingFoundLabel);
        } else {
            builder.setItems(getFileNames(), (DialogInterface.OnClickListener) activity);
        }
        builder.setNegativeButton(R.string.cancel, null);
        return builder.create();
    }

    public void setFiles(File[] files) {
        this.files = files;
    }

    public File[] getFiles() {
        return files;
    }

    private String[] getFileNames() {
        String[] result = new String[files.length];
        for (int i = 0; i < files.length; i++) {
            result[i] = files[i].getName();
        }
        return result;
    }
}
