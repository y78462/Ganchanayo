package com.example.myapplication;

import android.content.ComponentName;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    EditText sjView;
    EditText mjView;
    EditText mglView;
    EditText smView;
    Button addBtn,hjBtn,hdBtn,kcalBtn, calendarBtn;
    String db_sj, db_mj, db_mgl, db_sm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sjView=findViewById(R.id.text_sj);
        mjView=findViewById(R.id.text_mj);
        mglView=findViewById(R.id.text_mgl);
        smView=findViewById(R.id.text_sm);
        addBtn=findViewById(R.id.add_btn);
        hjBtn=findViewById(R.id.hj_btn);
        hdBtn=findViewById(R.id.hd_btn);
        kcalBtn=findViewById(R.id.calculatebtn);
        calendarBtn=findViewById(R.id.calendar_btn);


        hdBtn.setOnClickListener(this);
        kcalBtn.setOnClickListener(this);
        addBtn.setOnClickListener(this);
        hjBtn.setOnClickListener(this);
        calendarBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {


        if(v==addBtn) {
            String sj = sjView.getText().toString();
            String mj = mjView.getText().toString();
            String mgl = mglView.getText().toString();
            String sm = smView.getText().toString();

            DBHelper helper = new DBHelper(this);
            SQLiteDatabase db = helper.getWritableDatabase();
            //데이터저장
            db.execSQL("insert into tb_memo (sj, mj, mgl, sm) values (?,?,?,?)",
                    new String[]{sj, mj, mgl, sm});

            //데이터 불러오기
            Cursor cursor = db.rawQuery("select sj,mj,mgl,sm from tb_memo order by _id desc limit 1", null);
            while (cursor.moveToNext()) {
                db_sj = cursor.getString(0);
                db_mj = cursor.getString(1);
                db_mgl = cursor.getString(2);
                db_sm = cursor.getString(3);
            }
            db.close();

            Toast.makeText(this, "술 섭취량이 저장되었습니다!\n" +
                    "소주 " + db_sj + "잔\n" +
                    "맥주 " + db_mj + "잔\n" +
                    "막걸리 " + db_mgl + "잔\n" +
                    "쏘맥 " + db_sm + "잔\n", Toast.LENGTH_SHORT).show();

//            Intent intent = new Intent(this, ReadDBActivity.class);
//            startActivity(intent);
        }

        if(v==kcalBtn) //칼로리버튼
        {

            Intent intent = new Intent();

            ComponentName componentName = new ComponentName("com.example.myapplication","com.example.myapplication.cal_kcal");
            intent.setComponent(componentName);
            startActivity(intent);
        }
        if(v==hdBtn)  //해독시간버튼
        {

            Intent intent = new Intent();

            ComponentName componentName = new ComponentName("com.example.myapplication","com.example.myapplication.hdTime");
            intent.setComponent(componentName);
            startActivity(intent);

        }

        if(v==hjBtn)  //해장지도버튼
        {

            Intent intent = new Intent();

            ComponentName componentName = new ComponentName("com.example.myapplication","com.example.myapplication.hjMap");
            intent.setComponent(componentName);
            startActivity(intent);


        }

        if(v==calendarBtn) //캘린더버튼
        {

            Intent intent = new Intent();

            ComponentName componentName = new ComponentName("com.example.myapplication","com.example.myapplication.calendarActivity");
            intent.setComponent(componentName);
            startActivity(intent);
        }


    }
}
