package ru.startandroid.personalplanner;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TimePicker;

public class time_picker extends AppCompatActivity implements View.OnClickListener{

    Button btnGotovoT;
    TimePicker timePicker;

    String savedTextN;
    String savedTextD, savedTextM, savedTextY;
    String savedTextHourE, savedTextMinE;
    String savedTextHourB, savedTextMinB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_picker);

        btnGotovoT = findViewById(R.id.btnGotovoT);
        timePicker = findViewById(R.id.timePicker);

        btnGotovoT.setOnClickListener(this);

        loadTexet();
    }

    void loadTexet(){
        Intent intent = getIntent();
        savedTextN = intent.getStringExtra("name");
        savedTextD = intent.getStringExtra("day");
        savedTextM = intent.getStringExtra("month");
        savedTextY = intent.getStringExtra("year");
        savedTextHourB = intent.getStringExtra("hourB");
        savedTextMinB = intent.getStringExtra("minuteB");
        savedTextHourE = intent.getStringExtra("hourE");
        savedTextMinE = intent.getStringExtra("minuteE");
    }

    void saveText() {
        Intent intent = new Intent(this, addEvent.class);
        intent.putExtra("name", savedTextN);
        intent.putExtra("day", savedTextD);
        intent.putExtra("month", savedTextM);
        intent.putExtra("year", savedTextY);
        intent.putExtra("hourE", savedTextHourE);
        intent.putExtra("minuteE", savedTextMinE);
        intent.putExtra("hourB", Integer.toString(timePicker.getHour()));
        intent.putExtra("minuteB", Integer.toString(timePicker.getMinute()));
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnGotovoT:
                saveText();
                break;
        }
    }
}