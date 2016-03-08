package com.example.kyle.myapplication.Database;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

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
    public List<Tbl_IncidentLink> incidentLinks;

    public Tbl_Incident()
    {
    }

    public Tbl_Incident(String startTime, String endTime, String description, String address, String citySTZip, double latitude, double longitude, List<Tbl_IncidentLink> incidentLinks)
    {
        this.startTime = startTime;
        this.endTime = endTime;
        this.description = description;
        this.address = address;
        this.citySTZip = citySTZip;
        this.latitude = latitude;
        this.longitude = longitude;
        this.incidentLinks = incidentLinks;
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
        if (incidentLinks == null || incidentLinks.size() == 0)
        {
            anyErrors += "\nYou must set up personnel for the incident";
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
        String personnelAndRoles = "";
        List<Tbl_IncidentLink> sortedIncidentLinks = new ArrayList<Tbl_IncidentLink>(incidentLinks);
        //sort the list by ranking
        Collections.sort(sortedIncidentLinks, new Comparator<Tbl_IncidentLink>()
        {
            @Override
            public int compare(Tbl_IncidentLink a, Tbl_IncidentLink b)
            {
                return a.ranking - b.ranking;
            }
        });

        int listSize = sortedIncidentLinks.size();
        for (int i = 0; i < listSize; i++)
        {
            Tbl_IncidentLink link = sortedIncidentLinks.get(i);
            personnelAndRoles += Integer.toString(link.ranking) + " - " + link.personnel.getDataGridDisplayValue() + ": " + link.role.title;
            if (i < listSize - 1)
            {
                personnelAndRoles += "\n";
            }
        }
        return "ID: " + Integer.toString(this.incidentID) + "\n" +
                "Start Time: " + this.startTime + "\n" +
                "End Time: " + this.endTime + "\n" +
                "Description: " + this.description + "\n" +
                "Address: " + this.address + "\n" +
                "City, ST Zip: " + this.citySTZip + "\n" +
                "Latitude: " + this.latitude + "\n" +
                "Longitude: " + this.longitude + "\n" +
                "Personnel:\n" + personnelAndRoles;
    }
}