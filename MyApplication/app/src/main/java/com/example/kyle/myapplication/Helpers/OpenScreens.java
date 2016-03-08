package com.example.kyle.myapplication.Helpers;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;

import com.example.kyle.myapplication.Database.Abstract_Table_Manager;
import com.example.kyle.myapplication.Database.Tbl_Incident;
import com.example.kyle.myapplication.Database.Tbl_Personnel;
import com.example.kyle.myapplication.Database.Tbl_Role;
import com.example.kyle.myapplication.R;
import com.example.kyle.myapplication.Screens.CreateEdit.CreateIncident;
import com.example.kyle.myapplication.Screens.CreateEdit.CreatePersonnel;
import com.example.kyle.myapplication.Screens.CreateEdit.CreateRole;
import com.example.kyle.myapplication.Screens.DataList;
import com.example.kyle.myapplication.Screens.Login;
import com.example.kyle.myapplication.Screens.MainScreen;
import com.example.kyle.myapplication.Screens.Manage_Data;

/**
 * Created by Kyle on 2/27/2016.
 *
 * You should use the static methods in this class to open new screens
 */
public class OpenScreens extends Activity
{
    private static Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        context = this;
        OpenLoginScreen();
    }

    public static void OpenLoginScreen()
    {
        context.startActivity(new Intent(context, Login.class));
    }

    public static void OpenMainScreen()
    {
        context.startActivity(new Intent(context, MainScreen.class));
    }

    public static void OpenManageDataScreen()
    {
        context.startActivity(new Intent(context, Manage_Data.class));
    }

    public static void OpenDataListScreen(boolean forEditing, Abstract_Table_Manager.Table table)
    {
        DataList.forEditing = forEditing;
        DataList.SpecificTable = table;
        context.startActivity(new Intent(context, DataList.class));
    }

    public static void OpenCreatePersonnelScreen(Tbl_Personnel toUpdate, boolean fromLoginScreen)
    {
        CreatePersonnel.toUpdate = toUpdate;
        CreatePersonnel.fromLoginScreen = fromLoginScreen;
        context.startActivity(new Intent(context, CreatePersonnel.class));
    }

    public static void OpenCreateIncidentScreen(Tbl_Incident toUpdate)
    {
        CreateIncident.toUpdate = toUpdate;
        context.startActivity(new Intent(context, CreateIncident.class));
    }

    public static void OpenCreateRoleScreen(Tbl_Role toUpdate)
    {
        //CreateRole.toUpdate = toUpdate;
        context.startActivity(new Intent(context, CreateRole.class));
    }
}
