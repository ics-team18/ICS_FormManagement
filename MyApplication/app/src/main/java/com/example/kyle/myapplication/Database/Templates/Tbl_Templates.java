package com.example.kyle.myapplication.Database.Templates;

import com.example.kyle.myapplication.Database.Abstract.Abstract_Table;
import com.example.kyle.myapplication.Database.Role.Tbl_Role;

/**
 * Created by Kyle on 1/31/2016.
 */
public class Tbl_Templates extends Abstract_Table
{
    public long templatesID = -1;
    public long roleID = -1;
    public String description = "";
    public String templateURL = "";
    public Tbl_Role role = null;

    public Tbl_Templates()
    {
    }

    public Tbl_Templates(long templatesID)
    {
        this.templatesID = templatesID;
    }

    public Tbl_Templates(long roleID, String description, String templateURL)
    {
        this.roleID = roleID;
        this.description = description;
        this.templateURL = templateURL;
    }

    public Tbl_Templates(Tbl_Role role, String description, String templateURL)
    {
        this(role.roleID, description, templateURL);
        this.role = role;
    }

    @Override
    public String isValidRecord()
    {
        String anyErrors = "";

        if (roleID < 1)
        {
            anyErrors += "\nRole is a required field";
        }
        if (description.isEmpty())
        {
            anyErrors += "\nDescription is a required field";
        }
        if (templateURL.isEmpty())
        {
            anyErrors += "\nTemplate URL is a required field";
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
        return this.role.title + " - " + this.description;
    }

    @Override
    public String getDataGridPopupMessageValue()
    {
        return "ID: " + Long.toString(this.templatesID) + "\n" +
                "Role: " + this.role.title + "\n" +
                "Description: " + this.description + "\n" +
                "Template URL: " + this.templateURL;
    }
}
