package com.example.kyle.myapplication.Database.SubmittedForms;

import com.example.kyle.myapplication.Database.Abstract.Abstract_Table;
import com.example.kyle.myapplication.Database.Personnel.Tbl_Personnel;

/**
 * Created by Kyle on 1/31/2016.
 */
public class Tbl_SubmittedForms  extends Abstract_Table
{
    public long submittedFormID = -1;
    public long incidentID = -1;
    public long personnelID = -1;
    public String form = "";
    public String description = "";
    public String timeSubmitted = "";
    public Tbl_Personnel personnel = null;

    public Tbl_SubmittedForms()
    {
    }

    public Tbl_SubmittedForms(long submittedFormID)
    {
        this.submittedFormID = submittedFormID;
    }

    public Tbl_SubmittedForms(long incidentID, long personnelID, String form, String description, String timeSubmitted)
    {
        this.incidentID = incidentID;
        this.personnelID = personnelID;
        this.description = description;
        this.form = form;
        this.timeSubmitted = timeSubmitted;
    }

    public Tbl_SubmittedForms(long incidentID, Tbl_Personnel personnel, String form, String description, String timeSubmitted)
    {
        this(incidentID, personnel.personnelID, form, description, timeSubmitted);
        this.personnel = personnel;
    }

    @Override
    public String isValidRecord()
    {
        String anyErrors = "";

        if (personnelID < 1)
        {
            anyErrors += "\nProgrammer error, you have to set the personnelID of the logged in user";
        }
        if (form.isEmpty())
        {
            anyErrors += "\nForm is a required field";
        }
        if (description.isEmpty())
        {
            anyErrors += "\nDescription is a required field";
        }
        if (timeSubmitted.isEmpty())
        {
            anyErrors += "\nProgrammer error, you have to set the time submitted";
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
        return this.personnel.getDataGridDisplayValue() + " - " + this.description;
    }

    @Override
    public String getDataGridPopupMessageValue()
    {
        // this table won't be shown in the Manage Data screen
        // so we don't need to return an actual value
        return "";
    }
}
