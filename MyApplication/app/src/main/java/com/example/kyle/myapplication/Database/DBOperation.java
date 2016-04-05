package com.example.kyle.myapplication.Database;

import android.content.Context;

import com.example.kyle.myapplication.Database.Abstract.Abstract_Table;

import java.util.List;

/**
 * Created by Kyle on 4/2/2016.
 */
public class DBOperation
{
    public Abstract_Table.SQLMode SQLMode;
    public String Operation = "";
    public String SuccessMessage = "";
    public String FailMessage = "";
    public String ResultMessage = "";
    public String JSONResult = "";
    public long ResultID = -1;
    public int AffectedRows = -1;

    public boolean Success()
    {
        return ResultMessage.equals(SuccessMessage);
    }

    public DBOperation(Abstract_Table.SQLMode SQLMode, String Operation)
    {
        this(SQLMode, Operation, "", "");
    }

    public DBOperation(Abstract_Table.SQLMode SQLMode, String Operation, String SuccessMessage, String FailMessage)
    {
        this.SQLMode = SQLMode;
        this.Operation = Operation;
        this.SuccessMessage = SuccessMessage;
        this.FailMessage = FailMessage;
    }
}
