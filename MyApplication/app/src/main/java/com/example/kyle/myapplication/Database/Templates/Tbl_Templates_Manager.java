package com.example.kyle.myapplication.Database.Templates;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.kyle.myapplication.Database.Abstract.Abstract_Table_Manager;
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
public class Tbl_Templates_Manager extends Abstract_Table_Manager<Tbl_Templates>
{
    public static Tbl_Templates_Manager current = new Tbl_Templates_Manager();

    public enum Attributes
    {
        TEMPLATESID,
        ROLEID,
        DESCRIPTION,
        TEMPLATEURL,
    }

    @Override
    public List<Tbl_Templates> Select(SQLiteDatabase db, Tbl_Templates searchCriteria)
    {
        List<Tbl_Templates> templatesList = super.Select(db, searchCriteria);
        for (int i = 0; i < templatesList.size(); i++)
        {
            Tbl_Templates template = templatesList.get(i);

            //get the role for this template record
            Tbl_Role roleSearchCriteria = new Tbl_Role();
            roleSearchCriteria.roleID = template.roleID;
            template.role = Tbl_Role_Manager.current.Select(db, roleSearchCriteria).get(0);
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
    public String GetCreateScript()
    {
        return Attributes.TEMPLATESID.name() + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                Attributes.ROLEID.name() + " TEXT COLLATE NOCASE, " +
                Attributes.DESCRIPTION.name() + " TEXT COLLATE NOCASE, " +
                Attributes.TEMPLATEURL.name() + " TEXT COLLATE NOCASE, " +
                "FOREIGN KEY(" + Attributes.ROLEID.name() + ") REFERENCES " + Tbl_Role_Manager.current.GetTableName() + "(" + Tbl_Role_Manager.Attributes.ROLEID.name() + ")";
    }

    @Override
    public String GetSelectScript(Tbl_Templates searchCritera)
    {
        String whereClause = "";
        if (searchCritera.templatesID > -1)
        {
            whereClause += " AND " + Attributes.TEMPLATESID.name() + " = '" + Long.toString(searchCritera.templatesID) + "'";
        }
        if (searchCritera.roleID > -1)
        {
            whereClause += " AND " + Attributes.ROLEID.name() + " = '" + searchCritera.roleID + "'";
        }
        if (!searchCritera.description.isEmpty())
        {
            whereClause += " AND " + Attributes.DESCRIPTION.name() + " = '" + searchCritera.description + "'";
        }
        if (!searchCritera.templateURL.isEmpty())
        {
            whereClause += " AND " + Attributes.TEMPLATEURL.name() + " = '" + searchCritera.templateURL + "'";
        }

        return whereClause;
    }

    @Override
    public ContentValues GetContentValues(Tbl_Templates record, boolean isUpdate)
    {
        ContentValues values = new ContentValues();
        values.put(Attributes.ROLEID.name(), record.roleID);
        values.put(Attributes.DESCRIPTION.name(), record.description);
        values.put(Attributes.TEMPLATEURL.name(), record.templateURL);
        if (isUpdate)
        {
            values.put(Attributes.TEMPLATESID.name(), record.templatesID);
        }
        return values;
    }

    @Override
    public String GetPrimaryKeyValue(Tbl_Templates record)
    {
        return Long.toString(record.templatesID);
    }

    @Override
    public List<Tbl_Templates> GetList(Cursor cursor)
    {
        List<Tbl_Templates> resultList = new ArrayList<Tbl_Templates>();
        while (cursor.moveToNext())
        {
            Tbl_Templates record = new Tbl_Templates();
            record.templatesID = cursor.getLong(Attributes.TEMPLATESID.ordinal());
            record.roleID = cursor.getLong(Attributes.ROLEID.ordinal());
            record.description = cursor.getString(Attributes.DESCRIPTION.ordinal());
            record.templateURL = cursor.getString(Attributes.TEMPLATEURL.ordinal());
            resultList.add(record);
        }
        return resultList;
    }
}
