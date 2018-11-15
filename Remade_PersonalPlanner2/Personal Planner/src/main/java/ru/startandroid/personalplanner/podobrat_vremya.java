package ru.startandroid.personalplanner;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class podobrat_vremya extends AppCompatActivity implements View.OnClickListener {

    EditText editTextDur;
    TextView tvDuration, tvDate, TextD, btnPodobrat, tvDurs;
    TextView btnGoToaddEvent;
    String savedTextDPod, savedTextMPod, savedTextYPod;
    String savedTextDurPod;
    DBHelper dbHelper;

    int i = 0, j = 1, c = 0;
    int Hour, Minute;

    int DurId[];
    {DurId = new int[100000];}
    int Dur[];
    {Dur = new int[100000];}
    int DurB[];
    {DurB = new int[100000];}
    int DurE[];
    {DurE = new int[100000];}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_podobrat_vremya);

        editTextDur = findViewById(R.id.editTextDur);
        tvDate = findViewById(R.id.tvDate);
        TextD = findViewById(R.id.TextD);
        tvDuration = findViewById(R.id.tvDuration);
        btnPodobrat = findViewById(R.id.btnPodobrat);
        tvDurs = findViewById(R.id.tvDurs);

        btnGoToaddEvent = findViewById(R.id.btnGoToaddEvent);

        tvDate.setOnClickListener(this);
        btnPodobrat.setOnClickListener(this);
        btnGoToaddEvent.setOnClickListener(this);

        loadText();

        if (savedTextDurPod != null) {
            editTextDur.setText(savedTextDurPod);
        }

        if (savedTextDPod != null && savedTextMPod != null && savedTextYPod != null) {
            TextD.setText(savedTextDPod + "/" + savedTextMPod + "/" + savedTextYPod);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvDate:
                saveText();
                break;
            case R.id.btnPodobrat:
                podobrat();
                break;
            case R.id.btnGoToaddEvent:
                Intent addEvent = new Intent(this, addEvent.class);
                startActivity(addEvent);
                break;
        }
    }

    void loadText() {
        Intent intent = getIntent();
        savedTextDPod = intent.getStringExtra("dayPod");
        savedTextMPod = intent.getStringExtra("monthPod");
        savedTextYPod = intent.getStringExtra("yearPod");
        savedTextDurPod = intent.getStringExtra("durationPod");
    }

    void saveText() {
        Intent date_picker_Day = new Intent(this, date_picker_podobrat.class);
        date_picker_Day.putExtra("durationPod", editTextDur.getText().toString());
        startActivity(date_picker_Day);
    }

    void podobrat(){

        dbHelper = new DBHelper(this);
        SQLiteDatabase db1 = dbHelper.getWritableDatabase();

        String selection1 = null;
        String[] selectionArgs1 = null;

        selection1 = "day == ? AND month == ? AND year == ?";
        selectionArgs1 = new String[] { savedTextDPod, savedTextMPod, savedTextYPod };

        if (savedTextDPod != null && savedTextMPod != null && savedTextYPod != null && !editTextDur.getText().toString().equals("В минутах") && !editTextDur.getText().toString().equals("")) {

            Cursor c1 = db1.query("events", null, selection1, selectionArgs1, null, null, "durationB");

            if (c1.moveToFirst()) {
                do {
                    i++;
                } while (c1.moveToNext());
            }
            c1.close();


            dbHelper = new DBHelper(this);
            SQLiteDatabase db2 = dbHelper.getWritableDatabase();

            String selection2 = null;
            String[] selectionArgs2 = null;

            selection2 = "day == ? AND month == ? AND year == ?";
            selectionArgs2 = new String[]{savedTextDPod, savedTextMPod, savedTextYPod};

            Cursor c2 = db2.query("events", null, selection2, selectionArgs2, null, null, "durationB");

            if (c2.moveToFirst()) {
                int idIndex = c2.getColumnIndex("id");
                int durationIndex = c2.getColumnIndex("duration");
                int durationBIndex = c2.getColumnIndex("durationB");
                int durationEIndex = c2.getColumnIndex("durationE");

                do {
                    DurId[j] = Integer.parseInt(c2.getString(idIndex));
                    Dur[j] = Integer.parseInt(c2.getString(durationIndex));
                    DurB[j] = Integer.parseInt(c2.getString(durationBIndex));
                    DurE[j] = Integer.parseInt(c2.getString(durationEIndex));

                    j++;

                } while (c2.moveToNext());
            }
            c2.close();

            for (int a = 1; a <= i; a++) {
                if (a == 1) {
                    if (DurB[1] >= Integer.parseInt(editTextDur.getText().toString())) {
                        Hour = 0;
                        Minute = 0;
                        tvDurs.setText("Подходящее время:" + "\n" + "часы:" + Integer.toString(Hour) + "\n" + "минуты:" + Integer.toString(Minute));
                        c = 1;
                    }
                }
                if (a != 1) {
                    if (DurB[a] - DurE[a - 1] >= Integer.parseInt(editTextDur.getText().toString())) {
                        Hour = DurE[a - 1] / 60;
                        Minute = DurE[a - 1] % 60;
                        if (c != 1) {
                            tvDurs.setText("Подходящее время:" + "\n" + "часы:" + Integer.toString(Hour) + "\n" + "минуты:" + Integer.toString(Minute));
                            c = 1;
                        }
                    }
                }
                if (a == i) {
                    if (1440 - DurE[a] >= Integer.parseInt(editTextDur.getText().toString())) {
                        Hour = DurE[a] / 60;
                        Minute = DurE[a] % 60;
                        if (c != 1) {
                            tvDurs.setText("Подходящее время:" + "\n" + "часы:" + Integer.toString(Hour) + "\n" + "минуты:" + Integer.toString(Minute));
                            c = 1;
                        }
                    }
                }
            }
            if (i == 0) {
                Hour = 0;
                Minute = 0;
                tvDurs.setText("Подходящее время:" + "\n" + "часы:" + Integer.toString(Hour) + "\n" + "минуты:" + Integer.toString(Minute));
            }
        }else {
            Toast toast = Toast.makeText(getApplicationContext(),
                    "Вы заполнили не все поля", Toast.LENGTH_SHORT);
            toast.show();
        }
    }
}