package com.example.kyle.myapplication.CustomControls;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

/**
 * Created by Kyle on 2/2/2016.
 */
public class DeleteCancelMessageBox extends Activity
{
    private View view;

    public DeleteCancelMessageBox(View view)
    {
        this.view = view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(view);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Erase hard drive");
        builder.setMessage("Are you sure?");
        builder.setIcon(android.R.drawable.ic_dialog_alert);
        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int which)
            {
                //deleted
            }
        });
        builder.setNegativeButton("Cancel", null);
        builder.show();
    }
}