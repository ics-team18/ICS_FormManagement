package com.example.kyle.myapplication.Helpers;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.example.kyle.myapplication.Database.Abstract.Abstract_Table_Manager;
import com.example.kyle.myapplication.Database.Incident.Tbl_Incident;
import com.example.kyle.myapplication.Database.Personnel.Tbl_Personnel;
import com.example.kyle.myapplication.Database.Role.Tbl_Role;
import com.example.kyle.myapplication.Database.Templates.Tbl_Templates;
import com.example.kyle.myapplication.R;
import com.example.kyle.myapplication.Screens.CreateEdit.CreateEditIncident;
import com.example.kyle.myapplication.Screens.CreateEdit.CreateEditPersonnel;
import com.example.kyle.myapplication.Screens.CreateEdit.CreateEditRole;
import com.example.kyle.myapplication.Screens.DataList;
import com.example.kyle.myapplication.Screens.DownloadTemplate;
import com.example.kyle.myapplication.Screens.Login;
import com.example.kyle.myapplication.Screens.MainScreen;
import com.example.kyle.myapplication.Screens.CreateEdit.Manage_Data;
import com.example.kyle.myapplication.Screens.CreateEdit.CreateEditIncidentLinks;
import com.example.kyle.myapplication.Screens.CreateEdit.CreateEditTemplates;

/**
 * Created by Kyle on 2/27/2016.
 * <p/>
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

    public static void OpenDataListScreen(DataList.Mode mode, Abstract_Table_Manager.Table table)
    {
        DataList.mode = mode;
        DataList.SpecificTable = table;
        context.startActivity(new Intent(context, DataList.class));
    }

    public static void OpenCreatePersonnelScreen(Tbl_Personnel toUpdate, boolean fromLoginScreen)
    {
        CreateEditPersonnel.toUpdate = toUpdate;
        CreateEditPersonnel.fromLoginScreen = fromLoginScreen;
        context.startActivity(new Intent(context, CreateEditPersonnel.class));
    }

    public static void OpenCreateIncidentScreen(Tbl_Incident toUpdate)
    {
        CreateEditIncident.toUpdate = toUpdate;
        context.startActivity(new Intent(context, CreateEditIncident.class));
    }

    public static void OpenCreateRoleScreen(Tbl_Role toUpdate)
    {
        CreateEditRole.toUpdate = toUpdate;
        context.startActivity(new Intent(context, CreateEditRole.class));
    }

    public static void OpenDownloadTemplateScreen()
    {
        context.startActivity(new Intent(context, DownloadTemplate.class));
    }

    public static void OpenUploadTemplateScreen(Tbl_Templates toUpdate)
    {
        CreateEditTemplates.toUpdate = toUpdate;
        context.startActivity(new Intent(context, CreateEditTemplates.class));
    }

    public static void OpenSetupPersonnelScreen()
    {
        context.startActivity(new Intent(context, CreateEditIncidentLinks.class));
    }
}
