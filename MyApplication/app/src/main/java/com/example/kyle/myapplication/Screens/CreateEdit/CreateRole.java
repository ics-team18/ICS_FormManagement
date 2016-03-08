package com.example.kyle.myapplication.Screens.CreateEdit;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;

import com.example.kyle.myapplication.R;

public class CreateRole extends AppCompatActivity {

    public static boolean InEditMode = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_role);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }
}
