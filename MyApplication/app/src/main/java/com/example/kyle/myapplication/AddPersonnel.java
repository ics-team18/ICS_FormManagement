package com.example.kyle.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.kyle.myapplication.Database.Database_Manager;
import com.example.kyle.myapplication.Database.Tbl_Personnel;
import com.example.kyle.myapplication.Database.Tbl_Personnel_Manager;

import java.util.List;

public class AddPersonnel extends AppCompatActivity
{
    Database_Manager db;
    EditText txtFirstName;
    EditText txtLastName;
    EditText txtEmail;
    EditText txtPassword;
    EditText txtMobilePhone;
    EditText txtTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_personnel);
        db = new Database_Manager(this);
        txtFirstName = (EditText) findViewById(R.id.txtFirstName);
        txtLastName = (EditText) findViewById(R.id.txtLastName);
        txtEmail = (EditText) findViewById(R.id.txtEmail);
        txtPassword = (EditText) findViewById(R.id.txtPassword);
        txtMobilePhone = (EditText) findViewById(R.id.txtMobilePhone);
        txtTitle = (EditText) findViewById(R.id.txtTitle);

        db.onUpgrade(db.getWritableDatabase(), 0, 0);
        //insert some dummy records
        Tbl_Personnel_Manager.current.Insert(db.getWritableDatabase(), new Tbl_Personnel("Kyle", "Wertz", "Wertz.8@wright.edu", "Password1", "111-111-1111", "Developer"));
        Tbl_Personnel_Manager.current.Insert(db.getWritableDatabase(), new Tbl_Personnel("Brandon", "Bradley", "Bradley.85@wright.edu", "Password2", "222-222-2222", "Developer"));
        Tbl_Personnel_Manager.current.Insert(db.getWritableDatabase(), new Tbl_Personnel("Naif", "Alqahtani", "Alqahtani.31@wright.edu", "Password3", "333-333-3333", "Developer"));
        Tbl_Personnel_Manager.current.Insert(db.getWritableDatabase(), new Tbl_Personnel("Justin", "Lagenbach", "Lagenbach.2@wright.edu", "Password4", "444-444-4444", "Developer"));

        Button btnAdd = (Button) findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                addNewRecord();
            }
        });

        Button btnClear = (Button) findViewById(R.id.btnClear);
        btnClear.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                clearFields();
            }
        });

        Button btnDelete = (Button) findViewById(R.id.btnDelete);
        btnDelete.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                DeleteData.SetSpecificTable(DeleteData.Table.PERSONNEL);
                Intent deleteData = new Intent(getApplicationContext(), DeleteData.class);
                startActivity(deleteData);
            }
        });

    }

    public void addNewRecord()
    {
        String firstName = txtFirstName.getText().toString();
        String lastName = txtLastName.getText().toString();
        String email = txtEmail.getText().toString();
        String password = txtPassword.getText().toString();
        String mobilePhone = txtMobilePhone.getText().toString();
        String title = txtTitle.getText().toString();
        Tbl_Personnel record = new Tbl_Personnel(firstName, lastName, email, password, mobilePhone, title);
        String anyErrors = record.isValidRecord();
        if (anyErrors.isEmpty())
        {
            Tbl_Personnel_Manager.current.Insert(db.getWritableDatabase(), record);
            Toast.makeText(AddPersonnel.this, "Personnel Added", Toast.LENGTH_LONG).show();
            clearFields();
        }
        else
        {
            Toast.makeText(AddPersonnel.this, anyErrors, Toast.LENGTH_LONG).show();
        }
    }

    public void clearFields()
    {
        txtFirstName.setText("");
        txtLastName.setText("");
        txtEmail.setText("");
        txtPassword.setText("");
        txtMobilePhone.setText("");
        txtTitle.setText("");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings)
        {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
