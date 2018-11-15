package ru.startandroid.personalplanner;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Point;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.database.sqlite.SQLiteDatabase;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Calendar;

public class TestCalendar extends AppCompatActivity implements OnClickListener {

    ConstraintLayout MyLay;
    ConstraintLayout MainLay;
    TextView tvTextCalendar, tvMonth, tvLeft, tvRight, tvYear, button, btnClear;
    Button plus;

    int n = 0;
    int d = 0;
    int mont = 0;
    String daye, monthe, yeare;

    DBHelper dbHelper;

    String months[];
    {
        months = new String[12];
        months[0] = "Январь";
        months[1] = "Февраль";
        months[2] = "Март";
        months[3] = "Апрель";
        months[4] = "Май";
        months[5] = "Июнь";
        months[6] = "Июль";
        months[7] = "Август";
        months[8] = "Сентябрь";
        months[9] = "Октябрь";
        months[10] = "Ноябрь";
        months[11] = "Декабрь";
    }

    String daysofweek[];
    {
        daysofweek = new String[7];
        daysofweek[0] = "Пн";
        daysofweek[1] = "Вт";
        daysofweek[2] = "Ср";
        daysofweek[3] = "Чт";
        daysofweek[4] = "Пт";
        daysofweek[5] = "Сб";
        daysofweek[6] = "Вс";
    }

    Calendar cal = Calendar.getInstance();
    int year = cal.get(Calendar.YEAR);
    int month = cal.get(Calendar.MONTH);
    int day = cal.get(Calendar.DAY_OF_MONTH);
    int weekday = cal.get(Calendar.DAY_OF_WEEK);


    @Override
    protected void onRestart() {
        super.onRestart();
        redraw_calendar();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        redraw_calendar();
    }

    void clear_calendar() {
        MyLay = findViewById(R.id.MyLay);
        MyLay.removeAllViews();
    }

    void redraw_calendar() {
        setContentView(R.layout.activity_test_calendar);
        clear_calendar();

        MyLay = findViewById(R.id.MyLay);
        MainLay = findViewById(R.id.MainLay);
        tvTextCalendar = findViewById(R.id.tvTextCalendar);
        tvMonth = findViewById(R.id.tvMonth);
        tvLeft = findViewById(R.id.tvLeft);
        tvRight = findViewById(R.id.tvRight);
        tvYear = findViewById(R.id.tvYear);
        btnClear = findViewById(R.id.btnClear);
        plus = findViewById(R.id.plus);

        int m = 1;
        int i = 1;
        int w = 0;
        int daysInMonth = cal.getActualMaximum(Calendar.DAY_OF_MONTH);

        mont = cal.get(Calendar.MONTH);

        cal.set(Calendar.DAY_OF_MONTH, 1);
        weekday = cal.get(Calendar.DAY_OF_WEEK);

        if (weekday == 1)
            n = 6;
        else
            n = weekday - 2;

        tvYear.setText(Integer.toString(year));
        tvMonth.setText(months[month]);

        tvRight.setOnClickListener(this);
        tvLeft.setOnClickListener(this);
        plus.setOnClickListener(this);
        btnClear.setOnClickListener(this);

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int ScreenWidth = size.x;
        int ScreenHeight = size.y;

        tvTextCalendar.getLayoutParams().height = ScreenHeight / 12;

        plus.getLayoutParams().height = ScreenWidth / 6;
        plus.getLayoutParams().width = ScreenWidth / 6;

        tvMonth.getLayoutParams().height = ScreenHeight / 12;
        tvMonth.getLayoutParams().width = (ScreenWidth / 6) * 2;

        tvYear.getLayoutParams().height = ScreenHeight / 12;
        tvYear.getLayoutParams().width = (ScreenWidth / 6) * 2;

        tvLeft.getLayoutParams().width = (ScreenWidth / 6);
        tvLeft.getLayoutParams().height = ScreenHeight / 12;

        tvRight.getLayoutParams().width = (ScreenWidth / 6);
        tvRight.getLayoutParams().height = ScreenHeight / 12;

        MyLay.getLayoutParams().height = ((ScreenHeight / 12) * 7);

        while (i <= daysInMonth) {

            final TextView btn = new TextView(this);
            btn.setId(i);
            btn.setText(Integer.toString(i));
            btn.setTextColor(getResources().getColor(R.color.White));
            btn.setGravity(Gravity.CENTER);
            btn.setWidth(ScreenWidth / 9);
            btn.setHeight(ScreenWidth / 9);
            btn.setBackgroundResource(R.drawable.button_size);
            btn.setX(n * (ScreenWidth / 7) + 20);
            btn.setY(m * (ScreenWidth / 7) + 20);
            daye = Integer.toString(i);
            btn.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v){
                    daye = (String) btn.getText();
                    writeFile();
                    Intent Events = new Intent(TestCalendar.this, Events.class);
                    startActivity(Events);
                }
            });


            dbHelper = new DBHelper(this);
            SQLiteDatabase db = dbHelper.getWritableDatabase();

            Cursor c = db.query("events", null, null, null, null, null, "day");

            if (c.moveToFirst()) {
                int dayIndex = c.getColumnIndex("day");
                int monthIndex = c.getColumnIndex("month");
                int yearIndex = c.getColumnIndex("year");

                do {
                    if(c.getString(dayIndex).equals(Integer.toString(i)) && months[Integer.parseInt(c.getString(monthIndex)) - 1].equals(tvMonth.getText()) && c.getString(yearIndex).equals(tvYear.getText()))
                        btn.setBackgroundResource(R.drawable.is_event);
                } while (c.moveToNext());
            } else {
                btn.setBackgroundResource(R.drawable.button_size);
            }
            c.close();

            MyLay.addView(btn);
            n++;

            if (n % 7 == 0) {
                m++;
                n = 0;
            }
            i++;
        }

        for (int q = 0; q < 7; q++) {
            TextView weekday = new TextView(this);
            weekday.setText(daysofweek[q]);
            weekday.setGravity(Gravity.CENTER);
            weekday.setTextColor(getResources().getColor(R.color.White));
            weekday.setBackground(getResources().getDrawable(R.drawable.button_size_daysofweek));
            weekday.setWidth(ScreenWidth / 9);
            weekday.setHeight(ScreenWidth / 9);
            weekday.setX(q * (ScreenWidth / 7) + 20);
            weekday.setY(w * (ScreenWidth / 7) + 20);
            MyLay.addView(weekday);
        }

        if (d == 0) {
            button = findViewById(day);
            button.setBackground(getResources().getDrawable(R.drawable.button_size_day));
        }

        monthe = (String) tvMonth.getText();
        yeare = (String) tvYear.getText();
    }

    @Override
    public void onClick(View v) {

        dbHelper = new DBHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        switch (v.getId()) {
            case R.id.btnClear:

                db.delete("events", null, null);

                Toast toast = Toast.makeText(getApplicationContext(),
                        "Все события удалены", Toast.LENGTH_SHORT);
                toast.show();

                redraw_calendar();
                break;

            case R.id.tvRight:
                cal.set(Calendar.MONTH, cal.get(Calendar.MONTH) + 1);
                month = cal.get(Calendar.MONTH);
                d++;
                mont++;
                if (mont == 12) {
                    year++;
                    mont = 0;
                    d++;
                    tvYear.setText(Integer.toString(year));
                    redraw_calendar();
                }
                tvMonth.setText(months[month]);
                weekday = cal.get(Calendar.DAY_OF_WEEK);
                redraw_calendar();
                break;

            case R.id.tvLeft:
                cal.set(Calendar.MONTH, cal.get(Calendar.MONTH) - 1);
                month = cal.get(Calendar.MONTH);
                d--;
                mont--;
                if (mont == -1) {
                    year--;
                    mont = 11;
                    d--;
                    tvYear.setText(Integer.toString(year));
                }
                tvMonth.setText(months[month]);
                weekday = cal.get(Calendar.DAY_OF_WEEK);
                redraw_calendar();
                break;

            case R.id.plus:
                Intent addEvent = new Intent(this, addEvent.class);
                startActivity(addEvent);
                break;
        }
    }

    void writeFile() {
        try {
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(
                    openFileOutput("daye", MODE_PRIVATE)));
            bw.write(daye);
            bw.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(
                    openFileOutput("monthe", MODE_PRIVATE)));
            bw.write(monthe);
            bw.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(
                    openFileOutput("yeare", MODE_PRIVATE)));
            bw.write(yeare);
            bw.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}