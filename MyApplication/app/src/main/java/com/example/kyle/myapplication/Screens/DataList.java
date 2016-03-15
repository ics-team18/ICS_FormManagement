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
import com.example.kyle.myapplication.Database.Database_Manager;
import com.example.kyle.myapplication.Database.Incident.Tbl_Incident;
import com.example.kyle.myapplication.Database.Incident.Tbl_Incident_Manager;
import com.example.kyle.myapplication.Database.Personnel.Tbl_Personnel;
import com.example.kyle.myapplication.Database.Personnel.Tbl_Personnel_Manager;
import com.example.kyle.myapplication.Database.Role.Tbl_Role;
import com.example.kyle.myapplication.Database.Role.Tbl_Role_Manager;
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
    private Database_Manager db;
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
        db = new Database_Manager(this);
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
                valueList = new ArrayList<Abstract_Table>(Tbl_Personnel_Manager.current.Select(db.getReadableDatabase()));
                break;
            case ROLE:
                valueList = new ArrayList<Abstract_Table>(Tbl_Role_Manager.current.Select(db.getReadableDatabase()));
                break;
            case INCIDENT:
                Tbl_Incident searchCriteria = new Tbl_Incident();
                if (inEditMode())
                {
                    searchCriteria.endTime = "= ''";
                }
                valueList = new ArrayList<Abstract_Table>(Tbl_Incident_Manager.current.Select(db.getReadableDatabase(), searchCriteria));
                break;
            case SUBMITTEDFORMS:
                //valueList = new ArrayList<Abstract_Table>(Tbl_SubmittedForms_Manager.current.Select(db.getReadableDatabase()));
                break;
        }
        if (inEditMode())
        {
            lblNoData.setText("No data to edit");
            gridView.setOnItemClickListener(new OnItemClickListener()
            {
                public void onItemClick(AdapterView parent, View v, int position, long id)
                {
                    selectedRecord = valueList.get(position);
                    AlertDialog.Builder builder = new AlertDialog.Builder(DataList.this);
                    builder.setMessage(selectedRecord.getDataGridPopupMessageValue());
                    builder.setPositiveButton("Edit", dialogClickListener);
                    builder.setNegativeButton("Cancel", null);
                    builder.show();
                }
            });

        }
        else
        {
            lblNoData.setText("No data to delete");
            gridView.setOnItemClickListener(new OnItemClickListener()
            {
                public void onItemClick(AdapterView parent, View v, int position, long id)
                {
                    selectedRecord = valueList.get(position);
                    AlertDialog.Builder builder = new AlertDialog.Builder(DataList.this);
                    builder.setMessage(selectedRecord.getDataGridPopupMessageValue());
                    builder.setPositiveButton("Delete", dialogClickListener);
                    builder.setNegativeButton("Cancel", null);
                    builder.show();
                }
            });
        }
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
                            case SUBMITTEDFORMS:
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
                                switch (SpecificTable)
                                {
                                    case PERSONNEL:
                                        Tbl_Personnel_Manager.current.Delete(db.getWritableDatabase(), (Tbl_Personnel) selectedRecord);
                                        break;
                                    case ROLE:
                                        Tbl_Role_Manager.current.Delete(db.getWritableDatabase(), (Tbl_Role) selectedRecord);
                                        break;
                                    case INCIDENT:
                                        Tbl_Incident_Manager.current.Delete(db.getWritableDatabase(), (Tbl_Incident) selectedRecord);
                                        break;
                                    case SUBMITTEDFORMS:
                                        //Tbl_SubmittedForms_Manager.current.Delete(db.getWritableDatabase(), (Tbl_SubmittedForms) selectedRecord);
                                        break;
                                }
                                valueList.remove(selectedRecord);
                                gridView.invalidateViews();
                                Toast.makeText(DataList.this, "Record successfully deleted", Toast.LENGTH_LONG).show();
                                showHideNoDataLabel();
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