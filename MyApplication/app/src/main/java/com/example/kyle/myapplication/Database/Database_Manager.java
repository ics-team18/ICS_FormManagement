package com.example.kyle.myapplication.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.kyle.myapplication.Database.Incident.Tbl_Incident_Manager;
import com.example.kyle.myapplication.Database.IncidentLink.Tbl_IncidentLink_Manager;
import com.example.kyle.myapplication.Database.Personnel.Tbl_Personnel_Manager;
import com.example.kyle.myapplication.Database.Role.Tbl_Role_Manager;
import com.example.kyle.myapplication.Database.SubmittedForms.Tbl_SubmittedForms_Manager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kyle on 1/31/2016.
 */
public class Database_Manager extends SQLiteOpenHelper
{
    public static final String databaseName = "ICS.db";

    public Database_Manager(Context context)
    {
        super(context, databaseName, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        Tbl_Personnel_Manager.current.Create(db);
        Tbl_Role_Manager.current.Create(db);
        Tbl_Incident_Manager.current.Create(db);
        Tbl_IncidentLink_Manager.current.Create(db);
        Tbl_SubmittedForms_Manager.current.Create(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        List<String> tableList = new ArrayList<String>();
        tableList.add(Tbl_Personnel_Manager.current.GetTableName());
        tableList.add(Tbl_Role_Manager.current.GetTableName());
        tableList.add(Tbl_Incident_Manager.current.GetTableName());
        tableList.add(Tbl_IncidentLink_Manager.current.GetTableName());
        tableList.add(Tbl_SubmittedForms_Manager.current.GetTableName());
        for (String table : tableList)
        {
            db.execSQL("DROP TABLE IF EXISTS " + table);
        }
        onCreate(db);
    }

}
