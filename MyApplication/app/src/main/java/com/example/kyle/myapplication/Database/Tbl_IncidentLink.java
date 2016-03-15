package com.example.kyle.myapplication.Database;

import java.util.List;

/**
 * Created by Kyle on 3/8/2016.
 */
public class Tbl_IncidentLink extends Abstract_Table
{
    public long incidentLinkID = -1;
    public long incidentID = -1;
    public long personnelID = -1;
    public long roleID = -1;
    public int ranking = -1;
    public Tbl_Personnel personnel;
    public Tbl_Role role;

    public Tbl_IncidentLink()
    {
    }

    public Tbl_IncidentLink(long incidentID, long personnelID, long roleID, int ranking)
    {
        this.incidentID = incidentID;
        this.personnelID = personnelID;
        this.roleID = roleID;
        this.ranking = ranking;
    }

    public Tbl_IncidentLink(long incidentID, Tbl_Personnel personnel, Tbl_Role role, int ranking)
    {
        this.incidentID = incidentID;
        this.personnel = personnel;
        this.personnelID = personnel.personnelID;
        this.role = role;
        this.roleID = role.roleID;
        this.ranking = ranking;
    }

    public Tbl_IncidentLink(Tbl_IncidentLink toClone)
    {
        this(toClone.incidentID, toClone.personnel, toClone.role, toClone.ranking);
        this.incidentLinkID = toClone.incidentLinkID;
    }

    public String customIsValidRecord(int selectedListPosition, Tbl_IncidentLink toAdd, List<Tbl_IncidentLink> incidentLinks)
    {
        String anyErrors = isValidRecord();

        for (int i = 0; i < incidentLinks.size(); i++)
        {
            if (i == selectedListPosition)
            {
                continue;
            }
            Tbl_IncidentLink incidentLink = incidentLinks.get(i);
            if (incidentLink.sqlMode != Abstract_Table.SQLMode.DELETE && incidentLink.personnelID == toAdd.personnel.personnelID)
            {
                //we already found one occurrence, there shouldn't be any more
                anyErrors += "\n" + toAdd.personnel.getDataGridDisplayValue() + " was already assigned a role";
                break;
            }
        }

        if (!anyErrors.isEmpty())
        {
            anyErrors = "Please fix the following errors:" + anyErrors;
        }
        return anyErrors;
    }

    @Override
    public String isValidRecord()
    {
        String anyErrors = "";

        if (incidentID < 1)
        {
            anyErrors += "\nIncident ID is a required field";
        }
        if (personnel != null && personnel.personnelID == -1)
        {
            anyErrors += "\nPersonnel is a required field";
        }
        if (role != null && role.roleID == -1)
        {
            anyErrors += "\nRole is a required field";
        }
        if (ranking < 1)
        {
            anyErrors += "\nRanking is a required field";
        }

        return anyErrors;
    }

    @Override
    public String getDataGridDisplayValue()
    {
        return personnel.getDataGridDisplayValue() + " - " + Integer.toString(this.ranking) + ": " + role.title;
    }

    @Override
    public String getDataGridPopupMessageValue()
    {
        return "ID: " + Long.toString(this.incidentLinkID) + "\n" +
                "Personnel: " + personnel.getDataGridDisplayValue() + "\n" +
                "Role: " + role.getDataGridDisplayValue() + "\n" +
                "Ranking: " + Integer.toString(this.ranking);
    }
}