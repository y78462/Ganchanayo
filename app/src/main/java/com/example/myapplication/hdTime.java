package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


public class hdTime extends AppCompatActivity {

    EditText edit1;
    TextView textResult;

    String sj, mj, mgl, sm;
    Button calculation;
    Spinner spinner;
    String gender, weight;
    Double Rate, detox, detoxtime, detoxmin, Alcohol, sj_amount, mj_amount, mgl_amount, sm_amount;
    Integer a, b, c;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hd_time);

        edit1 = (EditText) findViewById(R.id.kg);
        textResult = (TextView) findViewById(R.id.result);
        calculation = (Button) findViewById(R.id.time);
        spinner = (Spinner) findViewById(R.id.gender);
        ArrayAdapter genderAdapter = ArrayAdapter.createFromResource(this, R.array.gender, android.R.layout.simple_spinner_item);
        genderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(genderAdapter);

        DBHelper helper = new DBHelper(hdTime.this);
        SQLiteDatabase db = helper.getWritableDatabase();
        Cursor cursor = db.rawQuery("select sj,mj,mgl,sm from tb_memo order by _id desc limit 1", null);

        while (cursor.moveToNext()) {
            sj = cursor.getString(0);
            mj = cursor.getString(1);
            mgl = cursor.getString(2);
            sm = cursor.getString(3);
        }
        db.close();

        if(sj == null || sj.trim().equals("")) {
            sj="0";
        } else { }
        if(mj == null || mj.trim().equals("")) {
            mj="0";
        } else { }
        if(mgl == null || mgl.trim().equals("")) {
            mgl="0";
        } else { }
        if(sm == null || sm.trim().equals("")) {
            sm="0";
        } else { }

        sj_amount = (Integer.parseInt(sj) * 40 * 0.19 * 0.7894 * 0.7);
        mj_amount = (Integer.parseInt(mj) * 500 * 0.05 * 0.7894 * 0.7);
        mgl_amount = (Integer.parseInt(mgl) * 750 * 0.06 * 0.7894 * 0.7);
        sm_amount = ((50 * 0.2 + 150 * 0.05) * (Integer.parseInt(sm)) * 0.7894 * 0.7);
        Alcohol = sj_amount + mj_amount + mgl_amount + sm_amount;


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                c=i;
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        calculation.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                gender = spinner.getSelectedItem().toString();
                weight = edit1.getText().toString();

                if (c == 0)
                    Rate = 0.7;
                else if (c==1)
                    Rate = 0.6;

                if(weight == null || weight.trim().equals("")) {
                    weight="0";
                } else { }

                detox = (Alcohol / (Rate * Integer.parseInt(weight))) / 10;
                detoxtime = (detox - 0.015) / 0.015;

                a = Integer.valueOf(detoxtime.intValue());
                detoxmin = (detoxtime - a) * 60;

                b = Integer.valueOf(detoxmin.intValue());

                textResult.setText("해독시간은 총   \n" +a.toString()+"시간" + b.toString() + "분      "+"\n  입니다ㅠㅡㅠ     ");
            }
        });
    }
}

