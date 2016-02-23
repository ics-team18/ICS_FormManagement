package com.example.kyle.myapplication.CustomControls;

import android.content.Context;
import android.util.Pair;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.kyle.myapplication.Database.Abstract_Table;
import com.example.kyle.myapplication.Database.Tbl_Personnel;

import java.lang.Object;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by Kyle on 2/2/2016.
 */
public class CustomGridAdapter extends BaseAdapter
{
    Context context;
    List<Abstract_Table> valueList = new ArrayList<Abstract_Table>();

    public CustomGridAdapter(Context context, List<Abstract_Table> valueList)
    {
        this.context = context;
        Collections.sort(valueList, new Comparator<Abstract_Table>()
        {
            @Override
            public int compare(Abstract_Table a, Abstract_Table b)
            {
                return a.getDataGridDisplayValue().compareToIgnoreCase(b.getDataGridDisplayValue());
            }
        });
        this.valueList = valueList;
    }

    @Override
    public int getCount()
    {
        return valueList.size();
    }

    @Override
    public Object getItem(int position)
    {
        return valueList.get(position);
    }

    @Override
    public long getItemId(int position)
    {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        TextView lblName = new TextView(context);
        lblName.setTextSize(30);
        lblName.setText(valueList.get(position).getDataGridDisplayValue());
        return lblName;
    }
}
