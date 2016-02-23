package com.example.kyle.myapplication.Screens;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.example.kyle.myapplication.R;

public class ManageData extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_data);
        Button btnPersonnel = (Button) findViewById(R.id.btnPersonnel);
        Button btnRoles = (Button) findViewById(R.id.btnRoles);
        Button btnIncidents = (Button) findViewById(R.id.btnIncidents);
        Button btnSubmittedForms = (Button) findViewById(R.id.btnSubmittedForms);

        btnPersonnel.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Register.fromHomeScreen(false);
                startActivity(new Intent(getApplicationContext(), Register.class));
            }
        });

        btnRoles.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                startActivity(new Intent(getApplicationContext(), CreateRole.class));
            }
        });

        btnIncidents.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                DeleteData.SetSpecificTable(DeleteData.Table.INCIDENT);
                startActivity(new Intent(getApplicationContext(), DeleteData.class));
            }
        });

        btnSubmittedForms.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                //todo
            }
        });
    }

}
