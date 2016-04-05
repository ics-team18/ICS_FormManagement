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
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);

        Button btnEditIncident = (Button) findViewById(R.id.btnEditIncident);
        btnEditIncident.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                OpenScreens.OpenDataListScreen(DataList.Mode.Edit, Abstract_Table_Manager.Table.INCIDENT);
            }
        });

        Button btnCreateIncident = (Button) findViewById(R.id.btnCreateIncident);
        btnCreateIncident.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                OpenScreens.OpenCreateIncidentScreen(null);
            }
        });

        Button btnManageData = (Button) findViewById(R.id.btnManageData);
        btnManageData.setVisibility(LoggedInUser.User.isSupervisor ? View.VISIBLE : View.GONE);
        btnManageData.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                OpenScreens.OpenManageDataScreen();
            }
        });

        Button btnDownloadTemplate = (Button) findViewById(R.id.btnDownloadTemplate);
        btnDownloadTemplate.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                OpenScreens.OpenDownloadTemplateScreen();
            }
        });
    }
}
