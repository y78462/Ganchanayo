package com.example.myapplication;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.app.AlertDialog;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.content.DialogInterface;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.CalendarMode;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.Executors;

public class calendarActivity extends AppCompatActivity {

    private final OneDayDecorator oneDayDecorator = new OneDayDecorator();
    Cursor cursor;
    MaterialCalendarView materialCalendarView;
    String selectedDate;
    ArrayList<String> request_result = new ArrayList<>(); //질이의 결과

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_calendar);

        materialCalendarView = (MaterialCalendarView)findViewById(R.id.calendarView);

        materialCalendarView.state().edit()
                .setFirstDayOfWeek(Calendar.SUNDAY)
                .setMinimumDate(CalendarDay.from(2017, 0, 1)) // 달력의 시작
                .setMaximumDate(CalendarDay.from(2030, 11, 31)) // 달력의 끝
                .setCalendarDisplayMode(CalendarMode.MONTHS)
                .commit();

        materialCalendarView.addDecorators(
                new Sundaydacorator(),
                new SaturdayDacorator(),
                oneDayDecorator);


       String[] result = request_result.toArray(new String[request_result.size()]);//질이의 결과
        new ApiSimulator(result).executeOnExecutor(Executors.newSingleThreadExecutor());

        materialCalendarView.setOnDateChangedListener(new OnDateSelectedListener() {

            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                int Year = date.getYear();
                int Month = date.getMonth() + 1;
                int Day = date.getDay();

                Log.i("Year test", Year + "");
                Log.i("Month test", Month + "");
                Log.i("Day test", Day + "");

                String shot_Day = Year + "-" + Month + "-" + Day;
                selectedDate = shot_Day;

                Log.i("shot_Day test", shot_Day + "");
               // materialCalendarView.clearSelection();
                //Toast.makeText(getApplicationContext(), shot_Day, Toast.LENGTH_SHORT).show();


                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            boolean success = jsonObject.getBoolean("success");
                            if(success)
                            {
                                //로그인에 성공한 경우
                                String dateKey = jsonObject.getString("date");
                                String drinkAmount = jsonObject.getString("drinkAmount");
                                String memo = jsonObject.getString("memo");
                                String Kcal = jsonObject.getString("Kcal");

                                TextView tv_Kcal=findViewById(R.id.tv_Kcal);
                                TextView tv_memo=findViewById(R.id.tv_memo);
                                TextView tv_drinkAmount=findViewById(R.id.tv_drinkAmount);
                                tv_drinkAmount.setText("술 섭취량 : "+drinkAmount);
                                tv_memo.setText("메모\n"+memo);
                                tv_Kcal.setText(Kcal+"Kcal");




                                //Toast.makeText(getApplicationContext()," 성공하였습니다.",Toast.LENGTH_SHORT).show();
                                //Toast.makeText(getApplicationContext(),dateKey+drinkAmount+memo+Kcal,Toast.LENGTH_SHORT).show();

                            }else
                            {
                                //실패한 경우
                                //Toast.makeText(getApplicationContext(),"내용 없음",Toast.LENGTH_SHORT).show();
                                TextView tv_Kcal=findViewById(R.id.tv_Kcal);
                                TextView tv_memo=findViewById(R.id.tv_memo);
                                TextView tv_drinkAmount=findViewById(R.id.tv_drinkAmount);
                                tv_drinkAmount.setText("");
                                tv_memo.setText("메모\n");
                                tv_Kcal.setText("");
                                return;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                };
                LoginRequest loginRequest= new LoginRequest(shot_Day,responseListener);
                RequestQueue queue = Volley.newRequestQueue(calendarActivity.this);
                queue.add(loginRequest);

                //textview에 네가지정보 보여주기
            }
        });

    }

    private class ApiSimulator extends AsyncTask<Void, Void, List<CalendarDay>> {

        String[] Time_Result;

        ApiSimulator(String[] Time_Result){
            this.Time_Result = Time_Result;
        }

        @Override
        protected List<CalendarDay> doInBackground(@NonNull Void... voids) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            Calendar calendar = Calendar.getInstance();
            ArrayList<CalendarDay> dates = new ArrayList<>();

            /*특정날짜 달력에 점표시해주는곳*/
            /*월은 0이 1월 년,일은 그대로*/
            //string 문자열인 Time_Result 을 받아와서 -를 기준으로짜르고 string을 int 로 변환
            for(int i = 0 ; i < Time_Result.length ; i ++){
                CalendarDay day = CalendarDay.from(calendar);
                String[] time = Time_Result[i].split("-");
                int year = Integer.parseInt(time[0]);
                int month = Integer.parseInt(time[1]);
                int dayy = Integer.parseInt(time[2]);

                dates.add(day);
                calendar.set(year,month-1,dayy);
            }
            return dates;
        }

        @Override
        protected void onPostExecute(@NonNull List<CalendarDay> calendarDays) {
            super.onPostExecute(calendarDays);

            if (isFinishing()) {
                return;
            }

            materialCalendarView.addDecorator(new EventDecorator(Color.GREEN, calendarDays,calendarActivity.this));
        }
    }
}
