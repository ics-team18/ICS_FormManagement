package com.example.kyle.myapplication.Database;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.concurrent.ExecutionException;

public class DatabaseManager extends AsyncTask<Void, Void, DBOperation>
{
    private String URL = "http://71.79.36.3/ICSFormsManagement/";
    private Context context;
    private DBOperation operation;
    private String phpFile;

    public static void CreateDatabase(Context context, DBOperation operation)
    {
        PerformOperation(context, null, "CreateDB.php");
        PerformOperation(context, operation);
    }

    public static DBOperation PerformOperation(DBOperation operation)
    {
        return PerformOperation(null, operation, "DBOperation.php");
    }

    public static DBOperation PerformOperation(Context context, DBOperation operation)
    {
        return PerformOperation(context, operation, "DBOperation.php");
    }

    private static DBOperation PerformOperation(Context context, DBOperation operation, String phpFile)
    {
        try
        {
            DatabaseManager dbManager = new DatabaseManager(context, operation, phpFile);
            operation = dbManager.execute().get();
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
        catch (ExecutionException e)
        {
            e.printStackTrace();
        }
        return operation;
    }

    DatabaseManager(Context context, DBOperation operation, String phpFile)
    {
        this.context = context;
        this.operation = operation;
        this.phpFile = phpFile;
    }

    protected void onPreExecute()
    {

    }

    @Override
    protected DBOperation doInBackground(Void... params)
    {
        try
        {
            String theURL = URL + phpFile;

            URL url = new URL(theURL);
            URLConnection conn = url.openConnection();
            conn.setDoOutput(true);
            if (operation != null && !operation.Operation.isEmpty())
            {
                OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
                String op = URLEncoder.encode("operation", "UTF-8") + "=" + URLEncoder.encode(operation.Operation, "UTF-8") +
                        "&" + URLEncoder.encode("mode", "UTF-8") + "=" + URLEncoder.encode(operation.SQLMode.name(), "UTF-8");
                wr.write(op);
                wr.flush();
            }
            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line = "";
            String result = "";
            boolean firstLine = true;
            while ((line = reader.readLine()) != null)
            {
                if (firstLine)
                {
                    result = line;
                    firstLine = false;
                }
                sb.append(line);
                break;
            }

            operation.ResultMessage = sb.toString();
            if (result.equals("FAIL"))
            {
                operation.ResultMessage = operation.FailMessage;
            }
            else
            {
                switch (operation.SQLMode)
                {
                    case INSERT:
                        operation.ResultID = Long.parseLong(operation.ResultMessage);
                        operation.AffectedRows = 1;
                        break;
                    case UPDATE:
                    case DELETE:
                        operation.AffectedRows = Integer.parseInt(operation.ResultMessage);
                        break;
                    case SELECT:
                        operation.JSONResult = operation.ResultMessage;
                        break;
                }
                operation.ResultMessage = operation.SuccessMessage;
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
        return operation;
    }

    @Override
    protected void onPostExecute(DBOperation operation)
    {
        if (context != null && operation != null && !operation.ResultMessage.isEmpty())
        {
            Toast.makeText(context, operation.ResultMessage, Toast.LENGTH_LONG).show();
        }
    }
}