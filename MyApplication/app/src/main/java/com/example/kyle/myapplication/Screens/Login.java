package com.example.kyle.myapplication.Screens;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.kyle.myapplication.Database.Abstract.Abstract_Table;
import com.example.kyle.myapplication.Database.DBOperation;
import com.example.kyle.myapplication.Database.DatabaseManager;
import com.example.kyle.myapplication.Database.Incident.Tbl_Incident;
import com.example.kyle.myapplication.Database.Incident.Tbl_Incident_Manager;
import com.example.kyle.myapplication.Database.IncidentLink.Tbl_IncidentLink;
import com.example.kyle.myapplication.Database.IncidentLink.Tbl_IncidentLink_Manager;
import com.example.kyle.myapplication.Database.Personnel.Tbl_Personnel;
import com.example.kyle.myapplication.Database.Personnel.Tbl_Personnel_Manager;
import com.example.kyle.myapplication.Database.Role.Tbl_Role;
import com.example.kyle.myapplication.Database.Role.Tbl_Role_Manager;
import com.example.kyle.myapplication.Database.SubmittedForms.Tbl_SubmittedForms;
import com.example.kyle.myapplication.Database.SubmittedForms.Tbl_SubmittedForms_Manager;
import com.example.kyle.myapplication.Database.Templates.Tbl_Templates;
import com.example.kyle.myapplication.Database.Templates.Tbl_Templates_Manager;
import com.example.kyle.myapplication.Helpers.FTPManager;
import com.example.kyle.myapplication.Helpers.LoggedInUser;
import com.example.kyle.myapplication.Helpers.OpenScreens;
import com.example.kyle.myapplication.R;

import java.util.ArrayList;
import java.util.List;

public class Login extends AppCompatActivity
{
    Button btnLogin, btnRegister;
    EditText txtEmail, txtPassword;

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

        performDevTests();
    }

    private void performDevTests()
    {
        Thread thread = new Thread()
        {
            @Override
            public void run()
            {
                try
                {
                    synchronized (this)
                    {
                        testDatabase();
                        testUploadDownload();
                    }
                }
                catch (Exception ex)
                {
                }
            }
        };
        thread.start();
    }

    private void testUploadDownload()
    {
        //UploadDownloadFile(Context context, boolean isUpload, boolean isTemplate, String filePathAndName)
        //download the template and put it into the specified path
        FTPManager.UploadDownloadFile(this, FTPManager.FTPMode.DOWNLOAD, "data/data/com.example.kyle.myapplication/Incident Commander - Template1.docx", "Incident Commander - Template1.docx");
    }

    private void testDatabase()
    {
        String op = Tbl_Personnel_Manager.current.GetFullCreateScript() +
                Tbl_Role_Manager.current.GetFullCreateScript() +
                Tbl_Incident_Manager.current.GetFullCreateScript() +
                Tbl_IncidentLink_Manager.current.GetFullCreateScript() +
                Tbl_Templates_Manager.current.GetFullCreateScript() +
                Tbl_SubmittedForms_Manager.current.GetFullCreateScript();

        DBOperation operation = new DBOperation(Abstract_Table.SQLMode.CREATETTABLES, op, "ICS database successfully created", "Unable to create ICS database");
        DatabaseManager.CreateDatabase(this, operation);

        //personnel
        Tbl_Personnel person1 = new Tbl_Personnel("Kyle", "Wertz", "Wertz.8@wright.edu", "Password1", "111-111-1111", "Developer", true);
        person1.sqlMode = Abstract_Table.SQLMode.INSERT;
        Tbl_Personnel person2 = new Tbl_Personnel("Brandon", "Bradley", "Bradley.85@wright.edu", "Password2", "222-222-2222", "Developer", true);
        person2.sqlMode = Abstract_Table.SQLMode.INSERT;
        Tbl_Personnel person3 = new Tbl_Personnel("Naif", "Alqahtani", "Alqahtani.31@wright.edu", "Password3", "333-333-3333", "Developer", true);
        person3.sqlMode = Abstract_Table.SQLMode.INSERT;
        Tbl_Personnel person4 = new Tbl_Personnel("Justin", "Lagenbach", "Lagenbach.2@wright.edu", "Password4", "444-444-4444", "Developer", true);
        person4.sqlMode = Abstract_Table.SQLMode.INSERT;
        Tbl_Personnel_Manager.current.RecordOperation(person1);
        Tbl_Personnel_Manager.current.RecordOperation(person2);
        Tbl_Personnel_Manager.current.RecordOperation(person3);
        Tbl_Personnel_Manager.current.RecordOperation(person4);

        //roles
        //examples taken from https://upload.wikimedia.org/wikipedia/commons/3/3e/ICS_Structure.PNG
        Tbl_Role role1 = new Tbl_Role("Incident Commander");
        role1.sqlMode = Abstract_Table.SQLMode.INSERT;
        Tbl_Role role2 = new Tbl_Role("Public Information Officer");
        role2.sqlMode = Abstract_Table.SQLMode.INSERT;
        Tbl_Role role3 = new Tbl_Role("Liaison Officer");
        role3.sqlMode = Abstract_Table.SQLMode.INSERT;
        Tbl_Role role4 = new Tbl_Role("Safety Officer");
        role4.sqlMode = Abstract_Table.SQLMode.INSERT;
        Tbl_Role_Manager.current.RecordOperation(role1);
        Tbl_Role_Manager.current.RecordOperation(role2);
        Tbl_Role_Manager.current.RecordOperation(role3);
        Tbl_Role_Manager.current.RecordOperation(role4);

        try
        {
            Thread.sleep(1000);
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }

        //templates
        Tbl_Templates template1 = new Tbl_Templates(1, "Template 1", "Incident Commander - Template1.docx");
        template1.sqlMode = Abstract_Table.SQLMode.INSERT;
        Tbl_Templates_Manager.current.RecordOperation(template1);
        Tbl_Templates template2 = new Tbl_Templates(2, "Template 2", "Public Information Officer - Template2.docx");
        template2.sqlMode = Abstract_Table.SQLMode.INSERT;
        Tbl_Templates_Manager.current.RecordOperation(template2);
        Tbl_Templates template3 = new Tbl_Templates(3, "Template 3", "Liaison Officer - Template3.docx");
        template3.sqlMode = Abstract_Table.SQLMode.INSERT;
        Tbl_Templates_Manager.current.RecordOperation(template3);
        Tbl_Templates template4 = new Tbl_Templates(4, "Template 4", "Safety Officer - Template4.docx");
        template4.sqlMode = Abstract_Table.SQLMode.INSERT;
        Tbl_Templates_Manager.current.RecordOperation(template4);
        Tbl_Templates template5 = new Tbl_Templates(1, "Template 5", "Public Information Officer - Template5.docx");
        template5.sqlMode = Abstract_Table.SQLMode.INSERT;
        Tbl_Templates_Manager.current.RecordOperation(template5);
        Tbl_Templates template6 = new Tbl_Templates(2, "Template 6", "Incident Commander - Template6.docx");
        template6.sqlMode = Abstract_Table.SQLMode.INSERT;
        Tbl_Templates_Manager.current.RecordOperation(template6);
        Tbl_Templates template7 = new Tbl_Templates(3, "Template 7", "Public Information Officer - Template7.docx");
        template7.sqlMode = Abstract_Table.SQLMode.INSERT;
        Tbl_Templates_Manager.current.RecordOperation(template7);
        Tbl_Templates template8 = new Tbl_Templates(4, "Template 8", "Liaison Officer - Template8.docx");
        template8.sqlMode = Abstract_Table.SQLMode.INSERT;
        Tbl_Templates_Manager.current.RecordOperation(template8);
        Tbl_Templates template9 = new Tbl_Templates(1, "Template 9", "Safety Officer - Template9.docx");
        template9.sqlMode = Abstract_Table.SQLMode.INSERT;
        Tbl_Templates_Manager.current.RecordOperation(template9);
        Tbl_Templates template10 = new Tbl_Templates(2, "Template 10", "Public Information Officer - Template10.docx");
        template10.sqlMode = Abstract_Table.SQLMode.INSERT;
        Tbl_Templates_Manager.current.RecordOperation(template10);
        Tbl_Templates template11 = new Tbl_Templates(3, "Template 11", "Incident Commander - Template11.docx");
        template11.sqlMode = Abstract_Table.SQLMode.INSERT;
        Tbl_Templates_Manager.current.RecordOperation(template11);
        Tbl_Templates template12 = new Tbl_Templates(4, "Template 12", "Public Information Officer - Template12.docx");
        template12.sqlMode = Abstract_Table.SQLMode.INSERT;
        Tbl_Templates_Manager.current.RecordOperation(template12);
        Tbl_Templates template13 = new Tbl_Templates(1, "Template 13", "Liaison Officer - Template13.docx");
        template13.sqlMode = Abstract_Table.SQLMode.INSERT;
        Tbl_Templates_Manager.current.RecordOperation(template13);
        Tbl_Templates template14 = new Tbl_Templates(2, "Template 14", "Safety Officer - Template14.docx");
        template14.sqlMode = Abstract_Table.SQLMode.INSERT;
        Tbl_Templates_Manager.current.RecordOperation(template14);
        Tbl_Templates template15 = new Tbl_Templates(3, "Template 15", "Public Information Officer - Template15.docx");
        template15.sqlMode = Abstract_Table.SQLMode.INSERT;
        Tbl_Templates_Manager.current.RecordOperation(template15);
        Tbl_Templates template16 = new Tbl_Templates(4, "Template 16", "Incident Commander - Template16.docx");
        template16.sqlMode = Abstract_Table.SQLMode.INSERT;
        Tbl_Templates_Manager.current.RecordOperation(template16);

        //incidents
        List<Tbl_IncidentLink> dummyLinks = new ArrayList<Tbl_IncidentLink>();
        Tbl_IncidentLink incidentLink1 = new Tbl_IncidentLink(1, 1, 1, 1);
        incidentLink1.sqlMode = Abstract_Table.SQLMode.INSERT;
        Tbl_IncidentLink incidentLink2 = new Tbl_IncidentLink(1, 2, 2, 2);
        incidentLink2.sqlMode = Abstract_Table.SQLMode.INSERT;
        Tbl_IncidentLink incidentLink3 = new Tbl_IncidentLink(1, 3, 3, 3);
        incidentLink3.sqlMode = Abstract_Table.SQLMode.INSERT;
        Tbl_IncidentLink incidentLink4 = new Tbl_IncidentLink(1, 4, 4, 4);
        incidentLink4.sqlMode = Abstract_Table.SQLMode.INSERT;
        dummyLinks.add(incidentLink1);
        dummyLinks.add(incidentLink2);
        dummyLinks.add(incidentLink3);
        dummyLinks.add(incidentLink4);

        List<Tbl_SubmittedForms> dummyForms = new ArrayList<Tbl_SubmittedForms>();
        Tbl_SubmittedForms forms1 = new Tbl_SubmittedForms(1, 1, 1, "data/data/com.example.kyle.myapplication/1.docx", "1.docx", "1st form", "09-11-2001 08:47:00");
        forms1.sqlMode = Abstract_Table.SQLMode.INSERT;
        Tbl_SubmittedForms forms2 = new Tbl_SubmittedForms(1, 2, 2, "data/data/com.example.kyle.myapplication/2.docx", "2.docx", "2nd form", "09-11-2001 09:46:00");
        forms2.sqlMode = Abstract_Table.SQLMode.INSERT;
        Tbl_SubmittedForms forms3 = new Tbl_SubmittedForms(1, 3, 3, "data/data/com.example.kyle.myapplication/3.docx", "3.docx", "3rd form", "09-11-2001 10:46:00");
        forms3.sqlMode = Abstract_Table.SQLMode.INSERT;
        Tbl_SubmittedForms forms4 = new Tbl_SubmittedForms(1, 4, 4, "data/data/com.example.kyle.myapplication/4.docx", "4.docx", "4th form", "09-11-2001 11:46:00");
        forms4.sqlMode = Abstract_Table.SQLMode.INSERT;
        dummyForms.add(forms1);
        dummyForms.add(forms2);
        dummyForms.add(forms3);
        dummyForms.add(forms4);

        Tbl_Incident incident1 = new Tbl_Incident("09-11-2001 08:46:00", "", "Attack on the first tower", "180 Greenwich St", "New York NY 10007", "40.711667", "-74.0125", dummyLinks, dummyForms);
        incident1.sqlMode = Abstract_Table.SQLMode.INSERT;
        Tbl_Incident_Manager.current.RecordOperation(incident1);

        dummyLinks = new ArrayList<Tbl_IncidentLink>();
        incidentLink1 = new Tbl_IncidentLink(2, 2, 3, 1);
        incidentLink1.sqlMode = Abstract_Table.SQLMode.INSERT;
        incidentLink2 = new Tbl_IncidentLink(2, 1, 4, 2);
        incidentLink2.sqlMode = Abstract_Table.SQLMode.INSERT;
        incidentLink3 = new Tbl_IncidentLink(2, 3, 2, 3);
        incidentLink3.sqlMode = Abstract_Table.SQLMode.INSERT;
        incidentLink4 = new Tbl_IncidentLink(2, 4, 1, 4);
        incidentLink4.sqlMode = Abstract_Table.SQLMode.INSERT;
        dummyLinks.add(incidentLink1);
        dummyLinks.add(incidentLink2);
        dummyLinks.add(incidentLink3);
        dummyLinks.add(incidentLink4);

        dummyForms = new ArrayList<Tbl_SubmittedForms>();
        forms1 = new Tbl_SubmittedForms(3, 4, 5, "data/data/com.example.kyle.myapplication/1.docx", "1.docx", "1st form", "09-11-2001 09:04:00");
        forms1.sqlMode = Abstract_Table.SQLMode.INSERT;
        forms2 = new Tbl_SubmittedForms(3, 3, 6, "data/data/com.example.kyle.myapplication/2.docx", "2.docx", "2nd form", "09-11-2001 10:03:00");
        forms2.sqlMode = Abstract_Table.SQLMode.INSERT;
        forms3 = new Tbl_SubmittedForms(3, 2, 7, "data/data/com.example.kyle.myapplication/3.docx", "3.docx", "3rd form", "09-11-2001 11:03:00");
        forms3.sqlMode = Abstract_Table.SQLMode.INSERT;
        forms4 = new Tbl_SubmittedForms(3, 1, 8, "data/data/com.example.kyle.myapplication/4.docx", "4.docx", "4th form", "09-11-2001 12:03:00");
        forms4.sqlMode = Abstract_Table.SQLMode.INSERT;
        dummyForms.add(forms1);
        dummyForms.add(forms2);
        dummyForms.add(forms3);
        dummyForms.add(forms4);

        Tbl_Incident incident2 = new Tbl_Incident("09-11-2001 09:03:00", "", "Attack on the second tower", "180 Greenwich St", "New York NY 10007", "40.711667", "-74.0125", dummyLinks, dummyForms);
        incident2.sqlMode = Abstract_Table.SQLMode.INSERT;
        Tbl_Incident_Manager.current.RecordOperation(incident2);

        dummyLinks = new ArrayList<Tbl_IncidentLink>();
        incidentLink1 = new Tbl_IncidentLink(3, 4, 1, 1);
        incidentLink1.sqlMode = Abstract_Table.SQLMode.INSERT;
        incidentLink2 = new Tbl_IncidentLink(3, 2, 4, 2);
        incidentLink2.sqlMode = Abstract_Table.SQLMode.INSERT;
        incidentLink3 = new Tbl_IncidentLink(3, 3, 3, 3);
        incidentLink3.sqlMode = Abstract_Table.SQLMode.INSERT;
        incidentLink4 = new Tbl_IncidentLink(3, 1, 2, 4);
        incidentLink4.sqlMode = Abstract_Table.SQLMode.INSERT;
        dummyLinks.add(incidentLink1);
        dummyLinks.add(incidentLink2);
        dummyLinks.add(incidentLink3);
        dummyLinks.add(incidentLink4);

        dummyForms = new ArrayList<Tbl_SubmittedForms>();
        forms1 = new Tbl_SubmittedForms(4, 4, 9, "data/data/com.example.kyle.myapplication/1.docx", "1.docx", "1st form", "02-23-2016 14:26:01");
        forms1.sqlMode = Abstract_Table.SQLMode.INSERT;
        forms2 = new Tbl_SubmittedForms(4, 1, 10, "data/data/com.example.kyle.myapplication/2.docx", "2.docx", "2nd form", "02-23-2016 15:25:01");
        forms2.sqlMode = Abstract_Table.SQLMode.INSERT;
        forms3 = new Tbl_SubmittedForms(4, 3, 11, "data/data/com.example.kyle.myapplication/3.docx", "3.docx", "3rd form", "02-23-2016 16:25:01");
        forms3.sqlMode = Abstract_Table.SQLMode.INSERT;
        forms4 = new Tbl_SubmittedForms(4, 2, 12, "data/data/com.example.kyle.myapplication/4.docx", "4.docx", "4th form", "02-23-2016 17:25:01");
        forms4.sqlMode = Abstract_Table.SQLMode.INSERT;
        dummyForms.add(forms1);
        dummyForms.add(forms2);
        dummyForms.add(forms3);
        dummyForms.add(forms4);

        Tbl_Incident incident3 = new Tbl_Incident("02-23-2016 14:25:01", "", "Fire at Wright State", "3640 Colonel Glenn Hwy", "Fairborn, OH 45342", "39.7815", "-84.0635983", dummyLinks, dummyForms);
        incident3.sqlMode = Abstract_Table.SQLMode.INSERT;
        Tbl_Incident_Manager.current.RecordOperation(incident3);

        dummyLinks = new ArrayList<Tbl_IncidentLink>();
        incidentLink1 = new Tbl_IncidentLink(4, 3, 4, 1);
        incidentLink1.sqlMode = Abstract_Table.SQLMode.INSERT;
        incidentLink2 = new Tbl_IncidentLink(4, 4, 1, 2);
        incidentLink2.sqlMode = Abstract_Table.SQLMode.INSERT;
        incidentLink3 = new Tbl_IncidentLink(4, 2, 2, 3);
        incidentLink3.sqlMode = Abstract_Table.SQLMode.INSERT;
        incidentLink4 = new Tbl_IncidentLink(4, 1, 3, 4);
        incidentLink4.sqlMode = Abstract_Table.SQLMode.INSERT;
        dummyLinks.add(incidentLink1);
        dummyLinks.add(incidentLink2);
        dummyLinks.add(incidentLink3);
        dummyLinks.add(incidentLink4);

        dummyForms = new ArrayList<Tbl_SubmittedForms>();
        forms1 = new Tbl_SubmittedForms(4, 4, 13, "data/data/com.example.kyle.myapplication/1.docx", "1.docx", "1st form", "02-25-2016 19:06:45");
        forms1.sqlMode = Abstract_Table.SQLMode.INSERT;
        forms2 = new Tbl_SubmittedForms(4, 2, 14, "data/data/com.example.kyle.myapplication/2.docx", "2.docx", "2nd form", "02-25-2016 20:05:45");
        forms2.sqlMode = Abstract_Table.SQLMode.INSERT;
        forms3 = new Tbl_SubmittedForms(4, 1, 15, "data/data/com.example.kyle.myapplication/3.docx", "3.docx", "3rd form", "02-25-2016 21:05:45");
        forms3.sqlMode = Abstract_Table.SQLMode.INSERT;
        forms4 = new Tbl_SubmittedForms(4, 3, 16, "data/data/com.example.kyle.myapplication/4.docx", "4.docx", "4th form", "02-25-2016 22:05:45");
        forms4.sqlMode = Abstract_Table.SQLMode.INSERT;
        dummyForms.add(forms1);
        dummyForms.add(forms2);
        dummyForms.add(forms3);
        dummyForms.add(forms4);

        Tbl_Incident incident4 = new Tbl_Incident("02-25-2016 19:05:45", "", "Burglary at Wright State", "3640 Colonel Glenn Hwy", "Fairborn, OH 45342", "39.7815", "-84.0635983", dummyLinks, dummyForms);
        incident4.sqlMode = Abstract_Table.SQLMode.INSERT;
        Tbl_Incident_Manager.current.RecordOperation(incident4);
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
            List<Tbl_Personnel> resultList = Tbl_Personnel_Manager.current.Select(searchCriteria);

            //defaulted to true so you don't have to enter your username and password every time
            boolean isValid = true && resultList.size() > 0; //resultList.size() == 1;
            //boolean isValid = resultList.size() == 1;
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
