package com.example.kyle.myapplication.Database.Incident;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Pair;

import com.example.kyle.myapplication.Database.Abstract.Abstract_Table;
import com.example.kyle.myapplication.Database.Abstract.Abstract_Table_Manager;
import com.example.kyle.myapplication.Database.DBOperation;
import com.example.kyle.myapplication.Database.IncidentLink.Tbl_IncidentLink;
import com.example.kyle.myapplication.Database.IncidentLink.Tbl_IncidentLink_Manager;
import com.example.kyle.myapplication.Database.Role.Tbl_Role;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
    public DBOperation RecordOperation(Context context, Tbl_Incident record, String SuccessMessage, String FailMessage)
    {
        DBOperation operation = null;
        switch (record.sqlMode)
        {
            case INSERT:
                operation = super.RecordOperation(context, record, SuccessMessage, FailMessage);
                if (operation.ResultID != -1)
                {
                    for (int i = 0; i < record.incidentLinks.size(); i++)
                    {
                        Tbl_IncidentLink incidentLink = record.incidentLinks.get(i);
                        incidentLink.incidentID = operation.ResultID;
                        incidentLink.sqlMode = record.sqlMode;
                        DBOperation resultOperation = Tbl_IncidentLink_Manager.current.RecordOperation(incidentLink);
                        if (resultOperation.ResultID == -1)
                        {
                            break;
                        }
                    }
                }
                break;
            case UPDATE:
                operation = super.RecordOperation(context, record, SuccessMessage, FailMessage);
                for (int i = 0; i < record.incidentLinks.size(); i++)
                {
                    Tbl_IncidentLink incidentLink = record.incidentLinks.get(i);
                    if (incidentLink.sqlMode == Abstract_Table.SQLMode.INSERT)
                    {
                        incidentLink.incidentID = record.incidentID;
                    }
                    Tbl_IncidentLink_Manager.current.RecordOperation(incidentLink);
                }
                break;
            case DELETE:
                for (int i = 0; i < record.incidentLinks.size(); i++)
                {
                    Tbl_IncidentLink incidentLink = record.incidentLinks.get(i);
                    incidentLink.sqlMode = record.sqlMode;
                    Tbl_IncidentLink_Manager.current.RecordOperation(incidentLink);
                }
                operation = super.RecordOperation(context, record, SuccessMessage, FailMessage);
                break;
        }

        return operation;
    }

    /*
        @Override
        public boolean Update(Tbl_Incident toUpdate)
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
*/
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
    protected String GetCreateScript()
    {
        return Attributes.INCIDENTID.name() + " INT(11) NOT NULL AUTO_INCREMENT,\n" +
                Attributes.STARTTIME.name() + " VARCHAR(20) NOT NULL,\n" +
                Attributes.ENDTIME.name() + " VARCHAR(20) NOT NULL,\n" +
                Attributes.DESCRIPTION.name() + " VARCHAR(100) NOT NULL,\n" +
                Attributes.ADDRESS.name() + " VARCHAR(100) NOT NULL,\n" +
                Attributes.CITYSTZIP.name() + " VARCHAR(100) NOT NULL,\n" +
                Attributes.LATITUDE.name() + " VARCHAR(20) NOT NULL,\n" +
                Attributes.LONGITUDE.name() + " VARCHAR(20) NOT NULL,\n" +
                "PRIMARY KEY (" + Attributes.INCIDENTID.name() + ")\n";
    }

    @Override
    protected List<Pair<String, String>> GetContentValues(Tbl_Incident record)
    {
        List<Pair<String, String>> values = new ArrayList<Pair<String, String>>();
        values.add(new Pair<String, String>(Attributes.INCIDENTID.name(), Long.toString(record.incidentID)));
        values.add(new Pair<String, String>(Attributes.STARTTIME.name(), record.startTime));
        values.add(new Pair<String, String>(Attributes.ENDTIME.name(), record.endTime));
        values.add(new Pair<String, String>(Attributes.DESCRIPTION.name(), record.description));
        values.add(new Pair<String, String>(Attributes.ADDRESS.name(), record.address));
        values.add(new Pair<String, String>(Attributes.CITYSTZIP.name(), record.citySTZip));
        values.add(new Pair<String, String>(Attributes.LATITUDE.name(), record.latitude));
        values.add(new Pair<String, String>(Attributes.LONGITUDE.name(), record.longitude));
        return values;
    }

    @Override
    public String GetPrimaryKeyValue(Tbl_Incident record)
    {
        return Long.toString(record.incidentID);
    }

    @Override
    public List<Tbl_Incident> GetList(List<JSONObject> JSONList)
    {
        List<Tbl_Incident> resultList = new ArrayList<Tbl_Incident>();
        try
        {
            for (JSONObject json : JSONList)
            {
                Tbl_Incident record = new Tbl_Incident();
                record.incidentID = json.getLong(Attributes.INCIDENTID.name());
                record.startTime = json.getString(Attributes.STARTTIME.name());
                record.endTime = json.getString(Attributes.ENDTIME.name());
                record.description = json.getString(Attributes.DESCRIPTION.name());
                record.address = json.getString(Attributes.ADDRESS.name());
                record.citySTZip = json.getString(Attributes.CITYSTZIP.name());
                record.latitude = json.getString(Attributes.LATITUDE.name());
                record.longitude = json.getString(Attributes.LONGITUDE.name());

                //get the incident links for this record
                Tbl_IncidentLink incidentLink = new Tbl_IncidentLink();
                incidentLink.incidentID = record.incidentID;
                record.incidentLinks = Tbl_IncidentLink_Manager.current.Select(incidentLink);
                resultList.add(record);
            }
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
        return resultList;
    }
}
