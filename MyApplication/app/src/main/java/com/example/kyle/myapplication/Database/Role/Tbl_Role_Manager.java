package com.example.kyle.myapplication.Database.Role;

import android.database.Cursor;
import android.util.Pair;

import com.example.kyle.myapplication.Database.Abstract.Abstract_Table_Manager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kyle on 1/31/2016.
 */
public class Tbl_Role_Manager extends Abstract_Table_Manager<Tbl_Role>
{
    public static Tbl_Role_Manager current = new Tbl_Role_Manager();

    public enum Attributes
    {
        ROLEID,
        TITLE,
    }

    @Override
    public String GetPrimaryKey()
    {
        return Attributes.ROLEID.name();
    }

    @Override
    public String GetTableName()
    {
        return Tbl_Role.class.getSimpleName();
    }

    @Override
    protected String GetCreateScript()
    {
        return Attributes.ROLEID.name() + " INT(11) NOT NULL AUTO_INCREMENT,\n" +
                Attributes.TITLE.name() + " VARCHAR(30) NOT NULL,\n" +
                "PRIMARY KEY (" + Attributes.ROLEID.name() + "),\n" +
                "UNIQUE KEY " + Attributes.TITLE.name() + "(" + Attributes.TITLE.name() + ")\n";
    }

    @Override
    protected List<Pair<String, String>> GetContentValues(Tbl_Role record)
    {
        List<Pair<String, String>> values = new ArrayList<Pair<String, String>>();
        values.add(new Pair<String, String>(Attributes.ROLEID.name(), Long.toString(record.roleID)));
        values.add(new Pair<String, String>(Attributes.TITLE.name(), record.title));
        return values;
    }

    @Override
    public String GetPrimaryKeyValue(Tbl_Role record)
    {
        return Long.toString(record.roleID);
    }

    @Override
    public List<Tbl_Role> GetList(List<JSONObject> JSONList)
    {
        List<Tbl_Role> resultList = new ArrayList<Tbl_Role>();
        try
        {
            for (JSONObject json : JSONList)
            {
                Tbl_Role record = new Tbl_Role();
                record.roleID = json.getLong(Attributes.ROLEID.name());
                record.title = json.getString(Attributes.TITLE.name());
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
