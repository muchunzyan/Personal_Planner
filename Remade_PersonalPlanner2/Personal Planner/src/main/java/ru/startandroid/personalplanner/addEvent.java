package ru.startandroid.personalplanner;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class addEvent extends AppCompatActivity implements OnClickListener {

    String savedTextD;
    String savedTextM;
    String savedTextY;
    String savedTextHourB, savedTextMinB;
    String savedTextHourE, savedTextMinE;
    String savedTextN;
    String duration = "1", durationB, durationE;

    TextView tvName, tvDate, tvTimeB, tvTimeE, btnSave, TextD, TextTB, TextTE, btnPodobratVremya;
    EditText editTextN;
    ConstraintLayout CLay;

    DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);

        btnSave = findViewById(R.id.btnSave);
        tvName = findViewById(R.id.tvName);
        tvDate = findViewById(R.id.tvDate);
        tvTimeB = findViewById(R.id.tvTimeB);
        tvTimeE = findViewById(R.id.tvTimeE);
        editTextN = findViewById(R.id.editTextN);
        TextTB = findViewById(R.id.TextTB);
        TextTE = findViewById(R.id.TextTE);
        TextD = findViewById(R.id.TextD);
        btnPodobratVremya = findViewById(R.id.btnPodobratVremya);
        CLay = findViewById(R.id.CLay);

        btnSave.setOnClickListener(this);
        tvDate.setOnClickListener(this);
        tvTimeB.setOnClickListener(this);
        tvTimeE.setOnClickListener(this);
        btnPodobratVremya.setOnClickListener(this);

        dbHelper = new DBHelper(this);

        loadText();
    }

    @Override
    public void onClick(View v) {

        ContentValues cv = new ContentValues();

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        switch (v.getId()) {
            case R.id.btnSave:

                String name = editTextN.getText().toString();
                String date = TextD.getText().toString();
                String timeB = TextTB.getText().toString();
                String timeE = TextTE.getText().toString();

                if(name.equals("") || date.equals("") || timeB.equals("") || timeE.equals("")){
                    Toast toast = Toast.makeText(getApplicationContext(),
                            "Вы заполнили не все поля", Toast.LENGTH_SHORT);
                    toast.show();
                }
                else if (Integer.parseInt(duration) <= 0){
                    Toast toast = Toast.makeText(getApplicationContext(),
                            "Данные введены не корректно", Toast.LENGTH_SHORT);
                    toast.show();
                }

                if (name != "" && date != "" && timeB != "" && timeE != "") {
                    duration = Integer.toString((Integer.parseInt(savedTextHourE) * 60 + Integer.parseInt(savedTextMinE)) - (Integer.parseInt(savedTextHourB) * 60 + Integer.parseInt(savedTextMinB)));
                    durationB = Integer.toString(Integer.parseInt(savedTextHourB) * 60 + Integer.parseInt(savedTextMinB));
                    durationE = Integer.toString(Integer.parseInt(savedTextHourE) * 60 + Integer.parseInt(savedTextMinE));
                }

                if (!name.equals("") && !date.equals("") && !timeB.equals("") && !timeE.equals("") && Integer.parseInt(duration) > 0 && Integer.parseInt(durationB) >= 0 && Integer.parseInt(durationE) > 0){
                    cv.put("name", name);
                    cv.put("day", savedTextD);
                    cv.put("month", savedTextM);
                    cv.put("year", savedTextY);
                    cv.put("hourB", savedTextHourB);
                    cv.put("minuteB", savedTextMinB);
                    cv.put("hourE", savedTextHourE);
                    cv.put("minuteE", savedTextMinE);
                    cv.put("duration", duration);
                    cv.put("durationB", durationB);
                    cv.put("durationE", durationE);

                    db.insert("events", null, cv);

                    Intent kalendar = new Intent(this, TestCalendar.class);
                    startActivity(kalendar);

                    Toast toast = Toast.makeText(getApplicationContext(),
                            "Событие сохранено", Toast.LENGTH_SHORT);
                    toast.show();
                }
                break;

            case R.id.tvDate:
                Intent date_picker_Day = new Intent(this, date_picker_Day.class);
                savedTextN = editTextN.getText().toString();
                date_picker_Day.putExtra("name", savedTextN);
                date_picker_Day.putExtra("hourB", savedTextHourB);
                date_picker_Day.putExtra("minuteB", savedTextMinB);
                date_picker_Day.putExtra("hourE", savedTextHourE);
                date_picker_Day.putExtra("minuteE", savedTextMinE);
                startActivity(date_picker_Day);
                break;

            case R.id.tvTimeB:
                Intent time_pickerB = new Intent(this, time_picker.class);
                savedTextN = editTextN.getText().toString();
                time_pickerB.putExtra("name", savedTextN);
                time_pickerB.putExtra("day", savedTextD);
                time_pickerB.putExtra("month", savedTextM);
                time_pickerB.putExtra("year", savedTextY);
                time_pickerB.putExtra("hourE", savedTextHourE);
                time_pickerB.putExtra("minuteE", savedTextMinE);
                time_pickerB.putExtra("hourB", savedTextHourB);
                time_pickerB.putExtra("minuteB", savedTextMinB);
                startActivity(time_pickerB);
                break;

            case R.id.tvTimeE:
                Intent time_pickerE = new Intent(this, time_pickerE.class);
                savedTextN = editTextN.getText().toString();
                time_pickerE.putExtra("name", savedTextN);
                time_pickerE.putExtra("day", savedTextD);
                time_pickerE.putExtra("month", savedTextM);
                time_pickerE.putExtra("year", savedTextY);
                time_pickerE.putExtra("hourB", savedTextHourB);
                time_pickerE.putExtra("minuteB", savedTextMinB);
                time_pickerE.putExtra("hourE", savedTextHourE);
                time_pickerE.putExtra("minuteE", savedTextMinE);
                startActivity(time_pickerE);
                break;
            case R.id.btnPodobratVremya:
                Intent podobrat_vremya = new Intent(this, podobrat_vremya.class);
                startActivity(podobrat_vremya);
                break;
        }

        db.close();
    }

    void loadText() {
        Intent intent = getIntent();
        savedTextN = intent.getStringExtra("name");
        savedTextD = intent.getStringExtra("day");
        savedTextM = intent.getStringExtra("month");
        savedTextY = intent.getStringExtra("year");
        savedTextHourB = intent.getStringExtra("hourB");
        savedTextMinB = intent.getStringExtra("minuteB");
        savedTextHourE = intent.getStringExtra("hourE");
        savedTextMinE = intent.getStringExtra("minuteE");

        if (savedTextN != null)
            editTextN.setText(savedTextN);

        if (savedTextD != null) {
            TextD.setText(savedTextD + "/" + savedTextM + "/" + savedTextY);
        }
        if (savedTextHourB != null){
            TextTB.setText(savedTextHourB + ":" + savedTextMinB);
        }
        if (savedTextHourE != null){
            TextTE.setText(savedTextHourE + ":" + savedTextMinE);
        }
    }
}