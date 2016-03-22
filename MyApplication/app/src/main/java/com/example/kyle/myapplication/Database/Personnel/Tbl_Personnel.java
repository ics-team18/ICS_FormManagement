package com.example.kyle.myapplication.Database.Personnel;

import com.example.kyle.myapplication.Database.Abstract.Abstract_Table;

/**
 * Created by Kyle on 1/31/2016.
 */
public class Tbl_Personnel extends Abstract_Table
{
    public long personnelID = -1;
    public String firstName = "";
    public String lastName = "";
    public String email = "";
    public String password = "";
    public String mobilePhone = "";
    public String title = "";
    public Boolean isSupervisor = null;

    public Tbl_Personnel()
    {
    }

    public Tbl_Personnel(long personnelID)
    {
        this.personnelID = personnelID;
    }

    public Tbl_Personnel(String firstName, String lastName, String email, String password, String mobilePhone, String title, Boolean isSupervisor)
    {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.mobilePhone = mobilePhone;
        this.title = title;
        this.isSupervisor = isSupervisor;
    }

    @Override
    public String isValidRecord()
    {
        String anyErrors = "";

        if (firstName.isEmpty())
        {
            anyErrors += "\nFirst Name is a required field";
        }
        if (lastName.isEmpty())
        {
            anyErrors += "\nLast Name is a required field";
        }
        if (email.isEmpty())
        {
            anyErrors += "\nE-mail is a required field";
        }
        if (password.isEmpty())
        {
            anyErrors += "\nPassword is a required field";
        }
        if (mobilePhone.isEmpty())
        {
            anyErrors += "\nMobile Phone is a required field";
        }
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
        return this.lastName + ", " + this.firstName;
    }

    @Override
    public String toString()
    {
        String value = "";
        if (!this.lastName.isEmpty() && !this.firstName.isEmpty())
        {
            value = getDataGridDisplayValue();
        }
        return value;
    }
    @Override
    public String getDataGridPopupMessageValue()
    {
        return "ID: " + Long.toString(this.personnelID) + "\n" +
                "First Name: " + this.firstName + "\n" +
                "Last Name: " + this.lastName + "\n" +
                "Email: " + this.email + "\n" +
                "Password: " + this.password + "\n" +
                "Mobile Phone: " + this.mobilePhone + "\n" +
                "Title: " + this.title + "\n" +
                "Supervisor?: " + (this.isSupervisor ? "Yes":"No");
    }
}
