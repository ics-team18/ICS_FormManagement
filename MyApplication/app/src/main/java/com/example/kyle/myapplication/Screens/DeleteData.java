package com.example.kyle.myapplication.Screens;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.Toast;

import com.example.kyle.myapplication.Database.Abstract_Table;
import com.example.kyle.myapplication.Database.Database_Manager;
import com.example.kyle.myapplication.Database.Tbl_Personnel;
import com.example.kyle.myapplication.Database.Tbl_Personnel_Manager;
import com.example.kyle.myapplication.R;

import java.util.ArrayList;
import java.util.List;

public class DeleteData extends AppCompatActivity
{
    private Database_Manager db;
    private GridView gridView;
    private List<Abstract_Table> valueList;
    private Abstract_Table selectedRecord;

    public static enum Table
    {
        NONE,
        PERSONNEL,
        ROLE,
        INCIDENT,
        SUBMITTEDFORMS,
    }

    private static Table SpecificTable = Table.NONE;

    public static void SetSpecificTable(Table value)
    {
        SpecificTable = value;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_data);
        db = new Database_Manager(this);
        gridView = (GridView) findViewById(R.id.gridView);
        showGrid();
    }

    private void showGrid()
    {
        switch (SpecificTable)
        {
            case NONE:
                super.onBackPressed();
                Toast.makeText(DeleteData.this, "Programmer Error: You need to set the Specific table variable", Toast.LENGTH_LONG).show();
                break;
            case PERSONNEL:
                valueList = new ArrayList<Abstract_Table>(Tbl_Personnel_Manager.current.Select(db.getReadableDatabase()));
                break;
            case ROLE:
                //valueList = new ArrayList<Abstract_Table>(Tbl_Role_Manager.current.Select(db.getReadableDatabase()));
                break;
            case INCIDENT:
                //valueList = new ArrayList<Abstract_Table>(Tbl_Incident_Manager.current.Select(db.getReadableDatabase()));
                break;
            case SUBMITTEDFORMS:
                //valueList = new ArrayList<Abstract_Table>(Tbl_SubmittedForms_Manager.current.Select(db.getReadableDatabase()));
                break;
        }

        gridView.setOnItemClickListener(new OnItemClickListener()
        {
            public void onItemClick(AdapterView parent, View v, int position, long id)
            {
                selectedRecord = valueList.get(position);
                AlertDialog.Builder builder = new AlertDialog.Builder(DeleteData.this);
                builder.setMessage(selectedRecord.getDataGridPopupMessageValue());
                builder.setPositiveButton("Delete", dialogClickListener);
                builder.setNegativeButton("Cancel", dialogClickListener);
                builder.show();
            }
        });
        gridView.setAdapter(new CustomGridAdapter(DeleteData.this, valueList));
    }

    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener()
    {
        @Override
        public void onClick(DialogInterface dialog, int which)
        {
            switch (which)
            {
                case DialogInterface.BUTTON_POSITIVE:
                    AlertDialog.Builder builder = new AlertDialog.Builder(DeleteData.this);
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
                                    //Tbl_Role_Manager.current.Delete(db.getWritableDatabase(), (Tbl_Role) selectedRecord);
                                    break;
                                case INCIDENT:
                                    //Tbl_Incident_Manager.current.Delete(db.getWritableDatabase(), (Tbl_Incident) selectedRecord);
                                    break;
                                case SUBMITTEDFORMS:
                                    //Tbl_SubmittedForms_Manager.current.Delete(db.getWritableDatabase(), (Tbl_SubmittedForms) selectedRecord);
                                    break;
                            }
                            valueList.remove(selectedRecord);
                            gridView.invalidateViews();
                            Toast.makeText(DeleteData.this, "Record successfully deleted", Toast.LENGTH_LONG).show();
                        }
                    });
                    builder.show();
                case DialogInterface.BUTTON_NEGATIVE:
                    //Do nothing
                    break;
            }
        }
    };
}

