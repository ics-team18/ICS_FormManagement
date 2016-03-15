package com.example.kyle.myapplication.Screens.CreateEdit;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.kyle.myapplication.Database.Abstract.Abstract_Table_Manager;
import com.example.kyle.myapplication.Helpers.OpenScreens;
import com.example.kyle.myapplication.R;
import com.example.kyle.myapplication.Screens.DataList;

public class Manage_Data extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_data);
        Button btnPersonnel = (Button) findViewById(R.id.btnCreate);
        Button btnRoles = (Button) findViewById(R.id.btnRoles);
        Button btnIncidents = (Button) findViewById(R.id.btnIncidents);
        Button btnSubmittedForms = (Button) findViewById(R.id.btnSubmittedForms);

        btnPersonnel.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                SelectionMenu(Abstract_Table_Manager.Table.PERSONNEL);
            }
        });

        btnRoles.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                SelectionMenu(Abstract_Table_Manager.Table.ROLE);
            }
        });

        btnIncidents.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                SelectionMenu(Abstract_Table_Manager.Table.INCIDENT);
            }
        });

        btnSubmittedForms.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                SelectionMenu(Abstract_Table_Manager.Table.SUBMITTEDFORMS);
            }
        });
    }

    private void SelectionMenu(final Abstract_Table_Manager.Table table)
    {
        final Dialog selectionDialog = new Dialog(Manage_Data.this);
        selectionDialog.setContentView(R.layout.activity_manage_data_selection_menu);
        selectionDialog.show();

        TextView txtTable = (TextView) selectionDialog.findViewById(R.id.txtTable);
        txtTable.setText(table.toString());
        Button btnCreate = (Button) selectionDialog.findViewById(R.id.btnCreate);
        Button btnEdit = (Button) selectionDialog.findViewById(R.id.btnEdit);
        Button btnDelete = (Button) selectionDialog.findViewById(R.id.btnDelete);

        if (table == Abstract_Table_Manager.Table.INCIDENT)
        {
            btnCreate.setVisibility(View.GONE);
            btnEdit.setVisibility(View.GONE);
        }
        btnCreate.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                switch (table)
                {
                    case PERSONNEL:
                        OpenScreens.OpenCreatePersonnelScreen(null,false);
                        break;
                    case ROLE:
                        OpenScreens.OpenCreateRoleScreen(null);
                        break;
                    case SUBMITTEDFORMS:
                        break;
                }
                selectionDialog.cancel();
            }
        });

        btnEdit.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                openDataListScreen(DataList.Mode.Edit, table, selectionDialog);
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                openDataListScreen(DataList.Mode.View, table, selectionDialog);
            }
        });
    }

    private void openDataListScreen(DataList.Mode mode, Abstract_Table_Manager.Table table, Dialog selectionDialog)
    {
        OpenScreens.OpenDataListScreen(mode, table);
        selectionDialog.cancel();
    }
}
