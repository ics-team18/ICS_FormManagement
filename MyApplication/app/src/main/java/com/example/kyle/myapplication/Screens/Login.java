package com.example.kyle.myapplication.Screens;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.kyle.myapplication.Database.Database_Manager;
import com.example.kyle.myapplication.Database.Tbl_Personnel;
import com.example.kyle.myapplication.Database.Tbl_Personnel_Manager;
import com.example.kyle.myapplication.R;

import java.util.ArrayList;
import java.util.List;

public class Login extends AppCompatActivity
{
    Button btnLogin, btnRegister;
    EditText txtEmail, txtPassword;
    Database_Manager db;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnRegister = (Button) findViewById(R.id.btnRegister);
        txtEmail = (EditText) findViewById(R.id.txtEmail);
        txtPassword = (EditText) findViewById(R.id.txtPassword);

        db = new Database_Manager(this);
        db.onUpgrade(db.getWritableDatabase(), 0, 0);
        //insert some dummy records
        Tbl_Personnel_Manager.current.Insert(db.getWritableDatabase(), new Tbl_Personnel("Kyle", "Wertz", "Wertz.8@wright.edu", "Password1", "111-111-1111", "Developer", true));
        Tbl_Personnel_Manager.current.Insert(db.getWritableDatabase(), new Tbl_Personnel("Brandon", "Bradley", "Bradley.85@wright.edu", "Password2", "222-222-2222", "Developer", true));
        Tbl_Personnel_Manager.current.Insert(db.getWritableDatabase(), new Tbl_Personnel("Naif", "Alqahtani", "Alqahtani.31@wright.edu", "Password3", "333-333-3333", "Developer", true));
        Tbl_Personnel_Manager.current.Insert(db.getWritableDatabase(), new Tbl_Personnel("Justin", "Lagenbach", "Lagenbach.2@wright.edu", "Password4", "444-444-4444", "Developer", true));

        btnLogin.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                login();
            }
        });
        btnRegister.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                register();
            }
        });
    }

    private void login()
    {
        Tbl_Personnel searchCriteria = new Tbl_Personnel();
        searchCriteria.email = txtEmail.getText().toString();
        searchCriteria.password = txtPassword.getText().toString();
        // for dev purposes we will leave this empty
        /*if (searchCriteria.email.isEmpty())
        {
            Toast.makeText(this, "E-mail address is required.", Toast.LENGTH_LONG).show();
        }
        else if (searchCriteria.password.isEmpty())
        {
            Toast.makeText(this, "Password is required.", Toast.LENGTH_LONG).show();
        }
        else*/
        {
            List<Tbl_Personnel> resultList = new ArrayList<>(Tbl_Personnel_Manager.current.Select(db.getReadableDatabase(), searchCriteria));

            //defaulted to true so you don't have to enter your username and password every time
            boolean isValid = true && resultList.size()>0; //resultList.size() == 1;
            if (isValid)
            {
                LoggedInUser.User = resultList.get(0);
                startActivity(new Intent(this, MainScreen.class));
            }
            else
            {
                Toast.makeText(this, "Invalid E-mail address or password.", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void register()
    {
        Register.fromHomeScreen(true);
        startActivity(new Intent(this, Register.class));
    }
}
