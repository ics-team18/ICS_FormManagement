package com.example.kyle.myapplication.Screens;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.example.kyle.myapplication.Database.Abstract.Abstract_Table_Manager;
import com.example.kyle.myapplication.Helpers.LoggedInUser;
import com.example.kyle.myapplication.Helpers.OpenScreens;
import com.example.kyle.myapplication.R;

public class MainScreen extends AppCompatActivity
{

    Button btnEditIncident, btnCreateIncident, btnManageData;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);

        btnEditIncident = (Button) findViewById(R.id.btnEditIncident);
        btnCreateIncident = (Button) findViewById(R.id.btnCreateIncident);
        btnManageData = (Button) findViewById(R.id.btnManageData);
        btnManageData.setVisibility(LoggedInUser.User.isSupervisor ? View.VISIBLE : View.GONE);
        btnEditIncident.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                OpenScreens.OpenDataListScreen(DataList.Mode.Edit, Abstract_Table_Manager.Table.INCIDENT);
            }
        });

        btnCreateIncident.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                OpenScreens.OpenCreateIncidentScreen(null);
            }
        });

        btnManageData.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                OpenScreens.OpenManageDataScreen();
            }
        });
    }
}
