package ru.startandroid.personalplanner;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;

public class date_picker_Day extends AppCompatActivity implements View.OnClickListener {

    DatePicker datePicker_D;
    Button btnGotovoD;

    String savedTextHourB, savedTextMinB;
    String savedTextHourE, savedTextMinE;
    String savedTextN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_date_picker__day);

        datePicker_D = findViewById(R.id.datePicker_D);
        btnGotovoD = findViewById(R.id.btnGotovoD);

        btnGotovoD.setOnClickListener(this);

        loadText();
    }

    void loadText(){
        Intent intent = getIntent();
        savedTextN = intent.getStringExtra("name");
        savedTextHourB = intent.getStringExtra("hourB");
        savedTextMinB = intent.getStringExtra("minuteB");
        savedTextHourE = intent.getStringExtra("hourE");
        savedTextMinE = intent.getStringExtra("minuteE");
    }

    void saveText() {
        Intent intent = new Intent(this, addEvent.class);
        intent.putExtra("name", savedTextN);
        intent.putExtra("hourB", savedTextHourB);
        intent.putExtra("minuteB", savedTextMinB);
        intent.putExtra("hourE", savedTextHourE);
        intent.putExtra("minuteE", savedTextMinE);
        intent.putExtra("day", Integer.toString(datePicker_D.getDayOfMonth()));
        intent.putExtra("month", Integer.toString(datePicker_D.getMonth() + 1));
        intent.putExtra("year", Integer.toString(datePicker_D.getYear()));
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnGotovoD:
                saveText();
                break;
        }
    }
}