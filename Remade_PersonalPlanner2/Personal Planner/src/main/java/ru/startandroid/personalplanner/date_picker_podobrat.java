package ru.startandroid.personalplanner;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;

public class date_picker_podobrat extends AppCompatActivity implements View.OnClickListener {

    DatePicker datePicker_D;
    Button btnGotovoD;

    String savedTextDurPod;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_date_picker_podobrat);

        datePicker_D = findViewById(R.id.datePicker_D);
        btnGotovoD = findViewById(R.id.btnGotovoD);

        btnGotovoD.setOnClickListener(this);

        loadText();
    }

    void loadText() {
        Intent intent = getIntent();
        savedTextDurPod = intent.getStringExtra("durationPod");
    }

    void saveTextPod() {
        Intent podobrat_vremya = new Intent(this, podobrat_vremya.class);
        podobrat_vremya.putExtra("dayPod", Integer.toString(datePicker_D.getDayOfMonth()));
        podobrat_vremya.putExtra("monthPod", Integer.toString(datePicker_D.getMonth() + 1));
        podobrat_vremya.putExtra("yearPod", Integer.toString(datePicker_D.getYear()));
        podobrat_vremya.putExtra("durationPod", savedTextDurPod);
        startActivity(podobrat_vremya);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnGotovoD:
                saveTextPod();
                break;
        }
    }
}