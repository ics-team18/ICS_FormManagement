package com.example.kyle.myapplication.Screens.CreateEdit;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kyle.myapplication.CustomControls.DataListGridAdapter;
import com.example.kyle.myapplication.Database.Abstract.Abstract_Table;
import com.example.kyle.myapplication.Database.Database_Manager;
import com.example.kyle.myapplication.Database.IncidentLink.Tbl_IncidentLink;
import com.example.kyle.myapplication.Database.Personnel.Tbl_Personnel;
import com.example.kyle.myapplication.Database.Personnel.Tbl_Personnel_Manager;
import com.example.kyle.myapplication.Database.Role.Tbl_Role;
import com.example.kyle.myapplication.Database.Role.Tbl_Role_Manager;
import com.example.kyle.myapplication.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class CreateEditIncidentLinks extends AppCompatActivity
{
    private Database_Manager db;
    private GridView gridView;
    private Button btnAdd, btnClear;
    private TextView lblAddPersonnel;
    private EditText txtRanking;
    private Spinner cboPersonnel, cboRole;
    private List<Abstract_Table> incidentLinks;
    private Tbl_IncidentLink toUpdate = null;
    private int selectedGridPosition;
    private int selectedListPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_createedit_incidentlinks);
        gridView = (GridView) findViewById(R.id.gridView);
        btnAdd = (Button) findViewById(R.id.btnAdd);
        btnClear = (Button) findViewById(R.id.btnClear);
        lblAddPersonnel = (TextView) findViewById(R.id.lblAddPersonnel);
        txtRanking = (EditText) findViewById(R.id.txtRanking);
        cboPersonnel = (Spinner) findViewById(R.id.cboPersonnel);
        cboRole = (Spinner) findViewById(R.id.cboRole);
        db = new Database_Manager(this);
        setSpinnerData();

        incidentLinks = new ArrayList<Abstract_Table>(CreateEditIncident.incidentLinks);

        btnAdd.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                add();
            }
        });

        btnClear.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                clear();
            }
        });
        showGrid();
    }

    private void setSpinnerData()
    {
        List<Tbl_Personnel> personnelList = Tbl_Personnel_Manager.current.Select(db.getReadableDatabase());
        List<Tbl_Role> roleList = Tbl_Role_Manager.current.Select(db.getReadableDatabase());
        Collections.sort(personnelList, new Comparator<Tbl_Personnel>()
        {
            @Override
            public int compare(Tbl_Personnel a, Tbl_Personnel b)
            {
                return a.toString().compareToIgnoreCase(b.toString());
            }
        });
        Collections.sort(roleList, new Comparator<Tbl_Role>()
        {
            @Override
            public int compare(Tbl_Role a, Tbl_Role b)
            {
                return a.toString().compareToIgnoreCase(b.toString());
            }
        });
        personnelList.add(0, new Tbl_Personnel());
        roleList.add(0, new Tbl_Role());
        cboPersonnel.setAdapter(new ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, personnelList));
        cboRole.setAdapter(new ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, roleList));
    }

    private void add()
    {
        String strRanking = txtRanking.getText().toString();
        int ranking = 0;
        if (!strRanking.isEmpty())
        {
            ranking = Integer.parseInt(strRanking);
        }
        Tbl_Personnel personnel = (Tbl_Personnel) cboPersonnel.getSelectedItem();
        Tbl_Role role = (Tbl_Role) cboRole.getSelectedItem();
        //note we are passing an incident id of 1 but this will get overwritten with whatever the current id is when we create the incident record
        Tbl_IncidentLink toAdd = new Tbl_IncidentLink(1, personnel, role, ranking);
        toAdd.sqlMode = Abstract_Table.SQLMode.INSERT;
        String anyErrors = toAdd.customIsValidRecord(selectedListPosition, toAdd, CreateEditIncident.incidentLinks);

        if (anyErrors.isEmpty())
        {
            if (toUpdate != null)
            {
                toAdd.incidentID = toUpdate.incidentID;
                toAdd.incidentLinkID = toUpdate.incidentLinkID;
                toAdd.sqlMode = Abstract_Table.SQLMode.UPDATE;
                CreateEditIncident.incidentLinks.set(selectedListPosition, toAdd);
                incidentLinks.set(selectedGridPosition, toAdd);
                toUpdate = null;
                btnAdd.setText("Add");
                btnClear.setText("Clear");
            }
            else
            {
                CreateEditIncident.incidentLinks.add(toAdd);
                incidentLinks.add(toAdd);
            }
            gridView.invalidateViews();
            showHideNoDataLabel();
            clear();
        }
        else
        {
            Toast.makeText(this, anyErrors, Toast.LENGTH_LONG).show();
        }
    }

    private void clear()
    {
        if (toUpdate != null)
        {
            for (int i = 0; i < cboPersonnel.getCount(); i++)
            {
                String value = cboPersonnel.getItemAtPosition(i).toString();
                if (value.equals(toUpdate.personnel.toString()))
                {
                    cboPersonnel.setSelection(i);
                    break;
                }
            }
            cboPersonnel.setEnabled(false);
            for (int i = 0; i < cboRole.getCount(); i++)
            {
                String value = cboRole.getItemAtPosition(i).toString();
                if (value.equals(toUpdate.role.toString()))
                {
                    cboRole.setSelection(i);
                    break;
                }
            }
            txtRanking.setText(Integer.toString(toUpdate.ranking));
        }
        else
        {
            cboPersonnel.setEnabled(true);
            cboPersonnel.setSelection(0);
            cboRole.setSelection(0);
            txtRanking.setText("");
        }
    }

    private void showGrid()
    {
        gridView.setOnItemClickListener(new OnItemClickListener()
        {
            public void onItemClick(AdapterView parent, View v, int position, long id)
            {
                if (toUpdate == null)
                {//don't allow the user to remove/edit a different row until they have finished updating
                    selectedGridPosition = position;
                    AlertDialog.Builder builder = new AlertDialog.Builder(CreateEditIncidentLinks.this);
                    builder.setMessage("Choose an option below.");
                    builder.setPositiveButton("Remove", new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialog, int which)
                        {
                            Tbl_IncidentLink record = (Tbl_IncidentLink) incidentLinks.get(selectedGridPosition);
                            record = CreateEditIncident.incidentLinks.get(CreateEditIncident.incidentLinks.indexOf(record));
                            if (record.incidentLinkID == -1)
                            {
                                CreateEditIncident.incidentLinks.remove(record);
                            }
                            else
                            {
                                record.sqlMode = Abstract_Table.SQLMode.DELETE;
                            }
                            incidentLinks.remove(selectedGridPosition);
                            gridView.invalidateViews();
                            showHideNoDataLabel();
                        }
                    });
                    builder.setNegativeButton("Edit", new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialog, int which)
                        {
                            Tbl_IncidentLink record = (Tbl_IncidentLink) incidentLinks.get(selectedGridPosition);
                            selectedListPosition = CreateEditIncident.incidentLinks.indexOf(record);
                            toUpdate = CreateEditIncident.incidentLinks.get(selectedListPosition);
                            btnAdd.setText("Update");
                            btnClear.setText("Revert Changes");
                            clear();
                        }
                    });
                    builder.show();
                }
            }
        });

        gridView.setAdapter(new DataListGridAdapter(CreateEditIncidentLinks.this, incidentLinks, 15));

        showHideNoDataLabel();
    }

    private void showHideNoDataLabel()
    {
        if (incidentLinks.size() == 0)
        {
            lblAddPersonnel.setVisibility(View.VISIBLE);
            gridView.setVisibility(View.INVISIBLE);
        }
        else
        {
            lblAddPersonnel.setVisibility(View.INVISIBLE);
            gridView.setVisibility(View.VISIBLE);
        }
    }
}