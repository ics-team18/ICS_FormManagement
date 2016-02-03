package com.example.kyle.myapplication.Database;

/**
 * Created by Kyle on 1/31/2016.
 */
public class Tbl_Role
{
    public int roleID = -1;
    public int ranking = -1;
    public String title = "";

    public Tbl_Role()
    {
    }

    public Tbl_Role(int ranking, String title)
    {
        this.ranking = ranking;
        this.title = title;
    }
}
