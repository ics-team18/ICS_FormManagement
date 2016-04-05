package com.example.kyle.myapplication.Database.Templates;

import com.example.kyle.myapplication.Database.Abstract.Abstract_Table_Manager;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Pair;

import com.example.kyle.myapplication.Database.Personnel.Tbl_Personnel;
import com.example.kyle.myapplication.Database.Role.Tbl_Role;
import com.example.kyle.myapplication.Database.Role.Tbl_Role_Manager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kyle on 1/31/2016.
 */
public class Tbl_Templates_Manager extends Abstract_Table_Manager<Tbl_Templates>
{
    public static Tbl_Templates_Manager current = new Tbl_Templates_Manager();

    public enum Attributes
    {
        TEMPLATESID,
        ROLEID,
        DESCRIPTION,
        FILENAME,
    }

    @Override
    public List<Tbl_Templates> Select(Tbl_Templates searchCriteria)
    {
        List<Tbl_Templates> templatesList = super.Select(searchCriteria);
        for (int i = 0; i < templatesList.size(); i++)
        {
            Tbl_Templates template = templatesList.get(i);

            //get the role for this template record
            Tbl_Role roleSearchCriteria = new Tbl_Role();
            roleSearchCriteria.roleID = template.roleID;
            template.role = Tbl_Role_Manager.current.Select(roleSearchCriteria).get(0);
        }
        return templatesList;
    }

    @Override
    public String GetPrimaryKey()
    {
        return Attributes.TEMPLATESID.name();
    }

    @Override
    public String GetTableName()
    {
        return Tbl_Templates.class.getSimpleName();
    }

    @Override
    protected String GetCreateScript()
    {
        return Attributes.TEMPLATESID.name() + " INT(11) NOT NULL AUTO_INCREMENT,\n" +
                Attributes.ROLEID.name() + " INT(11) NOT NULL,\n" +
                Attributes.DESCRIPTION.name() + " VARCHAR(30) NOT NULL,\n" +
                Attributes.FILENAME.name() + " VARCHAR(100) NOT NULL,\n" +
                "PRIMARY KEY (" + Attributes.TEMPLATESID.name() + "),\n" +
                "FOREIGN KEY (" + Attributes.ROLEID.name() + ") REFERENCES " + Tbl_Role_Manager.current.GetTableName() + "(" + Tbl_Role_Manager.Attributes.ROLEID.name() + ")\n";
    }

    @Override
    protected List<Pair<String, String>> GetContentValues(Tbl_Templates record)
    {
        List<Pair<String, String>> values = new ArrayList<Pair<String, String>>();
        values.add(new Pair<String, String>(Attributes.TEMPLATESID.name(), Long.toString(record.templatesID)));
        values.add(new Pair<String, String>(Attributes.ROLEID.name(), Long.toString(record.roleID)));
        values.add(new Pair<String, String>(Attributes.DESCRIPTION.name(), record.description));
        values.add(new Pair<String, String>(Attributes.FILENAME.name(), record.fileName));
        return values;
    }

    @Override
    public String GetPrimaryKeyValue(Tbl_Templates record)
    {
        return Long.toString(record.templatesID);
    }

    @Override
    public List<Tbl_Templates> GetList(List<JSONObject> JSONList)
    {
        List<Tbl_Templates> resultList = new ArrayList<Tbl_Templates>();
        try
        {
            for (JSONObject json : JSONList)
            {
                Tbl_Templates record = new Tbl_Templates();
                record.templatesID = json.getLong(Attributes.TEMPLATESID.name());
                record.roleID = json.getLong(Attributes.ROLEID.name());
                record.description = json.getString(Attributes.DESCRIPTION.name());
                record.fileName = json.getString(Attributes.FILENAME.name());

                Tbl_Role roleSearchCriteria = new Tbl_Role();
                roleSearchCriteria.roleID = record.roleID;
                record.role = Tbl_Role_Manager.current.Select(roleSearchCriteria).get(0);
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
