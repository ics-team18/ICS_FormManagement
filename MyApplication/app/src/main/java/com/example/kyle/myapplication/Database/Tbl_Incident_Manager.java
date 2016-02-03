package com.example.kyle.myapplication.Database;

import android.content.ContentValues;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kyle on 1/31/2016.
 */
public class Tbl_Incident_Manager extends Abstract_Table_Manager<Tbl_Incident>
{
    public static Tbl_Incident_Manager current = new Tbl_Incident_Manager();

    public static enum Attributes
    {
        INCIDENTID,
        STARTTIME,
        ENDTIME,
        LOCATION,
        DESCRIPTION,
    }

    @Override
    public String GetPrimaryKey()
    {
        return Attributes.INCIDENTID.name();
    }

    @Override
    public String GetTableName()
    {
        return "Tbl_Incident";
    }

    @Override
    public String GetCreateScript()
    {
        return Attributes.INCIDENTID.name() + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                Attributes.STARTTIME.name() + " TEXT, " +
                Attributes.ENDTIME.name() + " TEXT, " +
                Attributes.LOCATION.name() + " TEXT, " +
                Attributes.DESCRIPTION.name() + " TEXT";
    }

    @Override
    public String GetSelectScript(Tbl_Incident searchCritera)
    {
        String whereClause = "";
        if (searchCritera.incidentID > -1)
        {
            whereClause += Attributes.INCIDENTID.name() + " = " + Integer.toString(searchCritera.incidentID);
        }
        if (!searchCritera.startTime.isEmpty())
        {
            whereClause += Attributes.STARTTIME.name() + " = " + searchCritera.startTime;
        }
        if (!searchCritera.endTime.isEmpty())
        {
            whereClause += Attributes.ENDTIME.name() + " = " + searchCritera.endTime;
        }
        if (!searchCritera.location.isEmpty())
        {
            whereClause += Attributes.LOCATION.name() + " = " + searchCritera.location;
        }
        if (!searchCritera.description.isEmpty())
        {
            whereClause += Attributes.DESCRIPTION.name() + " = " + searchCritera.description;
        }
        return whereClause;
    }

    @Override
    public ContentValues GetContentValues(Tbl_Incident record, boolean isUpdate)
    {
        ContentValues values = new ContentValues();
        values.put(Attributes.STARTTIME.name(), record.startTime);
        values.put(Attributes.ENDTIME.name(), record.endTime);
        values.put(Attributes.LOCATION.name(), record.location);
        values.put(Attributes.DESCRIPTION.name(), record.description);
        if (isUpdate)
        {
            values.put(Attributes.INCIDENTID.name(), record.incidentID);
        }
        return values;
    }

    @Override
    public String GetPrimaryKeyValue(Tbl_Incident record)
    {
        return Integer.toString(record.incidentID);
    }

    @Override
    public List<Tbl_Incident> GetList(Cursor cursor)
    {
        List<Tbl_Incident> resultList = new ArrayList<Tbl_Incident>();
        while (cursor.moveToNext())
        {
            Tbl_Incident record = new Tbl_Incident();
            record.incidentID = cursor.getInt(Attributes.INCIDENTID.ordinal());
            record.startTime = cursor.getString(Attributes.STARTTIME.ordinal());
            record.endTime = cursor.getString(Attributes.ENDTIME.ordinal());
            record.location = cursor.getString(Attributes.LOCATION.ordinal());
            record.description = cursor.getString(Attributes.DESCRIPTION.ordinal());
            resultList.add(record);
        }
        return resultList;
    }
}
