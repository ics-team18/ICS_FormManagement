package com.example.kyle.myapplication.Database.IncidentLink;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.kyle.myapplication.Database.Abstract.Abstract_Table_Manager;
import com.example.kyle.myapplication.Database.Incident.Tbl_Incident_Manager;
import com.example.kyle.myapplication.Database.Personnel.Tbl_Personnel;
import com.example.kyle.myapplication.Database.Personnel.Tbl_Personnel_Manager;
import com.example.kyle.myapplication.Database.Role.Tbl_Role;
import com.example.kyle.myapplication.Database.Role.Tbl_Role_Manager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kyle on 3/8/2016.
 */
public class Tbl_IncidentLink_Manager extends Abstract_Table_Manager<Tbl_IncidentLink>
{
    public static Tbl_IncidentLink_Manager current = new Tbl_IncidentLink_Manager();

    public enum Attributes
    {
        INCIDENTLINKID,
        INCIDENTID,
        PERSONNELID,
        ROLEID,
        RANKING,
    }

    @Override
    public List<Tbl_IncidentLink> Select(SQLiteDatabase db, Tbl_IncidentLink searchCriteria)
    {
        List<Tbl_IncidentLink> incidentLinkList = super.Select(db, searchCriteria);
        for (int i = 0; i < incidentLinkList.size(); i++)
        {
            Tbl_IncidentLink incidentLink = incidentLinkList.get(i);

            //get the personnel for this link record
            incidentLink.personnel = Tbl_Personnel_Manager.current.Select(db, new Tbl_Personnel(incidentLink.personnelID)).get(0);

            //get the role for this link record
            incidentLink.role = Tbl_Role_Manager.current.Select(db, new Tbl_Role(incidentLink.roleID)).get(0);
        }
        return incidentLinkList;
    }

    @Override
    public String GetPrimaryKey()
    {
        return Attributes.INCIDENTLINKID.name();
    }

    @Override
    public String GetTableName()
    {
        return Tbl_IncidentLink.class.getSimpleName();
    }

    @Override
    public String GetCreateScript()
    {
        return Attributes.INCIDENTLINKID.name() + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                Attributes.INCIDENTID.name() + " INTEGER, " +
                Attributes.PERSONNELID.name() + " INTEGER, " +
                Attributes.ROLEID.name() + " INTEGER, " +
                Attributes.RANKING.name() + " INTEGER, " +
                "FOREIGN KEY(" + Attributes.INCIDENTID.name() + ") REFERENCES " + Tbl_Incident_Manager.current.GetTableName() + "(" + Tbl_Incident_Manager.Attributes.INCIDENTID.name() + ")," +
                "FOREIGN KEY(" + Attributes.PERSONNELID.name() + ") REFERENCES " + Tbl_Personnel_Manager.current.GetTableName() + "(" + Tbl_Personnel_Manager.Attributes.PERSONNELID.name() + ")," +
                "FOREIGN KEY(" + Attributes.ROLEID.name() + ") REFERENCES " + Tbl_Role_Manager.current.GetTableName() + "(" + Tbl_Role_Manager.Attributes.ROLEID.name() + ")";
    }

    @Override
    public String GetSelectScript(Tbl_IncidentLink searchCritera)
    {
        String whereClause = "";
        if (searchCritera.incidentLinkID > -1)
        {
            whereClause += Attributes.INCIDENTLINKID.name() + " = " + Long.toString(searchCritera.incidentLinkID);
        }
        if (searchCritera.incidentID > -1)
        {
            whereClause += Attributes.INCIDENTID.name() + " = " + Long.toString(searchCritera.incidentID);
        }
        if (searchCritera.personnelID > -1)
        {
            whereClause += Attributes.PERSONNELID.name() + " = " + Long.toString(searchCritera.personnelID);
        }
        if (searchCritera.roleID > -1)
        {
            whereClause += Attributes.ROLEID.name() + " = " + Long.toString(searchCritera.roleID);
        }
        if (searchCritera.ranking > -1)
        {
            whereClause += Attributes.RANKING.name() + " = " + searchCritera.ranking;
        }

        return whereClause;
    }

    @Override
    public ContentValues GetContentValues(Tbl_IncidentLink record, boolean isUpdate)
    {
        ContentValues values = new ContentValues();
        values.put(Attributes.INCIDENTID.name(), record.incidentID);
        values.put(Attributes.PERSONNELID.name(), record.personnelID);
        values.put(Attributes.ROLEID.name(), record.roleID);
        values.put(Attributes.RANKING.name(), record.ranking);
        if (isUpdate)
        {
            values.put(Attributes.INCIDENTLINKID.name(), record.incidentLinkID);
        }
        return values;
    }

    @Override
    public String GetPrimaryKeyValue(Tbl_IncidentLink record)
    {
        return Long.toString(record.incidentLinkID);
    }

    @Override
    public List<Tbl_IncidentLink> GetList(Cursor cursor)
    {
        List<Tbl_IncidentLink> resultList = new ArrayList<Tbl_IncidentLink>();
        while (cursor.moveToNext())
        {
            Tbl_IncidentLink record = new Tbl_IncidentLink();
            record.incidentLinkID = cursor.getLong(Attributes.INCIDENTLINKID.ordinal());
            record.incidentID = cursor.getLong(Attributes.INCIDENTID.ordinal());
            record.personnelID = cursor.getLong(Attributes.PERSONNELID.ordinal());
            record.roleID = cursor.getLong(Attributes.ROLEID.ordinal());
            record.ranking = cursor.getInt(Attributes.RANKING.ordinal());
            resultList.add(record);
        }
        return resultList;
    }
}
