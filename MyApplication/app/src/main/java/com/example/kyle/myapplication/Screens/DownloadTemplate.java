package com.example.kyle.myapplication.Screens;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.kyle.myapplication.Database.Role.Tbl_Role;
import com.example.kyle.myapplication.Database.Role.Tbl_Role_Manager;
import com.example.kyle.myapplication.Database.Templates.Tbl_Templates;
import com.example.kyle.myapplication.Database.Templates.Tbl_Templates_Manager;
import com.example.kyle.myapplication.Helpers.FTPManager;
import com.example.kyle.myapplication.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class DownloadTemplate extends Activity
{

    private Spinner cboRole, cboTemplateDescription;
    private EditText txtFileName;
    private List<Tbl_Templates> masterTemplateList;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download_template);

        cboRole = (Spinner) findViewById(R.id.cboRole);
        cboTemplateDescription = (Spinner) findViewById(R.id.cboTemplateDescription);
        txtFileName = (EditText) findViewById(R.id.txtFileName);

        Button btnClear = (Button) findViewById(R.id.btnClear);
        btnClear.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                clear();
            }
        });

        Button btnDownload = (Button) findViewById(R.id.btnDownload);
        btnDownload.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                downloadTemplate();
            }
        });
        setSpinnerData();
    }

    @Override
    public void onStart()
    {
        super.onStart();
        cboRole.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id)
            {
                setTemplateSpinnerData();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView)
            {
                // your code here
            }
        });

        cboTemplateDescription.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id)
            {
                setFileName();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView)
            {
                // your code here
            }
        });
    }

    private void setSpinnerData()
    {
        List<Tbl_Role> roleList = Tbl_Role_Manager.current.Select();
        Collections.sort(roleList, new Comparator<Tbl_Role>()
        {
            @Override
            public int compare(Tbl_Role a, Tbl_Role b)
            {
                return a.toString().compareToIgnoreCase(b.toString());
            }
        });
        roleList.add(0, new Tbl_Role());
        cboRole.setAdapter(new ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, roleList));
        masterTemplateList = Tbl_Templates_Manager.current.Select();
    }

    private void setTemplateSpinnerData()
    {
        List<Tbl_Templates> templateList = new ArrayList<>();
        Tbl_Role role = (Tbl_Role) cboRole.getSelectedItem();
        if (role.roleID > -1)
        {
            for (Tbl_Templates template : masterTemplateList)
            {
                if (template.roleID == role.roleID)
                {
                    templateList.add(template);
                }
            }
            Collections.sort(templateList, new Comparator<Tbl_Templates>()
            {
                @Override
                public int compare(Tbl_Templates a, Tbl_Templates b)
                {
                    return a.toString().compareToIgnoreCase(b.toString());
                }
            });
            cboTemplateDescription.setEnabled(true);
        }
        else
        {
            cboTemplateDescription.setEnabled(false);
        }
        templateList.add(0, new Tbl_Templates());
        cboTemplateDescription.setAdapter(new ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, templateList));
    }

    private void setFileName()
    {
        Tbl_Templates selectedTemplate = (Tbl_Templates) cboTemplateDescription.getSelectedItem();
        txtFileName.setText(!selectedTemplate.fileName.isEmpty() ? selectedTemplate.fileName : "");
    }

    private void clear()
    {
        cboRole.setSelection(0);
        cboTemplateDescription.setSelection(0);
        txtFileName.setText("");
    }

    private void downloadTemplate()
    {
        Tbl_Role selectedRole = (Tbl_Role) cboRole.getSelectedItem();
        Tbl_Templates selectedTemplate = (Tbl_Templates) cboTemplateDescription.getSelectedItem();
        String fileName = txtFileName.getText().toString();
        if (!selectedRole.title.isEmpty() && !selectedTemplate.fileName.isEmpty() && !fileName.isEmpty())
        {
            String saveFile = "data/data/com.example.kyle.myapplication/files/" + fileName;
            Boolean result = FTPManager.UploadDownloadFile(DownloadTemplate.this, FTPManager.FTPMode.DOWNLOAD, FTPManager.FileType.TEMPLATE, saveFile, selectedTemplate.fileName);
            if (result)
            {
                clear();
            }
        }
        else
        {
            String anyErrors = "Please fix the following errors:";
            if (selectedRole.title.isEmpty())
            {
                anyErrors += "\nRole is a required field";
            }
            if (selectedTemplate.fileName.isEmpty())
            {
                anyErrors += "\nTemplate is a required field";
            }
            if (fileName.isEmpty())
            {
                anyErrors += "\nFile Name is a required field";
            }
            Toast.makeText(this, anyErrors, Toast.LENGTH_LONG).show();
        }
    }

    /*@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        Intent chooseFile;
        Intent intent;
        chooseFile = new Intent(Intent.ACTION_GET_CONTENT);
        chooseFile.setType("file/*");
        intent = Intent.createChooser(chooseFile, "Choose a file");
        startActivityForResult(intent, 1);
        if (resultCode == RESULT_OK)
        {
            if (requestCode == 1)
            {
                Uri uri = data.getData();
                String path = "fd";
                path += "hi";
            }
        }
    }*/
}
