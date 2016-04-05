package com.example.kyle.myapplication.Database.SubmittedForms;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Pair;

import com.example.kyle.myapplication.Database.Abstract.Abstract_Table_Manager;
import com.example.kyle.myapplication.Database.Incident.Tbl_Incident_Manager;
import com.example.kyle.myapplication.Database.Personnel.Tbl_Personnel;
import com.example.kyle.myapplication.Database.Personnel.Tbl_Personnel_Manager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
    public List<Tbl_SubmittedForms> Select(Tbl_SubmittedForms searchCriteria)
    {
        List<Tbl_SubmittedForms> submittedFormsList = super.Select(searchCriteria);
        for (int i = 0; i < submittedFormsList.size(); i++)
        {
            Tbl_SubmittedForms submittedForm = submittedFormsList.get(i);

            //get the personnel for this record
            submittedForm.personnel = Tbl_Personnel_Manager.current.Select(new Tbl_Personnel(submittedForm.personnelID)).get(0);
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
    protected String GetCreateScript()
    {
        return Attributes.SUBMITTEDFORMID.name() + " INT(11) NOT NULL AUTO_INCREMENT,\n" +
                Attributes.INCIDENTID.name() + " INT(11) NOT NULL,\n" +
                Attributes.PERSONNELID.name() + " INT(11) NOT NULL,\n" +
                Attributes.FORM.name() + " VARCHAR(100) NOT NULL,\n" +
                Attributes.DESCRIPTION.name() + " VARCHAR(100) NOT NULL,\n" +
                Attributes.TIMESUBMITTED.name() + " VARCHAR(20) NOT NULL,\n" +
                "PRIMARY KEY (" + Attributes.SUBMITTEDFORMID.name() + "),\n" +
                "FOREIGN KEY(" + Attributes.INCIDENTID.name() + ") REFERENCES " + Tbl_Incident_Manager.current.GetTableName() + "(" + Tbl_Incident_Manager.Attributes.INCIDENTID.name() + "),\n" +
                "FOREIGN KEY(" + Attributes.PERSONNELID.name() + ") REFERENCES " + Tbl_Personnel_Manager.current.GetTableName() + "(" + Tbl_Personnel_Manager.Attributes.PERSONNELID.name() + ")\n";
    }

    @Override
    protected List<Pair<String, String>> GetContentValues(Tbl_SubmittedForms record)
    {
        List<Pair<String, String>> values = new ArrayList<Pair<String, String>>();
        values.add(new Pair<String, String>(Attributes.SUBMITTEDFORMID.name(), Long.toString(record.submittedFormID)));
        values.add(new Pair<String, String>(Attributes.INCIDENTID.name(), Long.toString(record.incidentID)));
        values.add(new Pair<String, String>(Attributes.PERSONNELID.name(), Long.toString(record.personnelID)));
        values.add(new Pair<String, String>(Attributes.FORM.name(), record.form));
        values.add(new Pair<String, String>(Attributes.TIMESUBMITTED.name(), record.timeSubmitted));
        return values;
    }

    @Override
    public String GetPrimaryKeyValue(Tbl_SubmittedForms record)
    {
        return Long.toString(record.submittedFormID);
    }

    @Override
    public List<Tbl_SubmittedForms> GetList(List<JSONObject> JSONList)
    {
        List<Tbl_SubmittedForms> resultList = new ArrayList<Tbl_SubmittedForms>();
        try
        {
            for (JSONObject json : JSONList)
            {
                Tbl_SubmittedForms record = new Tbl_SubmittedForms();
                record.submittedFormID = json.getLong(Attributes.SUBMITTEDFORMID.name());
                record.incidentID = json.getLong(Attributes.INCIDENTID.name());
                record.personnelID = json.getLong(Attributes.PERSONNELID.name());
                record.form = json.getString(Attributes.FORM.name());
                record.timeSubmitted = json.getString(Attributes.TIMESUBMITTED.name());
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
