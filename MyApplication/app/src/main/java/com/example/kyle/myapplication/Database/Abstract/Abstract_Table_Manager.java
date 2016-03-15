package com.example.kyle.myapplication.Database.Abstract;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.List;

/**
 * Created by Kyle on 1/31/2016.
 */
public abstract class Abstract_Table_Manager<T>
{
    public enum Table
    {
        NONE,
        PERSONNEL,
        ROLE,
        INCIDENT,
        SUBMITTEDFORMS,
    }

    public void Create(SQLiteDatabase db)
    {
        db.execSQL("CREATE TABLE IF NOT EXISTS " + GetTableName() + "(" + GetCreateScript() + ")");
    }

    public long Insert(SQLiteDatabase db, T toInsert)
    {
        ContentValues values = GetContentValues(toInsert, false);
        long result = db.insert(GetTableName(), null, values);
        return result;
    }

    public boolean Update(SQLiteDatabase db, T toUpdate)
    {
        ContentValues values = GetContentValues(toUpdate, true);
        String whereClause = GetWhereClause();
        String[] whereArgs = GetWhereArgs(toUpdate);
        db.update(GetTableName(), values, whereClause, whereArgs);
        return true;
    }

    public void Delete(SQLiteDatabase db, T toDelete)
    {
        String whereClause = GetWhereClause();
        String[] whereArgs = GetWhereArgs(toDelete);
        int rowsDeleted = db.delete(GetTableName(), whereClause, whereArgs);
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
                whereClause = whereClause.replace("= NOT NULL","IS NOT NULL");
                whereClause = whereClause.replace("= NULL","IS NULL");
                whereClause = whereClause.replace("= !=","!=");
                whereClause = whereClause.replace("= =", "=");
                whereClause = "WHERE " + whereClause;
                whereClause = whereClause.replace("WHERE  AND ", "WHERE ");
            }
        }
        Cursor result = db.rawQuery("SELECT * FROM " + GetTableName() + " " + whereClause, null);
        List<T> resultList = GetList(result);
        return resultList;
    }

    public int GetNextID(SQLiteDatabase db)
    {
        String query = "SELECT SEQ FROM SQLITE_SEQUENCE WHERE NAME = '" + GetTableName() + "'";
        int id = 1;
        Cursor result = db.rawQuery(query, null);
        if(result.moveToFirst())
        {
            id = result.getInt(0);
            ++id;
        }
        return id;
    }

    public int GetLastID(SQLiteDatabase db)
    {
        String query = "SELECT MAX(" + GetPrimaryKey() + ") FROM " + GetTableName();
        int id = 1;
        Cursor result = db.rawQuery(query, null);
        if(result.moveToFirst())
        {
            id = result.getInt(0);
            ++id;
        }
        return id;
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