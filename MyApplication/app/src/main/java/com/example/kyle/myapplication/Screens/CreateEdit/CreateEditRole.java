package com.example.kyle.myapplication.Screens.CreateEdit;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.kyle.myapplication.Database.Abstract.Abstract_Table;
import com.example.kyle.myapplication.Database.Abstract.Abstract_Table_Manager;
import com.example.kyle.myapplication.Database.DBOperation;
import com.example.kyle.myapplication.Database.Role.Tbl_Role;
import com.example.kyle.myapplication.Database.Role.Tbl_Role_Manager;
import com.example.kyle.myapplication.Helpers.OpenScreens;
import com.example.kyle.myapplication.R;
import com.example.kyle.myapplication.Screens.DataList;

public class CreateEditRole extends AppCompatActivity
{
    private EditText txtRoleTitle;
    public static Tbl_Role toUpdate = null;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_createedit_role);
        txtRoleTitle = (EditText) findViewById(R.id.txtRoleTitle);

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
            btnCreate.setText("Update");
            btnClear.setText("Revert Changes");
        }

        clear();
    }

    private void create()
    {
        String roleTitle = txtRoleTitle.getText().toString();
        Tbl_Role record = new Tbl_Role(roleTitle);
        String anyErrors = record.isValidRecord();
        if (anyErrors.isEmpty())
        {
            if (toUpdate != null)
            {
                record.roleID = toUpdate.roleID;
                record.sqlMode = Abstract_Table.SQLMode.UPDATE;
                toUpdate = record;
                DBOperation resultOperation = Tbl_Role_Manager.current.RecordOperation(this, toUpdate, "Role Updated", "Unable to Update Role");
                if (resultOperation != null && resultOperation.Success())
                {
                    OpenScreens.OpenDataListScreen(DataList.Mode.Edit, Abstract_Table_Manager.Table.ROLE);
                    finish();
                }
            }
            else
            {
                record.sqlMode = Abstract_Table.SQLMode.INSERT;
                DBOperation resultOperation = Tbl_Role_Manager.current.RecordOperation(this, record, "Role Added", "Unable to Add Role");
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
            txtRoleTitle.setText(toUpdate.title);
        }
        else
        {
            txtRoleTitle.setText("");
        }
    }
}
