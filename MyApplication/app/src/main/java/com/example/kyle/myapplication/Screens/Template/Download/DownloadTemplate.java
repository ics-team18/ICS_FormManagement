package com.example.kyle.myapplication.Screens.Template.Download;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.kyle.myapplication.R;

public class DownloadTemplate extends Activity {

    private Spinner role_spinner;
    private Spinner org_spinner;
    private Spinner form_spinner;
    private Button btnDownload;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download_template);

        addListenerOnButtons();
        addListenerOnSpinnerItemSelection();
    }

    public void addListenerOnSpinnerItemSelection() {
        role_spinner = (Spinner) findViewById(R.id.role_spinner);
        org_spinner = (Spinner) findViewById(R.id.org_spinner);
        form_spinner = (Spinner) findViewById(R.id.form_spinner);

        role_spinner.setOnItemSelectedListener(new DownloadTemplateSpinnerListener());
        org_spinner.setOnItemSelectedListener(new DownloadTemplateSpinnerListener());
        form_spinner.setOnItemSelectedListener(new DownloadTemplateSpinnerListener());

    }

    // get the selected dropdown list value
    public void addListenerOnButtons() {

        role_spinner = (Spinner) findViewById(R.id.role_spinner);
        org_spinner = (Spinner) findViewById(R.id.org_spinner);
        form_spinner = (Spinner) findViewById(R.id.form_spinner);

        btnDownload = (Button) findViewById(R.id.btnDownload);
        btnDownload.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Toast.makeText(DownloadTemplate.this,
                        "Template will be downloaded", Toast.LENGTH_SHORT).show();
            }

        });
    }
}
