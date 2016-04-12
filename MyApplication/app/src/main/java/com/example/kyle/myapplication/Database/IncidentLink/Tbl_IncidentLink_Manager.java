package com.example.kyle.myapplication.Database.IncidentLink;

import android.util.Pair;
import com.example.kyle.myapplication.Database.Abstract.Abstract_Table_Manager;
import com.example.kyle.myapplication.Database.Incident.Tbl_Incident_Manager;
import com.example.kyle.myapplication.Database.Personnel.Tbl_Personnel;
import com.example.kyle.myapplication.Database.Personnel.Tbl_Personnel_Manager;
import com.example.kyle.myapplication.Database.Role.Tbl_Role;
import com.example.kyle.myapplication.Database.Role.Tbl_Role_Manager;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kyle on 3/8/2016.
 */
public class Tbl_IncidentLink_Manager extends Abstract_Table_Manager<Tbl_IncidentLink>
{
    public static Tbl_IncidentLink_Manager current = new Tbl_IncidentLink_Manager();

    public enum Attributes
    {
        INCIDENTLINKID,
        INCIDENTID,
        PERSONNELID,
        ROLEID,
        RANKING,
    }

    @Override
    public String GetPrimaryKey()
    {
        return Attributes.INCIDENTLINKID.name();
    }

    @Override
    public String GetTableName()
    {
        return Tbl_IncidentLink.class.getSimpleName();
    }

    @Override
    protected String GetCreateScript()
    {
        return Attributes.INCIDENTLINKID.name() + " INT(11) NOT NULL AUTO_INCREMENT,\n" +
                Attributes.INCIDENTID.name() + " INT(11) NOT NULL,\n" +
                Attributes.PERSONNELID.name() + " INT(11) NOT NULL,\n" +
                Attributes.ROLEID.name() + " INT(11) NOT NULL,\n" +
                Attributes.RANKING.name() + " INT(11) NOT NULL,\n" +
                "PRIMARY KEY (" + Attributes.INCIDENTLINKID.name() + "),\n" +
                "FOREIGN KEY(" + Attributes.INCIDENTID.name() + ") REFERENCES " + Tbl_Incident_Manager.current.GetTableName() + "(" + Tbl_Incident_Manager.Attributes.INCIDENTID.name() + "),\n" +
                "FOREIGN KEY(" + Attributes.PERSONNELID.name() + ") REFERENCES " + Tbl_Personnel_Manager.current.GetTableName() + "(" + Tbl_Personnel_Manager.Attributes.PERSONNELID.name() + "),\n" +
                "FOREIGN KEY(" + Attributes.ROLEID.name() + ") REFERENCES " + Tbl_Role_Manager.current.GetTableName() + "(" + Tbl_Role_Manager.Attributes.ROLEID.name() + ")\n";
    }

    @Override
    protected List<Pair<String, String>> GetContentValues(Tbl_IncidentLink record)
    {
        List<Pair<String, String>> values = new ArrayList<Pair<String, String>>();
        values.add(new Pair<String, String>(Attributes.INCIDENTLINKID.name(), Long.toString(record.incidentLinkID)));
        values.add(new Pair<String, String>(Attributes.INCIDENTID.name(), Long.toString(record.incidentID)));
        values.add(new Pair<String, String>(Attributes.PERSONNELID.name(), Long.toString(record.personnelID)));
        values.add(new Pair<String, String>(Attributes.ROLEID.name(), Long.toString(record.roleID)));
        values.add(new Pair<String, String>(Attributes.RANKING.name(), Long.toString(record.ranking)));
        return values;
    }

    @Override
    public String GetPrimaryKeyValue(Tbl_IncidentLink record)
    {
        return Long.toString(record.incidentLinkID);
    }

    @Override
    public List<Tbl_IncidentLink> GetList(List<JSONObject> JSONList)
    {
        List<Tbl_IncidentLink> resultList = new ArrayList<Tbl_IncidentLink>();
        try
        {
            for (JSONObject json : JSONList)
            {
                Tbl_IncidentLink record = new Tbl_IncidentLink();
                record.incidentLinkID = json.getLong(Attributes.INCIDENTLINKID.name());
                record.incidentID = json.getLong(Attributes.INCIDENTID.name());
                record.personnelID = json.getLong(Attributes.PERSONNELID.name());
                record.roleID = json.getLong(Attributes.ROLEID.name());
                record.ranking = json.getInt(Attributes.RANKING.name());
                //get the personnel for this link record
                record.personnel = Tbl_Personnel_Manager.current.Select(new Tbl_Personnel(record.personnelID)).get(0);
                //get the role for this link record
                record.role = Tbl_Role_Manager.current.Select(new Tbl_Role(record.roleID)).get(0);
                resultList.add(record);
            }
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
        return resultList;
    }
}
