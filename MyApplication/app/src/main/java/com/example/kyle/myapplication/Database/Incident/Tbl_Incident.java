package com.example.kyle.myapplication.Database.Incident;

import com.example.kyle.myapplication.Database.Abstract.Abstract_Table;
import com.example.kyle.myapplication.Database.IncidentLink.Tbl_IncidentLink;
import com.example.kyle.myapplication.Database.SubmittedForms.Tbl_SubmittedForms;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by Kyle on 1/31/2016.
 */
public class Tbl_Incident extends Abstract_Table
{
    public long incidentID = -1;
    public String startTime = "";
    public String endTime = "";
    public String description = "";
    public String address = "";
    public String citySTZip = "";
    public String latitude = "";
    public String longitude = "";
    public List<Tbl_IncidentLink> incidentLinks;
    public List<Tbl_SubmittedForms> submittedForms;

    public Tbl_Incident()
    {
    }

    public Tbl_Incident(long incidentID)
    {
        this.incidentID = incidentID;
    }

    public Tbl_Incident(String startTime, String endTime, String description, String address, String citySTZip, String latitude, String longitude, List<Tbl_IncidentLink> incidentLinks, List<Tbl_SubmittedForms> submittedForms)
    {
        this.startTime = startTime;
        this.endTime = endTime;
        this.description = description;
        this.address = address;
        this.citySTZip = citySTZip;
        this.latitude = latitude;
        this.longitude = longitude;
        this.incidentLinks = incidentLinks;
        this.submittedForms = submittedForms;
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
        if (latitude.isEmpty())
        {
            anyErrors += "\nLatitude is a required field";
        }
        if (longitude.isEmpty())
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

        String result = "ID: " + Long.toString(this.incidentID) + "\n" +
                "Start Time: " + this.startTime + "\n" +
                "End Time: " + this.endTime + "\n" +
                "Description: " + this.description + "\n" +
                "Address: " + this.address + "\n" +
                "City, ST Zip: " + this.citySTZip + "\n" +
                "Latitude: " + this.latitude + "\n" +
                "Longitude: " + this.longitude + "\n" +
                "Personnel:\n" + personnelAndRoles;

        if (submittedForms != null && submittedForms.size() > 0)
        {
            String personnelAndForms = "";
            List<Tbl_SubmittedForms> sortedForms = new ArrayList<Tbl_SubmittedForms>(submittedForms);

            Collections.sort(sortedForms, new Comparator<Tbl_SubmittedForms>()
            {
                @Override
                public int compare(Tbl_SubmittedForms a, Tbl_SubmittedForms b)
                {
                    return a.description.compareTo(b.description);
                }
            });

            listSize = sortedForms.size();
            for (int i = 0; i < listSize; i++)
            {
                Tbl_SubmittedForms form = sortedForms.get(i);
                personnelAndForms += form.personnel.getDataGridDisplayValue() + " - " + form.description + ": " + form.fileName;
                if (i < listSize - 1)
                {
                    personnelAndForms += "\n";
                }
            }
            result += "\nSubmitted Forms:\n" + personnelAndForms;
        }
        return result;
    }
}