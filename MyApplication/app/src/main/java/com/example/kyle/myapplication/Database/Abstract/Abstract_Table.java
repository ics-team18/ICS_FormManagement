package com.example.kyle.myapplication.Database.Abstract;

/**
 * Created by Kyle on 1/31/2016.
 */
public abstract class Abstract_Table
{
    public enum SQLMode
    {
        NONE,
        CREATETTABLES,
        INSERT,
        UPDATE,
        DELETE,
        SELECT,
    }

    public SQLMode sqlMode = SQLMode.NONE;

    public abstract String isValidRecord();

    public abstract String getDataGridDisplayValue();

    public abstract String getDataGridPopupMessageValue();
}
