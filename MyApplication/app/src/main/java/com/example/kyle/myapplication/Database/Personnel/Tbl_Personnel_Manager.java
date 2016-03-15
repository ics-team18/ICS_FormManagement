package com.example.kyle.myapplication.Database.Personnel;

import android.content.ContentValues;
import android.database.Cursor;

import com.example.kyle.myapplication.Database.Abstract.Abstract_Table_Manager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kyle on 1/31/2016.
 */
public class Tbl_Personnel_Manager extends Abstract_Table_Manager<Tbl_Personnel>
{
    public static Tbl_Personnel_Manager current = new Tbl_Personnel_Manager();

    public enum Attributes
    {
        PERSONNELID,
        FIRSTNAME,
        LASTNAME,
        EMAIL,
        PASSWORD,
        MOBILEPHONE,
        TITLE,
        ISSUPERVISOR,
    }

    @Override
    public String GetPrimaryKey()
    {
        return Attributes.PERSONNELID.name();
    }

    @Override
    public String GetTableName()
    {
        return Tbl_Personnel.class.getSimpleName();
    }

    @Override
    public String GetCreateScript()
    {
        return Attributes.PERSONNELID.name() + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                Attributes.FIRSTNAME.name() + " TEXT COLLATE NOCASE, " +
                Attributes.LASTNAME.name() + " TEXT COLLATE NOCASE, " +
                Attributes.EMAIL.name() + " TEXT COLLATE NOCASE, " +
                Attributes.PASSWORD.name() + " TEXT, " +
                Attributes.MOBILEPHONE.name() + " TEXT COLLATE NOCASE, " +
                Attributes.TITLE.name() + " TEXT COLLATE NOCASE," +
                Attributes.ISSUPERVISOR.name() + " INTEGER";
    }

    @Override
    public String GetSelectScript(Tbl_Personnel searchCritera)
    {
        String whereClause = "";
        if (searchCritera.personnelID > -1)
        {
            whereClause += " AND " + Attributes.PERSONNELID.name() + " = '" + Long.toString(searchCritera.personnelID) + "'";
        }
        if (!searchCritera.firstName.isEmpty())
        {
            whereClause += " AND " + Attributes.FIRSTNAME.name() + " = '" + searchCritera.firstName + "'";
        }
        if (!searchCritera.lastName.isEmpty())
        {
            whereClause += " AND " + Attributes.LASTNAME.name() + " = '" + searchCritera.lastName + "'";
        }
        if (!searchCritera.email.isEmpty())
        {
            whereClause += " AND " + Attributes.EMAIL.name() + " = '" + searchCritera.email + "'";
        }
        if (!searchCritera.password.isEmpty())
        {
            whereClause += " AND " + Attributes.PASSWORD.name() + " = '" + searchCritera.password + "'";
        }
        if (!searchCritera.mobilePhone.isEmpty())
        {
            whereClause += " AND " + Attributes.MOBILEPHONE.name() + " = '" + searchCritera.mobilePhone + "'";
        }
        if (!searchCritera.title.isEmpty())
        {
            whereClause += " AND " + Attributes.TITLE.name() + " = '" + searchCritera.title + "'";
        }
        if (searchCritera.isSupervisor != null)
        {
            whereClause += " AND " + Attributes.ISSUPERVISOR.name() + " = " + Integer.toString(searchCritera.isSupervisor ? 1 : 0) + "";
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
        values.put(Attributes.ISSUPERVISOR.name(), record.isSupervisor);
        if (isUpdate)
        {
            values.put(Attributes.PERSONNELID.name(), record.personnelID);
        }
        return values;
    }

    @Override
    public String GetPrimaryKeyValue(Tbl_Personnel record)
    {
        return Long.toString(record.personnelID);
    }

    @Override
    public List<Tbl_Personnel> GetList(Cursor cursor)
    {
        List<Tbl_Personnel> resultList = new ArrayList<Tbl_Personnel>();
        while (cursor.moveToNext())
        {
            Tbl_Personnel record = new Tbl_Personnel();
            record.personnelID = cursor.getLong(Attributes.PERSONNELID.ordinal());
            record.firstName = cursor.getString(Attributes.FIRSTNAME.ordinal());
            record.lastName = cursor.getString(Attributes.LASTNAME.ordinal());
            record.email = cursor.getString(Attributes.EMAIL.ordinal());
            record.password = cursor.getString(Attributes.PASSWORD.ordinal());
            record.mobilePhone = cursor.getString(Attributes.MOBILEPHONE.ordinal());
            record.title = cursor.getString(Attributes.TITLE.ordinal());
            record.isSupervisor = cursor.getInt(Attributes.ISSUPERVISOR.ordinal()) != 0;
            resultList.add(record);
        }
        return resultList;
    }
}
