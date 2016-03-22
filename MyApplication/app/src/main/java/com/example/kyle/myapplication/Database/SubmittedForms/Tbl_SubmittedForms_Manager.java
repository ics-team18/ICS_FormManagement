package com.example.kyle.myapplication.Database.SubmittedForms;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.kyle.myapplication.Database.Abstract.Abstract_Table_Manager;
import com.example.kyle.myapplication.Database.Incident.Tbl_Incident;
import com.example.kyle.myapplication.Database.Incident.Tbl_Incident_Manager;
import com.example.kyle.myapplication.Database.IncidentLink.Tbl_IncidentLink;
import com.example.kyle.myapplication.Database.Personnel.Tbl_Personnel;
import com.example.kyle.myapplication.Database.Personnel.Tbl_Personnel_Manager;
import com.example.kyle.myapplication.Database.Role.Tbl_Role;
import com.example.kyle.myapplication.Database.Role.Tbl_Role_Manager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kyle on 1/31/2016.
 */
public class Tbl_SubmittedForms_Manager extends Abstract_Table_Manager<Tbl_SubmittedForms>
{
    public static Tbl_SubmittedForms_Manager current = new Tbl_SubmittedForms_Manager();

    public enum Attributes
    {
        SUBMITTEDFORMID,
        INCIDENTID,
        PERSONNELID,
        FORM,
        DESCRIPTION,
        TIMESUBMITTED,
    }

    @Override
    public List<Tbl_SubmittedForms> Select(SQLiteDatabase db, Tbl_SubmittedForms searchCriteria)
    {
        List<Tbl_SubmittedForms> submittedFormsList = super.Select(db, searchCriteria);
        for (int i = 0; i < submittedFormsList.size(); i++)
        {
            Tbl_SubmittedForms submittedForm = submittedFormsList.get(i);

            //get the personnel for this record
            submittedForm.personnel = Tbl_Personnel_Manager.current.Select(db, new Tbl_Personnel(submittedForm.personnelID)).get(0);
        }
        return submittedFormsList;
    }

    @Override
    public String GetPrimaryKey()
    {
        return Attributes.SUBMITTEDFORMID.name();
    }

    @Override
    public String GetTableName()
    {
        return Tbl_SubmittedForms.class.getSimpleName();
    }

    @Override
    public String GetCreateScript()
    {
        return Attributes.SUBMITTEDFORMID.name() + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                Attributes.INCIDENTID.name() + " INTEGER, " +
                Attributes.PERSONNELID.name() + " INTEGER, " +
                Attributes.FORM.name() + " TEXT COLLATE NOCASE, " +
                Attributes.DESCRIPTION.name() + " TEXT COLLATE NOCASE, " +
                Attributes.TIMESUBMITTED.name() + " TEXT COLLATE NOCASE," +
                "FOREIGN KEY(" + Attributes.INCIDENTID.name() + ") REFERENCES " + Tbl_Incident_Manager.current.GetTableName() + "(" + Tbl_Incident_Manager.Attributes.INCIDENTID.name() + ")," +
                "FOREIGN KEY(" + Attributes.PERSONNELID.name() + ") REFERENCES " + Tbl_Personnel_Manager.current.GetTableName() + "(" + Tbl_Personnel_Manager.Attributes.PERSONNELID.name() + ")";
    }

    @Override
    public String GetSelectScript(Tbl_SubmittedForms searchCritera)
    {
        String whereClause = "";
        if (searchCritera.submittedFormID > -1)
        {
            whereClause += Attributes.SUBMITTEDFORMID.name() + " = " + Long.toString(searchCritera.submittedFormID);
        }
        if (searchCritera.incidentID > -1)
        {
            whereClause += Attributes.INCIDENTID.name() + " = " + searchCritera.incidentID;
        }
        if (searchCritera.personnelID > -1)
        {
            whereClause += Attributes.PERSONNELID.name() + " = " + searchCritera.personnelID;
        }
        if (!searchCritera.form.isEmpty())
        {
            whereClause += Attributes.FORM.name() + " = " + searchCritera.form;
        }
        if (!searchCritera.timeSubmitted.isEmpty())
        {
            whereClause += Attributes.TIMESUBMITTED.name() + " = " + searchCritera.timeSubmitted;
        }
        return whereClause;
    }

    @Override
    public ContentValues GetContentValues(Tbl_SubmittedForms record, boolean isUpdate)
    {
        ContentValues values = new ContentValues();
        values.put(Attributes.INCIDENTID.name(), record.incidentID);
        values.put(Attributes.PERSONNELID.name(), record.personnelID);
        values.put(Attributes.FORM.name(), record.form);
        values.put(Attributes.TIMESUBMITTED.name(), record.timeSubmitted);
        if (isUpdate)
        {
            values.put(Attributes.SUBMITTEDFORMID.name(), record.submittedFormID);
        }
        return values;
    }

    @Override
    public String GetPrimaryKeyValue(Tbl_SubmittedForms record)
    {
        return Long.toString(record.submittedFormID);
    }

    @Override
    public List<Tbl_SubmittedForms> GetList(Cursor cursor)
    {
        List<Tbl_SubmittedForms> resultList = new ArrayList<Tbl_SubmittedForms>();
        while (cursor.moveToNext())
        {
            Tbl_SubmittedForms record = new Tbl_SubmittedForms();
            record.submittedFormID = cursor.getLong(Attributes.SUBMITTEDFORMID.ordinal());
            record.incidentID = cursor.getLong(Attributes.INCIDENTID.ordinal());
            record.personnelID = cursor.getLong(Attributes.PERSONNELID.ordinal());
            record.form = cursor.getString(Attributes.FORM.ordinal());
            record.timeSubmitted = cursor.getString(Attributes.TIMESUBMITTED.ordinal());
            resultList.add(record);
        }
        return resultList;
    }
}
