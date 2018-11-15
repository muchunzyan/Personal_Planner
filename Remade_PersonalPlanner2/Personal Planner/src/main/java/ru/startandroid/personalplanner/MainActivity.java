package ru.startandroid.personalplanner;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Point;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity implements OnClickListener {

    TextView btnCalender, btnMenu;
    TextView btnDelete;
    ScrollView scrollViewMain;
    ConstraintLayout ConstraintLayoutMain;
    LinearLayout linearLayMain;
    int e = 0;
    int count = 0;
    String id;
    String chas, minuta;

    DBHelper dbHelper;

    Calendar cal = Calendar.getInstance();
    int year = cal.get(Calendar.YEAR);
    int month = cal.get(Calendar.MONTH);
    int day = cal.get(Calendar.DAY_OF_MONTH);

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
        setContentView(R.layout.activity_main);

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int ScreenWidth = size.x;
        int ScreenHeight = size.y;

        btnCalender = findViewById(R.id.btnCalender);
        btnMenu = findViewById(R.id.btnMenu);
        scrollViewMain = findViewById(R.id.scrollViewMain);
        ConstraintLayoutMain = findViewById(R.id.ConstraintLayoutMain);
        linearLayMain = findViewById(R.id.linearLayMain);
        btnDelete = findViewById(R.id.btnDelete);

        ViewGroup.LayoutParams params = scrollViewMain.getLayoutParams();
        params.height = ScreenHeight - 400;
        scrollViewMain.setLayoutParams(params);

        btnCalender.setOnClickListener(this);
        btnMenu.setOnClickListener(this);

        dbHelper = new DBHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        String selection = null;
        String[] selectionArgs = null;

        selection = "day == ? AND month == ? AND year == ?";
        selectionArgs = new String[] { Integer.toString(day), Integer.toString(month + 1), Integer.toString(year) };

        Cursor c = db.query("events", null, selection, selectionArgs, null, null, "hourB");

        if (c.moveToFirst()) {
            final int idIndex = c.getColumnIndex("id");
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
                event.setBackground(getResources().getDrawable(R.drawable.events));
                event.setTag(c.getString(idIndex));
                event.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v){

                        if (count % 2 == 0) {
                            ViewButton();

                            id = event.getTag().toString();
                        }
                        if (count % 2 == 1){
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
                linearLayMain.addView(event);
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
            layoutParams.setMargins(70, 100, 70, 0);
            noevents.setPadding(40, 40,40,40);
            noevents.setLayoutParams(layoutParams );
            linearLayMain.addView(noevents);
        }

        TextView space = new TextView(this);

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, 50);
        space.setLayoutParams(layoutParams);
        linearLayMain.addView(space);

        c.close();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnCalender:
                Intent TestCalendar = new Intent(this, TestCalendar.class);
                startActivity(TestCalendar);
                break;
            case R.id.btnMenu:
                Intent ActivityMenu = new Intent(this, ActivityMenu.class);
                startActivity(ActivityMenu);
                break;
            case R.id.btnDelete:
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

    void ViewButton(){
        btnDelete.setBackground(getResources().getDrawable(R.drawable.bordoviy));
        btnDelete.setTextColor(getResources().getColor(R.color.White));
        btnDelete.setOnClickListener(this);
    }

    void UnViewButton(){
        btnDelete.setBackgroundColor(getResources().getColor(R.color.Milck));
        btnDelete.setTextColor(getResources().getColor(R.color.Milck));
        btnDelete.setOnClickListener(null);
    }
}