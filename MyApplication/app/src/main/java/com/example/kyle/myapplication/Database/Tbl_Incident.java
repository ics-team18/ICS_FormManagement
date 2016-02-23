package com.example.kyle.myapplication.Database;

/**
 * Created by Kyle on 1/31/2016.
 */
public class Tbl_Incident extends Abstract_Table
{
    public int incidentID = -1;
    public String startTime = "";
    public String endTime = "";
    public String description = "";
    public String address = "";
    public String citySTZip = "";
    public double latitude = 0.0;
    public double longitude = 0.0;

    public Tbl_Incident()
    {
    }

    public Tbl_Incident(String startTime, String endTime, String description, String address, String citySTZip, double latitude, double longitude)
    {
        this.startTime = startTime;
        this.endTime = endTime;
        this.description = description;
        this.address = address;
        this.citySTZip = citySTZip;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    @Override
    public String isValidRecord()
    {
        String anyErrors = "";

        if (description.isEmpty())
        {
            anyErrors += "\nDescription is a required field";
        }
        if (address.isEmpty())
        {
            anyErrors += "\nAddress is a required field";
        }
        if (citySTZip.isEmpty())
        {
            anyErrors += "\nCity, ST Zip is a required field";
        }
        if (latitude == 0.0)
        {
            anyErrors += "\nLatitude is a required field";
        }
        if (longitude == 0.0)
        {
            anyErrors += "\nLongitude is a required field";
        }

        if (!anyErrors.isEmpty())
        {
            anyErrors = "Please fix the following errors:" + anyErrors;
        }

        return anyErrors;
    }

    @Override
    public String getDataGridDisplayValue()
    {
        return this.incidentID + " - " + this.description;
    }

    @Override
    public String getDataGridPopupMessageValue()
    {
        return "ID: " + Integer.toString(this.incidentID) + "\n" +
                "Start Time: " + this.startTime + "\n" +
                "End Time: " + this.endTime + "\n" +
                "Description: " + this.description + "\n" +
                "Address: " + this.address + "\n" +
                "City, ST Zip: " + this.citySTZip + "\n" +
                "Latitude: " + this.latitude + "\n" +
                "Longitude: " + this.longitude;
    }
}