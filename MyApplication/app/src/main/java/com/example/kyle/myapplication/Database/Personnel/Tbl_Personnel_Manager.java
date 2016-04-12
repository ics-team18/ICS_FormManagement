package com.example.kyle.myapplication.Database.Personnel;

import android.util.Pair;

import com.example.kyle.myapplication.Database.Abstract.Abstract_Table_Manager;

import org.json.JSONException;
import org.json.JSONObject;

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
    protected String GetCreateScript()
    {
        return Attributes.PERSONNELID.name() + " INT(11) NOT NULL AUTO_INCREMENT,\n" +
                Attributes.FIRSTNAME.name() + " VARCHAR(30) NOT NULL,\n" +
                Attributes.LASTNAME.name() + " VARCHAR(30) NOT NULL,\n" +
                Attributes.EMAIL.name() + " VARCHAR(100) NOT NULL,\n" +
                Attributes.PASSWORD.name() + " VARCHAR(15) NOT NULL,\n" +
                Attributes.MOBILEPHONE.name() + " VARCHAR(12) NOT NULL,\n" +
                Attributes.TITLE.name() + " VARCHAR(100) NOT NULL,\n" +
                Attributes.ISSUPERVISOR.name() + " INT(1) NOT NULL,\n" +
                "PRIMARY KEY (" + Attributes.PERSONNELID.name() + "),\n" +
                "UNIQUE KEY " + Attributes.MOBILEPHONE.name() + "(" + Attributes.MOBILEPHONE.name() + "),\n" +
                "UNIQUE KEY " + Attributes.EMAIL.name() + " (" + Attributes.EMAIL.name() + ")\n";
    }

    @Override
    protected List<Pair<String, String>> GetContentValues(Tbl_Personnel record)
    {
        List<Pair<String, String>> values = new ArrayList<Pair<String, String>>();
        values.add(new Pair<String, String>(Attributes.PERSONNELID.name(), Long.toString(record.personnelID)));
        values.add(new Pair<String, String>(Attributes.FIRSTNAME.name(), record.firstName));
        values.add(new Pair<String, String>(Attributes.LASTNAME.name(), record.lastName));
        values.add(new Pair<String, String>(Attributes.EMAIL.name(), record.email));
        values.add(new Pair<String, String>(Attributes.PASSWORD.name(), record.password));
        values.add(new Pair<String, String>(Attributes.MOBILEPHONE.name(), record.mobilePhone));
        values.add(new Pair<String, String>(Attributes.TITLE.name(), record.title));
        if (record.isSupervisor != null)
        {
            values.add(new Pair<String, String>(Attributes.ISSUPERVISOR.name(), record.isSupervisor ? "1" : "0"));
        }
        return values;
    }

    @Override
    public String GetPrimaryKeyValue(Tbl_Personnel record)
    {
        return Long.toString(record.personnelID);
    }

    @Override
    public List<Tbl_Personnel> GetList(List<JSONObject> JSONList)
    {
        List<Tbl_Personnel> resultList = new ArrayList<Tbl_Personnel>();
        try
        {
            for (JSONObject json : JSONList)
            {
                Tbl_Personnel record = new Tbl_Personnel();
                record.personnelID = json.getLong(Attributes.PERSONNELID.name());
                record.firstName = json.getString(Attributes.FIRSTNAME.name());
                record.lastName = json.getString(Attributes.LASTNAME.name());
                record.email = json.getString(Attributes.EMAIL.name());
                record.password = json.getString(Attributes.PASSWORD.name());
                record.mobilePhone = json.getString(Attributes.MOBILEPHONE.name());
                record.title = json.getString(Attributes.TITLE.name());
                record.isSupervisor = json.getInt(Attributes.ISSUPERVISOR.name()) != 0;
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
