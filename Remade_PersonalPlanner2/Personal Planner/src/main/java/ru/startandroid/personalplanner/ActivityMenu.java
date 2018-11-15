package ru.startandroid.personalplanner;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class ActivityMenu extends AppCompatActivity implements OnClickListener {

    Button btnAbout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        btnAbout = findViewById(R.id.btnAbout);

        btnAbout.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnAbout:
                Intent ActivityAbout = new Intent(this, ActivityAbout.class);
                startActivity(ActivityAbout);
                break;
        }
    }
}