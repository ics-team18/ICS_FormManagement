package com.example.kyle.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainScreen extends AppCompatActivity {

         Button btnSelectIncident, btnCreateIncident, btnManageData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);

        btnSelectIncident = (Button) findViewById(R.id.btnSelectIncident);
        btnCreateIncident = (Button) findViewById(R.id.btnCreateIncident);
        btnManageData = (Button) findViewById(R.id.btnManageData);
        btnManageData.setVisibility(LoggedInUser.User.isSupervisor? View.VISIBLE : View.GONE);
        btnSelectIncident.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                selectIncident();
            }
        });

        btnCreateIncident.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                createIncident();
            }
        });

        btnManageData.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                manageData();
            }
        });
    }

    private void selectIncident()
    {
        //startActivity(new Intent(this, SelectIncident.class));
    }

    private void createIncident()
    {
        //startActivity(new Intent(this, CreateIncident.class));
    }

    private void manageData()
    {
        startActivity(new Intent(this, ManageData.class));
    }
}
