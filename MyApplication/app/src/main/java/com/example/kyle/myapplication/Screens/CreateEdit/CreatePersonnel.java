package com.example.kyle.myapplication.Screens.CreateEdit;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.example.kyle.myapplication.Database.Abstract_Table_Manager;
import com.example.kyle.myapplication.Database.Database_Manager;
import com.example.kyle.myapplication.Database.Tbl_Personnel;
import com.example.kyle.myapplication.Database.Tbl_Personnel_Manager;
import com.example.kyle.myapplication.Helpers.LoggedInUser;
import com.example.kyle.myapplication.Helpers.OpenScreens;
import com.example.kyle.myapplication.R;
import com.example.kyle.myapplication.Screens.DataList;

public class CreatePersonnel extends AppCompatActivity
{
    private Database_Manager db;
    private EditText txtFirstName, txtLastName, txtPositionTitle, txtPhoneNumber, txtEmail, txtPassword, txtConfirmPassword;
    private CheckBox chkIsSupervisor;
    public static boolean fromLoginScreen = true;
    public static Tbl_Personnel toUpdate = null;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
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

        if (toUpdate != null)
        {
            btnRegister.setText("Update");
            btnClear.setText("Revert Changes");
        }

        Button btnPhoneNumber = (Button) findViewById(R.id.btnPhoneNumber);
        btnPhoneNumber.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                setUsersPhoneNumber();
            }
        });

        clear();

        int isSupervisorVisible;
        if (fromLoginScreen)
        {
            //if we are coming from the login screen then the button will always be invisible
            isSupervisorVisible = View.GONE;
            //if we are coming from the login screen then it is likely the user is setting up their own account
            //so use the user's phone number
            setUsersPhoneNumber();
        }
        else
        {
            //otherwise we will check to see if the user has access to delete
            isSupervisorVisible = LoggedInUser.User.isSupervisor ? View.VISIBLE : View.GONE;
        }

        chkIsSupervisor.setVisibility(isSupervisorVisible);
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
                if (toUpdate != null)
                {
                    record.personnelID = toUpdate.personnelID;
                    toUpdate = record;
                    Tbl_Personnel_Manager.current.Update(db.getWritableDatabase(), toUpdate);
                    Toast.makeText(this, "Personnel Updated", Toast.LENGTH_LONG).show();
                    OpenScreens.OpenDataListScreen(DataList.Mode.Edit, Abstract_Table_Manager.Table.PERSONNEL);
                }
                else
                {
                    Tbl_Personnel_Manager.current.Insert(db.getWritableDatabase(), record);
                    Toast.makeText(this, "Personnel Added", Toast.LENGTH_LONG).show();
                }
                finish();
            }
            else
            {
                Toast.makeText(this, anyErrors, Toast.LENGTH_LONG).show();
            }
        }
    }

    private void clear()
    {
        if (toUpdate != null)
        {
            txtFirstName.setText(toUpdate.firstName);
            txtLastName.setText(toUpdate.lastName);
            txtPositionTitle.setText(toUpdate.title);
            txtPhoneNumber.setText(toUpdate.mobilePhone);
            txtEmail.setText(toUpdate.email);
            txtPassword.setText(toUpdate.password);
            txtConfirmPassword.setText(toUpdate.password);
            chkIsSupervisor.setChecked(toUpdate.isSupervisor);
        }
        else
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
}
