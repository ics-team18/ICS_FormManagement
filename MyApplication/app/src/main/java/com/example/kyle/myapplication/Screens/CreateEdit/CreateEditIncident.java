package com.example.kyle.myapplication.Screens.CreateEdit;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kyle.myapplication.Database.Abstract.Abstract_Table;
import com.example.kyle.myapplication.Database.Abstract.Abstract_Table_Manager;
import com.example.kyle.myapplication.Database.DBOperation;
import com.example.kyle.myapplication.Database.Incident.Tbl_Incident;
import com.example.kyle.myapplication.Database.IncidentLink.Tbl_IncidentLink;
import com.example.kyle.myapplication.Database.Incident.Tbl_Incident_Manager;
import com.example.kyle.myapplication.Database.SubmittedForms.Tbl_SubmittedForms;
import com.example.kyle.myapplication.Helpers.OpenScreens;
import com.example.kyle.myapplication.R;
import com.example.kyle.myapplication.Screens.DataList;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class CreateEditIncident extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener
{
    private TextView lblDate;
    private EditText txtDescription, txtAddress, txtCitySTZip, txtLatitude, txtLongitude;
    public static List<Tbl_IncidentLink> incidentLinks = new ArrayList<>();
    public static List<Tbl_SubmittedForms> submittedForms = new ArrayList<>();
    public static Tbl_Incident toUpdate = null;
    private static final int PERMISSION_REQUEST_CODE = 1;
    private GoogleApiClient googleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_createedit_incident);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        lblDate = (TextView) findViewById(R.id.lblDate);
        txtDescription = (EditText) findViewById(R.id.txtDescription);
        txtAddress = (EditText) findViewById(R.id.txtAddress);
        txtCitySTZip = (EditText) findViewById(R.id.txtCitySTZip);
        txtLatitude = (EditText) findViewById(R.id.txtLatitude);
        txtLongitude = (EditText) findViewById(R.id.txtLongitude);
        googleApiClient = new GoogleApiClient.Builder(this).addConnectionCallbacks(this).addOnConnectionFailedListener(this).addApi(LocationServices.API).addApi(AppIndex.API).build();
        //auto update the time and the incident id
        Thread t = new Thread()
        {
            @Override
            public void run()
            {
                try
                {
                    while (!isInterrupted())
                    {
                        Thread.sleep(1000);
                        runOnUiThread(new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                setDate();
                            }
                        });
                    }
                }
                catch (InterruptedException e)
                {
                }
            }
        };

        Button btnCreate = (Button) findViewById(R.id.btnCreate);
        btnCreate.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                create(false);
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

        Button btnCloseIncident = (Button) findViewById(R.id.btnCloseIncident);
        btnCloseIncident.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                closeIncident();
            }
        });

        Button btnFetchLocation = (Button) findViewById(R.id.btnFetchLocation);
        btnFetchLocation.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                setLocation();
            }
        });

        Button btnSetupPersonnel = (Button) findViewById(R.id.btnSetupPersonnel);
        btnSetupPersonnel.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                setupPersonnel();
            }
        });

        Button btnSubmitForms = (Button) findViewById(R.id.btnSubmitForms);
        btnSubmitForms.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                submitForms();
            }
        });

        if (toUpdate == null)
        {
            t.start();
            btnCloseIncident.setVisibility(View.GONE);
            btnSubmitForms.setVisibility(View.GONE);
        }
        else
        {
            btnCreate.setText("Update");
            btnClear.setText("Revert Changes");
            btnCloseIncident.setVisibility(View.VISIBLE);
        }

        clear();
    }

    private String getCurrentDateTime()
    {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");
        return sdf.format(calendar.getTime());
    }

    private void setDate()
    {
        String formattedDate = getCurrentDateTime();
        lblDate.setText(formattedDate);
    }

    protected void onStart()
    {
        googleApiClient.connect();
        super.onStart();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(Action.TYPE_VIEW, // TODO: choose an action type.
                "CreateEditIncident Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.example.kyle.myapplication/http/host/path"));
        AppIndex.AppIndexApi.start(googleApiClient, viewAction);
    }

    protected void onStop()
    {
        googleApiClient.disconnect();
        super.onStop();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(Action.TYPE_VIEW, // TODO: choose an action type.
                "CreateEditIncident Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.example.kyle.myapplication/http/host/path"));
        AppIndex.AppIndexApi.end(googleApiClient, viewAction);
    }

    private void closeIncident()
    {
        //if we are in here then that means the user is in edit mode
        //are you sure you want to close the incident?
        //if yes then update the record
        AlertDialog.Builder builder = new AlertDialog.Builder(CreateEditIncident.this);
        builder.setMessage("Are you sure you want to close this incident? (Once you have closed an incident it cannot be reopened for editing)");
        builder.setNegativeButton("No", null);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                create(true);
            }
        });
        builder.show();
    }

    private void clear()
    {
        if (toUpdate != null)
        {
            lblDate.setText(toUpdate.startTime);
            txtDescription.setText(toUpdate.description);
            txtAddress.setText(toUpdate.address);
            txtCitySTZip.setText(toUpdate.citySTZip);
            txtLatitude.setText(toUpdate.latitude);
            txtLongitude.setText(toUpdate.longitude);
            incidentLinks = new ArrayList<Tbl_IncidentLink>(toUpdate.incidentLinks.size());
            for (Tbl_IncidentLink element : toUpdate.incidentLinks)
            {
                incidentLinks.add(new Tbl_IncidentLink(element));
            }
            submittedForms = new ArrayList<Tbl_SubmittedForms>(toUpdate.submittedForms.size());
            for (Tbl_SubmittedForms element : toUpdate.submittedForms)
            {
                submittedForms.add(new Tbl_SubmittedForms(element));
            }
        }
        else
        {
            txtDescription.setText("");
            txtAddress.setText("");
            txtCitySTZip.setText("");
            txtLatitude.setText("");
            txtLongitude.setText("");
            incidentLinks.clear();
            submittedForms.clear();
        }
    }

    private void create(boolean closeIncident)
    {
        String startDate = lblDate.getText().toString();
        String endDate = closeIncident ? getCurrentDateTime() : "";
        String description = txtDescription.getText().toString();
        String address = txtAddress.getText().toString();
        String citySTZip = txtCitySTZip.getText().toString();
        String latitude = txtLatitude.getText().toString();
        String longitude = txtLongitude.getText().toString();
        Tbl_Incident record = new Tbl_Incident(startDate, endDate, description, address, citySTZip, latitude, longitude, incidentLinks, submittedForms);
        String anyErrors = record.isValidRecord();
        if (anyErrors.isEmpty())
        {
            if (toUpdate != null)
            {
                record.incidentID = toUpdate.incidentID;
                record.sqlMode = Abstract_Table.SQLMode.UPDATE;
                toUpdate = record;
                String successMessage = closeIncident ? "Incident Closed" : "Incident Updated";
                String failMessage = closeIncident ? "Unable to Close Incident" : "Unable to Update Incident";
                DBOperation resultOperation = Tbl_Incident_Manager.current.RecordOperation(this, toUpdate, successMessage, failMessage);
                if (resultOperation != null && resultOperation.Success())
                {
                    OpenScreens.OpenDataListScreen(DataList.Mode.Edit, Abstract_Table_Manager.Table.INCIDENT);
                    finish();
                }
            }
            else
            {
                record.sqlMode = Abstract_Table.SQLMode.INSERT;
                DBOperation resultOperation = Tbl_Incident_Manager.current.RecordOperation(this, record, "Incident Created", "Unable to Create Incident");
                if (resultOperation != null && resultOperation.Success())
                {
                    finish();
                }
            }
        }
        else
        {
            Toast.makeText(this, anyErrors, Toast.LENGTH_LONG).show();
        }
    }

    private void setLocation()
    {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_REQUEST_CODE);
        }
        else
        {
            Location lastLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
            if (lastLocation != null)
            {
                double latitude = lastLocation.getLatitude();
                double longitude = lastLocation.getLongitude();

                String address = "";
                String citySTZip = "";
                Geocoder geoCoder = new Geocoder(this, Locale.getDefault());
                StringBuilder builder = new StringBuilder();
                try
                {
                    List<Address> addressList = geoCoder.getFromLocation(latitude, longitude, 1);
                    int maxLines = addressList.get(0).getMaxAddressLineIndex();
                    for (int i = 0; i < maxLines; i++)
                    {
                        String str = addressList.get(0).getAddressLine(i);
                        builder.append(str);
                        if (i < maxLines - 1)
                        {
                            builder.append("\n");
                        }
                    }
                    String[] totalAddress = builder.toString().split("\n");
                    if (totalAddress.length == 1)
                    {
                        citySTZip = totalAddress[0];
                    }
                    else
                    {
                        address = totalAddress[0];
                        citySTZip = totalAddress[1];
                    }
                }
                catch (Exception e)
                {
                    Toast.makeText(this, "Unable to retrieve your current location.\nMake sure that Location Services is enabled.", Toast.LENGTH_LONG).show();
                }
                txtAddress.setText(address);
                txtCitySTZip.setText(citySTZip);
                txtLatitude.setText(Double.toString(latitude));
                txtLongitude.setText(Double.toString(longitude));
            }
        }
    }

    private void setupPersonnel()
    {
        OpenScreens.OpenIncidentLinksScreen();
    }

    private void submitForms()
    {
        OpenScreens.OpenSubmittedFormsScreen(toUpdate.incidentID);
    }

    @Override
    public void onConnected(Bundle connectionHint)
    {
        if (toUpdate == null)
        {
            setLocation();
        }
    }

    @Override
    public void onConnectionSuspended(int i)
    {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult)
    {

    }
}
