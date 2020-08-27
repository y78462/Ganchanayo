package com.example.myapplication;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.Date;

public class cal_kcal extends AppCompatActivity {

    String sj, mj, mgl, sm, string_result;
    int int_sj, int_mj, int_mgl, int_sm;
    double sjCal = 0, mjCal = 0, mglCal = 0, smCal = 0;
    double result = 0;

    double jogging = 0, climbing = 0, riding = 0, stair_climbing = 0;
    double jhour = 0, jmin = 0;
    double chour = 0, cmin = 0;
    double rhour = 0, rmin = 0;
    double shour = 0, smin = 0;
    String string_jogging, string_climbing, string_riding, string_stair_climbing;
    String drinkAmount ;
    String  Kcal;
    final String[] memo = {""};

    Button btn_register;
    String msg;


    Button btnYearMonthPicker;

    DatePickerDialog.OnDateSetListener d = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth){
            Log.d("YearMonthPickerTest", "year = " + year + ", month = " + monthOfYear + ", day = " + dayOfMonth);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_cal_kcal);

        TextView totalkcal = (TextView) findViewById(R.id.totalkcal);
        TextView string_jogging = (TextView) findViewById(R.id.running_text);
        TextView string_climbing = (TextView) findViewById(R.id.hiker_text);
        TextView string_riding = (TextView) findViewById(R.id.cycling_text);
        TextView string_stair_climbing = (TextView) findViewById(R.id.climbing_stairs_text);

        DBHelper helper = new DBHelper(this);
        SQLiteDatabase db = helper.getWritableDatabase();
        Cursor cursor = db.rawQuery("select sj,mj,mgl,sm from tb_memo order by _id desc limit 1", null);
        while (cursor.moveToNext()) {
            sj = cursor.getString(0);
            mj = cursor.getString(1);
            mgl = cursor.getString(2);
            sm = cursor.getString(3);
        }
        db.close();

        if (sj == null || sj.trim().equals("")) {
            sj = "0";
        } else {
        }
        if (mj == null || mj.trim().equals("")) {
            mj = "0";
        } else {
        }
        if (mgl == null || mgl.trim().equals("")) {
            mgl = "0";
        } else {
        }
        if (sm == null || sm.trim().equals("")) {
            sm = "0";
        } else {
        }

        int_sj = Integer.parseInt(sj);
        int_mj = Integer.parseInt(mj);
        int_mgl = Integer.parseInt(mgl);
        int_sm = Integer.parseInt(sm);

        sjCal = (double) 56 * int_sj;
        mjCal = (double) 140 * int_mj;
        mglCal = (double) 252 * int_mgl;
        smCal = (double) 93.8 * int_sm;

        result = sjCal + mjCal + mglCal + smCal;
        string_result = Double.toString(result);
        totalkcal.setText(string_result);



        btn_register = findViewById(R.id.btn_register);
        //btn click event
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                AlertDialog.Builder ad = new AlertDialog.Builder(cal_kcal.this);

                ad.setTitle("Memo");
                final EditText et_memo = new EditText(cal_kcal.this);
                ad.setView(et_memo);

                ad.setPositiveButton("저장", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) { //메모 저장 버튼 눌렀을 때 서버에 저장하는거 추가하기

                        String newmemo = et_memo.getText().toString();
                        memo[0] = newmemo;
                        Response.Listener<String> responseListener = new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    boolean success = jsonObject.getBoolean("success");
                                    if (success) {
                                        //성공한 경우
                                        Toast.makeText(getApplicationContext(), "다이어리 작성 성공!", Toast.LENGTH_SHORT).show();
                                    } else {
                                        //회원등록에 실패한 경우 그냥 토스트 띄우고 리턴함.
                                        Toast.makeText(getApplicationContext(), "다이어리 작성 실패!", Toast.LENGTH_SHORT).show();
                                        return;
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        };

                        //String date, String drinkAmount, String memo, String Kcal, Response.Listener<String> listener
                        //서버로 volley라이브러리를 이요해서 요청을 함.
                        //Toast.makeText(cal_kcal.this, ""+date+drinkAmount+memo+Kcal, Toast.LENGTH_SHORT).show();
                        RegisterRequest registerRequest = new RegisterRequest(msg, drinkAmount, memo[0], Kcal, responseListener);
                        RequestQueue queue = Volley.newRequestQueue(cal_kcal.this);
                        queue.add(registerRequest);
                        Toast.makeText(getApplicationContext(), "다이어리 작성 성공!", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                });

                ad.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();
                        Toast.makeText(getApplicationContext(), "다이어리 작성 실패!", Toast.LENGTH_SHORT).show();
                        return;
                    }
                });

                ad.show();
                //서버에 넣을 데이터 : 날짜, drinkAmount, kcal, memo
                 drinkAmount = "소주 "+sj+" 잔,"+"맥주 "+mj+" 잔, "+"막걸리 "+mgl+" 사발,"+" 소맥"+sm+" 잔";
                  Kcal = string_result+"Kcal";

                init(drinkAmount, memo[0],Kcal);
            }
        });

        //여기부터 운동
        jogging = result * 15;
        climbing = result * 7.14285714;
        riding = result * 7.14285714;
        stair_climbing = result * 8.10810811;

        jmin = jogging / 60;
        jhour = jmin / 60;
        jogging = jogging % 60;
        jmin = jmin % 60;

        cmin = climbing / 60;
        chour = cmin / 60;
        climbing = climbing % 60;
        cmin = cmin % 60;

        rmin = riding / 60;
        rhour = rmin / 60;
        riding = riding % 60;
        rmin = rmin % 60;

        smin = stair_climbing / 60;
        shour = smin / 60;
        stair_climbing = stair_climbing % 60;
        smin = smin % 60;

        string_jogging.setText((int) jhour + "시간 " + (int) jmin + "분 " + (int) jogging + " 초");
        string_climbing.setText((int) chour + "시간 " + (int) cmin + "분 " + (int) climbing + " 초");
        string_riding.setText((int) rhour + "시간 " + (int) rmin + "분 " + (int) riding + " 초");
        string_stair_climbing.setText((int) shour + "시간 " + (int) smin + "분 " + (int) stair_climbing + " 초");
    }


    private void init(final String drinkAmount, final String memo, final String Kcal) {

        String TAG = "cal_kcal";

        //Calendar를 이용하여 년, 월, 일, 시간, 분을 PICKER에 넣어준다.
        final Calendar cal = Calendar.getInstance();

        Log.e(TAG, cal.get(Calendar.YEAR) + "");
        Log.e(TAG, cal.get(Calendar.MONTH) + 1 + "");
        Log.e(TAG, cal.get(Calendar.DATE) + "");
        Log.e(TAG, cal.get(Calendar.HOUR_OF_DAY) + "");
        Log.e(TAG, cal.get(Calendar.MINUTE) + "");

        DatePickerDialog dialog = new DatePickerDialog(cal_kcal.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int date) {

                msg = String.format("%d-%d-%d", year, month + 1, date);
                //Toast.makeText(cal_kcal.this, msg, Toast.LENGTH_SHORT).show();
//                Response.Listener<String> responseListener = new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        try {
//                            JSONObject jsonObject = new JSONObject(response);
//                            boolean success = jsonObject.getBoolean("success");
//                            if (success) {
//                                //성공한 경우
//                                Toast.makeText(getApplicationContext(), "다이어리 작성 성공!", Toast.LENGTH_SHORT).show();
//                            } else {
//                                //회원등록에 실패한 경우 그냥 토스트 띄우고 리턴함.
//                                Toast.makeText(getApplicationContext(), "다이어리 작성 실패!", Toast.LENGTH_SHORT).show();
//                                return;
//                            }
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                };
//
//                //String date, String drinkAmount, String memo, String Kcal, Response.Listener<String> listener
//                //서버로 volley라이브러리를 이요해서 요청을 함.
//                //Toast.makeText(cal_kcal.this, ""+date+drinkAmount+memo+Kcal, Toast.LENGTH_SHORT).show();
//                RegisterRequest registerRequest = new RegisterRequest(msg, drinkAmount, memo, Kcal, responseListener);
//                RequestQueue queue = Volley.newRequestQueue(cal_kcal.this);
//                queue.add(registerRequest);

            }
        }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DATE));

        //dialog.getDatePicker().setMaxDate(new Date().getTime());    //입력한 날짜 이후로 클릭 안되게 옵션
        dialog.show();
    }
}