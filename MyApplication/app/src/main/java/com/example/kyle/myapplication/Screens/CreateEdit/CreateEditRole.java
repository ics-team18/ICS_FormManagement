package com.example.kyle.myapplication.Screens.CreateEdit;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.kyle.myapplication.Database.Abstract.Abstract_Table_Manager;
import com.example.kyle.myapplication.Database.Database_Manager;
import com.example.kyle.myapplication.Database.Role.Tbl_Role;
import com.example.kyle.myapplication.Database.Role.Tbl_Role_Manager;
import com.example.kyle.myapplication.Helpers.OpenScreens;
import com.example.kyle.myapplication.R;
import com.example.kyle.myapplication.Screens.DataList;

public class CreateEditRole extends AppCompatActivity
{
    private Database_Manager db;
    private EditText txtRoleTitle;
    public static Tbl_Role toUpdate = null;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_createedit_role);
        db = new Database_Manager(this);
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
                toUpdate = record;
                Tbl_Role_Manager.current.Update(db.getWritableDatabase(), toUpdate);
                Toast.makeText(this, "Role Updated", Toast.LENGTH_LONG).show();
                OpenScreens.OpenDataListScreen(DataList.Mode.Edit, Abstract_Table_Manager.Table.ROLE);
                finish();
            }
            else
            {
                Tbl_Role_Manager.current.Insert(db.getWritableDatabase(), record);
                Toast.makeText(this, "Role Added", Toast.LENGTH_LONG).show();
                clear();
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
