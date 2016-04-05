package com.example.kyle.myapplication.Screens.CreateEdit;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.kyle.myapplication.Database.Abstract.Abstract_Table;
import com.example.kyle.myapplication.Database.Abstract.Abstract_Table_Manager;
import com.example.kyle.myapplication.Database.DBOperation;
import com.example.kyle.myapplication.Database.Role.Tbl_Role;
import com.example.kyle.myapplication.Database.Role.Tbl_Role_Manager;
import com.example.kyle.myapplication.Database.Templates.Tbl_Templates;
import com.example.kyle.myapplication.Database.Templates.Tbl_Templates_Manager;
import com.example.kyle.myapplication.Helpers.OpenScreens;
import com.example.kyle.myapplication.R;
import com.example.kyle.myapplication.Screens.DataList;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class CreateEditTemplates extends AppCompatActivity
{
    private EditText txtDescription, txtFileName;
    private Spinner cboRole;
    public static Tbl_Templates toUpdate = null;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_createedit_templates);
        txtDescription = (EditText) findViewById(R.id.txtDescription);
        txtFileName = (EditText) findViewById(R.id.txtFileName);
        cboRole = (Spinner) findViewById(R.id.cboRole);
        setSpinnerData();
        Button btnCreate = (Button) findViewById(R.id.btnCreate);
        btnCreate.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                create();
            }
        });

        Button btnClear = (Button) findViewById(R.id.btnClear);
        btnClear.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                clear();
            }
        });

        if (toUpdate != null)
        {
            btnCreate.setText("Update Template");
            btnClear.setText("Revert Changes");
        }

        clear();
    }

    private void setSpinnerData()
    {
        List<Tbl_Role> roleList = Tbl_Role_Manager.current.Select();
        Collections.sort(roleList, new Comparator<Tbl_Role>()
        {
            @Override
            public int compare(Tbl_Role a, Tbl_Role b)
            {
                return a.toString().compareToIgnoreCase(b.toString());
            }
        });
        roleList.add(0, new Tbl_Role());
        cboRole.setAdapter(new ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, roleList));
    }

    private void create()
    {
        Tbl_Role role = (Tbl_Role) cboRole.getSelectedItem();
        String description = txtDescription.getText().toString();
        String fileName = role.title + " - " + txtFileName.getText().toString();
        Tbl_Templates record = new Tbl_Templates(role, description, fileName);
        String anyErrors = record.isValidRecord();
        if (anyErrors.isEmpty())
        {
            if (toUpdate != null)
            {
                record.templatesID = toUpdate.templatesID;
                record.sqlMode = Abstract_Table.SQLMode.UPDATE;
                toUpdate = record;
                DBOperation resultOperation = Tbl_Templates_Manager.current.RecordOperation(this, toUpdate, "Template Updated", "Unable to Update Template");
                if (resultOperation != null && resultOperation.Success())
                {
                    OpenScreens.OpenDataListScreen(DataList.Mode.Edit, Abstract_Table_Manager.Table.TEMPLATES);
                    finish();
                }
            }
            else
            {
                record.sqlMode = Abstract_Table.SQLMode.INSERT;
                DBOperation resultOperation = Tbl_Templates_Manager.current.RecordOperation(this, record, "Template Added", "Unable to Add Template");
                if (resultOperation != null && resultOperation.Success())
                {
                    clear();
                }
            }
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
            txtDescription.setText(toUpdate.description);
            txtFileName.setText(toUpdate.fileName);
            for (int i = 0; i < cboRole.getCount(); i++)
            {
                String value = cboRole.getItemAtPosition(i).toString();
                if (value.equals(toUpdate.role.toString()))
                {
                    cboRole.setSelection(i);
                    break;
                }
            }
            cboRole.setEnabled(false);
        }
        else
        {
            cboRole.setEnabled(true);
            cboRole.setSelection(0);
            txtDescription.setText("");
            txtFileName.setText("");
        }
    }
}