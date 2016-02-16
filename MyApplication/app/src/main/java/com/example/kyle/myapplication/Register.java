package com.example.kyle.myapplication;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kyle.myapplication.Database.Database_Manager;
import com.example.kyle.myapplication.Database.Tbl_Personnel;
import com.example.kyle.myapplication.Database.Tbl_Personnel_Manager;

public class Register extends AppCompatActivity
{
    Database_Manager db;
    EditText txtFirstName, txtLastName, txtPositionTitle, txtEmail, txtPassword, txtConfirmPassword;
    CheckBox chkIsSupervisor;
    static int btnDeleteVisibility = View.GONE;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        db = new Database_Manager(this);
        Button btnRegister = (Button) findViewById(R.id.btnRegister);
        Button btnClear = (Button) findViewById(R.id.btnClear);
        Button btnDelete = (Button) findViewById(R.id.btnDelete);
        btnDelete.setVisibility(btnDeleteVisibility);
        txtFirstName = (EditText) findViewById(R.id.txtFirstName);
        txtLastName = (EditText) findViewById(R.id.txtLastName);
        txtPositionTitle = (EditText) findViewById(R.id.txtPositionTitle);
        txtEmail = (EditText) findViewById(R.id.txtEmail);
        txtPassword = (EditText) findViewById(R.id.txtPassword);
        txtConfirmPassword = (EditText) findViewById(R.id.txtConfirmPassword);
        chkIsSupervisor = (CheckBox) findViewById(R.id.chkIsSupervisor);
        btnRegister.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                register();
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

        btnDelete.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                delete();
            }
        });

    }

    private void register()
    {
        String password = txtPassword.getText().toString();
        String confirmPassword = txtConfirmPassword.getText().toString();
        if (!password.equals(confirmPassword))
        {
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_LONG).show();
        }
        else
        {
            String firstName = txtFirstName.getText().toString();
            String lastName = txtLastName.getText().toString();
            String email = txtEmail.getText().toString();
            String mobilePhone = "123-456-7890";
            try
            {
                TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
                mobilePhone = tm.getLine1Number();
            }
            catch (Exception ex)
            {
                // unable to get the mobile phone number
                // this should only happen for development because the emulator doesn't have a phone number
                Toast.makeText(this, "Phone number not found, using default phone number", Toast.LENGTH_LONG).show();
            }
            String title = txtPositionTitle.getText().toString();
            Boolean isSupervisor = chkIsSupervisor.isChecked();
            Tbl_Personnel record = new Tbl_Personnel(firstName, lastName, email, password, mobilePhone, title, isSupervisor);
            String anyErrors = record.isValidRecord();
            if (anyErrors.isEmpty())
            {
                Tbl_Personnel_Manager.current.Insert(db.getWritableDatabase(), record);
                Toast.makeText(this, "Personnel Added", Toast.LENGTH_LONG).show();
                clear();
            }
            else
            {
                Toast.makeText(this, anyErrors, Toast.LENGTH_LONG).show();
            }
        }
    }

    private void clear()
    {
        txtFirstName.setText("");
        txtLastName.setText("");
        txtEmail.setText("");
        txtPositionTitle.setText("");
        txtPassword.setText("");
        txtConfirmPassword.setText("");
        chkIsSupervisor.setChecked(false);
    }

    private void delete()
    {
        DeleteData.SetSpecificTable(DeleteData.Table.PERSONNEL);
        startActivity(new Intent(getApplicationContext(), DeleteData.class));
    }

    public static void fromHomeScreen(boolean fromLoginScreen)
    {
        if (fromLoginScreen)
        {
            //if we are coming from the login screen then the button will always be invisible
            btnDeleteVisibility = View.GONE;
        }
        else
        {
         //otherwise we will check to see if the user has access to delete
            btnDeleteVisibility = LoggedInUser.User.isSupervisor? View.VISIBLE : View.GONE;
        }
    }
}
