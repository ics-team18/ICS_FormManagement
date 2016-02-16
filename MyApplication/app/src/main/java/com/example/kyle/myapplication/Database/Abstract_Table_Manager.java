package com.example.kyle.myapplication.Database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kyle on 1/31/2016.
 */
public abstract class Abstract_Table_Manager<T>
{
    public void Create(SQLiteDatabase db)
    {
        db.execSQL("CREATE TABLE IF NOT EXISTS " + GetTableName() + "(" + GetCreateScript() + ")");
    }

    public boolean Insert(SQLiteDatabase db, T toInsert)
    {
        ContentValues values = GetContentValues(toInsert, false);
        long result = db.insert(GetTableName(), null, values);
        return result == -1;
    }

    public boolean Update(SQLiteDatabase db, T toUpdate)
    {
        ContentValues values = GetContentValues(toUpdate, true);
        String whereClause = GetWhereClause();
        String[] whereArgs = GetWhereArgs(toUpdate);
        db.update(GetTableName(), values, whereClause, whereArgs);
        return true;
    }

    public Integer Delete(SQLiteDatabase db, T toDelete)
    {
        String whereClause = GetWhereClause();
        String[] whereArgs = GetWhereArgs(toDelete);
        return db.delete(GetTableName(), whereClause, whereArgs);
    }

    public List<T> Select(SQLiteDatabase db)
    {
        return Select(db, null);
    }

    public List<T> Select(SQLiteDatabase db, T searchCriteria)
    {
        String whereClause = "";
        if (searchCriteria != null)
        {
            whereClause = GetSelectScript(searchCriteria);
            if (!whereClause.isEmpty())
            {
                whereClause = "WHERE " + whereClause;
                whereClause = whereClause.replace("WHERE  AND ", "WHERE ");
            }
        }
        Cursor result = db.rawQuery("SELECT * FROM " + GetTableName() + " " + whereClause, null);
        List<T> resultList = GetList(result);
        return resultList;
    }

    public abstract String GetPrimaryKey();

    public abstract String GetPrimaryKeyValue(T record);

    public abstract String GetTableName();

    public abstract String GetCreateScript();

    public abstract String GetSelectScript(T searchCriteria);

    public abstract List<T> GetList(Cursor cursor);

    public abstract ContentValues GetContentValues(T record, boolean isUpdate);

    private String GetWhereClause()
    {
        return GetPrimaryKey() + " = ?";
    }

    private String[] GetWhereArgs(T record)
    {
        return new String[]{GetPrimaryKeyValue(record)};
    }
}
