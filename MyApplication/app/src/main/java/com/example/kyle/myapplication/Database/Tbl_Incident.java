package com.example.kyle.myapplication.Database;

/**
 * Created by Kyle on 1/31/2016.
 */
public class Tbl_Incident
{
    public int incidentID = -1;
    public String startTime = "";
    public String endTime = "";
    public String location = "";
    public String description = "";

    public Tbl_Incident()
    {
    }

    public Tbl_Incident(String startTime, String endTime, String location, String description)
    {
        this.startTime = startTime;
        this.endTime = endTime;
        this.location = location;
        this.description = description;
    }
}
