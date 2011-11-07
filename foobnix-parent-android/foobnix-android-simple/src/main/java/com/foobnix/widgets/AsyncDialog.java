package com.foobnix.widgets;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;

public abstract class AsyncDialog extends AsyncTask<Void, Void, Boolean> implements DialogInterface.OnCancelListener,
        DialogInterface.OnDismissListener {

    private final ProgressDialog dialog;
    private final String msg;

    public AsyncDialog(Context context) {
        this(context, "Processign...");
    }
    public AsyncDialog(Context context, String msg) {
        this.msg = msg;
        dialog = new ProgressDialog(context);
        dialog.setOnCancelListener(this);
        dialog.setOnDismissListener(this);
        execute();
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        dialog.dismiss();
        cancel(true);
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        dialog.dismiss();
        cancel(true);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        dialog.setMessage(msg);
        dialog.show();
    }

    @Override
    protected void onPostExecute(Boolean result) {
        super.onPostExecute(result);
        if (dialog != null) {
            dialog.dismiss();
        }
    }

}
