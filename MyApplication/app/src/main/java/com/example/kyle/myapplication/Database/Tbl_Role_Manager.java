package com.example.kyle.myapplication.Database;

import android.content.ContentValues;
import android.database.Cursor;

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
    public String GetCreateScript()
    {
        return Attributes.ROLEID.name() + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                Attributes.TITLE.name() + " TEXT";
    }

    @Override
    public String GetSelectScript(Tbl_Role searchCritera)
    {
        String whereClause = "";
        if (searchCritera.roleID > -1)
        {
            whereClause += Attributes.ROLEID.name() + " = " + Integer.toString(searchCritera.roleID);
        }
        if (!searchCritera.title.isEmpty())
        {
            whereClause += Attributes.TITLE.name() + " = " + searchCritera.title;
        }
        return whereClause;
    }

    @Override
    public ContentValues GetContentValues(Tbl_Role record, boolean isUpdate)
    {
        ContentValues values = new ContentValues();
        values.put(Attributes.TITLE.name(), record.title);
        if (isUpdate)
        {
            values.put(Attributes.ROLEID.name(), record.roleID);
        }
        return values;
    }

    @Override
    public String GetPrimaryKeyValue(Tbl_Role record)
    {
        return Integer.toString(record.roleID);
    }

    @Override
    public List<Tbl_Role> GetList(Cursor cursor)
    {
        List<Tbl_Role> resultList = new ArrayList<Tbl_Role>();
        while (cursor.moveToNext())
        {
            Tbl_Role record = new Tbl_Role();
            record.roleID = cursor.getInt(Attributes.ROLEID.ordinal());
            record.title = cursor.getString(Attributes.TITLE.ordinal());
            resultList.add(record);
        }
        return resultList;
    }
}
