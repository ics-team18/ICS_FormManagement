package com.example.kyle.myapplication.Database.Abstract;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Pair;

import com.example.kyle.myapplication.Database.DBOperation;
import com.example.kyle.myapplication.Database.DatabaseManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
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
        TEMPLATES,
        SUBMITTEDFORMS,
    }

    public String GetFullCreateScript()
    {
        String createScript = "CREATE TABLE IF NOT EXISTS " + GetTableName() + "(" + GetCreateScript() + ");";
        return createScript;
    }

    public DBOperation RecordOperation(T record)
    {
        return RecordOperation(null, record, "", "");
    }

    public DBOperation RecordOperation(Context context, T record)
    {
        return RecordOperation(context, record, "", "");
    }

    public DBOperation RecordOperation(Context context, T record, String SuccessMessage, String FailMessage)
    {
        List<Pair<String, String>> pairList = GetContentValues(record);
        int listSize = pairList.size();
        String query = "";
        Abstract_Table.SQLMode sqlMode = ((Abstract_Table) record).sqlMode;
        String primaryKeyField = GetPrimaryKey();
        switch (sqlMode)
        {
            case INSERT:
                query = "INSERT INTO " + GetTableName() + "(";
                String values = "";
                for (int i = 0; i < listSize; i++)
                {
                    Pair<String, String> pair = pairList.get(i);
                    if (pair.first.equals(primaryKeyField))
                    {
                        continue;
                    }
                    query += pair.first;
                    values += "'" + pair.second + "'";
                    if (i < listSize - 1)
                    {
                        query += ", ";
                        values += ", ";
                    }
                }
                query += ") VALUES(" + values + ")";
                break;
            case UPDATE:
            case DELETE:
                if (sqlMode == Abstract_Table.SQLMode.DELETE)
                {
                    query = "DELETE FROM " + GetTableName();
                }
                else if (sqlMode == Abstract_Table.SQLMode.UPDATE)
                {
                    query = "UPDATE " + GetTableName() + " SET ";
                    for (int i = 0; i < listSize; i++)
                    {
                        Pair<String, String> pair = pairList.get(i);
                        if (pair.first.equals(primaryKeyField))
                        {
                            continue;
                        }
                        query += pair.first + " = '" + pair.second + "'";
                        if (i < listSize - 1)
                        {
                            query += ", ";
                        }
                    }
                }
                query += " WHERE " + primaryKeyField + " = " + GetPrimaryKeyValue(record);
                break;
        }
        DBOperation operation = new DBOperation(sqlMode, query, SuccessMessage, FailMessage);
        operation = DatabaseManager.PerformOperation(context, operation);
        return operation;
    }

    public List<T> Select()
    {
        return Select(null);
    }

    public List<T> Select(T searchCriteria)
    {
        String whereClause = "";
        if (searchCriteria != null)
        {
            List<Pair<String, String>> pairList = GetContentValues(searchCriteria);
            int listSize = pairList.size();
            for (int i = 0; i < listSize; i++)
            {
                Pair<String, String> pair = pairList.get(i);
                if (pair.first != null && pair.second != null && !pair.first.isEmpty() && !pair.second.isEmpty() && !pair.second.equals("-1"))
                {
                    whereClause += "AND " + pair.first + " = '" + pair.second + "'";
                    if (i < listSize - 1)
                    {
                        whereClause += "\n";
                    }
                }
            }
            if (!whereClause.isEmpty())
            {
                whereClause = "WHERE " + whereClause;
                whereClause = whereClause.replace("WHERE AND ", "WHERE ");
                whereClause = whereClause.replace("''''", "''");
            }
        }
        String query = "SELECT * FROM " + GetTableName() + " " + whereClause;
        DBOperation operation = new DBOperation(Abstract_Table.SQLMode.SELECT, query);
        operation = DatabaseManager.PerformOperation(operation);
        List<JSONObject> JSONList = new ArrayList<JSONObject>();
        try
        {
            JSONArray jsonArray = new JSONArray(operation.JSONResult);
            for (int i = 0; i < jsonArray.length(); i++)
            {
                JSONList.add(jsonArray.getJSONObject(i));
            }
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
        List<T> resultList = GetList(JSONList);
        return resultList;
    }

    public long GetNextID()
    {
        String query = "SELECT AUTO_INCREMENT FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA = 'ICS' AND TABLE_NAME = '" + GetTableName() + "'";
        long id = 1;
        //Techincally this is a select but we can use insert to save having to decode any JSON
        DBOperation operation = new DBOperation(Abstract_Table.SQLMode.INSERT, query);
        operation = DatabaseManager.PerformOperation(operation);
        id = operation.ResultID + 1;
        return id;
    }

    protected abstract String GetPrimaryKey();

    protected abstract String GetPrimaryKeyValue(T record);

    protected abstract String GetTableName();

    protected abstract String GetCreateScript();

    protected abstract List<T> GetList(List<JSONObject> JSONList);

    protected abstract List<Pair<String, String>> GetContentValues(T record);
}
