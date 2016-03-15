package com.example.kyle.myapplication.Database.SubmittedForms;

/**
 * Created by Kyle on 1/31/2016.
 */
public class Tbl_SubmittedForms
{
    public long formID = -1;
    public long incidentID = -1;
    public long personnelID = -1;
    public String form = "";
    public String timeSubmitted = "";


    public Tbl_SubmittedForms()
    {
    }

    public Tbl_SubmittedForms(long incidentID, long personnelID, String form, String timeSubmitted)
    {
        this.incidentID = incidentID;
        this.personnelID = personnelID;
        this.form = form;
        this.timeSubmitted = timeSubmitted;
    }
}
