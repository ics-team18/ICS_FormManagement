package com.example.kyle.myapplication.Screens;

import android.Manifest;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kyle.myapplication.Database.Database_Manager;
import com.example.kyle.myapplication.Database.Tbl_Incident;
import com.example.kyle.myapplication.Database.Tbl_Incident_Manager;
import com.example.kyle.myapplication.R;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class CreateIncident extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener
{
    private Database_Manager db;
    private TextView lblIncidentID, lblDate;
    private EditText txtDescription, txtAddress, txtCitySTZip, txtLatitude, txtLongitude;
    private static final int PERMISSION_REQUEST_CODE = 1;
    private GoogleApiClient googleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_incident);

        db = new Database_Manager(this);
        lblIncidentID = (TextView) findViewById(R.id.lblIncidentID);
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
                                setIDAndDate();
                            }
                        });
                    }
                }
                catch (InterruptedException e)
                {
                }
            }
        };

        t.start();

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

        Button btnFetchLocation = (Button) findViewById(R.id.btnFetchLocation);
        btnFetchLocation.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                setLocation();
            }
        });
    }

    private void setIDAndDate()
    {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");
        String formattedDate = sdf.format(calendar.getTime());
        lblDate.setText(formattedDate);
        int nextIncidentID = Tbl_Incident_Manager.current.GetNextID(db.getReadableDatabase());
        lblIncidentID.setText(Integer.toString(nextIncidentID));
    }

    protected void onStart()
    {
        googleApiClient.connect();
        super.onStart();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(Action.TYPE_VIEW, // TODO: choose an action type.
                "CreateIncident Page", // TODO: Define a title for the content shown.
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
                "CreateIncident Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.example.kyle.myapplication/http/host/path"));
        AppIndex.AppIndexApi.end(googleApiClient, viewAction);
    }

    private void clear()
    {
        txtDescription.setText("");
        txtAddress.setText("");
        txtCitySTZip.setText("");
        txtLatitude.setText("");
        txtLongitude.setText("");
    }

    private void create()
    {
        int incidentID = Integer.parseInt(lblIncidentID.getText().toString());
        String startDate = lblDate.getText().toString();
        String endDate = "";
        String description = txtDescription.getText().toString();
        String address = txtAddress.getText().toString();
        String citySTZip = txtCitySTZip.getText().toString();
        double latitude = 0.0;
        double longitude = 0.0;
        String latitudeStr = txtLatitude.getText().toString();
        String longitudeStr = txtLongitude.getText().toString();
        if (!latitudeStr.isEmpty())
        {
            latitude = Double.parseDouble(latitudeStr);
        }
        if (!longitudeStr.isEmpty())
        {
            longitude = Double.parseDouble(longitudeStr);
        }
        Tbl_Incident record = new Tbl_Incident(incidentID, startDate, endDate, description, address, citySTZip, latitude, longitude);
        String anyErrors = record.isValidRecord();
        if (anyErrors.isEmpty())
        {
            Tbl_Incident_Manager.current.Insert(db.getWritableDatabase(), record);
            Toast.makeText(this, "Incident Created", Toast.LENGTH_LONG).show();
            //instead of showing a message go to the manage incident screen
            clear();
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
                    address = totalAddress[0];
                    citySTZip = totalAddress[1];
                }
                catch (Exception e)
                {
                }
                txtAddress.setText(address);
                txtCitySTZip.setText(citySTZip);
                txtLatitude.setText(Double.toString(latitude));
                txtLongitude.setText(Double.toString(longitude));
            }
        }
    }

    @Override
    public void onConnected(Bundle connectionHint)
    {
        setLocation();
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
