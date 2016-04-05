package com.example.kyle.myapplication.Helpers;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.concurrent.ExecutionException;

/**
 * Created by Kyle on 4/3/2016.
 */

public class FTPManager extends AsyncTask<Void, Void, Boolean>
{
    public enum FTPMode
    {
        UPLOAD,
        DOWNLOAD,
    }

    public enum FileType
    {
        TEMPLATE,
        SUBMITTEDFORM,
    }

    private String IP = "192.168.0.15";
    private String userName = "ICSFormsDev";
    private String password = "CEG4981";
    private String filePathAndName = "";
    private String fileName = "";
    private FTPMode ftpMode;
    private FileType fileType;
    private Context context;
    private String ResultMessage = "";

    public static Boolean UploadDownloadFile(Context context, FTPMode ftpMode, FileType fileType, String filePathAndName, String fileName)
    {
        FTPManager ftpManager = new FTPManager(context, ftpMode, fileType, filePathAndName, fileName);
        Boolean result = false;
        try
        {
            result = ftpManager.execute().get();
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
        catch (ExecutionException e)
        {
            e.printStackTrace();
        }
        return result;
    }

    private FTPManager(Context context, FTPMode ftpMode, FileType fileType, String filePathAndName, String fileName)
    {
        this.context = context;
        this.ftpMode = ftpMode;
        this.fileType = fileType;
        this.filePathAndName = filePathAndName;
        this.fileName = fileName;
    }

    protected void onPreExecute()
    {

    }

    @Override
    protected Boolean doInBackground(Void... params)
    {
        Boolean result = false;
        try
        {
            fileName = "/" + (fileType == FileType.TEMPLATE ? "Templates" : "SubmittedForms") + "/" + fileName;
            if (ftpMode == FTPMode.UPLOAD)
            {
                FTPClient con = new FTPClient();
                con.connect(IP);
                if (con.login(userName, password))
                {
                    con.enterLocalPassiveMode(); // important!
                    con.setFileType(FTP.BINARY_FILE_TYPE);
                    FileInputStream in = new FileInputStream(new File(filePathAndName));
                    result = con.storeFile(fileName, in);
                    ResultMessage = result ? "File successfully uploaded" : "Failed to upload file";
                    in.close();
                    con.logout();
                    con.disconnect();
                }
            }
            else
            {
                FTPClient con = new FTPClient();
                con.connect(IP);
                if (con.login(userName, password))
                {
                    con.enterLocalPassiveMode(); // important!
                    con.setFileType(FTP.BINARY_FILE_TYPE);
                    OutputStream out = new FileOutputStream(new File(filePathAndName));
                    result = con.retrieveFile(fileName, out);
                    ResultMessage = result ? "File successfully downloaded" : "Failed to download file";
                    out.close();
                    con.logout();
                    con.disconnect();
                }
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
        return result;
    }

    @Override
    protected void onPostExecute(Boolean result)
    {
        if (context != null && !ResultMessage.isEmpty())
        {
            Toast.makeText(context, ResultMessage, Toast.LENGTH_LONG).show();
        }
    }
}