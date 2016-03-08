package com.example.kyle.myapplication.Database;

/**
 * Created by Kyle on 3/8/2016.
 */
public class Tbl_IncidentLink extends Abstract_Table
{
    public int incidentLinkID = -1;
    public int incidentID = -1;
    public int personnelID = -1;
    public int roleID = -1;
    public int ranking = -1;
    public Tbl_Personnel personnel;
    public Tbl_Role role;

    public Tbl_IncidentLink()
    {
    }

    public Tbl_IncidentLink(int incidentID, int personnelID, int roleID, int ranking)
    {
        this.incidentID = incidentID;
        this.personnelID = personnelID;
        this.roleID = roleID;
        this.ranking = ranking;
    }

    @Override
    public String isValidRecord()
    {
        String anyErrors = "";

        if (incidentID < 1)
        {
            anyErrors += "\nIncident ID is a required field";
        }
        if (personnelID < 1)
        {
            anyErrors += "\nPersonnel ID is a required field";
        }
        if (roleID < 1)
        {
            anyErrors += "\nRole ID is a required field";
        }
        if (ranking < 1)
        {
            anyErrors += "\nRanking is a required field";
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
        return Integer.toString(this.incidentLinkID);
    }

    @Override
    public String getDataGridPopupMessageValue()
    {
        return "ID: " + Integer.toString(this.incidentLinkID) + "\n" +
                "Personnel: " + personnel.getDataGridDisplayValue() + "\n" +
                "Role: " + role.getDataGridDisplayValue() + "\n" +
                "Ranking: " + Integer.toString(this.ranking);
    }
}