package com.example.kyle.myapplication.Screens;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kyle.myapplication.CustomControls.DataListGridAdapter;
import com.example.kyle.myapplication.Database.Abstract.Abstract_Table;
import com.example.kyle.myapplication.Database.Abstract.Abstract_Table_Manager;
import com.example.kyle.myapplication.Database.DBOperation;
import com.example.kyle.myapplication.Database.Incident.Tbl_Incident;
import com.example.kyle.myapplication.Database.Incident.Tbl_Incident_Manager;
import com.example.kyle.myapplication.Database.Personnel.Tbl_Personnel;
import com.example.kyle.myapplication.Database.Personnel.Tbl_Personnel_Manager;
import com.example.kyle.myapplication.Database.Role.Tbl_Role;
import com.example.kyle.myapplication.Database.Role.Tbl_Role_Manager;
import com.example.kyle.myapplication.Database.Templates.Tbl_Templates;
import com.example.kyle.myapplication.Database.Templates.Tbl_Templates_Manager;
import com.example.kyle.myapplication.Helpers.OpenScreens;
import com.example.kyle.myapplication.R;

import java.util.ArrayList;
import java.util.List;

public class DataList extends AppCompatActivity
{
    public enum Mode
    {
        View,
        Edit,
    }

    private GridView gridView;
    private TextView lblNoData;
    private List<Abstract_Table> valueList;
    public static Abstract_Table selectedRecord;
    public static Mode mode = Mode.View;
    public static Abstract_Table_Manager.Table SpecificTable = Abstract_Table_Manager.Table.NONE;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_list);
        gridView = (GridView) findViewById(R.id.gridView);
        lblNoData = (TextView) findViewById(R.id.lblNoData);
        showGrid();
    }

    private boolean inEditMode()
    {
        return mode == Mode.Edit;
    }

    private void showGrid()
    {
        switch (SpecificTable)
        {
            case NONE:
                super.onBackPressed();
                Toast.makeText(DataList.this, "Programmer Error: You need to set the Specific table variable", Toast.LENGTH_LONG).show();
                break;
            case PERSONNEL:
                valueList = new ArrayList<Abstract_Table>(Tbl_Personnel_Manager.current.Select());
                break;
            case ROLE:
                valueList = new ArrayList<Abstract_Table>(Tbl_Role_Manager.current.Select());
                break;
            case INCIDENT:
                Tbl_Incident searchCriteria = new Tbl_Incident();
                if (inEditMode())
                {
                    searchCriteria.endTime = "''";
                }
                valueList = new ArrayList<Abstract_Table>(Tbl_Incident_Manager.current.Select(searchCriteria));
                break;
            case TEMPLATES:
                valueList = new ArrayList<Abstract_Table>(Tbl_Templates_Manager.current.Select());
                break;
        }

        String noDataMessage = inEditMode() ? "No data to edit" : "No data to delete";
        lblNoData.setText(noDataMessage);
        gridView.setOnItemClickListener(new OnItemClickListener()
        {
            public void onItemClick(AdapterView parent, View v, int position, long id)
            {
                selectedRecord = valueList.get(position);
                AlertDialog.Builder builder = new AlertDialog.Builder(DataList.this);
                builder.setMessage(selectedRecord.getDataGridPopupMessageValue());
                String editDelete = inEditMode() ? "Edit" : "Delete";
                builder.setPositiveButton(editDelete, dialogClickListener);
                builder.setNegativeButton("Cancel", null);
                builder.show();
            }
        });

        gridView.setAdapter(new DataListGridAdapter(DataList.this, valueList));
        showHideNoDataLabel();
    }

    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener()
    {
        @Override
        public void onClick(DialogInterface dialog, int which)
        {
            switch (which)
            {
                case DialogInterface.BUTTON_POSITIVE:
                    if (inEditMode())
                    {
                        switch (SpecificTable)
                        {
                            case PERSONNEL:
                                OpenScreens.OpenCreatePersonnelScreen((Tbl_Personnel) DataList.selectedRecord, false);
                                break;
                            case ROLE:
                                OpenScreens.OpenCreateRoleScreen((Tbl_Role) DataList.selectedRecord);
                                break;
                            case INCIDENT:
                                OpenScreens.OpenCreateIncidentScreen((Tbl_Incident) DataList.selectedRecord);
                                break;
                            case TEMPLATES:
                                OpenScreens.OpenUploadTemplateScreen((Tbl_Templates) DataList.selectedRecord);
                                break;
                        }
                        finish();
                    }
                    else
                    {
                        AlertDialog.Builder builder = new AlertDialog.Builder(DataList.this);
                        builder.setMessage("Are you sure you want to delete this record? (This cannot be undone)");
                        builder.setNegativeButton("Cancel", null);
                        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int which)
                            {
                                selectedRecord.sqlMode = Abstract_Table.SQLMode.DELETE;
                                String successMessage = "Record successfully deleted";
                                String failMessage = "Unable to Delete Record";
                                DBOperation resultOperation = null;
                                switch (SpecificTable)
                                {
                                    case PERSONNEL:
                                        resultOperation = Tbl_Personnel_Manager.current.RecordOperation(DataList.this, (Tbl_Personnel) selectedRecord, successMessage, failMessage);
                                        break;
                                    case ROLE:
                                        resultOperation = Tbl_Role_Manager.current.RecordOperation(DataList.this, (Tbl_Role) selectedRecord, successMessage, failMessage);
                                        break;
                                    case INCIDENT:
                                        resultOperation = Tbl_Incident_Manager.current.RecordOperation(DataList.this, (Tbl_Incident) selectedRecord, successMessage, failMessage);
                                        break;
                                    case TEMPLATES:
                                        resultOperation = Tbl_Templates_Manager.current.RecordOperation(DataList.this, (Tbl_Templates) selectedRecord, successMessage, failMessage);
                                        break;
                                }
                                if (resultOperation != null && resultOperation.Success())
                                {
                                    valueList.remove(selectedRecord);
                                    gridView.invalidateViews();
                                    showHideNoDataLabel();
                                }
                            }
                        });
                        builder.show();
                    }
            }
        }
    };

    private void showHideNoDataLabel()
    {
        if (valueList.size() == 0)
        {
            lblNoData.setVisibility(View.VISIBLE);
        }
        else
        {
            lblNoData.setVisibility(View.INVISIBLE);
        }
    }
}