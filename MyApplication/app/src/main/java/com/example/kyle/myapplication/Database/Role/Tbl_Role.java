package com.example.kyle.myapplication.Database.Role;

import com.example.kyle.myapplication.Database.Abstract.Abstract_Table;

/**
 * Created by Kyle on 1/31/2016.
 */
public class Tbl_Role extends Abstract_Table
{
    public long roleID = -1;
    public String title = "";

    public Tbl_Role()
    {
    }

    public Tbl_Role(long roleID)
    {
        this.roleID = roleID;
    }

    public Tbl_Role( String title)
    {
        this.title = title;
    }

    @Override
    public String isValidRecord()
    {
        String anyErrors = "";

        if (title.isEmpty())
        {
            anyErrors += "\nTitle is a required field";
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
        return this.roleID + " - " + this.title;
    }

    @Override
    public String toString()
    {
        return this.title;
    }
    @Override
    public String getDataGridPopupMessageValue()
    {
        return "ID: " + Long.toString(this.roleID) + "\n" +
                "Title: " + this.title;
    }
}
