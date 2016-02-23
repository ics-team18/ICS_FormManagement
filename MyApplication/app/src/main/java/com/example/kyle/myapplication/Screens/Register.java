package com.example.kyle.myapplication.Screens;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.example.kyle.myapplication.Database.Database_Manager;
import com.example.kyle.myapplication.Database.Tbl_Personnel;
import com.example.kyle.myapplication.Database.Tbl_Personnel_Manager;
import com.example.kyle.myapplication.Helpers.LoggedInUser;
import com.example.kyle.myapplication.R;

public class Register extends AppCompatActivity
{
    private Database_Manager db;
    private EditText txtFirstName, txtLastName, txtPositionTitle, txtPhoneNumber, txtEmail, txtPassword, txtConfirmPassword;
    private CheckBox chkIsSupervisor;
    private static int btnDeleteVisibility = View.GONE;
    private static boolean fromLoginScreen = false;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        db = new Database_Manager(this);
        txtFirstName = (EditText) findViewById(R.id.txtFirstName);
        txtLastName = (EditText) findViewById(R.id.txtLastName);
        txtPositionTitle = (EditText) findViewById(R.id.txtPositionTitle);
        txtPhoneNumber = (EditText) findViewById(R.id.txtPhoneNumber);
        txtEmail = (EditText) findViewById(R.id.txtEmail);
        txtPassword = (EditText) findViewById(R.id.txtPassword);
        txtConfirmPassword = (EditText) findViewById(R.id.txtConfirmPassword);
        chkIsSupervisor = (CheckBox) findViewById(R.id.chkIsSupervisor);

        Button btnRegister = (Button) findViewById(R.id.btnRegister);
        btnRegister.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                register();
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

        Button btnDelete = (Button) findViewById(R.id.btnDelete);

        if (fromLoginScreen)
        {
            //if we are coming from the login screen then the button will always be invisible
            btnDeleteVisibility = View.GONE;
            //if we are coming from the login screen then it is likely the user is setting up their own account
            //so use the user's phone number
            setUsersPhoneNumber();
        }
        else
        {
            //otherwise we will check to see if the user has access to delete
            btnDeleteVisibility = LoggedInUser.User.isSupervisor ? View.VISIBLE : View.GONE;
        }

        btnDelete.setVisibility(btnDeleteVisibility);
        btnDelete.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                delete();
            }
        });

        Button btnPhoneNumber = (Button) findViewById(R.id.btnPhoneNumber);
        btnPhoneNumber.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                setUsersPhoneNumber();
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
            String title = txtPositionTitle.getText().toString();
            String mobilePhone = txtPhoneNumber.getText().toString();
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
        txtPhoneNumber.setText("");
    }

    private void delete()
    {
        DeleteData.SetSpecificTable(DeleteData.Table.PERSONNEL);
        startActivity(new Intent(getApplicationContext(), DeleteData.class));
    }

    private void setUsersPhoneNumber()
    {
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

        txtPhoneNumber.setText(mobilePhone);
    }

    public static void fromHomeScreen(boolean fromLogin)
    {
        fromLoginScreen = fromLogin;
    }
}
