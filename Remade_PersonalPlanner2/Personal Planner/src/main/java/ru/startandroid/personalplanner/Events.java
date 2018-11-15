package ru.startandroid.personalplanner;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Point;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

public class Events extends AppCompatActivity implements View.OnClickListener{

    TextView textViewDate;
    TextView btnDeleteEve;
    LinearLayout linearLayE;
    ScrollView scrollViewEve;
    String daye, monthe, yeare;
    String tmonth, monthno;
    String id, chas, minuta;
    int e = 0;
    int count = 0;

    DBHelper dbHelper;

    @Override
    protected void onRestart() {
        super.onRestart();
        create();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        create();
    }

    void create(){
        setContentView(R.layout.activity_events);
        dbHelper = new DBHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int ScreenWidth = size.x;
        int ScreenHeight = size.y;

        textViewDate = findViewById(R.id.textViewDate);
        scrollViewEve = findViewById(R.id.scrollViewEve);
        linearLayE = findViewById(R.id.linearLayE);
        btnDeleteEve = findViewById(R.id.btnDeleteEve);

        ViewGroup.LayoutParams params = scrollViewEve.getLayoutParams();
        params.height = ScreenHeight - 550;
        scrollViewEve.setLayoutParams(params);

        readFile();
        changeMonth();

        textViewDate.setText(daye + " " + tmonth + " " + yeare);

        String selection = null;
        String[] selectionArgs = null;

        changeMonthnow();

        selection = "day == ? AND month == ? AND year == ?";
        selectionArgs = new String[] { daye, monthno, yeare };

        Cursor c = db.query("events", null, selection, selectionArgs, null, null, "hourB");

        if (c.moveToFirst()) {
            int idIndex = c.getColumnIndex("id");
            int nameIndex = c.getColumnIndex("name");
            int dayIndex = c.getColumnIndex("day");
            int monthIndex = c.getColumnIndex("month");
            int yearIndex = c.getColumnIndex("year");
            int hourBIndex = c.getColumnIndex("hourB");
            int minuteBIndex = c.getColumnIndex("minuteB");
            int hourEIndex = c.getColumnIndex("hourE");
            int minuteEIndex = c.getColumnIndex("minuteE");
            int durationIndex = c.getColumnIndex("duration");

            do {
                final TextView event = new TextView(this);
                event.setId(e);
                event.setTextSize(20);
                event.setTag(c.getString(idIndex));
                event.setBackground(getResources().getDrawable(R.drawable.events));
                event.setOnClickListener(new View.OnClickListener() {
                     public void onClick(View v) {

                         if (count % 2 == 0) {
                             ViewButton();

                             id = event.getTag().toString();
                         }
                         if (count % 2 == 1) {
                             UnViewButton();
                         }
                         count++;
                     }
                 });
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                layoutParams.setMargins(70, 100, 70, 0);
                event.setPadding(40, 40,40,40);
                event.setLayoutParams(layoutParams);
                linearLayE.addView(event);
                e++;

                int ch = Integer.parseInt(c.getString(durationIndex)) / 60;

                if (ch % 10 == 1)
                    chas = " час ";
                if (ch % 10 == 2 || ch % 10 == 3 || ch % 10 == 4)
                    chas = " часа ";
                if (ch % 10 > 4 || ch % 10 == 0)
                    chas = " часов ";

                int mi = Integer.parseInt(c.getString(durationIndex)) % 60;

                if (mi % 10 == 1)
                    minuta = " минута ";
                if (mi % 10 == 2 || mi % 10 == 3 || mi % 10 == 4)
                    minuta = " минуты ";
                if (mi % 10 > 4 || mi % 10 == 0)
                    minuta = " минут ";

                event.setText(
                        "Название: " + c.getString(nameIndex) + "\n" +
                                "Дата: " + c.getString(dayIndex) + "/" + c.getString(monthIndex) + "/" + c.getString(yearIndex) + "\n" +
                                "Время начала: " + c.getString(hourBIndex) + ":" + c.getString(minuteBIndex) + "\n" +
                                "Время конца: " + c.getString(hourEIndex) + ":" + c.getString(minuteEIndex) + "\n" +
                                "Продолжительность: " + Integer.toString(Integer.parseInt(c.getString(durationIndex)) / 60) + chas + Integer.toString(Integer.parseInt(c.getString(durationIndex)) % 60) + minuta);
            } while (c.moveToNext());
        } else {
            TextView noevents = new TextView(this);
            noevents.setText("Нет событий");
            noevents.setTextSize(20);
            noevents.setBackground(getResources().getDrawable(R.drawable.events));
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(70, 100, 0, 0);
            noevents.setPadding(40, 40,40,40);
            noevents.setLayoutParams(layoutParams );
            linearLayE.addView(noevents);
        }

        TextView space = new TextView(this);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, 50);
        space.setLayoutParams(layoutParams);
        linearLayE .addView(space);

        c.close();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnDeleteEve:
                dbHelper = new DBHelper(this);
                SQLiteDatabase db = dbHelper.getWritableDatabase();

                db.delete("events", "id = " + id, null);
                create();

                Toast toast = Toast.makeText(getApplicationContext(),
                        "Событие удалено", Toast.LENGTH_SHORT);
                toast.show();
                break;
        }
    }

    void readFile() {
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(
                    openFileInput("daye")));
            daye = br.readLine();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(
                    openFileInput("monthe")));
            monthe = br.readLine();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(
                    openFileInput("yeare")));
            yeare = br.readLine();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void changeMonth(){
        if (monthe.equals("Январь"))
            tmonth = "Января";
        if (monthe.equals("Февраль"))
            tmonth = "Февраля";
        if (monthe.equals("Март"))
            tmonth = "Марта";
        if (monthe.equals("Апрель"))
            tmonth = "Апреля";
        if (monthe.equals("Май"))
            tmonth = "Мая";
        if (monthe.equals("Июнь"))
            tmonth = "Июня";
        if (monthe.equals("Июль"))
            tmonth = "Июля";
        if (monthe.equals("Август"))
            tmonth = "Августа";
        if (monthe.equals("Сегтябрь"))
            tmonth = "Сетября";
        if (monthe.equals("Октябрь"))
            tmonth = "Октября";
        if (monthe.equals("Ноябрь"))
            tmonth = "Ноября";
        if (monthe.equals("Декабрь"))
            tmonth = "Декабря";
    }

    void changeMonthnow(){
        if (monthe.equals("Январь"))
            monthno = "1";
        if (monthe.equals("Февраль"))
            monthno = "2";
        if (monthe.equals("Март"))
            monthno = "3";
        if (monthe.equals("Апрель"))
            monthno = "4";
        if (monthe.equals("Май"))
            monthno = "5";
        if (monthe.equals("Июнь"))
            monthno = "6";
        if (monthe.equals("Июль"))
            monthno = "7";
        if (monthe.equals("Август"))
            monthno = "8";
        if (monthe.equals("Сентябрь"))
            monthno = "9";
        if (monthe.equals("Октябрь"))
            monthno = "10";
        if (monthe.equals("Ноябрь"))
            monthno = "11";
        if (monthe.equals("Декабрь"))
            monthno = "12";
    }

    void ViewButton(){
        btnDeleteEve.setBackground(getResources().getDrawable(R.drawable.bordoviy));
        btnDeleteEve.setTextColor(getResources().getColor(R.color.White));
        btnDeleteEve.setOnClickListener(this);
    }

    void UnViewButton(){
        btnDeleteEve.setBackgroundColor(getResources().getColor(R.color.Milck));
        btnDeleteEve.setTextColor(getResources().getColor(R.color.Milck));
        btnDeleteEve.setOnClickListener(null);
    }
}