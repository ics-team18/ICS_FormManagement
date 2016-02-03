package com.example.kyle.myapplication.Database;

import android.content.ContentValues;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kyle on 1/31/2016.
 */
public class Tbl_Personnel_Manager extends Abstract_Table_Manager<Tbl_Personnel>
{
    public static Tbl_Personnel_Manager current = new Tbl_Personnel_Manager();

    public static enum Attributes
    {
        PERSONNELID,
        FIRSTNAME,
        LASTNAME,
        EMAIL,
        PASSWORD,
        MOBILEPHONE,
        TITLE,
    }

    @Override
    public String GetPrimaryKey()
    {
        return Attributes.PERSONNELID.name();
    }

    @Override
    public String GetTableName()
    {
        return "Tbl_Personnel";
    }

    @Override
    public String GetCreateScript()
    {
        return Attributes.PERSONNELID.name() + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                Attributes.FIRSTNAME.name() + " TEXT, " +
                Attributes.LASTNAME.name() + " TEXT, " +
                Attributes.EMAIL.name() + " TEXT, " +
                Attributes.PASSWORD.name() + " TEXT, " +
                Attributes.MOBILEPHONE.name() + " TEXT, " +
                Attributes.TITLE.name() + " TEXT";
    }

    @Override
    public String GetSelectScript(Tbl_Personnel searchCritera)
    {
        String whereClause = "";
        if (searchCritera.personnelID > -1)
        {
            whereClause += Attributes.PERSONNELID.name() + " = " + Integer.toString(searchCritera.personnelID);
        }
        if (!searchCritera.firstName.isEmpty())
        {
            whereClause += Attributes.FIRSTNAME.name() + " = " + searchCritera.firstName;
        }
        if (!searchCritera.lastName.isEmpty())
        {
            whereClause += Attributes.LASTNAME.name() + " = " + searchCritera.lastName;
        }
        if (!searchCritera.email.isEmpty())
        {
            whereClause += Attributes.EMAIL.name() + " = " + searchCritera.email;
        }
        if (!searchCritera.password.isEmpty())
        {
            whereClause += Attributes.PASSWORD.name() + " = " + searchCritera.password;
        }
        if (!searchCritera.mobilePhone.isEmpty())
        {
            whereClause += Attributes.MOBILEPHONE.name() + " = " + searchCritera.mobilePhone;
        }
        if (!searchCritera.title.isEmpty())
        {
            whereClause += Attributes.TITLE.name() + " = " + searchCritera.title;
        }
        return whereClause;
    }

    @Override
    public ContentValues GetContentValues(Tbl_Personnel record, boolean isUpdate)
    {
        ContentValues values = new ContentValues();
        values.put(Attributes.FIRSTNAME.name(), record.firstName);
        values.put(Attributes.LASTNAME.name(), record.lastName);
        values.put(Attributes.EMAIL.name(), record.email);
        values.put(Attributes.PASSWORD.name(), record.password);
        values.put(Attributes.MOBILEPHONE.name(), record.mobilePhone);
        values.put(Attributes.TITLE.name(), record.title);
        if (isUpdate)
        {
            values.put(Attributes.PERSONNELID.name(), record.personnelID);
        }
        return values;
    }

    @Override
    public String GetPrimaryKeyValue(Tbl_Personnel record)
    {
        return Integer.toString(record.personnelID);
    }

    @Override
    public List<Tbl_Personnel> GetList(Cursor cursor)
    {
        List<Tbl_Personnel> resultList = new ArrayList<Tbl_Personnel>();
        while (cursor.moveToNext())
        {
            Tbl_Personnel record = new Tbl_Personnel();
            record.personnelID = cursor.getInt(Attributes.PERSONNELID.ordinal());
            record.firstName = cursor.getString(Attributes.FIRSTNAME.ordinal());
            record.lastName = cursor.getString(Attributes.LASTNAME.ordinal());
            record.email = cursor.getString(Attributes.EMAIL.ordinal());
            record.password = cursor.getString(Attributes.PASSWORD.ordinal());
            record.mobilePhone = cursor.getString(Attributes.MOBILEPHONE.ordinal());
            record.title = cursor.getString(Attributes.TITLE.ordinal());
            resultList.add(record);
        }
        return resultList;
    }
}
