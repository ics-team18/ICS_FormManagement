package com.example.kyle.myapplication.Database;

/**
 * Created by Kyle on 1/31/2016.
 */
public class Tbl_SubmittedForms
{
    public int formID = -1;
    public int incidentID = -1;
    public int personnelID = -1;
    public String form = "";
    public String timeSubmitted = "";


    public Tbl_SubmittedForms()
    {
    }

    public Tbl_SubmittedForms(int incidentID, int personnelID, String form, String timeSubmitted)
    {
        this.incidentID = incidentID;
        this.personnelID = personnelID;
        this.form = form;
        this.timeSubmitted = timeSubmitted;
    }
}
