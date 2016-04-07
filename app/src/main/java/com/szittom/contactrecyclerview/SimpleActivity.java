package com.szittom.contactrecyclerview;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class SimpleActivity extends AppCompatActivity implements View.OnClickListener {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple);
        findViewById(R.id.btn_contact).setOnClickListener(this);
        findViewById(R.id.btn_city).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_contact:
                startActivity(new Intent(this,MainActivity.class));
                break;
            case R.id.btn_city:
                startActivity(new Intent(this,CityRxActvity.class));
                break;
        }
    }
}
