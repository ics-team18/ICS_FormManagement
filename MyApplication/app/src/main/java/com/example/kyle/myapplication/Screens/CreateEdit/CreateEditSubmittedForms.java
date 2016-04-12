package com.example.kyle.myapplication.Screens.CreateEdit;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kyle.myapplication.CustomControls.DataListGridAdapter;
import com.example.kyle.myapplication.Database.Abstract.Abstract_Table;
import com.example.kyle.myapplication.Database.IncidentLink.Tbl_IncidentLink;
import com.example.kyle.myapplication.Database.IncidentLink.Tbl_IncidentLink_Manager;
import com.example.kyle.myapplication.Database.SubmittedForms.Tbl_SubmittedForms;
import com.example.kyle.myapplication.Database.Templates.Tbl_Templates;
import com.example.kyle.myapplication.Database.Templates.Tbl_Templates_Manager;
import com.example.kyle.myapplication.Helpers.LoggedInUser;
import com.example.kyle.myapplication.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by Kyle on 4/5/2016.
 */
public class CreateEditSubmittedForms extends AppCompatActivity
{
    private GridView gridView;
    private Button btnAdd, btnClear;
    private TextView lblSubmitForms;
    private EditText txtDescription, txtFileName;
    private Spinner cboTemplateDescription;
    private List<Abstract_Table> submittedForms;
    private Tbl_SubmittedForms toUpdate = null;
    public static long IncidentID;
    private int selectedGridPosition;
    private int selectedListPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_createedit_submittedforms);
        gridView = (GridView) findViewById(R.id.gridView);
        btnAdd = (Button) findViewById(R.id.btnAdd);
        btnClear = (Button) findViewById(R.id.btnClear);
        lblSubmitForms = (TextView) findViewById(R.id.lblSubmitForms);
        txtDescription = (EditText) findViewById(R.id.txtDescription);
        txtFileName = (EditText) findViewById(R.id.txtFileName);
        cboTemplateDescription = (Spinner) findViewById(R.id.cboTemplateDescription);
        setSpinnerData();
        submittedForms = new ArrayList<Abstract_Table>(CreateEditIncident.submittedForms);

        btnAdd.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                selectFile();
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (resultCode == RESULT_OK)
        {
            String path = data.getData().getPath();
            String filename = path.substring(path.lastIndexOf("/") + 1).replace("primary:", "");
            String saveFileLocation = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + filename;//uri.getLastPathSegment();
            add(saveFileLocation);
        }
    }

    private void setSpinnerData()
    {
        Tbl_IncidentLink searchCriteria = new Tbl_IncidentLink();
        searchCriteria.personnelID = LoggedInUser.User.personnelID;
        searchCriteria.incidentID = IncidentID;
        Tbl_IncidentLink incidentLink = Tbl_IncidentLink_Manager.current.Select(searchCriteria).get(0);
        List<Tbl_Templates> allTemplates = Tbl_Templates_Manager.current.Select();
        List<Tbl_Templates> templateList = new ArrayList<Tbl_Templates>();
        for (Tbl_Templates template : allTemplates)
        {
            if (template.roleID == incidentLink.roleID)
            {
                templateList.add(template);
            }
        }
        Collections.sort(templateList, new Comparator<Tbl_Templates>()
        {
            @Override
            public int compare(Tbl_Templates a, Tbl_Templates b)
            {
                return a.toString().compareToIgnoreCase(b.toString());
            }
        });
        templateList.add(0, new Tbl_Templates());
        cboTemplateDescription.setAdapter(new ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, templateList));
    }

    private void selectFile()
    {
        Intent chooseFile;
        Intent intent;
        chooseFile = new Intent(Intent.ACTION_GET_CONTENT);
        chooseFile.setType("*/*");
        intent = Intent.createChooser(chooseFile, "Choose a file");
        startActivityForResult(intent, 1);
    }

    private void add(String saveFileLocation)
    {
        Tbl_Templates template = (Tbl_Templates) cboTemplateDescription.getSelectedItem();
        String description = txtDescription.getText().toString();
        String fileName = txtFileName.getText().toString();
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");
        String timeSubmitted = sdf.format(calendar.getTime());

        //note we are passing an incident id of 1 but this will get overwritten with whatever the current id is when we create the incident record
        Tbl_SubmittedForms toAdd = new Tbl_SubmittedForms(1, LoggedInUser.User, template, saveFileLocation, fileName, description, timeSubmitted);
        String anyErrors = toAdd.isValidRecord();

        if (anyErrors.isEmpty())
        {
            if (toUpdate != null)
            {
                toAdd.incidentID = toUpdate.incidentID;
                toAdd.submittedFormID = toUpdate.submittedFormID;
                toAdd.sqlMode = Abstract_Table.SQLMode.UPDATE;
                CreateEditIncident.submittedForms.set(selectedListPosition, toAdd);
                submittedForms.set(selectedGridPosition, toAdd);
                toUpdate = null;
                btnAdd.setText("Add");
                btnClear.setText("Clear");
            }
            else
            {
                toAdd.sqlMode = Abstract_Table.SQLMode.INSERT;
                CreateEditIncident.submittedForms.add(toAdd);
                submittedForms.add(toAdd);
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
            for (int i = 0; i < cboTemplateDescription.getCount(); i++)
            {
                String value = cboTemplateDescription.getItemAtPosition(i).toString();
                if (value.equals(toUpdate.template.toString()))
                {
                    cboTemplateDescription.setSelection(i);
                    break;
                }
            }
            cboTemplateDescription.setEnabled(false);
            txtDescription.setText(toUpdate.description);
            txtFileName.setText(toUpdate.fileName);
        }
        else
        {
            cboTemplateDescription.setEnabled(true);
            cboTemplateDescription.setSelection(0);
            txtDescription.setText("");
            txtFileName.setText("");
        }
    }

    private void showGrid()
    {
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            public void onItemClick(AdapterView parent, View v, int position, long id)
            {
                if (toUpdate == null)
                {//don't allow the user to remove/edit a different row until they have finished updating
                    selectedGridPosition = position;
                    AlertDialog.Builder builder = new AlertDialog.Builder(CreateEditSubmittedForms.this);
                    builder.setMessage("Choose an option below.");
                    builder.setPositiveButton("Remove", new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialog, int which)
                        {
                            Tbl_SubmittedForms record = (Tbl_SubmittedForms) submittedForms.get(selectedGridPosition);
                            record = CreateEditIncident.submittedForms.get(CreateEditIncident.submittedForms.indexOf(record));
                            if (record.submittedFormID == -1)
                            {
                                CreateEditIncident.submittedForms.remove(record);
                            }
                            else
                            {
                                record.sqlMode = Abstract_Table.SQLMode.DELETE;
                            }
                            submittedForms.remove(selectedGridPosition);
                            gridView.invalidateViews();
                            showHideNoDataLabel();
                        }
                    });
                    builder.setNegativeButton("Edit", new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialog, int which)
                        {
                            Tbl_SubmittedForms record = (Tbl_SubmittedForms) submittedForms.get(selectedGridPosition);
                            selectedListPosition = CreateEditIncident.submittedForms.indexOf(record);
                            toUpdate = CreateEditIncident.submittedForms.get(selectedListPosition);
                            btnAdd.setText("Update");
                            btnClear.setText("Revert Changes");
                            clear();
                        }
                    });
                    builder.show();
                }
            }
        });

        gridView.setAdapter(new DataListGridAdapter(CreateEditSubmittedForms.this, submittedForms, 15));

        showHideNoDataLabel();
    }

    private void showHideNoDataLabel()
    {
        if (submittedForms.size() == 0)
        {
            lblSubmitForms.setVisibility(View.VISIBLE);
            gridView.setVisibility(View.INVISIBLE);
        }
        else
        {
            lblSubmitForms.setVisibility(View.INVISIBLE);
            gridView.setVisibility(View.VISIBLE);
        }
    }
}