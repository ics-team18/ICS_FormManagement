package com.example.kyle.myapplication.Screens;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.kyle.myapplication.Database.Database_Manager;
import com.example.kyle.myapplication.Database.Incident.Tbl_Incident;
import com.example.kyle.myapplication.Database.IncidentLink.Tbl_IncidentLink;
import com.example.kyle.myapplication.Database.Incident.Tbl_Incident_Manager;
import com.example.kyle.myapplication.Database.Personnel.Tbl_Personnel;
import com.example.kyle.myapplication.Database.Personnel.Tbl_Personnel_Manager;
import com.example.kyle.myapplication.Database.Role.Tbl_Role;
import com.example.kyle.myapplication.Database.Role.Tbl_Role_Manager;
import com.example.kyle.myapplication.Helpers.LoggedInUser;
import com.example.kyle.myapplication.Helpers.OpenScreens;
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
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnRegister = (Button) findViewById(R.id.btnRegister);
        txtEmail = (EditText) findViewById(R.id.txtEmail);
        txtPassword = (EditText) findViewById(R.id.txtPassword);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        db = new Database_Manager(this);
        db.onUpgrade(db.getWritableDatabase(), 0, 0);
        insertDummyRecords();
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
                OpenScreens.OpenCreatePersonnelScreen(null, true);
            }
        });
    }

    private void insertDummyRecords()
    {
        //personnel
        Tbl_Personnel_Manager.current.Insert(db.getWritableDatabase(), new Tbl_Personnel("Kyle", "Wertz", "Wertz.8@wright.edu", "Password1", "111-111-1111", "Developer", true));
        Tbl_Personnel_Manager.current.Insert(db.getWritableDatabase(), new Tbl_Personnel("Brandon", "Bradley", "Bradley.85@wright.edu", "Password2", "222-222-2222", "Developer", true));
        Tbl_Personnel_Manager.current.Insert(db.getWritableDatabase(), new Tbl_Personnel("Naif", "Alqahtani", "Alqahtani.31@wright.edu", "Password3", "333-333-3333", "Developer", true));
        Tbl_Personnel_Manager.current.Insert(db.getWritableDatabase(), new Tbl_Personnel("Justin", "Lagenbach", "Lagenbach.2@wright.edu", "Password4", "444-444-4444", "Developer", true));

        //roles
        //examples taken from https://upload.wikimedia.org/wikipedia/commons/3/3e/ICS_Structure.PNG
        Tbl_Role_Manager.current.Insert(db.getWritableDatabase(), new Tbl_Role("Incident Commander"));
        Tbl_Role_Manager.current.Insert(db.getWritableDatabase(), new Tbl_Role("Public Information Officer"));
        Tbl_Role_Manager.current.Insert(db.getWritableDatabase(), new Tbl_Role("Liaison Officer"));
        Tbl_Role_Manager.current.Insert(db.getWritableDatabase(), new Tbl_Role("Safety Officer"));

        //incidents
        List<Tbl_IncidentLink> dummyLinks1 = new ArrayList<Tbl_IncidentLink>();
        dummyLinks1.add(new Tbl_IncidentLink(1, 1, 1, 1));
        dummyLinks1.add(new Tbl_IncidentLink(1, 2, 2, 2));
        dummyLinks1.add(new Tbl_IncidentLink(1, 3, 3, 3));
        dummyLinks1.add(new Tbl_IncidentLink(1, 4, 4, 4));
        Tbl_Incident_Manager.current.Insert(db.getWritableDatabase(), new Tbl_Incident("09-11-2001 08:46:00", "", "Attack on the first tower", "180 Greenwich St", "New York NY 10007", 40.711667, -74.0125, dummyLinks1));

        List<Tbl_IncidentLink> dummyLinks2 = new ArrayList<Tbl_IncidentLink>();
        dummyLinks2.add(new Tbl_IncidentLink(2, 2, 3, 1));
        dummyLinks2.add(new Tbl_IncidentLink(2, 1, 4, 2));
        dummyLinks2.add(new Tbl_IncidentLink(2, 3, 2, 3));
        dummyLinks2.add(new Tbl_IncidentLink(2, 4, 1, 4));
        Tbl_Incident_Manager.current.Insert(db.getWritableDatabase(), new Tbl_Incident("09-11-2001 09:03:00", "", "Attack on the second tower", "180 Greenwich St", "New York NY 10007", 40.711667, -74.0125, dummyLinks2));

        List<Tbl_IncidentLink> dummyLinks3 = new ArrayList<Tbl_IncidentLink>();
        dummyLinks3.add(new Tbl_IncidentLink(3, 4, 1, 1));
        dummyLinks3.add(new Tbl_IncidentLink(3, 2, 4, 2));
        dummyLinks3.add(new Tbl_IncidentLink(3, 3, 3, 3));
        dummyLinks3.add(new Tbl_IncidentLink(3, 1, 2, 4));
        Tbl_Incident_Manager.current.Insert(db.getWritableDatabase(), new Tbl_Incident("02-23-2016 14:25:01", "", "Fire at Wright State", "3640 Colonel Glenn Hwy", "Fairborn, OH 45342", 39.7815, -84.0635983, dummyLinks3));

        List<Tbl_IncidentLink> dummyLinks4 = new ArrayList<Tbl_IncidentLink>();
        dummyLinks4.add(new Tbl_IncidentLink(4, 3, 4, 1));
        dummyLinks4.add(new Tbl_IncidentLink(4, 4, 1, 2));
        dummyLinks4.add(new Tbl_IncidentLink(4, 2, 2, 3));
        dummyLinks4.add(new Tbl_IncidentLink(4, 1, 3, 4));
        Tbl_Incident_Manager.current.Insert(db.getWritableDatabase(), new Tbl_Incident("02-25-2016 19:05:45", "", "Burglary at Wright State", "3640 Colonel Glenn Hwy", "Fairborn, OH 45342", 39.7815, -84.0635983, dummyLinks4));

        //submitted forms
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
            boolean isValid = true && resultList.size() > 0; //resultList.size() == 1;
            if (isValid)
            {
                LoggedInUser.User = resultList.get(0);
                OpenScreens.OpenMainScreen();
            }
            else
            {
                Toast.makeText(this, "Invalid E-mail address or password.", Toast.LENGTH_LONG).show();
            }
        }
    }
}
