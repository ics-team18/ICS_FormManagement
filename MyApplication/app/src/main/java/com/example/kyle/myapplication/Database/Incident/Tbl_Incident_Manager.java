package com.example.kyle.myapplication.Database.Incident;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.kyle.myapplication.Database.Abstract.Abstract_Table_Manager;
import com.example.kyle.myapplication.Database.IncidentLink.Tbl_IncidentLink;
import com.example.kyle.myapplication.Database.IncidentLink.Tbl_IncidentLink_Manager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kyle on 1/31/2016.
 */
public class Tbl_Incident_Manager extends Abstract_Table_Manager<Tbl_Incident>
{
    public static Tbl_Incident_Manager current = new Tbl_Incident_Manager();

    public enum Attributes
    {
        INCIDENTID,
        STARTTIME,
        ENDTIME,
        DESCRIPTION,
        ADDRESS,
        CITYSTZIP,
        LATITUDE,
        LONGITUDE,
    }

    @Override
    public List<Tbl_Incident> Select(SQLiteDatabase db, Tbl_Incident searchCriteria)
    {
        List<Tbl_Incident> incidentList = super.Select(db, searchCriteria);
        for (int i = 0; i < incidentList.size(); i++)
        {
            Tbl_Incident incident = incidentList.get(i);
            Tbl_IncidentLink linkSearchCriteria = new Tbl_IncidentLink();
            linkSearchCriteria.incidentID = incident.incidentID;
            incident.incidentLinks = Tbl_IncidentLink_Manager.current.Select(db, linkSearchCriteria);
        }
        return incidentList;
    }

    @Override
    public long Insert(SQLiteDatabase db, Tbl_Incident toInsert)
    {
        long resultID = super.Insert(db, toInsert);
        if (resultID != -1)
        {
            for (int i = 0; i < toInsert.incidentLinks.size(); i++)
            {
                Tbl_IncidentLink incidentLink = toInsert.incidentLinks.get(i);
                incidentLink.incidentID = resultID;
                long id = Tbl_IncidentLink_Manager.current.Insert(db, incidentLink);
                if (id == -1)
                {
                    break;
                }
            }
        }
        return resultID;
    }

    @Override
    public boolean Update(SQLiteDatabase db, Tbl_Incident toUpdate)
    {
        super.Update(db, toUpdate);
        for (int i = 0; i < toUpdate.incidentLinks.size(); i++)
        {
            Tbl_IncidentLink incidentLink = toUpdate.incidentLinks.get(i);
            switch (incidentLink.sqlMode)
            {
                case INSERT:
                    incidentLink.incidentID = toUpdate.incidentID;
                    Tbl_IncidentLink_Manager.current.Insert(db, incidentLink);
                    break;
                case DELETE:
                    Tbl_IncidentLink_Manager.current.Delete(db, incidentLink);
                    break;
                case UPDATE:
                    Tbl_IncidentLink_Manager.current.Update(db, incidentLink);
                    break;
            }
        }
        return true;
    }

    @Override
    public void Delete(SQLiteDatabase db, Tbl_Incident toDelete)
    {
        super.Delete(db, toDelete);
        for (int i = 0; i < toDelete.incidentLinks.size(); i++)
        {
            Tbl_IncidentLink_Manager.current.Delete(db, toDelete.incidentLinks.get(i));
        }
    }

    @Override
    public String GetPrimaryKey()
    {
        return Attributes.INCIDENTID.name();
    }

    @Override
    public String GetTableName()
    {
        return Tbl_Incident.class.getSimpleName();
    }

    @Override
    public String GetCreateScript()
    {
        return Attributes.INCIDENTID.name() + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                Attributes.STARTTIME.name() + " TEXT, " +
                Attributes.ENDTIME.name() + " TEXT, " +
                Attributes.DESCRIPTION.name() + " TEXT, " +
                Attributes.ADDRESS.name() + " TEXT, " +
                Attributes.CITYSTZIP.name() + " TEXT, " +
                Attributes.LATITUDE.name() + " REAL, " +
                Attributes.LONGITUDE.name() + " REAL";
    }

    @Override
    public String GetSelectScript(Tbl_Incident searchCritera)
    {
        String whereClause = "";
        if (searchCritera.incidentID > -1)
        {
            whereClause += Attributes.INCIDENTID.name() + " = " + Long.toString(searchCritera.incidentID);
        }
        if (!searchCritera.startTime.isEmpty())
        {
            whereClause += Attributes.STARTTIME.name() + " = " + searchCritera.startTime;
        }
        if (!searchCritera.endTime.isEmpty())
        {
            whereClause += Attributes.ENDTIME.name() + " = " + searchCritera.endTime;
        }
        if (!searchCritera.description.isEmpty())
        {
            whereClause += Attributes.DESCRIPTION.name() + " = " + searchCritera.description;
        }
        if (!searchCritera.address.isEmpty())
        {
            whereClause += Attributes.ADDRESS.name() + " = " + searchCritera.address;
        }
        if (!searchCritera.citySTZip.isEmpty())
        {
            whereClause += Attributes.CITYSTZIP.name() + " = " + searchCritera.citySTZip;
        }
        if (searchCritera.latitude != 0.0)
        {
            whereClause += Attributes.LATITUDE.name() + " = " + Double.toString(searchCritera.latitude);
        }
        if (searchCritera.longitude != 0.0)
        {
            whereClause += Attributes.LONGITUDE.name() + " = " + Double.toString(searchCritera.longitude);
        }
        return whereClause;
    }

    @Override
    public ContentValues GetContentValues(Tbl_Incident record, boolean isUpdate)
    {
        ContentValues values = new ContentValues();
        values.put(Attributes.STARTTIME.name(), record.startTime);
        values.put(Attributes.ENDTIME.name(), record.endTime);
        values.put(Attributes.DESCRIPTION.name(), record.description);
        values.put(Attributes.ADDRESS.name(), record.address);
        values.put(Attributes.CITYSTZIP.name(), record.citySTZip);
        values.put(Attributes.LATITUDE.name(), record.latitude);
        values.put(Attributes.LONGITUDE.name(), record.longitude);
        if (isUpdate)
        {
            values.put(Attributes.INCIDENTID.name(), record.incidentID);
        }
        return values;
    }

    @Override
    public String GetPrimaryKeyValue(Tbl_Incident record)
    {
        return Long.toString(record.incidentID);
    }

    @Override
    public List<Tbl_Incident> GetList(Cursor cursor)
    {
        List<Tbl_Incident> resultList = new ArrayList<Tbl_Incident>();
        while (cursor.moveToNext())
        {
            Tbl_Incident record = new Tbl_Incident();
            record.incidentID = cursor.getLong(Attributes.INCIDENTID.ordinal());
            record.startTime = cursor.getString(Attributes.STARTTIME.ordinal());
            record.endTime = cursor.getString(Attributes.ENDTIME.ordinal());
            record.description = cursor.getString(Attributes.DESCRIPTION.ordinal());
            record.address = cursor.getString(Attributes.ADDRESS.ordinal());
            record.citySTZip = cursor.getString(Attributes.CITYSTZIP.ordinal());
            record.latitude = cursor.getDouble(Attributes.LATITUDE.ordinal());
            record.longitude = cursor.getDouble(Attributes.LONGITUDE.ordinal());
            resultList.add(record);
        }
        return resultList;
    }
}
